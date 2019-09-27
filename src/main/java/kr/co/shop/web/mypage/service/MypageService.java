package kr.co.shop.web.mypage.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.account.AccountAuth;
import kr.co.shop.common.config.Config;
import kr.co.shop.common.constant.BaseCommonCode;
import kr.co.shop.common.message.Message;
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsObject;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.constant.MessageCode;
import kr.co.shop.interfaces.module.member.MembershipPointService;
import kr.co.shop.interfaces.module.member.model.BuyFixProduct;
import kr.co.shop.interfaces.module.member.model.CardNumber;
import kr.co.shop.interfaces.module.member.model.PrivateReport;
import kr.co.shop.interfaces.module.member.model.StorePointHistory;
import kr.co.shop.interfaces.module.member.utils.MemberShipProcessException;
import kr.co.shop.util.UtilsCrypt;
import kr.co.shop.util.UtilsDate;
import kr.co.shop.util.UtilsRequest;
import kr.co.shop.web.cmm.service.MailService;
import kr.co.shop.web.cmm.service.MessageService;
import kr.co.shop.web.cmm.vo.MessageVO;
import kr.co.shop.web.customer.repository.master.CmOnlineMemberPolicyDao;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberCertificationHistory;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.member.model.master.MbMemberEasyLogin;
import kr.co.shop.web.member.model.master.MbMemberExpostSavePoint;
import kr.co.shop.web.member.model.master.MbMemberInterestBrand;
import kr.co.shop.web.member.model.master.MbMemberPoint;
import kr.co.shop.web.member.repository.master.MbMemberCertificationHistoryDao;
import kr.co.shop.web.member.repository.master.MbMemberDao;
import kr.co.shop.web.member.service.MemberCouponService;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.mypage.vo.MemberShipErrorCode;
import kr.co.shop.web.mypage.vo.MypageVO;
import kr.co.shop.web.mypage.vo.PointVO;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.service.OrderOtherPartService;
import kr.co.shop.web.order.vo.OrderOtherPartVo;
import kr.co.shop.web.system.service.CommonCodeService;
import kr.co.shop.web.system.service.SystemService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MypageService {

	@Autowired
	private MypageService mypageService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberCouponService memberCouponService;

	@Autowired
	private OrderOtherPartService orderOtherPartService;

	@Autowired
	private MbMemberDao mbMemberDao;

	@Autowired
	private MessageService messageService;

	@Autowired
	private MailService mailService;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private MembershipPointService membershipPointService;

	@Autowired
	private CmOnlineMemberPolicyDao cmOnlineMemberPolicyDao;

	@Autowired
	private MbMemberCertificationHistoryDao mbMemberCertificationHistoryDao;

	@Autowired
	private SystemService systemService;

	/**
	 * @Desc : SAFE_KEY_TEXT(안심키) 조회
	 * @Method Name : getSafeKeyText
	 * @Date : 2019. 3. 25.
	 * @Author : 유성민
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public String getSafeKeyText(String memberNo) throws Exception {
		return memberService.getSafeKey(memberNo);
	}

	/**
	 * @Desc :회원기본정보영역 조회(쿠폰 갯수, 포인트, 멤버쉽 번호)
	 * @Method Name : getMemberBasicInfo
	 * @Date : 2019. 3. 18.
	 * @Author : 유성민
	 * @param param
	 * @return
	 */
	public Map<String, Object> getMemberBasicInfo(MbMember param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			if ((!UtilsText.equals(param.getMemberTypeCode(), CommonCode.MEMBER_TYPE_NONMEMBER))
					&& UtilsText.equals(param.getEmpLoginYn(), Const.BOOLEAN_FALSE)) {
				int couponCount = memberCouponService.getAvailableCouponCount(param.getMemberNo());
				resultMap.put("couponCount", couponCount);

				if (UtilsText.isNotBlank(param.getSafeKey())) {
					// 가용포인트 조회 [memA910a]
					// 1 totalPoint : 총포인트
					// 2 accessPoint : 가능포인트
					// 3 extinctPoint : 소멸포인트
					// 4 stampCount : 스템프카운트(사용안함)
					// 5 totalStampCount : 전체스템프카운트(사용안함)
					// 6 toDoSavePoint : 적립예정포인트
					// 7 eventPoint : 이벤트포인트
					// 8 eventDate : 이벤트만료일자
					// 9 preSavePoint : 적립예정포인트
					PrivateReport privateReport = membershipPointService.getPrivateReportBySafeKey(param.getSafeKey());
					resultMap.put("privateReport", privateReport);

					if (UtilsText.equals(param.getMemberTypeCode(), CommonCode.MEMBER_TYPE_MEMBERSHIP)
							&& UtilsText.isBlank(param.getMbshpCardNo())) {
						// 멤버십 카드 번호 조회 [memA830a]
						CardNumber userCardNumber = membershipPointService
								.getUserCardNumberBySafeKey(param.getSafeKey());
						String mbshpCardNo = userCardNumber.getCardNumber();
						resultMap.put("mbshpCardNo", mbshpCardNo);
					}
				}
			}
		} catch (Exception e) {
			log.error("getMemberBasicInfo error : {}", e.getMessage());
		}
		return resultMap;
	}

	/**
	 * @Desc : 포인트화면 기본 정보
	 * @Method Name : getPointInfo
	 * @Date : 2019. 3. 21.
	 * @Author : 유성민
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPointInfo(MypageVO param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 가능포인트, 적립예정, 소멸예정, 포인트구분(이벤트, 구매) 포인트
		// getMemberBasicInfo 가용포인트 조회 [memA910a]

		// 포인트 적립 이력 [memA920a]
		List<StorePointHistory> pointHistoryList = new ArrayList<>();
		if (UtilsText.isNotBlank(param.getSafeKey())) {
			pointHistoryList = membershipPointService.getStorePointHistoryBySafeKey(param.getSafeKey());
		}
		resultMap.put("pointHistoryList", pointHistoryList);
		return resultMap;
	}

	/**
	 * @Desc :포인트 비밀번호 변경
	 * @Method Name : updateCardPassword
	 * @Date : 2019. 3. 22.
	 * @Author : 유성민
	 * @param param
	 * @return
	 */
	public Map<String, Object> updateCardPassword(PointVO param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_FALSE;
		String resultMsg = "";

		// 회원정보 조회
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(param.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);

		String pswdSaltText = memberInfo.getPswdSaltText();
		String pswdTextEncode = UtilsCrypt.sha256(param.getPswdText(), pswdSaltText);

		if (UtilsText.equals(param.getPswdText(),
				memberInfo.getHdphnNoText().substring(memberInfo.getHdphnNoText().length() - 4,
						memberInfo.getHdphnNoText().length()))
				|| UtilsText.equals(param.getPswdText(), String.valueOf(memberInfo.getBirthYmd()).substring(4, 10))) {

			resultMsg = Message.getMessage("mypage.error.privateInfo");

		} else if (UtilsText.equals(pswdTextEncode, memberInfo.getPswdText())) {

			String pointPasswordEncode = UtilsCrypt.sha256(param.getPointPassword(), memberInfo.getPointPswdSaltText());
			// 맴버십카드 비밀번호 변경 [memA970a]
			boolean chkChangePassword = membershipPointService.changeCardPasswordBySafeKey(memberInfo.getSafeKey(),
					pointPasswordEncode);

			if (chkChangePassword) {
				resultCode = Const.BOOLEAN_TRUE;
				resultMsg = Message.getMessage("mypage.msg.membershipPassword");
			} else {
				resultMsg = Message.getMessage("mypage.error.membershipPassword");
			}

		} else {
			resultMsg = Message.getMessage("mypage.error.password");
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * @Desc : 비밀번호 유효성 체크
	 * @Method Name : getPasswordValidation
	 * @Date : 2019. 3. 26.
	 * @Author : 유성민
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPasswordValidation(MypageVO param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_FALSE;
		String resultMsg = Message.getMessage("mypage.error.password");

		// 회원정보 조회
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(param.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);

		String pswdSaltText = memberInfo.getPswdSaltText();
		String pswdTextEncode = UtilsCrypt.sha256(param.getPswdText(), pswdSaltText);

		if (UtilsText.equals(pswdTextEncode, memberInfo.getPswdText())) {
			resultCode = Const.BOOLEAN_TRUE;
			resultMsg = Message.getMessage("mypage.msg.password");
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 *
	 * @Desc : 주문개수, 주문건수에 따른 회원 등급 조회
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 3. 21.
	 * @Author : choi
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMemberGrade() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> policyMap = new HashMap<String, Object>();
		// TODO : 주문 개발 후 회원의 주문개수, 주문총금액 조회 적용 필요
		resultMap.put("orderCount", "1");
		resultMap.put("orderAmount", "4000000");
		policyMap = mypageService.getPolicyData();
		resultMap.put("policyData", policyMap.get("policyData"));
		resultMap.put("startDate", policyMap.get("startDate"));
		resultMap.put("endDate", policyMap.get("endDate"));

		return resultMap;
	}

	/**
	 *
	 * @Desc : 온라인 회원 정책 데이터 조회
	 * @Method Name : getPolicyData
	 * @Date : 2019. 5. 23.
	 * @Author : 최경호
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPolicyData() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		// 현재월의 말일
		data.put("endDate",
				cal.get(cal.YEAR) + "." + (cal.get(cal.MONTH) + 1) + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 6개월전
																															// 말일

		// 6개월전의 1일
		cal.add(cal.MONTH, -5);
		data.put("startDate",
				cal.get(cal.YEAR) + "." + (cal.get(cal.MONTH) + 1) + "." + cal.getActualMinimum(Calendar.DAY_OF_MONTH)); // 6개월전
																															// 1일

		// 온라인 구매 혜택 조회
		data.put("policyData", cmOnlineMemberPolicyDao.selectPolicyData());

		return data;
	}

	/**
	 * @Desc :멤버십 포인트 사후 적립 신청
	 * @Method Name : insertLatedSavePoint
	 * @Date : 2019. 3. 28.
	 * @Author : 유성민
	 * @param param
	 * @return
	 */
	public Map<String, Object> insertLatedSavePoint(MbMemberExpostSavePoint params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 월 10회미만 체크
		int count = memberService.getLatedSavePointRecent1MonthCount(params);
		if (count >= 10) {
			resultMap.put("resultCode", Const.BOOLEAN_FALSE);
			resultMap.put("resultMsg", Message.getMessage("mypage.error.latedSavePoint10"));
		} else {
			/** 온라인,오프라인 사후 적립 **/
			if (UtilsText.equals(params.getOnlnBuyYn(), Const.BOOLEAN_TRUE)) {
				resultMap = mypageService.saveLatedSavePointForOnlineOrder(params);
			} else if (UtilsText.equals(params.getOnlnBuyYn(), Const.BOOLEAN_FALSE)) {
				resultMap = mypageService.savePossibleLatedSavePointForOfflineOrder(params);
			} else {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", Message.getMessage("mypage.error.wrongApproach"));
			}

		}
		return resultMap;
	}

	/**
	 * @Desc : 온라인 사후적립
	 * @Method Name : saveLatedSavePointForOnlineOrder
	 * @Date : 2019. 3. 28.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveLatedSavePointForOnlineOrder(MbMemberExpostSavePoint params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;
		OrderOtherPartVo orderOtherPartVo = null;

		resultMap.put("resultCode", Const.BOOLEAN_TRUE);
		resultMap.put("resultMsg", Message.getMessage("mypage.msg.latedSavePoint"));

		try {
			mbMember.setMemberNo(params.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);

			OcOrder ocOrder = new OcOrder();
			ocOrder.setMemberNo(params.getMemberNo());
			ocOrder.setOrderNo(params.getBuyNoText());
			orderOtherPartVo = orderOtherPartService.getOverDayYnAfterBuyDecision(ocOrder);

			/********** 1. 온라인 사후적립 가능 확인 **********/
			Map<String, Object> result = mypageService.checkPossibleLatedSavePoint(params, memberInfo,
					orderOtherPartVo);
			if (UtilsText.equals(String.valueOf(result.get("resultCode")), Const.BOOLEAN_FALSE)) {
				return result;
			}

			/********** 2. ENCODE ORDER_NUM GET **********/
			if (UtilsText.isBlank(params.getCrtfcNoText())) {
				params.setCrtfcNoText(orderOtherPartVo.getCrtfcNoText());
			}

			/********** 3. PROCEDURE_DECODE CALL **********/
			String procResult = orderOtherPartService.callProcedureForDecodeOrderNum("on", params.getBuyNoText(),
					params.getCrtfcNoText());
			log.debug("procResult : {}", procResult);
			if (!UtilsText.equals(procResult, "1")) {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", Message.getMessage("mypage.error.dealCont"));
				return resultMap;
			}

			/********** 4. 멤버십 포인트 적립 통신[memA890a] **********/
			OcOrderProduct orderProductParams = new OcOrderProduct();
			orderProductParams.setOrderNo(params.getBuyNoText());
			List<OcOrderProduct> orderProductList = orderOtherPartService.getOrderProductInfoList(orderProductParams);
			// 자사 상품 포함여부
			boolean includedMmnyPrdtY = orderProductList.stream().map(OcOrderProduct::getMmnyPrdtYn)
					.anyMatch(x -> UtilsText.equals(x, Const.BOOLEAN_TRUE));
			log.debug("자사 상품 포함여부  : {}", includedMmnyPrdtY);

			// 입점사 상품 포함여부
			boolean includedMmnyPrdtN = orderProductList.stream().map(OcOrderProduct::getMmnyPrdtYn)
					.anyMatch(x -> UtilsText.equals(x, Const.BOOLEAN_FALSE));
			log.debug("입점사 상품 포함여부  : {}", includedMmnyPrdtN);

			// TODO 유성민멤버십 포인트 적립 통신 자사, 입점 구분필요
			List<BuyFixProduct> products = null;
			if (includedMmnyPrdtY) {
				products = membershipPointService.buyFixRequest(memberInfo.getSafeKey(), params.getBuyNoText());
			}

			if (includedMmnyPrdtN) {

			}

			if (products.isEmpty()) {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", Message.getMessage("mypage.error.memberShipProcess"));
				return resultMap;
			}

			/********** 5. 우리쪽 DB에 완료데이터 저장 **********/
			// MB_MEMBER_EXPOST_SAVE_POINT
			int expostSavePointSeq = memberService.selectExpostSavePointSeqNextVal(params);
			params.setExpostSavePointSeq(expostSavePointSeq);
			memberService.insertExpostSavePoint(params);

			// OC_ORDER update
			OcOrder ocOrderParma = new OcOrder();
			ocOrderParma.setMemberNo(params.getMemberNo());
			ocOrderParma.setOrderNo(params.getBuyNoText());
			ocOrderParma.setModerNo(params.getMemberNo());
			orderOtherPartService.updatePointSaveYn(ocOrderParma);

		} catch (MemberShipProcessException e) {
			resultMap.put("resultCode", Const.BOOLEAN_FALSE);
			resultMap.put("resultMsg", Message.getMessage("mypage.error.memberShipProcess"));
		} catch (Exception e) {
			resultMap.put("resultCode", Const.BOOLEAN_FALSE);
			resultMap.put("resultMsg", Message.getMessage("mypage.error.latedSavePoint"));
		}

		try {
			/********** 7. 적립신청완료 SMS알림톡 전송 **********/
			if (UtilsText.isNotBlank(memberInfo.getHdphnNoText())) {

				Map<String, String> map = new HashMap<>();
				map.put("memberName", memberInfo.getMemberName());
				map.put("landingUrl", Config.getString("url.www.https") + "/mypage/point");

				MessageVO messageVO = new MessageVO();
				messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME); // 발신자
				messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER); // 발신 번호
				messageVO.setRcvrName(memberInfo.getMemberName()); // 수신사
				messageVO.setRecvTelNoText(memberInfo.getHdphnNoText()); // 수신번호
				messageVO.setMesgId(MessageCode.LATED_SAVE_POINT); // 사후적립 mesg_id 세팅
				messageVO.setMessageTemplateModel(map);
				messageVO.setReal(true);

				messageService.setSendMessageProcessNoTrx(messageVO); // 알림톡 등록
			}

		} catch (Exception e) {
			log.debug("message send error : {}");
		}

		return resultMap;
	}

	/**
	 * @Desc :오프라인 사후적립
	 * @Method Name : savePossibleLatedSavePointForOfflineOrder
	 * @Date : 2019. 3. 28.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> savePossibleLatedSavePointForOfflineOrder(MbMemberExpostSavePoint params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		MbMember memberInfo = null;
		String orderNo = "";
		MbMember mbMember = new MbMember();
		resultMap.put("resultCode", Const.BOOLEAN_TRUE);
		resultMap.put("resultMsg", Message.getMessage("mypage.msg.latedSavePoint"));

		try {
			mbMember.setMemberNo(params.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			if (!UtilsText.equals(memberInfo.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)) {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", Message.getMessage("mypage.error.notMembershipUser"));
				return resultMap;
			}

			/********** 1. 결제일로부터 (policy)일 이내 (관리자 채널이 아닐때만) **********/
			if (UtilsText.equals(params.getAdminRgstYn(), Const.BOOLEAN_FALSE)) {

				LocalDate saleDateDayAdd = LocalDate.from(params.getBuyYmd().toLocalDateTime()).plusDays(30);
				LocalDate today = LocalDate.now();

				if (saleDateDayAdd.isBefore(today)) {
					resultMap.put("resultCode", Const.BOOLEAN_FALSE);
					resultMap.put("resultMsg", Message.getMessage("mypage.error.saveDateExpr"));
					return resultMap;
				}
			}

			/********** 2. PROCEDURE_DECODE CALL **********/
			orderNo = UtilsText.concat(params.getPosNoText(), params.getDealNoText());
			String procResult = orderOtherPartService.callProcedureForDecodeOrderNum("off", orderNo,
					params.getCrtfcNoText());
			log.debug("procResult : {}", procResult);
			if (!UtilsText.equals(procResult, "1")) {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", Message.getMessage("mypage.error.dealCont"));
				return resultMap;
			}

			/********** 3. 멤버십 포인트 부여 API [memB300 통신] **********/
			String failCode = membershipPointService.updatePointAfterPurchase(memberInfo.getSafeKey(),
					params.getStoreNo(), params.getPosNoText(), params.getDealNoText(), params.getPymntAmt(),
					UtilsDate.formatter(UtilsDate.DEFAULT_DATE_PATTERN_NOT_CHARACTERS, params.getBuyYmd()));

			if (UtilsText.isNotBlank(failCode)) {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", MemberShipErrorCode.getByCode(failCode).getDesc());
				return resultMap;
			}

			/********** 4. 우리쪽 DB에 완료데이터 저장 **********/
			// MB_MEMBER_EXPOST_SAVE_POINT
			int expostSavePointSeq = memberService.selectExpostSavePointSeqNextVal(params);
			params.setExpostSavePointSeq(expostSavePointSeq);
			memberService.insertExpostSavePoint(params);

		} catch (MemberShipProcessException e) {
			resultMap.put("resultCode", Const.BOOLEAN_FALSE);
			resultMap.put("resultMsg", Message.getMessage("mypage.error.memberShipProcess"));
		} catch (Exception e) {
			resultMap.put("resultCode", Const.BOOLEAN_FALSE);
			resultMap.put("resultMsg", Message.getMessage("mypage.error.latedSavePoint"));
		}

		/********** 5. 적립신청완료 SMS알림톡 전송 **********/
		// 알림톡 보내기
		try {
			Map<String, String> map = new HashMap<>();
			map.put("memberName", memberInfo.getMemberName());
			map.put("landingUrl", Config.getString("url.www.https") + "/mypage/point");

			MessageVO messageVO = new MessageVO();
			messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME); // 발신자
			messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER); // 발신 번호
			messageVO.setRcvrName(memberInfo.getMemberName()); // 수신사
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText()); // 수신번호
			messageVO.setMesgId(MessageCode.LATED_SAVE_POINT); // 사후적립 mesg_id
			messageVO.setMessageTemplateModel(map);
			messageVO.setReal(true);

			messageService.setSendMessageProcessNoTrx(messageVO); // 알림톡 등록
		} catch (Exception e) {
			log.debug("messageService.setSendMessageProcessNoTrx error : {}");
		}

		return resultMap;
	}

	/**
	 * @Desc : 온라인 사후적립이 가능 여부 확인
	 * @Method Name : checkPossibleLatedSavePoint
	 * @Date : 2019. 4. 24.
	 * @Author : 유성민
	 * @param params
	 * @param memberInfo
	 */
	private Map<String, Object> checkPossibleLatedSavePoint(MbMemberExpostSavePoint params, MbMember memberInfo,
			OrderOtherPartVo orderOtherPartVo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_FALSE;
		String resultMsg = "";

		if (UtilsObject.isEmpty(orderOtherPartVo)) {
			resultMsg = Message.getMessage("mypage.error.notOrderNo");
		} else if (UtilsObject.isEmpty(orderOtherPartVo.getBuyDcsnDtm())) {
			resultMsg = Message.getMessage("mypage.error.notBuyDcsnDtm");
		} else if (UtilsText.equals(orderOtherPartVo.getMemberShipYn(), Const.BOOLEAN_TRUE)) {
			resultMsg = Message.getMessage("mypage.error.orderDtmMemberShipOrder");
		} else if (!UtilsText.equals(memberInfo.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)) {
			resultMsg = Message.getMessage("mypage.error.noMemberShip");
		} else if (UtilsText.equals(orderOtherPartVo.getPointSaveYn(), Const.BOOLEAN_TRUE)) {
			resultMsg = Message.getMessage("mypage.error.pointSaveYn");
		} else if ((params.getPymntAmt() - orderOtherPartVo.getPymntAmt()) != 0) {
			log.debug("params.getPymntAmt() : {}", params.getPymntAmt());
			log.debug("orderOtherPartVo.getPymntAmt() : {}", orderOtherPartVo.getPymntAmt());
			resultMsg = Message.getMessage("mypage.error.pymntAmtDiff");
		} else if (!UtilsText.equals(
				UtilsDate.formatter(UtilsDate.DEFAULT_DATE_PATTERN_NOT_CHARACTERS, params.getBuyYmd()),
				UtilsDate.formatter(UtilsDate.DEFAULT_DATE_PATTERN_NOT_CHARACTERS, orderOtherPartVo.getOrderDtm()))) {
			log.debug("getBuyYmd : {}",
					UtilsDate.formatter(UtilsDate.DEFAULT_DATE_PATTERN_NOT_CHARACTERS, params.getBuyYmd()));
			log.debug("getOrderDtm : {}",
					UtilsDate.formatter(UtilsDate.DEFAULT_DATE_PATTERN_NOT_CHARACTERS, orderOtherPartVo.getOrderDtm()));
			resultMsg = Message.getMessage("mypage.error.buyYmdDiff");
		} else if (UtilsText.equals(orderOtherPartVo.getBuydayOverday(), Const.BOOLEAN_TRUE)) {
			resultMsg = Message.getMessage("mypage.error.saveDateExpr");
		}

		if (UtilsText.isBlank(resultMsg)) {
			resultCode = Const.BOOLEAN_TRUE;
		}
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * @Desc : 온라인 사후적립 회원포인트 저장
	 * @Method Name : saveMemberPoint
	 * @Date : 2019. 4. 24.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveMemberPointOnline(List<BuyFixProduct> products, MbMemberExpostSavePoint params,
			List<OcOrderProduct> orderProductList) throws Exception {
		int resultCnt = 0;
		MbMemberPoint pointParams = null;

		for (BuyFixProduct product : products) {
			pointParams = new MbMemberPoint();
			pointParams.setMemberNo(params.getMemberNo());
			int pointSeq = memberService.selectPointSeqNextVal(pointParams);
			pointParams.setPointSeq(pointSeq);
			pointParams.setSavePathType(CommonCode.RTRVL_GBN_TYPE_ONLINE); // savePathType : O(온라인:d), F(오프라인,매장)
			pointParams.setSaveTypeCode(CommonCode.SAVE_TYPE_CODE_EARN_AFTERPUCHASE); // 사후적립
			pointParams.setSaveUseDtm(UtilsDate.getSqlTimeStamp());
			pointParams.setSaveUseContText("적립");
			pointParams.setSaveAmt(product.getPoint());
			pointParams.setUseAmt(0);
			pointParams.setValidStartDtm(UtilsDate.getSqlTimeStamp());
			pointParams.setValidEndDtm(
					Timestamp.valueOf(LocalDateTime.of(LocalDate.now().plusYears(2).getYear(), 12, 31, 23, 59)));
			pointParams.setOrderNo(params.getBuyNoText());
			pointParams.setExpostSavePointSeq(params.getExpostSavePointSeq());
			pointParams.setRgsterNo(params.getMemberNo());

			// 주문상품순번
			Optional<Short> orderPrdtSeq = orderProductList.stream()
					.filter(x -> UtilsText.equals(x.getPrdtNo(), product.getPrdtCode())
							&& UtilsText.equals(x.getPrdtOptnNo(), product.getOptnId()))
					.map(OcOrderProduct::getOrderPrdtSeq).findAny();
			if (orderPrdtSeq.isPresent()) {
				pointParams.setOrderPrdtSeq(orderPrdtSeq.get());
			}

			resultCnt += memberService.insertMemberPoint(pointParams);
		}

		return resultCnt;
	}

	/**
	 * @Desc : 오프라인 사후적립 포인트 저장
	 * @Method Name : saveMemberPointOffline
	 * @Date : 2019. 4. 26.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveMemberPointOffline(MbMemberExpostSavePoint params) throws Exception {

		MbMemberPoint pointParams = new MbMemberPoint();
		pointParams.setMemberNo(params.getMemberNo());
		int pointSeq = memberService.selectPointSeqNextVal(pointParams);
		pointParams.setPointSeq(pointSeq);
		pointParams.setSavePathType(CommonCode.RTRVL_GBN_TYPE_OFFLINE); // savePathType : O(온라인:d), F(오프라인,매장)
		pointParams.setSaveTypeCode(CommonCode.SAVE_TYPE_CODE_EARN_AFTERPUCHASE); // 사후적립
		pointParams.setSaveUseDtm(UtilsDate.getSqlTimeStamp());
		pointParams.setSaveUseContText("적립");
		pointParams.setSaveAmt(0);
		pointParams.setUseAmt(0);
		pointParams.setValidStartDtm(UtilsDate.getSqlTimeStamp());
		pointParams.setValidEndDtm(
				Timestamp.valueOf(LocalDateTime.of(LocalDate.now().plusYears(2).getYear(), 12, 31, 23, 59)));
		pointParams.setOrderNo(params.getBuyNoText());
		pointParams.setExpostSavePointSeq(params.getExpostSavePointSeq());
		pointParams.setRgsterNo(params.getMemberNo());

		return memberService.insertMemberPoint(pointParams);
	}

	/**
	 * @Desc :환불계좌 인증
	 * @Method Name : getAccountAuth
	 * @Date : 2019. 4. 18.
	 * @Author : 최경호
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAccountAuthProcess(MbMember mbMember) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		AccountAuth accountAuth = new AccountAuth();

		paramMap = this.setAccountParam(mbMember); // 계좌인증에 필요한 파라미터를 세팅
		Map<String, Object> result = accountAuth.start(paramMap);

		try {
			MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();
			String sessionId = UtilsRequest.getSession().getId();

			memberCertificationHistory.setCrtfcPathCode(BaseCommonCode.CRTFC_PATH_CODE_REFUND_ACCOUNT); // 인증경로코드
			memberCertificationHistory.setCrtfcTypeCode(BaseCommonCode.CRTFC_TYPE_CODE_REFUND_ACCOUNT); // 인증유형코드
			memberCertificationHistory.setMemberNo(mbMember.getMemberNo()); // 회원번호
			memberCertificationHistory.setAccessIpText(systemService.getIpAddress()); // 접근ip
			memberCertificationHistory.setSessionId(sessionId); // 세션아이디
			memberCertificationHistory.setCrtfcSuccessYn((String) result.get("flag")); // 인증성공여부

			mypageService.setMemberCertificationHistoryNoTrx(memberCertificationHistory); // 인증로그 남기기
		} catch (Exception e) {
			log.debug("getAccountAuthProcess setMemberCertificationHistoryNoTrx error : {}", e.getMessage());
		}

		return result;
	}

	/**
	 * @Desc :MB_회원인증이력로그 남기기
	 * @Method Name : setMemberCertificationHistoryNoTrx
	 * @Date : 2019. 5. 27.
	 * @Author : 최경호
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void setMemberCertificationHistoryNoTrx(MbMemberCertificationHistory memberCertificationHistory)
			throws Exception {
		mbMemberCertificationHistoryDao.insert(memberCertificationHistory);
	}

	/**
	 * @Desc :환불계좌 파라미터 세팅
	 * @Method Name : setAccountParam
	 * @Date : 2019. 4. 19.
	 * @Author : 최경호
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> setAccountParam(MbMember mbMember) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("url", Config.getString("nice.account.url")); // 계좌인증 URL
		paramMap.put("niceUid", Config.getString("nice.account.niceUid")); // ID
		paramMap.put("svcPwd", Config.getString("nice.account.svcPwd")); // PW
		paramMap.put("inq_rsn", Config.getString("nice.account.inq_rsn")); // 조회사유[10:회원가입 20:기존회원가입 30:성인인증 40:비회원확인
																			// 90:기타사유]
		paramMap.put("strGbn", Config.getString("nice.account.strGbn")); // 개인[1]/사업자[2]
		paramMap.put("service", Config.getString("nice.account.serviceGroup2").split("\\|")[0]); // 서비스구분[1=계좌소유주확인
																									// 2=계좌성명확인
																									// 3=계좌유효성확인]
		paramMap.put("svcGbn", Config.getString("nice.account.serviceGroup2").split("\\|")[1]); // 업무구분

		paramMap.put("strAccountNo", mbMember.getAcntNoText()); // 계좌번호
		paramMap.put("strBankCode", mbMember.getBankCode()); // 환불은행코드
		paramMap.put("strNm", mbMember.getAcntHldrName()); // 예금주명
		return paramMap;
	}

	/**
	 * @Desc :환불계좌 등록
	 * @Method Name : getAccountAuth
	 * @Date : 2019. 4. 19.
	 * @Author : 최경호
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int setRefundAcntUpdate(MbMember mbMember) throws Exception {
		return mbMemberDao.updateRefundAcnt(mbMember);
	}

	/**
	 * @Desc : 기본배송지 설정
	 * @Method Name : setDfltDlvyAddr
	 * @Date : 2019. 4. 19.
	 * @Author : 유성민
	 * @param params
	 * @return
	 */
	public Map<String, Object> setDefaultDeliveryAddress(MbMemberDeliveryAddress params) throws Exception {
		params.setModerNo(params.getMemberNo());
		// 기존 기본배송지 update
		memberService.updateDefaultDeliveryAddressCancle(params);
		// 신규 기본배송지 update
		params.setDfltDlvyAddrYn(Const.BOOLEAN_TRUE);
		params.setModDtm(UtilsDate.getSqlTimeStamp());
		return memberService.updateMemberDeliveryAddress(params);
	}

	/**
	 * @Desc : 관심브랜드 조회
	 * @Method Name : getMyInterestedBrandList
	 * @Date : 2019. 4. 29.
	 * @Author : 유성민
	 * @param pageable
	 * @return
	 */
	public Page<MbMemberInterestBrand> getMyInterestedBrandList(
			Pageable<MbMemberInterestBrand, MbMemberInterestBrand> pageable) throws Exception {
		log.debug("MypageVO params :{}", pageable.getBean());
		int count = memberService.selectMyInterestedBrandListCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			List<MbMemberInterestBrand> dataList = memberService.selectMyInterestedBrandList(pageable);

			pageable.setContent(dataList);
		}

		return pageable.getPage();
	}

	/**
	 * @Desc : 관심브랜드 삭제
	 * @Method Name : deleteMyInterestedBrand
	 * @Date : 2019. 4. 30.
	 * @Author : 유성민
	 * @param params
	 */
	public void deleteMyInterestedBrand(MbMemberInterestBrand params) throws Exception {
		memberService.deleteMyInterestedBrand(params);
	}

	/**
	 * @Desc : 회원간편로그인 조회
	 * @Method Name : getMemberEasyLoginInfo
	 * @Date : 2019. 5. 3.
	 * @Author : 유성민
	 * @param params
	 * @return
	 */
	public Map<String, Object> getMemberEasyLoginInfo(MypageVO params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		MbMemberEasyLogin mbMemberEasyLogin = new MbMemberEasyLogin();
		mbMemberEasyLogin.setMemberNo(params.getMemberNo());

		List<MbMemberEasyLogin> easyLoginList = memberService.getMemberEasyLoginInfo(mbMemberEasyLogin);
		resultMap.put("easyLoginList", easyLoginList);
		resultMap.put("commonCode", commonCodeService.getUseCode(CommonCode.SNS_GBN_CODE));

		return resultMap;
	}

	/**
	 * @Desc : sns 계정정보 조회
	 * @Method Name : getMemberEasyLoginAccount
	 * @Date : 2019. 5. 10.
	 * @Author : 이동엽
	 * @param mbMemberEasyLogin
	 * @return
	 * @throws Exception
	 */
	public MbMemberEasyLogin getMemberEasyLoginAccount(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		return memberService.getMemberEasyLoginAccount(mbMemberEasyLogin);
	}

	/**
	 * @Desc : sns 정보 변경
	 * @Method Name : updateEasyLoginInfo
	 * @Date : 2019. 5. 10.
	 * @Author : 이동엽
	 * @param mbMemberEasyLogin
	 * @throws Exception
	 */
	public void updateEasyLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		memberService.updateEasyLoginInfo(mbMemberEasyLogin);
	}

	public void deleteEasyLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		memberService.deleteEasyLoginInfo(mbMemberEasyLogin);
	}

}
