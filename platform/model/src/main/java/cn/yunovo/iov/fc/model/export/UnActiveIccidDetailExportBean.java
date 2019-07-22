package cn.yunovo.iov.fc.model.export;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UnActiveIccidDetailExportBean", description = "未激活流量卡信息表")
public class UnActiveIccidDetailExportBean extends BaseRowModel implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ExcelProperty(index = 1, value = "卡号")
	private String card_sn;
	
	@ExcelProperty(index = 2, value = "卡ICCID")
	private String card_iccid;
	
	@ExcelProperty(index = 3, value = "卡批次号")
	private String batch_sn;

	@ExcelProperty(index = 10, value = "导卡时间")
	private String time_added;
	

}
