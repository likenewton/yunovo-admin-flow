package cn.yunovo.iov.fc.model.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

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
@ApiModel(value = "CurrencyForm对象", description = "货币表单")
public class CurrencyForm extends BaseForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="货币id")
	@NotNull(message="请选择您要处理的货币",groups= {UpdateGroupValidate.class, DeleteGroupValidate.class})
	private Integer currency_id;

	@ApiModelProperty(value="货币名称")
	@NotEmpty(message="请输入货币名称", groups= {InsertGroupValidate.class, UpdateGroupValidate.class})
	@Size(max=32,min=3,groups= {InsertGroupValidate.class, UpdateGroupValidate.class},message="货币名称必须在3至32个字符之间！")
	private String title = "";

	@ApiModelProperty(value="货币代码")
	@NotEmpty(message="请输入货币代码", groups= {InsertGroupValidate.class, UpdateGroupValidate.class})
	@Size(max=3,min=3,groups= {InsertGroupValidate.class, UpdateGroupValidate.class},message="货币代码必须是3个字符！")
	private String code = "";

	@ApiModelProperty(value="左符号")
	private String symbol_left = "";

	@ApiModelProperty(value="右符号")
	private String symbol_right = "";

	@ApiModelProperty(value="小数位")
	private Character decimal_place;

	@ApiModelProperty(value="汇率")
	private Float value = 0F;

	@ApiModelProperty(value="状态")
	private Short status = 1;
	
	/*@ApiModelProperty(value="需要被删除的id数组")
	@NotEmpty(message="系统系统,请选择您需要删除的货币", groups=DeleteGroupValidate.class)
	private Integer[] ids;*/
	
}
