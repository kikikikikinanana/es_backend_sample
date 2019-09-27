package kr.co.shop.zconfiguration.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.shop.common.request.FileUpload;
import lombok.extern.slf4j.Slf4j;

/**
 * 파라메터 값이 MultipartFile일 경우 FileUpload객체로 매핑 처리.
 * 
 * @author 장진철(zerocooldog@zen9.co.kr)
 */
@Slf4j
@Component
public class MultipartFileToFileUploadConverter extends BaseDateConverter
		implements Converter<MultipartFile, FileUpload> {

	@Override
	public FileUpload convert(MultipartFile source) {
		return new FileUpload(source);
	}
}
