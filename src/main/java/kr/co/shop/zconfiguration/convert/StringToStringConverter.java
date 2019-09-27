package kr.co.shop.zconfiguration.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import kr.co.shop.util.UtilsText;
import lombok.extern.slf4j.Slf4j;

/**
 * String 문자열로 들어오는 파라메터 값은 무조건 xss처리 한다.
 * 
 * @author 장진철(zerocooldog@zen9.co.kr)
 */
@Slf4j
@Component
public class StringToStringConverter extends BaseDateConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {

		if (!UtilsText.isBlank(source)) {
			return UtilsText.escapeXss(source);
		}

		return source;
	}
}
