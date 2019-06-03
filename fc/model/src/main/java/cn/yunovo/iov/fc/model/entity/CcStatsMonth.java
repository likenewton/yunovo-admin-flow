package cn.yunovo.iov.fc.model.entity;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量卡月流量使用情况统计表
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_stats_month对象", description = "流量卡月流量使用情况统计表")
public class CcStatsMonth extends BaseRowModel {


	@ExcelProperty(index=5, value="月份")
	@ApiModelProperty(value = "月份:201601")
	private Integer how_month;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ExcelProperty(index=6, value="月使用流量（MB）")
	@ApiModelProperty(value = "月使用流量(MB)")
	private Double month_used;

	@ApiModelProperty(value = "月剩余流量(MB)")
	private Double month_unused;

	@ApiModelProperty(value = "月超标流量(MB)")
	private Double month_over;

	@ApiModelProperty(value = "白名单日用流量(MB)")
	private Double month_wlist;

	@ApiModelProperty(value = "修改时间")
	private String time_modify;
	
	@ExcelProperty(index=1, value="卡ICCID")
	@ApiModelProperty(value = "卡iccid")
	private String card_iccid;
	
	@ApiModelProperty(value = "卡商名称")
	private String card_type;
	
	@ExcelProperty(index=2, value="卡商名称")
	@ApiModelProperty(value = "卡类型中文名称")
	private String card_type_name;
	
	@ApiModelProperty(value = "机构id")
	private String org_id;
	
	@ExcelProperty(index=3, value="所属机构")
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@ExcelProperty(index=7, value="激活时间")
	@ApiModelProperty(value = "流量卡激活时间")
	private String time_active;
	
	@ExcelProperty(index=4, value="导卡时间")
	@ApiModelProperty(value = "流量卡入库时间")
	private String time_added;
	
	@ApiModelProperty(value = "总消耗流量")
	private Long usedTotal;
	
}
