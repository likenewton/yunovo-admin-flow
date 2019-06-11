package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class NationForm extends BaseForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "自动编号")
	private Integer ntid;

	@ApiModelProperty(value = "父结点")
	private Integer parent = 0;

	@ApiModelProperty(value = "省份")
	private String ntname;

	@ApiModelProperty(value = "邮政编码")
	private Integer zipcode;
	
	@ApiModelProperty(value = "需要被删除的ntid")
	private Integer[] ntids;

}
