package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CardRestBean对象", description = "业务管理-流量卡重置bean")
public class CardRestBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String iccid;
	
	private String ret;
	
	private String msg;
}
