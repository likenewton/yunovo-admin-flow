package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import cn.yunovo.iov.fc.common.utils.math.MathUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
@ApiModel(value = "UnicomStatResultBean对象", description = "统计分析-联通情况查询结果bean")
public class UnicomStatResultBean extends BaseRowModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "机构id")
	private Integer org_id;

	@Getter
	@Setter
	@ExcelProperty(index = 1, value="机构名称")
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@Getter
	@Setter
	@ExcelProperty(index = 2, value="售卡数量")
	@ApiModelProperty(value = "售卡数量")
	private Integer card_count;
	
	@Getter
	@Setter
	@ExcelProperty(index = 3, value="未激活数量")
	private Integer nonactivated;
	
	@Setter
	@ExcelProperty(index = 4, value="未激活率(%)")
	private Float nonactivated_rate;
	
	@Getter
	@Setter
	@ExcelProperty(index = 5, value="已激活数量")
	private Integer activated;
	
	@Setter
	@ExcelProperty(index = 6, value="已激活率(%)")
	private Float activated_rate;
	
	@Getter
	@Setter
	@ExcelProperty(index = 7, value="使用总流量")
	@ApiModelProperty(value = "使用总流量")
	private Double unicom_count;
	
	@Getter
	@Setter
	@ExcelProperty(index = 8, value="月使用流量")
	@ApiModelProperty(value = "当月使用流量")
	private Double month_count;
	
	public Float getNonactivated_rate() {
		
		nonactivated =  nonactivated == null ? 0 : nonactivated;
		if(card_count == null || card_count == 0) {
			return 0F;
		}
		
		return MathUtils.round(nonactivated / card_count.floatValue() * 100, 3, BigDecimal.ROUND_DOWN).floatValue();
	}

	public Float getActivated_rate() {
		
		activated =  activated == null ? 0 : activated;
		if(card_count == 0) {
			return 0F;
		}
		return MathUtils.round(activated / card_count.floatValue() * 100, 3, BigDecimal.ROUND_DOWN).floatValue();
	}
	
}
