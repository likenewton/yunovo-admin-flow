package cn.yunovo.iov.fc.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.sunshine.dcda.system.service.model.SystemResourceVo;
import org.sunshine.dcda.view.system.viewcomponent.ISystemResourceViewComponent;

import cn.yunovo.iov.fc.service.ISystemResourceService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemResourceServiceImpl implements ISystemResourceService{

	@Resource
	private ISystemResourceViewComponent systemResourceViewComponent;
	
	@Override
	public List<SystemResourceVo> menus(String user_id) {
		
		if(user_id == null) {
			return null;
		}
		List<SystemResourceVo>  menus = null;
		try {
			menus = systemResourceViewComponent.queryMenuResources(user_id, "FLOW_CLOUD_PLATFORM");
		} catch (Exception e) {
			
			log.error("[menus][exception]params={user_id:{}}");
		}
		return null;
	}

	@Override
	public List<SystemResourceVo> buttons(String user_id, String super_resource_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
