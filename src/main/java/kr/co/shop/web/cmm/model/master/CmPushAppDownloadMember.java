package kr.co.shop.web.cmm.model.master;

import kr.co.shop.web.cmm.model.master.base.BaseCmPushAppDownloadMember;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CmPushAppDownloadMember extends BaseCmPushAppDownloadMember {

	private String loginId;

}
