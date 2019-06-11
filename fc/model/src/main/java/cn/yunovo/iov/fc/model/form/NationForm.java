package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class NationForm extends BaseForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="系统提示： 请选择您要修改的区域！", groups= {UpdateGroupValidate.class})
	@ApiModelProperty(value = "自动编号")
	private Integer ntid;

	@ApiModelProperty(value = "父结点")
	private Integer parent = 0;

	@NotEmpty(message="系统提示： 区域名称不能为空且必须在2至128个字符之间！", groups= {InsertGroupValidate.class, UpdateGroupValidate.class})
	@Size(message="系统提示： 区域名称不能为空且必须在2至128个字符之间！", groups= {InsertGroupValidate.class, UpdateGroupValidate.class})
	@ApiModelProperty(value = "省份")
	private String ntname;

	@NotEmpty(message="系统提示： 邮政编码不能为空且必须在11个字符以下！", groups= {InsertGroupValidate.class, UpdateGroupValidate.class})
	@Size(max = 10, message="系统提示： 邮政编码不能为空且必须在11个字符以下！", groups= {InsertGroupValidate.class, UpdateGroupValidate.class})
	@ApiModelProperty(value = "邮政编码")
	private Integer zipcode;
	
	@ApiModelProperty(value = "需要被删除的ntid")
	@NotEmpty(message="系统提示：请选择您要删除的区域信息", groups=DeleteGroupValidate.class)
	private Integer[] ntids;

}
