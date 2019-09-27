package kr.co.shop.web.cart.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcCartInfo implements Serializable {

	// 최근 본 상품 resent , 내가 찜한 상품 wish
	private String searchType;
	// 회원 번호
	private String memberNo;
	// 회원 번호
	private String siteNo;

	/** 상품 옵션 */
	/**  */
	private String prdtName;
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

	/** 온라인물류 */
	private Integer stockAiQty;
	/** 스마트물류 */
	private Integer stockAwQty;
	/** 오프라인매장 */
	private Integer stockAsQty;
	/** 입점사배송 */
	private Integer stockVdQty;

	private Integer totStockQty;
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
	private Integer normalViewAmt;
	private Integer sellViewAmt;
	private Integer dscntAmt;

	private Integer dcrate;

	/** 상품 옵션 가격 */
	/** 옵션추가금액 */
	private Integer optnAddAmt;

	/** 브랜드명 */
	private String brandName;

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

}
