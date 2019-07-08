package cn.yunovo.iov.fc.model.export;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * GPRS流量充值表
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CcGprsPayExportBean对象", description = "充值明细导出Bean")
public class CcGprsPayExportBean extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ExcelProperty(index=1, value = "卡ICCID")
	private String card_iccid;
	
	@ExcelProperty(index=2, value = "卡商名称")
	private String card_type_name;
	
	@ExcelProperty(index=3, value="卡批次号")
	private String batch_sn;
	
	@ExcelProperty(index=4, value = "所属机构")
	private String org_name;
	
	@ExcelProperty(index=5, value = "激活时间")
	private String time_active;
	
	@ExcelProperty(index=6, value = "累记用量(MB)")
	private Double unicom_total;
	
	@ExcelProperty(index=7, value = "支付方式")
	private String pay_method_name;
	
	@ExcelProperty(index=8, value = "支付流水号")
	private String transfer_id;
	
	@ExcelProperty(index=9, value = "套餐流量(MB)")
	private Double gprs_amount;
	
	@ExcelProperty(index=10, value = "套餐月数")
	private Float live_month;
	
	@ExcelProperty(index=11, value = "套餐金额(元)")
	private BigDecimal gprs_price;
	
	@ExcelProperty(index=12, value = "返利金额(元)")
	private BigDecimal rebate_money;

	@ExcelProperty(index=13, value = "过期时间")
	private String time_expire;
	
	@ExcelProperty(index=14, value = "充值时间")
	private String time_added;
	
	@ExcelProperty(index=15, value="付款状态")
	private String is_paid_name;
	
	@ExcelProperty(index=16, value = "付款时间")
	private String time_paid;
	
	
	
	@ApiModelProperty(value = "是否已支付:0未付款,1已付款")
	private Short is_paid;
	
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "支付订单来源")
	private String pay_from;

	@ApiModelProperty(value = "付款方式：0赠送,1微信,2支付宝,3充值卡,4转账")
	private Short pay_method;

	@ApiModelProperty(value = "机构名称")
	private Integer card_type;

}
