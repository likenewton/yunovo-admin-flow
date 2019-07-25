package cn.yunovo.iov.fc.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.model.form.api.WhitelistsReportForm;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/fc/api")
public class ApkWhitelistController extends BaseController{

	
	
	@RequestMapping(path="/whitelists/report", method=RequestMethod.POST, produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public Result<Object> report(WhitelistsReportForm form){
		
		//表单校验
		form.validate(); 
		
		//判断流量卡是否我司流量卡
		
		
		
		//判断nonce 是否有效
		
		return null;
	}
	
}
