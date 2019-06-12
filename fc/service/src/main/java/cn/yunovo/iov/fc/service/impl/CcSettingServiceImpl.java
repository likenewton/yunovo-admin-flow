package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.dao.ICcSettingMapper;
import cn.yunovo.iov.fc.model.entity.CcSetting;
import cn.yunovo.iov.fc.service.ICcSettingService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
	
	public List<CcSetting> getSettings(String group, String key) {
		
		QueryWrapper<CcSetting> queryWrapper = new QueryWrapper<>();
		
		if(StringUtils.isNotEmpty(group)) {
			queryWrapper.eq("group", group);
		}
		
		if(StringUtils.isNotEmpty(key)) {
			queryWrapper.eq("key", key);
		}
		
		return this.getBaseMapper().selectList(queryWrapper);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public int editSetting(String group, List<CcSetting> settings) {
		
		if(group == null) {
			throw new BusinessException("无效的分组");
		}
		
		if(CollectionUtils.isEmpty(settings)) {
			return 0;
		}
		List<CcSetting> temp = null;
		CcSetting temp1 = null;
		for (CcSetting ccSetting : settings) {
			
			if(StringUtils.isEmpty(ccSetting.getKey())) {
				continue;
			}
			temp = this.getSettings(group, ccSetting.getKey());
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
		
		return 1;
	}

}
