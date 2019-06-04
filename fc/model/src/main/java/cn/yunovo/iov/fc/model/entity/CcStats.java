package cn.yunovo.iov.fc.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 各项参数统计表
 * </p>
 *
 * @author bill
 * @since 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CcStats对象", description = "各项参数统计表")
public class CcStats implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "统计日期")
	private String stats_date;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "实名卡数")
	private Integer rlname_cards;

	@ApiModelProperty(value = "售卡总数")
	private Integer card_total;

	@ApiModelProperty(value = "累计续费卡数")
	private Integer pay_cards;

	@ApiModelProperty(value = "返利金额")
	private BigDecimal rebate_money;

	@ApiModelProperty(value = "累计充值次数")
	private Integer pay_total;

	@ApiModelProperty(value = "充值次数")
	private Integer pay_count;

	@ApiModelProperty(value = "充值失败次数")
	private Integer pay_failed;

	@ApiModelProperty(value = "充值成功次数")
	private Integer pay_succeed;

	@ApiModelProperty(value = "续费失败金额")
	private BigDecimal pay_failed_money;

	@ApiModelProperty(value = "续费成功金额")
	private BigDecimal pay_succeed_money;

	@ApiModelProperty(value = "流量卡使用总数")
	private Integer online_count;

	@ApiModelProperty(value = "正常使用数量")
	private Integer online_api;

	@ApiModelProperty(value = "非正常使用数量")
	private Integer online_other;

	@ApiModelProperty(value = "累计激活数")
	private Integer active_total;

	@ApiModelProperty(value = "流量卡激活总数")
	private Integer active_count;

	@ApiModelProperty(value = "联通激活数")
	private Integer active_unicom;

	@ApiModelProperty(value = "设备端激活总数")
	private Integer active_device;

	@ApiModelProperty(value = "未激活数")
	private Integer unactive_total;

	@ApiModelProperty(value = "开启卡数")
	private Integer open_count;

	@ApiModelProperty(value = "停卡总数")
	private Integer stop_total;

	@ApiModelProperty(value = "停卡数量")
	private Integer stop_count;

	@ApiModelProperty(value = "有上报停卡")
	private Integer stop_yreport;

	@ApiModelProperty(value = "无上报停卡")
	private Integer stop_nreport;

	@ApiModelProperty(value = "初始值用完总数")
	private Integer used2g_total;

	@ApiModelProperty(value = "初始值用完充值次数")
	private Integer used2g_pay;

	@ApiModelProperty(value = "充值流量(MB)")
	private BigDecimal gprs_amount;

	@ApiModelProperty(value = "消耗流量(MB)")
	private BigDecimal used_amount;

	@ApiModelProperty(value = "剩余流量(MB)")
	private BigDecimal unused_amount;

	@ApiModelProperty(value = "超标流量(MB)")
	private BigDecimal over_amount;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	

}
