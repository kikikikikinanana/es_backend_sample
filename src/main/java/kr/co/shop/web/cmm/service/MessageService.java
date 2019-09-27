package kr.co.shop.web.cmm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.web.cmm.model.master.CmMessageSendHistory;
import kr.co.shop.web.cmm.model.master.CmMessageTemplate;
import kr.co.shop.web.cmm.vo.MessageVO;
import kr.co.shop.web.member.repository.master.MbMemberDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	MessageTemplateService msgTemplateService;

	@Autowired
	MbMemberDao mbMemberDao;

	@Autowired
	MessageService messageService;

	/**
	 * @Desc : 메세지 발송
	 * @Method Name : setSendMessageProcess
	 * @Date : 2019. 3. 29.
	 * @Author : choi
	 * @param messageVO
	 * @throws Exception
	 */
	public void setSendMessageProcess(MessageVO messageVO) throws Exception {
		this.isMessageValidate(messageVO); // validation

		if (UtilsText.isNotBlank(messageVO.getMesgId())) {
			processTemplate(messageVO);
		}
		this.setSendMessage(messageVO, messageVO.isReal());
	}

	/**
	 * @Desc : 메세지 발송 NoTrx
	 * @Method Name : setSendMessageProcess
	 * @Date : 2019. 3. 29.
	 * @Author : choi
	 * @param messageVO
	 * @throws Exception
	 */
	public void setSendMessageProcessNoTrx(MessageVO messageVO) throws Exception {
		this.isMessageValidate(messageVO); // validation

		if (UtilsText.isNotBlank(messageVO.getMesgId())) {
			processTemplate(messageVO);
		}
		this.setSendMessage(messageVO, messageVO.isReal());
	}

	/**
	 * @Desc : 메세지 보내기 필수 항목의 validation 체크
	 * @Method Name : isMessageValidate
	 * @Date : 2019. 4. 24.
	 * @Author : choi
	 * @param messageVO
	 * @throws Exception
	 */
	public void isMessageValidate(MessageVO messageVO) throws Exception {

		if (messageVO.getSndrName() == null) {
			throw new Exception("발송자명이 없습니다.");
		} else if (messageVO.getSendTelNoText() == null) {
			throw new Exception("발송자 전화번호가 없습니다.");
		} else if (messageVO.getRcvrName() == null) {
			throw new Exception("수신자명이 없습니다.");
		} else if (messageVO.getRecvTelNoText() == null) {
			throw new Exception("수신자 전화번호가 없습니다.");
		} else if (messageVO.getMesgContText() == null && messageVO.getMesgId() == null) {
			throw new Exception("MESG_ID 또는 내용을 입력하세요");
		}
	}

	/**
	 * @Desc : 메세지 발송 관련 테이블에 등록
	 * @Method Name : setSendMessage
	 * @Date : 2019. 3. 29.
	 * @Author : choi
	 * @param messageVO
	 * @param isReal
	 * @throws Exception
	 */
	private void setSendMessage(MessageVO messageVO, boolean isReal) throws Exception {
		CmMessageSendHistory param = new CmMessageSendHistory(); // 메세기 로그 클래스
		String sendTypeCode = messageVO.getSendTypeCode();
		String mesgContText = messageVO.getMesgContText();
		String failedMsg = messageVO.getFailedMsg();
		String failedSubject = messageVO.getFailedSubject();

		if (messageVO.getSiteNo() == null) {
			messageVO.setSiteNo("10000"); // 사이트[10000:통합몰[A-RT], 100001:OTS]
		}

		if (sendTypeCode == null) { // 기본 kko로 세팅
			messageVO.setSendTypeCode("10003"); // SMS:10000,LMS:10001,MMS:10002,KKO:10003
			sendTypeCode = messageVO.getSendTypeCode();
		}

		if (failedMsg == null) {
			messageVO.setFailedMsg(mesgContText);
		}

		if (StringUtils.length(messageVO.getFailedMsg()) > 80) {
			messageVO.setFailedType("LMS");
			if (failedSubject == null) {
				messageVO.setFailedSubject("[A-RT]에서 보내드립니다.");
			}
		} else {
			messageVO.setFailedType("SMS");
		}
		String reSendTypeCode = ("SMS".equals(messageVO.getFailedType()) ? "10000" : "10001");

		String OrderPrdtSeq = messageVO.getOrderPrdtSeq();
		short OrderPrdtSeqShort = (OrderPrdtSeq == null) ? 0 : Short.valueOf(OrderPrdtSeq);

		param.setSiteNo(messageVO.getSiteNo());
		param.setSendTelNoText(messageVO.getSendTelNoText()); // 발송전화번호
		param.setSndrName(messageVO.getSndrName()); // 발송자명
		param.setRecvTelNoText(messageVO.getRecvTelNoText()); // 수신자 전화번호
		param.setRcvrName(messageVO.getRcvrName()); // 수신자명
		param.setMesgContText(mesgContText); // 메세지 내용
		param.setSendYn("N"); // 송신여부[기본N]
		param.setOrderNo(messageVO.getOrderNo()); // 주문번호
		param.setOrderPrdtSeq(OrderPrdtSeqShort); // 주문상품순번
		param.setCnslScriptSeq(messageVO.getCnslScriptSeq()); // 상담스크립트순번
		param.setMemberNo(messageVO.getMemberNo()); // 회원번호
		param.setMesgId(messageVO.getMesgId()); // 메세지ID
		param.setAdminSendYn(messageVO.getAdminSendYn()); // 관리자 발송 Y/N
		param.setSendTypeCode(messageVO.getSendTypeCode()); // 발송 타입
		param.setResendMesgContText(messageVO.getFailedMsg()); // 재전송 메세지
		param.setResendTypeCode(reSendTypeCode); // 재전송 타입

		// 즉시 발송 해아하는 메세지(isReal true)라면 관련 배치를 실행시켜서 바로 메세지가 발송되도록 한다.
		int resultCnt = 0;
//		System.out.println("isReal >>>>>" + isReal + "\nsendTypeCode >>>>>" + sendTypeCode);

		if (isReal) {
			try {
				// sendTypeCode에 따라서 테이블 INSERT가 달라진다.
				if ("10000".equals(sendTypeCode)) {
					resultCnt = msgTemplateService.setSendRealTimeSmsNoTrx(messageVO);
				} else if ("10001".equals(sendTypeCode)) {
					resultCnt = msgTemplateService.setSendRealTimeLmsNoTrx(messageVO);
				} else if ("10003".equals(sendTypeCode)) {
					resultCnt = msgTemplateService.setSendRealTimeKkoNoTrx(messageVO);
				}

				if (resultCnt > 0) {
					param.setSendYn("Y");
				}
			} catch (Exception e) {
				log.debug("DB 링크 insert error {}");
				param.setSendYn("N");
			}
		}
		msgTemplateService.setMessageSendHistory(param); // 메세지 로그 테이블 insert
	}

	/**
	 * @Desc : 템블릿 코드에 해당하는 메일 템플릿을 조회한다.
	 * @Method Name : getMessageTemplateByEmailId
	 * @Date : 2019. 3. 29.
	 * @Author : choi
	 * @param emailId
	 * @return
	 * @throws Exception
	 */
	private CmMessageTemplate getMessageTemplateByMesgId(String mesgId) throws Exception {
		return msgTemplateService.getMessageTemplateByMesgId(mesgId);
	}

	/**
	 * @Desc : 메일 템플릿을 조회한다.
	 * @Method Name : getMessageTemplate
	 * @Date : 2019. 7. 4.
	 * @Author : 유성민
	 * @param messageVO
	 * @return
	 */
	private CmMessageTemplate getMessageTemplate(MessageVO messageVO) throws Exception {
		return msgTemplateService.getMessageTemplate(messageVO);
	}

	/**
	 * @Desc : 템플릿에 따른 변수에 값을 세팅해서 반환해준다.
	 * @Method Name : processTemplate
	 * @Date : 2019. 3. 29.
	 * @Author : choi
	 * @param messageVO
	 * @throws Exception
	 */
	private void processTemplate(MessageVO messageVO) throws Exception {
		CmMessageTemplate cmMessageTemplate = getMessageTemplateByMesgId(messageVO.getMesgId());
//		CmMessageTemplate cmMessageTemplate = getMessageTemplate(messageVO);
		Template template = new Template(messageVO.getMesgId(), cmMessageTemplate.getMesgContText(), freemarkerConfig);
		String result = FreeMarkerTemplateUtils.processTemplateIntoString(template,
				messageVO.getMessageTemplateModel());
		log.debug("freemarker template result - {}", result);
		messageVO.setMesgContText(result);

		template = new Template(messageVO.getMesgId(), cmMessageTemplate.getFailMesgContText(), freemarkerConfig);
		result = FreeMarkerTemplateUtils.processTemplateIntoString(template, messageVO.getMessageTemplateModel());
		log.debug("freemarker template result - {}", result);

		messageVO.setFailedMsg(result);
	}

}
