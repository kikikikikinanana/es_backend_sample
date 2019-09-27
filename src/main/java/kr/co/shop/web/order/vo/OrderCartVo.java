package kr.co.shop.web.order.vo;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

/**
 * @Desc : 주문 관련 VO
 * @FileName : OrderCartVo.java
 * @Project : shop.fo
 * @Date : 2019. 4. 8.
 * @Author : ljyoung@3top.co.kr
 */
@Data
public class OrderCartVo extends BaseBean implements Serializable {

	private static final long serialVersionUID = -3830382582231453561L;

	/**
	 * 설명 : 사이트번호
	 */
	private String siteNo;

	/**
	 * 주문서 진입시 사용되는 장바구니순번
	 */
	private String[] cartSeq;

	/**
	 * 주문서 진입시 배송 유형 D : 일반배송, P : 픽업배송 기본 D
	 */
	private String cartDeliveryType;

	/**
	 * 설명 : 회원번호
	 */
	private String memberNo;

	/**
	 * 설명 : 로그인ID
	 */
	private String loginId;

	/**
	 * 설명 : 로그인여부
	 */
	private boolean isLogin;

	/**
	 * 설명 : 회원유형코드
	 */
	private String memberTypeCode;

	/**
	 * 디바이스코드
	 */
	private String deviceCode;

	/**
	 * 세션아이디
	 */
	private String sessionId;

	/**
	 * 임직원 여부
	 */
	private String empYn;

	/**
	 * 회원등급
	 */
	private String mbshpGradeCode;
}
