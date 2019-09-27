package kr.co.shop.web.claim.model.master;

import kr.co.shop.web.claim.model.master.base.BaseOcClaimCertificationHistory;
import kr.co.shop.web.member.model.master.MbMember;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcClaimCertificationHistory extends BaseOcClaimCertificationHistory {

	private MbMember mbMember; // mbMember model

}
