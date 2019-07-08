package cn.yunovo.iov.fc.model.export;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
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
public class CcStatsExportBean extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelProperty(index=1, value = "统计日期")
	private String stats_date;

	@ExcelProperty(index=2, value = "售卡总数")
	private Integer card_total;

	@ExcelProperty(index=3, value = "累计充值次数")
	private Integer pay_total;

	@ExcelProperty(index=4, value = "充值次数")
	private Integer pay_count;

	@ExcelProperty(index=5, value = "充值失败次数")
	private Integer pay_failed;

	@ExcelProperty(index=6, value = "充值成功次数")
	private Integer pay_succeed;

	@ExcelProperty(index=7, value = "流量卡使用总数")
	private Integer online_count;

	@ExcelProperty(index=8, value = "正常使用数量")
	private Integer online_api;

	@ExcelProperty(index=9, value = "非正常使用数量")
	private Integer online_other;

	@ExcelProperty(index=10, value = "累计激活数")
	private Integer active_total;

	@ExcelProperty(index=11, value = "流量卡激活总数")
	private Integer active_count;

	@ExcelProperty(index=12, value = "联通激活数")
	private Integer active_unicom;

	@ExcelProperty(index=13, value = "设备端激活总数")
	private Integer active_device;

	@ExcelProperty(index=14, value = "累计停卡数")
	private Integer stop_total;

	@ExcelProperty(index=15, value = "停卡数量")
	private Integer stop_count;

	@ExcelProperty(index=16, value = "有上报停卡")
	private Integer stop_yreport;

	@ExcelProperty(index=17, value = "无上报停卡")
	private Integer stop_nreport;

	@ExcelProperty(index=18, value = "初始值用完总数")
	private Integer used2g_total;

	@ExcelProperty(index=19,value = "初始值用完充值次数")
	private Integer used2g_pay;

	

}
