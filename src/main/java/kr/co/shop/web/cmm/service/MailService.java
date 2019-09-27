package kr.co.shop.web.cmm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import kr.co.shop.common.exception.EmailException;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsMail;
import kr.co.shop.web.cmm.model.master.CmEmailSendHistory;
import kr.co.shop.web.cmm.model.master.CmEmailTemplate;
import kr.co.shop.web.cmm.vo.MailTemplateVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailService {

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	EmailTemplateService emailTemplateService;

	@Autowired
	CmLogHistoryService cmLogHistoryService;

	public void sendMail(MailTemplateVO mailTemplateVo) throws Exception {
		CmEmailSendHistory cmEmailSendHistory = getCmEmailSendHistory(mailTemplateVo);

		if (mailTemplateVo.isRealTime()) {
			try {
				UtilsMail.send(cmEmailSendHistory.getSndrEmailAddrText(), cmEmailSendHistory.getSndrName(),
						cmEmailSendHistory.getRcvrEmailAddrText(), cmEmailSendHistory.getRcvrName(),
						cmEmailSendHistory.getEmailTitleText(), cmEmailSendHistory.getEmailContText());
				cmEmailSendHistory.setSendYn(Const.BOOLEAN_TRUE);
			} catch (EmailException ee) {
				ee.printStackTrace();
				throw new Exception("실시간 이메일 발송에 실패하였습니다.");
			}
		}

		cmLogHistoryService.insertCmEmailSendHistory(cmEmailSendHistory);
	}

	private CmEmailSendHistory getCmEmailSendHistory(MailTemplateVO mailTemplateVo) throws Exception {
		CmEmailSendHistory cmEmailSendHistory = new CmEmailSendHistory();

		cmEmailSendHistory = processTemplate(mailTemplateVo, cmEmailSendHistory);

		// TODO 김영현 config & OTS 분리 여부
		cmEmailSendHistory.setSndrEmailAddrText(Const.SYS_SENDER_EMAIL_ADDRESS);
		cmEmailSendHistory.setSndrName(Const.SYS_SENDER_EMAIL_NAME);

		cmEmailSendHistory.setMemberNo(mailTemplateVo.getReceiverMemberNo());
		cmEmailSendHistory.setRcvrEmailAddrText(mailTemplateVo.getReceiverEmail());
		cmEmailSendHistory.setRcvrName(mailTemplateVo.getReceiverName());

		cmEmailSendHistory.setSendYn(Const.BOOLEAN_FALSE);
		cmEmailSendHistory.setRgsterNo(Const.SYSTEM_ADMIN_NO);

		return cmEmailSendHistory;
	}

	private CmEmailSendHistory processTemplate(MailTemplateVO mailTemplateVo, CmEmailSendHistory cmEmailSendHistory)
			throws Exception {
		CmEmailTemplate cmEmailTemplate = getMailTemplateByEmailId(mailTemplateVo.getMailTemplateId());

		String templateId = cmEmailTemplate.getEmailId();
		String templateTitleName = UtilsText.concat(templateId, "Title");
		String templateContentName = UtilsText.concat(templateId, "Content");

		// title template
		Template templateTitle = new Template(templateTitleName, cmEmailTemplate.getEmailTitleText(), freemarkerConfig);
		String sTitle = FreeMarkerTemplateUtils.processTemplateIntoString(templateTitle,
				mailTemplateVo.getMailTemplateModel());

		// content template
		Template templateContent = new Template(templateContentName, cmEmailTemplate.getEmailContText(),
				freemarkerConfig);
		String sContent = FreeMarkerTemplateUtils.processTemplateIntoString(templateContent,
				mailTemplateVo.getMailTemplateModel());

		log.debug("freemarker template result - {}, {}", sTitle, sContent);

		cmEmailSendHistory.setSiteNo(cmEmailTemplate.getSiteNo());
		cmEmailSendHistory.setEmailTmplSeq(cmEmailTemplate.getEmailTmplSeq());
		cmEmailSendHistory.setEmailTitleText(sTitle);
		cmEmailSendHistory.setEmailContText(sContent);

		return cmEmailSendHistory;
	}

	private CmEmailTemplate getMailTemplateByEmailId(String emailId) throws Exception {
		return emailTemplateService.getEmailTemplateByMailId(emailId);
	}

}
