package kr.co.shop.zconfiguration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import kr.co.shop.common.message.Message;

@Configuration
public class ResourceBundleMessageSourceConfig {

	/**
	 * MvcWebConfig 에서 인터셉터를 추가 한다.
	 * 
	 * @see kr.co.shop.zfset.configuration.web.WebMvcConfig ->
	 *      addInterceptors(InterceptorRegistry registry)
	 * @return LocaleChangeInterceptor
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean(name = "localeResolver")
	public CookieLocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		Locale defaultLocale = new Locale("ko");
		localeResolver.setDefaultLocale(defaultLocale);
		localeResolver.setCookieName("lang");
		return localeResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:i18n/common", "classpath:i18n/goods", "classpath:i18n/member",
				"classpath:i18n/order", "classpath:i18n/system", "classpath:i18n/goods", "classpath:i18n/mypage",
				"classpath:i18n/cart");

		messageSource.setUseCodeAsDefaultMessage(true); // 메세지 코드가 정의되지 않았을 경우 호출한 코드 명을 보여준다.
		messageSource.setFallbackToSystemLocale(false);
		messageSource.setDefaultEncoding("UTF-8");
//        messageSource.setCacheSeconds(3600);

		Message.setMessageSource(messageSource);

		return messageSource;
	}
}
