package cn.yunovo.iov.fc.ant.currency.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
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
public class Cc_currency implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "currency_id", type = IdType.AUTO)
	private Integer currency_id;

	private String title;

	private String code;

	private String symbol_left;

	private String symbol_right;

	private String decimal_place;

	private Float value;

	private Short status;

	private String date_modified;

}
