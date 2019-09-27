package kr.co.shop.web.giftcard.model.master.base;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

@Data
public class BaseOcGiftCardOrder extends BaseBean implements Serializable {

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
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 임직원주문여부
	 */
	private String empOrderYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 회원번호
	 */
	private String memberNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 회원유형코드
	 */
	private String memberTypeCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 멤버십등급코드
	 */
	private String mbshpGradeCode;

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
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 주문자명
	 */
	private String buyerName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 주문자핸드폰번호
	 */
	private String buyerHdphnNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수취인명
	 */
	private String rcvrName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수취인핸드폰번호
	 */
	private String rcvrHdphnNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드번호
	 */
	private String giftCardNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제금액
	 */
	private java.lang.Integer pymntAmt;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드승인번호
	 */
	private String giftCardAprvNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드승인일시
	 */
	private java.sql.Timestamp giftCardAprvDtm;

	/**
	 * 카드번호
	 */
	private String cardNoText;

	/**
	 * 카드PIN번호
	 */
	private String cardPinNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드주문유형코드
	 */
	private String giftCardOrderTypeCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드상태코드
	 */
	private String giftCardStatCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 현금영수증발급여부
	 */
	private String cashRcptIssueYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 세금계산서발급상태코드
	 */
	private String taxBillIssueStatCode;

	/**
	 * MMS 전송 메세지
	 */
	private String mmsSendMesgText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : MMS재전송일시
	 */
	private java.sql.Timestamp mmsReSendDtm;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 주문상태코드
	 */
	private String orderStatCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 등록자번호
	 */
	private String rgsterNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 등록일시
	 */
	private java.sql.Timestamp rgstDtm;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수정자번호
	 */
	private String moderNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수정일시
	 */
	private java.sql.Timestamp modDtm;

}
