package kr.co.shop.web.product.model.master;

import java.util.Date;
import java.util.List;

import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.event.model.master.EvEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 상품 정보 페이징 객체
 * @FileName : PdProductWrapper.java
 * @Project : shop.backend
 * @Date : 2019. 5. 21.
 * @Author : tennessee
 */
@Slf4j
@Data
public class PdProductWrapper extends PdProduct {

	/** 관련프로모션정보 */
	private List<PdProductPromotion> promotion;
	/** 관련쿠폰정보 */
	private List<PdProductCoupon> coupon;

	/** 사은품 */
	private List<PdProduct> productGift;

	/** 이벤트(draw)정보 */
	private EvEvent eventDraw;

	/** 이벤트(draw) 메시지 코드 */
	private String eventDrawMessageCode;

	/** 상품종류 */
	private String productKind;

	/** 상품표시가격 (판매가) */
	private Integer displayProductPrice;
	/** 상품표시할인율 */
	private Integer displayDiscountRate;

	/** DRAW 이벤트 상태정보. (값 = B(시작전), P(진행중), A(종료)) */
	private String drawStatus;

	/** DRAW 이벤트 시작시간기준 남은시간 */
	private long drawRemainTime;

	/** 판매시작일기준 남은시간 */
	private long sellRemainTime;

	/** 배송비 */
	private Integer dlvyAmt;

	/** 무료배송기준금액 */
	private Integer freeDlvyStdrAmt;

	/**
	 * @Desc : 이벤트 상태 및 예상시간(초) 설정
	 * @Method Name : setSellWaitTime
	 * @Date : 2019. 7. 4.
	 * @Author : tennessee
	 */
	public void setDrawRemainTime() {
		if (this.getEventDraw() != null) {
			if (this.getEventDraw().getEventStartDtm().getTime() <= new Date().getTime()) {
				// 이벤트 시작됨
				if (this.getEventDraw().getEventEndDtm().getTime() >= new Date().getTime()) {
					// 이벤트 종료 안됨. 종료예상시간 설정
					this.setDrawStatus("P");
					this.setDrawRemainTime((this.getEventDraw().getEventEndDtm().getTime() - new Date().getTime()));
				} else {
					// 이벤트 종료됨
					this.setDrawStatus("A");
				}
			} else {
				// 이벤트 시작안됨. 시작예상시간 설정
				this.setDrawStatus("B");
				this.setDrawRemainTime((this.getEventDraw().getEventStartDtm().getTime() - new Date().getTime()));
			}
		}
	}

	/**
	 * @Desc : 런칭예정상품인 경우 대기시간(초) 설정
	 * @Method Name : setSellRemainTime
	 * @Date : 2019. 7. 4.
	 * @Author : tennessee
	 */
	public void setSellRemainTime() {
		if (UtilsText.equals(CommonCode.SELL_STAT_CODE_PREPARE, this.getSellStatCode())) {
			if (this.getSellStartDtm() != null && this.getSellStartDtm().getTime() > new Date().getTime()) {
				// 런치예정 상품인 경우
				this.setSellRemainTime(this.getSellStartDtm().getTime() - new Date().getTime());
			}
		}
	}

}
