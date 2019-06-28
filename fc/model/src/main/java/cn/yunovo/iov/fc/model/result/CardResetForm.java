package cn.yunovo.iov.fc.model.result;

import javax.validation.constraints.NotEmpty;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
@ApiModel(value = "CardResetForm对象", description = "业务管理-流量卡重置bean")
public class CardResetForm extends BaseForm{

	@NotEmpty(message="请输入您要重置的流量卡iccid")
	private String iccids;
}
