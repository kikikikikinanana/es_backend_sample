package kr.co.shop.zconfiguration.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.ReadFrom;
import kr.co.shop.cache.EvictCacheDuration;
import kr.co.shop.zconfiguration.cache.property.RedisPropertyMaster;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

	@Autowired
	RedisPropertyMaster redisPropertyMaster;

	@Autowired
	EvictCacheDuration evictCacheDuration;

	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Profile("local")
	@Bean("redisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
				.readFrom(ReadFrom.SLAVE_PREFERRED).build();
		log.debug("redisPropertyMaster => {}", redisPropertyMaster);
		RedisStaticMasterReplicaConfiguration redisConf = new RedisStaticMasterReplicaConfiguration(
				redisPropertyMaster.getHost(), redisPropertyMaster.getPort());
		redisConf.addNode(redisPropertyMaster.getHost(), redisPropertyMaster.getPort() + 1);
		redisConf.setPassword(RedisPassword.of(redisPropertyMaster.getPassword()));

		return new LettuceConnectionFactory(redisConf, clientConfig);
	}

	@Profile("!local")
	@Bean("redisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactorySentinel() {

		log.debug("sentinel property : {}", redisPropertyMaster);

		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
		sentinelConfig.setMaster(redisPropertyMaster.getSentinel().getMaster());
		sentinelConfig.setPassword(RedisPassword.of(redisPropertyMaster.getPassword()));
		sentinelConfig.setSentinels(redisPropertyMaster.getSentinel().getRedisNodes());
		return new LettuceConnectionFactory(sentinelConfig);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager() {

		// 캐시매니저 설정
		RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//    		.prefixKeysWith("") //cachename::key 형식으로 레디스에 입력 시 cachename::부분을 제거 할 경우에 사용
//	        .entryTtl(Duration.ofSeconds(5)) //캐시 전체의 기본 TTL 5초 지나면 캐시가 제거 된다.
				.disableCachingNullValues()
				.serializeKeysWith(SerializationPair.fromSerializer(RedisSerializer.string())) // StringRedisSerializer
				.serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.java())); // JdkSerializationRedisSerializer

		RedisCacheManager rcm = RedisCacheManager.builder(redisConnectionFactory())
				.cacheDefaults(defaultCacheConfiguration).withInitialCacheConfigurations(evictCacheDuration)
				.transactionAware().build();
		return rcm;
	}

	@Override
	@Bean
	public CacheErrorHandler errorHandler() {
		return new RedisCacheConfig.DefaultCacheErrorHandler();
	}

	public static class DefaultCacheErrorHandler implements CacheErrorHandler {

		@Override
		public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
			log.warn("[Redis] - Unable to get from cache:{}, {}", cache.getName(), exception);
		}

		@Override
		public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
			log.warn("[Redis] - Unable to put in cache:{}, {}", cache.getName(), exception);
		}

		@Override
		public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
			log.warn("[Redis] - Unable to evict from cache:{}, {}", cache.getName(), exception);
		}

		@Override
		public void handleCacheClearError(RuntimeException exception, Cache cache) {
			log.warn("[Redis] - Unable to clear cache:{}, {}", cache.getName(), exception);
		}
	}

}
