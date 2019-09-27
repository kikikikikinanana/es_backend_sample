package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEventTargetCoupon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventTargetCoupon extends BaseEvEventTargetCoupon {

	private String cpnName;

	private String usePlaceGbnType;

	private String dscntType;

	private Integer dscntValue;

	private Integer minLimitSellAmt;

}
