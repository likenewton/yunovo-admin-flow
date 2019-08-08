package cn.yunovo.iov.fc.api.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.api.form.ApiCommonParamsForm;
import cn.yunovo.iov.fc.api.model.AppKey;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.service.ICcOrgService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FcApiSignatureFilter implements Filter{

	private WebApplicationContext wac;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		wac = (WebApplicationContext) filterConfig.getServletContext()
				.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//chain.doFilter(request, response);
		Result<Object> errResult = null;
		HttpServletRequest _request = (HttpServletRequest) request;
		//必输参数校验
		ApiCommonParamsForm form = null;
		try {
			form = this.verifyRequired(_request);
		} catch (FormValidateException e) {
			log.warn("[AppApiFilter][参数校验]params={},exception={}", e.getParams(), ExceptionUtils.getStackTrace(e));
			errResult = ResultUtil.build(400, e.getMessage());
			returnJson(errResult, response);
			return;
		}
		
		//partner_id 校验
		CcOrg org = null;
		try {
			org = this.getOrgByPartnerid(form.getPartner_id());
		} catch (Exception e1) {
			log.error("[AppApiFilter][exception]params={},exception={}", form.buildJsonString(), ExceptionUtils.getStackTrace(e1));
			errResult = ResultUtil.exception();
			returnJson(errResult, response);
			return;
		}
		
		if(org == null) {
			log.warn("[AppApiFilter][invalid partner_id]params={}", form.buildJsonString());
			errResult = ResultUtil.build(400, "invalid partner_id");
			returnJson(errResult, response);
			return;
		}
		//api key 信息校验
		AppKey appkey = null;
		
		try {
			appkey = getAppKey(form.getApi_key());
		} catch (Exception e) {
			log.error("[AppApiFilter][exception]params={},exception={}", form.buildJsonString(), ExceptionUtils.getStackTrace(e));
			errResult = ResultUtil.exception();
			returnJson(errResult, response);
			return;
		}
		
		
		
		if(appkey == null) {
			log.warn("[AppApiFilter][invalid api_key]params={}", form.buildJsonString());
			errResult = ResultUtil.build(400, "invalid api_key");
			returnJson(errResult, response);
			return;
		}
		
		//拼装api_sigin 数据，并MD5
		
		String beforeSign = this.assembly(_request, appkey);
		String algorithm = appkey.getGenerativeAlgorithm();
		String afterSign="";
		
		
		//签名比较
		try {
			if(StringUtils.isBlank(algorithm)|| "MD5".equalsIgnoreCase(algorithm)) {
				afterSign = DigestUtils.md5Hex(beforeSign);
			}else if("SHA1".equalsIgnoreCase(algorithm)){
				afterSign = DigestUtils.sha1Hex(beforeSign);
			}else if("SHA256".equalsIgnoreCase(algorithm)){
				afterSign = DigestUtils.sha256Hex(beforeSign);
			}else if("SHA512".equalsIgnoreCase(algorithm)){
				afterSign = DigestUtils.sha512Hex(beforeSign);
			}

			if (form.getApi_sign().equalsIgnoreCase(afterSign)) {
				chain.doFilter(request, response);
			} else {
				
				log.warn("[AppApiFilter][invalid api_sign]params={}", form.buildJsonString());
				errResult = ResultUtil.build(401, "invalid api_sign");
				returnJson(errResult, response);
				return;
			}
		} catch (Exception e) {
			
			log.error("[AppApiFilter][exception]params={},exception={}", form.buildJsonString(), ExceptionUtils.getStackTrace(e));
			errResult = ResultUtil.exception();
			returnJson(errResult, response);
			return;
		}
		
		
	}

	/**
	 * 必输参数校验
	 * @param _request
	 * @return 
	 */
	private ApiCommonParamsForm verifyRequired(HttpServletRequest request) {
		
		ApiCommonParamsForm form = new ApiCommonParamsForm();
		form.setApi_key(request.getParameter("api_key"));
		form.setPartner_id(request.getParameter("partner_id"));
		form.setApp_name(request.getParameter("app_name"));
		form.setApp_ver(request.getParameter("app_ver"));
		form.setRom_ver(request.getParameter("rom_ver"));
		form.setPlatform(request.getParameter("platform"));
		form.setImei(request.getParameter("imei"));
		form.setImsi(request.getParameter("imsi"));
		form.setTimestamp(request.getParameter("timestamp"));
		form.setApi_sign(request.getParameter("api_sign"));
		
		form.validate();
		return form;
		
	}
	
	
	/**
	 * 获取appkey信息
	 * @param api_key
	 * @return
	 */
	public AppKey getAppKey(String api_key) {
		
		JedisPoolUtil jedisPoolUtil = wac.getBean(JedisPoolUtil.class);
		List<String> data = jedisPoolUtil.hmget("appKeyAndAppSecret", api_key);
		
		if(CollectionUtils.isEmpty(data) || StringUtils.isEmpty(data.get(0))) {
			return null;
		}
		
		String app_str = data.get(0);
		
		return JSONObject.parseObject(app_str, AppKey.class);
	}
	
	public CcOrg getOrgByPartnerid(String partner_id) {
		
		ICcOrgService orgService = wac.getBean(ICcOrgService.class);
		
		return orgService.getByPartnerId(partner_id);
	}
	

	/**
	 * 拼凑验签字符串,以便跟客户端进行比较
	 * @param _request
	 * @param appKey
	 * @return
	 */
	public String assembly(HttpServletRequest request, AppKey appKey) {
		
		Enumeration<String> parameters = request.getParameterNames();
		Map<String, String> parameterMap = new TreeMap<String, String>();
		String api_secret = appKey.getAppSecret();
		parameterMap.put("api_secret", api_secret);
		while (parameters.hasMoreElements()) {
			String key = (String) parameters.nextElement();
			if (!"api_sign".equalsIgnoreCase(key)) {
				parameterMap.put(key, request.getParameter(key));
			}
		}
		Iterator<String> it = parameterMap.keySet().iterator();
		String beforeSign = "";
		while (it.hasNext()) {
			String key = it.next().toString();
			if (StringUtils.isBlank(beforeSign)) {
				beforeSign = key + "=" + parameterMap.get(key);
			} else {
				if ("api_secret".equalsIgnoreCase(key)) {
					beforeSign += ";" + key + "=" + api_secret;
				} else {
					beforeSign += ";" + key + "=" + parameterMap.get(key);
				}
			}

		}
		
		return beforeSign;
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
