package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberCertificationHistory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberCertificationHistory extends BaseMbMemberCertificationHistory {

	private int validTime;
	private String loginId;

}
