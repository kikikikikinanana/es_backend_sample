package kr.co.shop.web.product.model.master;

import java.util.List;

import kr.co.shop.web.event.model.master.EvEvent;
import kr.co.shop.web.promotion.model.master.PrPromotionTargetProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 프로모션 상품 정보 페이징 객체
 * @FileName : PrPromotionProductWrapper.java
 * @Project : shop.backend
 * @Date : 2019. 5. 21.
 * @Author : tennessee
 */
@Slf4j
@Data
public class PrPromotionProductWrapper extends PdProduct {

	/** 관련프로모션정보 */
	private List<PdProductPromotion> promotion;
	/** 관련쿠폰정보 */
	private List<PdProductCoupon> coupon;

	/** 사은품 */
	private List<PdProduct> productGift;

	/** 이벤트(draw)정보 */
	private EvEvent eventDraw;

	/** 상품종류 */
	private String productKind;

	/** 상품표시가격 (판매가) */
	private Integer displayProductPrice;
	/** 상품표시할인율 */
	private Integer displayDiscountRate;

	/** 상품표시할인율 */
	private List<PrPromotionTargetProduct> promotionTargetProduct;

}
