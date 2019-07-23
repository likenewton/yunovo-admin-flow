package cn.yunovo.iov.fc.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfiguration {

	@Bean
	public JedisPool jedisPool(JedisConnectionFactory jedisConnectionFactory) {
		return new JedisPool(jedisConnectionFactory.getPoolConfig(),  jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(), jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword(), jedisConnectionFactory.getDatabase());
	}
	
	@Bean
	public JedisPoolUtil jedisPoolUtil(JedisPool jedisPool) {
		
		JedisPoolUtil jedisPoolUtil = new JedisPoolUtil();
		jedisPoolUtil.setJedisPool(jedisPool);
		return jedisPoolUtil;
	}
	
	
}
