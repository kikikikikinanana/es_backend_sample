package kr.co.shop.web.giftcard.model.master.base;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

@Data
public class BaseOcKakaoExchangeOrder extends BaseBean implements Serializable {

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드주문번호
	 */
	private String giftCardOrderNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 주문일시
	 */
	private java.sql.Timestamp orderDtm;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 사이트번호
	 */
	private String siteNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 회원번호
	 */
	private String memberNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 회원유형코드
	 */
	private String memberTypeCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 임직원여부
	 */
	private String empYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : OTS VIP 여부
	 */
	private String otsVipYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 디바이스코드
	 */
	private String deviceCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 교환금액
	 */
	private java.lang.Integer exchngAmt;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수취인명
	 */
	private String rcvrName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수취인핸드폰번호
	 */
	private String rcvrHdphnNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카드번호
	 */
	private String cardNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카드PIN번호
	 */
	private String cardPinNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수트랜재션ID
	 */
	private String trId;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수승인번호
	 */
	private String admitNum;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수쿠폰번호
	 */
	private String couponNum;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수금액
	 */
	private java.lang.Integer useAmount;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수후잔액
	 */
	private java.lang.Integer orderBalance;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수결과Data
	 */
	private String dataHash;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수결과코드
	 */
	private String resultCodeText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카카오회수현금영수증금액
	 */
	private java.lang.Integer cashReceiptAmount;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 비과세금액
	 */
	private java.lang.Integer noTaxAmount;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 등록자번호
	 */
	private String rgsterNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 등록일시
	 */
	private java.sql.Timestamp rgstDtm;

}
