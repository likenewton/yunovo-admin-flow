package cn.yunovo.iov.fc.web.configuration;

import java.io.Serializable;

import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.yunovo.iov.cas.client.authentication.AbstractClientAuthenticationFilter;
import cn.yunovo.iov.cas.client.authentication.H5ClientAuthenticationFilter;
import cn.yunovo.iov.cas.client.configuration.AuthTokenCryptoProperties;
import cn.yunovo.iov.cas.client.configuration.SpringCasProperties;
import cn.yunovo.iov.cas.client.filter.H5ClientSingleSignOutFilter;
import cn.yunovo.iov.cas.client.filter.TokenFilter;
import cn.yunovo.iov.cas.client.session.ClientRedisSessionMappingStorage;
import cn.yunovo.iov.cas.client.session.ClientSessionMappingStorage;
import cn.yunovo.iov.cas.client.util.CasClientUtil;
import cn.yunovo.iov.cas.client.util.CasJedisPoolUtil;
import cn.yunovo.iov.cas.client.util.cipher.CipherExecutor;
import cn.yunovo.iov.cas.client.util.cipher.TokenCipherExecutor;
import cn.yunovo.iov.cas.client.util.token.JWTTokenBuilder;
import cn.yunovo.iov.cas.client.util.token.TokenBuilder;
import cn.yunovo.iov.cas.client.validation.AbstractTicketValidationFilter;
import cn.yunovo.iov.cas.client.validation.H5ClientCas20ProxyReceivingTicketValidationFilter;
import cn.yunovo.iov.fc.service.ISystemResourceService;
import cn.yunovo.iov.fc.web.filter.H5LoginUserAdapterFilter;
import redis.clients.jedis.JedisPool;

@Configuration
public class CasClientConfig {
	
	public CasClientConfig() {
	}

	@Bean
	@ConfigurationProperties(prefix="")
	public SpringCasProperties getSpringCasAutoconfig() {
		return new SpringCasProperties();
	}

	/**
	 * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 */
	@Bean
	public FilterRegistrationBean<AbstractConfigurationFilter> logOutFilter(SpringCasProperties springCasProperties) {
		FilterRegistrationBean<AbstractConfigurationFilter> filterRegistration = new FilterRegistrationBean<>();
		AbstractConfigurationFilter logoutFilter = null;
		logoutFilter = new H5ClientSingleSignOutFilter();
		
		filterRegistration.setFilter(logoutFilter);
		filterRegistration.setEnabled(springCasProperties.getCasClient().getCasEnabled());
		filterRegistration.addUrlPatterns("/api/*");
		filterRegistration.addInitParameter("casServerUrlPrefix", springCasProperties.getCasClient().getCasServerUrlPrefix());
		filterRegistration.addInitParameter("serverName", springCasProperties.getCasClient().getServerName());
		filterRegistration.setOrder(2);
		return filterRegistration;
	}
	
	@Bean
	public TokenBuilder tokenBuilder(SpringCasProperties springCasProperties) {
		
		TokenBuilder tokenBuilder = new JWTTokenBuilder(tokenCipherExecutor(springCasProperties));
		return tokenBuilder;
		
	}
	
	@Bean
	public CipherExecutor<Serializable, String> tokenCipherExecutor(SpringCasProperties springCasProperties) {
		
		AuthTokenCryptoProperties p = springCasProperties.getCasClient().getAuthTokenCrypto();
		CipherExecutor<Serializable, String> tokenCipherExecutor = new TokenCipherExecutor(p.getEncryptionKey(), p.getSigningKey(),p.getCryptoAlg(),true);
		return tokenCipherExecutor;
	}


	/**
	 * 该过滤器负责用户的认证工作
	 */
	@Bean
	public FilterRegistrationBean<AbstractClientAuthenticationFilter> authenticationFilter(SpringCasProperties springCasProperties) {
		FilterRegistrationBean<AbstractClientAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
		
		AbstractClientAuthenticationFilter authenticationFilter = null;
		authenticationFilter = new H5ClientAuthenticationFilter();
		authenticationFilter.setSpringCasProperties(springCasProperties);
		authenticationFilter.setCasServerLoginUrl(springCasProperties.getCasClient().getCasServerLoginUrl());
		filterRegistration.setFilter(authenticationFilter);
		filterRegistration.setEnabled(springCasProperties.getCasClient().getCasEnabled());
		filterRegistration.addUrlPatterns("/api/*");
		filterRegistration.setOrder(4);
		return filterRegistration;
	}
	
	@Bean
	public ClientSessionMappingStorage clientSessionMappingStorage() {
		
		return new ClientRedisSessionMappingStorage();
	}

	/**
	 * 该过滤器负责对Ticket的校验工作
	 */
	@Bean
	public FilterRegistrationBean<AbstractTicketValidationFilter> cas20ProxyReceivingTicketValidationFilter(SpringCasProperties springCasProperties) {
		
		
		FilterRegistrationBean<AbstractTicketValidationFilter> filterRegistration = new FilterRegistrationBean<>();
		AbstractTicketValidationFilter cas20ProxyReceivingTicketValidationFilter = null;
		cas20ProxyReceivingTicketValidationFilter = new H5ClientCas20ProxyReceivingTicketValidationFilter();
		cas20ProxyReceivingTicketValidationFilter.setSessionMappingStorage(clientSessionMappingStorage());
		
		cas20ProxyReceivingTicketValidationFilter.setTokenBuilder(tokenBuilder(springCasProperties));
		cas20ProxyReceivingTicketValidationFilter.setServerName(springCasProperties.getCasClient().getServerName());
		cas20ProxyReceivingTicketValidationFilter.setSpringCasProperties(springCasProperties);
		filterRegistration.setFilter(cas20ProxyReceivingTicketValidationFilter);
		filterRegistration.setEnabled(springCasProperties.getCasClient().getCasEnabled());

		filterRegistration.addUrlPatterns("/api/*");
		filterRegistration.setOrder(5);
		return filterRegistration;
	}
	
	/**
	 * @param springCasProperties
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<TokenFilter> tokenFilter(SpringCasProperties springCasProperties) {
		FilterRegistrationBean<TokenFilter> filterRegistration = new FilterRegistrationBean<TokenFilter>();
		
		TokenFilter tokenFilter = new TokenFilter(tokenBuilder(springCasProperties), tokenCipherExecutor(springCasProperties));
		filterRegistration.setFilter(tokenFilter);
		filterRegistration.setEnabled(springCasProperties.getCasClient().getCasEnabled());
		filterRegistration.addUrlPatterns("/api/*");
		filterRegistration.setOrder(6);
		return filterRegistration;
	}
	
	@Bean
	public CasClientUtil initCasClientUtil(JedisPool jedisPool, SpringCasProperties springCasProperties) {
		CasJedisPoolUtil jedisPoolUtil = new CasJedisPoolUtil();
		jedisPoolUtil.setJedisPool(jedisPool);
		CasClientUtil.setCasJedisPoolUtil(jedisPoolUtil);
		CasClientUtil.setSpringCasProperties(springCasProperties);
		return null;
	}
	
	/**
	 * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
	 * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
	 * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
	 */
	@Bean
	public FilterRegistrationBean<H5LoginUserAdapterFilter> assertionThreadLocalFilter(SpringCasProperties springCasProperties, ISystemResourceService systemResourceService) {
		FilterRegistrationBean<H5LoginUserAdapterFilter> filterRegistration = new FilterRegistrationBean<H5LoginUserAdapterFilter>();
		
		H5LoginUserAdapterFilter filter = new H5LoginUserAdapterFilter();
		filter.setSpringCasProperties(springCasProperties);
		filterRegistration.setFilter(filter);
		filterRegistration.setEnabled(springCasProperties.getCasClient().getCasEnabled());
		filterRegistration.addUrlPatterns("/api/*");
		filterRegistration.setOrder(8);
		return filterRegistration;
	}

}
