package cn.yunovo.iov.fc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.entity.CcSetting;

/**
 * <p>
 * 设置表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
public interface ICcSettingService extends IService<CcSetting> {

	public int editSetting(String group, List<CcSetting> settings);
	
}
