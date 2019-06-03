package cn.yunovo.iov.fc.web.controller;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.cas.client.authentication.H5ClientAuthenticationFilter;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.model.LoginInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {

	public Assertion getAssertion() {

		return (Assertion) WebRequestUtil.getAttribute(H5ClientAuthenticationFilter.CONST_CAS_ASSERTION);
	}
	
	@ExceptionHandler(value=Exception.class)
	public Result<?> exception(Exception e) {
		
		log.error("[][exception]params={},exception={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()), ExceptionUtils.getStackTrace(e));
		return ResultUtil.exception();
	}

	public LoginInfo getLoginBaseInfo() {

		Assertion assertion = this.getAssertion();

		Map<String, Object> attr = assertion.getPrincipal().getAttributes();
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginName(String.valueOf(attr.get("loginName")));
		loginInfo.setUserName(String.valueOf(attr.get("userName")));
		return loginInfo;
	}
}
