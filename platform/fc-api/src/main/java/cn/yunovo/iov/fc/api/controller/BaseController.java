package cn.yunovo.iov.fc.api.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {


	
	@ExceptionHandler(value=Exception.class)
	public Result<?> exception(Exception e) {
		
		log.error("[exception][exception]params={},exception={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()), ExceptionUtils.getStackTrace(e));
		return ResultUtil.exception();
	}
	
	@ExceptionHandler(value=BusinessException.class)
	public Result<String> businessException(BusinessException e) {
		
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
		return ResultUtil.build(402, "请求参数有误");
	}
	
}
