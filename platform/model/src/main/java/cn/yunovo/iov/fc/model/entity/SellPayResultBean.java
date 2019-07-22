package cn.yunovo.iov.fc.model.entity;


import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "SellPayResultBean对象", description = "续费数据查询结果bean")
public class SellPayResultBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@ApiModelProperty(value = "售卡数量")
	private Integer card_count;
	
	@ApiModelProperty(value = "激活卡数")
	private Integer act_count;
	
	@ApiModelProperty(value = "续费卡数")
	private Integer pay_cards;
	
	@ApiModelProperty(value = "续费比率")
	private Double pay_rate;
}
