package cn.yunovo.iov.fc.model.export;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ActiveIccidDetailExportBean", description = "已激活流量卡信息表")
public class ActiveIccidDetailExportBean extends BaseRowModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Getter
	@Setter
	@ExcelProperty(index = 1, value = "卡号")
	private String card_sn;
	
	@Getter
	@Setter
	@ExcelProperty(index = 2, value = "卡ICCID")
	private String card_iccid;
	
	@Getter
	@Setter
	@ExcelProperty(index = 3, value = "卡批次号")
	private String batch_sn;
	
	@Getter
	@Setter
	//L联通是否已停卡:0否1是
	private Short unicom_stop;
	
	@Setter
	@ExcelProperty(index = 4, value = "状态")
	private String unicom_stop_cn;
	
	@Getter
	@Setter
	@ExcelProperty(index = 5, value = "使用总流量(MB)")
	private Double unicom_total;
	
	@Getter
	@Setter
	@ExcelProperty(index = 6, value = "剩余流量(MB)")
	private Double unicom_unused;
	
	@Getter
	@Setter
	@ExcelProperty(index = 7, value = "设备更新时间")
	private String time_last;
	
	@Getter
	@Setter
	@ExcelProperty(index = 8, value = "联通建卡时间")
	private String unicom_ctime;
	
	@Getter
	@Setter
	@ExcelProperty(index = 9, value = "激活时间")
	private String time_active;
	
	@Getter
	@Setter
	@ExcelProperty(index = 10, value = "导卡时间")
	private String time_added;
	
	@Getter
	@Setter
	@ExcelProperty(index =11, value = "过期时间")
	private String time_expire;

	public String getUnicom_stop_cn() {
		
		return unicom_stop == null ? "未知" :  unicom_stop == 0 ? "正常":"停用";
	}
	
	

}
