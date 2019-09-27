package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BasePrPromotionProcduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrPromotionProduct extends BasePrPromotionProcduct {

	// 상품, 브랜드, 카테고리에서는 채널, 디바이스, 회원은 항상 들어간다. JOIN 채널,디바이스,회원은 항상 프로모션과 합쳐지고
	// 상품,브랜드,카테고리는 조회를 따로하는게 좋겠다.

	/** 프로모션유형코드 */
	private String[] promoTypeCodeArr;

	/** 채널번호 */
	private String chnnlNo;
	/** 사이트번호 */
	private String siteNo;

	/** 디바이스코드 */
	private String deviceCode;

	/** 회원번호 */
	private String memberNo;
	/** 회원유형코드 */
	private String memberTypeCode;
	/** 멤버십등급코드 */
	private String mbshpGradeCode;
	/** 임직원여부 */
	private String empYn;

	/** 구매수량 */
	private Integer buyQty;
	/** 할인율 */
	private Integer dscntRate;

	/** 상품번호 */
	private String prdtNo;
	/** 상품옵션번호 */
	private String prdtOptnNo;
	/** 상품옵션명 */
	private String optnName;
	/** 행사제한수량 */
	private Integer eventLimitQty;
	/** 수수료율 */
	private Integer cmsnRate;

	/** 카테고리 */
	private String ctgrNo;

	/** 브랜드 */
	private String brandNo;

	/** 정상가 */
	private Integer normalAmt;

	/** 할인가 */
	private Integer dscntAmt;

	/** 옵션추가금액 */
	private Integer optnAddAmt;

	/** 상품 수량 */
	private Integer orderQty;

	/** 사은품 상품 코드 */
	private String giftPrdtNo;

	/** 사은품 옵션번호 */
	private String giftPrdtOptnNo;

	/** 사은품 상품 명 */
	private String giftPrdtName;

	/** 프로모션유형코드명 */
	private String promoTypeName;

	/** 할인금액 */
	private Integer dcAmt;

}
