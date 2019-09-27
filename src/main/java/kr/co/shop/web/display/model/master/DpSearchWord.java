package kr.co.shop.web.display.model.master;

import kr.co.shop.web.display.model.master.base.BaseDpSearchWord;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpSearchWord extends BaseDpSearchWord {

	private String searchBarWord;

	private int rank;

}
