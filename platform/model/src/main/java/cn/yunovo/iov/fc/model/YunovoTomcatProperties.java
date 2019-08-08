package cn.yunovo.iov.fc.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.PropertyMapper;

@lombok.Data
public class YunovoTomcatProperties {
	
	private final Connector connector = new Connector();

	private final Context context = new Context();

	@lombok.Data
	public static class Context {

		private String docBase;
		private String path;
		private Boolean reloadable = false;

		public Map<String, Object> buildProperties() {

			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getDocBase).whenNonNull().to(properties.in("docBase"));
			map.from(this::getPath).whenNonNull().to(properties.in("path"));
			map.from(this::getReloadable).whenNonNull().to(properties.in("reloadable"));

			return properties.with();
		}
	}

	@lombok.Data
	public static class Connector {

		private Integer port = 8080;
		private String protocol = "org.apache.coyote.http11.Http11Nio2Protocol";
		private Charset URIEncoding = StandardCharsets.UTF_8;
		private Integer minSpareThreads = 25;
		private Integer maxSpareThreads = 75;
		private Boolean enableLookups = false;
		private Boolean disableUploadTimeout = true;
		private Integer connectionTimeout = 10000;
		private Integer acceptCount = 300;
		private Integer maxThreads = 300;
		private Integer maxProcessors = 1000;
		private Integer minProcessors = 5;
		private Integer acceptorThreadCount = 1;
		private Integer redirectPort = 443;
		private Boolean tcpNoDelay = true;
		private String compression = "off";
		private Integer maxConnections = 10000;
		private String scheme = "http";

		public Map<String, Object> buildProperties() {

			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getPort).to(properties.in("port"));
			map.from(this::getProtocol).whenNonNull().to(properties.in("protocol"));
			map.from(this::getURIEncoding).whenNonNull().to(properties.in("URIEncoding"));
			map.from(this::getMinSpareThreads).whenNonNull().to(properties.in("minSpareThreads"));
			map.from(this::getMaxSpareThreads).whenNonNull().to(properties.in("maxSpareThreads"));
			map.from(this::getEnableLookups).whenNonNull().to(properties.in("enableLookups"));
			map.from(this::getDisableUploadTimeout).whenNonNull().to(properties.in("disableUploadTimeout"));
			map.from(this::getConnectionTimeout).whenNonNull().to(properties.in("connectionTimeout"));
			map.from(this::getAcceptCount).whenNonNull().to(properties.in("acceptCount"));
			map.from(this::getMaxProcessors).whenNonNull().to(properties.in("maxProcessors"));
			map.from(this::getMaxThreads).whenNonNull().to(properties.in("maxThreads"));
			map.from(this::getMinProcessors).whenNonNull().to(properties.in("minProcessors"));
			map.from(this::getAcceptorThreadCount).whenNonNull().to(properties.in("acceptorThreadCount"));
			map.from(this::getRedirectPort).whenNonNull().to(properties.in("redirectPort"));
			map.from(this::getTcpNoDelay).whenNonNull().to(properties.in("tcpNoDelay"));
			map.from(this::getCompression).whenNonNull().to(properties.in("compression"));
			map.from(this::getMaxConnections).whenNonNull().to(properties.in("maxConnections"));
			map.from(this::getScheme).whenNonNull().to(properties.in("scheme"));

			return properties.with();
		}

	}
	

	@SuppressWarnings("serial")
	private static class Properties extends HashMap<String, Object> {
	
		public <V> java.util.function.Consumer<V> in(String key) {
			return (value) -> put(key, value);
		}
	
		public Properties with(Map<String, String> properties) {
			putAll(properties);
			return this;
		}
	
		public Properties with() {
			return this;
		}
	
	}

}

