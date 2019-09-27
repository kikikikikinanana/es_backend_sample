package kr.co.shop.web.giftcard.model.master.base;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

@Data
public class BaseOcGiftCardOrderPayment extends BaseBean implements Serializable {

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드주문번호
	 */
	private String giftCardOrderNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 기프트카드주문결제순번
	 */
	private java.lang.Short giftCardOrderPymntSeq;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제일시
	 */
	private java.sql.Timestamp pymntDtm;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 디바이스코드
	 */
	private String deviceCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 주결제수단여부
	 */
	private String mainPymntMeansYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제수단코드
	 */
	private String pymntMeansCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제업체코드
	 */
	private String pymntVndrCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제기관코드
	 */
	private String pymntOrganCodeText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제기관명
	 */
	private String pymntOrganName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제기관번호
	 */
	private String pymntOrganNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 무이자여부
	 */
	private String intrstFreeYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 할부기간
	 */
	private java.lang.Short instmtTermCount;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카드구분
	 */
	private String cardGbnType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 카드유형
	 */
	private String cardType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제승인번호
	 */
	private String pymntAprvNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제금액
	 */
	private java.lang.Integer pymntAmt;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 현금영수증발급여부
	 */
	private String cashRcptIssueYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 현금영수증승인번호
	 */
	private String cashRcptAprvNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 현금영수증거래번호
	 */
	private String cashRcptDealNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 상품권PIN번호
	 */
	private String giftCardPinNoText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제상태코드
	 */
	private String pymntStatCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 응답코드
	 */
	private String rspnsCodeText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 응답메시지
	 */
	private String rspnsMesgText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 결제로그
	 */
	private String pymntLogInfo;

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
