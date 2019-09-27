package kr.co.shop.zconfiguration;

import java.nio.charset.Charset;

import javax.servlet.Filter;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

import kr.co.shop.common.constant.BaseConst;

@Configuration
public class EncodingConfig {
	/**
	 * reponse 결과를 화면에 출력 시 강제로 UTF-8로 설정
	 * 
	 * @return HttpMessageConverter
	 */
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		return new StringHttpMessageConverter(Charset.forName(BaseConst.DEFAULT_CHARSET_UTF_8));
	}
}
