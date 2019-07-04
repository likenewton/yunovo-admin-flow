package cn.yunovo.iov.fc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.sunshine.dcda.basecomponent.enums.StatusEnum;
import org.sunshine.dcda.system.service.model.SystemResourceQueryBean;
import org.sunshine.dcda.system.service.model.SystemResourceVo;
import org.sunshine.dcda.view.system.viewcomponent.ISystemResourceViewComponent;

import cn.yunovo.iov.fc.model.ResourcesBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ISystemResourceService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemResourceServiceImpl implements ISystemResourceService{

	@Resource
	private ISystemResourceViewComponent systemResourceViewComponent;
	
	private List<ResourcesBean> converToResourceBean(List<SystemResourceVo> resources) {
		
		List<ResourcesBean> result = new ArrayList<>(resources.size());
		ResourcesBean temp = null;
		for (SystemResourceVo bean : resources) {
			temp = new ResourcesBean();
			BeanUtils.copyProperties(bean, temp, "childResources");
			result.add(temp);
		}
		
		return result;
	}



	@Override
	public List<ResourcesBean> listToTreeMenu(List<ResourcesBean> data){
		
		List<ResourcesBean> tree = new ArrayList<>();
		Map<String, ResourcesBean> hash = data.stream().collect(Collectors.toMap(ResourcesBean::getId, Function.identity()));
		
		ResourcesBean parent = null;
		for (ResourcesBean bean : data) {
			
			if(!StringUtils.equals(bean.getRescType(),"1")) {
				continue;
			}
			parent = hash.get(bean.getSupperResId());
			if(parent != null) {
				
				if(parent.getChildResources() == null) {
					parent.setChildResources(new ArrayList<>());
				}
				parent.getChildResources().add(bean);
			}else {
				if(bean.getLevel() == 2) {
					tree.add(bean);
				}
			}
		}
		
		return tree;
	}

	@Override
	public List<ResourcesBean> allResourceBySiteCode(String site_code) {
		
		SystemResourceQueryBean queryBean = new SystemResourceQueryBean();
		queryBean.setSiteCode(FcConstant.SITE_CODE);
		queryBean.setStatus(StatusEnum.USING.getKey());
		queryBean.setPageNo(1);
		queryBean.setPageSize(999999);
		
		List<SystemResourceVo> resourceList = null;
		try {
			resourceList = systemResourceViewComponent.querySystemResourceList(queryBean);
		} catch (Exception e) {
			log.error("[allResourceBySiteCode][exception]params={site_code:{}},exception:{}",site_code, ExceptionUtils.getStackTrace(e));
		}
		
		if(CollectionUtils.isEmpty(resourceList)) {
			return Collections.emptyList();
		}
		
		List<ResourcesBean> data = this.converToResourceBean(resourceList);
		return data;
	}
	
	@Override
	public List<ResourcesBean> allResourceBySiteCodeAndUserid(String user_id, String site_code) {
		
		List<SystemResourceVo> resourceList = null;
		try {
			resourceList = systemResourceViewComponent.querySystemResourceByUserIdAndSiteCode(user_id, site_code);
		} catch (Exception e) {
			log.error("[allResourceBySiteCodeAndUserid][exception]params={site_code:{}},exception:{}",site_code, ExceptionUtils.getStackTrace(e));
		}
		
		if(CollectionUtils.isEmpty(resourceList)) {
			return Collections.emptyList();
		}
		
		List<ResourcesBean> data = this.converToResourceBean(resourceList);
		return data;
	}
	
	@Override
	public Map<String, String> getNeedFilterResource(List<ResourcesBean> data){
		
		Map<String, String> resource = new HashMap<>();
		for (ResourcesBean resourcesBean : data) {
			if(StringUtils.isEmpty(resourcesBean.getResUrl()) || "#".equals(resourcesBean.getResUrl())) {
				continue;
			}
			resource.put(resourcesBean.getResUrl(), resourcesBean.getResName());
		}
		
		return resource;
	}
	
	@Override
	public Set<String> getUserResUrl(List<ResourcesBean> data){
		
		Set<String> resUrl = new HashSet<>();
		for (ResourcesBean resourcesBean : data) {
			if(StringUtils.isEmpty(resourcesBean.getResUrl()) || "#".equals(resourcesBean.getResUrl())) {
				continue;
			}
			resUrl.add(resourcesBean.getResUrl());
		}
		
		return resUrl;
	}
	
	@Override
	public Map<String, List<ResourcesBean>> buttonGroup(List<ResourcesBean> data) {
		
		Stream<ResourcesBean> filter = data.stream().filter(bean -> "2".equals(bean.getRescType()));
		
		Map<String, String> hash = data.stream().collect(Collectors.toMap(ResourcesBean::getId, ResourcesBean::getRescCode));
		
		Map<String, List<ResourcesBean>> group =  filter.collect(Collectors.groupingBy(ResourcesBean::getSupperResId));
		if(group == null || group.isEmpty()) {
			return null;
		}
		
		Map<String, List<ResourcesBean>> resultData = new HashMap<>();
		
		Set<String> keys = group.keySet();
		
		keys.forEach((key) -> {
			
			if(hash.containsKey(key)) {
				
				resultData.put(hash.get(key), group.get(key));
			}
		});
		
		return resultData;
	}

}
