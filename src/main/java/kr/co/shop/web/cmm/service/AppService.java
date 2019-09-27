package kr.co.shop.web.cmm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.Const;
import kr.co.shop.web.cmm.model.master.CmPushAppDownloadMember;
import kr.co.shop.web.cmm.repository.master.CmPushAppDownloadMemberDao;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbPersistentLogins;
import kr.co.shop.web.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppService {

	@Autowired
	MemberService memberService;

	@Autowired
	CmPushAppDownloadMemberDao cmPushAppDownloadMemberDao;

	/**
	 * @Desc : fcm 토큰을 포함한 디바이스 정보 저장
	 * @Method Name : setFcmToken
	 * @Date : 2019. 6. 27.
	 * @Author : Kimyounghyun
	 * @param cmPushAppDownloadMember
	 */
	public void setFcmToken(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception {
		cmPushAppDownloadMemberDao.insertFcmToken(cmPushAppDownloadMember);
	}

	/**
	 * @Desc : 디바이스 토큰을 저장한다.
	 * @Method Name : setDeviceToken
	 * @Date : 2019. 6. 28.
	 * @Author : Kimyounghyun
	 * @param cmPushAppDownloadMember
	 */
	public void setDeviceToken(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception {
		MbMember mbMember = new MbMember();
		mbMember.setLoginId(cmPushAppDownloadMember.getLoginId());

		mbMember = memberService.getMemberByLoginId(mbMember);
		cmPushAppDownloadMember.setMemberNo(mbMember.getMemberNo());

		cmPushAppDownloadMemberDao.updateDeviceToken(cmPushAppDownloadMember);
	}

	/**
	 * @Desc : app push 수신여부를 저장한다.
	 * @Method Name : setReceivePush
	 * @Date : 2019. 7. 1.
	 * @Author : Kimyounghyun
	 * @param cmPushAppDownloadMember
	 */
	public void setReceivePush(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception {
		cmPushAppDownloadMemberDao.updateReceivePush(cmPushAppDownloadMember);
	}

	/**
	 * @Desc : 바이오인증 로그인 사용여부를 저장한다. 바이오인증 로그인이 활성화되면 자동로그인은 off처리한다.
	 * @Method Name : setBioLogin
	 * @Date : 2019. 7. 1.
	 * @Author : Kimyounghyun
	 * @param cmPushAppDownloadMember
	 */
	public void setBioLogin(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception {
		if (Const.BOOLEAN_TRUE.equals(cmPushAppDownloadMember.getBioCrtfcLoginUseYn())) {
			MbPersistentLogins mbPersistentLogins = new MbPersistentLogins();
			mbPersistentLogins.setId(cmPushAppDownloadMember.getLoginId());
			mbPersistentLogins.setSiteNo(cmPushAppDownloadMember.getSiteNo());

			memberService.removeUserTokens(mbPersistentLogins);
		}

		cmPushAppDownloadMemberDao.updateBioLogin(cmPushAppDownloadMember);

	}

}
