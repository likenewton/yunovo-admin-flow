package cn.yunovo.iov.fc.web.controller;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.cas.client.authentication.H5ClientAuthenticationFilter;
import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {

	public Assertion getAssertion() {

		return (Assertion) WebRequestUtil.getAttribute(H5ClientAuthenticationFilter.CONST_CAS_ASSERTION);
	}
	
	@ExceptionHandler(value=Exception.class)
	public Result<?> exception(Exception e) {
		
		log.error("[exception][exception]params={},exception={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()), ExceptionUtils.getStackTrace(e));
		return ResultUtil.exception();
	}
	
	@ExceptionHandler(value=BusinessException.class)
	public Result<String> businessException(BusinessException e) {
		
		log.error("[businessException][exception]params={},exception={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()), ExceptionUtils.getStackTrace(e));
		return ResultUtil.build(e.getException_code(), e.getMessage());
	}
	
	@ExceptionHandler(value=FormValidateException.class)
	public Result<?> formValidateException(FormValidateException e) {
		
		log.error("[formValidateException][exception]params={},exception={}", e.getParams(), ExceptionUtils.getStackTrace(e));
		return ResultUtil.build(e.getCode(), e.getMessage());
	}
	
	@ExceptionHandler(value=HttpMessageNotReadableException.class)
	public Result<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		
		log.error("[httpMessageNotReadableException][exception]params={},exception={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()), ExceptionUtils.getStackTrace(e));
		return ResultUtil.build(400, "请求参数有误");
	}
	
	

	public LoginInfo getLoginBaseInfo() {

		Assertion assertion = this.getAssertion();

		Map<String, Object> attr = assertion.getPrincipal().getAttributes();
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginName(String.valueOf(attr.get("loginName")));
		loginInfo.setUserName(String.valueOf(attr.get("userName")));
		loginInfo.setId(NumberUtils.createInteger(String.valueOf(attr.get("id"))));
		loginInfo.setOrganCode(String.valueOf(attr.get("organCode")));
		return loginInfo;
	}
}
