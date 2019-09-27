package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BaseDpBrandPage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpBrandPage extends BaseDpBrandPage {

	private String pagingSortType;

	private java.lang.Integer rowsPerPage;

	private java.lang.Integer pageNum;

	private java.lang.Integer brandStyleSeq;

	private String ctgrNo;

	private String chnnlNo;

}
