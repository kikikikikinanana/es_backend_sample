package kr.co.shop.web.display.model.master;

import kr.co.shop.common.util.UtilsText;
import kr.co.shop.web.display.model.master.base.BaseDpDisplayPageCornerSet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpDisplayPageCornerSet extends BaseDpDisplayPageCornerSet {

	/**
	 * 설명 : 전시컨텐츠수
	 */
	private java.lang.Integer dispContCount;

	private String linkUrl;

	public String getLinkUrl() {
		String url = this.getAddInfo7();
		if (UtilsText.equals(this.getContTypeCode(), "10002")) {
			switch (this.getAddInfo5()) {
			case "E":
				url = UtilsText.join("/promotion/event/detail?eventNo=", url);
				break;
			case "P":
				url = UtilsText.join("/promotion/planning-display/detail?plndpNo=", url);
				break;
			case "B":
				url = UtilsText.join("/product/brand/page?brandNo=", url);
				break;
			case "N":
				url = "javascript:void(0);";
				break;
			default:
				break;
			}
		}

		return url;
	}

}
