package kr.co.shop.web.member.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.constant.BaseCommonCode;
import kr.co.shop.common.message.Message;
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsCrypt;
import kr.co.shop.common.util.UtilsHashKey;
import kr.co.shop.common.util.UtilsRequest;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.constant.MailCode;
import kr.co.shop.util.Kmcism;
import kr.co.shop.util.Siren24;
import kr.co.shop.util.UtilsDate;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.cmm.model.master.CmPushAppDownloadMember;
import kr.co.shop.web.cmm.model.master.CmStore;
import kr.co.shop.web.cmm.service.MailService;
import kr.co.shop.web.cmm.service.MessageService;
import kr.co.shop.web.cmm.service.StoreService;
import kr.co.shop.web.cmm.vo.MailTemplateVO;
import kr.co.shop.web.cmm.vo.MessageVO;
import kr.co.shop.web.member.exception.MemberException;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberCertificationHistory;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.member.model.master.MbMemberEasyLogin;
import kr.co.shop.web.member.model.master.MbMemberExpostSavePoint;
import kr.co.shop.web.member.model.master.MbMemberInterestBrand;
import kr.co.shop.web.member.model.master.MbMemberInterestStore;
import kr.co.shop.web.member.model.master.MbMemberLoginHistory;
import kr.co.shop.web.member.model.master.MbMemberPoint;
import kr.co.shop.web.member.model.master.MbMemberTermsAgree;
import kr.co.shop.web.member.model.master.MbPersistentLogins;
import kr.co.shop.web.member.repository.master.MbMemberCertificationHistoryDao;
import kr.co.shop.web.member.repository.master.MbMemberDao;
import kr.co.shop.web.member.repository.master.MbMemberDeliveryAddressDao;
import kr.co.shop.web.member.repository.master.MbMemberEasyLoginDao;
import kr.co.shop.web.member.repository.master.MbMemberExpostSavePointDao;
import kr.co.shop.web.member.repository.master.MbMemberInterestBrandDao;
import kr.co.shop.web.member.repository.master.MbMemberInterestStoreDao;
import kr.co.shop.web.member.repository.master.MbMemberJoinCertificationHistoryDao;
import kr.co.shop.web.member.repository.master.MbMemberLoginHistoryDao;
import kr.co.shop.web.member.repository.master.MbMemberPointDao;
import kr.co.shop.web.member.repository.master.MbMemberTermsAgreeDao;
import kr.co.shop.web.member.repository.master.MbPersistentLoginsDao;
import kr.co.shop.web.member.vo.CertificationVO;
import kr.co.shop.web.mypage.service.MypageService;
import kr.co.shop.web.mypage.vo.MypageVO;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.service.OrderOtherPartService;
import kr.co.shop.web.order.vo.OrderOtherPartVo;
import kr.co.shop.web.product.service.BrandPageService;
import kr.co.shop.web.system.service.SystemService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {

	@Autowired
	private MemberService memberService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private MypageService mypageService;

	@Autowired
	private BrandPageService brandPageService;

	@Autowired
	private MbMemberDao mbMemberDao;

	@Autowired
	private MbMemberTermsAgreeDao mbMemberTermsAgreeDao;

	@Autowired
	private MbPersistentLoginsDao mbPersistentLoginsDao;

	@Autowired
	private MbMemberEasyLoginDao mbMemberEasyLoginDao;

	@Autowired
	private MbMemberLoginHistoryDao mbMemberLoginHistoryDao;

	@Autowired
	private MbMemberJoinCertificationHistoryDao mbMemberJoinCertificationHistoryDao;

	@Autowired
	private MbMemberCertificationHistoryDao mbMemberCertificationHistoryDao;

	@Autowired
	private MbMemberExpostSavePointDao mbMemberExpostSavePointDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private MbMemberDeliveryAddressDao mbMemberDeliveryAddressDao;

	@Autowired
	private MbMemberInterestStoreDao mbMemberInterestStoreDao;

	@Autowired
	private MbMemberPointDao mbMemberPointDao;

	@Autowired
	private MessageService messageService;

	@Autowired
	private OrderOtherPartService orderOtherPartService;

	@Autowired
	private MbMemberInterestBrandDao mbMemberInterestBrandDao;

	public MbMember getMemberByLoginId(MbMember mbMember) throws Exception {
		mbMember.setLeaveYn(Const.BOOLEAN_FALSE);
		return mbMemberDao.selectByLoginId(mbMember);
	}

	public void createNewToken(MbPersistentLogins mbPersistentLogins) throws Exception {
		mbPersistentLoginsDao.insert(mbPersistentLogins);
	}

	public void updateToken(MbPersistentLogins mbPersistentLogins) throws Exception {
		mbPersistentLoginsDao.update(mbPersistentLogins);
	}

	public MbPersistentLogins getTokenForSeries(MbPersistentLogins mbPersistentLogins) throws Exception {
		return mbPersistentLoginsDao.selectByPrimaryKey(mbPersistentLogins);
	}

	public void removeUserTokens(MbPersistentLogins mbPersistentLogins) throws Exception {
		mbPersistentLoginsDao.removeUserTokens(mbPersistentLogins);
	}

	public List<MbMember> selectMemberList(MbMember mbMember) throws Exception {
		mbMember.setLeaveYn(Const.BOOLEAN_FALSE);
		return mbMemberDao.selectMemberList(mbMember);
	}

	public MbMember selectMemberInfo(MbMember mbMember) throws Exception {
		mbMember.setLeaveYn(Const.BOOLEAN_FALSE);
		return mbMemberDao.selectMemberInfo(mbMember);
	}

	public List<MbMember> selectIdSerarchInfoList(MbMember mbMember) throws Exception {
		mbMember.setLeaveYn(Const.LEAVE_RESULT);
		return mbMemberDao.selectIdSerarchInfoList(mbMember);
	}

	public MbMember getSnsLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		MbMember member = new MbMember();
		MbMemberEasyLogin easyLogin = memberService.getMemberEasyLoginAccount(mbMemberEasyLogin);

		if (easyLogin != null) {
			member.setMemberNo(easyLogin.getMemberNo());
			member = this.selectMemberInfo(member);
		}

		return member;
	}

	/**
	 * @Desc : ?????? SNS ???????????? ??????
	 * @Method Name : createSnsConnectInfo
	 * @Date : 2019. 5. 9.
	 * @Author : ?????????
	 * @param easyLogin
	 * @throws Exception
	 */
	public void createSnsConnectInfo(MbMemberEasyLogin easyLogin) throws Exception {
		mbMemberEasyLoginDao.insert(easyLogin);
	}

	/**
	 * @Desc : ?????? ????????? ?????? ????????? ????????????
	 * @Method Name : updateLoginFailCount
	 * @Date : 2019. 5. 14.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void updateLoginFailCount(MbMember mbMember) throws Exception {
		mbMemberDao.updateLoginFailCount(mbMember);
	}

	/**
	 * @Desc : ?????? ?????? ??????
	 * @Method Name : updateMemberInfo
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void updateMemberInfo(MbMember params) throws Exception {

		MbMember oldData = mbMemberDao.selectByPrimaryKey(params);

		List<String> userInfoChangeList = this.setMemberParam(oldData, params);

		try {
			if (!userInfoChangeList.isEmpty()) {

				params.setInfoModYn(Const.BOOLEAN_TRUE);

				// TODO ????????? ???????????? ??????

				// ???????????? ??????
				MessageVO messageVO = new MessageVO();
				messageVO.setMesgId("MC-01013");
				messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
				messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
				messageVO.setRecvTelNoText(params.getHdphnNoText());
				messageVO.setRcvrName(params.getMemberName());
				messageVO.setReal(true);

				String requestDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
				String landingUrl = UtilsText.concat("https://art.com", "/mypage/private-info");

				Map<String, String> map = new HashMap<>();
				map.put("memberName", params.getMemberName());
				map.put("chngList", UtilsText.join(userInfoChangeList, ","));
				map.put("requestDate", requestDate);
				map.put("landingUrl", landingUrl);
				messageVO.setMessageTemplateModel(map);

				messageService.setSendMessageProcessNoTrx(messageVO);

			}
		} catch (Exception e) {
			log.error("???????????? ?????? ????????? error", e.getMessage());
		}

		mbMemberDao.update(params);

	}

	public List<String> setMemberParam(MbMember oldData, MbMember params) {

		List<String> userInfoChangeList = new ArrayList<>();

		// ?????????
		if (UtilsText.isNotBlank(params.getEmailAddrText())
				&& !UtilsText.equals(oldData.getEmailAddrText(), params.getEmailAddrText())) {
			userInfoChangeList.add("?????????");
		}
		// ????????? ??????
		if (UtilsText.isNotBlank(params.getHdphnNoText())
				&& !UtilsText.equals(oldData.getHdphnNoText(), params.getHdphnNoText())) {
			userInfoChangeList.add("????????? ??????");
		}
		// ?????????
		if (params.getAnnvrYmd() != null
				&& !UtilsText.equals(String.valueOf(params.getAnnvrYmd()), String.valueOf(oldData.getAnnvrYmd()))) {
			params.setAnnvrYmdModDtm(UtilsDate.getSqlTimeStamp()); // ???????????? ????????????
			userInfoChangeList.add("?????????");
		}
		int marketingChgCnt = 0;
		// SMS????????????
		if (UtilsText.isNotBlank(params.getSmsRecvYn())
				&& !UtilsText.equals(oldData.getSmsRecvYn(), params.getSmsRecvYn())) {
			marketingChgCnt++;
			params.setSmsRecvYnModDtm(UtilsDate.getSqlTimeStamp());
		}
		// ?????????????????????
		if (UtilsText.isNotBlank(params.getEmailRecvYn())
				&& !UtilsText.equals(oldData.getEmailRecvYn(), params.getEmailRecvYn())) {
			marketingChgCnt++;
			params.setEmailRecvYnModDtm(UtilsDate.getSqlTimeStamp());
		}
		if (marketingChgCnt > 0) {
			userInfoChangeList.add("????????? ?????? ?????? ??????");
		}

		int nightMarketingChgCnt = 0;
		// ??????SMS????????????
		if (UtilsText.isNotBlank(params.getNightSmsRecvYn())
				&& !UtilsText.equals(oldData.getNightSmsRecvYn(), params.getNightSmsRecvYn())) {
			nightMarketingChgCnt++;
			params.setNightSmsRecvYnModDtm(UtilsDate.getSqlTimeStamp());
		}
		// ???????????????????????????
		if (UtilsText.isNotBlank(params.getNightEmailRecvYn())
				&& !UtilsText.equals(oldData.getNightEmailRecvYn(), params.getNightEmailRecvYn())) {
			nightMarketingChgCnt++;
			params.setNightEmailRecvYnModDtm(UtilsDate.getSqlTimeStamp());
		}
		if (nightMarketingChgCnt > 0) {
			userInfoChangeList.add("?????? ????????? ?????? ?????? ??????");
		}

		if (UtilsText.equals(params.getBlackListYn(), Const.BOOLEAN_FALSE)) {
			params.setBlackListTypeCode("");
		}
		params.setModDtm(UtilsDate.getSqlTimeStamp());

		return userInfoChangeList;
	}

	/**
	 * @Desc : ?????? ?????? ?????? ??????
	 * @Method Name : createMemberHistory
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param mbMemberLoginHistory
	 * @throws Exception
	 */
	public void createMemberHistory(MbMemberLoginHistory mbMemberLoginHistory) throws Exception {
		mbMemberLoginHistoryDao.insertMemberHistory(mbMemberLoginHistory);
	}

	/**
	 * @Desc : SAFE_KEY_TEXT(?????????) ??????
	 * @Method Name : getSafeKeyText
	 * @Date : 2019. 3. 25.
	 * @Author : ?????????
	 * @param memberNo
	 * @return
	 */
	public String getSafeKey(String memberNo) throws Exception {
		return mbMemberDao.getSafeKey(memberNo);
	}

	/**
	 * @Desc : ????????? ???????????? ??????.
	 * @Method Name : createCertificationNumber
	 * @Date : 2019. 3. 28.
	 * @Author : Kimyounghyun
	 * @param memberJoinCertificationHistory
	 * @throws Exception
	 */
	/*
	 * public void createCertNumberHdphn(CertificationVO certificationVO) throws
	 * Exception { limitExceededCertificationNumber(certificationVO);
	 *
	 * MbMemberCertificationHistory memberCertificationHistory = new
	 * MbMemberCertificationHistory();
	 *
	 * String certificationNumber =
	 * UtilsHashKey.makeRandomNumber(Const.CERTIFY_NUMBER_DIGIT, true); String
	 * sessionId = UtilsRequest.getSession().getId();
	 *
	 * // TODO SMS(?????????) ????????? ?????? MessageVO messageVO = new MessageVO();
	 * messageVO.setMesgContText("[A-RT] ???????????? (" + certificationNumber +
	 * ")??? ??????????????????"); messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
	 * messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
	 * messageVO.setRecvTelNoText(certificationVO.getCrtfcNoSendInfo());
	 * messageVO.setRcvrName(certificationVO.getMemberName());
	 * messageVO.setReal(true);
	 *
	 * messageService.setSendMessageProcess(messageVO);
	 *
	 * memberCertificationHistory.setCrtfcPathCode(BaseCommonCode.
	 * CRTFC_PATH_CODE_MEMBER_MODIFY);
	 * memberCertificationHistory.setCrtfcTypeCode(BaseCommonCode.
	 * CRTFC_TYPE_CODE_PHONE);
	 * memberCertificationHistory.setCrtfcNoText(certificationNumber);
	 * memberCertificationHistory.setCrtfcNoSendInfo(certificationVO.
	 * getCrtfcNoSendInfo());
	 * memberCertificationHistory.setAccessIpText(systemService.getIpAddress());
	 * memberCertificationHistory.setSessionId(sessionId);
	 * memberCertificationHistory.setCrtfcSuccessYn(Const.BOOLEAN_FALSE);
	 *
	 * mbMemberCertificationHistoryDao.insertCertification(
	 * memberCertificationHistory);
	 *
	 * }
	 */

	// TODO : ???????????? ?????? ??????
	public String createCertNumberHdphn(CertificationVO certificationVO) throws Exception {
		limitExceededCertificationNumber(certificationVO);

		MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();

		String certificationNumber = UtilsHashKey.makeRandomNumber(Const.CERTIFY_NUMBER_DIGIT, true);
		String sessionId = UtilsRequest.getSession().getId();

		// TODO SMS(?????????) ????????? ??????
		MessageVO messageVO = new MessageVO();
		messageVO.setMesgContText("[A-RT] ???????????? (" + certificationNumber + ")??? ??????????????????");
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		messageVO.setRecvTelNoText(certificationVO.getCrtfcNoSendInfo());
		messageVO.setRcvrName(certificationVO.getMemberName());
		messageVO.setReal(true);

		messageService.setSendMessageProcess(messageVO);

		memberCertificationHistory.setCrtfcPathCode(BaseCommonCode.CRTFC_PATH_CODE_MEMBER_MODIFY);
		memberCertificationHistory.setCrtfcTypeCode(BaseCommonCode.CRTFC_TYPE_CODE_PHONE);
		memberCertificationHistory.setCrtfcNoText(certificationNumber);
		memberCertificationHistory.setCrtfcNoSendInfo(certificationVO.getCrtfcNoSendInfo());
		memberCertificationHistory.setAccessIpText(systemService.getIpAddress());
		memberCertificationHistory.setSessionId(sessionId);
		memberCertificationHistory.setCrtfcSuccessYn(Const.BOOLEAN_FALSE);

		mbMemberCertificationHistoryDao.insertCertification(memberCertificationHistory);

		return certificationNumber;
	}

	/**
	 * @Desc : ????????? ???????????? ??????
	 * @Method Name : createCertNumberEmail
	 * @Date : 2019. 4. 9.
	 * @Author : ?????????
	 * @param memberCertificationHistory
	 * @throws Exception
	 */
	public void createCertNumberEmail(CertificationVO certificationVO) throws Exception {
		limitExceededCertificationNumber(certificationVO);
		MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();

		String certificationNumber = UtilsHashKey.makeRandomNumber(Const.CERTIFY_NUMBER_DIGIT, true);
		String sessionId = UtilsRequest.getSession().getId();

		certificationVO.setCertificationNumber(certificationNumber);
		mailService.sendMail(getMailTemplateVo(certificationVO));

		memberCertificationHistory.setCrtfcPathCode(certificationVO.getCrtfcPathCode());
		memberCertificationHistory.setCrtfcTypeCode(BaseCommonCode.CRTFC_TYPE_CODE_PHONE);
		memberCertificationHistory.setCrtfcNoText(certificationNumber);
		memberCertificationHistory.setCrtfcNoSendInfo(certificationVO.getCrtfcNoSendInfo());
		memberCertificationHistory.setAccessIpText(systemService.getIpAddress());
		memberCertificationHistory.setSessionId(sessionId);
		memberCertificationHistory.setCrtfcSuccessYn(Const.BOOLEAN_FALSE);

		mbMemberCertificationHistoryDao.insertCertification(memberCertificationHistory);
	}

	/**
	 * @Desc : ?????? ????????? vo??? ????????????.
	 * @Method Name : getMailTemplateVo
	 * @Date : 2019. 4. 11.
	 * @Author : Kimyounghyun
	 * @param certificationNumber
	 * @return
	 * @throws Exception
	 */
	private MailTemplateVO getMailTemplateVo(CertificationVO certificationVO) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("certificationNumber", certificationVO.getCertificationNumber());
		map.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		MailTemplateVO mailTemplateVO = new MailTemplateVO();
		mailTemplateVO.setMailTemplateId(MailCode.MEMBER_CERTIFICATION_NUMBER);
		mailTemplateVO.setMailTemplateModel(map);
		mailTemplateVO.setReceiverEmail(certificationVO.getCrtfcNoSendInfo());
		mailTemplateVO.setReceiverName(certificationVO.getMemberName());
		mailTemplateVO.setRealTime(true);

		return mailTemplateVO;
	}

	/**
	 * @Desc : ????????? ???????????? ????????? ??????
	 * @Method Name : validateCertificationNumber
	 * @Date : 2019. 3. 28.
	 * @Author : Kimyounghyun
	 * @param memberJoinCertificationHistory
	 * @throws Exception
	 */
	public void setValidateCertificationNumber(CertificationVO certificationVO) throws Exception {

		MbMember mbMember = new MbMember();
		MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();

		memberCertificationHistory.setCrtfcNoSendInfo(certificationVO.getCrtfcNoSendInfo());
		memberCertificationHistory.setCrtfcNoText(certificationVO.getCrtfcNoText());
		memberCertificationHistory.setCrtfcSuccessYn(Const.BOOLEAN_FALSE);
		memberCertificationHistory.setValidTime(Const.CERTIFY_NUMBER_VALID_TIME);

		memberCertificationHistory = mbMemberCertificationHistoryDao.selectCertification(memberCertificationHistory);

		if (memberCertificationHistory == null) {
			throw new MemberException.InvalidCertificationNumberException("????????? ???????????? 6????????? ??????????????????.");
		}

		if (UtilsText.isNotEmpty(certificationVO.getLoginId())) {
			mbMember.setLoginId(certificationVO.getLoginId());
			mbMember = selectMemberInfo(mbMember);
		}

		if (UtilsText.isNotEmpty(mbMember.getMemberNo())) {
			memberCertificationHistory.setMemberNo(mbMember.getMemberNo());
		} else {
			memberCertificationHistory.setMemberNo(certificationVO.getMemberNo());
		}

		memberService.updateCertificationNumber(memberCertificationHistory);
	}

	/**
	 * @Desc : ????????? ???????????? ????????? ?????? ????????????
	 * @Method Name : updateCertificationNumber
	 * @Date : 2019. 3. 28.
	 * @Author : Kimyounghyun
	 * @param memberJoinCertificationHistory
	 */
	public void updateCertificationNumber(MbMemberCertificationHistory memberCertificationHistory) {
		memberCertificationHistory.setCrtfcSuccessYn(Const.BOOLEAN_TRUE);

		mbMemberCertificationHistoryDao.updateCertificationNumber(memberCertificationHistory);
	}

	/**
	 * @Desc : ?????? ?????? ??? ???????????? ????????????
	 * @Method Name : updateCertificationMember
	 * @Date : 2019. 4. 10.
	 * @Author : ?????????
	 * @param mbMemberCertificationHistory
	 * @throws Exception
	 */
	public void updateCertificationMember(CertificationVO certificationVO) throws Exception {
		// ????????? ????????? ?????? ????????? ????????? ???????????? ??????????????? ??????
		MbMember mbMember = new MbMember();
		MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();

		mbMember.setLoginId(certificationVO.getLoginId());
		mbMember = selectMemberInfo(mbMember);

		memberCertificationHistory.setCrtfcNoSendInfo(certificationVO.getCrtfcNoSendInfo());
		memberCertificationHistory.setCrtfcNoText(certificationVO.getCrtfcNoText());
		memberCertificationHistory.setCrtfcSuccessYn(Const.BOOLEAN_TRUE);
		memberCertificationHistory = mbMemberCertificationHistoryDao.selectCertification(memberCertificationHistory);

		if (memberCertificationHistory == null) {
			throw new MemberException.NotCertificationNumberInfo("??????????????? ???????????? ????????????.");
		}

		memberCertificationHistory.setMemberNo(mbMember.getMemberNo());
		memberService.updateCertificationNumber(memberCertificationHistory);
	}

	/**
	 * @Desc : ????????????
	 * @Method Name : createNewMemberInfo
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void createNewMemberInfo(MbMember mbMember) throws Exception {

		Pair<String, String> pair = UtilsCrypt.getEncryptPassword(mbMember.getPswdText());

		// ???????????? ?????????
		mbMember.setPswdSaltText(pair.getSecond());
		mbMember.setPswdText(pair.getFirst());

		// ?????? ????????? ??????
		String memberId = memberService.selectMemberSequence();

		// ????????????, ?????????, ?????????
		mbMember.setInfoModYn(Const.BOOLEAN_TRUE);
		mbMember.setMemberNo(memberId);
		mbMember.setRgsterNo(memberId);
		mbMember.setModerNo(memberId);
		// ???????????? ??????
		mbMember.setSmsRecvYn(
				UtilsText.equals("on", mbMember.getSmsRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);
		mbMember.setEmailRecvYn(
				UtilsText.equals("on", mbMember.getEmailRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);
		mbMember.setNightSmsRecvYn(
				UtilsText.equals("on", mbMember.getNightSmsRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);
		mbMember.setNightEmailRecvYn(
				UtilsText.equals("on", mbMember.getNightEmailRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);

		// safeKeySeq ?????? (????????? ????????? ?????? ??? ??????, ????????? ????????? safeKey??? ???????????? ??????)
		if (UtilsText.equals(mbMember.getMemberTypeCode(), BaseCommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)) {
			mbMember.setSafeKeySeq(hasSetSafeKeySeq(mbMember));

			// ????????? ???????????? ??????
			Pair<String, String> point = UtilsCrypt.getEncryptPassword(mbMember.getPointPswdText());
			mbMember.setPointPswdText(point.getFirst());
			mbMember.setPointPswdSaltText(point.getSecond());
		}

		// ????????????
		memberService.insertMemberInfo(mbMember);

		// ???????????? ??????
		memberService.insertMemberTermsAgree(mbMember);

		// TODO: ?????? ?????? ?????? ???????????? ?????? ??????
		// TODO: ?????? ????????? ?????? ??????
		try {
			MessageVO messageVO = new MessageVO();
			messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
			messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
			messageVO.setRecvTelNoText(mbMember.getHdphnNoText());
			messageVO.setRcvrName(mbMember.getMemberName());
			messageVO.setMesgId("MB00000004"); // TODO MesgId ???????????? ?????? ?????? ??? ????????? ????????????

			mbMember.setLinkUrl1(Config.getString("url.www.mypage"));
			mbMember.setLinkUrl2(Config.getString("url.www.grade"));
			messageVO.setMessageTemplateModel(mbMember);

			messageService.setSendMessageProcessNoTrx(messageVO); // ?????? ????????? ??????
			memberService.sendJoinEmailProcessNoTrx(mbMember);
		} catch (Exception e) {
			log.debug("createNewMemberInfo setSendMessageProcessNoTrx error : {}", e.getMessage());
		}
	}

	/**
	 * @Desc : ???????????? ?????? ??????
	 * @Method Name : sendEmailProcessNoTrx
	 * @Date : 2019. 7. 3.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void sendJoinEmailProcessNoTrx(MbMember mbMember) throws Exception {
		MailTemplateVO mailVO = new MailTemplateVO();
		Map<String, Object> dataMap = new HashMap<>();

		String joinDtmArray[] = UtilsDate.today().split("\\.");
		String joinDtm = UtilsText.concat(joinDtmArray[0] + "??? " + joinDtmArray[1] + "??? " + joinDtmArray[2] + "???");
		log.debug("joinDtm>>>>>>>>>" + joinDtm);

		dataMap.put("joinDtm", joinDtm);
		dataMap.put("memberInfo", mbMember);
		dataMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ?????????ID, ???????????? ???????????? ??????, ?????? ??????, ????????? ??????, ??????
		if (UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_ONLINE)) {
			mailVO.setMailTemplateId(MailCode.ONLINE_JOIN_CONGRATULATIONS);
		} else if (UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)) {
			mailVO.setMailTemplateId(MailCode.MEMBERSHIP_JOIN_CONGRATULATIONS);
		}
		mailVO.setMailTemplateModel(dataMap);
		mailVO.setReceiverMemberNo(mbMember.getMemberNo());
		mailVO.setReceiverEmail(mbMember.getEmailAddrText());
		mailVO.setReceiverName(mbMember.getMemberName());

		mailService.sendMail(mailVO);
	}

	/**
	 * @Desc : ?????? ????????? ??????
	 * @Method Name : selectMemberSequence
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @return
	 * @throws Exception
	 */
	public String selectMemberSequence() throws Exception {
		return mbMemberDao.selectMemberSequence();
	}

	/**
	 * @Desc : ?????? ?????? ??????
	 * @Method Name : insertMemberTermsAgree
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void insertMemberTermsAgree(MbMember mbMember) throws Exception {
		for (MbMemberTermsAgree mbMemberTermsAgree : mbMember.getTermsList()) {
			mbMemberTermsAgree.setMemberNo(mbMember.getMemberNo());
			mbMemberTermsAgree.setTermsAgreeYn(
					UtilsText.equals(Const.BOOLEAN_TRUE, mbMemberTermsAgree.getTermsAgreeYn()) ? Const.BOOLEAN_TRUE
							: Const.BOOLEAN_FALSE);
			mbMemberTermsAgreeDao.insertMemberTermsAgree(mbMemberTermsAgree);
		}
	}

	/**
	 * @Desc : ?????? ???????????? ??????
	 * @Method Name : insertMemberInfo
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void insertMemberInfo(MbMember mbMember) throws Exception {
		mbMemberDao.insertMemberInfo(mbMember);
	}

	public void updatePwChange(MbMember mbMember) throws Exception {
		MbMember result = new MbMember();

		// ????????? ???????????? ?????? ?????? ????????? ??????
		result = selectMemberInfo(mbMember);

		String passwordSaleText = result.getPswdSaltText();

		if (UtilsText.isEmpty(passwordSaleText)) {
			passwordSaleText = UtilsCrypt.getSalt();
		}

		mbMember.setMemberNo(result.getMemberNo());
		mbMember.setPswdText(UtilsCrypt.sha256(mbMember.getPswdText(), passwordSaleText));
		mbMember.setPswdChngDtm(UtilsDate.getSqlTimeStamp());

		memberService.updateMemberInfo(mbMember);
	}

	/**
	 * @Desc : 1????????? ???????????? ?????? ?????????
	 * @Method Name : getLatedSavePointCount
	 * @Date : 2019. 3. 28.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public int getLatedSavePointRecent1MonthCount(MbMemberExpostSavePoint params) throws Exception {
		return mbMemberExpostSavePointDao.getLatedSavePointRecent1MonthCount(params);
	}

	/**
	 * @Desc : ???????????? ?????? 1??? ?????? ??????
	 * @Method Name : limitExceededCertificationNumber
	 * @Date : 2019. 4. 9.
	 * @Author : Kimyounghyun
	 * @param memberJoinCertificationHistory
	 * @throws Exception
	 */
	private void limitExceededCertificationNumber(CertificationVO certificationVO) throws Exception {
		MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();
		memberCertificationHistory.setCrtfcNoSendInfo(certificationVO.getCrtfcNoSendInfo());

		int cnt = mbMemberCertificationHistoryDao.selectCertificationCount(memberCertificationHistory);
		if (cnt >= Const.CERTIFY_NUMBER_LIMIT_COUNT) {
			throw new MemberException.LimitExceededCertificationNumber("1??? ?????? ????????? ?????????????????????.");
		}
	}

	/**
	 * @Desc : ????????? ????????? ??????
	 * @Method Name : getIpinCertifyCheck
	 * @Date : 2019. 4. 16.
	 * @Author : ?????????
	 * @param siren24
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getIpinCertifyCheck(Siren24 siren24) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		siren24.beforeSend();

		resultMap.put("reqInfo", siren24.getReqInfo());
		resultMap.put("retUrl", siren24.getRetUrl());
		resultMap.put("reqNum", siren24.getReqNum());

		return resultMap;
	}

	public Map<String, Object> setLeaveProcess(MbMember mbMember) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		// sns ????????? ?????? ?????? - tobe????????? ????????????

		// ??????,??????,????????? ????????? ??????
		OcOrder ocOrder = new OcOrder();
		ocOrder.setMemberNo(mbMember.getMemberNo());
		OrderOtherPartVo orderOtherPartVo = orderOtherPartService.getProgressOrderClmAsCount(ocOrder);

		// if ??????,?????????,as??? ????????? ?????? ???????????? ??????
		if (orderOtherPartVo.getOrderCnt() > 0 || orderOtherPartVo.getClaimCnt() > 0
				|| orderOtherPartVo.getAsCnt() > 0) {
			StringBuffer leaveMessge = new StringBuffer();

			if (orderOtherPartVo.getOrderCnt() > 0) {
				leaveMessge.append("<br/>?????? " + orderOtherPartVo.getOrderCnt() + "???");
			}
			if (orderOtherPartVo.getClaimCnt() > 0) {
				leaveMessge.append("<br/>????????? " + orderOtherPartVo.getClaimCnt() + "???");
			}
			if (orderOtherPartVo.getAsCnt() > 0) {
				leaveMessge.append("<br/>A/S " + orderOtherPartVo.getAsCnt() + "???");
			}

			result.put("success", "0"); // ???????????? ??????[0]??? ??????
			result.put("order_data", leaveMessge); // ??????, ?????????, A/S ????????? ?????? ?????? ?????? ????????? ?????? ?????? ??? ????????????
			return result;
		} else {
			// ?????? ????????? ???????????? ??????

			// ????????? ???????????? ??? ??????[?????? ???, null or ??????????????? ?????? ????????? update]

			// ?????? ????????? update
			String leaveRsnCode = mbMember.getLeaveRsnCode();
			mbMember = memberService.selectMemberInfo(mbMember);
			mbMember.setLeaveYn("Y");
			mbMember.setLeaveGbnCode("10000");
			mbMember.setLeaveRsnCode(leaveRsnCode);
			result.put("success", String.valueOf(mbMemberDao.updateLeave(mbMember)));

			// ???????????? delete
			// ???????????? ?????? delete
			// ???????????? delete
			// ????????? ?????? delete
			// ???????????? delete
			// ????????? ???????????? delete
			// ????????? delete
			// ?????? ???????????? delete
			// ?????? ????????? delete
			// ???????????? ???????????? delete
			// ??????????????? ???????????????????????? ?????? ?????? ????????? delete [???????????? ?????? ??????]
			// SNS ?????? ?????? ????????? ??????
			// SNS ?????? ????????? ??????

			// ???????????? ????????? ??????
			MessageVO messageVO = new MessageVO();
			try {
				messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
				messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
				messageVO.setRecvTelNoText(mbMember.getHdphnNoText());
				messageVO.setRcvrName(mbMember.getMemberName());
				messageVO.setMesgId("M1234"); // TODO MesgId ???????????? ?????? ?????? ??? ????????? ????????????
				messageVO.setMessageTemplateModel(mbMember);

				messageService.setSendMessageProcessNoTrx(messageVO);
			} catch (Exception e) {
				log.debug("setSendMessageProcessNoTrx error : {}", e.getMessage());
			}
			// ???????????? ?????? ??????
			sendLeaveMailNoTrx(mbMember);
		}

		return result;
	}

	/**
	 * @Desc : ?????? ?????? ?????? ?????????
	 * @Method Name : sendLeaveMailNoTrx
	 * @Date : 2019. 7. 5.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void sendLeaveMailNoTrx(MbMember mbMember) throws Exception {
		MailTemplateVO mailTemplateVo = new MailTemplateVO();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);
		dataMap.put("memberName", mbMember.getMemberName());

		mailTemplateVo.setMailTemplateId("EC-05006");
		mailTemplateVo.setMailTemplateModel(dataMap);
		mailTemplateVo.setReceiverMemberNo(mbMember.getMemberNo());
		mailTemplateVo.setReceiverEmail(mbMember.getEmailAddrText());
		mailTemplateVo.setReceiverName(mbMember.getMemberName());

		mailService.sendMail(mailTemplateVo); // ?????? ??????
	}

	/**
	 * @Desc : ????????? ???????????? ????????? ??????
	 * @Method Name : getKmciCertifyCheck
	 * @Date : 2019. 4. 16.
	 * @Author : ?????????
	 * @param kmcism
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getKmciCertifyCheck(Kmcism kmcism) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		kmcism.beforeSend();

		resultMap.put("tr_cert", kmcism.getTr_cert());
		resultMap.put("tr_url", kmcism.getTr_url());
		resultMap.put("certNum", kmcism.getCertNum());

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : selectMemberDeliveryAddress
	 * @Date : 2019. 4. 10.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Page<MbMemberDeliveryAddress> selectMemberDeliveryAddress(
			Pageable<MbMemberDeliveryAddress, MbMemberDeliveryAddress> pageable) throws Exception {
		int count = mbMemberDeliveryAddressDao.selectMemberDeliveryAddressCount(pageable.getBean());
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.setContent(mbMemberDeliveryAddressDao.selectMemberDeliveryAddressPaging(pageable));
		}

		return pageable.getPage();
	}

	/**
	 * @Desc : ??????????????? ????????????(??????????????????)
	 * @Method Name : insertMemberDeliveryAddress
	 * @Date : 2019. 4. 19.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> insertMemberDeliveryAddress(MbMemberDeliveryAddress params) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String resultCode = "";
		String resultMsg = "";

		// ????????? ??????10??? ?????? ??????
		int count = mbMemberDeliveryAddressDao.selectMemberDeliveryAddressCount(params);
		if (count >= 10) {
			resultCode = Const.BOOLEAN_FALSE;
			resultMsg = Message.getMessage("member.error.limitDeliveryAddress");

			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMsg", resultMsg);
			return resultMap;
		}

		// ??????????????? ??????
		int dlvyAddrSeq = mbMemberDeliveryAddressDao.selectDlvyAddrSeqNextVal(params);
		params.setDlvyAddrSeq(dlvyAddrSeq);
		if (dlvyAddrSeq == 1) {
			params.setDfltDlvyAddrYn(Const.BOOLEAN_TRUE);
			params.setAddDlvyAddrYn(Const.BOOLEAN_FALSE);
		} else {
			params.setDfltDlvyAddrYn(Const.BOOLEAN_FALSE);
			params.setAddDlvyAddrYn(Const.BOOLEAN_TRUE);
		}
		params.setRgsterNo(params.getMemberNo());
		mbMemberDeliveryAddressDao.insert(params);

		resultCode = Const.BOOLEAN_TRUE;
		resultMsg = Message.getMessage("member.msg.saved");

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : updateMemberDeliveryAddress
	 * @Date : 2019. 4. 19.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateMemberDeliveryAddress(MbMemberDeliveryAddress params) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		params.setModerNo(params.getMemberNo());
		mbMemberDeliveryAddressDao.update(params);
		resultMap.put("resultCode", Const.BOOLEAN_TRUE);
		resultMap.put("resultMsg", Message.getMessage("member.msg.saved"));
		return resultMap;
	}

	/**
	 * @Desc : ?????? ?????? ??????
	 * @Method Name : selectLeaveMemberInfo
	 * @Date : 2019. 4. 15.
	 * @Author : ?????????
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public MbMember selectLeaveMemberInfo(MbMember mbMember) throws Exception {
		return mbMemberDao.selectLeaveMemberInfo(mbMember);
	}

	/**
	 * @Desc : SafeKeySeq ??????
	 * @Method Name : hasSafeKeyNum
	 * @Date : 2019. 4. 15.
	 * @Author : ?????????
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public String hasSetSafeKeySeq(MbMember mbMember) throws Exception {
		String safeKeyNumber = "";
		// ???????????? ??????
		MbMember leaveInfo = memberService.selectLeaveMemberInfo(mbMember);

		if (leaveInfo == null) {
			safeKeyNumber = "01";
		} else {
			if (UtilsText.isEmpty(leaveInfo.getSafeKeySeq())) {
				safeKeyNumber = "01";
			} else {
				int num = Integer.parseInt(leaveInfo.getSafeKeySeq()) + 1;
				if (num < 10) {
					safeKeyNumber = "0" + String.valueOf(num);
				} else {
					safeKeyNumber = String.valueOf(num);
				}
			}
		}

		return safeKeyNumber;
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : updateDefaultDeliveryAddressCancle
	 * @Date : 2019. 4. 19.
	 * @Author : ?????????
	 * @param params
	 */
	public int updateDefaultDeliveryAddressCancle(MbMemberDeliveryAddress params) throws Exception {
		return mbMemberDeliveryAddressDao.updateDefaultDeliveryAddressCancle(params);
	}

	/**
	 * @Desc : ????????? ??????
	 * @Method Name : deliveryAddressDelete
	 * @Date : 2019. 4. 20.
	 * @Author : ?????????
	 * @param params
	 */
	public void deleteMemberDeliveryAddress(MbMemberDeliveryAddress[] params) throws Exception {
		for (MbMemberDeliveryAddress param : params) {
			mbMemberDeliveryAddressDao.delete(param);
		}
	}

	/**
	 * @Desc : ?????????????????? ?????????
	 * @Method Name : getCustomStore
	 * @Date : 2019. 4. 22.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCustomStore(MypageVO params) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		MbMemberInterestStore mbMemberInterestStore = new MbMemberInterestStore();
		mbMemberInterestStore.setMemberNo(params.getMemberNo());
		mbMemberInterestStore.setSiteNo(params.getSiteNo());

		CmStore myStoreInfo = this.getInterestStore(mbMemberInterestStore);
		if (myStoreInfo != null) {
			myStoreInfo.setEventImageUrl(storeService.getStoreBannerUrlMap(myStoreInfo.getStoreNo())
					.get(UtilsText.equals(params.getIsMobile(), Const.BOOLEAN_TRUE) ? "mobile" : "pc"));
		}
		resultMap.put("myStoreInfo", myStoreInfo);

		return resultMap;
	}

	/**
	 * @Desc : ??????????????????
	 * @Method Name : getInterestStore
	 * @Date : 2019. 5. 14.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public CmStore getInterestStore(MbMemberInterestStore params) throws Exception {
		params.validate();
		MbMemberInterestStore storeInfo = mbMemberInterestStoreDao.select(params).stream()
				.sorted(Comparator.comparing(MbMemberInterestStore::getRgstDtm).reversed()).findFirst().orElse(null);
		CmStore myStoreInfo = null;
		if (storeInfo != null) {
			CmStore cmStore = new CmStore();
			cmStore.setStoreNo(storeInfo.getStoreNo());
			cmStore.setSiteNo(params.getSiteNo());
			myStoreInfo = storeService.getStoreDetail(cmStore);
		}
		return myStoreInfo;
	}

	/**
	 * @Desc : ???????????? ??????
	 * @Method Name : updateCustomStore
	 * @Date : 2019. 4. 23.
	 * @Author : ?????????
	 * @param params
	 */
	public void updateCustomStore(MbMemberInterestStore params) throws Exception {
		mbMemberInterestStoreDao.deleteCustomStore(params);
		mbMemberInterestStoreDao.insert(params);
	}

	/**
	 * @Desc : ???????????? ??????
	 * @Method Name : deleteCustomStore
	 * @Date : 2019. 4. 23.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public void deleteCustomStore(MbMemberInterestStore params) throws Exception {
		mbMemberInterestStoreDao.deleteCustomStore(params);
	}

	/**
	 * @Desc : ???????????? ??????
	 * @Method Name : selectExpostSavePointSeqNextVal
	 * @Date : 2019. 4. 24.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public int selectExpostSavePointSeqNextVal(MbMemberExpostSavePoint params) throws Exception {
		log.debug("MbMemberExpostSavePoint params : {}", params);
		return mbMemberExpostSavePointDao.selectExpostSavePointSeqNextVal(params);
	}

	/**
	 * @Desc : ???????????? ??????
	 * @Method Name : insertExpostSavePoint
	 * @Date : 2019. 4. 24.
	 * @Author : ?????????
	 * @param params
	 */
	public void insertExpostSavePoint(MbMemberExpostSavePoint params) throws Exception {
		mbMemberExpostSavePointDao.insert(params);
	}

	/**
	 * @Desc : ????????? ?????? ??????
	 * @Method Name : selectPointSeqNextVal
	 * @Date : 2019. 4. 24.
	 * @Author : ?????????
	 * @param pointParams
	 * @return
	 */
	public int selectPointSeqNextVal(MbMemberPoint params) throws Exception {
		return mbMemberPointDao.selectPointSeqNextVal(params);
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : insertMemberPoint
	 * @Date : 2019. 4. 24.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public int insertMemberPoint(MbMemberPoint params) throws Exception {
		return mbMemberPointDao.insert(params);
	}

	/**
	 * @Desc : ????????? ?????? ?????? ??????
	 * @Method Name : updateMemberChangeInfo
	 * @Date : 2019. 4. 26.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void updateMemberChangeInfo(MbMember mbMember) throws Exception {
		memberService.insertMemberTermsAgree(mbMember);

		mbMember.setMemberCnvrtDtm(UtilsDate.getSqlTimeStamp());
		memberService.updateMemberInfo(mbMember);
	}

	/**
	 * @Desc : ??????????????? ?????????
	 * @Method Name : selectMyInterestedBrandListCount
	 * @Date : 2019. 4. 29.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public int selectMyInterestedBrandListCount(Pageable<MbMemberInterestBrand, MbMemberInterestBrand> pageable)
			throws Exception {
		return mbMemberInterestBrandDao.selectMyInterestedBrandListCount(pageable);
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : selectMyInterestedBrandList
	 * @Date : 2019. 4. 29.
	 * @Author : ?????????
	 * @param bean
	 * @return
	 */
	public List<MbMemberInterestBrand> selectMyInterestedBrandList(
			Pageable<MbMemberInterestBrand, MbMemberInterestBrand> pageable) throws Exception {
		List<MbMemberInterestBrand> dataList = mbMemberInterestBrandDao.selectMyInterestedBrandList(pageable);

		for (MbMemberInterestBrand data : dataList) {
			Map<String, Object> brandData = brandPageService.getBrandProductBest(data.getBrandNo());
			data.setProductMap(brandData);
		}

		return dataList;
	}

	/**
	 * @Desc : ?????? ?????? ??????(?????? ?????? ??? ???????????? ??????)
	 * @Method Name : updateMemberChangeJoin
	 * @Date : 2019. 4. 29.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void updateMemberChangeJoin(MbMember mbMember) throws Exception {
		// ?????? ?????? ????????? ??????
		memberService.insertMemberTermsAgree(mbMember);

		// ???????????? ?????????
		Pair<String, String> pair = UtilsCrypt.getEncryptPassword(mbMember.getPswdText());

		// ???????????? ?????????
		mbMember.setPswdSaltText(pair.getSecond());
		mbMember.setPswdText(pair.getFirst());

		// ????????? ???????????? ?????????
		mbMember.setSmsRecvYn(
				UtilsText.equals("on", mbMember.getSmsRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);
		mbMember.setEmailRecvYn(
				UtilsText.equals("on", mbMember.getEmailRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);
		mbMember.setNightSmsRecvYn(
				UtilsText.equals("on", mbMember.getNightSmsRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);
		mbMember.setNightEmailRecvYn(
				UtilsText.equals("on", mbMember.getNightEmailRecvYn()) ? Const.BOOLEAN_TRUE : Const.BOOLEAN_FALSE);

		mbMember.setModerNo(mbMember.getMemberNo());
		mbMember.setMbshpCnvrtDtm(UtilsDate.getSqlTimeStamp());
		mbMember.setMemberCnvrtDtm(UtilsDate.getSqlTimeStamp());

		memberService.updateMemberInfo(mbMember);

		try {
			MessageVO messageVO = new MessageVO();
			messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
			messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
			messageVO.setRecvTelNoText(mbMember.getHdphnNoText());
			messageVO.setRcvrName(mbMember.getMemberName());
			messageVO.setMesgId("MB00000004"); // TODO MesgId ???????????? ?????? ?????? ??? ????????? ????????????

			mbMember.setLinkUrl1(Config.getString("url.www.mypage"));
			mbMember.setLinkUrl2(Config.getString("url.www.grade"));
			messageVO.setMessageTemplateModel(mbMember);

			messageService.setSendMessageProcessNoTrx(messageVO); // ?????? ????????? ??????
		} catch (Exception e) {
			log.debug("createNewMemberInfo setSendMessageProcessNoTrx error : {}", e.getMessage());
		}
		// ????????? ??????
		sendTransitionMailNoTrx(mbMember);
	}

	/**
	 * @Desc : ???????????? ????????? ??????
	 * @Method Name : sendTransitionMailNoTrx
	 * @Date : 2019. 7. 5.
	 * @Author : ?????????
	 * @param mbMember
	 */
	public void sendTransitionMailNoTrx(MbMember mbMember) {
		MailTemplateVO mailVO = new MailTemplateVO();
		Map<String, Object> dataMap = new HashMap<>();

		try {
			String joinDtmArray[] = UtilsDate.today().split("\\.");
			String joinDtm = UtilsText.concat(joinDtmArray[0] + "??? " + joinDtmArray[1] + "??? " + joinDtmArray[2] + "???");
			log.debug("joinDtm>>>>>>>>>" + joinDtm);
			dataMap.put("joinDtm", joinDtm);

			// ?????????ID, ???????????? ???????????? ??????, ?????? ??????, ????????? ??????, ??????
			if (UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_ONLINE)) {
				mailVO.setMailTemplateId(MailCode.ONLINE_TRANSITION_CONGRATULATIONS);
			} else if (UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)) {
				mailVO.setMailTemplateId(MailCode.MEMBERSHIP_TRANSITION_CONGRATULATIONS);
			}

			dataMap.put("memberInfo", mbMember);
			dataMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

			mailVO.setMailTemplateModel(dataMap);
			mailVO.setReceiverMemberNo(mbMember.getMemberNo());
			mailVO.setReceiverEmail(mbMember.getEmailAddrText());
			mailVO.setReceiverName(mbMember.getMemberName());

			mailService.sendMail(mailVO);
		} catch (Exception e) {
			log.debug("sendTransitionMailNoTrx error: {}", e.getMessage());
		}
	}

	/**
	 * @Desc : ?????? ??????????????? ?????? (????????????)
	 * @Method Name : setMemberDeliveryAddress
	 * @Date : 2019. 4. 30.
	 * @Author : ?????????
	 * @param memberDelivery
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberDeliveryAddress> setMemberDeliveryAddress(MbMemberDeliveryAddress memberDelivery)
			throws Exception {

		List<MbMemberDeliveryAddress> memberDeliveryList = null;
		// ?????????????????? ???????????? ?????? ?????? ???????????? ??????
		memberService.updateDefaultDeliveryAddressCancle(memberDelivery);

		// ????????? ?????? ??????
		int dlvyAddrSeq = mbMemberDeliveryAddressDao.selectDlvyAddrSeqNextVal(memberDelivery);

		// ?????? ??????????????? ??????
		memberDelivery.setDlvyAddrSeq(dlvyAddrSeq);
		memberDelivery.setDfltDlvyAddrYn(Const.BOOLEAN_TRUE);
		memberDelivery.setAddDlvyAddrYn(Const.BOOLEAN_FALSE);
		memberDelivery.setRgsterNo(memberDelivery.getMemberNo());
		memberDelivery.setModerNo(memberDelivery.getMemberNo());
		memberDelivery.setModDtm(UtilsDate.getSqlTimeStamp());

		mbMemberDeliveryAddressDao.insert(memberDelivery);

		// ?????? ????????? ???????????? ??????
		memberDeliveryList = memberService.getMemberDeliveryList(memberDelivery.getMemberNo());
		return memberDeliveryList;
	}

	/**
	 * @Desc : ?????? ????????? ????????? ??????
	 * @Method Name : getMemberDeliveryList
	 * @Date : 2019. 4. 30.
	 * @Author : ?????????
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberDeliveryAddress> getMemberDeliveryList(String memberNo) throws Exception {
		return mbMemberDeliveryAddressDao.selectMemberDeliveryList(memberNo);
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : deleteMyInterestedBrand
	 * @Date : 2019. 4. 30.
	 * @Author : ?????????
	 * @param params
	 */
	public void deleteMyInterestedBrand(MbMemberInterestBrand params) throws Exception {
		mbMemberInterestBrandDao.deleteMyInterestedBrand(params);
	}

	/**
	 * @Desc : sns ???????????? ??????
	 * @Method Name : getMemberEasyLoginAccount
	 * @Date : 2019. 5. 10.
	 * @Author : ?????????
	 * @param mbMemberEasyLogin
	 * @return
	 * @throws Exception
	 */
	public MbMemberEasyLogin getMemberEasyLoginAccount(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		return mbMemberEasyLoginDao.selectSnsLoginInfo(mbMemberEasyLogin);
	}

	/**
	 * @Desc : ????????????????????? ??????
	 * @Method Name : getMemberEasyLoginInfo
	 * @Date : 2019. 5. 3.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public List<MbMemberEasyLogin> getMemberEasyLoginInfo(MbMemberEasyLogin params) throws Exception {
		return mbMemberEasyLoginDao.select(params);
	}

	public void updateEasyLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		mbMemberEasyLoginDao.updateEasyLoginInfo(mbMemberEasyLogin);
	}

	public void deleteEasyLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		mbMemberEasyLoginDao.deleteEasyLoginInfo(mbMemberEasyLogin);
	}

	public CmStore selectRecmdStore(String storeCode) throws Exception {
		return storeService.getCmStoreDetailByStoreNo(storeCode);
	}

	/**
	 * @Desc : ?????? ?????????????????? ??????
	 * @Method Name : getMemberPoint
	 * @Date : 2019. 6. 17.
	 * @Author : ?????????
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMemberPoint(MbMember param) throws Exception {
		MbMember mbMember = memberService.selectMemberInfo(param);
		mbMember.setEmpLoginYn(param.getEmpLoginYn());
		return mypageService.getMemberBasicInfo(mbMember);
	}

	/**
	 * @Desc : ?????? ???????????? ??????
	 * @Method Name : setPaymentMeansCode
	 * @Date : 2019. 6. 18.
	 * @Author : ?????????
	 * @param mbMember
	 * @throws Exception
	 */
	public void setPaymentMeansCode(MbMember mbMember) throws Exception {
		mbMemberDao.updatePaymentMeansCode(mbMember);
	}

	/**
	 * @Desc : App ????????? ??????
	 * @Method Name : getAppLoginInfo
	 * @Date : 2019. 7. 11.
	 * @Author : ?????????
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public MbMember getAppLoginInfo(MbMember mbMember) throws Exception {
		MbMember member = new MbMember();
		// TODO : CM_PUSH_APP?????????????????? ??????????????? ????????????
		// CmPushAppDownloadMember appDownloadMember = getAppDownloadMemberInfo(String
		// loginId, String deviceToken); 
		CmPushAppDownloadMember appDownloadMember = new CmPushAppDownloadMember();

		if (appDownloadMember != null) {
			member.setLoginId(mbMember.getLoginId());
			member = this.selectMemberInfo(member);
		}

		return member;
	}
}
