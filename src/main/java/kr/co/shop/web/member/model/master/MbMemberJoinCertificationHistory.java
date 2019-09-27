package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberJoinCertificationHistory;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class MbMemberJoinCertificationHistory extends BaseMbMemberJoinCertificationHistory {

	private int validTime;

}
