package cn.yunovo.iov.fc.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
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
	
	@ApiModelProperty(value = "卡iccid")
	private String card_iccid;
	
	@ApiModelProperty(value = "卡商名称")
	private String card_type;
	
	@ApiModelProperty(value = "卡类型中文名称")
	private String card_type_name;
	
	@ApiModelProperty(value = "机构id")
	private String org_id;
	
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	

}
