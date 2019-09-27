package kr.co.shop.web.display.model.master;

import java.util.List;

import kr.co.shop.web.display.model.master.base.BaseBdPopup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BdPopup extends BaseBdPopup {

	private List<BdPopupDevice> bdPopupDevices;
	private List<BdPopupDisplayPosition> bdPopupDisplayPositions;

	private String popupOpenType;

	private String dispPostnUrl;

	private String[] dispLimitTypes;
	private List<String> exceptions;
}
