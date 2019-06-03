package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcUser;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户管理表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcUserMapper extends BaseMapper<CcUser> {

	public CcUser getUserInfoByUsernameAndStatus(@Param("username")String username);
	
}
