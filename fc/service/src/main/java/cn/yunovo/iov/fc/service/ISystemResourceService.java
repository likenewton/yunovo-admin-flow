package cn.yunovo.iov.fc.service;

import java.util.List;

import org.sunshine.dcda.system.service.model.SystemResourceVo;

import cn.yunovo.iov.cas.client.IResourceService;

public interface ISystemResourceService extends IResourceService{

	/**
	 * 获取当前用户拥有的菜单列表
	 * @param user_id 用户id
	 * @return 树形菜单集合
	 */
	public  List<SystemResourceVo> menus(Integer user_id);
	
	/**
	 * 获取当前用户对应的功能
	 * @param login_name
	 * @param super_resource_id
	 * @return
	 */
	public List<SystemResourceVo> buttons(Integer user_id, String super_resource_id);
	
}
