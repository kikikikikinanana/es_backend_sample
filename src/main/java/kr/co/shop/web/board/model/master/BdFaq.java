package kr.co.shop.web.board.model.master;

import kr.co.shop.web.board.model.master.base.BaseBdFaq;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BdFaq extends BaseBdFaq {

	private String searchValue;
	private String faqCount;

}
