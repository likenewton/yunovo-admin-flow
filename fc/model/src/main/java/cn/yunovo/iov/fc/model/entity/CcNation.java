package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 国家区域表
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_nation对象", description = "国家区域表")
public class CcNation implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自动编号")
	@TableId(value = "ntid", type = IdType.AUTO)
	private Integer ntid;

	@ApiModelProperty(value = "父结点")
	private Integer parent;

	@ApiModelProperty(value = "省份")
	private String ntname;

	@ApiModelProperty(value = "邮政编码")
	private Integer zipcode;

}
