package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户管理表
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Data
@TableName("cc_user")
@ApiModel(value = "CcUser对象", description = "用户管理表")
public class CcUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户编号")
	@TableId(value = "user_id", type = IdType.AUTO)
	private Integer user_id;

	
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "用户组编号")
	private Integer group_id;

	@ApiModelProperty(value = "微信OPENID")
	private String open_id;

	@ApiModelProperty(value = "登录户名")
	private String username;

	@ApiModelProperty(value = "用户密码")
	private String password;

	@ApiModelProperty(value = "散列值")
	private String salt;

	@ApiModelProperty(value = "名字")
	private String firstname;

	@ApiModelProperty(value = "姓氏")
	private String lastname;

	@ApiModelProperty(value = "电子邮箱")
	private String email;

	@ApiModelProperty(value = "联系电话")
	private String tel;

	@ApiModelProperty(value = "找回密码代号")
	private String code;

	@ApiModelProperty(value = "登录语言")
	private String lang;

	@ApiModelProperty(value = "最近登录IP")
	private String ip;

	@ApiModelProperty(value = "IP登录地址")
	private String ipaddr;

	@ApiModelProperty(value = "用户状态")
	private Short status;

	@ApiModelProperty(value = "重置密码代号")
	private String reset_code;

	@ApiModelProperty(value = "登录出错数")
	private Short error_count;

	@ApiModelProperty(value = "创建时间")
	private String date_added;

	@ApiModelProperty(value = "最后登录时间")
	private String date_last;
	
	@ApiModelProperty(value = "机构串: *全部,多个用逗号分隔")
	private String orgpos;
	
	private String create_by;
	
	private String update_by;

}
