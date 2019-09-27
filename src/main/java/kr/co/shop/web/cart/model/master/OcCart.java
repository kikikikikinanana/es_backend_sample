package kr.co.shop.web.cart.model.master;

import java.util.List;

import kr.co.shop.common.request.annotation.ParameterOption;
import kr.co.shop.web.cart.model.master.base.BaseOcCart;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class OcCart extends BaseOcCart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5943773551982635705L;

	private boolean isLogin; // 로그인 여부

	private String nonMemberNo; // 비회원 회원번호

	private String memberTypeCode; // 회원유형
	private String mbshpGradeCode; // 회원등급
	private String empYn; // 임직원여부
	private String deviceCode; // 디바이스코드

	private String[] prdtTypeCodes; // 상품유형코드's

	// 업체 정보
	private String vndrNo; // 업체번호
	private String vndrGbnType; // 업체구분타입
	private String vndrName; // 업체명
	private java.lang.Integer freeDlvyStdrAmt; // 무료배송기준금액
	private java.lang.Integer dlvyAmt; // 배송비
	private Long vndrTotalPrdtCnt; // 업체별 상품 count ( 주문수량 미포함)
	private String freeDlvyFlag; // 업체별 무료배송 여부

	// 장바구니 loop flag
	private String loopflag; // 업체구분타입 /*cart*/

	// 상품 마스터 정보
	private String prdtName; // 상품명
	private String prdtColorInfo; // 색상코드
	private String optnName; // 상품옵션명

	private String storePickupPsbltYn; // 픽업가능여부 /* cart & product */
	private String freeDlvyYn; // 무료배송여부
	private String stockIntgrYn; // 재고통합여부
	private String stockMgmtYn; // 재고관리여부
	private String buyLimitYn; // 구매제한여부
	private String mmnyPrdtYn; // 자사상품여부
	private java.lang.Integer minBuyPsbltQty; // 최소구매가능수량
	private java.lang.Integer dayMaxBuyPsbltQty; // 1일최대구매가능수량
	private java.lang.Integer maxBuyPsbltQty; // 최대구매가능수량

	private String brandName; // 브랜드명

	// 상품 옵션 정보
	private java.lang.Integer totalStockQty; // 총재고수량
	private java.lang.Integer totalOrderQty; // 총주문수량
	private java.lang.Integer orderPsbltQty; // 주문가능수량
	private String vndrPrdtNoText; // 업체상품번호
	private String sellStatCode; // 판매상태코드

	private String imagePathText; // 이미지경로
	private String imageUrl; // 이미지URL

	private java.lang.Integer stockTotalQty; /* cart */
	/** 온라인물류 */
	private java.lang.Integer stockAiQty;
	/** 스마트물류 */
	private java.lang.Integer stockAwQty;
	/** 오프라인매장 */
	private java.lang.Integer stockAsQty;
	/** 입점사배송 */
	private java.lang.Integer stockVdQty;

	// 상품 금액 정보
	private java.lang.Integer normalAmt; // 정상가
	private java.lang.Integer sellAmt; // 판매가
	private java.lang.Integer dscntAmt; // 할인가
	private java.lang.Integer totalSellAmt; // 판매가 *수량
	private java.lang.Integer orgDscntAmt; // 할인가
	private java.lang.Short erpDscntRate; // 기간계할인율
	private java.lang.Short onlnDscntRate; // 온라인할인율
	private java.lang.Short empDscntRate; // 임직원할인율

	private java.lang.Integer dlvyCalAmt; // 실판매 가능 상품 금액 (배송비 계산위함)

	private java.lang.Integer promoTotDcAmt; // 상품별 프로모션 합계금액

	private java.lang.Integer manyShoesDcAmt; // 상품별 프로모션 ㄷㅏㅈㅗㄱㄱㅜㅁㅁㅐ

	private java.lang.Integer savePoint; // 적립포인트(예상)

	/** 상품 할인 **/
	private java.lang.Integer prdtDscntAmt;

	private OcCart[] prdtList; // 상품 옵션 배열

	private String[] cartSeqs; // 장바구니상품 배열
	private String clickTabId; // 장바구니 tab 정보
	private String reloadFlag; // 행사 초기화 flag

	private String nomalCpnNo;// 일반쿠폰 번호
	private String plusCpnNo;// 플러스쿠폰 번호
	private String nomalHoldCpnSeq; // 일반 쿠폰 보유 쿠폰 순번
	private String plusHoldCpnSeq; // 플러스 쿠폰 보유 쿠폰 순번

	private java.lang.Integer nomalCpnDcAmt;// 일반쿠폰 할인금액
	private java.lang.Integer plusCpnDcAmt; // 플러스쿠폰 할인금액

	private java.lang.Integer cpnTotDcAmt; // 상품별 쿠폰할인 금액 ( 일반 + 플러스)

	@ParameterOption(target = "cartSeq")
	private List<PrPromotion> promoList;
}
