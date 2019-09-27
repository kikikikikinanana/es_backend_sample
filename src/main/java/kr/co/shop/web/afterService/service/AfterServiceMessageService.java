package kr.co.shop.web.afterService.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.afterService.model.master.OcAsAccept;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProduct;
import kr.co.shop.web.afterService.model.master.OcAsPayment;
import kr.co.shop.web.cmm.service.MailService;
import kr.co.shop.web.cmm.service.MessageService;
import kr.co.shop.web.cmm.vo.MailTemplateVO;
import kr.co.shop.web.cmm.vo.MessageVO;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : AS 메세지 전송 서비스
 * @FileName : AfterServiceMessageService.java
 * @Project : shop.backend
 * @Date : 2019. 7. 11.
 * @Author : 이강수
 */
@Slf4j
@Service
public class AfterServiceMessageService {

	@Autowired
	private MessageService messageService;

	@Autowired
	private MailService mailService;

	@Autowired
	private AfterServiceService afterServiceService;

	@Autowired
	private MemberService memberService;

	/**
	 * @Desc : 심의 접수 완료 - BO 처리, 고객접수
	 * @Method Name : asJudgeAcceptComplete
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asJudgeAcceptComplete(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asJudgeAcceptComplete message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");
		// AS 결제정보 조회
		OcAsPayment ocAsPayment = (OcAsPayment) asMap.get("ocAsPaymentInfo");

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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		mailTempVO.setReceiverName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());
		mailTempMap.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		mailTempMap.put("asAcceptNo", detail.getAsAcceptNo());
		// 신청 날짜
		mailTempMap.put("asAcceptDtm", detail.getAsAcceptDate().substring(0, 4) + "년 "
				+ detail.getAsAcceptDate().substring(5, 7) + "월 " + detail.getAsAcceptDate().substring(8, 10) + "일");
		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());
		// 수령지
		mailTempMap.put("rcvrPostCodeText", detail.getRcvrPostCodeText());
		mailTempMap.put("rcvrPostAddrText", detail.getRcvrPostAddrText());
		mailTempMap.put("rcvrDtlAddrText", detail.getRcvrDtlAddrText());

		if (ocAsPayment == null) {
			// ${배송비 결제방식명}
			map.put("dlvyPymntMeansName", "");
			mailTempMap.put("dlvyPymntMeansName", "");
			// ${배송비 결제금액}
			map.put("dlvyPymntAmt", "");
			mailTempMap.put("dlvyPymntAmt", "");

			ocAsPayment = new OcAsPayment();
			ocAsPayment.setPymntOrganName("");
			ocAsPayment.setCardMask("");
			ocAsPayment.setInstmtTermCount((short) 0);
			ocAsPayment.setStrPymntDtm("");
			ocAsPayment.setPymntMeansCode("");
			ocAsPayment.setPymntMeansCodeName("");

			// ${배송비 모델}
			mailTempMap.put("dlvyAmtPymnt", ocAsPayment);

		} else {
			// ${배송비 결제방식명}
			map.put("dlvyPymntMeansName", ocAsPayment.getPymntMeansCodeName());
			mailTempMap.put("dlvyPymntMeansName", ocAsPayment.getPymntMeansCodeName());
			// ${배송비 결제금액}
			map.put("dlvyPymntAmt", UtilsText.concat(formatter.format(ocAsPayment.getPymntAmt()), " 원"));
			mailTempMap.put("dlvyPymntAmt", UtilsText.concat(formatter.format(ocAsPayment.getPymntAmt()), " 원"));

			ocAsPayment.setStrPymntDtm(ocAsPayment.getPymntCmlptDate());

			// ${배송비 모델}
			mailTempMap.put("dlvyAmtPymnt", ocAsPayment);
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04016-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03009-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04016-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03009-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03009-1");

		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		/** 교환 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ${상품 정보} - mail
		mailTempMap.put("prdt", prdt);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 심의 상품 수령 완료 (메세지 만)
	 * @Method Name : asJudgeReceiptComplete
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asJudgeReceiptComplete(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asJudgeReceiptComplete message start");
		Map<String, String> map = new HashMap<>();

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");

		/**
		 * SMS알림톡 전송 set
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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04018-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04018-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04018-1");
		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);
	}

	/**
	 * @Desc : 심의 불가 반송 (메세지 만)
	 * @Method Name : asJudgeImpsbltSendBack
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asJudgeImpsbltSendBack(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asJudgeImpsbltSendBack message start");
		Map<String, String> map = new HashMap<>();

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");

		/**
		 * SMS알림톡 전송 set
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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());

		// ${asGbnCodeName}
		map.put("asGbnCodeName", "심의");

		// ${주문번호}asGbnCodeName
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());

		// ${택배사명}
		map.put("logisVndrName", "");
		// ${운송장번호}
		map.put("waybilNoText", "");
		// ${택배사 랜딩 URL}
		map.put("logisVndrLandingUrl", "");

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04020-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04020-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04020-1");

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);
	}

	/**
	 * @Desc : 수선 접수 - BO 접수
	 * @Method Name : asRepairAcceptByAdmin
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asRepairAcceptByAdmin(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asRepairAcceptByAdmin message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");
		// AS 결제정보 조회
		OcAsPayment ocAsPayment = (OcAsPayment) asMap.get("ocAsPaymentInfo");

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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		mailTempVO.setReceiverName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());
		mailTempMap.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		mailTempMap.put("asAcceptNo", detail.getAsAcceptNo());
		// 신청 날짜
		mailTempMap.put("asAcceptDtm", detail.getAsAcceptDate().substring(0, 4) + "년 "
				+ detail.getAsAcceptDate().substring(5, 7) + "월 " + detail.getAsAcceptDate().substring(8, 10) + "일");
		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());
		// 수령지
		mailTempMap.put("rcvrPostCodeText", detail.getRcvrPostCodeText());
		mailTempMap.put("rcvrPostAddrText", detail.getRcvrPostAddrText());
		mailTempMap.put("rcvrDtlAddrText", detail.getRcvrDtlAddrText());

		if (ocAsPayment == null) {
			// ${배송비 결제방식명}
			mailTempMap.put("dlvyPymntMeansName", "");
			// ${배송비 결제금액}
			mailTempMap.put("dlvyPymntAmt", "");

			ocAsPayment = new OcAsPayment();
			ocAsPayment.setPymntOrganName("");
			ocAsPayment.setCardMask("");
			ocAsPayment.setInstmtTermCount((short) 0);
			ocAsPayment.setStrPymntDtm("");
			ocAsPayment.setPymntMeansCode("");
			ocAsPayment.setPymntMeansCodeName("");

			// ${배송비 모델}
			mailTempMap.put("dlvyAmtPymnt", ocAsPayment);

		} else {
			// ${배송비 결제방식명}
			mailTempMap.put("dlvyPymntMeansName", ocAsPayment.getPymntMeansCodeName());
			// ${배송비 결제금액}
			mailTempMap.put("dlvyPymntAmt", UtilsText.concat(formatter.format(ocAsPayment.getPymntAmt()), " 원"));

			ocAsPayment.setStrPymntDtm(ocAsPayment.getPymntCmlptDate());

			// ${배송비 모델}
			mailTempMap.put("dlvyAmtPymnt", ocAsPayment);
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04010-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03003-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04010-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03003-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04010-1");
		// mailTempVO.setMailTemplateId("EC-03003-1");

		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		/** 교환 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ${상품 정보} - mail
		mailTempMap.put("prdt", prdt);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 수선 접수 완료 - 수거지시
	 * @Method Name : asRepairAcceptComplete
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asRepairAcceptComplete(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asRepairAcceptComplete message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");
		// AS 결제정보 조회
		OcAsPayment ocAsPayment = (OcAsPayment) asMap.get("ocAsPaymentInfo");

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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		mailTempVO.setReceiverName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());
		mailTempMap.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		mailTempMap.put("asAcceptNo", detail.getAsAcceptNo());
		// 신청 날짜
		mailTempMap.put("asAcceptDtm", detail.getAsAcceptDate().substring(0, 4) + "년 "
				+ detail.getAsAcceptDate().substring(5, 7) + "월 " + detail.getAsAcceptDate().substring(8, 10) + "일");
		// 회수지
		mailTempMap.put("buyerPostCodeText", detail.getBuyerPostCodeText());
		mailTempMap.put("buyerPostAddrText", detail.getBuyerPostAddrText());
		mailTempMap.put("buyerDtlAddrText", detail.getBuyerDtlAddrText());
		// 수령지
		mailTempMap.put("rcvrPostCodeText", detail.getRcvrPostCodeText());
		mailTempMap.put("rcvrPostAddrText", detail.getRcvrPostAddrText());
		mailTempMap.put("rcvrDtlAddrText", detail.getRcvrDtlAddrText());

		if (ocAsPayment == null) {
			// ${배송비 결제방식명}
			map.put("dlvyPymntMeansName", "");
			mailTempMap.put("dlvyPymntMeansName", "");
			// ${배송비 결제금액}
			map.put("dlvyPymntAmt", "");
			mailTempMap.put("dlvyPymntAmt", "");

			ocAsPayment = new OcAsPayment();
			ocAsPayment.setPymntOrganName("");
			ocAsPayment.setCardMask("");
			ocAsPayment.setInstmtTermCount((short) 0);
			ocAsPayment.setStrPymntDtm("");
			ocAsPayment.setPymntMeansCode("");
			ocAsPayment.setPymntMeansCodeName("");

			// ${배송비 모델}
			mailTempMap.put("dlvyAmtPymnt", ocAsPayment);

		} else {
			// ${배송비 결제방식명}
			map.put("dlvyPymntMeansName", ocAsPayment.getPymntMeansCodeName());
			mailTempMap.put("dlvyPymntMeansName", ocAsPayment.getPymntMeansCodeName());
			// ${배송비 결제금액}
			map.put("dlvyPymntAmt", UtilsText.concat(formatter.format(ocAsPayment.getPymntAmt()), " 원"));
			mailTempMap.put("dlvyPymntAmt", UtilsText.concat(formatter.format(ocAsPayment.getPymntAmt()), " 원"));

			ocAsPayment.setStrPymntDtm(ocAsPayment.getPymntCmlptDate());

			// ${배송비 모델}
			mailTempMap.put("dlvyAmtPymnt", ocAsPayment);
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04011-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03009-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04011-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03009-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// mailTempVO.setMailTemplateId("EC-03009-1");

		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		/** 교환 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// ${상품 정보} - mail
		mailTempMap.put("prdt", prdt);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 수선비용 발생 (메세지만)
	 * @Method Name : asRepairAmtOccurrence
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asRepairAmtOccurrence(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asRepairAmtOccurrence message start");
		Map<String, String> map = new HashMap<>();

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");

		/**
		 * SMS알림톡 전송 set
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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		// ${수신자명} ocrncRsnName
		map.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());

		if (detail.getAsProcTypeCodeName() == null || detail.getAsProcTypeCodeName().equals("")) {
			// ${발생사유}
			map.put("ocrncRsnName", "");
		} else {
			// ${발생사유}
			map.put("ocrncRsnName", detail.getAsProcTypeCodeName());
		}

		if (detail.getTotalAsAmt() == null || detail.getTotalAsAmt() == 0) {
			// ${수선비용}
			map.put("repairAmt", "0 원");
		} else {
			DecimalFormat formatter = new DecimalFormat(Const.DEFAULT_NUMBER_PATTERN_COMMA_THREE_DIGIT);
			// ${수선비용}
			map.put("repairAmt", UtilsText.concat(formatter.format(detail.getTotalAsAmt()), " 원"));
		}

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04012-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04012-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04012-1");

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);
	}

	/**
	 * @Desc : 수선 회수 완료 시
	 * @Method Name : asRepairRetrievalComplete
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asRepairRetrievalComplete(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asRepairRetrievalComplete message start");
		Map<String, String> map = new HashMap<>();

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");

		/**
		 * SMS알림톡 전송 set
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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		// ${수신자명} ocrncRsnName
		map.put("memberName", memberInfo.getMemberName());

		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04013-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04013-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04013-1");

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);
	}

	/**
	 * @Desc : 수선 배송 처리
	 * @Method Name : asRepairDeliveryProc
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asRepairDeliveryProc(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asRepairDeliveryProc message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");

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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		mailTempVO.setReceiverName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());
		mailTempMap.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		mailTempMap.put("asAcceptNo", detail.getAsAcceptNo());
		// 신청 날짜
		mailTempMap.put("asAcceptDtm", detail.getAsAcceptDate().substring(0, 4) + "년 "
				+ detail.getAsAcceptDate().substring(5, 7) + "월 " + detail.getAsAcceptDate().substring(8, 10) + "일");
		// 수선완료날짜
		if (detail.getStrModDtm() == null || detail.getStrModDtm().equals("")) {
			mailTempMap.put("modDtm", "");
		} else {
			mailTempMap.put("modDtm", detail.getStrModDtm().substring(0, 4) + "년 "
					+ detail.getStrModDtm().substring(5, 7) + "월 " + detail.getStrModDtm().substring(8, 10) + "일");
		}

		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());
		// ${상품 정보} - mail
		mailTempMap.put("prdt", prdt);

		// ${택배사명}
		map.put("logisVndrName", "");
		// ${운송장번호}
		map.put("waybilNoText", "");
		// ${택배사 랜딩 URL}
		map.put("logisVndrLandingUrl", "");

		// ${배송방법명}
		mailTempMap.put("dlvyTypeCodeName", "");
		// ${택배사명}
		mailTempMap.put("logisVndrName", "");
		// ${운송장번호}
		mailTempMap.put("waybilNoText", "");

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04014-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03010-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04014-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03010-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04014-1");
		// mailTempVO.setMailTemplateId("EC-03010-1");

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		/** 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}

	/**
	 * @Desc : 수선 불가 반송
	 * @Method Name : asRepairImpsbltSendBack
	 * @Date : 2019. 7. 11.
	 * @Author : 이강수
	 * @param OcAsAccept
	 * @throws Exception
	 */
	public void asRepairImpsbltSendBack(OcAsAccept ocAsAccept) throws Exception {

		log.debug("=========================== asRepairImpsbltSendBack message start");
		Map<String, String> map = new HashMap<>();
		Map<String, Object> mailTempMap = new HashMap<>();

		// 이메일 상단 URL
		mailTempMap.put("abcUrl", "");
		mailTempMap.put("otsUrl", "");

		/** AS 상세조회 */
		Map<String, Object> asMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		// AS 마스터
		OcAsAccept detail = (OcAsAccept) asMap.get("asAcceptInfo");
		// AS 상품 리스트
		OcAsAcceptProduct prdt = (OcAsAcceptProduct) asMap.get("ocAsAcceptProductInfo");

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
		// 회원인 경우
		mbMember.setMemberNo(detail.getMemberNo());
		MbMember memberInfo = memberService.selectMemberInfo(mbMember);
		// 수신자명
		messageVO.setRcvrName(memberInfo.getMemberName());
		mailTempVO.setReceiverName(memberInfo.getMemberName());
		// 수신번호
		messageVO.setRecvTelNoText(memberInfo.getHdphnNoText());
		mailTempVO.setReceiverEmail(memberInfo.getEmailAddrText());
		// ${수신자명}
		map.put("memberName", memberInfo.getMemberName());
		mailTempMap.put("memberName", memberInfo.getMemberName());

		// ${주문번호}
		map.put("orderNo", detail.getOrderNo());
		// ${접수번호}
		map.put("asAcceptNo", detail.getAsAcceptNo());
		mailTempMap.put("asAcceptNo", detail.getAsAcceptNo());
		// 신청 날짜
		mailTempMap.put("asAcceptDtm", detail.getAsAcceptDate().substring(0, 4) + "년 "
				+ detail.getAsAcceptDate().substring(5, 7) + "월 " + detail.getAsAcceptDate().substring(8, 10) + "일");
		// 수선완료날짜
		if (detail.getStrModDtm() == null || detail.getStrModDtm().equals("")) {
			mailTempMap.put("modDtm", "");
		} else {
			mailTempMap.put("modDtm", detail.getStrModDtm().substring(0, 4) + "년 "
					+ detail.getStrModDtm().substring(5, 7) + "월 " + detail.getStrModDtm().substring(8, 10) + "일");
		}

		// ${asGbnCodeName}
		map.put("asGbnCodeName", "수선");

		// ${상품명}
		map.put("prdtName", prdt.getPrdtName());
		// ${상품 정보} - mail
		mailTempMap.put("prdt", prdt);

		// ${택배사명}
		map.put("logisVndrName", "");
		// ${운송장번호}
		map.put("waybilNoText", "");
		// ${택배사 랜딩 URL}
		map.put("logisVndrLandingUrl", "");

		// ${배송방법명}
		mailTempMap.put("dlvyTypeCodeName", "");
		// ${택배사명}
		mailTempMap.put("logisVndrName", "");
		// ${운송장번호}
		mailTempMap.put("waybilNoText", "");

		// ${랜딩 URL}
		String landingUrl = "";
		if (detail.getSiteNo().equals("10000")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04020-0");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03006-0");
			// 사이트 : 통합몰 (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);

		} else if (detail.getSiteNo().equals("10001")) {
			// 메세지 번호
			messageVO.setMesgId("MC-04020-1");
			// 이메일 번호
			mailTempVO.setMailTemplateId("EC-03006-1");
			// 사이트 : OTS (임시)
			landingUrl = UtilsText.concat("https://art.com", "/mypage/afterservice-request-detail?asAcceptNo=",
					detail.getAsAcceptNo());
			// ${랜딩 URL}
			map.put("landingUrl", landingUrl);
			mailTempMap.put("landingUrl", landingUrl);
		}
		// messageVO.setMesgId("MC-04020-1");
		// mailTempVO.setMailTemplateId("EC-03006-1");

		// 알림톡 등록
		messageVO.setMessageTemplateModel(map);
		messageService.setSendMessageProcess(messageVO);

		/** 메일은 ? */
		// 이미지경로
		mailTempMap.put("imgPath", Const.URL_IMG_UPLOAD_PATH);

		// 메일 발송
		mailTempVO.setMailTemplateModel(mailTempMap);
		mailService.sendMail(mailTempVO);
	}
}
