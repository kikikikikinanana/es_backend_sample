package kr.co.shop.web.product.model.master;

import java.util.List;

import kr.co.shop.web.product.model.master.base.BasePdProduct;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class PdProductWithAll extends BasePdProduct {

	/** 장바구니 순번 */
	private Long cartSeq;
	/** 회원번호 */
	private String memberNo;
	/** 회원등급 */
	private String memberTypeCode;

	/** 상품 옵션 */
	/** 상품옵션번호 */
	private String prdtOptnNo;
	/** 옵션명 */
	private String optnName;
	/** 총재고수량 */
	private Integer totalStockQty;
	/** 총주문수량 */
	private Integer totalOrderQty;
	/** 주문가능수량 */
	private Integer orderPsbltQty;
	/** 업체상품번호 */
	private String vndrPrdtNoText;
	/** 판매상태코드 */
	private String optnSellStatCode;
	/** 품절사유 */
	private String soldOutRsnText;
	/** 정렬순번 */
	private Integer sortSeq;
	/** 사용여부 */
	private String optnUseYn;

	/** 상품 옵션 재고 */
	/** 재고구분코드 */
	private String stockGbnCode;
	/** 재고수량 */
	private Integer stockQty;
	/** 주문건수 */
	private Integer orderCount;
	/** 재고구분코드 목록 */
	private List<SyCodeDetail> stockGbnCodeList;
	// 상품옵션재고 정보
	/** 온라인물류 */
	private Integer stockAiQty;
	/** 스마트물류 */
	private Integer stockAwQty;
	/** 오프라인매장 */
	private Integer stockAsQty;
	/** 입점사배송 */
	private Integer stockVdQty;
	/** 온라인물류 재고구분코드 */
	private String stockAiGbnCode;
	/** 스마트물류 재고구분코드 */
	private String stockAwGbnCode;
	/** 오프라인매장 재고구분코드 */
	private String stockAsGbnCode;
	/** 입점사배송 재고구분코드 */
	private String stockVdGbnCode;

	/** 상품 가격 */
	/** 정상가 */
	private Integer normalAmt;
	/** 판매가 */
	private Integer sellAmt;
	/** 할인가 */
	private Integer dscntAmt;
	/** 기간계할인율 */
	private Short erpDscntRate;
	/** 온라인할인율 */
	private Short onlnDscntRate;
	/** 임직원할인율 */
	private Short empDscntRate;

	/** 상품 옵션 가격 */
	/** 옵션추가금액 */
	private Integer optnAddAmt;

	/** 판매가(정상가-프로모션)*수량 */
	private Integer totalSellAmt;

	/** 브랜드명 */
	private String brandName;

	/** 프로모션 */
	private PrPromotion[] promotion;

	/** 상품관련파일 */
	/** 이미지경로 */
	private String imagePathText;
	/** 이미지URL */
	private String imageUrl;

	/** 계산 검증 필요 요소들 */
	/** 프로모션 번호 */
	private String promoNo;
	/** 쿠폰 번호 */
	private String cpnNo;
	/** 주문 수량 */
	private Integer orderQty;
	/** 프로모션 할인 가격 */
	private Integer promoDscntAmt;
	/** 쿠폰 할인 가격 */
	private Integer cpnDscntAmt;
	/** 상품 할인 **/
	private Integer prdtDscntAmt;

	/** 프로모션 할인 */
	private double dscntRate;
	private java.lang.Integer dlvyCalAmt; // 실판매 가능 상품 금액 (배송비 계산위함)

	/** 카테고리 번호 */
	private String ctgrNo;

}
