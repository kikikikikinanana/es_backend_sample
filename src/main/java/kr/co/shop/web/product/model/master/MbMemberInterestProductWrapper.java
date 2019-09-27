package kr.co.shop.web.product.model.master;

import java.util.List;

import kr.co.shop.web.event.model.master.EvEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 관심상품 정보 페이징 객체
 * @FileName : MbMemberInterestProductWrapper.java
 * @Project : shop.backend
 * @Date : 2019. 6. 26.
 * @Author : hsjhsj19
 */
@Slf4j
@Data
public class MbMemberInterestProductWrapper extends PdProduct {

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

	/** 선택 사이즈 */
	private String prdtOptnNo;

	/** 관심상품순번 */
	private String intrstPrdtSeq;

	/** 출시예정일 */
	private String strSellStartDtm;
}
