package cn.yunovo.iov.fc.model;

import lombok.Data;

@Data
public class FcProperties {

	private CdpService cdp = new CdpService();
	
	
	@Data
	public static class CdpService{
		
		private String isUrl = "";
		
		private String lsUrl = "";
	}
	
}
