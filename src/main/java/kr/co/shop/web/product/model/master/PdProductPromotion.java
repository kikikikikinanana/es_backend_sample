package kr.co.shop.web.product.model.master;

import kr.co.shop.web.promotion.model.master.PrPromotion;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PdProductPromotion extends PrPromotion {

	/** 정상가 */
	private Integer normalAmt;

	/** 할인금액 */
	private Integer dscntAmt;

}
