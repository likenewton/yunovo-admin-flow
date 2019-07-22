package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户分类表
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
@TableName("cc_user_group")
@ApiModel(value = "CcUserGroup对象", description = "用户分类表")
public class CcUserGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "权限组编号")
	@TableId(value = "group_id", type = IdType.AUTO)
	private Integer group_id;

	@ApiModelProperty(value = "创建者编号")
	private Integer user_id;

	@ApiModelProperty(value = "更改者编号")
	private Integer alter_id;

	@ApiModelProperty(value = "权限组名称")
	private String name;

	@ApiModelProperty(value = "机构串: *全部多个用逗号分隔")
	private String orgpos;

	@ApiModelProperty(value = "权限组权限")
	private String permission;

	@ApiModelProperty(value = "权限组相关权限描述")
	private String description;

	@ApiModelProperty(value = "增加时间")
	private String time_added;

	@ApiModelProperty(value = "修改时间")
	private String time_modify;

}
