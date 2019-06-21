package cn.yunovo.iov.fc.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class GprsCalculateBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double month;
	
	private Double total;
	
	private Boolean is_unicom;
	
	private Boolean open_card;
	
	private Integer this_month;
}
