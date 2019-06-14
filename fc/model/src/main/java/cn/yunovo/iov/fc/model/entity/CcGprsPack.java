package cn.yunovo.iov.fc.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量充值套餐表
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_gprs_pack对象", description = "流量充值套餐表")
public class CcGprsPack implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "套餐编号")
	@TableId(value = "pack_id", type = IdType.AUTO)
	private Integer pack_id;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "创建者编号")
	private Integer user_id;

	@ApiModelProperty(value = "更改者编号")
	private Integer alter_id;

	@ApiModelProperty(value = "套餐名称")
	private String pack_name;

	@ApiModelProperty(value = "套餐备注")
	private String pack_memo;

	@ApiModelProperty(value = "套餐模式：0叠加套餐 1延期套餐")
	private Short pack_mode;

	@ApiModelProperty(value = "是否推荐：0否 1是")
	private Short pack_recom;

	@ApiModelProperty(value = "返利金额(元)")
	private BigDecimal pack_rebate;

	@ApiModelProperty(value = "套餐状态：0停用 1启用")
	private Short pack_status;

	@ApiModelProperty(value = "流量(MB)")
	private Double gprs_amount;

	@ApiModelProperty(value = "流量价格(元)")
	private BigDecimal gprs_price;

	@ApiModelProperty(value = "流量折扣>0<=1(0.5代表打五折)")
	private BigDecimal gprs_discount;

	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

	@ApiModelProperty(value = "售出份数")
	private Integer sell_amount;

	@ApiModelProperty(value = "排序值")
	private Integer sort_order;

	@ApiModelProperty(value = "添加时间")
	private String time_added;

	@ApiModelProperty(value = "修改时间")
	private String time_modify;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "操作者姓名(CN)")
	private String first_name;

	@TableField(exist=false)
	@ApiModelProperty(value = "更改者编号")
	private String alter_name;

}
