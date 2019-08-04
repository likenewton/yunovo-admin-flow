package cn.yunovo.iov.fc.model.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author bill
 * @since 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_apk_whitelist_used_report_info对象", description = "")
public class CcApkWhitelistUsedReportInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键")
	private String id;

	@ApiModelProperty(value = "流量卡所属机构id")
	private Integer org_id;

	@ApiModelProperty(value = "设备Sn")
	private String sn;

	@ApiModelProperty(value = "流量卡id")
	private Integer card_id;

	@ApiModelProperty(value = "流量卡iccid")
	private String iccid;

	@ApiModelProperty(value = "当月渠道商白名单流量消耗")
	private Long org_gprs_month;

	@ApiModelProperty(value = "当月云智白名单流量消耗")
	private Long yunovo_gprs_month;

	@ApiModelProperty(value = "上一次渠道流量消耗上报值")
	private Long prev_org_gprs_month;

	@ApiModelProperty(value = "上一次云智流量消耗上报值")
	private Long prev_yunovo_gprs_month;

	@ApiModelProperty(value = "状态：0 待补偿，1 已补偿，2、不补偿，3、补偿失败,4、补偿部分")
	private Integer status;

	@ApiModelProperty(value = "是否异常，0 正常，1 异常")
	private Integer error_code;

	@ApiModelProperty(value = "支付订单")
	private String pay_sn;

	@ApiModelProperty(value = "场景值")
	private Long nonce;

	@ApiModelProperty(value = "备注")
	private String memo;

	private String create_by;

	private String create_datetime;

	private String update_by;

	private String update_datetime;

	@ApiModelProperty(value = "赠送流量数")
	private Long give_yunovo_gprs;

	@ApiModelProperty(value = "赠送流量数")
	private Long give_org_gprs;

}
