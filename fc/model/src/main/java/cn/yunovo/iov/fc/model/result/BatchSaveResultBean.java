package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "BatchSaveResultBean对象", description = "业务管理-批次导入结果")
public class BatchSaveResultBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String batch_sn;
	
	private Integer iccid_count;
	
	private Integer success_count;
	
	private Integer failed_count;
	
	private Integer update_count;
}
