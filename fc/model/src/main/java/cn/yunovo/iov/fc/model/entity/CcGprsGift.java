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
 * GPRS流量赠送表
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_gprs_gift对象", description = "GPRS流量赠送表")
public class CcGprsGift implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "GPRS赠送编号")
	@TableId(value = "gift_id", type = IdType.AUTO)
	private Integer gift_id;

	@ApiModelProperty(value = "操作者编号")
	private Integer user_id;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "卡ICCID")
	private String card_iccid;

	@ApiModelProperty(value = "流量(MB)")
	private Double gprs_amount;

	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

	@ApiModelProperty(value = "赠送者姓名")
	private String gift_name;

	@ApiModelProperty(value = "添加时间")
	private String time_added;

	@ApiModelProperty(value = "过期时间")
	private String time_expire;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构编号(CN)")
	private String org_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "操作者姓名(CN)")
	private String first_name;

}
