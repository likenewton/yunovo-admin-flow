package cn.yunovo.iov.fc.api.configuration;

/**
 * 
 */

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cn.yunovo.iov.fc.common.utils.http.HttpClientUtils;
import cn.yunovo.iov.fc.model.CdpProperties;
import cn.yunovo.iov.fc.model.FcProperties;


@Configuration
public class RestTemplateConfiguration {
	
	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory){
		return new RestTemplate(factory);
	}
	
	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
		try {
			CloseableHttpClient httpClient = HttpClientUtils.createHttpsClient();
	        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
	        return factory;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	 }
	
	@Bean
	@ConfigurationProperties(prefix="fc.gprs")
	public FcProperties fcProperties() {
		
		return new FcProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="cdp")
	public CdpProperties cdpProperties() {
		
		return new CdpProperties();
	}
	
}
