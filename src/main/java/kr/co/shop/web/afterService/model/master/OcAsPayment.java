package kr.co.shop.web.afterService.model.master;

import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.afterService.model.master.base.BaseOcAsPayment;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcAsPayment extends BaseOcAsPayment {

	private String deviceCodeName;
	private String pymntMeansCodeName;
	private String pymntVndrCode;
	private String pymntVndrCodeName;
	private String ocrncRsnCodeName;
	private String pymntStatCodeName;
	private String pymntCmlptDate;
	private String cardMask;
	private String strPymntDtm;

	public String getCardMask() {
		String cardMask = "";
		if (getPymntOrganNoText() != null && getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
			// if (getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
			cardMask = UtilsMasking.cardNumber(getPymntOrganNoText(), true);
		}
		return cardMask;
	}

}
