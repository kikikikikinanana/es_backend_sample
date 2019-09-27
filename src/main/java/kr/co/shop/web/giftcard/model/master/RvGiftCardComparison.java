package kr.co.shop.web.giftcard.model.master;

import kr.co.shop.web.giftcard.model.master.base.BaseRvGiftCardComparison;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RvGiftCardComparison extends BaseRvGiftCardComparison {

	private String pymntMeansCode;
	private String historyStartDtm;
	private String historyEndDtm;

}
