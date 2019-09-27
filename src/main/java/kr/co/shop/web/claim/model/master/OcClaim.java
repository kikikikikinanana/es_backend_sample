package kr.co.shop.web.claim.model.master;

import java.util.List;

import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.web.claim.model.master.base.BaseOcClaim;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcClaim extends BaseOcClaim {

	// 코드필드
	private String codeField;

	// 클레임상품 배열
	private OcClaimProduct[] ocClaimProducts;

	// 클레임상품목록
	private List<OcClaimProduct> ocClaimProductList;

	private java.lang.Integer ocClaimProductListSize;

	// ~ 일자 부터
	private String fromDate;

	// ~ 일자 까지
	private String toDate;

	// 클레임 접수일자
	private String strClmDtm;

	// 클레임 접수완료일자
	private String strModDtm;

	// 원주문 접수일자
	private String strOrgOrderDtm;

	// 주문고객 이메일
	private String buyerEmailAddrText;

	// 은행코드 명
	private String bankCodeName;

	// 클레임사유코드 명
	private String clmRsnCodeName;

	// 클레임기타사유
	private String clmEtcRsnText;

	// 상품타입코드 배열
	private String[] prdtTypeCodeList;

	// kcp 결제 승인 model
	private KcpPaymentApproval kcpPaymentApproval;

	// 클레임결제 model
	private OcClaimPayment ocClaimPayment;

	// 상품관련파일순번
	private java.lang.Integer prdtRltnFileSeq;

	// 비회원 주문 번호
	private String nonUserOrderNo;
	// 비회원 주문 비밀번호
	private String nonUserOrderPw;

	// 리오더 주문번호
	private String reOrderNo;

	// 결제수단코드
	private String pymntMeansCode;

	// 결제수단코드 : 무통장입금(가상계좌)
	private String pymntMeansCodeVirtualAccount;

	// 가상계좌입금여부 YN
	private String vrtlAcntYn;

	/**
	 * 취소/반품 - 환불금액 - 취소금액영역
	 */
	// 주문금액
	private java.lang.Integer sumOrderAmt;
	// 환불 배송비
	private java.lang.Integer sumDlvyAmt;

	// 상품 유형 코드
	private String prdtTypeCode;

	// 환수환불구분타입
	private String redempRfndGbnType;
	// 자사처리대상여부
	private String mmnyProcTrgtYn;
	// 주결제수단여부
	private String mainPymntMeansYn;
	// 이력구분타입
	private String histGbnType;

}
