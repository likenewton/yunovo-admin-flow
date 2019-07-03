package cn.yunovo.iov.fc.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.sunshine.dcda.basecomponent.enums.StatusEnum;
import org.sunshine.dcda.system.service.model.SystemResourceQueryBean;
import org.sunshine.dcda.system.service.model.SystemResourceVo;
import org.sunshine.dcda.view.system.viewcomponent.ISystemResourceViewComponent;

import cn.yunovo.iov.fc.service.FcConstant;
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
			menus = systemResourceViewComponent.queryMenuResources(user_id, FcConstant.SITE_CODE);
		} catch (Exception e) {
			log.error("[menus][exception]params={user_id:{}},exception:{}",user_id, ExceptionUtils.getStackTrace(e));
		}
		return menus;
	}

	@Override
	public List<SystemResourceVo> buttons(String user_id, String super_resource_id) {

		/*SystemResourceQueryBean queryBean = new SystemResourceQueryBean();
		queryBean.setStatus(StatusEnum.USING.getKey());
	    queryBean.setSupperResId(super_resource_id);
	    queryBean.set*/
		List<SystemResourceVo> datas = new ArrayList<>();
		List<SystemResourceVo> buttons = null;
		try {
			buttons = systemResourceViewComponent.querySystemResourceByUserIdAndSiteCode(user_id, FcConstant.SITE_CODE);
			if(CollectionUtils.isEmpty(buttons)) {
				return null;
			}
			
			for (SystemResourceVo systemResourceVo : buttons) {
				if(StringUtils.equals(systemResourceVo.getSupperResId(), super_resource_id) && "2".equals(systemResourceVo.getRescType())) {
					datas.add(systemResourceVo);
				}
			}
		} catch (Exception e) {
			log.error("[menus][exception]params={user_id:{},super_resource_id:{}},exception:{}",user_id, ExceptionUtils.getStackTrace(e));
		}
		return datas;
	}

}
