package cn.yunovo.iov.fc.model;

import java.util.Map;

import lombok.Data;

@Data
public class FcProperties {

	private CdpService cdp = new CdpService();
	
	private String file_server_url = "";
	
	private String file_dir_root = "";
	
	@Data
	public static class CdpService{
		
		private String isUrl = "";
		
		private String lsUrl = "";
	}
	
	private Map<Short, String> arr_allot_reset;
	
	private Map<Short, String> arr_pay_status;
 	
}
