package kr.co.shop.zconfiguration.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 파라메터 값이 문자열인데 model 매핑 필드가 MultipartFile일 경우 null 처리.
 * 
 * @author 장진철(zerocooldog@zen9.co.kr)
 */
@Slf4j
@Component
public class StringToMultipartFileConverter implements Converter<String, MultipartFile> {

	@Override
	public MultipartFile convert(String source) {
		return null;
	}
}
