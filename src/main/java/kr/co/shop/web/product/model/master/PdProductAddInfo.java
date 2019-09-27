package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BasePdProductAddInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PdProductAddInfo extends BasePdProductAddInfo {

	/** 정보고시이름 */
	private String infoNotcName;

}
