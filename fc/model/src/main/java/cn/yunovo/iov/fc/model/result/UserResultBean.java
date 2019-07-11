package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "UserResultBean对象", description = "用户管理-用户列表")
public class UserResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户编号")
	@TableId(value = "user_id", type = IdType.AUTO)
	private Integer user_id;

	
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;
	
	private String org_name;

	@ApiModelProperty(value = "登录户名")
	private String username;

	@ApiModelProperty(value = "名字")
	private String firstname;

	@ApiModelProperty(value = "用户状态")
	private Short status;

	@ApiModelProperty(value = "创建时间")
	private String date_added;

	@ApiModelProperty(value = "最后登录时间")
	private String date_last;
	
	@ApiModelProperty(value = "机构串: *全部,多个用逗号分隔")
	private String orgpos;
	
	private String orgpos_name;
	
	private String create_by;
	
	private String update_by;
	
}
