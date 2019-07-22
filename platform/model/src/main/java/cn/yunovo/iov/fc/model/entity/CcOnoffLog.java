package cn.yunovo.iov.fc.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 停卡日志
 * </p>
 *
 * @author bill
 * @since 2019-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_onoff_log对象", description = "停卡日志")
@TableName(value="cc_onoff_log")
public class CcOnoffLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "开关编号")
	@TableId(value = "onoff_id", type = IdType.AUTO)
	private Integer onoff_id;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "开关类型：0开1关")
	private Integer onoff_type;

	@ApiModelProperty(value = "执行结果状态：0失败1成功")
	private Integer exec_status;

	@ApiModelProperty(value = "剩余流量(MB)")
	private Double balance_value;

	@ApiModelProperty(value = "操作者编号")
	private Integer user_id;

	@ApiModelProperty(value = "操作者姓名")
	private String user_name;

	@ApiModelProperty(value = "操作时间")
	private String time_added;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "卡iccid")
	private String card_iccid;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "卡商名称")
	private String card_type;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "卡类型中文名称")
	private String card_type_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构id")
	private String org_id;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "上次充值时间")
	private String time_paid;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "停卡次数")
	private Integer stop_num;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "充值次数")
	private Integer pay_count;

	@TableField(exist=false)
	@ApiModelProperty(value = "最后时间")
	private String time_last;

	@TableField(exist=false)
	@ApiModelProperty(value = "上次停号时间")
	private String time_stop;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "上次停卡流量")
	private Double stop_value;
	
}
