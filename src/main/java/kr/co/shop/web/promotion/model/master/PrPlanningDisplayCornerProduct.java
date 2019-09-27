package kr.co.shop.web.promotion.model.master;

import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.promotion.model.master.base.BasePrPlanningDisplayCornerProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrPlanningDisplayCornerProduct extends BasePrPlanningDisplayCornerProduct {

	private PdProduct pdProduct;

}
