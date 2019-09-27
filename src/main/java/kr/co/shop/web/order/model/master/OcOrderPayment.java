package kr.co.shop.web.order.model.master;

import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.web.order.model.master.base.BaseOcOrderPayment;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcOrderPayment extends BaseOcOrderPayment {

	/**
	 * 결제 수단 명
	 */
	private String pymntMeansCodeName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 회원번호
	 */
	private String memberNo;

	/**
	 * 코드 추가 설명 1
	 */
	private String addInfo1;

	// 주문번호 배열
	private String[] orderNos;

	public String getCardMask() {
		String cardMask = "";
		if (getPymntOrganNoText() != null && getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
			cardMask = UtilsMasking.cardNumber(getPymntOrganNoText(), true);
		}
		return cardMask;
	}

	// 결제번호
	private String paymntNo;

	// 사유
	private String pymntReason;

	// 결제상태
	private String pymntStatCodeName;

	// 카드/실시간포인트 제외한 결제수단이 있는경우
	private int pymntMeansCnt;

	// 결제수단 변경 COUNT
	private int changeCnt;

	// 업체 주문 상품건수
	private int vendorPrdtCnt;

	// 클레임 접수 건수
	private int claimCnt;

	// 결제 모듈 설정겂
	private KcpPaymentApproval kcpPaymentApproval;

	// 카드 인지 실시간인지 구분코드
	private String pymntMeansCode;

}
