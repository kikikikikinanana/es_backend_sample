package kr.co.shop.web.giftcard.vo;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

@Data
public class GiftcardVo extends BaseBean implements Serializable {

	private String memberNo; // 회원번호
	private String memberTypeCode; // 온라인회원, 맴버쉽회원, 비회원
	private String mbshpGradeCode; // 일반, VIP
	private String empYn; // 임직원여부 (맴버쉽회원이면서 임직원여부 Y일경우)
	private String otsVipYn;
	private String giftCardName; // 카드명
	private String giftCardNo; // 기프트카드 번호
	private String cardPinNoText; // 인증번호
	private String rprsntCardYn; // 대표카드유무
	private String conPin; // 인증번호

	private String cardNum;
	private String payType;
	private String payAmt;
	private String validDtm;
	private String conBalanceAmount;
	private String couponEndDate;
	private String pymntMeansCode;

	private String kakaoCouponNum; // 카카오톡 쿠폰번호
	private String admitNum; // 인증번호
	private String useAmount; // 사용금액
	private String rcvrHdphnNoText; // 수취인 휴대폰 번호
	private String rcvrName; // 수취인명

	private String giftCardOrderNo; // 주문번호
	private String siteNo;

	private String rprSntCardNoText;
	private String[] cardAmt;
	private String[] cardNoText;
	private String[] cardPin;

	private String memberName; // 회원명
	private String memberEmail; // 회원이메일
}
