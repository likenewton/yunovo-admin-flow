package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流量卡发货批次表
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CcGprsBatchForm对象", description = "流量卡出货批次表单")
public class CcGprsBatchForm extends BaseForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message="请填写批次编号", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@Size(max=50, message="批次编号长度不能超过50个字", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@ApiModelProperty(value = "批次编号")
	private String batch_sn;

	@NotEmpty(message="请填写批次名称", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@Size(max=50, message="批次名称长度不能超过50个字", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@ApiModelProperty(value = "批次名称")
	private String batch_name;

	@Size(max=200, message="批次名称长度不能超过200个字", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@ApiModelProperty(value = "批次备注")
	private String batch_memo;

	@NotEmpty(message="请填写发货人姓名", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@Size(max=50, message="发货人姓名长度不能超过50个字", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@ApiModelProperty(value = "发货人姓名")
	private String batch_shipper;

	@NotNull(message="请选择机构", groups= {InsertGroupValidate.class})
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@NotNull(message="请选择销往省份", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@ApiModelProperty(value = "销省份编号")
	private Integer province_id;

	@NotNull(message="请选择销往城市", groups= {InsertGroupValidate.class,UpdateGroupValidate.class})
	@ApiModelProperty(value = "销往城市编号")
	private Integer city_id;

	@ApiModelProperty(value = "销区县编号")
	private Integer district_id;

	@ApiModelProperty(value = "车联网设备入库批次编号")
	private Integer clw_batch_id;

	@NotNull(message="请填写流量值", groups= {InsertGroupValidate.class})
	@DecimalMin(value = "0", message="请填写正确的流量值", groups= {InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐总量(MB)")
	private Double gprs_amount;

	
	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@NotNull(message="请填写有效周期", groups= {InsertGroupValidate.class})
	@DecimalMin(value = "0.01", message="请填写正确的有效周期", groups= {InsertGroupValidate.class})
	@ApiModelProperty(value = "实始套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

	@ApiModelProperty(value = "认证成功后赠送流量(MB)")
	private Double give_value = 0D;

	@ApiModelProperty(value = "认证成功后赠送流量有效周期：>=1按月，(>0<1)*100按天")
	private Float give_live_month = 0F;

	@ApiModelProperty(value = "车主完善资料后赠送流量(MB)")
	private Double wszl_value = 0D;

	@ApiModelProperty(value = "完善资料后赠送流量有效周期：>=1按月，(>0<1)*100按天")
	private Float wszl_live_month = 0F;

	@ApiModelProperty(value = "车主绑定设备后赠送流量(MB)")
	private Double bind_value = 0D;

	@ApiModelProperty(value = "车主绑定设备后赠送流量有效周期：>=1按月，(>0<1)*100按天")
	private Float bind_live_month;
	
	@NotNull(message="请填选择正确流量卡商", groups= {InsertGroupValidate.class})
	@Range(min = 0, max = 5, message="请填选择正确流量卡商", groups= {InsertGroupValidate.class})
	@ApiModelProperty(value = "卡的类型:0未知卡,1智网吉林,2智网JASPER卡,3吉林长春,4智网定向卡")
	private Integer card_type;
	
	@NotNull(message="请上传对应的文件", groups= {InsertGroupValidate.class})
	@ApiModelProperty("文件")
	private MultipartFile file;

}
