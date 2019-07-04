package cn.yunovo.iov.fc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.yunovo.iov.fc.model.ResourcesBean;

public interface ISystemResourceService{

	List<ResourcesBean> listToTreeMenu(List<ResourcesBean> data);

	List<ResourcesBean> allResourceBySiteCode(String site_code);

	List<ResourcesBean> allResourceBySiteCodeAndUserid(String user_id, String site_code);

	Map<String, String> getNeedFilterResource(List<ResourcesBean> data);

	Set<String> getUserResUrl(List<ResourcesBean> data);

	Map<String, List<ResourcesBean>> buttonGroup(List<ResourcesBean> data);

	
}
