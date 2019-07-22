package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量卡日志档案表
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cc_card_log")
@ApiModel(value = "Cc_card_log对象", description = "流量卡日志档案表")
public class CcCardLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "日志增号")
	@TableId(value = "log_id", type = IdType.AUTO)
	private Long log_id;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "日志类型:1入库,2激活,3赠送,4充值,5停卡,6开卡,7变更,8迁移,9重置,10实名")
	private Integer log_type;

	@ApiModelProperty(value = "日志文本简介")
	private String log_text;

	@ApiModelProperty(value = "详情连接地址")
	private String log_url;

	@ApiModelProperty(value = "增加时间")
	private String time_added;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "日志类型CN")
	private String log_type_name;

}
