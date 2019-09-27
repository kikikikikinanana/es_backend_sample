package kr.co.shop.web.display.model.master;

import java.util.List;

import kr.co.shop.web.display.model.master.base.BaseDpCategory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpCategory extends BaseDpCategory {

	List<DpCategory> subCategoryList;

	private boolean checkedCategory = false;

	private String pagingSortType;

	private String prdtNo;

	private String deviceCode;

	private Integer discountRate;

	private String newYn;

	private String brandNo;

}
