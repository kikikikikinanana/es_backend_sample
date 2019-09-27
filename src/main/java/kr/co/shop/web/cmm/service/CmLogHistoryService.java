package kr.co.shop.web.cmm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.cmm.model.master.CmEmailSendHistory;
import kr.co.shop.web.cmm.model.master.CmMessageSendHistory;
import kr.co.shop.web.cmm.repository.master.CmEmailSendHistoryDao;
import kr.co.shop.web.cmm.repository.master.CmMessageSendHistoryDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CmLogHistoryService {

	@Autowired
	CmEmailSendHistoryDao cmEmailSendHistoryDao;

	@Autowired
	CmMessageSendHistoryDao cmMessageSendHistoryDao;

	/**
	 * @Desc : 메일이력 테이블 insert
	 * @Method Name : insertCmEmailSendHistory
	 * @Date : 2019. 4. 11.
	 * @Author : Kimyounghyun
	 * @param cmEmailSendHistory
	 * @return
	 * @throws Exception
	 */
	protected int insertCmEmailSendHistory(CmEmailSendHistory cmEmailSendHistory) throws Exception {
		return cmEmailSendHistoryDao.insertMail(cmEmailSendHistory);
	}

	/**
	 * @Desc : 메시지이력 테이블 insert
	 * @Method Name : insertCmMessageSendHistory
	 * @Date : 2019. 4. 11.
	 * @Author : Kimyounghyun
	 * @param cmEmailSendHistory
	 * @return
	 * @throws Exception
	 */
	private int insertCmMessageSendHistory(CmMessageSendHistory cmEmailSendHistory) throws Exception {
		return cmMessageSendHistoryDao.insert(cmEmailSendHistory);
	}

}
