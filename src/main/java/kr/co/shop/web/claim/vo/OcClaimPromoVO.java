package kr.co.shop.web.claim.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 클레임 프로모션 관련 정보
 * @FileName : OcClaimPromoVO.java
 * @Project : shop.bo
 * @Date : 2019. 4. 22.
 * @Author : KTH
 */
@Slf4j
@Data
public class OcClaimPromoVO {
	private String clmNo;
	private java.lang.Short clmPrdtSeq;
	private String orgOrderNo;
	private String orderNo;
	private java.lang.Short orderPrdtSeq;
	private String promoNo; // 프로모션번호
	private java.lang.Short applyPromoSeq; // 적용프로모션순번

	private java.lang.Integer prdtNormalAmt; // 상품정상가
	private java.lang.Integer prdtSellAmt; // 상품판매가
	private java.lang.Integer optnAddAmt; // 옵션추가금액
	private java.lang.Integer sellAmt; // 판매가
	private java.lang.Integer totalDscntAmt; // 할인금액합계
	private java.lang.Integer cpnDscntAmt; // 쿠폰할인금액
	private java.lang.Integer orderAmt; // 주문금액
	private String dscntType; // 할인유형
	private java.lang.Integer dscntValue; // 할인값
	private java.lang.Integer dscntAmt; // 할인금액
	private java.lang.Integer chngDscntValue; // 변경할인값
	private java.lang.Integer chngDscntAmt; // 변경할인금액

	private String nowClmNo; // 클레임번호(현재 진행 중인 클레임 구분용)
	private java.lang.Short nowClmPrdtSeq; // 클레임상품순번(현재 진행 중인 클레임 구분용)
	private String canceledClmNo; // 클레임번호(클레임으로 인한 취소된 프로모션 구분용)
	private String mmnyPrdtYn; // 자사상품여부
	private String vndrNo; // 업체번호
}
