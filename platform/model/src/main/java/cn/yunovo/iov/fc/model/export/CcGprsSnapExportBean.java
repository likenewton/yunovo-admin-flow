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
 * GPRS流量快照表
 * </p>
 *
 * @author bill
 * @since 2019-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CcGprsSnapExportBean对象", description = "GPRS流量快照表")
public class CcGprsSnapExportBean extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ExcelProperty(index=1, value = "卡ICCID")
	private String card_iccid;
	
	@ExcelProperty(index=2, value = "卡商名称")
	private String card_type_name;
	
	@ExcelProperty(index=3, value="卡批次号")
	private String batch_sn;
	
	@ExcelProperty(index=4, value = "所属机构")
	private String org_name;

	@ExcelProperty(index=5, value = "充值总流量(MB)")
	private Double gprs_amount1;
	
	@ExcelProperty(index=6, value = "充值类型")
	private String pay_memo1;
	
	@ExcelProperty(index=7, value = "充值价格(元)")
	private BigDecimal gprs_price1;
	
	@ExcelProperty(index=8, value = "快照时间")
	private String time_snap;
	
	@ExcelProperty(index=9, value = "快照总流量(MB)")
	private Double gprs_amount;
	
	@ExcelProperty(index=10, value = "快照类型")
	private String pay_memo;
	
	@ExcelProperty(index=11, value = "快照价格(元)")
	private BigDecimal gprs_price;
	
	@ExcelProperty(index=12, value = "是否清零")
	private String allot_reset_cn;
	
	@ExcelProperty(index=13, value = "分配月数")
	private Integer allot_month;
	
	@ExcelProperty(index=14, value = "月均流量(MB)")
	private Double allot_value;
	
	@ExcelProperty(index=15, value = "已配月数")
	private Integer assigned_month;
	
	@ExcelProperty(index=16, value = "激活时间")
	private String time_added;

	@ExcelProperty(index=17, value = "过期时间")
	private String time_expire;
	
	@ExcelProperty(index=18, value = "月流量(MB)")
	private Double gprs_value;
	
	@ExcelProperty(index=19, value = "月剩余流量(MB)")
	private Double balance_dval;
	
	@ExcelProperty(index=20, value = "已用总流量(MB)")
	private Double total_used;
	
	@ExcelProperty(index=21, value = "剩余总流量(MB)")
	private Double total_balance;
	
	@ExcelProperty(index=22, value = "设备更新时间")
	private String time_device;
	
	@ExcelProperty(index=23, value = "订单来源")
	private String pay_from;
	
	
	
	

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "付款方式：0赠送,1微信,2支付宝,3充值卡,4转账")
	private Short pay_method;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "机构名称")
	private Integer card_type;
}
