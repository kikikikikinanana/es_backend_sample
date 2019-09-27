package kr.co.shop.web.cmm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.cmm.model.master.CmEmailTemplate;
import kr.co.shop.web.cmm.repository.master.CmEmailTemplateDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailTemplateService {

	@Autowired
	CmEmailTemplateDao cmEmailTemplateDao;

	public CmEmailTemplate getEmailTemplateByMailId(String emailId) {
		return cmEmailTemplateDao.selectCmEmailTemplateByEmailId(emailId);
	}

}
