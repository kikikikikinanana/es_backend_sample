package kr.co.shop.web.vendor.model.master;

import kr.co.shop.web.vendor.model.master.base.BaseVdVendorProductAllNotice;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class VdVendorProductAllNotice extends BaseVdVendorProductAllNotice {

	/** 상품번호 */
	private String prdtNo;

}
