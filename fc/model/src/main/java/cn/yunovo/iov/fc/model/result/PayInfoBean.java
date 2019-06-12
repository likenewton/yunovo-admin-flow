package cn.yunovo.iov.fc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "PayInfoBean对象", description = "支付信息bean")
public class PayInfoBean {

	@ApiModelProperty("类型")
	private String type;
	
	@ApiModelProperty("链接地址")
	private String link_url;
	
	@ApiModelProperty("表单地址")
	private String form_url;
	
	@ApiModelProperty("图标地址")
	private String icon_url;
	
	@ApiModelProperty("描述")
	private String name;
	
	@ApiModelProperty("是否安装,true 表示已安装")
	private Boolean is_install;
	
}
