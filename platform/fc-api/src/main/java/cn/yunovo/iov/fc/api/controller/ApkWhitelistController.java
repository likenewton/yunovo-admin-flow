package cn.yunovo.iov.fc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.form.api.WhitelistsReportForm;
import cn.yunovo.iov.fc.service.ICcApkWhitelistUsedReportInfoService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/fc/api")
public class ApkWhitelistController extends BaseController{

	@Autowired
	private ICcApkWhitelistUsedReportInfoService iCcApkWhitelistUsedReportInfoService;
	
	
	@RequestMapping(path="/whitelists/report", method=RequestMethod.POST, produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public Result<Object> report(WhitelistsReportForm form){
		
		//表单校验
		form.validate();
		iCcApkWhitelistUsedReportInfoService.report(form);
		return ResultUtil.build(0, "OK");
	}
	
}
