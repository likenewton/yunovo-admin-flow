package cn.yunovo.iov.fc.api.configuration;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.yunovo.iov.fc.api.filter.FcApiFilter;
import cn.yunovo.iov.fc.api.filter.FcApiSignatureFilter;


@Configuration
public class FilterConfig {

	/**
	 * 验签过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<FcApiSignatureFilter> checkSignatureFilter() {

        FilterRegistrationBean<FcApiSignatureFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new FcApiSignatureFilter());
        registration.addUrlPatterns("/fc/api/*");
        registration.setName("fcApiSignatureFilter");
        registration.setOrder(1);
        return registration;
    }

	/**
	 * 验签过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<FcApiFilter> appApiFilter() {

        FilterRegistrationBean<FcApiFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new FcApiFilter());
        registration.addUrlPatterns("/app/api/*");
        registration.setName("appApiFilter");
        registration.setOrder(1);
        return registration;
    }

	
}
