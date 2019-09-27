package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberCoupon;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class MbMemberCoupon extends BaseMbMemberCoupon {

	// pr_coupon.쿠폰명
	private String cpnName;
	// pr_coupon.쿠폰타입코드
	private String cpnTypeCode;
	// pr_coupon.할인유형
	private String dscntType;
	// pr_coupon.할인값
	private Integer dscntValue;
	// pr_coupon.일반쿠폰여부
	private String normalCpnYn;
	// pr_coupon.최소제한상품판매가
	private Integer minLimitSellAmt;
	// pr_coupon.제한할인율
	private Short limitDscntRate;
	// pr_coupon.쿠폰적용유형
	private String cpnApplyType;
	// pr_coupon.최대할인제한여부
	private String maxDscntLimitYn;
	// pr_coupon.최대할인제한금액
	private Integer maxDscntLimitAmt;
	// pr_coupon.최대쿠폰적용여부
	private String maxCpnApplyYn;
	// pr_coupon.최대쿠폰적용수량
	private Integer maxCpnApplyQty;
	// 재발급 보유쿠폰순번
	private java.lang.Integer reIssueHoldCpnSeq;

	/** 할인가 */
	private Integer dscntAmt;

	private String cartCpnNo;
	private boolean cartCpnSelected = false;

}
