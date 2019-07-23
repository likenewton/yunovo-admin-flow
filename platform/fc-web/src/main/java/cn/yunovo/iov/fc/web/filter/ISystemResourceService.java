package cn.yunovo.iov.fc.web.filter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.yunovo.iov.fc.model.ResourcesBean;
import cn.yunovo.iov.fc.model.UserResourceInfoBean;

public interface ISystemResourceService{

	List<ResourcesBean> listToTreeMenu(List<ResourcesBean> data);

	List<ResourcesBean> allResourceBySiteCode(String site_code);

	List<ResourcesBean> allResourceBySiteCodeAndUserid(String user_id, String site_code);

	Map<String, String> getNeedFilterResource(List<ResourcesBean> data);

	Set<String> getUserResUrl(List<ResourcesBean> data);

	Map<String, Map<String, Boolean>> buttonGroup(List<ResourcesBean> data);

	UserResourceInfoBean getUserResourceInfo(String token, String user_id);

	boolean isPermission(String requestURI, String token, String user_id);

	void destory(String token);

	
}
