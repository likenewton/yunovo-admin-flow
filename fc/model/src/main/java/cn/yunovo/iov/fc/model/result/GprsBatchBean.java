package cn.yunovo.iov.fc.model.result;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GprsBatchBean extends BaseRowModel{

	@ExcelProperty(index=1)
	private String ICCID;
	
	@ExcelProperty(index=0)
	private String MSISDN;
	
	@ExcelProperty(index=2)
	private String IMSI;
	
}
