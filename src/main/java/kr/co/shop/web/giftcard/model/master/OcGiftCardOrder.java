package kr.co.shop.web.giftcard.model.master;

import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.web.giftcard.model.master.base.BaseOcGiftCardOrder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcGiftCardOrder extends BaseOcGiftCardOrder {

	private String pymntMeansCode;
	private String kcpPayType;
	private String imageUrl;
	private String historyStartDtm;
	private String historyEndDtm;
	private String pymntAprvNoText;
	private String couponEndDate;
	private String giftCardName;

	private String memberEmail; // 회원이메일
	/**
	 * kcp결제 모듈에 필요한 객체
	 */
	private KcpPaymentApproval kcpPaymentApproval;

}
