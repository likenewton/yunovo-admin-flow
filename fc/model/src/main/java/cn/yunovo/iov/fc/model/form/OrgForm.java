package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrgForm  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("机构id(修改时该参数被传)")
	private Integer org_id;

	@ApiModelProperty("机构名称")
	private String name; 
	
	@ApiModelProperty("所属父机构id")
	private Integer parent_id;
	
	@ApiModelProperty("可开账户数量")
	private Integer user_total;
	
	@ApiModelProperty("异步通知地址")
	private String notify_url;
	
	@ApiModelProperty("机构描述")
	private String memo; 
	
	@ApiModelProperty("负责人邮箱")
	private String email;
	
	@ApiModelProperty("负责人手机")
	private String tel;
	
	@ApiModelProperty("返利比率")
	private Float rebate_value;
	
	
}
