package cn.yunovo.iov.fc.model.result;


import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import cn.yunovo.iov.fc.common.utils.math.MathUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ApiModel(value = "CardUsedResultBean对象", description = "累计用量查询结果bean")
public class CardUsedResultBean extends BaseRowModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Setter
	@Getter
	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@Setter
	@Getter
	@ExcelProperty(index = 1, value = "机构名称")
	private String org_name;
	
	@Setter
	@Getter
	@ExcelProperty(index = 2, value = "售卡数量")
	private Integer card_count;
	
	@Setter
	@Getter
	@ApiModelProperty(value = "停卡数")
	private Integer unicom_stop;
	
	@Setter
	@Getter
	@ExcelProperty(index = 5, value = "已激活卡数")
	private Integer activated;
	
	@Setter
	@Getter
	@ExcelProperty(index = 3, value = "未激活卡数")
	private Integer nonactivated;
	
	@Setter
	@ExcelProperty(index = 4, value = "未激活率(%)")
	private Float nonactivated_rate;
	
	@Setter
	@ExcelProperty(index = 6, value = "已激活卡率")
	private Float activated_rate;
	
	@Setter
	@Getter
	@ExcelProperty(index = 9, value = "剩余流量")
	private Double unused_count;
	
	@Setter
	@Getter
	@ExcelProperty(index = 8, value = "使用流量")
	private Double used_count;
	
	@Setter
	@Getter
	@ApiModelProperty(value = "分配总流量")
	private Double pay_total;
	
	@Setter
	@ExcelProperty(index = 10, value = "使用流量率")
	private Float used_rate;
	
	@Setter
	@ExcelProperty(index = 7, value = "流量总计")
	private Double gprs_amount;

	public Float getNonactivated_rate() {
		
		nonactivated =  (nonactivated == null ? 0 : nonactivated);
		if(card_count == null || card_count == 0) {
			return 0F;
		}
		
		return MathUtils.round((nonactivated / (card_count * 0.1F)) * 100, 2).floatValue();
	}

	public Float getUsed_rate() {
		
		unused_count = unused_count == null ? 0D : unused_count;
		used_count = used_count == null ? 0D : used_count;
		
		if((unused_count + used_count) == 0) {
			return 0F;
		}
		return MathUtils.round(used_count / (unused_count + used_count) * 100, 2).floatValue();
	}

	public Double getGprs_amount() {
		
		unused_count = unused_count == null ? 0D : unused_count;
		used_count = used_count == null ? 0D : used_count;
		
		return unused_count + used_count;
	}

	public Float getActivated_rate() {
		
		activated =  activated == null ? 0 : activated;
		if(card_count == 0) {
			return 0F;
		}
		return MathUtils.round(activated / (card_count * 0.1F) * 100, 2).floatValue();
	}
	
	
	
}
