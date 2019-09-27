package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BasePdProductOption;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PdProductOption extends BasePdProductOption {

	/** 재고량 테이블기준 재고수량 합계(AI+AW+AS+VD) */
	private Integer stockQty;

	private PdProductOptionPriceHistory optionPrice;

}
