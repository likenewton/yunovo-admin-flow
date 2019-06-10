package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.entity.CcUser;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户管理表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcUserService extends IService<CcUser>  {

	public CcUser findUserOrgAndOrgpos(String username);
	
	public String getOrgpos(String loginname);
	
	public Map<String, String> userMap();
	
	public Map<Integer, String> userIdMap();
	
}
