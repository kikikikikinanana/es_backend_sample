package kr.co.shop.cache;

import java.time.Duration;
import java.util.HashMap;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EvictCacheDuration extends HashMap<String, RedisCacheConfiguration>{


	/**
	 * 캐시 생존 주기를 설정 한다.
	 */
	public EvictCacheDuration () {
        //cacheName으로 정의된 값을 기준으로  생존 주기를 설정한다.  
//        put("memberService.loadUserByUsername", ttl(Duration.ofSeconds(10)));
	}
	
	
	/***
	 * 캐시 생존 주기를 정의 한다.
	 * @param duration
	 * @return RedisCacheConfiguration
	 */
    private final RedisCacheConfiguration ttl(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(duration);
    }
}