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
	 * @Desc : SAFE_KEY_TEXT(?????????) ??????
	 * @Method Name : getSafeKeyText
	 * @Date : 2019. 3. 25.
	 * @Author : ?????????
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public String getSafeKeyText(String memberNo) throws Exception {
		return memberService.getSafeKey(memberNo);
	}

	/**
	 * @Desc :???????????????????????? ??????(?????? ??????, ?????????, ????????? ??????)
	 * @Method Name : getMemberBasicInfo
	 * @Date : 2019. 3. 18.
	 * @Author : ?????????
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
					// ??????????????? ?????? [memA910a]
					// 1 totalPoint : ????????????
					// 2 accessPoint : ???????????????
					// 3 extinctPoint : ???????????????
					// 4 stampCount : ??????????????????(????????????)
					// 5 totalStampCount : ????????????????????????(????????????)
					// 6 toDoSavePoint : ?????????????????????
					// 7 eventPoint : ??????????????????
					// 8 eventDate : ?????????????????????
					// 9 preSavePoint : ?????????????????????
					PrivateReport privateReport = membershipPointService.getPrivateReportBySafeKey(param.getSafeKey());
					resultMap.put("privateReport", privateReport);

					if (UtilsText.equals(param.getMemberTypeCode(), CommonCode.MEMBER_TYPE_MEMBERSHIP)
							&& UtilsText.isBlank(param.getMbshpCardNo())) {
						// ????????? ?????? ?????? ?????? [memA830a]
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
	 * @Desc : ??????????????? ?????? ??????
	 * @Method Name : getPointInfo
	 * @Date : 2019. 3. 21.
	 * @Author : ?????????
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPointInfo(MypageVO param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// ???????????????, ????????????, ????????????, ???????????????(?????????, ??????) ?????????
		// getMemberBasicInfo ??????????????? ?????? [memA910a]

		// ????????? ?????? ?????? [memA920a]
		List<StorePointHistory> pointHistoryList = new ArrayList<>();
		if (UtilsText.isNotBlank(param.getSafeKey())) {
			pointHistoryList = membershipPointService.getStorePointHistoryBySafeKey(param.getSafeKey());
		}
		resultMap.put("pointHistoryList", pointHistoryList);
		return resultMap;
	}

	/**
	 * @Desc :????????? ???????????? ??????
	 * @Method Name : updateCardPassword
	 * @Date : 2019. 3. 22.
	 * @Author : ?????????
	 * @param param
	 * @return
	 */
	public Map<String, Object> updateCardPassword(PointVO param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_FALSE;
		String resultMsg = "";

		// ???????????? ??????
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
			// ??????????????? ???????????? ?????? [memA970a]
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
	 * @Desc : ???????????? ????????? ??????
	 * @Method Name : getPasswordValidation
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPasswordValidation(MypageVO param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_FALSE;
		String resultMsg = Message.getMessage("mypage.error.password");

		// ???????????? ??????
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
	 * @Desc : ????????????, ??????????????? ?????? ?????? ?????? ??????
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 3. 21.
	 * @Author : choi
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMemberGrade() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> policyMap = new HashMap<String, Object>();
		// TODO : ?????? ?????? ??? ????????? ????????????, ??????????????? ?????? ?????? ??????
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
	 * @Desc : ????????? ?????? ?????? ????????? ??????
	 * @Method Name : getPolicyData
	 * @Date : 2019. 5. 23.
	 * @Author : ?????????
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPolicyData() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		// ???????????? ??????
		data.put("endDate",
				cal.get(cal.YEAR) + "." + (cal.get(cal.MONTH) + 1) + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 6?????????
																															// ??????

		// 6???????????? 1???
		cal.add(cal.MONTH, -5);
		data.put("startDate",
				cal.get(cal.YEAR) + "." + (cal.get(cal.MONTH) + 1) + "." + cal.getActualMinimum(Calendar.DAY_OF_MONTH)); // 6?????????
																															// 1???

		// ????????? ?????? ?????? ??????
		data.put("policyData", cmOnlineMemberPolicyDao.selectPolicyData());

		return data;
	}

	/**
	 * @Desc :????????? ????????? ?????? ?????? ??????
	 * @Method Name : insertLatedSavePoint
	 * @Date : 2019. 3. 28.
	 * @Author : ?????????
	 * @param param
	 * @return
	 */
	public Map<String, Object> insertLatedSavePoint(MbMemberExpostSavePoint params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// ??? 10????????? ??????
		int count = memberService.getLatedSavePointRecent1MonthCount(params);
		if (count >= 10) {
			resultMap.put("resultCode", Const.BOOLEAN_FALSE);
			resultMap.put("resultMsg", Message.getMessage("mypage.error.latedSavePoint10"));
		} else {
			/** ?????????,???????????? ?????? ?????? **/
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
	 * @Desc : ????????? ????????????
	 * @Method Name : saveLatedSavePointForOnlineOrder
	 * @Date : 2019. 3. 28.
	 * @Author : ?????????
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

			/********** 1. ????????? ???????????? ?????? ?????? **********/
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

			/********** 4. ????????? ????????? ?????? ??????[memA890a] **********/
			OcOrderProduct orderProductParams = new OcOrderProduct();
			orderProductParams.setOrderNo(params.getBuyNoText());
			List<OcOrderProduct> orderProductList = orderOtherPartService.getOrderProductInfoList(orderProductParams);
			// ?????? ?????? ????????????
			boolean includedMmnyPrdtY = orderProductList.stream().map(OcOrderProduct::getMmnyPrdtYn)
					.anyMatch(x -> UtilsText.equals(x, Const.BOOLEAN_TRUE));
			log.debug("?????? ?????? ????????????  : {}", includedMmnyPrdtY);

			// ????????? ?????? ????????????
			boolean includedMmnyPrdtN = orderProductList.stream().map(OcOrderProduct::getMmnyPrdtYn)
					.anyMatch(x -> UtilsText.equals(x, Const.BOOLEAN_FALSE));
			log.debug("????????? ?????? ????????????  : {}", includedMmnyPrdtN);

			// TODO ?????????????????? ????????? ?????? ?????? ??????, ?????? ????????????
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

			/********** 5. ????????? DB??? ??????????????? ?????? **********/
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
			/********** 7. ?????????????????? SMS????????? ?????? **********/
			if (UtilsText.isNotBlank(memberInfo.getHdphnNoText())) {

				Map<String, String> map = new HashMap<>();
				map.put("memberName", memberInfo.getMemberName());
				map.put("landingUrl", Config.getString("url.www.https") + "/mypage/point");

				MessageVO messageVO = new MessageVO();
				messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME); // ?????????
				messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER); // ?????? ??????
				messageVO.setRcvrName(memberInfo.getMemberName()); // ?????????
				messageVO.setRecvTelNoText(memberInfo.getHdphnNoText()); // ????????????
				messageVO.setMesgId(MessageCode.LATED_SAVE_POINT); // ???????????? mesg_id ??????
				messageVO.setMessageTemplateModel(map);
				messageVO.setReal(true);

				messageService.setSendMessageProcessNoTrx(messageVO); // ????????? ??????
			}

		} catch (Exception e) {
			log.debug("message send error : {}");
		}

		return resultMap;
	}

	/**
	 * @Desc :???????????? ????????????
	 * @Method Name : savePossibleLatedSavePointForOfflineOrder
	 * @Date : 2019. 3. 28.
	 * @Author : ?????????
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

			/********** 1. ?????????????????? (policy)??? ?????? (????????? ????????? ????????????) **********/
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

			/********** 3. ????????? ????????? ?????? API [memB300 ??????] **********/
			String failCode = membershipPointService.updatePointAfterPurchase(memberInfo.getSafeKey(),
					params.getStoreNo(), params.getPosNoText(), params.getDealNoText(), params.getPymntAmt(),
					UtilsDate.formatter(UtilsDate.DEFAULT_DATE_PATTERN_NOT_CHARACTERS, params.getBuyYmd()));

			if (UtilsText.isNotBlank(failCode)) {
				resultMap.put("resultCode", Const.BOOLEAN_FALSE);
				resultMap.put("resultMsg", MemberShipErrorCode.getByCode(failCode).getDesc());
				return resultMap;
			}

			/********** 4. ????????? DB??? ??????????????? ?????? **********/
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

		/********** 5. ?????????????????? SMS????????? ?????? **********/
		// ????????? ?????????
		try {
			Map<String, String> map = new HashMap<>();
			map.put("memberName", memberInfo.getMemberName());
			map.put("landingUrl", Config.getString("url.www.https") + "/mypage/point");

			MessageVO messageVO = new MessageVO();
			messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME); // ?????????
			messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER); // ?????? ??????
			messageVO.setRcvrName(memberInfo.getMemberName()); // ?????????
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText()); // ????????????
			messageVO.setMesgId(MessageCode.LATED_SAVE_POINT); // ???????????? mesg_id
			messageVO.setMessageTemplateModel(map);
			messageVO.setReal(true);

			messageService.setSendMessageProcessNoTrx(messageVO); // ????????? ??????
		} catch (Exception e) {
			log.debug("messageService.setSendMessageProcessNoTrx error : {}");
		}

		return resultMap;
	}

	/**
	 * @Desc : ????????? ??????????????? ?????? ?????? ??????
	 * @Method Name : checkPossibleLatedSavePoint
	 * @Date : 2019. 4. 24.
	 * @Author : ?????????
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
	 * @Desc : ????????? ???????????? ??????????????? ??????
	 * @Method Name : saveMemberPoint
	 * @Date : 2019. 4. 24.
	 * @Author : ?????????
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
			pointParams.setSavePathType(CommonCode.RTRVL_GBN_TYPE_ONLINE); // savePathType : O(?????????:d), F(????????????,??????)
			pointParams.setSaveTypeCode(CommonCode.SAVE_TYPE_CODE_EARN_AFTERPUCHASE); // ????????????
			pointParams.setSaveUseDtm(UtilsDate.getSqlTimeStamp());
			pointParams.setSaveUseContText("??????");
			pointParams.setSaveAmt(product.getPoint());
			pointParams.setUseAmt(0);
			pointParams.setValidStartDtm(UtilsDate.getSqlTimeStamp());
			pointParams.setValidEndDtm(
					Timestamp.valueOf(LocalDateTime.of(LocalDate.now().plusYears(2).getYear(), 12, 31, 23, 59)));
			pointParams.setOrderNo(params.getBuyNoText());
			pointParams.setExpostSavePointSeq(params.getExpostSavePointSeq());
			pointParams.setRgsterNo(params.getMemberNo());

			// ??????????????????
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
	 * @Desc : ???????????? ???????????? ????????? ??????
	 * @Method Name : saveMemberPointOffline
	 * @Date : 2019. 4. 26.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveMemberPointOffline(MbMemberExpostSavePoint params) throws Exception {

		MbMemberPoint pointParams = new MbMemberPoint();
		pointParams.setMemberNo(params.getMemberNo());
		int pointSeq = memberService.selectPointSeqNextVal(pointParams);
		pointParams.setPointSeq(pointSeq);
		pointParams.setSavePathType(CommonCode.RTRVL_GBN_TYPE_OFFLINE); // savePathType : O(?????????:d), F(????????????,??????)
		pointParams.setSaveTypeCode(CommonCode.SAVE_TYPE_CODE_EARN_AFTERPUCHASE); // ????????????
		pointParams.setSaveUseDtm(UtilsDate.getSqlTimeStamp());
		pointParams.setSaveUseContText("??????");
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
	 * @Desc :???????????? ??????
	 * @Method Name : getAccountAuth
	 * @Date : 2019. 4. 18.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAccountAuthProcess(MbMember mbMember) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		AccountAuth accountAuth = new AccountAuth();

		paramMap = this.setAccountParam(mbMember); // ??????????????? ????????? ??????????????? ??????
		Map<String, Object> result = accountAuth.start(paramMap);

		try {
			MbMemberCertificationHistory memberCertificationHistory = new MbMemberCertificationHistory();
			String sessionId = UtilsRequest.getSession().getId();

			memberCertificationHistory.setCrtfcPathCode(BaseCommonCode.CRTFC_PATH_CODE_REFUND_ACCOUNT); // ??????????????????
			memberCertificationHistory.setCrtfcTypeCode(BaseCommonCode.CRTFC_TYPE_CODE_REFUND_ACCOUNT); // ??????????????????
			memberCertificationHistory.setMemberNo(mbMember.getMemberNo()); // ????????????
			memberCertificationHistory.setAccessIpText(systemService.getIpAddress()); // ??????ip
			memberCertificationHistory.setSessionId(sessionId); // ???????????????
			memberCertificationHistory.setCrtfcSuccessYn((String) result.get("flag")); // ??????????????????

			mypageService.setMemberCertificationHistoryNoTrx(memberCertificationHistory); // ???????????? ?????????
		} catch (Exception e) {
			log.debug("getAccountAuthProcess setMemberCertificationHistoryNoTrx error : {}", e.getMessage());
		}

		return result;
	}

	/**
	 * @Desc :MB_???????????????????????? ?????????
	 * @Method Name : setMemberCertificationHistoryNoTrx
	 * @Date : 2019. 5. 27.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void setMemberCertificationHistoryNoTrx(MbMemberCertificationHistory memberCertificationHistory)
			throws Exception {
		mbMemberCertificationHistoryDao.insert(memberCertificationHistory);
	}

	/**
	 * @Desc :???????????? ???????????? ??????
	 * @Method Name : setAccountParam
	 * @Date : 2019. 4. 19.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> setAccountParam(MbMember mbMember) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("url", Config.getString("nice.account.url")); // ???????????? URL
		paramMap.put("niceUid", Config.getString("nice.account.niceUid")); // ID
		paramMap.put("svcPwd", Config.getString("nice.account.svcPwd")); // PW
		paramMap.put("inq_rsn", Config.getString("nice.account.inq_rsn")); // ????????????[10:???????????? 20:?????????????????? 30:???????????? 40:???????????????
																			// 90:????????????]
		paramMap.put("strGbn", Config.getString("nice.account.strGbn")); // ??????[1]/?????????[2]
		paramMap.put("service", Config.getString("nice.account.serviceGroup2").split("\\|")[0]); // ???????????????[1=?????????????????????
																									// 2=??????????????????
																									// 3=?????????????????????]
		paramMap.put("svcGbn", Config.getString("nice.account.serviceGroup2").split("\\|")[1]); // ????????????

		paramMap.put("strAccountNo", mbMember.getAcntNoText()); // ????????????
		paramMap.put("strBankCode", mbMember.getBankCode()); // ??????????????????
		paramMap.put("strNm", mbMember.getAcntHldrName()); // ????????????
		return paramMap;
	}

	/**
	 * @Desc :???????????? ??????
	 * @Method Name : getAccountAuth
	 * @Date : 2019. 4. 19.
	 * @Author : ?????????
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int setRefundAcntUpdate(MbMember mbMember) throws Exception {
		return mbMemberDao.updateRefundAcnt(mbMember);
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : setDfltDlvyAddr
	 * @Date : 2019. 4. 19.
	 * @Author : ?????????
	 * @param params
	 * @return
	 */
	public Map<String, Object> setDefaultDeliveryAddress(MbMemberDeliveryAddress params) throws Exception {
		params.setModerNo(params.getMemberNo());
		// ?????? ??????????????? update
		memberService.updateDefaultDeliveryAddressCancle(params);
		// ?????? ??????????????? update
		params.setDfltDlvyAddrYn(Const.BOOLEAN_TRUE);
		params.setModDtm(UtilsDate.getSqlTimeStamp());
		return memberService.updateMemberDeliveryAddress(params);
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : getMyInterestedBrandList
	 * @Date : 2019. 4. 29.
	 * @Author : ?????????
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
	 * @Desc : ??????????????? ??????
	 * @Method Name : deleteMyInterestedBrand
	 * @Date : 2019. 4. 30.
	 * @Author : ?????????
	 * @param params
	 */
	public void deleteMyInterestedBrand(MbMemberInterestBrand params) throws Exception {
		memberService.deleteMyInterestedBrand(params);
	}

	/**
	 * @Desc : ????????????????????? ??????
	 * @Method Name : getMemberEasyLoginInfo
	 * @Date : 2019. 5. 3.
	 * @Author : ?????????
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
	 * @Desc : sns ???????????? ??????
	 * @Method Name : getMemberEasyLoginAccount
	 * @Date : 2019. 5. 10.
	 * @Author : ?????????
	 * @param mbMemberEasyLogin
	 * @return
	 * @throws Exception
	 */
	public MbMemberEasyLogin getMemberEasyLoginAccount(MbMemberEasyLogin mbMemberEasyLogin) throws Exception {
		return memberService.getMemberEasyLoginAccount(mbMemberEasyLogin);
	}

	/**
	 * @Desc : sns ?????? ??????
	 * @Method Name : updateEasyLoginInfo
	 * @Date : 2019. 5. 10.
	 * @Author : ?????????
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
