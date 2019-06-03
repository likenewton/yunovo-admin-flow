package cn.yunovo.iov.fc.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PageForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
     * SQL ASC 字段
     */
	@ApiModelProperty(value="需要升序排序的字段")
    private String[] ascs;
    /**
     * SQL  DESC 字段
     */
	@ApiModelProperty(value="需要降序排序的字段")
    private String[] descs;
    
    /**
     * 每页显示记录数
     */
	@ApiModelProperty(value="每页大少")
    private Integer size = 50;
    
    /**
     * 当前页码
     */
	@ApiModelProperty(value="当前页码")
    private Integer current = 1;
	
}
