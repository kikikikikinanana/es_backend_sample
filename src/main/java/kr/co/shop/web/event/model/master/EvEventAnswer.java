package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEventAnswer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventAnswer extends BaseEvEventAnswer {

	private String memberNo;

	private String loginId;

	private String limitType;

}
