package kr.co.shop.web.display.model.master;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kr.co.shop.web.display.model.master.base.BaseDpWebzine;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpWebzine extends BaseDpWebzine {

	// 페이지 번호
	private java.lang.Integer pageNum;

	private List<DpWebzineDetailImage> dpWebzineDetailImages;

	private List<PdProductWrapper> dpWebzineProducts;

	private String prdtNo;

	// 사이트,채널번호
	private String siteNo;
	private String chnnlNo;

	private String rgstDtmLabel;

	public String getRgstDtmLabel() {

		if (this.getRgstDtm() != null) {

			SimpleDateFormat fmt = new SimpleDateFormat("MMM dd, yyyy", new Locale("en", "US"));

			return fmt.format(getRgstDtm()).toUpperCase();
		}

		return null;
	}
}
