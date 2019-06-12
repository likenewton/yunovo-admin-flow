package cn.yunovo.iov.fc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设置表
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "Cc_setting对象", description = "设置表")
@TableName(value = "cc_setting")
public class CcSetting implements Serializable {
	
	

	public CcSetting(String group, String key, String value) {
		super();
		this.group = group;
		this.key = key;
		this.value = value;
	}

	private static final long serialVersionUID = 1L;

	@TableId(value = "setting_id", type = IdType.AUTO)
	private Integer setting_id;

	private Integer store_id = 0;

	private String group;

	private String key;

	private String value;

	private Integer serialized = 0;

}
