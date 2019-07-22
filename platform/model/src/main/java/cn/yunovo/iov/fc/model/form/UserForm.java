package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class UserForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(groups= {InsertGroupValidate.class}, message="请选择用户所属机构")
	@ApiModelProperty(value = "所属机构")
	private Integer org_id;

	@NotEmpty
	@NotNull(groups= {InsertGroupValidate.class,UpdateGroupValidate.class,DeleteGroupValidate.class}, message="请选择对应的用户")
	@ApiModelProperty(value = "登录户名")
	private String username;

	@NotNull
	@NotNull(groups= {UpdateGroupValidate.class,InsertGroupValidate.class}, message="请选择正确的用户状态")
	@Range(max=1, min=0, groups= {UpdateGroupValidate.class,InsertGroupValidate.class}, message="请选择正确的用户状态")
	@ApiModelProperty(value = "用户状态")
	private Short status;

	@Length(min=0, max=200, groups= {InsertGroupValidate.class}, message="您选择的机构列表已超过限制")
	@ApiModelProperty(value = "其他可控机构")
	private String orgpos;

}
