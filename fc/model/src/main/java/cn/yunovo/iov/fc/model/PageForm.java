package cn.yunovo.iov.fc.model;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
	
	public <T> Page<T> build(Class<T> clazz,String defaultAsc, String defaultDesc){
		
		Page<T> page = new Page<>();
		if(ArrayUtils.isEmpty(this.getAscs()) && ArrayUtils.isEmpty(this.getDescs())) {
			
			page.setAsc(defaultAsc);
			page.setDesc(defaultDesc);
		}else {
			
			page.setDesc(this.getDescs());
			page.setAsc(this.getAscs());
		}
		
		page.setCurrent(this.getCurrent());
		page.setSize(this.getSize());
		
		return page;
	}
	
}
