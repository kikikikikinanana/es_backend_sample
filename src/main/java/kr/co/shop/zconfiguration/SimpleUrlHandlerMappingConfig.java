package kr.co.shop.zconfiguration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import kr.co.shop.common.controller.JSPController;

@Configuration
public class SimpleUrlHandlerMappingConfig {

 
    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
         
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("/**/*.jspa", jspController());
        simpleUrlHandlerMapping.setUrlMap(urlMap);
        simpleUrlHandlerMapping.setOrder(0); //핸들러 실행 우선순위를 맨 처음으로 둔다.
        return simpleUrlHandlerMapping;
    }
 
    @Bean
    public JSPController jspController() {
        return new JSPController();
    }
}