package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CardGiftBean对象", description = "业务管理-流量卡赠送bean")
public class CardGiftBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String iccid;
	
	private String ret;
	
	private String msg;
}
