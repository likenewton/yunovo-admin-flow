package cn.yunovo.iov.fc.model.form;

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
@ApiModel(value = "CcGprsBatchForm对象", description = "流量卡出货批次表单")
public class CcGprsBatchForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "批次编号")
	private String batch_sn;

	@ApiModelProperty(value = "批次名称")
	private String batch_name;

	@ApiModelProperty(value = "批次备注")
	private String batch_memo;

	@ApiModelProperty(value = "发货人姓名")
	private String batch_shipper;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

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

}
