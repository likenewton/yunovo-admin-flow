package cn.yunovo.iov.fc.service.manage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.model.CdpProperties;

@Service
public class ZhiWangApiService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CdpProperties cdpProperties;
	
	private final String GET_Terminal_Details_URL = "/rest/api/zhiwang/getTerminalDetails";
	private final String CARD_ON_OFF_URL = "/rest/api/zhiwang/cardOnoff";
	
	public JSONObject getTerminalDetails(String card_sn) {
		
		if(card_sn == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("serialNumber", card_sn);
		
		HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(body, headers);
		
		String data = restTemplate.postForObject(cdpProperties.getIsUrl()+GET_Terminal_Details_URL, formEntity, String.class);
		
		return StringUtils.isEmpty(data) ? null : JSONObject.parseObject(data);
	}
	
	/**
	 * 开卡或停卡
	 * @param card_sn 卡号码,gprs.cc_gprs_card.card_sn
	 * @param opFlag 开关：0为开卡，1为停卡
	 * @return
	 */
	public JSONObject cardOnoff(String card_sn, Integer opFlag) {
		
		if(card_sn == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("serialNumber", String.valueOf(card_sn));
		body.add("opFlag", String.valueOf(opFlag));
		
		HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(body, headers);
		
		String data = restTemplate.postForObject(cdpProperties.getIsUrl()+CARD_ON_OFF_URL, formEntity, String.class);
		
		return StringUtils.isEmpty(data) ? null : JSONObject.parseObject(data);
	}
	
	public boolean isSuccess(JSONObject data) {
		
		if(data == null || data.isEmpty()) {
			
			return false;
		}
		
		return StringUtils.equals(data.getString("status"), "1");
	}
	
}
