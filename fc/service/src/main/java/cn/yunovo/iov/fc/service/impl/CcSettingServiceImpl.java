package cn.yunovo.iov.fc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.Md5Util;
import cn.yunovo.iov.fc.dao.ICcSettingMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcSetting;
import cn.yunovo.iov.fc.model.form.PayForm;
import cn.yunovo.iov.fc.model.form.SystemParamsForm;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcSettingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 设置表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
@Service
public class CcSettingServiceImpl extends ServiceImpl<ICcSettingMapper, CcSetting> implements ICcSettingService {

	@Autowired
	private ICcSettingMapper iCcSettingMapper;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	public List<CcSetting> getSettings(String group, String key, Integer store_id) {
		
		return iCcSettingMapper.queryList(group, key, store_id);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED, transactionManager="clwTransactionManager")
	public int editSetting(String group, List<CcSetting> settings) {
		
		if(group == null) {
			systemSettings(true);
			throw new BusinessException("无效的分组");
		}
		
		if(CollectionUtils.isEmpty(settings)) {
			systemSettings(true);
			return 0;
		}
		List<CcSetting> temp = null;
		CcSetting temp1 = null;
		for (CcSetting ccSetting : settings) {
			
			if(StringUtils.isEmpty(ccSetting.getKey())) {
				continue;
			}
			temp = this.getSettings(group, ccSetting.getKey(), null);
			if(CollectionUtils.isEmpty(temp)) {
				temp1 = new CcSetting();
				temp1.setGroup(group);
				temp1.setKey(ccSetting.getKey());
				temp1.setValue(StringUtils.defaultIfEmpty(ccSetting.getValue(), ""));
				
				this.save(temp1);
			}else {
				iCcSettingMapper.update(group, ccSetting.getKey(), StringUtils.defaultIfEmpty(ccSetting.getValue(), ""));
			}
		}
		systemSettings(true);
		return 1;
	}

	@Override
	public List<CcSetting> systemSettings(Boolean noCache){
		
		noCache = noCache == null ? false : noCache;
		
		String cacheKey = "SELECT * FROM cc_setting WHERE store_id = 0";
		cacheKey = FcConstant.CACHE_SQL_PREFIX+Md5Util.getMD5String(cacheKey);
		String cache = null;
		if(!noCache) {
			cache = jedisPoolUtil.get(cacheKey);
		}
		
		List<CcSetting> settings = null;
		if(StringUtils.isEmpty(cache)) {
			
			settings = this.getSettings(null, null, 0);
			if(!CollectionUtils.isEmpty(settings)) {
				jedisPoolUtil.setEx(cacheKey, JSONArray.toJSONString(settings, SerializerFeature.WriteMapNullValue));
			}
		}else {
			settings = JSONArray.parseArray(cache, CcSetting.class);
		}
		
		return settings;
	}
	
	@Override
	public Map<String, String> systemConfigMap() {
		
		List<CcSetting> settings = this.systemSettings(null);
		
		Map<String, String> map = new HashMap<>();
		for (CcSetting ccSetting : settings) {
			map.put(ccSetting.getKey(), ccSetting.getValue());
		}
		
		return map;
	}
	
	@Override
	public SystemParamsForm systemParams() {
		
		List<CcSetting> settings = this.getSettings("config", null, null);
		
		if(CollectionUtils.isEmpty(settings)) {
			return null;
		}
		
		JSONObject map = new JSONObject(settings.size());
		for (CcSetting ccSetting : settings) {
			map.put(ccSetting.getKey(), ccSetting.getValue());
		}
		
		SystemParamsForm data = map.toJavaObject(SystemParamsForm.class);
		
		return data;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED, transactionManager="clwTransactionManager")
	public int updateSystemParams(PayForm form, LoginInfo loginBaseInfo) {
		
		List<CcSetting> data = form.build();
		if(CollectionUtils.isEmpty(data)) {
			return 0;
		}
		
		return this.editSetting(data.get(0).getGroup(), data);
	}

}
