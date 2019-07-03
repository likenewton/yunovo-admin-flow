package cn.yunovo.iov.fc.service;

import java.util.List;

import org.sunshine.dcda.system.service.model.SystemResourceVo;

public interface ISystemResourceService{

	/**
	 * 获取当前用户拥有的菜单列表
	 * @param user_id 用户id
	 * @return 树形菜单集合
	 */
	public  List<SystemResourceVo> menus(String user_id);
	
	/**
	 * 获取当前用户对应的功能
	 * @param user_id 用户id
	 * @param super_resource_id 父资源id
	 * @return 父资源id下所有子资源
	 */
	public List<SystemResourceVo> buttons(String user_id, String super_resource_id);
	
	
	
}
