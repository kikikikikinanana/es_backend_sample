package kr.co.shop.web.display.model.master;

import kr.co.shop.web.display.model.master.base.BaseDpDisplayPage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpDisplayPage extends BaseDpDisplayPage {

	private String prdtNo;

	private String deviceCode;
}
