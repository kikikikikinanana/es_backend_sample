package kr.co.shop.zconfiguration.cache.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
@ToString(callSuper = true, includeFieldNames = true)
public class RedisPropertyMaster extends RedisProperty {

	private RedisSentinelProperty sentinel;
}