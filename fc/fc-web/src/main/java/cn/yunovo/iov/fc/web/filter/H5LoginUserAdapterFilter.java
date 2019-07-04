package cn.yunovo.iov.fc.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import cn.yunovo.iov.cas.client.configuration.SpringCasProperties;
import cn.yunovo.iov.cas.client.constant.CasConstant;
import cn.yunovo.iov.cas.client.util.CasClientUtil;
import cn.yunovo.iov.cas.client.util.IgnoreOperatorUtils;
import cn.yunovo.iov.cas.client.util.TokenUtil;
import cn.yunovo.iov.fc.model.ResourcesBean;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcUserService;
import cn.yunovo.iov.fc.service.ISystemResourceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class H5LoginUserAdapterFilter implements javax.servlet.Filter {

	private final static String LOGIN_NAME = "loginName";
	private final static String LOGIN_PASSWORD = "loginPassword";
	
	public final static String USER_RESOURCE_LIST_KEY = "USER_RESOURCE_LIST";
	public final static String NEED_FILTER_RESOURCE_MAP_KEY = "NEED_FILTER_RESOURCE_MAP";
	public final static String USER_RESOURCE_URL_SET_KEY = "USER_RESOURCE_URL_SET";
	
	
	
	private SpringCasProperties springCasProperties;
	
	private ISystemResourceService systemResourceService;

	public ISystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public void setSystemResourceService(ISystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public SpringCasProperties getSpringCasProperties() {
		return springCasProperties;
	}

	public void setSpringCasProperties(SpringCasProperties springCasProperties) {
		this.springCasProperties = springCasProperties;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			final String requestUri = httpRequest.getRequestURI();
	        String exludes = this.getSpringCasProperties().getCasClient().getExludeFilterResource();// CasConstant.getParamValue(CasConstant.EXLUDE_FILTER_RESOURCE);
	        boolean flag = IgnoreOperatorUtils.ignore(requestUri, exludes);

	        if (flag) {
	            log.debug("--- flag is true ---");
	            log.debug("--- ClientAuthenticationFilter end ---\r\n");
	            chain.doFilter(request, response);
	            return;
	        }
			
			String token = TokenUtil.getToken(httpRequest);
			// 获取Cas Session中的Assertion
			Assertion object = null;
//			CasClientUtil.getCasJedisPoolUtil().get(AbstractCasFilter.CONST_CAS_ASSERTION + "#" + token, AssertionImpl.class);
			object = request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) != null ? (Assertion)request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : null;
			if(object == null) {
				
				CommonUtils.constructRedirectUrl(springCasProperties.getCasClient().getCasServerLogoutUrl(), getServiceParameterName(),
	        			this.getSpringCasProperties().getCasClient().getService(), false, false);
				this.sendRedirect(httpRequest, httpResponse);
				return;
			}
			
			
			Map<String, Object> map = object.getPrincipal().getAttributes();
			// 获取cas服务端的登录名称
			String loginName = (String) map.get(LOGIN_NAME);
			// 获取cas服务端的登录密码
			String loginPassword = (String) map.get(LOGIN_PASSWORD);
			//log.info("当前登陆系统：用户中心。登录系统的用户名为：" + loginName);
			
			String user_id = String.valueOf(map.get("id"));
			
			Object temp = map.get(NEED_FILTER_RESOURCE_MAP_KEY);
			Object temp2 = map.get(USER_RESOURCE_LIST_KEY);
			if(temp == null || temp2 == null) {
				
				List<ResourcesBean>  all_resource = systemResourceService.allResourceBySiteCode(FcConstant.SITE_CODE);
				List<ResourcesBean>  user_resource = systemResourceService.allResourceBySiteCodeAndUserid(user_id, FcConstant.SITE_CODE);
				Map<String, String> needFilter = systemResourceService.getNeedFilterResource(all_resource);
				Set<String> userRes = systemResourceService.getUserResUrl(user_resource);
				if(!(CollectionUtils.isEmpty(all_resource) || CollectionUtils.isEmpty(user_resource))) {
					
					map.put(USER_RESOURCE_LIST_KEY, user_resource);
					map.put(NEED_FILTER_RESOURCE_MAP_KEY, needFilter);
					map.put(USER_RESOURCE_URL_SET_KEY, userRes);
					request.setAttribute(AbstractCasFilter.CONST_CAS_ASSERTION, object);
					CasClientUtil.getCasJedisPoolUtil().set(AbstractCasFilter.CONST_CAS_ASSERTION + "#" + token, object);
				}
				
			}
			
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.error("[H5LoginUserAdapterFilter][exception]exception={}", ExceptionUtils.getStackTrace(e));
		} 
	}

	private String getArtifactParameterName() {
		return this.getSpringCasProperties().getCasClient().getClientAuth().getArtifactParameterName();
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
		
		final String requestUri = request.getRequestURI();
		String exludes = springCasProperties.getCasClient().getExludeFilterResource();
        boolean flag = IgnoreOperatorUtils.ignore(requestUri, exludes);
		if(flag) {
            response.sendRedirect(this.getSpringCasProperties().getCasClient().getCasServerLogoutUrl());
		}else {
			String str = "{\"status\":401, \"redirect\":\"http://www.baidu.com\"}";
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);// 解决中文乱码
            try {
                PrintWriter writer = response.getWriter();
                writer.write(str);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
		// 跳转到重新登录页面
	}

	@Override
	public void init(FilterConfig arg0) {
		
	}
}
