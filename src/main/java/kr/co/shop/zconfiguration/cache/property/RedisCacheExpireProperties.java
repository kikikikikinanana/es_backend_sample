package kr.co.shop.zconfiguration.cache.property;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
//@ConfigurationProperties(prefix = "spring.redis.expire")
public class RedisCacheExpireProperties {

    private long defaultExpireTime = 0L;
    private Map<String, Long> expireTime = new HashMap<>();

}