package kr.co.shop.zconfiguration.convert;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import kr.co.shop.util.UtilsDate;
import lombok.extern.slf4j.Slf4j;

/**
 * BaseConst에 선언되어 있는 날짜 포맷 4가지로 파라메터 문자열이 넘어올 경우 자동으로 Timestamp객체에 변환되어 설정된다.
 * 
 * @see kr.co.zen9.rivendell.core.constant.BaseConst.DEFAULT_DATE_PATTERN_NOT_CHARACTERS
 * @see kr.co.zen9.rivendell.core.constant.BaseConst.DEFAULT_DATETIME_MIN_PATTERN_NOT_CHARACTERS
 * @see kr.co.zen9.rivendell.core.constant.BaseConst.DEFAULT_DATETIME_PATTERN_NOT_CHARACTERS
 * @see kr.co.zen9.rivendell.core.constant.BaseConst.DEFAULT_DATETIME_MILI_PATTERN_NOT_CHARACTERS
 * @author 장진철(zerocooldog@zen9.co.kr)
 */
@Slf4j
@Component
public class StringToTimestampConverter extends BaseDateConverter implements Converter<String, Timestamp> {

	@Override
	public Timestamp convert(String source) {
		return UtilsDate.getSqlTimeStamp(parseDate(source));
	}
}
