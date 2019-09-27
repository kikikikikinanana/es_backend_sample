package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberPoint;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberPoint extends BaseMbMemberPoint {
	private int point;
	private int eventPoint;

}
