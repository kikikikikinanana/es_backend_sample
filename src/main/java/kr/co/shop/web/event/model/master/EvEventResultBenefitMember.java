package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEventResultBenefitMember;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventResultBenefitMember extends BaseEvEventResultBenefitMember {

	private String hdphnNoText;

	private String memberName;
}
