package kr.co.shop.web.product.model.master;

import java.util.List;

import kr.co.shop.web.product.model.master.base.BasePdRelationProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PdRelationProduct extends BasePdRelationProduct {

	/** 브랜드번호 */
	private String brandNo;

	/** 연관상품정보 */
	private List<PdProduct> product;

}
