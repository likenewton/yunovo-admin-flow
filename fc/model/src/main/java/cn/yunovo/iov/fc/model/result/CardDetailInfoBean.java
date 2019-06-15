package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CardDetailInfoBean对象", description = "业务管理-流量卡详情bean")
public class CardDetailInfoBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;
	
	@ApiModelProperty(value = "卡ICCID")
	private String card_iccid;
	
	@ApiModelProperty(value = "累记使用流量(MB)")
	private Double used_total;

	@ApiModelProperty(value = "当月使用流量(MB)")
	private Double used_month;

	@ApiModelProperty(value = "累记充值流量(MB)")
	private Double pay_total;

	@ApiModelProperty(value = "最大可使用流量(MB)")
	private Double max_unused;

	@ApiModelProperty(value = "累计白名单用量(MB)")
	private Double wlistTotal;
	
	@ApiModelProperty(value = "最后时间")
	private String time_last;
	
}
