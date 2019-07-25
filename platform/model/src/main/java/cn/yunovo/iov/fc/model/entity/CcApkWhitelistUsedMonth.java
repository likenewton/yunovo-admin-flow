package cn.yunovo.iov.fc.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2019-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_apk_whitelist_used_month对象", description = "")
@TableName(value="cc_apk_whitelist_used_month")
public class CcApkWhitelistUsedMonth implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "月份")
	private Integer month;

	@ApiModelProperty(value = "流量卡iccid")
	private String iccid;

	@ApiModelProperty(value = "当月渠道商白名单流量消耗")
	private Long org_gprs;

	@ApiModelProperty(value = "当月云智白名单流量消耗")
	private Long yunovo_gprs;

	private String create_by;

	private String update_by;

	private String create_datetime;

	private String update_datetime;

}