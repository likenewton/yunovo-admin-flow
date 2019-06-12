package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 货币表
 * </p>
 *
 * @author bill
 * @since 2019-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_currency对象", description = "货币表")
@TableName("cc_currency")
public class CcCurrency implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "currency_id", type = IdType.AUTO)
	@ApiModelProperty(value="货币id")
	private Integer currency_id;

	@ApiModelProperty(value="货币名称")
	private String title = "";

	@ApiModelProperty(value="货币代码")
	private String code = "";

	@ApiModelProperty(value="左符号")
	private String symbol_left = "";

	@ApiModelProperty(value="右符号")
	private String symbol_right = "";

	@ApiModelProperty(value="小数位")
	private String decimal_place;

	@ApiModelProperty(value="汇率")
	private Float value = 0F;

	@ApiModelProperty(value="状态")
	private Short status = 1;

	@ApiModelProperty(value="最近更新时间")
	private String date_modified;
	
	@ApiModelProperty(value="是否默认货币,true 表示默认")
	private Boolean is_default = false;

}
