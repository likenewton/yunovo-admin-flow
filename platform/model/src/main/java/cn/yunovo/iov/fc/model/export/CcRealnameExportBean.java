package cn.yunovo.iov.fc.model.export;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableField;

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
@ApiModel(value = "CcRealnameExportBean对象", description = "流量卡实名制表")
public class CcRealnameExportBean extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 1L;


	@ExcelProperty(index=2, value="设备SN号")
	private String device_sn;

	@ExcelProperty(index=1, value="卡ICCID")
	private String card_iccid;

	@ExcelProperty(index=3, value="身份证姓名")
	private String owner_name;

	@ExcelProperty(index=4, value="身份证编号")
	private String owner_cdi;

	@ExcelProperty(index=5, value = "身份证正面图")
	private String cdi_img1;

	@ExcelProperty(index=6, value = "身份证背面图")
	private String cdi_img2;

	@ExcelProperty(index=7, value = "手持身份证照")
	private String cdi_img3;

	@ApiModelProperty(value = "审核状态:0待审,1无效,2有效")
	private Short cdi_status;

	@ExcelProperty(index=8, value = "实名时间")
	private String time_added;

}
