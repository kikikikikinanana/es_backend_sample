package kr.co.shop.web.event.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.event.model.master.EvEvent;
import kr.co.shop.web.event.model.master.EvEventAnswer;
import kr.co.shop.web.event.model.master.EvEventAttendanceCheckBenefit;
import kr.co.shop.web.event.model.master.EvEventAttendanceCheckMember;
import kr.co.shop.web.event.model.master.EvEventJoinMember;
import kr.co.shop.web.event.model.master.EvEventPublicationNumber;
import kr.co.shop.web.event.model.master.EvEventResult;
import kr.co.shop.web.event.model.master.EvEventResultBenefitMember;
import kr.co.shop.web.event.model.master.EvEventRouletteBenefit;
import kr.co.shop.web.event.model.master.EvEventRouletteJoinMember;
import kr.co.shop.web.event.model.master.EvEventTargetGrade;
import kr.co.shop.web.event.model.master.EvEventTargetProduct;
import kr.co.shop.web.event.repository.master.EvEventAnswerDao;
import kr.co.shop.web.event.repository.master.EvEventAttendanceCheckBenefitDao;
import kr.co.shop.web.event.repository.master.EvEventAttendanceCheckMemberDao;
import kr.co.shop.web.event.repository.master.EvEventDao;
import kr.co.shop.web.event.repository.master.EvEventJoinMemberDao;
import kr.co.shop.web.event.repository.master.EvEventProductReceiptStoreDao;
import kr.co.shop.web.event.repository.master.EvEventPublicationNumberDao;
import kr.co.shop.web.event.repository.master.EvEventResultBenefitMemberDao;
import kr.co.shop.web.event.repository.master.EvEventResultDao;
import kr.co.shop.web.event.repository.master.EvEventRouletteBenefitDao;
import kr.co.shop.web.event.repository.master.EvEventRouletteJoinMemberDao;
import kr.co.shop.web.event.repository.master.EvEventTargetCouponDao;
import kr.co.shop.web.event.repository.master.EvEventTargetGradeDao;
import kr.co.shop.web.event.repository.master.EvEventTargetProductDao;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberPoint;
import kr.co.shop.web.member.repository.master.MbMemberDao;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductOption;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.repository.master.PdProductOptionDao;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.repository.master.PrCouponDao;
import kr.co.shop.web.promotion.service.CouponService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {
	@Autowired
	private MbMemberDao mbMemberDao;

	@Autowired
	private EvEventDao evEventDao;

	@Autowired
	private EvEventResultDao evEventResultDao;

	@Autowired
	private EvEventAnswerDao evEventAnswerDao;

	@Autowired
	private EvEventResultBenefitMemberDao evEventResultBenefitMemberDao;

	@Autowired
	private EvEventTargetGradeDao evEventTargetGradeDao;

	@Autowired
	private EvEventAttendanceCheckBenefitDao evEventAttendanceCheckBenefitDao;

	@Autowired
	private EvEventAttendanceCheckMemberDao evEventAttendanceCheckMemberDao;

	@Autowired
	private EvEventRouletteBenefitDao evEventRouletteBenefitDao;

	@Autowired
	private EvEventRouletteJoinMemberDao evEventRouletteJoinMemberDao;

	@Autowired
	private EvEventTargetProductDao evEventTargetProductDao;

	@Autowired
	private PrCouponDao prCouponDao;

	@Autowired
	private EvEventJoinMemberDao evEventJoinMemberDao;

	@Autowired
	private PdProductOptionDao productOptionDao;

	@Autowired
	private EvEventProductReceiptStoreDao evEventProductReceiptStoreDao;

	@Autowired
	private EvEventTargetCouponDao evEventTargetCouponDao;

	@Autowired
	private EvEventPublicationNumberDao evEventPublicationNumberDao;

	@Autowired
	private CouponService couponService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProductService productService;

	/**
	 * @Desc : ????????? ????????? ??????
	 * @Method Name : getEventList
	 * @Date : 2019. 5. 10.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getEventList(Pageable<EvEvent, EvEvent> pageable) throws Exception {
		Map<String, Object> map = new HashMap<>();

		int totalCount = evEventDao.selectEventCount(pageable);

		if (totalCount > 0) {
			List<EvEvent> eventList = evEventDao.selectEventList(pageable);
			map.put("eventList", eventList);
		}

		map.put("totalCount", totalCount);

		return map;
	}

	/**
	 * @Desc : ????????? ?????? ??????
	 * @Method Name : getEvent
	 * @Date : 2019. 5. 15.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getEvent(EvEvent evEvent) throws Exception {
		Map<String, Object> map = new HashMap<>();

		EvEvent event = evEventDao.selectEvent(evEvent);

		if (event != null) {
			// ???????????? ??????
			if (UtilsText.equals(event.getEventTypeCode(),
					CommonCode.EVENT_TYPE_CODE_PARTICIPATION_TYPE_ATTENDANCE_CHECK)) {
				EvEventAttendanceCheckMember eventAttendanceCheckMember = new EvEventAttendanceCheckMember();
				eventAttendanceCheckMember.setEventNo(event.getEventNo());
				eventAttendanceCheckMember.setMemberNo(evEvent.getMemberNo());

				List<EvEventAttendanceCheckBenefit> attendanceBenefitList = evEventAttendanceCheckBenefitDao
						.selectAttendanceBenefitByEventNo(event.getEventNo());
				// ?????? ?????????
				map.put("attendanceBenefitList", attendanceBenefitList);

				List<EvEventAttendanceCheckMember> attendanceCheckCalendar = evEventAttendanceCheckMemberDao
						.selectAttendanceCheckCalendar(eventAttendanceCheckMember);
				// ???????????????
				map.put("attendanceCheckCalendar", attendanceCheckCalendar);

				int attendanceCheckMemberCnt = evEventAttendanceCheckMemberDao
						.selectAttendanceCheckMemberCnt(eventAttendanceCheckMember);
				// ???????????? ???
				map.put("attendanceCheckMemberCnt", attendanceCheckMemberCnt);

				eventAttendanceCheckMember.setAtendDtm(new Timestamp(new Date().getTime()));
				int todayAttendanceCheckMemberCnt = evEventAttendanceCheckMemberDao
						.selectAttendanceCheckMemberCnt(eventAttendanceCheckMember);

				// today ???????????? ???
				map.put("todayAttendanceCheckMemberCnt", todayAttendanceCheckMemberCnt);
			} else if (UtilsText.equals(event.getEventTypeCode(), CommonCode.EVENT_TYPE_CODE_NOTICE_TYPE)) {
				if (UtilsText.isNotBlank(event.getEventJoinType())) {
					String eventJoinType = event.getEventJoinType();
					if (UtilsText.equals(eventJoinType, "C")) {
						// ????????? ?????? ?????? ?????????
						map.put("targetCouponList",
								evEventTargetCouponDao.getEventTargetCouponListByEventNo(event.getEventNo()));
					}
				}
			} else if (UtilsText.equals(event.getEventTypeCode(), CommonCode.EVENT_TYPE_CODE_PARTICIPATION_TYPE_DRAW)) {
				map.put("targetProductList",
						evEventTargetProductDao.selectDrawTargetProductListByEventNo(event.getEventNo()));
			}
		}

		map.put("event", event);

		return map;
	}

	/**
	 * @Desc : ????????? ?????? ????????? ??????
	 * @Method Name : getEventResultList
	 * @Date : 2019. 5. 10.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getEventResultList(Pageable<EvEventResult, EvEventResult> pageable) throws Exception {
		Map<String, Object> map = new HashMap<>();

		int totalCount = evEventResultDao.selectEventResultCount(pageable);

		if (totalCount > 0) {
			List<EvEventResult> eventResultList = evEventResultDao.selectEventResultList(pageable);
			map.put("eventResultList", eventResultList);
		}

		map.put("totalCount", totalCount);

		return map;
	}

	/**
	 * @Desc : ????????? ?????? ?????? ??????
	 * @Method Name : getEventResult
	 * @Date : 2019. 5. 14.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getEventResult(EvEventResult evEventResult) throws Exception {
		Map<String, Object> map = new HashMap<>();

		EvEventResult eventResult = evEventResultDao.selectEventResult(evEventResult);

		EvEventResultBenefitMember benefitMember = new EvEventResultBenefitMember();
		benefitMember.setEventNo(evEventResult.getEventNo());
		List<EvEventResultBenefitMember> benefitMemberList = evEventResultBenefitMemberDao
				.selectEventResultBeneftMemberList(benefitMember);

		map.put("eventResult", eventResult);
		map.put("benefitMemberList", benefitMemberList);

		return map;
	}

	/**
	 * @Desc : ?????? ?????? ????????? ????????? ??????
	 * @Method Name : getMemberEventList
	 * @Date : 2019. 5. 08.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getMemberEventList(Pageable<EvEvent, EvEvent> pageable) throws Exception {
		Map<String, Object> map = new HashMap<>();

		int totalCount = evEventDao.selectMemberEventCount(pageable);
		if (totalCount > 0) {
			List<EvEvent> eventList = evEventDao.selectMemberEventList(pageable);
			map.put("eventList", eventList);
		}
		map.put("totalCount", totalCount);

		return map;
	}

	/**
	 * @Desc : ?????? ?????? ????????? ?????? ??????(?????? 2??????)
	 * @Method Name : getEventJoinMemberCount
	 * @Date : 2019. 6. 25.
	 * @Author : ?????????
	 * @param evEventJoinMember
	 * @return
	 */
	public Map<String, Object> getEventJoinMemberCount(EvEventJoinMember evEventJoinMember) throws Exception {
		Map<String, Object> map = new HashMap<>();
		evEventJoinMember.setRecentYn("Y");
		evEventJoinMember.setWinYn("N");
		map.put("joinCount", evEventJoinMemberDao.selectEventJoinMemberCount(evEventJoinMember));
		evEventJoinMember.setWinYn("Y");
		map.put("winCount", evEventJoinMemberDao.selectEventJoinMemberCount(evEventJoinMember));

		return map;
	}

	/**
	 * @Desc : ????????? ?????? ?????? ????????? ??????
	 * @Method Name : getEventAnswerList
	 * @Date : 2019. 5. 16.
	 * @Author : ?????????
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getEventAnswerList(Pageable<EvEventAnswer, EvEventAnswer> pageable) throws Exception {
		Map<String, Object> map = new HashMap<>();

		int totalCount = evEventAnswerDao.selectEventAnswerCount(pageable);

		if (totalCount > 0) {
			List<EvEventAnswer> eventAnswerList = evEventAnswerDao.selectEventAnswerList(pageable);
			map.put("eventAnswerList", eventAnswerList);
		}

		map.put("totalCount", totalCount);

		return map;
	}

	/**
	 * ????????? ?????? ??????
	 * 
	 * @Desc : ????????? ?????? ??????
	 * @Method Name : insertEventAnswer
	 * @Date : 2019. 5. 16
	 * @Author : easyhun
	 * @param prCoupon
	 * @throws Exception
	 */
	public Map<String, Object> insertEventAnswer(EvEventAnswer evEventAnswer) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String eventNo = evEventAnswer.getEventNo();
		String memberNo = evEventAnswer.getMemberNo();
		String exceptionType = "";

		if (eventNo != null) {
			MbMember mbMember = new MbMember();
			mbMember.setMemberNo(memberNo);
			mbMember = mbMemberDao.selectByPrimaryKey(mbMember);

			EvEventTargetGrade evEventTargetGrade = new EvEventTargetGrade();
			evEventTargetGrade.setEventNo(eventNo);
			evEventTargetGrade.setMemberTypeCode(mbMember.getMemberTypeCode());
			evEventTargetGrade.setMbshpGradeCode(mbMember.getMbshpGradeCode());
			evEventTargetGrade.setEmpYn(mbMember.getEmpYn());
			evEventTargetGrade = evEventTargetGradeDao.selectEventTargetGrade(evEventTargetGrade);

			if (evEventTargetGrade != null) {
				if (!UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_UNLIMITED)) {
					int joinCount = 0;
					evEventAnswer.setLimitType(evEventTargetGrade.getLimitType());
					joinCount = evEventAnswerDao.selectEventAnswerJoinCount(evEventAnswer);

					if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
							CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_DAY)) { // 1??? 1???
						if (joinCount >= 1)
							exceptionType = "oncePerDay";
					} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
							CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_ID)) { // ID ??? 1???
						if (joinCount >= 1)
							exceptionType = "oncePerId";
					} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
							CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_DAY)) { // 1??? 2???
						if (joinCount >= 2)
							exceptionType = "twicePerDay";
					} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
							CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_ID)) { // ID ??? 2???
						if (joinCount >= 2)
							exceptionType = "twicePerId";
					}
				}

				if (UtilsText.isBlank(exceptionType)) {
					evEventAnswer.setRgstDtm(new Timestamp(new Date().getTime()));
					evEventAnswer.setModDtm(new Timestamp(new Date().getTime()));
					evEventAnswer.setModerNo(memberNo);
					evEventAnswer.setDeviceCode(CommonCode.DEVICE_PC);
					evEventAnswer.setDispYn("Y");
					evEventAnswerDao.insert(evEventAnswer);
				}
			} else {
				exceptionType = "noneTarget";
			}
		}
		map.put("exceptionType", exceptionType);

		return map;
	}

	/**
	 * @Desc : ????????? ?????? ?????? ??????
	 * @Method Name : updateEventAnswer
	 * @Date : 2019. 5. 20.
	 * @Author : ?????????
	 * @param evEventAnswer
	 * @return
	 */
	public void updateEventAnswer(EvEventAnswer evEventAnswer) throws Exception {
		evEventAnswerDao.updateEventAnswer(evEventAnswer);
	}

	/**
	 * @Desc : ????????? ?????? ?????? ??????
	 * @Method Name : deleteEventAnswer
	 * @Date : 2019. 5. 17.
	 * @Author : ?????????
	 * @param evEventAnswer
	 * @return
	 */
	public void deleteEventAnswer(EvEventAnswer evEventAnswer) throws Exception {
		evEventAnswerDao.delete(evEventAnswer);
	}

	/**
	 * @Desc : ????????? ???????????? ??????
	 * @Method Name : insertEventAttendanceCheckMember
	 * @Date : 2019. 5. 22.
	 * @Author : ?????????
	 * @param evEventAttendanceCheckMember
	 * @return
	 */
	public Map<String, Object> insertEventAttendanceCheckMember(
			EvEventAttendanceCheckMember evEventAttendanceCheckMember) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String eventNo = evEventAttendanceCheckMember.getEventNo();
		String memberNo = evEventAttendanceCheckMember.getMemberNo();
		String exceptionType = "";

		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = mbMemberDao.selectByPrimaryKey(mbMember);

		EvEventTargetGrade evEventTargetGrade = new EvEventTargetGrade();
		evEventTargetGrade.setEventNo(eventNo);
		evEventTargetGrade.setMemberTypeCode(mbMember.getMemberTypeCode());
		evEventTargetGrade.setMbshpGradeCode(mbMember.getMbshpGradeCode());
		evEventTargetGrade.setEmpYn(mbMember.getEmpYn());
		evEventTargetGrade = evEventTargetGradeDao.selectEventTargetGrade(evEventTargetGrade);

		if (evEventTargetGrade != null) {
			evEventAttendanceCheckMemberDao.insert(evEventAttendanceCheckMember);

			List<EvEventAttendanceCheckBenefit> attendanceBenefitList = evEventAttendanceCheckBenefitDao
					.selectAttendanceBenefitByEventNo(eventNo);

			int attendanceCheckMemberCnt = evEventAttendanceCheckMemberDao
					.selectAttendanceCheckMemberCnt(evEventAttendanceCheckMember);

			for (EvEventAttendanceCheckBenefit attendanceCheckBenefit : attendanceBenefitList) {
				if (attendanceCheckMemberCnt == attendanceCheckBenefit.getAtendDayCount()) {
					if (UtilsText.equals(attendanceCheckBenefit.getBenefitType(), "C")) {
						PrCoupon prCoupon = new PrCoupon();
						prCoupon.setCpnNo(attendanceCheckBenefit.getBenefitValue());
						prCoupon.setMemberNo(evEventAttendanceCheckMember.getMemberNo());
						couponService.insertCouponWithoutCondition(prCoupon);
					}
				}
			}
		} else {
			exceptionType = "noneTarget";
		}

		map.put("exceptionType", exceptionType);
		return map;
	}

	/**
	 * @Desc : ????????? ?????? ??????
	 * @Method Name : insertEventRoulette
	 * @Date : 2019. 5. 23.
	 * @Author : ?????????
	 * @param evEventAttendanceCheckMember
	 * @return
	 */
	public Map<String, Object> insertEventRoulette(EvEventRouletteBenefit evEventRouletteBenefit) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String eventNo = evEventRouletteBenefit.getEventNo();
		String memberNo = evEventRouletteBenefit.getMemberNo();
		String exceptionType = "";

		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = mbMemberDao.selectByPrimaryKey(mbMember);

		EvEventTargetGrade evEventTargetGrade = new EvEventTargetGrade();
		evEventTargetGrade.setEventNo(eventNo);
		evEventTargetGrade.setMemberTypeCode(mbMember.getMemberTypeCode());
		evEventTargetGrade.setMbshpGradeCode(mbMember.getMbshpGradeCode());
		evEventTargetGrade.setEmpYn(mbMember.getEmpYn());
		evEventTargetGrade = evEventTargetGradeDao.selectEventTargetGrade(evEventTargetGrade);

		if (evEventTargetGrade != null) {
			EvEventRouletteJoinMember eventRouletteJoinMember = new EvEventRouletteJoinMember();
			eventRouletteJoinMember.setEventNo(eventNo);
			eventRouletteJoinMember.setMemberNo(memberNo);
			eventRouletteJoinMember.setLimitType(evEventTargetGrade.getLimitType());
			if (!UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
					CommonCode.JOIN_LIMIT_TYPE_CODE_UNLIMITED)) {
				int joinCount = 0;
				joinCount = evEventRouletteJoinMemberDao.selectEventRouletteJoinMemberCount(eventRouletteJoinMember);

				if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_DAY)) { // 1??? 1???
					if (joinCount >= 1)
						exceptionType = "oncePerDay";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_ID)) { // ID ??? 1???
					if (joinCount >= 1)
						exceptionType = "oncePerId";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_DAY)) { // 1??? 2???
					if (joinCount >= 2)
						exceptionType = "twicePerDay";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_ID)) { // ID ??? 2???
					if (joinCount >= 2)
						exceptionType = "twicePerId";
				}
			}

			if (UtilsText.isBlank(exceptionType)) {
				int randomNumber = (int) (Math.random() * 100 + 1);
				List<Short> randomNumberList = new ArrayList<>();

				List<EvEventRouletteBenefit> eventRouletteBenefitList = evEventRouletteBenefitDao
						.selectEventRouletteBenefitByEventNo(eventNo);

				// ?????????????????? ????????? push
				for (EvEventRouletteBenefit eventRouletteBenefit : eventRouletteBenefitList) {
					for (int i = 0; i < eventRouletteBenefit.getWinRate(); i++) {
						randomNumberList.add(eventRouletteBenefit.getEventRuletBenefitSeq());
					}
				}

				int failCount = 100 - randomNumberList.size();

				for (int i = 0; i < failCount; i++) {
					randomNumberList.add((short) -1);
				}

				// ????????? ?????? ??????
				Collections.shuffle(randomNumberList);
				EvEventRouletteBenefit drawBenefit = new EvEventRouletteBenefit();
				drawBenefit.setEventNo(eventNo);
				if (randomNumberList.get(randomNumber) != -1) {
					drawBenefit.setEventRuletBenefitSeq(randomNumberList.get(randomNumber));
					drawBenefit = evEventRouletteBenefitDao.selectByPrimaryKey(drawBenefit);
					if (drawBenefit.getIssueCount() <= drawBenefit.getIssueLimitCount()) {
						if (UtilsText.equals(drawBenefit.getBenefitGbnCode(), CommonCode.BENEFIT_GBN_CODE_COUPON)) {
							PrCoupon prCoupon = new PrCoupon();
							prCoupon.setCpnNo(drawBenefit.getBenefitValue());
							prCoupon.setMemberNo(memberNo);
							couponService.insertCouponWithoutCondition(prCoupon);
							prCoupon = prCouponDao.selectPrCoupon(prCoupon);
							map.put("prCoupon", prCoupon);
						} else if (UtilsText.equals(drawBenefit.getBenefitGbnCode(),
								CommonCode.BENEFIT_GBN_CODE_POINT)) {
							MbMemberPoint memberPoint = new MbMemberPoint();
							memberPoint.setMemberNo(memberNo);
							memberPoint.setPointSeq(memberService.selectPointSeqNextVal(memberPoint));
							memberPoint.setSavePathType("O");
							memberPoint.setSaveTypeCode(CommonCode.SAVE_TYPE_CODE_EARN_EVENT);
							memberPoint.setSaveUseContText("????????? ?????? ??????");
							memberPoint.setSaveAmt(Integer.parseInt(drawBenefit.getBenefitValue()));
							memberPoint.setUseAmt(0);
							memberPoint.setEventNo(eventNo);
							memberPoint.setRgsterNo(memberNo);
							memberService.insertMemberPoint(memberPoint);
						}
					} else {
						exceptionType = "fail";
					}
					evEventRouletteBenefitDao.updateLimitWinRate(drawBenefit);
				} else {
					drawBenefit.setBenefitGbnCode(CommonCode.BENEFIT_GBN_CODE_FAIL);
					drawBenefit = evEventRouletteBenefitDao.selectByBenefitGbnCode(drawBenefit);
					exceptionType = "fail";
				}
				map.put("drawBenefit", drawBenefit);
				// ?????? ???????????? insert
				eventRouletteJoinMember.setEventRuletBenefitSeq(drawBenefit.getEventRuletBenefitSeq());
				eventRouletteJoinMember.setJoinDtm(new Timestamp(new Date().getTime()));
				eventRouletteJoinMember.setDeviceCode(evEventRouletteBenefit.getDeviceCode());
				if (UtilsText.equals(drawBenefit.getBenefitGbnCode(), CommonCode.BENEFIT_GBN_CODE_COUPON)
						|| UtilsText.equals(drawBenefit.getBenefitGbnCode(), CommonCode.BENEFIT_GBN_CODE_POINT)) {
					eventRouletteJoinMember.setIssueYn("Y");
				} else {
					eventRouletteJoinMember.setIssueYn("N");
				}
				eventRouletteJoinMember.setIssueInfo(evEventRouletteBenefit.getDeviceCode());
				evEventRouletteJoinMemberDao.insert(eventRouletteJoinMember);
			}

		} else {
			exceptionType = "noneTarget";
		}

		map.put("exceptionType", exceptionType);
		return map;
	}

	/**
	 * @Desc : ????????? ????????? ?????? ?????? top1
	 * @Method Name : getDrawTargetProduct
	 * @Date : 2019. 5. 28.
	 * @Author : ?????????
	 * @param getDrawTargetProduct
	 * @return
	 */
	public Map<String, Object> getDrawTargetProduct(Parameter<EvEventTargetProduct> parameter) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("drawTargetProduct", evEventTargetProductDao.selectDrawTargetProduct(parameter.get()));
		return map;
	}

	/**
	 * @Desc : ????????? ????????? ?????? ?????? ?????????
	 * @Method Name : getDrawTargetProductList
	 * @Date : 2019. 6. 21.
	 * @Author : ?????????
	 * @param getDrawTargetProductList
	 * @return
	 */
	public Map<String, Object> getDrawTargetProductList(Parameter<EvEventTargetProduct> parameter) throws Exception {
		Map<String, Object> map = new HashMap<>();

		EvEventTargetProduct evEventTargetProduct = parameter.get();

		PageableProduct<EvEventTargetProduct, PdProductWrapper> pageable = new PageableProduct<EvEventTargetProduct, PdProductWrapper>(
				parameter);

		pageable.setCondition(evEventTargetProduct.getSiteNo(), evEventTargetProduct.getChnnlNo());

		if (UtilsText.isNotBlank(evEventTargetProduct.getSearchDate())) {
			pageable.setConditionSellStartDate(evEventTargetProduct.getSearchDate());
		}

		/*
		 * pageable.setUseTableMapping("EV_EVENT_TARGET_PRODUCT", new
		 * LinkedHashMap<String, String>() { { put("prdt_no",
		 * parameter.get().getPrdtNo()); } }, null);
		 */

		// pageable.setCategoryMapping(parameter.get().getCtgrNo());
		if (UtilsText.isNotBlank(evEventTargetProduct.getSearchCtgrName())) {
			pageable.setConditionCategoryName(evEventTargetProduct.getSearchCtgrName());
		}

		String pagingSortType = evEventTargetProduct.getPagingSortType() != null
				? evEventTargetProduct.getPagingSortType()
				: PageableProduct.PAGING_SORT_TYPE_NEWEST;

		pageable.setUsePaging(true, pagingSortType, null, null);

		String tabType = "";
		if (UtilsText.equals(evEventTargetProduct.getTabType(), "ALL"))
			tabType = PageableProduct.CONDITION_LAUNCH_PRODUCT_ALL;
		else if (UtilsText.equals(evEventTargetProduct.getTabType(), "LAUNCHED"))
			tabType = PageableProduct.CONDITION_LAUNCH_PRODUCT_LAUNCHED;
		else
			tabType = PageableProduct.CONDITION_LAUNCH_PRODUCT_UPCOMMING;

		pageable.setConditionLaunchProduct(tabType);

		if (evEventTargetProduct.getRowsPerPage() == 10) {
			pageable.setRowsPerPage(12); // default
		} else {
			pageable.setRowsPerPage(evEventTargetProduct.getRowsPerPage());
		}

		pageable.setPageNum(parameter.get().getPageNum());
		Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageable);
		Map<String, Integer> totalCount = this.productService.getDisplayProductTotalCount(pageable);
		map.put("productList", productList);
		map.put("totalCount", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		return map;
	}

	/**
	 * @Desc : ????????? ?????? ??????
	 * @Method Name : insertEventJoinMember
	 * @Date : 2019. 6. 14.
	 * @Author : ?????????
	 * @param evEventJoinMember
	 * @return
	 */
	public Map<String, Object> insertEventJoinMember(EvEventJoinMember evEventJoinMember) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String eventNo = evEventJoinMember.getEventNo();
		String memberNo = evEventJoinMember.getMemberNo();
		String exceptionType = "";

		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = mbMemberDao.selectByPrimaryKey(mbMember);

		EvEventTargetGrade evEventTargetGrade = new EvEventTargetGrade();
		evEventTargetGrade.setEventNo(eventNo);
		evEventTargetGrade.setMemberTypeCode(mbMember.getMemberTypeCode());
		evEventTargetGrade.setMbshpGradeCode(mbMember.getMbshpGradeCode());
		evEventTargetGrade.setEmpYn(mbMember.getEmpYn());
		evEventTargetGrade = evEventTargetGradeDao.selectEventTargetGrade(evEventTargetGrade);

		if (evEventTargetGrade != null) {
			evEventJoinMember.setLimitType(evEventTargetGrade.getLimitType());
			if (!UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
					CommonCode.JOIN_LIMIT_TYPE_CODE_UNLIMITED)) {
				int joinCount = 0;
				joinCount = evEventJoinMemberDao.selectEventJoinMemberCount(evEventJoinMember);

				if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_DAY)) { // 1??? 1???
					if (joinCount >= 1)
						exceptionType = "oncePerDay";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_ID)) { // ID ??? 1???
					if (joinCount >= 1)
						exceptionType = "oncePerId";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_DAY)) { // 1??? 2???
					if (joinCount >= 2)
						exceptionType = "twicePerDay";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_ID)) { // ID ??? 2???
					if (joinCount >= 2)
						exceptionType = "twicePerId";
				}
			}

			if (UtilsText.isBlank(exceptionType)) {
				if (UtilsText.isNotBlank(evEventJoinMember.getLoginIdDupPermYn())) { // ????????? event
					if (UtilsText.equals(evEventJoinMember.getLoginIdDupPermYn(), "N")) {
						int loginIdDupCheckCount = evEventJoinMemberDao.selectEventJoinMemberCount(evEventJoinMember);
						if (loginIdDupCheckCount == 0) {
							evEventJoinMember.setWinYn("N");
							evEventJoinMemberDao.insert(evEventJoinMember);
						} else {
							exceptionType = "duplLoginId";
						}
					} else {
						evEventJoinMember.setWinYn("N");
						evEventJoinMemberDao.insert(evEventJoinMember);
					}
				} else { // draw
					evEventJoinMember.setWinYn("N");
					evEventJoinMemberDao.insert(evEventJoinMember);
					map.put("eventJoinMember", evEventJoinMemberDao.selectByPrimaryKey(evEventJoinMember));
					EvEventTargetProduct evEventTargetProduct = new EvEventTargetProduct();
					evEventTargetProduct.setPrdtNo(evEventJoinMember.getPrdtNo());
					map.put("drawTargetProduct", evEventTargetProductDao.selectDrawTargetProduct(evEventTargetProduct));
				}

			}
		} else {
			exceptionType = "noneTarget";
		}

		map.put("exceptionType", exceptionType);
		return map;
	}

	/**
	 * @Desc : ????????? ?????? ??????(????????? ????????????)
	 * @Method Name : insertEventJoinMember
	 * @Date : 2019. 6. 14.
	 * @Author : ?????????
	 * @param evEventJoinMember
	 * @return
	 */
	public Map<String, Object> insertEventPublNumber(EvEventJoinMember evEventJoinMember) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String eventNo = evEventJoinMember.getEventNo();
		String memberNo = evEventJoinMember.getMemberNo();
		String exceptionType = "";

		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = mbMemberDao.selectByPrimaryKey(mbMember);

		EvEventTargetGrade evEventTargetGrade = new EvEventTargetGrade();
		evEventTargetGrade.setEventNo(eventNo);
		evEventTargetGrade.setMemberTypeCode(mbMember.getMemberTypeCode());
		evEventTargetGrade.setMbshpGradeCode(mbMember.getMbshpGradeCode());
		evEventTargetGrade.setEmpYn(mbMember.getEmpYn());
		evEventTargetGrade = evEventTargetGradeDao.selectEventTargetGrade(evEventTargetGrade);

		if (evEventTargetGrade != null) {
			EvEventPublicationNumber evEventPublicationNumber = new EvEventPublicationNumber();
			evEventPublicationNumber.setEventNo(eventNo);
			evEventPublicationNumber.setEventPblicteNo(evEventJoinMember.getAddInfo1());
			evEventPublicationNumber = evEventPublicationNumberDao.selectByPrimaryKey(evEventPublicationNumber);

			if (evEventPublicationNumber != null && UtilsText.equals(evEventPublicationNumber.getRgstYn(), "N")) {
				evEventJoinMember.setWinYn("N");
				evEventJoinMemberDao.insert(evEventJoinMember);

				evEventPublicationNumber.setRgstYn("Y");
				evEventPublicationNumberDao.updatePublNumber(evEventPublicationNumber);
			} else if (evEventPublicationNumber != null
					&& UtilsText.equals(evEventPublicationNumber.getRgstYn(), "Y")) {
				exceptionType = "existEventJoin";
			} else {
				exceptionType = "nonePblicteNo";
			}
		} else {
			exceptionType = "noneTarget";
		}

		map.put("exceptionType", exceptionType);
		return map;
	}

	/**
	 * @Desc : ????????? ????????? ?????? ??????
	 * @Method Name : getDrawInfo
	 * @Date : 2019. 6. 14.
	 * @Author : ?????????
	 * @param getDrawInfo
	 * @return
	 */
	public Map<String, Object> getDrawInfo(EvEventJoinMember evEventJoinMember) throws Exception {
		Map<String, Object> map = new HashMap<>();
		PdProductOption pdProductOption = new PdProductOption();
		pdProductOption.setPrdtNo(evEventJoinMember.getPrdtNo());

		EvEvent evEvent = new EvEvent();
		evEvent.setEventNo(evEventJoinMember.getEventNo());

		map.put("storeInfoList",
				evEventProductReceiptStoreDao.selectEventProductReceiptStoreByEventNo(evEventJoinMember.getEventNo()));
		map.put("event", evEventDao.selectEvent(evEvent));
		map.put("productOptionList", productOptionDao.select(pdProductOption));

		return map;
	}

	/**
	 * @Desc : ????????? ????????? ?????? ?????? ??????(??????)
	 * @Method Name : getDrawOrderCheck
	 * @Date : 2019. 6. 25.
	 * @Author : ?????????
	 * @param evEventJoinMember
	 * @return
	 */
	public EvEventJoinMember getDrawOrderCheck(EvEventJoinMember evEventJoinMember) throws Exception {

		return evEventJoinMemberDao.selectDrawOrderCheckInfo(evEventJoinMember);
	}

	/**
	 * @Desc : ????????? ??????????????? (eventNo, memberNo, prdtNo)
	 * @Method Name : getDrawOrderCheck
	 * @Date : 2019. 6. 25.
	 * @Author : ?????????
	 * @param evEventJoinMember
	 * @return
	 */
	public Map<String, Object> getDrawOrderInfo(EvEventJoinMember evEventJoinMember) throws Exception {
		Map<String, Object> map = new HashMap<>();

		boolean drawOrderCheck = false;
		int checkCount = evEventJoinMemberDao.selectDrawOrderCheckCount(evEventJoinMember);
		if (checkCount > 0) {
			drawOrderCheck = true;

			EvEvent evEvent = new EvEvent();
			evEvent.setEventNo(evEventJoinMember.getEventNo());
			map.put("event", evEventDao.selectEvent(evEvent));
			map.put("eventJoinWinMember", evEventJoinMemberDao.selectEventJoinWinMemberDetail(evEventJoinMember));
		}

		map.put("drawOrderCheck", drawOrderCheck);
		return map;
	}

	/**
	 * @Desc : ????????? ??????????????? ?????? (eventNo, memberNo, prdtNo)
	 * @Method Name : getDrawParticipationCheck
	 * @Date : 2019. 7. 10.
	 * @Author : ?????????
	 * @param evEventJoinMember
	 * @return
	 */

	public String getDrawParticipationCheck(EvEventJoinMember evEventJoinMember) throws Exception {
		String exceptionType = "";

		String eventNo = evEventJoinMember.getEventNo();
		String memberNo = evEventJoinMember.getMemberNo();

		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = mbMemberDao.selectByPrimaryKey(mbMember);

		EvEventTargetGrade evEventTargetGrade = new EvEventTargetGrade();
		evEventTargetGrade.setEventNo(eventNo);
		evEventTargetGrade.setMemberTypeCode(mbMember.getMemberTypeCode());
		evEventTargetGrade.setMbshpGradeCode(mbMember.getMbshpGradeCode());
		evEventTargetGrade.setEmpYn(mbMember.getEmpYn());
		evEventTargetGrade = evEventTargetGradeDao.selectEventTargetGrade(evEventTargetGrade);

		if (evEventTargetGrade != null) {
			evEventJoinMember.setLimitType(evEventTargetGrade.getLimitType());
			if (!UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
					CommonCode.JOIN_LIMIT_TYPE_CODE_UNLIMITED)) {
				int joinCount = 0;
				joinCount = evEventJoinMemberDao.selectEventJoinMemberCount(evEventJoinMember);

				if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_DAY)) { // 1??? 1???
					if (joinCount >= 1)
						exceptionType = "oncePerDay";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_ONCE_PER_ID)) { // ID ??? 1???
					if (joinCount >= 1)
						exceptionType = "oncePerId";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_DAY)) { // 1??? 2???
					if (joinCount >= 2)
						exceptionType = "twicePerDay";
				} else if (UtilsText.equals(evEventTargetGrade.getJoinLimitTypeCode(),
						CommonCode.JOIN_LIMIT_TYPE_CODE_TWICE_PER_ID)) { // ID ??? 2???
					if (joinCount >= 2)
						exceptionType = "twicePerId";
				}
			}
		} else {
			exceptionType = "noneTarget";
		}

		return exceptionType;
	}

	/**
	 * Main Upcoming ?????? ????????? ??????
	 * 
	 * @Desc :
	 * @Method Name : getMainDrawTargetProductList
	 * @Date : 2019. 7. 10.
	 * @Author : SANTA
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMainDrawTargetProductList(Parameter<EvEventTargetProduct> parameter)
			throws Exception {
		Map<String, Object> map = new HashMap<>();

		EvEventTargetProduct evEventTargetProduct = parameter.get();

		PageableProduct<EvEventTargetProduct, PdProductWrapper> pageable = new PageableProduct<EvEventTargetProduct, PdProductWrapper>(
				parameter);

		pageable.setCondition(evEventTargetProduct.getSiteNo(), evEventTargetProduct.getChnnlNo());
		pageable.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_NEWEST, null, null);

		pageable.setConditionLaunchProduct(PageableProduct.CONDITION_LAUNCH_PRODUCT_UPCOMMING);
		pageable.setRowsPerPage(3);
		pageable.setPageNum(parameter.get().getPageNum());
		Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageable);

		// upcoming ????????? ????????? ?????? launched ???????????? ????????? ??????
		if (productList.getContent().size() < 3) {
			pageable.setConditionLaunchProduct(PageableProduct.CONDITION_LAUNCH_PRODUCT_LAUNCHED);
			pageable.setRowsPerPage(3 - productList.getContent().size());

			Page<PdProductWrapper> launchedProductList = this.productService.getDisplayProductList(pageable);

			productList.getContent().addAll(launchedProductList.getContent());
		}

		map.put("productList", productList);

		return map;
	}

}
