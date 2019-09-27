package kr.co.shop.web.promotion.model.master;

import kr.co.shop.web.promotion.model.master.base.BasePrCouponApplyProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrCouponApplyProduct extends BasePrCouponApplyProduct {

	private String pagingSortType;

	private String siteNo;

	private String chnnlNo;

}
