package cn.yunovo.iov.fc.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量卡实名制表
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_realname对象", description = "流量卡实名制表")
@TableName("cc_realname")
public class CcRealname implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "操作者编号")
	private Integer user_id;

	@ApiModelProperty(value = "设备SN号")
	private String device_sn;

	@ApiModelProperty(value = "卡ICCID")
	private String card_iccid;

	@ApiModelProperty(value = "身份证姓名")
	private String owner_name;

	@ApiModelProperty(value = "身份证性别0保密1男2女")
	private Boolean owner_gender;

	@ApiModelProperty(value = "身份证编号")
	private String owner_cdi;

	@ApiModelProperty(value = "身份证正面图")
	private String cdi_img1;

	@ApiModelProperty(value = "身份证背面图")
	private String cdi_img2;

	@ApiModelProperty(value = "手持身份证照")
	private String cdi_img3;

	@ApiModelProperty(value = "审核状态:0待审,1无效,2有效")
	private Short cdi_status;

	@ApiModelProperty(value = "认证成功后赠送流量(MB)")
	private BigDecimal give_value;

	@ApiModelProperty(value = "赠送时间")
	private String give_time;

	@ApiModelProperty(value = "增加时间")
	private String time_added;

	@ApiModelProperty(value = "修改时间")
	private String time_modify;

	@ApiModelProperty(value = "审核时间")
	private String time_audit;

	@ApiModelProperty(value = "最近一次修改者用户名")
	private String update_by;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "机构名称")
	private String org_name;

	@TableField(exist = false)
	@ApiModelProperty(value = "操作者姓名(CN)")
	private String first_name;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "最后上报时间")
	private String time_last;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "卡激活时间")
	private String time_active;
	
	
	public String cacheJsonString() {
		
		JSONObject cache = new JSONObject();
		cache.put("card_id",        this.card_id           );
		cache.put("user_id",        this.user_id           );
		cache.put("device_sn",      this.device_sn         );
		cache.put("card_iccid",     this.card_iccid        );
		cache.put("owner_name",     this.owner_name        );
		cache.put("owner_gender",   this.owner_gender      );
		cache.put("owner_cdi",      this.owner_cdi         );
		cache.put("cdi_img1",       this.cdi_img1          );
		cache.put("cdi_img2",       this.cdi_img2          );
		cache.put("cdi_img3",       this.cdi_img3          );
		cache.put("cdi_status",     this.cdi_status        );
		cache.put("give_value",     this.give_value        );
		cache.put("give_time",      this.give_time         );
		cache.put("time_added",     this.time_added        );
		cache.put("time_modify",    this.time_modify       );
		cache.put("time_audit",     this.time_audit        );

		return JSONObject.toJSONString(cache, SerializerFeature.WriteMapNullValue);
		
	}
}
