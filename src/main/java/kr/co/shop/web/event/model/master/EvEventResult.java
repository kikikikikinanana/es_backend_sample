package kr.co.shop.web.event.model.master;

import java.sql.Timestamp;

import kr.co.shop.web.event.model.master.base.BaseEvEventResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventResult extends BaseEvEventResult {

	private String eventName;

	private String prevEventNo;

	private String nextEventNo;

	private String prevEventName;

	private String nextEventName;

	private Timestamp prevPblcYmd;

	private Timestamp nextPclcYmd;

}
