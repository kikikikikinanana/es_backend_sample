package kr.co.shop.web.cart.model.master;

import kr.co.shop.web.cart.model.master.base.BaseOcCartBenefit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcCartBenefit extends BaseOcCartBenefit {

	private boolean isLogin; // 로그인 여부
	private String memberNo;
	private String sessionId;
	private String siteNo;
	private String memberTypeCode; // 회원유형
	private String mbshpGradeCode; // 회원등급
	private String empYn; // 임직원여부
	private String deviceCode; // 디바이스코드

	private java.lang.Integer dscntAmt; // 할인가

	private String applyCouponFlag; // 상품 쿠폰 적용여부
	private OcCartBenefit[] applyCouponList; // 상품 쿠폰 배열

	private String nomalCpnNo; // 일반 쿠폰 번호
	private String nomalHoldCpnSeq; // 일반 쿠폰 보유 쿠폰 순번
	private java.lang.Integer nomalCpnApplyDcAmt; // 일반 쿠폰 할인 금액
	private java.lang.Integer nomalCpnApplyQty; // 쿠폰적용수량

	private String plusCpnNo; // 플러스 쿠폰 번호
	private String plusHoldCpnSeq; // 플러스 쿠폰 보유 쿠폰 순번
	private java.lang.Integer plusCpnApplyDcAmt; // 플러스 쿠폰 할인 금액
	private java.lang.Integer plusCpnApplyQty; // 쿠폰적용수량

	private java.lang.Integer cpnApplyTotSellAmt; // 쿠폰 적용 판매가
	private java.lang.Integer cpnApplyTotPoint; // 쿠폰 적용 판매가에 대한 포인트 계산

	private java.lang.Integer cpnApplyDcAmt; // 쿠폰 적용 금액
	// private java.lang.Integer cpnApplySavePoint; // 쿠폰 적용 포인트

	private String cpnYn; // 일반쿠폰 여부

	private java.lang.Long delCartSeq;
	private java.lang.Integer delHoldCpnSeq;
	private String delDcamt;
	private java.lang.Integer delDscntAmt; // 삭제 쿠폰 기준 판매가

	private String[] cartSeqs;

}
