package cn.yunovo.iov.fc.web.configuration;


import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.catalina.Context;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.yunovo.iov.fc.model.YunovoTomcatProperties;



/*@EnableConfigurationProperties(OpenApiProperties.class)*/
@Configuration
public class TomcatConfiguration {

	@Bean
	@ConfigurationProperties(prefix="tomcat")
	public YunovoTomcatProperties yunovoTomcatProperties() {
		return new YunovoTomcatProperties();
	}
	
	@Bean
	public ServletWebServerFactory servletContainer(YunovoTomcatProperties properties) {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addConnectorCustomizers(new YunovoTomcatConnectionCustomizer(properties));
		tomcat.addContextCustomizers(new YunovoTomcatContextCustomizer(properties));
		return tomcat;
	}

	/**
	 *
	 * 默认http连接
	 *
	 * @version
	 * @author KK
	 *
	 */
	public class YunovoTomcatContextCustomizer implements TomcatContextCustomizer {

		private YunovoTomcatProperties properties;
		
		public YunovoTomcatContextCustomizer(YunovoTomcatProperties properties) {
			
			this.properties = properties;
		}

		@Override
		public void customize(Context context) {
			
			context.setPath(properties.getContext().getPath());
			context.setReloadable(properties.getContext().getReloadable());
			context.setDocBase(properties.getContext().getDocBase());
		}

	}

	/**
	 *
	 * 默认http连接
	 *
	 * @version
	 * @author KK
	 *
	 */
	public class YunovoTomcatConnectionCustomizer implements TomcatConnectorCustomizer {

		private YunovoTomcatProperties properties;
		
		public YunovoTomcatConnectionCustomizer(YunovoTomcatProperties properties) {
			
			this.properties = properties;
		}

		@Override
		public void customize(Connector connector) {

			
			Map<String, Object>  buildProperties = properties.getConnector().buildProperties();
			
			if(buildProperties == null || buildProperties.isEmpty()) {
				return;
			}
			
			Set<Entry<String, Object>> entrySet = buildProperties.entrySet();
			Iterator<Entry<String, Object>> it = entrySet.iterator();
			Entry<String, Object> temp = null;
			while(it.hasNext()) {
				temp = it.next();
				connector.setAttribute(temp.getKey(), temp.getValue());
			}

		}
	}

}
