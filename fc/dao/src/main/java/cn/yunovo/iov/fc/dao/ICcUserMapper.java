package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.model.result.UserResultBean;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

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
	
	public List<UserResultBean> userListPage(IPage<UserResultBean> page, @Param("username")String username, @Param("firstname")String firstname, @Param("org_id")Integer org_id, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public int updateByUsername(@Param("u")CcUser user);
	
	public Integer getTotalByOrg(@Param("org_id")Integer org_id);
	
	public UserResultBean userDetailInfo(@Param("username")String username);
	
}
