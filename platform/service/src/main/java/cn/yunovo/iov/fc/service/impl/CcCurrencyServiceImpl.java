package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcCurrencyMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcCurrency;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.model.form.CurrencyForm;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcCurrencyService;
import cn.yunovo.iov.fc.service.ICcSettingService;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 货币表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-12
 */
@Service
public class CcCurrencyServiceImpl extends ServiceImpl<ICcCurrencyMapper, CcCurrency> implements ICcCurrencyService {

	@Autowired
	private ICcCurrencyMapper iCcCurrencyMapper;
	
	@Autowired
	public ICcSettingService iCcSettingService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Override
	public PageData<CcCurrency, Object> getItemsPage(PageForm form, LoginInfo info) {
		
		Page<CcCurrency> page = form.build(CcCurrency.class, null, null);
		
		List<CcCurrency> records = iCcCurrencyMapper.getItemsPage(page);
		if(!CollectionUtils.isEmpty(records)) {
			Map<String, String> systemConfigMap = iCcSettingService.systemConfigMap();
			
			for (CcCurrency ccCurrency : records) {
				
				ccCurrency.setIs_default(StringUtils.equals(ccCurrency.getCode(), systemConfigMap.get("config_currency")));
			}
		}
		page.setRecords(records);
		PageData<CcCurrency, Object> data = new PageData<>();
		data.setPage(page);
		
		return data;
	}

	@Override
	public CcCurrency detail(Integer currency_id, LoginInfo loginBaseInfo) {
		
		if(currency_id == null) {
			return null;
		}
		
		CcCurrency ccCurrency = this.getById(currency_id);
		return ccCurrency;
	}
	
	public CcCurrency getByCode(String code) {
		
		if(StringUtils.isEmpty(code)) {
			return null;
		}
	
		QueryWrapper<CcCurrency> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("code", code);
		
		CcCurrency ccCurrency = this.getOne(queryWrapper, false);
		
		return ccCurrency;
		
	}
	
	@Override
	public int insert(CurrencyForm form, LoginInfo info) {
		
		CcCurrency ccCurrency = new CcCurrency();
		ccCurrency.setDate_modified(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		BeanUtils.copyProperties(form, ccCurrency);
		
		if(this.getByCode(form.getCode()) != null) {
			throw new FormValidateException("货币代码不能重复新增", "code", form.buildJsonString());
		}
		
		int result = this.baseMapper.insert(ccCurrency);
		
		//取消原有自动校正汇率逻辑
		return result;
	}

	@Override
	public int update(CurrencyForm form, LoginInfo loginBaseInfo) {
		
		CcCurrency ccCurrency = new CcCurrency();
		BeanUtils.copyProperties(form, ccCurrency);
		CcCurrency d = this.getByCode(form.getCode());
		if(d != null && d.getCurrency_id() - form.getCurrency_id() != 0) {
			throw new FormValidateException("货币代码已存在", "code", form.buildJsonString());
		}
		
		return iCcCurrencyMapper.updateInfo(ccCurrency);
	}

	@Override
	public int delete(CurrencyForm form, LoginInfo info) {
		
		CcCurrency currency = this.detail(form.getCurrency_id(), info);
		if(currency != null) {
			
			Map<String, String> systemConfigMap = iCcSettingService.systemConfigMap();
			if(StringUtils.equals(currency.getCode(), systemConfigMap.get("config_currency"))) {
				throw new BusinessException(-1, "系统提示： 该货币不能被删除，因为它是系统的默认货币！");
			}
			
			
		}
		return 0;
	}
	
	public List<CcCurrency> getCurrencies(){
		
		return iCcCurrencyMapper.getItemsPage(null);
	}
	
	public JSONObject currenciesMap(Boolean noCache) {
		
		noCache = noCache == null ? false : noCache;
		List<CcCurrency> temp1= null;
		
		String cache = null, cacheKey = FcConstant.memResKey("currency");
		JSONObject result = null;
		if(!noCache) {
			cache = jedisPoolUtil.get(cacheKey);
		}
		if(StringUtils.isEmpty(cache)) {
			result = new JSONObject();
			temp1 = this.getCurrencies();
			if(CollectionUtils.isEmpty(temp1)) {
				return result;
			}
			
			for (CcCurrency ccCurrency : temp1) {
				
				result.put(ccCurrency.getCode(), ccCurrency);
			}
			
			jedisPoolUtil.setEx(cacheKey, JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
		}else {
			result = JSONObject.parseObject(cache);
		}
		
		return result;
	}
	
	@Override
	public List<SelectBean> select(){
		
		JSONObject data = this.currenciesMap(null);
		if(CollectionUtils.isEmpty(data)) {
			return null;
		}
		
		Set<Entry<String, Object>> entrySet = data.entrySet();
		Iterator<Entry<String, Object>> it = entrySet.iterator();
		Entry<String, Object> temp = null;
		List<SelectBean> select = new ArrayList<>();
		while(it.hasNext()) {
			temp = it.next();
			select.add(new SelectBean(data.getJSONObject(temp.getKey()).getString("title"), temp.getKey()));
		}
		return select;
	}

}
