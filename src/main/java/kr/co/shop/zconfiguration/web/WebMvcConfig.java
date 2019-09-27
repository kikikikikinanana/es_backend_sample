package kr.co.shop.zconfiguration.web;

import java.util.List;

import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.constant.BaseConst;
import kr.co.shop.zconfiguration.ResourceBundleMessageSourceConfig;
import kr.co.shop.zconfiguration.argument.RequestArgumentResolver;
import kr.co.shop.zconfiguration.interceptor.ControllerMethodInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * @see ResourceBundleMessageSourceConfig.java
	 */
	@Autowired
	private LocaleChangeInterceptor localeChangeInterceptor;

	@Autowired
	private ControllerMethodInterceptor controllerMethodInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String profile = Config.getString("spring.profiles.active", "");

		log.debug("spring.profiles.active : {}", profile);
		log.debug("BaseConst.PROFILE_LOCAL.equalsIgnoreCase(profile) : {}",
				BaseConst.PROFILE_LOCAL.equalsIgnoreCase(profile));
		// local일 경우 static 경로 등록
		if (BaseConst.PROFILE_LOCAL.equalsIgnoreCase(profile)) {
			registry.addResourceHandler("/swagger-ui.html")
					.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor);
		registry.addInterceptor(controllerMethodInterceptor);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new RequestArgumentResolver());
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false).ignoreAcceptHeader(false).favorParameter(true)
				.useRegisteredExtensionsOnly(false).parameterName("_format").mediaType("html", MediaType.TEXT_HTML)
				.mediaType("json", MediaType.APPLICATION_JSON);
	}

	/**
	 * 접속 디바이스(mobile,tablet,pc) 판단하는 핸들러 등록
	 * 
	 * @return
	 */
	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		return new DeviceResolverHandlerInterceptor();
	}

	/**
	 * 접속 디바이스(mobile,tablet,pc) 판단하는 객체를 사용할 수 있도 Controller argument에 등록.
	 * 
	 * @return
	 */
	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		return new DeviceHandlerMethodArgumentResolver();
	}

	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {

			@Override
			protected void postProcessContext(org.apache.catalina.Context context) {
				((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
			}
		};
	}

}
