package cn.yunovo.iov.fc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class UserResourceInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ResourcesBean> user_resource;
	
	private Set<String> user_resource_url_set;
	
	private Map<String, String> need_filter_resource_map;
	
	
}
