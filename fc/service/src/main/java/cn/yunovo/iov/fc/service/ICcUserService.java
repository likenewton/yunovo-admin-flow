package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.model.form.UserForm;
import cn.yunovo.iov.fc.model.result.UserResultBean;

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

	PageData<UserResultBean, Object> userListPage(PageForm pageForm, String username, String firstname, Integer org_id,
			LoginInfo info);

	/**
	 * 用户启用停用接口
	 * @param userForm
	 * @param user
	 * @return
	 */
	boolean onoff(UserForm userForm, LoginInfo user);

	/**
	 * 用户信息修改
	 * @param userForm
	 * @param user
	 * @return
	 */
	boolean edit(UserForm userForm, LoginInfo user);

	/**
	 * 删除用户
	 * @param userForm
	 * @param user
	 * @return
	 */
	boolean del(UserForm userForm, LoginInfo user);

	public void userRegister(String username, String firstname);
	
	
	
}
