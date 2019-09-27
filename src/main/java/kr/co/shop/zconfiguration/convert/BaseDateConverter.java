package kr.co.shop.zconfiguration.convert;

import java.util.Date;

import kr.co.shop.common.constant.BaseConst;
import kr.co.shop.util.UtilsDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseDateConverter {

	/**
	 * 기본 포맷은 yyyyMMddHHmmss
	 * 
	 * @param source 날짜형식 문자열
	 * @return Date
	 */
	public Date parseDate(String source) {
		String format = source.replaceAll("[. :]", "");

		if (format.length() == 8) {
			return UtilsDate.parseDate(format, BaseConst.DEFAULT_DATE_PATTERN_NOT_CHARACTERS);
		} else if (format.length() == 14) {
			return UtilsDate.parseDate(format, BaseConst.DEFAULT_DATETIME_PATTERN_NOT_CHARACTERS);
		} else if (format.length() == 17) {
			return UtilsDate.parseDate(format, BaseConst.DEFAULT_DATETIME_MILI_PATTERN_NOT_CHARACTERS);
		}

		return UtilsDate.parseDate(format, BaseConst.DEFAULT_DATETIME_PATTERN_NOT_CHARACTERS);
	}
}
