package kr.co.shop.web.event.model.master;

import java.util.Date;

import kr.co.shop.web.event.model.master.base.BaseEvEventTargetProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventTargetProduct extends BaseEvEventTargetProduct {

	private String prdtName;

	private String rsvDlvyYmd;

	private java.sql.Timestamp sellStartDtm;

	private String brandName;

	private String sellAmt;

	private String imageUrl;

	private String engPrdtName;

	private String siteNo;

	private String chnnlNo;

	private String ctgrNo;

	private String pagingSortType;

	private String tabType;

	private String searchDate;

	private String searchCtgrName;

	private String bannerStatus;

	public long getSellStartTime() {
		long time = 0;

		if (getSellStartDtm() != null) {
			Date today = new Date();
			time = getSellStartDtm().getTime() - today.getTime();
		}
		return time / 1000;
	}

}
