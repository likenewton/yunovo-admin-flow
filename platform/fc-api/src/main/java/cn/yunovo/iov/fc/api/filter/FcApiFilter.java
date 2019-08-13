package cn.yunovo.iov.fc.api.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.common.utils.Md5Util;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcOrgService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FcApiFilter implements Filter{

	private WebApplicationContext wac;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		//获取WebApplicationContext
		wac = (WebApplicationContext) filterConfig.getServletContext()
				.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//chain.doFilter(request, response);
		Result<Object> errResult = null;
		HttpServletRequest _request = (HttpServletRequest) request;
		
		Long currentTime = System.currentTimeMillis() / 1000;
		String token = _request.getParameter("token");
		
		if(StringUtils.isEmpty(token)) {
			log.warn("[FcApiFilter][invalid token]params={}", JSONObject.toJSONString(request.getParameterMap()));
			errResult = ResultUtil.build(0, "token error");
			returnJson(errResult, response);
			return;
		}
		
		// 校验时间戳为系统当前时间前后15分钟以内才有效
		Long timestamp = StringUtils.isEmpty(_request.getParameter("timestamp")) ? currentTime : NumberUtils.createLong(_request.getParameter("timestamp"));
		if(timestamp < (currentTime - (60 * 15)) || timestamp > (currentTime + 60 * 15)) {
			
			log.warn("[FcApiFilter][timestamp error]params={}", JSONObject.toJSONString(request.getParameterMap()));
			errResult = ResultUtil.build(0, "timestamp error");
			returnJson(errResult, response);
			return;
		}
		
		 //所属机构校验
		String partner_id = StringUtils.trim(_request.getParameter("partner_id"));
		if(StringUtils.isEmpty(partner_id)) {
			
			log.warn("[FcApiFilter][partner_id error]params={}", JSONObject.toJSONString(request.getParameterMap()));
			errResult = ResultUtil.build(0, "partner_id error");
			returnJson(errResult, response);
			return;
		}
		
		//partner_id 校验
		CcOrg org = null;
		try {
			org = this.getOrgByPartnerid(partner_id);
		} catch (Exception e1) {
			log.error("[AppApiFilter][exception]params={},exception={}", JSONObject.toJSONString(request.getParameterMap()), ExceptionUtils.getStackTrace(e1));
			errResult = ResultUtil.exception();
			returnJson(errResult, response);
			return;
		}
		if(org == null) {
			log.warn("[AppApiFilter][invalid partner_id]params={}", JSONObject.toJSONString(request.getParameterMap()));
			errResult = ResultUtil.build(0, "partner_id error");
			returnJson(errResult, response);
			return;
		}
		
		//校验密码安全
		String vtoken = Md5Util.getMD5String(org.getPartner_key()+DateFormatUtils.format(currentTime *1000, "ddHHyyyyMM"));
		if(!StringUtils.equals(token, vtoken)) {
			log.warn("[FcApiFilter][token error]params={}", JSONObject.toJSONString(request.getParameterMap()));
			errResult = ResultUtil.build(0, "token error");
			returnJson(errResult, response);
			return;
		}
		
		chain.doFilter(request, response);
		
	}
	
	private CcOrg getOrgByPartnerid(String partner_id) {
		
		ICcOrgService orgService = wac.getBean(ICcOrgService.class);
		return orgService.getByPartnerId(partner_id);
	}

	private void returnJson(Result<Object> message, ServletResponse response) throws IOException {
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		response.setContentType("text/html;charset=utf-8");
		httpResponse.setCharacterEncoding("UTF-8");
		PrintWriter out = httpResponse.getWriter();
		out.write(JSONObject.toJSONString(message));
		out.flush();

	}
	
}
