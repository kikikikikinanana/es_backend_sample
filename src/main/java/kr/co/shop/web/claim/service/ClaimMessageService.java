package kr.co.shop.web.claim.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.util.UtilsArray;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.claim.model.master.OcClaim;
import kr.co.shop.web.claim.model.master.OcClaimPayment;
import kr.co.shop.web.claim.model.master.OcClaimProduct;
import kr.co.shop.web.cmm.service.MailService;
import kr.co.shop.web.cmm.service.MessageService;
import kr.co.shop.web.cmm.vo.MailTemplateVO;
import kr.co.shop.web.cmm.vo.MessageVO;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.repository.master.OcOrderDao;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 클레임 메세지발송 서비스
 * @FileName : ClaimMessageService.java
 * @Project : shop.backend
 * @Date : 2019. 7. 10.
 * @Author : 이강수
 */
@Slf4j
@Service
public class ClaimMessageService {

	@Autowired
	private OcOrderDao ocOrderDao;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private MailService mailService;

	@Autowired
	private ClaimService claimService;

	/**
	 * @Desc : 메세지발송 - 주문 취소
	 * @Method Name : orderAllCancel
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void orderAllCancel(OcClaim ocClaim) throws Exception {

		log.debug("=========================== orderAllCancel message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 취소/반품 클레임 - 환불정보 - 취소금액영역 (주문금액/환불배송비)
		OcClaim cancelAmtModel = (OcClaim) claimMap.get("cancelAmtModel");
		// 클레임 환불 영역 정보
		List<OcClaimPayment> refundPymntAmtList = (List<OcClaimPayment>) claimMap.get("refundPymntAmtList");
		// 취소/교환/반품 클레임 - 환수정보(추가결제금액)
		List<OcClaimPayment> redempPymntAmtList = (List<OcClaimPayment>) claimMap.get("redempPymntAmtList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		int intCancelAmt = 0;
		String pymntMeansName = "";
		String cancelAmt = "";

		if (!UtilsArray.isEmpty(refundPymntAmtList)) {

			for (int i = 0; i < refundPymntAmtList.size(); i++) {

				/** set pymntMeansName */
				if (i < refundPymntAmtList.size() - 1) {
					pymntMeansName += refundPymntAmtList.get(i).getPymntMeansCodeName() + ", ";
				} else {
					pymntMeansName += refundPymntAmtList.get(i).getPymntMeansCodeName();
				}

				/** set cancelAmt */
				intCancelAmt += refundPymntAmtList.get(i).getPymntAmt();
			}

			// String에 ,까지 찍고 '원' 붙인 취소금액
			cancelAmt = UtilsText.concat(formatter.format(intCancelAmt), " 원");
		}

		/**
		 * SMS알림톡 전송 & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${취소상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);
		// ${취소 금액}
		map.put("cancelAmt", cancelAmt);
		mailTempMap.put("cancelAmt",
				formatter.format(cancelAmtModel.getSumOrderAmt() + cancelAmtModel.getSumDlvyAmt()));
		// ${결제방식명} - 알림톡/SMS
		map.put("pymntMeansName", pymntMeansName);
		// ${추가 배송비} - mail
		mailTempMap.put("totalRedempAmt", formatter.format(detail.getTotalRedempAmt()));
		// ${취소 리스트} - mail
		mailTempMap.put("refundPymntAmtList", refundPymntAmtList);
		// ${총 환불금액} - mail
		mailTempMap.put("totalRfndAmt", formatter.format(detail.getTotalRfndAmt()));

		// ${랜딩 URL}
		String landingUrl = "";

		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-03018-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-01002-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://dev.a-rt.com", "/mypage/cancel-claim-detail?clmNo=",
					detail.getClmNo());

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-03018-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-01002-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://dev.a-rt.com", "/mypage/cancel-claim-detail?clmNo=",
					detail.getClmNo());
		}

		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);
		// 클레임날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");

		map.put("landingUrl", landingUrl);
		mailTempMap.put("landingUrl", landingUrl);

		// 알림톡/SMS 발송
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);

	}

	/**
	 * @Desc : 메세지발송 - 주문 부분 취소
	 * @Method Name : orderPartCancel
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void orderPartCancel(OcClaim ocClaim) throws Exception {

		log.debug("=========================== orderPartCancel message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 취소/반품 클레임 - 환불정보 - 취소금액영역 (주문금액/환불배송비)
		OcClaim cancelAmtModel = (OcClaim) claimMap.get("cancelAmtModel");
		// 클레임 환불 영역 정보
		List<OcClaimPayment> refundPymntAmtList = (List<OcClaimPayment>) claimMap.get("refundPymntAmtList");
		// 취소/교환/반품 클레임 - 환수정보(추가결제금액)
		List<OcClaimPayment> redempPymntAmtList = (List<OcClaimPayment>) claimMap.get("redempPymntAmtList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		int intCancelAmt = 0;
		String pymntMeansName = "";
		String cancelAmt = "";

		if (!UtilsArray.isEmpty(refundPymntAmtList)) {

			for (int i = 0; i < refundPymntAmtList.size(); i++) {

				/** set pymntMeansName */
				if (i < refundPymntAmtList.size() - 1) {
					pymntMeansName += refundPymntAmtList.get(i).getPymntMeansCodeName() + ", ";
				} else {
					pymntMeansName += refundPymntAmtList.get(i).getPymntMeansCodeName();
				}

				/** set cancelAmt */
				intCancelAmt += refundPymntAmtList.get(i).getPymntAmt();
			}

			// String에 ,까지 찍고 '원' 붙인 취소금액
			cancelAmt = UtilsText.concat(formatter.format(intCancelAmt), " 원");
		}

		/**
		 * SMS알림톡 전송 & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${취소상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);
		// ${취소 예정 금액}
		map.put("todoCancelAmt", cancelAmt);
		mailTempMap.put("cancelAmt",
				formatter.format(cancelAmtModel.getSumOrderAmt() + cancelAmtModel.getSumDlvyAmt()));
		// ${결제방식명} - 알림톡/SMS
		map.put("pymntMeansName", pymntMeansName);
		// ${추가 배송비} - mail
		mailTempMap.put("totalRedempAmt", formatter.format(detail.getTotalRedempAmt()));
		// ${취소 리스트} - mail
		mailTempMap.put("refundPymntAmtList", refundPymntAmtList);
		// ${총 환불금액} - mail
		mailTempMap.put("totalRfndAmt", formatter.format(detail.getTotalRfndAmt()));

		// ${랜딩 URL}
		String landingUrl = "";

		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-03017-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-01002-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://dev.a-rt.com", "/mypage/cancel-claim-detail?clmNo=",
					detail.getClmNo());

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-03017-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-01002-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://dev.a-rt.com", "/mypage/cancel-claim-detail?clmNo=",
					detail.getClmNo());
		}
		// mailTempVO.setMailTemplateId("EC-01002-1");
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);
		// 클레임날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");

		map.put("landingUrl", landingUrl);
		mailTempMap.put("landingUrl", landingUrl);

		// 알림톡/SMS 발송
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 메세지발송 - 교환 접수(자사, 입점 상품 공통) - 고객접수
	 * @Method Name : exchangeClaimAcceptByClient
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void exchangeClaimAcceptByClient(OcClaim ocClaim) throws Exception {

		log.debug("=========================== exchangeClaimAcceptByClient message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 교환/반품 배송비 결제 정보
		List<OcClaimPayment> dlvyAmtPymntList = (List<OcClaimPayment>) claimMap.get("dlvyAmtPymntList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		/**
		 * SMS알림톡 전송 & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// 접수번호
		mailTempMap.put("clmNo", detail.getClmNo());
		// 클레임날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");
		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());
		// 수령지
		mailTempMap.put("rcvrPostCodeText", detail.getRcvrPostCodeText());
		mailTempMap.put("rcvrPostAddrText", detail.getRcvrPostAddrText());
		mailTempMap.put("rcvrDtlAddrText", detail.getRcvrDtlAddrText());

		// 추가배송비결제타입
		mailTempMap.put("addDlvyAmtPymntType", detail.getAddDlvyAmtPymntType());

		if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
			// 선결제
			if (!UtilsArray.isEmpty(dlvyAmtPymntList)) {
				// ${교환배송비 결제방식명}
				map.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				mailTempMap.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				// ${교환배송비 결제금액}
				map.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));
				mailTempMap.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));

				OcClaimPayment dlvyAmtPymnt = dlvyAmtPymntList.get(0);

				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
				dlvyAmtPymnt.setStrPymntDtm(dateFormatter.format(dlvyAmtPymnt.getPymntDtm()));

				// ${반품배송비 모델}
				mailTempMap.put("dlvyAmtPymnt", dlvyAmtPymnt);
			}

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
			// 무료배송쿠폰
			// ${반품배송비 결제방식명}
			map.put("dlvyPymntMeansName", "무료배송쿠폰");
			mailTempMap.put("dlvyPymntMeansName", "무료배송쿠폰");
			// ${반품배송비 결제금액}
			map.put("dlvyPymntAmt", "0 원");
			mailTempMap.put("dlvyPymntAmt", "0 원");
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04001-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03001-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/exchange-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04001-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03001-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/exchange-claim-detail?clmNo=", detail.getClmNo());// ${랜딩
																														// URL}
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03001-1");
		/** 교환 메세지는 상품단위 */
		if (!UtilsArray.isEmpty(prdtList)) {
			for (OcClaimProduct ocClaimProduct : prdtList) {
				// ${상품명}
				map.put("prdtName", ocClaimProduct.getPrdtName());

				// ${교환옵션} - ex) ${교환옵션} or ${기존 옵션 -> 변경 옵션}
				if (ocClaimProduct.getChangePrdtOptnNo() != null) {
					if (ocClaimProduct.getPrdtOptnNo().equals(ocClaimProduct.getChangePrdtOptnNo())) {
						map.put("optnName", ocClaimProduct.getOptnName());
					} else {
						map.put("optnName", ocClaimProduct.getOptnName() + " -> " + ocClaimProduct.getChangeOptnName());
					}
				} else {
					map.put("optnName", ocClaimProduct.getOptnName());
				}

				// 알림톡 등록
				messageVO.setMessageTemplateModel(map);
				messageService.setSendMessageProcess(messageVO);
			}
		}

		/** 교환 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ${교환상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);

	}

	/**
	 * @Desc : 교환 접수 완료(자사, 입점 상품 공통) - 수거지시
	 * @Method Name : exchangeClaimAcceptComplete
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void exchangeClaimAcceptComplete(OcClaim ocClaim) throws Exception {

		log.debug("=========================== exchangeClaimAcceptComplete message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 교환/반품 배송비 결제 정보
		List<OcClaimPayment> dlvyAmtPymntList = (List<OcClaimPayment>) claimMap.get("dlvyAmtPymntList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		/**
		 * SMS알림톡 전송 set & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// 접수번호
		mailTempMap.put("clmNo", detail.getClmNo());
		// 클레임 신청 날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");
		// 클레임 접수완료 날짜
		if (detail.getStrModDtm() == null) {
			mailTempMap.put("modDtm", "");
		} else {
			mailTempMap.put("modDtm", detail.getStrModDtm().substring(0, 4) + "년 "
					+ detail.getStrModDtm().substring(5, 7) + "월 " + detail.getStrModDtm().substring(8, 10) + "일");
		}

		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());
		// 수령지
		mailTempMap.put("rcvrPostCodeText", detail.getRcvrPostCodeText());
		mailTempMap.put("rcvrPostAddrText", detail.getRcvrPostAddrText());
		mailTempMap.put("rcvrDtlAddrText", detail.getRcvrDtlAddrText());

		// 추가배송비결제타입
		mailTempMap.put("addDlvyAmtPymntType", detail.getAddDlvyAmtPymntType());

		if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
			// 선결제
			if (!UtilsArray.isEmpty(dlvyAmtPymntList)) {
				// ${교환배송비 결제방식명}
				map.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				mailTempMap.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				// ${교환배송비 결제금액}
				map.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));
				mailTempMap.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));

				if (!detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
					// ${입금정보} - 가상계좌입금결제 시 ex) ${*입금계좌정보 : #{은행명} #{계좌번호} #{예금주}}
					map.put("depositInfo", "");
				} else {
					// ${입금정보} - 가상계좌입금결제 시 ex) ${*입금계좌정보 : #{은행명} #{계좌번호} #{예금주}}
					map.put("depositInfo",
							"*입금계좌정보 : " + dlvyAmtPymntList.get(0).getBankCodeName() + " "
									+ dlvyAmtPymntList.get(0).getAcntNoText() + " "
									+ dlvyAmtPymntList.get(0).getAcntHldrName());
				}

				OcClaimPayment dlvyAmtPymnt = dlvyAmtPymntList.get(0);

				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
				dlvyAmtPymnt.setStrPymntDtm(dateFormatter.format(dlvyAmtPymnt.getPymntDtm()));

				// ${반품배송비 모델}
				mailTempMap.put("dlvyAmtPymnt", dlvyAmtPymnt);
			}

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
			// 무료배송쿠폰
			// ${반품배송비 결제방식명}
			map.put("dlvyPymntMeansName", "무료배송쿠폰");
			mailTempMap.put("dlvyPymntMeansName", "무료배송쿠폰");
			// ${반품배송비 결제금액}
			map.put("dlvyPymntAmt", "0 원");
			mailTempMap.put("dlvyPymntAmt", "0 원");
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04003-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03007-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/exchange-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04003-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03007-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/exchange-claim-detail?clmNo=", detail.getClmNo());// ${랜딩
																														// URL}
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03007-1");
		/** 교환 메세지는 상품단위 */
		if (!UtilsArray.isEmpty(prdtList)) {
			for (OcClaimProduct ocClaimProduct : prdtList) {
				// ${상품명}
				map.put("prdtName", ocClaimProduct.getPrdtName());

				// ${교환옵션} - ex) ${교환옵션} or ${기존 옵션 -> 변경 옵션}
				if (ocClaimProduct.getChangePrdtOptnNo() != null) {
					if (ocClaimProduct.getPrdtOptnNo().equals(ocClaimProduct.getChangePrdtOptnNo())) {
						map.put("optnName", ocClaimProduct.getOptnName());
					} else {
						map.put("optnName", ocClaimProduct.getOptnName() + " -> " + ocClaimProduct.getChangeOptnName());
					}
				} else {
					map.put("optnName", ocClaimProduct.getOptnName());
				}

				// 알림톡 등록
				messageVO.setMessageTemplateModel(map);
				messageService.setSendMessageProcess(messageVO);
			}
		}

		/** 교환 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ${교환상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 교환 상품 발송완료
	 * @Method Name : exchangeDeliveryComplete
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void exchangeDeliveryComplete(OcClaim ocClaim) throws Exception {

		log.debug("=========================== exchangeDeliveryComplete message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");

		/**
		 * SMS알림톡 전송 set & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// 접수번호
		mailTempMap.put("clmNo", detail.getClmNo());
		// 클레임 신청 날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");
		// 클레임 접수완료 날짜
		if (detail.getStrModDtm() == null) {
			mailTempMap.put("modDtm", "");
		} else {
			mailTempMap.put("modDtm", detail.getStrModDtm().substring(0, 4) + "년 "
					+ detail.getStrModDtm().substring(5, 7) + "월 " + detail.getStrModDtm().substring(8, 10) + "일");
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04004-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03004-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/exchange-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04004-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03004-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/exchange-claim-detail?clmNo=", detail.getClmNo());// ${랜딩
																														// URL}
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03004-1");
		/** 교환 메세지는 상품단위 */
		if (!UtilsArray.isEmpty(prdtList)) {
			for (OcClaimProduct ocClaimProduct : prdtList) {
				// ${상품명}
				map.put("prdtName", ocClaimProduct.getPrdtName());
				// ${브랜드명}
				map.put("brandName", ocClaimProduct.getBrandName());

				// ${교환옵션} - ex) ${교환옵션} or ${기존 옵션 -> 변경 옵션}
				if (ocClaimProduct.getChangePrdtOptnNo() != null) {
					if (ocClaimProduct.getPrdtOptnNo().equals(ocClaimProduct.getChangePrdtOptnNo())) {
						map.put("optnName", ocClaimProduct.getOptnName());
					} else {
						map.put("optnName", ocClaimProduct.getOptnName() + " -> " + ocClaimProduct.getChangeOptnName());
					}
				} else {
					map.put("optnName", ocClaimProduct.getOptnName());
				}

				// TODO :

				// ${택배사명}
				map.put("logisVndrName", "");
				// ${운송장번호}
				map.put("waybilNoText", "");
				// ${택배사 랜딩단축 URL}
				map.put("logisVndrLandingUrl", "");

				// 알림톡 등록
				messageVO.setMessageTemplateModel(map);
				messageService.setSendMessageProcess(messageVO);
			}
		}

		/** 교환 메일은 ? */
		// ${택배사명}
		mailTempMap.put("logisVndrName", "");
		// ${운송장번호}
		mailTempMap.put("waybilNoText", "");
		// ${택배사 랜딩단축 URL}
		mailTempMap.put("logisVndrLandingUrl", "");

		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ${교환상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 반품 접수(자사, 입점 상품 공통) - 고객접수
	 * @Method Name : returnClaimAcceptByClient
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaimMessageVO
	 * @throws Exception
	 */
	public void returnClaimAcceptByClient(OcClaim ocClaim) throws Exception {

		log.debug("=========================== returnClaimAcceptByClient message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 교환/반품 배송비 결제 정보
		List<OcClaimPayment> dlvyAmtPymntList = (List<OcClaimPayment>) claimMap.get("dlvyAmtPymntList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		/**
		 * SMS알림톡 전송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// 접수번호
		mailTempMap.put("clmNo", detail.getClmNo());
		// 클레임 신청 날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");
		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());

		// 추가배송비결제타입
		mailTempMap.put("addDlvyAmtPymntType", detail.getAddDlvyAmtPymntType());

		if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
			// 선결제
			if (!UtilsArray.isEmpty(dlvyAmtPymntList)) {
				// ${반품배송비 결제방식명}
				map.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				mailTempMap.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				// ${반품배송비 결제금액}
				map.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));
				mailTempMap.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));

				OcClaimPayment dlvyAmtPymnt = dlvyAmtPymntList.get(0);

				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
				dlvyAmtPymnt.setStrPymntDtm(dateFormatter.format(dlvyAmtPymnt.getPymntDtm()));

				// ${반품배송비 모델}
				mailTempMap.put("dlvyAmtPymnt", dlvyAmtPymnt);
			}

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_REFUND_DEDUCTION)) {
			// 환불금액차감
			// ${반품배송비 결제방식명}
			map.put("dlvyPymntMeansName", "환불금액차감");
			mailTempMap.put("dlvyPymntMeansName", "환불금액차감");
			// ${반품배송비 결제금액}
			map.put("dlvyPymntAmt", UtilsText.concat(formatter.format(detail.getAddDlvyAmt()), " 원"));
			mailTempMap.put("dlvyPymntAmt", UtilsText.concat(formatter.format(detail.getAddDlvyAmt()), " 원"));

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
			// 무료배송쿠폰
			// ${반품배송비 결제방식명}
			map.put("dlvyPymntMeansName", "무료배송쿠폰");
			mailTempMap.put("dlvyPymntMeansName", "무료배송쿠폰");
			// ${반품배송비 결제금액}
			map.put("dlvyPymntAmt", "0 원");
			mailTempMap.put("dlvyPymntAmt", "0 원");
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04005-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03002-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04005-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03002-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());// ${랜딩
																														// URL}
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03002-1");
		String prdtName = "";
		if (!UtilsArray.isEmpty(prdtList)) {
			for (int i = 0; i < prdtList.size(); i++) {
				/** set prdtName */
				if (i < prdtList.size() - 1) {
					prdtName += prdtList.get(i).getPrdtName() + ", ";
				} else {
					prdtName += prdtList.get(i).getPrdtName();
				}
			}
		}
		map.put("prdtName", prdtName);

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);
		// ${교환상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 반품 접수 완료(자사, 입점 상품 공통) - 수거지시
	 * @Method Name : returnClaimAcceptComplete
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void returnClaimAcceptComplete(OcClaim ocClaim) throws Exception {

		log.debug("=========================== returnClaimAcceptComplete message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 교환/반품 배송비 결제 정보
		List<OcClaimPayment> dlvyAmtPymntList = (List<OcClaimPayment>) claimMap.get("dlvyAmtPymntList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		/**
		 * SMS알림톡 전송 set & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// 접수번호
		mailTempMap.put("clmNo", detail.getClmNo());
		// 클레임 신청 날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");
		// 클레임 접수완료 날짜
		if (detail.getStrModDtm() == null) {
			mailTempMap.put("modDtm", "");
		} else {
			mailTempMap.put("modDtm", detail.getStrModDtm().substring(0, 4) + "년 "
					+ detail.getStrModDtm().substring(5, 7) + "월 " + detail.getStrModDtm().substring(8, 10) + "일");
		}

		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());

		// 추가배송비결제타입
		mailTempMap.put("addDlvyAmtPymntType", detail.getAddDlvyAmtPymntType());

		if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
			// 선결제
			if (!UtilsArray.isEmpty(dlvyAmtPymntList)) {
				// ${반품배송비 결제방식명}
				map.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				mailTempMap.put("dlvyPymntMeansName", dlvyAmtPymntList.get(0).getPymntMeansCodeName());
				// ${반품배송비 결제금액}
				map.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));
				mailTempMap.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));

				OcClaimPayment dlvyAmtPymnt = dlvyAmtPymntList.get(0);

				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
				dlvyAmtPymnt.setStrPymntDtm(dateFormatter.format(dlvyAmtPymnt.getPymntDtm()));

				// ${반품배송비 모델}
				mailTempMap.put("dlvyAmtPymnt", dlvyAmtPymnt);
			}

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_REFUND_DEDUCTION)) {
			// 환불금액차감
			// ${반품배송비 결제방식명}
			map.put("dlvyPymntMeansName", "환불금액차감");
			mailTempMap.put("dlvyPymntMeansName", "환불금액차감");
			// ${반품배송비 결제금액}
			map.put("dlvyPymntAmt", UtilsText.concat(formatter.format(detail.getAddDlvyAmt()), " 원"));
			mailTempMap.put("dlvyPymntAmt", UtilsText.concat(formatter.format(detail.getAddDlvyAmt()), " 원"));

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
			// 무료배송쿠폰
			// ${반품배송비 결제방식명}
			map.put("dlvyPymntMeansName", "무료배송쿠폰");
			mailTempMap.put("dlvyPymntMeansName", "무료배송쿠폰");
			// ${반품배송비 결제금액}
			map.put("dlvyPymntAmt", "0 원");
			mailTempMap.put("dlvyPymntAmt", "0 원");
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04007-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03008-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04007-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03008-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());// ${랜딩
																														// URL}
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03008-1");
		String prdtName = "";
		if (!UtilsArray.isEmpty(prdtList)) {
			for (int i = 0; i < prdtList.size(); i++) {
				/** set prdtName */
				if (i < prdtList.size() - 1) {
					prdtName += prdtList.get(i).getPrdtName() + ", ";
				} else {
					prdtName += prdtList.get(i).getPrdtName();
				}
			}
		}
		map.put("prdtName", prdtName);

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);
		// ${교환상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 반품처리 완료
	 * @Method Name : returnProcessComplete
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void returnProcessComplete(OcClaim ocClaim) throws Exception {

		log.debug("=========================== returnProcessComplete message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
		List<OcClaimProduct> prdtList = (List<OcClaimProduct>) claimMap.get("prdtList");
		// 클레임 환불 영역 정보
		List<OcClaimPayment> refundPymntAmtList = (List<OcClaimPayment>) claimMap.get("refundPymntAmtList");
		// 교환/반품 배송비 결제 정보
		List<OcClaimPayment> dlvyAmtPymntList = (List<OcClaimPayment>) claimMap.get("dlvyAmtPymntList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		/**
		 * SMS알림톡 전송 set & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		MailTemplateVO mailTempVO = new MailTemplateVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);
		mailTempVO.setRealTime(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			mailTempVO.setReceiverName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			mailTempVO.setReceiverEmail(ocOrder.getBuyerEmailAddrText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());
			mailTempMap.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			mailTempVO.setReceiverName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
			mailTempMap.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// 접수번호
		mailTempMap.put("clmNo", detail.getClmNo());
		// 클레임 신청 날짜
		mailTempMap.put("clmDtm", detail.getStrClmDtm().substring(0, 4) + "년 " + detail.getStrClmDtm().substring(5, 7)
				+ "월 " + detail.getStrClmDtm().substring(8, 10) + "일");
		// 클레임 접수완료 날짜
		if (detail.getStrModDtm() == null) {
			mailTempMap.put("modDtm", "");
		} else {
			mailTempMap.put("modDtm", detail.getStrModDtm().substring(0, 4) + "년 "
					+ detail.getStrModDtm().substring(5, 7) + "월 " + detail.getStrModDtm().substring(8, 10) + "일");
		}

		/**
		 * 결제금액 (주문전체결제금액)
		 */
		OcOrder orderInfo = new OcOrder();
		orderInfo.setOrderNo(detail.getOrderNo());
		orderInfo = ocOrderDao.selectOrderDetail(orderInfo);

		map.put("totalPymntAmt", UtilsText.concat(formatter.format(orderInfo.getPymntAmt()), " 원"));
		mailTempMap.put("totalPymntAmt", UtilsText.concat(formatter.format(orderInfo.getPymntAmt()), " 원"));

		/**
		 * 반품배송비
		 */
		// 추가배송비결제타입
		mailTempMap.put("addDlvyAmtPymntType", detail.getAddDlvyAmtPymntType());

		if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
			// 선결제
			if (!UtilsArray.isEmpty(dlvyAmtPymntList)) {
				// ${반품배송비 결제금액}
				map.put("returnDlvyInfo",
						UtilsText.concat("*반품 배송비 : ", dlvyAmtPymntList.get(0).getPymntMeansCodeName(), " ",
								formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));
				mailTempMap.put("dlvyPymntAmt",
						UtilsText.concat(formatter.format(dlvyAmtPymntList.get(0).getPymntAmt()), " 원"));
			}

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_REFUND_DEDUCTION)) {
			// 환불금액차감
			// ${반품배송비 결제금액}
			map.put("returnDlvyInfo",
					UtilsText.concat("*반품 배송비 : 환불금액차감 ", formatter.format(detail.getAddDlvyAmt()), " 원"));
			mailTempMap.put("dlvyPymntAmt", UtilsText.concat(formatter.format(detail.getAddDlvyAmt()), " 원"));

		} else if (detail.getAddDlvyAmtPymntType().equals(CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
			// 무료배송쿠폰
			// ${반품배송비 결제금액}
			map.put("returnDlvyInfo", "*반품 배송비 : 무료배송비쿠폰 0 원");
			mailTempMap.put("dlvyPymntAmt", "0 원");
		}

		/**
		 * 환불금액
		 */
		int intRefundAmt = 0;
		String refundAmt = "";
		if (!UtilsArray.isEmpty(refundPymntAmtList)) {
			for (int i = 0; i < refundPymntAmtList.size(); i++) {
				/** set refundAmt */
				intRefundAmt += refundPymntAmtList.get(i).getPymntAmt();
			}
			refundAmt = UtilsText.concat(formatter.format(intRefundAmt), " 원");
			map.put("refundAmt", refundAmt);
			mailTempMap.put("refundAmt", refundAmt);
		} else {
			map.put("refundAmt", "");
			mailTempMap.put("refundAmt", "");
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04008-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03005-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04008-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03005-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());// ${랜딩
																														// URL}
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}

		// mailTempVO.setMailTemplateId("EC-03005-1");
		String prdtName = "";
		if (!UtilsArray.isEmpty(prdtList)) {
			for (int i = 0; i < prdtList.size(); i++) {
				/** set prdtName */
				if (i < prdtList.size() - 1) {
					prdtName += prdtList.get(i).getPrdtName() + ", ";
				} else {
					prdtName += prdtList.get(i).getPrdtName();
				}
			}
		}
		map.put("prdtName", prdtName);

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);
		// ${교환상품 정보 리스트} - mail
		mailTempMap.put("prdtList", prdtList);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 환수 금액 발생 (메세지만)
	 * @Method Name : redempAmtOccurrence
	 * @Date : 2019. 7. 12.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public void redempAmtOccurrence(OcClaim ocClaim) throws Exception {

		log.debug("=========================== redempAmtOccurrence message start");
		Map<String, String> map = new HashMap<>();

		/** 클레임 상세조회 */
		Map<String, Object> claimMap = claimService.getClaimDetailInfo(ocClaim);
		// 클레임 마스터
		OcClaim detail = (OcClaim) claimMap.get("detail");
		// 취소/교환/반품 클레임 - 환수정보(추가결제금액) detail.getTotalRedempAmt()
		List<OcClaimPayment> redempPymntAmtList = (List<OcClaimPayment>) claimMap.get("redempPymntAmtList");

		DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);

		/**
		 * SMS알림톡 전송 set & 메일 발송 set
		 */
		MessageVO messageVO = new MessageVO();
		// 사이트
		messageVO.setSiteNo(detail.getSiteNo());
		// 발신자
		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
		// 발신번호
		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
		// 즉시발송여부
		messageVO.setReal(true);

		MbMember mbMember = new MbMember();
		MbMember memberInfo = null;

		// 회원 / 비회원 분기
		if (detail.getMemberNo().equals(Const.NON_MEMBER_NO)) {
			// 비회원인 경우
			OcOrder ocOrder = new OcOrder();
			ocOrder.setOrderNo(detail.getOrderNo());
			ocOrder = ocOrderDao.selectOrderDetail(ocOrder);
			// 수신자명
			messageVO.setRcvrName(ocOrder.getBuyerName());
			// 수신번호
			messageVO.setRecvTelNoText(ocOrder.getBuyerHdphnNoText());
			// ${수신자명}
			map.put("memberName", ocOrder.getBuyerName());

		} else {
			// 회원인 경우
			mbMember.setMemberNo(detail.getMemberNo());
			memberInfo = memberService.selectMemberInfo(mbMember);
			// 수신자명
			messageVO.setRcvrName(memberInfo.getMemberName());
			// 수신번호
			messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
			// ${수신자명}
			map.put("memberName", memberInfo.getMemberName());
		}

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${발생사유}
		map.put("ocrncRsnName", "환수 포인트 발생");

		if (redempPymntAmtList == null) {
			// ${추가결제금액}
			map.put("addPymntAmt", "0 원");
			// ${입금 가상 계좌}
			map.put("acntNoText", "");
		} else {
			if (redempPymntAmtList.size() == 0) {
				// ${추가결제금액}
				map.put("addPymntAmt", "0 원");
				// ${입금 가상 계좌}
				map.put("acntNoText", "");
			} else {
				OcClaimPayment redempPymntAmt = new OcClaimPayment();
				redempPymntAmt = redempPymntAmtList.get(0);
				// ${추가결제금액}
				if (detail.getTotalRedempAmt() == null || detail.getTotalRedempAmt() == 0) {
					map.put("addPymntAmt", "0 원");
				} else {
					map.put("addPymntAmt", UtilsText.concat(formatter.format(detail.getTotalRedempAmt()), " 원"));
				}
				// ${입금 가상 계좌}
				if (redempPymntAmt.getAcntNoText() == null || redempPymntAmt.getAcntNoText().equals("")) {
					map.put("acntNoText", "");
				} else {
					map.put("acntNoText", redempPymntAmt.getAcntNoText());
				}
			}
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04017-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04017-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/return-claim-detail?clmNo=", detail.getClmNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04017-1");

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);
	}
}
