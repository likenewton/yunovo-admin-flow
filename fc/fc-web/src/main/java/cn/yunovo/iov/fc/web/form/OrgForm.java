package cn.yunovo.iov.fc.web.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class OrgForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name; 
	
	private Integer parent_id;
	
	private Integer user_total;
	
	private String notify_url;
	
	private String memo; 
	
	private String email;
	
	private String tel;
	
	private Double rebate_value;
}
