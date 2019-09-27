package kr.co.shop.zconfiguration.cache.property;

import java.util.List;

import lombok.Data;

@Data
public class RedisClusterProperty {

    /*
     * spring.redis.cluster.nodes[0] = 127.0.0.1:7379
     * spring.redis.cluster.nodes[1] = 127.0.0.1:7380
     * ...
     */
    List<String> nodes;
    
    // redis 비밀 번호
    String password;
    
    int maxRedirects = 3;
}