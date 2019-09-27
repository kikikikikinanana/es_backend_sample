package kr.co.shop.web.promotion.model.master;

import java.util.List;

import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.promotion.model.master.base.BasePrPlanningDisplay;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrPlanningDisplay extends BasePrPlanningDisplay {

	private List<PdProduct> pdProducts;

	private String plndpCornerSeq;

	private String memberTypeCode;

	private String mbshpGradeCode;

	private String isPreview;

	private String deviceCode;

	private String siteNo;

}
