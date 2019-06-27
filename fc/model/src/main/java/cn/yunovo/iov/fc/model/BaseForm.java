package cn.yunovo.iov.fc.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.common.utils.ApplicationContextProvider;
import cn.yunovo.iov.fc.model.exception.FormValidateException;

public abstract class BaseForm {

	public Validator validator() {
		
		return (Validator) ApplicationContextProvider.getBean("hibernateValidator");
	}
	
	public void validate(Class<? extends ValidateGroup>... groups) {
		
		Validator validator = this.validator();
		Set<ConstraintViolation<BaseForm>>  errorSet = validator.validate(this, groups);
		if(!CollectionUtils.isEmpty(errorSet)) {
			
			ConstraintViolation<BaseForm> temp = errorSet.iterator().next();
			throw new FormValidateException(temp.getMessage(),temp.getPropertyPath().toString(), JSONObject.toJSONString(this));
		}
	}
	
	public String buildJsonString() {
		
		return JSONObject.toJSONString(this);
	}
	
}
