package cn.yunovo.iov.fc.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量卡日流量使用情况统计表
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_stats_day对象", description = "流量卡日流量使用情况统计表")
public class CcStatsDay implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "统计日期:2016-01-01")
	private LocalDate stats_date;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "日使用流量(MB)")
	private Double day_used;

	@ApiModelProperty(value = "日超标流量(MB)")
	private Double day_over;

	@ApiModelProperty(value = "白名单日用流量(MB)")
	private Double day_wlist;

	@ApiModelProperty(value = "昨日剩余流量(MB)")
	private Double ayer_unused;

	@ApiModelProperty(value = "当日剩余流量(MB)")
	private Double today_unused;

	@ApiModelProperty(value = "修改时间")
	private LocalDateTime time_modify;

}
