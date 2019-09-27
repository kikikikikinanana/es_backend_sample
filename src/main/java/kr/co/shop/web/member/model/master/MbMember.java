package kr.co.shop.web.member.model.master;

import kr.co.shop.common.request.annotation.ParameterOption;
import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.web.member.model.master.base.BaseMbMember;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class MbMember extends BaseMbMember {
	private String loginFailIncrease;
	private String pswdChangeYn;

	/**
	 * 기념일
	 */
	private java.sql.Timestamp oriAnnvrYmd;

	/**
	 * 링크 url1
	 */
	private String linkUrl1;

	/**
	 * 링크 url2
	 */
	private String linkUrl2;

	@ParameterOption(target = "termsSeq")
	private MbMemberTermsAgree[] termsList;

	private String empLoginYn;

	/**
	 * 은행명
	 */
	private String bankCodeName;

	/**
	 * 고객센터 회원 정보 마스킹
	 */
	public void setMemberTextMasking() {
		this.setLoginId(UtilsMasking.loginId(this.getLoginId()));
		this.setHdphnNoText(UtilsMasking.cellPhoneNumber(this.getHdphnNoText()));
		this.setEmailAddrText(UtilsMasking.emailAddress(this.getEmailAddrText()));
	}

	private String deviceToken;
}
