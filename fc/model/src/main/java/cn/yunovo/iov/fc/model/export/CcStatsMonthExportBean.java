package cn.yunovo.iov.fc.model.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CcStatsMonthExportBean对象", description = "流量卡月流量使用情况统计表导出")
public class CcStatsMonthExportBean extends BaseRowModel{

	
	@ExcelProperty(index=5, value="月份")
	@ApiModelProperty(value = "月份:201601")
	private Integer how_month;

	@ExcelProperty(index=6, value="月使用流量（MB）")
	@ApiModelProperty(value = "月使用流量(MB)")
	private Double month_used;

	@ExcelProperty(index=1, value="卡ICCID")
	private String card_iccid;
	
	@TableField(exist=false)
	private String card_type;
	
	@ExcelProperty(index=2, value="卡商名称")
	private String card_type_name;
	
	private String org_id;
	
	@ExcelProperty(index=3, value="所属机构")
	private String org_name;
	
	@ExcelProperty(index=7, value="激活时间")
	@ApiModelProperty(value = "流量卡激活时间")
	private String time_active;
	
	@ExcelProperty(index=4, value="导卡时间")
	@ApiModelProperty(value = "流量卡入库时间")
	private String time_added;
	
}
