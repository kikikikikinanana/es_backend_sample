package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEventAttendanceCheckMember;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventAttendanceCheckMember extends BaseEvEventAttendanceCheckMember {

	private String calDay;

	private String calWeek;

	private String memberNo;

	private String attendYn;

}
