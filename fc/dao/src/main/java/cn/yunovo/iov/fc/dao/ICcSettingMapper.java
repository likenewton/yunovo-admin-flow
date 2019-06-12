package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.yunovo.iov.fc.model.entity.CcSetting;

/**
 * <p>
 * 设置表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
public interface ICcSettingMapper extends BaseMapper<CcSetting> {
	
	public Integer update(@Param("group")String group, @Param("key")String key, @Param("value")String value);
	
	public Integer insert(@Param("group")String group, @Param("key")String key, @Param("value")String value);
	
	public List<CcSetting> queryList(@Param("group")String group, @Param("key")String key);
	
	public Integer deleteByGroup(@Param("group")String group);

}
