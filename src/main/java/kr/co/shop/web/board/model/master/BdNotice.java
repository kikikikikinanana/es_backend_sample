package kr.co.shop.web.board.model.master;

import kr.co.shop.web.board.model.master.base.BaseBdNotice;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BdNotice extends BaseBdNotice {

	private String topNotcCheck;
	private String siteName;
	private String rgstDt;

}
