package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BaseDpStoreProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpStoreProduct extends BaseDpStoreProduct {

	private String pagingSortType;

	private java.lang.Integer rowsPerPage;

	private java.lang.Integer pageNum;

}
