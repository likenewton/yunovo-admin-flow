package cn.yunovo.iov.fc.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

@Data
public class PageData<T, K> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Page<T> page;
	
	private K other;
}
