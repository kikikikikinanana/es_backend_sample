package kr.co.shop.web.product.model.master;

import kr.co.shop.web.promotion.model.master.PrCoupon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PdProductCoupon extends PrCoupon {

	/** 할인금액 */
	private Integer dscntAmt;

}
