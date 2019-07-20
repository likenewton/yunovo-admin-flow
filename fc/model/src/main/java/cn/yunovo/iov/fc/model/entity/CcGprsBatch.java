package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量卡发货批次表
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_gprs_batch对象", description = "流量卡发货批次表")
public class CcGprsBatch implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "批次增号")
	@TableId(value = "batch_id", type = IdType.AUTO)
	private Integer batch_id;

	@ApiModelProperty(value = "批次编号")
	private String batch_sn;

	@ApiModelProperty(value = "批次名称")
	private String batch_name;

	@ApiModelProperty(value = "批次备注")
	private String batch_memo;

	@ApiModelProperty(value = "发货人姓名")
	private String batch_shipper;

	@ApiModelProperty(value = "入卡数量")
	private Integer card_amount;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "创建者编号")
	private Integer user_id;

	@ApiModelProperty(value = "更改者编号")
	private Integer alter_id;

	@ApiModelProperty(value = "销省份编号")
	private Integer province_id;

	@ApiModelProperty(value = "销往城市编号")
	private Integer city_id;

	@ApiModelProperty(value = "销区县编号")
	private Integer district_id;

	@ApiModelProperty(value = "车联网设备入库批次编号")
	private Integer clw_batch_id;

	@ApiModelProperty(value = "套餐总量(MB)")
	private Double gprs_amount;

	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "实始套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

	@ApiModelProperty(value = "认证成功后赠送流量(MB)")
	private Double give_value;

	@ApiModelProperty(value = "认证成功后赠送流量有效周期：>=1按月，(>0<1)*100按天")
	private Float give_live_month;

	@ApiModelProperty(value = "车主完善资料后赠送流量(MB)")
	private Double wszl_value;

	@ApiModelProperty(value = "完善资料后赠送流量有效周期：>=1按月，(>0<1)*100按天")
	private Float wszl_live_month;

	@ApiModelProperty(value = "车主绑定设备后赠送流量(MB)")
	private Double bind_value;

	@ApiModelProperty(value = "车主绑定设备后赠送流量有效周期：>=1按月，(>0<1)*100按天")
	private Float bind_live_month;

	@ApiModelProperty(value = "增加时间")
	private String time_added;

	@ApiModelProperty(value = "修改时间")
	private String time_modify;
	
	@ApiModelProperty(value = "创建者用户名")
	private String create_by;
	
	@ApiModelProperty(value = "最近一次修改者用户名")
	private String update_by;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "销省份编号CN")
	private String province_name;

	@TableField(exist=false)
	@ApiModelProperty(value = "销往城市编号CN")
	private String city_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "销区县编号CN")
	private String district_name;

	
}
