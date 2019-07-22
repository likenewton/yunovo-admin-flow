package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "HomeDataResultInfoBean对象", description = "首页")
public class HomeDataResultInfoBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer total;
	
	private Integer actived;
	
	private Integer stoped;
	
	private Integer paid;
	
	
	private Integer pay_count;
	
	private Double pay_money;
	
	private Double rebate_money;
	
	
	
	

}
