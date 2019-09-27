package kr.co.shop.zconfiguration.cache.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterPropertyMaster extends RedisClusterProperty{

}