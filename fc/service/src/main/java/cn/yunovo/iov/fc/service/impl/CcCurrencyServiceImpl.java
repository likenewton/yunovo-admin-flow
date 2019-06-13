package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.dao.ICcCurrencyMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCurrency;
import cn.yunovo.iov.fc.model.form.CurrencyForm;
import cn.yunovo.iov.fc.service.ICcCurrencyService;
import cn.yunovo.iov.fc.service.ICcSettingService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	@Override
	public int insert(CurrencyForm form, LoginInfo info) {
		
		CcCurrency ccCurrency = new CcCurrency();
		ccCurrency.setDate_modified(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		BeanUtils.copyProperties(form, ccCurrency);
		
		int result = this.baseMapper.insert(ccCurrency);
		
		//取消原有自动校正汇率逻辑
		return result;
	}

	@Override
	public int update(CurrencyForm form, LoginInfo loginBaseInfo) {
		
		CcCurrency ccCurrency = new CcCurrency();
		BeanUtils.copyProperties(form, ccCurrency);
		
		return iCcCurrencyMapper.updateInfo(ccCurrency);
	}

	@Override
	@Deprecated
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

}
