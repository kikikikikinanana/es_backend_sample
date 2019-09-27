package kr.co.shop.zconfiguration.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import kr.co.shop.common.request.FileUpload;
import lombok.extern.slf4j.Slf4j;

/**
 * 파라메터 값이 문자열인데 model 매핑 필드가 FileUpload일 경우 null 처리.
 * 
 * @author 장진철(zerocooldog@zen9.co.kr)
 */
@Slf4j
@Component
public class StringToFileUploadConverter implements Converter<String, FileUpload> {

	@Override
	public FileUpload convert(String source) {
		return null;
	}
}
