package cn.yunovo.iov.fc.model;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResourcesBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("资源id")
	private String id;
	
	@ApiModelProperty("资源代码")
	private String rescCode;
	
	@ApiModelProperty("资源类型")
	private String rescType;
	
	@ApiModelProperty("资源名称")
	private String resName;
	
	@ApiModelProperty("资源地址")
	private String resUrl;
	
	@ApiModelProperty("父资源id")
	private String supperResId;
	
	@ApiModelProperty("排序")
	private Integer seqNum;
	
	@ApiModelProperty("图标")
	private String iconUrl;
	
	
	private Integer level;
	
	@ApiModelProperty("子节点")
	private List<ResourcesBean> childResources;
	
}
