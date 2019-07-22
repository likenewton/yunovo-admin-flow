package cn.yunovo.iov.fc.service.manage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.model.CdpProperties;
import cn.yunovo.iov.fc.model.form.unicom.EditNetworkAccessConfigRequestForm;
import cn.yunovo.iov.fc.model.form.unicom.EditTerminalRequestForm;
import cn.yunovo.iov.fc.model.form.unicom.GetTerminalDetailsRequestForm;

@Service
public class M2mService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CdpProperties cdpProperties;
	
	private final String GET_Terminal_Details_URL = "/rest/api/jasper/terminal/getTerminalDetails";
	private final String Edit_Terminal_URL = "/rest/api/jasper/terminal/editTerminal";
	private final String Edit_Network_Access_Config_URL = "/rest/api/jasper/terminal/editTerminal";
	
	
	public JSONObject getTerminalDetails(Integer card_type, String messageId, String version, String ...iccids) {
		
		if(card_type == null || iccids == null || iccids.length < 1) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		GetTerminalDetailsRequestForm form = new GetTerminalDetailsRequestForm();
		form.setCard_type(card_type);
		form.setMessageId(messageId);
		form.setVersion(version);
		form.setIccids(iccids);
		HttpEntity<String> formEntity = new HttpEntity<>(JSONObject.toJSONString(form), headers);
		String data = restTemplate.postForObject(cdpProperties.getIsUrl()+GET_Terminal_Details_URL, formEntity, String.class);
		
		return JSONObject.parseObject(data);
	}
	
	public JSONObject editTerminal(Integer card_type, String messageId, String version, String iccid, Integer changeType, String targetValue) {
		
		if(card_type == null || StringUtils.isEmpty(iccid) || changeType == null || StringUtils.isEmpty(targetValue)) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		EditTerminalRequestForm form = new EditTerminalRequestForm();
		form.setCard_type(card_type);
		form.setMessageId(messageId);
		form.setVersion(version);
		form.setChangeType(changeType);
		form.setIccid(iccid);
		form.setTargetValue(targetValue);
		HttpEntity<String> formEntity = new HttpEntity<>(JSONObject.toJSONString(form), headers);
		String data = restTemplate.postForObject(cdpProperties.getIsUrl()+Edit_Terminal_URL, formEntity, String.class);
		
		return JSONObject.parseObject(data);
	}
	
	/**
	 * @param card_type
	 * @param messageId
	 * @param version
	 * @param iccid
	 * @param nacid 通信计划编号 (PNL:公网不限速，PL:公网限速，NVNL:无视频不限速，NVL:无视频限速)
	 * @return
	 */
	public JSONObject editNetworkAccessConfig(Integer card_type, String messageId, String version, String iccid, String nacid) {
		
		if(card_type == null || StringUtils.isEmpty(nacid) || StringUtils.isEmpty(iccid)) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		EditNetworkAccessConfigRequestForm form = new EditNetworkAccessConfigRequestForm();
		form.setCard_type(card_type);
		form.setMessageId(messageId);
		form.setVersion(version);
		form.setIccid(iccid);
		form.setNacid(nacid);
		HttpEntity<String> formEntity = new HttpEntity<>(JSONObject.toJSONString(form), headers);
		String data = restTemplate.postForObject(cdpProperties.getIsUrl()+Edit_Network_Access_Config_URL, formEntity, String.class);
		
		return JSONObject.parseObject(data);
	}

	public boolean isSuccess(JSONObject data) {
		
		if(data == null || data.isEmpty()) {
			
			return false;
		}
		
		return StringUtils.equals(data.getString("code"), "0");
	}
	
}
