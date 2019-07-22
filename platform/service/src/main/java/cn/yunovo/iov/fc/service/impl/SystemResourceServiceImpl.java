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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.sunshine.dcda.basecomponent.enums.StatusEnum;
import org.sunshine.dcda.system.service.model.SystemResourceQueryBean;
import org.sunshine.dcda.system.service.model.SystemResourceVo;
import org.sunshine.dcda.view.system.viewcomponent.ISystemResourceViewComponent;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.cas.client.session.ClientSessionMappingStorage;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.model.ResourcesBean;
import cn.yunovo.iov.fc.model.UserResourceInfoBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ISystemResourceService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemResourceServiceImpl implements ISystemResourceService{

	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Resource
	private ISystemResourceViewComponent systemResourceViewComponent;
	
	@Override
	public UserResourceInfoBean getUserResourceInfo(String token, String user_id) {
		
		String cacheKey = FcConstant.SITE_CODE+"#UserResource#"+token;
		UserResourceInfoBean bean = jedisPoolUtil.get(cacheKey, UserResourceInfoBean.class);
		if(bean == null) {
			List<ResourcesBean>  all_resource = this.allResourceBySiteCode(FcConstant.SITE_CODE);
			
			if(CollectionUtils.isEmpty(all_resource)) {
				return null;
			}
			
			List<ResourcesBean>  user_resource = this.allResourceBySiteCodeAndUserid(user_id, FcConstant.SITE_CODE);
			if(CollectionUtils.isEmpty(user_resource)) {
				return null;
			}
			
			Map<String, String> needFilter = this.getNeedFilterResource(all_resource);
			Set<String> userRes = this.getUserResUrl(user_resource);
			
			
			bean = new UserResourceInfoBean();
			bean.setNeed_filter_resource_map(needFilter);
			bean.setUser_resource_url_set(userRes);
			bean.setUser_resource(user_resource);
			
			jedisPoolUtil.setEx(cacheKey, bean);
		}
		
		return bean;
		
	}
	
	
	
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
		
		
		if(CollectionUtils.isEmpty(data)) {
			return null;
		}
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
	public Map<String, Map<String, Boolean>> buttonGroup(List<ResourcesBean> data) {
		
		Stream<ResourcesBean> filter = data.stream().filter(bean -> "2".equals(bean.getRescType()));
		Map<String, String> hash = data.stream().collect(Collectors.toMap(ResourcesBean::getId, ResourcesBean::getRescCode));
		Map<String, List<ResourcesBean>> group =  filter.collect(Collectors.groupingBy(ResourcesBean::getSupperResId));
		if(group == null || group.isEmpty()) {
			return null;
		}
		
		Map<String, Map<String, Boolean>> resultData = new HashMap<>();
		Set<String> keys = group.keySet();
		
		keys.forEach((key) -> {
			if(hash.containsKey(key)) {
				
				List<ResourcesBean> temp = group.get(key);
				
				Map<String, Boolean> temp2 = temp.stream().collect(Collectors.toMap(ResourcesBean::getRescCode, new Function<ResourcesBean, Boolean>(){

					@Override
					public Boolean apply(ResourcesBean t) {
						return true;
					}
					
				}));
				resultData.put(hash.get(key), temp2);
			}
		});
		
		return resultData;
	}



	@Override
	public boolean isPermission(String requestURI, String token, String user_id) {
		
		UserResourceInfoBean resource = this.getUserResourceInfo(token, user_id);
		
		if(resource == null) {
			log.warn("[isPermission][未授权]params={requestURI:{},token:{},user_id:{},resource:{}}", requestURI, token, user_id, null);
			return false;
		}
		
		Map<String, String>  need_filter_resource_map = resource.getNeed_filter_resource_map();
		Set<String> user_resource_url_set = resource.getUser_resource_url_set();
		
		if(CollectionUtils.isEmpty(user_resource_url_set) || CollectionUtils.isEmpty(need_filter_resource_map)) {
			log.warn("[isPermission][未授权]params={requestURI:{},token:{},user_id:{}}", requestURI, token, user_id);
			return false;
		}
		
		if(!need_filter_resource_map.containsKey(requestURI)) {
			//log.debug("[isPermission][接口无需权限验证]params={requestURI:{},token:{},user_id:{},resource:{}}", requestURI, token, user_id, resource.buildJsonString());
			return true;
		}
		
		boolean isPermission = user_resource_url_set.contains(requestURI);
		
		if(!isPermission) {
			
			log.warn("[isPermission][未授权]params={requestURI:{},token:{},user_id:{},resource:{}}", requestURI, token, user_id);
		}
		
		return isPermission;
		
	}



	@Override
	public void destory(String token) {
		
		String cacheKey = FcConstant.SITE_CODE+"#UserResource#"+token;
		jedisPoolUtil.del(cacheKey);
		
	}

}
