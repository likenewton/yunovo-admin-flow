package cn.yunovo.iov.fc.api.configuration;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateValidatorConfiguration {

	
	@Bean(name="hibernateValidator")
    public static Validator validator() {
        return Validation
                .byProvider(HibernateValidator.class)
                .configure()
                //快速返回模式，有一个验证失败立即返回错误信息
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }
	
}
