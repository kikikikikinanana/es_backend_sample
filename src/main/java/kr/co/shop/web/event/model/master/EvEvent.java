package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEvent extends BaseEvEvent {

	private String memberNo;

	private String startYmd;

	private String endYmd;

	private String myResultCnt;

	private String resultCnt;

	private String imageUrl;

	private String eventProgressStatus;

	private String winningEvent;

	private String eventJoinYmd;

	private String statusType;

	private String chnnlName;

	private String chnnlNo;

}
