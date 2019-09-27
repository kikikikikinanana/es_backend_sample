/**
 * 
 */
package kr.co.shop.web.order.vo;

import lombok.Data;

/**
 * @Desc : 주문 배송 주소 VO
 * @FileName : OrderAddressVo.java
 * @Project : shop.backend
 * @Date : 2019. 5. 3.
 * @Author : ljyoung@3top.co.kr
 */
@Data
public class OrderAddressVo {
	/**
	 * 설명 : 회원번호
	 */
	private String memberNo;

	/**
	 * 설명 : 기본배송지여부
	 */
	private String dfltDlvyAddrYn;

	/**
	 * 설명 : 배송지명
	 */
	private String dlvyAddrName;

	/**
	 * 
	 * 설명 : 받는사람명
	 */
	private String rcvrName;

	/**
	 * 
	 * 설명 : 핸드폰번호
	 */
	private String hdphnNoText;

	/**
	 * 
	 * 설명 : 우편번호
	 */
	private String postCodeText;

	/**
	 * 
	 * 설명 : 우편주소
	 */
	private String postAddrText;

	/**
	 * 
	 * 설명 : 상세주소
	 */
	private String dtlAddrText;
}
