package cn.yunovo.iov.fc.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.http.MediaType;

import cn.yunovo.iov.cas.client.configuration.SpringCasProperties;
import cn.yunovo.iov.cas.client.util.IgnoreOperatorUtils;
import cn.yunovo.iov.cas.client.util.TokenUtil;
import cn.yunovo.iov.fc.service.ISystemResourceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiSecurityFilter implements javax.servlet.Filter {

	private SpringCasProperties springCasProperties;
	
	private ISystemResourceService iSystemResourceService;
	
	private final String ERROR_MSG = "{\"status\":403, \"msg\":\"您暂未分配该功能使用权限,如需使用,请联系管理员进行权限分配\"}";
	
	public SpringCasProperties getSpringCasProperties() {
		return springCasProperties;
	}

	public void setSpringCasProperties(SpringCasProperties springCasProperties) {
		this.springCasProperties = springCasProperties;
	}
	
	public ISystemResourceService getiSystemResourceService() {
		return iSystemResourceService;
	}

	public void setiSystemResourceService(ISystemResourceService iSystemResourceService) {
		this.iSystemResourceService = iSystemResourceService;
	}
	
	private final String NO_FILTER_URL = "*/api/sso/*,*/api/select/*";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			final String requestUri = httpRequest.getRequestURI();
	        String exludes = this.getSpringCasProperties().getCasClient().getExludeFilterResource();// CasConstant.getParamValue(CasConstant.EXLUDE_FILTER_RESOURCE);
	        boolean flag = IgnoreOperatorUtils.ignore(requestUri, exludes);

	        if (flag) {
	            chain.doFilter(request, response);
	            return;
	        }
	        
	        if(IgnoreOperatorUtils.ignore(requestUri, NO_FILTER_URL)) {
	        	 chain.doFilter(request, response);
		         return;
	        }
			
			String token = TokenUtil.getToken(httpRequest);
			// 获取Cas Session中的Assertion
			Assertion object = null;
//			CasClientUtil.getCasJedisPoolUtil().get(AbstractCasFilter.CONST_CAS_ASSERTION + "#" + token, AssertionImpl.class);
			object = request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) != null ? (Assertion)request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : null;
			
			Map<String, Object> map = object.getPrincipal().getAttributes();
			
			String user_id = String.valueOf(map.get("id"));
			
			if(iSystemResourceService.isPermission(httpRequest.getRequestURI(), token, user_id)) {
				chain.doFilter(request, response);
				return ;
			}else {
				this.sendRedirect(httpRequest, httpResponse);
				return;
			}
			
		} catch (Exception e) {
			log.error("[ApiSecurityFilter][exception]exception={}", ExceptionUtils.getStackTrace(e));
		} 
	}
	
	private String getServiceParameterName() {
		return this.getSpringCasProperties().getCasClient().getClientAuth().getServiceParameterName();
	}
	
	/**
	 * 错误跳转
	 * 
	 * @Description
	 * @param httpResponse
	 * @param url
	 *            跳转的URL
	 * @throws IOException
	 */
	private void sendRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);// 解决中文乱码
        try {
            PrintWriter writer = response.getWriter();
            writer.write(ERROR_MSG);
            writer.flush();
            writer.close();
        } catch (Exception e) {
        	log.error("[sendRedirect][exception]exception={}", ExceptionUtils.getStackTrace(e));
		}
		// 跳转到重新登录页面
	}

}
