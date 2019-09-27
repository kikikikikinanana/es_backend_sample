package kr.co.shop.web.display.vo;

import lombok.Data;

@Data
public class GnbVO {
	/**
	 * 전시페이지번호
	 */
	private String dispPageNo;

	/**
	 * 사이트번호
	 */
	private String siteNo;

	/**
	 * 채널번호
	 */
	private String chnnlNo;

	/**
	 * 전시페이지번호배열
	 */
	private String[] dispPageNoArr;

	private String prdtNo;

	private String deviceCode;
}
