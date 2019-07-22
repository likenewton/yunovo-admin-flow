package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CardTotalByOrgidInfoBean对象", description = "业务管理-机构流量卡数统计")
public class CardTotalByOrgidInfoBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("卡数")
	private Integer total;
	
	@ApiModelProperty("机构id")
	private Integer org_id;
	
	@ApiModelProperty("机构名称")
	private String org_name;

}
