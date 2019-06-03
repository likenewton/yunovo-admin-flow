package cn.yunovo.iov.fc.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.http.MediaType;

import cn.yunovo.iov.cas.client.configuration.SpringCasProperties;
import cn.yunovo.iov.cas.client.constant.CasConstant;
import cn.yunovo.iov.cas.client.util.CasClientUtil;
import cn.yunovo.iov.cas.client.util.IgnoreOperatorUtils;
import cn.yunovo.iov.cas.client.util.TokenUtil;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class H5LoginUserAdapterFilter implements javax.servlet.Filter {

	private final static String LOGIN_NAME = "loginName";
	private final static String LOGIN_PASSWORD = "loginPassword";

	private SpringCasProperties springCasProperties;
	
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
			Assertion object = CasClientUtil.getCasJedisPoolUtil().get(AbstractCasFilter.CONST_CAS_ASSERTION + "#" + token, AssertionImpl.class);
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
			
			chain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
