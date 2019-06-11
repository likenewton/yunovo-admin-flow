package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.regex.Matcher;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class OrgForm  extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("机构id(修改时该参数被传)")
	@NotNull(message="系统系统：请选择您要修改的机构", groups=UpdateGroupValidate.class)
	private Integer org_id;

	@Size(max=100, min=1, message="机构名必须在1至100个字符之间！", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	@NotEmpty(message="请输入机构名称", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	@ApiModelProperty("机构名称")
	private String name; 
	
	@NotNull(message="请选择所属机构", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	@ApiModelProperty("所属父机构id")
	private Integer parent_id;
	
	@ApiModelProperty("可开账户数量")
	@NotNull(message="请填写可开账户数量", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	@Min(message="请输入正确的账户数量", value=0, groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	private Integer user_total;
	
	@ApiModelProperty("异步通知地址")
	@NotEmpty(message="请填写异步通知地址", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	@URL(regexp="(^$)|(^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+(\\\\?{0,1}(([A-Za-z0-9-~]+\\\\={0,1})([A-Za-z0-9-~]*)\\\\&{0,1})*)$)",message="请输入正确的异步通知地址！", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	private String notify_url;
	
	@ApiModelProperty("机构描述")
	@Size(max=255, min=0, message="机构备注必须在1至255个字符之间！", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	private String memo; 
	
	@Email(regexp="(^$)|(^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$)", groups= {UpdateGroupValidate.class,InsertGroupValidate.class}, message="请输入正确的邮箱！")
	@ApiModelProperty("负责人邮箱")
	private String email;
	
	@ApiModelProperty("负责人手机")
	@Pattern(regexp="(^$)|(^(\\\\+86)?1[3,4,5,6,7,8,9](\\\\d{9})$)", message="请输入正确的手机号码！", groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	private String tel;
	
	@ApiModelProperty("返利比率")
	@DecimalMax(value="0.9999", message="无效的返利比率,值需小于1大于等于0",  groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	@Min(value=0 ,groups= {UpdateGroupValidate.class,InsertGroupValidate.class})
	private Float rebate_value = 0F;
	
	@ApiModelProperty("需要被删的机构id")
	@NotEmpty(message="系统提示：请选择您需要删除的机构", groups=DeleteGroupValidate.class)
	private Integer[] orgs;
	
	public static void main(String[] args) {
		
		boolean isMatch = java.util.regex.Pattern.matches("(^$)|(^(\\+86)?1[3,4,5,6,7,8,9](\\d{9})$)", "18888888888");
		System.out.println(isMatch);
		
	}

}
