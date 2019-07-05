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
@ApiModel(value = "AbnormalExportBean", description = "用量异常信息导出BEan")
public class AbnormalExportBean extends BaseRowModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@ExcelProperty(index=1,value="卡ICCID")
	private String card_iccid;

	@ExcelProperty(index=3,value = "平台使用总流量(MB)")
	private Double used_total;

	@ExcelProperty(index=4, value="联通使用总流量(MB)")
	private Double unicom_total;

	@ExcelProperty(index=2,value = "剩余流量(MB)")
	private Double max_unused;

}

