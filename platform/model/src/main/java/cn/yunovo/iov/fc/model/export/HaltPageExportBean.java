package cn.yunovo.iov.fc.model.export;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * GPRS流量卡信息表
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "HaltPageExportBean", description = "已停卡况信息表")
public class HaltPageExportBean extends BaseRowModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ExcelProperty(index=1,value="卡ICCID")
	private String card_iccid;

	@ExcelProperty(index=4,value = "累记使用流量")
	private Double used_total;

	@ExcelProperty(index=3, value="当月使用流量")
	private Double used_month;

	@ExcelProperty(index=5,value = "剩余流量")
	private Double max_unused;

	@ExcelProperty(index=7, value = "激活时间")
	private String time_active;

	@ExcelProperty(index=6, value = "导入时间")
	private String time_added;

	@ExcelProperty(index=8, value = "设备更新时间")
	private String time_last;

	@ExcelProperty(index=9, value = "上次停用时间")
	private String time_stop;

	@ExcelProperty(index=10, value = "过期时间")
	private String time_expire;
	
	@ExcelProperty(index=1,value="所属机构")
	private String org_name;
	


}
