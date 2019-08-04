package cn.yunovo.iov.fc.model.entity;

import com.alibaba.fastjson.JSONObject;
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
 * 流量卡最后一次应用白名单消耗上报信息
 * </p>
 *
 * @author bill
 * @since 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_apk_whitelist_lastreport_info对象", description = "流量卡最后一次应用白名单消耗上报信息")
public class CcApkWhitelistLastreportInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty(value = "流量卡id")
	private Integer card_id;

	@ApiModelProperty(value = "流量卡iccid")
	private String iccid;

	@ApiModelProperty(value = "当月渠道商apk白名单流量消耗数")
	private Long org_gprs_month;

	@ApiModelProperty(value = "当月云智apk白名单流量消耗数，单位Byte")
	private Long yunovo_gprs_month;

	@ApiModelProperty(value = "场景码")
	private Long nonce;

	@ApiModelProperty(value = "设备sn")
	private String sn;

	@ApiModelProperty(value = "上报次数")
	private Long count;

	private String create_datetime;

	private String update_datetime;

	private String update_by;

	private String create_by;

	public Object cacheJsonSring() {
		return JSONObject.toJSONString(this);
	}
	
	

}
