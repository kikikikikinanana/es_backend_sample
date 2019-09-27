package kr.co.shop.web.claim.model.master;

import kr.co.shop.web.claim.model.master.base.BaseOcClaimPayment;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcClaimPayment extends BaseOcClaimPayment {

	// 원주문번호
	private String orgOrderNo;

	// 원주문번호
	private String orderNo;

	// 결제수단코드 명
	private String pymntMeansCodeName;
	// 은행코드 명
	private String bankCodeName;
	// 결제발생사유코드 명
	private String ocrncRsnCodeName;
	// 결제상태코드 명
	private String pymntStatCodeName;
	// 에스크로전송응답코드
	private String escrSendRspnsCodeText;
	// 에스크로전송일시
	private java.sql.Timestamp escrSendDtm;

	// 마스킹 - 신용카드/체크카드
	private String cardMask;

	// String타입 pymntDtm
	private String strPymntDtm;
	// 디바이스코드명
	private String deviceCodeName;
	// 결제업체코드명
	private String pymntVndrCodeName;
	// 결제 누적 취소금(결제 취소 성공과 무관)
	private java.lang.Integer accumulatedPymntCnclAmt;
	// 결제 잔여금(결제 취소 성공과 무관)
	private java.lang.Integer remainPymntCnclAmt;
	// 실제 결제 취소금(결제취소 성공 기준)
	private java.lang.Integer realPymntCnclAmt;
	// 실제 결제 잔여금(결제취소 성공 기준)
	private java.lang.Integer realRemainPymntCnclAmt;
}
