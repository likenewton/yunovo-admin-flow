package cn.yunovo.iov.fc.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.sunshine.dcda.system.service.model.SystemResourceVo;

import cn.yunovo.iov.cas.client.Resource;
import cn.yunovo.iov.cas.client.authentication.AbstractClientAuthenticationFilter;
import cn.yunovo.iov.cas.client.configuration.SpringCasProperties;
import cn.yunovo.iov.cas.client.constant.CasConstant;
import cn.yunovo.iov.cas.client.util.TokenUtil;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.ResourcesBean;
import cn.yunovo.iov.fc.service.ISystemResourceService;
import cn.yunovo.iov.fc.web.filter.H5LoginUserAdapterFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path="/api/sso")
@Api(value="/api/sso",tags="单点登录接口列表")
public class SsoController extends BaseController{

	@Autowired
	private SpringCasProperties springCasProperties;
	
	@Autowired
	private ISystemResourceService iSystemResourceService;
	
	@GetMapping(path = "/isLogin", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "获取用户登录是否登录", notes="data = true, 表示用户属于已登录状态")
	public Result<Boolean> isLogin() {
		return ResultUtil.build(0, "ok", true);
	}
	
	@RequestMapping(path="/ssoLogin",method= {RequestMethod.GET, RequestMethod.POST})
	public void ssoLogin(HttpServletRequest request, HttpServletResponse response) {
		
		String token = TokenUtil.getToken(request);
		String h5IndexUrl = springCasProperties.getCasClient().getH5IndexUrl();
		try {
			h5IndexUrl = h5IndexUrl + "?"+CasConstant.CLIENT_TOKEN_NAME+"="+URLEncoder.encode(token, "UTF-8");
			response.setHeader("content-type",MediaType.TEXT_HTML_VALUE);
			response.setCharacterEncoding("utf8");
			PrintWriter pw = response.getWriter();
			StringBuilder str = new StringBuilder("");
			str.append(" <!DOCTYPE html>                                                                                                         ");
			str.append(" <html>                                                                                                                  ");
			str.append("                                                                                                                         ");
			str.append(" <head>                                                                                                                  ");
			str.append("     <title>云智易联</title>                                                                                   		 ");
			str.append("     <meta charset=UTF-8>                                                                                                ");
			str.append("     <meta http-equiv=cache-control content=no-cache>                                                                    ");
			str.append("     <meta name=keywords content=test>                                                                                   ");
			str.append("     <meta name=description content=深圳云智易联>                                                                      		  ");
			str.append("     <meta name=msapplication-tap-highlight content=no>                                                                  ");
			str.append("     <meta name=renderer content=webkit>                                                                                 ");
			str.append("     <meta name=screen-orientation content=portrait>                                                                     ");
			str.append("     <meta name=x5-orientation content=portrait>                                                                         ");
			str.append("     <meta name=browsermode content=application>                                                                         ");
			str.append("     <meta name=x5-page-mode content=app>                                                                                ");
			str.append("     <meta http-equiv=X-UA-Compatible content=\"IE=edge,chrome=1\">                                                      ");
			str.append("     <meta name=viewport content=\"initial-scale=1,width=device-width,maximum-scale=1,user-scalable=no\">                ");
			str.append("     <meta name=format-detection content=\"telephone=no, email=no\">                                                     ");
			str.append("     <meta name=apple-mobile-web-app-capable content=yes>                                                                ");
			str.append("     <meta name=x5-fullscreen content=true>                                                                              ");
			str.append("     <meta name=full-screen content=yes>                                                                                 ");
			str.append("                                                                                                                         ");
			str.append(" </head>                                                                                                                 ");
			str.append("                                                                                                                         ");
			str.append(" <body class=small-scroll>                                                                                               ");
			str.append("     <script >                                                                                                           ");
			str.append("         window.location.href = '"+h5IndexUrl+"';                                					                     ");
			str.append("     </script>                                                                                                           ");
			str.append("                                                                                                                         ");
			str.append(" </body>                                                                                                                 ");
			str.append(" </html>                                                                                                                 ");
						
			pw.write(str.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 获取登录者信息
	 * @return
	 */
	@ApiOperation(notes="获取当前用户信息", value = "获取当前用户信息")
	@GetMapping(path = "/getLoginInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<LoginInfo> getLoginInfo() {
		
		LoginInfo loginInfo = this.getLoginBaseInfo();
		loginInfo.setLogoutUrl(springCasProperties.getCasClient().getCasServerLogoutUrl());
		loginInfo.setUcIndexUrl(springCasProperties.getCasClient().getUcIndexUrl());
		return ResultUtil.build(0, "ok", loginInfo);
		
	}
	

	/**
	 * 获取登录菜单信息
	 * @return
	 */
	@ApiOperation(notes="获取当前用户菜单列表", value = "获取当前用户菜单列表")
	@GetMapping(path = "/menus", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<List<ResourcesBean>> menus() {
		
		Assertion assertion = this.getAssertion();
		if(assertion == null) {
			return ResultUtil.success(null);
		}
		Map<String, Object> attrs = assertion.getPrincipal().getAttributes();
		
		List<ResourcesBean> res = attrs.get(H5LoginUserAdapterFilter.USER_RESOURCE_LIST_KEY) == null ? null : (List<ResourcesBean>)attrs.get(H5LoginUserAdapterFilter.USER_RESOURCE_LIST_KEY);
		if(CollectionUtils.isEmpty(res)) {
			return ResultUtil.success(null);
		}
		
		return ResultUtil.success(iSystemResourceService.listToTreeMenu(res));
	}
	
	/**
	 * 获取登录菜单信息
	 * @return
	 */
	@ApiOperation(notes="获取当前用户对应按钮权限", value = "获取当前用户对应按钮权限")
	@GetMapping(path = "/buttons", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<Map<String, List<ResourcesBean>>> buttons(String super_resource_id) {
		
		Assertion assertion = this.getAssertion();
		if(assertion == null) {
			return ResultUtil.success(null);
		}
		Map<String, Object> attrs = assertion.getPrincipal().getAttributes();
		
		List<ResourcesBean> res = attrs.get(H5LoginUserAdapterFilter.USER_RESOURCE_LIST_KEY) == null ? null : (List<ResourcesBean>)attrs.get(H5LoginUserAdapterFilter.USER_RESOURCE_LIST_KEY);
		if(CollectionUtils.isEmpty(res)) {
			return ResultUtil.success(null);
		}
		return ResultUtil.success(iSystemResourceService.buttonGroup(res));
		//return ResultUtil.success(menus);
	}
}
