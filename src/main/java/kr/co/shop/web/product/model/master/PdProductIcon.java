package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BasePdProductIcon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PdProductIcon extends BasePdProductIcon {

	/** 아이콘정보 */
	private CmProductIcon icon;

}
