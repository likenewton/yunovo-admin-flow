package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量平台与设备中心机构关系表
 * </p>
 *
 * @author bill
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_org_relationship对象", description = "流量平台与设备中心机构关系表")
@TableName(value="cc_org_relationship")
public class CcOrgRelationship implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty(value = "流量平台机构id")
	private Integer org_id;

	@ApiModelProperty(value = "设备所属机构code")
	private String device_org_code;

	private String create_by;

	private String create_datetime;

	private String update_by;

	private String update_datetime;

}
