package cn.yunovo.iov.fc.web.log;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.cas.client.util.TokenUtil;
import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.log.LogBean;
import cn.yunovo.iov.fc.common.utils.log.OpBean;
import cn.yunovo.iov.fc.common.utils.log.OpLog;
import cn.yunovo.iov.fc.common.utils.log.OpLogUtil;
import cn.yunovo.iov.fc.common.utils.log.OpUser;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LogAspect {

	@Pointcut("@annotation(cn.yunovo.iov.fc.common.utils.log.OpLog)")
    public void pointcut() { }
	
	@Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
		
		LogBean logBean = new LogBean();
		logBean.setStartTimestamp(System.currentTimeMillis());
		Object result = null;
		
		//获取请求参数
		OpBean op = this.getOpData(point);
		logBean.setOp(op);
		
		//获取请求用户信息
		OpUser user = this.getOpUser();
		logBean.setUser(user);
		
		logBean.setToken(TokenUtil.getToken(WebRequestUtil.request()));
		logBean.setIp(getClientIp(WebRequestUtil.request()));
		try {
			result = point.proceed();
			logBean.setEndTimestamp(System.currentTimeMillis());
		}catch(Throwable e) {
			logBean.setEndTimestamp(System.currentTimeMillis());
			if(e instanceof FormValidateException) {
				op.setResult(this.buildExceptionResult((FormValidateException) e));
			}else if(e instanceof BusinessException) {
				op.setResult(this.buildExceptionResult((BusinessException) e));
			}else {
				logBean.setError(true);
				logBean.setError_code("500");
				logBean.setError_msg(ExceptionUtils.getStackTrace(e));
			}
			
			this.log(logBean);
			throw e;
		}
		
		
		try {
			if(result == null) {
				
			}else if(result instanceof Result) {
				op.setResult(JSONObject.toJSONString(result));
			}else {
				op.setResult(result.toString());
			}
		} catch (Exception e1) {
			log.error("[LogAspect][exception]exception={}", ExceptionUtils.getStackTrace(e1));
		}
		
		
		this.log(logBean);
		return result;
	}

	private OpUser getOpUser() {
		
		Assertion assertion = BaseController.getAssertionStatic();
		
		if(assertion == null) {
			return null;
		}
		
		Map<String, Object> attr = assertion.getPrincipal().getAttributes();
		
		if(attr == null) {
			return null;
		}
		
		OpUser loginInfo = new OpUser();
		loginInfo.setLoginName(String.valueOf(attr.get("loginName")));
		loginInfo.setUserName(String.valueOf(attr.get("userName")));
		loginInfo.setId(String.valueOf(attr.get("id")));
		loginInfo.setOrganCode(String.valueOf(attr.get("organCode")));
		return loginInfo;
	}

	private void log(LogBean logBean) {
		
		try {
			OpLogUtil.info(logBean);
		} catch (Exception e) {
			log.error("[LogAspect][exception]exception={}", ExceptionUtils.getStackTrace(e));
		}

	}
	
	public String buildExceptionResult(FormValidateException e) {
		
		
		return JSONObject.toJSONString(ResultUtil.build(e.getCode(), e.getMessage(),e.getFiled()));
	}
	
	public String buildExceptionResult(BusinessException e) {
		
		return JSONObject.toJSONString(ResultUtil.build(e.getException_code(), e.getMessage()));
	}

	/**
	 * @param joinPoint
	 */
	private OpBean getOpData(ProceedingJoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OpLog logAnnotation = method.getAnnotation(OpLog.class);
        OpBean op = new OpBean();
        
        if (logAnnotation != null) {
            // 注解上的描述
        	op.setName(logAnnotation.opName());
        	op.setType(logAnnotation.opType().getValue());
        	op.setDesc(logAnnotation.opDesc());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        op.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
        	JSONObject data = new JSONObject();
        	
            for (int i = 0; i < args.length; i++) {
            	
            	if(args[i] instanceof HttpServletRequest) {
            		continue;
            	}
            	
            	if(args[i] instanceof HttpServletResponse) {
            		continue;
            	}
            	
            	data.put(paramNames[i], args[i]);
            }
        }
        
        return op;
	}
	
	private String getClientIp(HttpServletRequest request) {
		if (request == null) { 
			return "127.0.0.1";
		}

		String ipStr = request.getHeader("X-Cluster-Client-Ip");
		if (ipStr == null || ipStr.trim().length() == 0 || "unknown".equalsIgnoreCase(ipStr)) {
			ipStr = request.getHeader("X-Forwarded-For");
		}

		if (ipStr != null && !"".equals(ipStr.trim())) {

			if (ipStr.indexOf(",") < 0) {
				return ipStr;
			}
			String[] ips = ipStr.split(",");
			for (String ip : ips) {
				if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
					continue;
				}
				return ip;
			}
		}
		return request.getRemoteAddr(); 
	}
	
	
}
