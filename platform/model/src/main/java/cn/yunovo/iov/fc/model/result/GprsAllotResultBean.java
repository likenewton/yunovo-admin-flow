package cn.yunovo.iov.fc.model.result;


import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GprsAllotResultBean对象", description = "流量分配查询结果bean")
public class GprsAllotResultBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "分配增号")
	private Integer allot_id;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "套餐总量(MB)")
	private Double gprs_amount;

	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "已分配几个月")
	private Integer assigned_month;

	@ApiModelProperty(value = "添加时间")
	private String time_added;

	@ApiModelProperty(value = "过期时间")
	private String time_expire;
	
	@ApiModelProperty(value = "自增号")
	private Integer gprs_vid;

/*	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "分配编号")
	private Integer allot_id;*/

	@ApiModelProperty(value = "当前月份")
	private Integer how_month;

	@ApiModelProperty(value = "当月流量(MB)")
	private Double gprs_value;

	@ApiModelProperty(value = "设备计算余量(MB)")
	private Double balance_dval;

	@ApiModelProperty(value = "剩余流量(MB)")
	private Double balance_value;
	
/*
	@ApiModelProperty(value = "添加时间")
	private String time_added;

	@ApiModelProperty(value = "过期时间")
	private String time_expire;*/

	@ApiModelProperty(value = "修改时间")
	private String time_modify;



}
