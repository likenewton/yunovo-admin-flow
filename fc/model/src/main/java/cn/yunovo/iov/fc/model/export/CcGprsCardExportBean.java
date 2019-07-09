package cn.yunovo.iov.fc.model.export;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * GPRS流量卡信息表
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CcGprsCardExportBean", description = "CcGprsCardExportBean流量卡信息表")
public class CcGprsCardExportBean extends BaseRowModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ExcelProperty(index=2, value="卡批次号")
	private String batch_sn;
	
	private Integer batch_id;

	@ExcelProperty(index=1, value="卡ICCID")
	private String card_iccid;

	@ApiModelProperty(value = "卡的类型:0未知卡,1智网吉林,2智网JASPER卡,3吉林长春,4智网定向卡")
	private Integer card_type;

	@ExcelProperty(index=6, value="累记使用流量")
	private Double used_total;

	@ExcelProperty(index=5, value="当月使用流量")
	private Double used_month;

	@ApiModelProperty(value = "累记充值流量(MB)")
	private Double pay_total;

	@ExcelProperty(index=7, value="剩余流量")
	private Double max_unused;

	@ExcelProperty(index=9, value="联通建卡时间")
	private String unicom_ctime;

	@ExcelProperty(index=10, value="激活时间")
	private String time_active;

	@ExcelProperty(index=8, value="导卡时间")
	private String time_added;

	@ExcelProperty(index=11, value="最后更新时间")
	private String time_last;

	@ExcelProperty(index=12, value="过期时间")
	private String time_expire;
	
	@ExcelProperty(index=4, value="所属卡商")
	private String card_type_name;
	
	@ExcelProperty(index=3, value="所属机构")
	private String org_name;


}
