package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 机构管理表
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Data
@ApiModel(value = "Cc_org对象", description = "机构管理表")
@TableName("cc_org")
public class CcOrg implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "机构编号")
	@TableId(value = "org_id", type = IdType.AUTO)
	private Integer org_id;

	@ApiModelProperty(value = "创建者编号")
	private Integer user_id;

	@ApiModelProperty(value = "更改者编号")
	private Integer alter_id;

	@ApiModelProperty(value = "父亲编号")
	private Integer parent_id;

	@ApiModelProperty(value = "机构名称")
	private String name;

	@ApiModelProperty(value = "机构描述")
	private String memo;

	@ApiModelProperty(value = "机构拼音简码")
	private String spell;

	@ApiModelProperty(value = "负责人邮箱")
	private String email;

	@ApiModelProperty(value = "负责人电话")
	private String tel;

	@ApiModelProperty(value = "返利点数值")
	private Float rebate_value;

	@ApiModelProperty(value = "合作伙伴编号")
	private String partner_id;

	@ApiModelProperty(value = "合作伙伴密匙")
	private String partner_key;

	@ApiModelProperty(value = "异步通知地址")
	private String notify_url;

	@ApiModelProperty(value = "可开账户数量")
	private Integer user_total;

	@ApiModelProperty(value = "增加时间")
	private String time_added;

	@ApiModelProperty(value = "修改时间")
	private String time_modify;
	
	@ApiModelProperty(value = "创建者用户名")
	private String create_by;
		
	@ApiModelProperty(value = "最近一次修改者用户名")
	private String update_by;
	
	@ApiModelProperty(value = "机构用户数")
	@TableField(exist=false)
	private Integer user_count;
	
	
	public String getOrgIdStr() {
		return String.valueOf(this.org_id);
	}

}
