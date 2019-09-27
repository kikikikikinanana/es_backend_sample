package kr.co.shop.web.cmm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.constant.BaseConst;
import kr.co.shop.constant.Const;
import kr.co.shop.web.cmm.model.master.CmMessageSendHistory;
import kr.co.shop.web.cmm.model.master.CmMessageTemplate;
import kr.co.shop.web.cmm.repository.master.CmMessageSendHistoryDao;
import kr.co.shop.web.cmm.repository.master.CmMessageTemplateDao;
import kr.co.shop.web.cmm.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageTemplateService {
	@Autowired
	CmMessageSendHistoryDao cmMessageSendHistoryDao;

	@Autowired
	CmMessageTemplateDao cmMessageTemplateDao;

	private String activeProfile = Config.getString("spring.profiles.active", "");

	public int setMessageSendHistory(CmMessageSendHistory params) throws Exception {
//		String sndrName = "(주)에이비씨마트코리아";

		String rgsterNo = Const.SYSTEM_ADMIN_NO;

		// TODO 김영현, 사이트별 Sender, Sender 정의 필요
//		if ("100001".equals(params.getSiteNo())) {
//			sndrName = "(주)에이비씨마트코리아";
//		}
//		params.setSndrName(sndrName);

//		if (UtilsText.isAllBlank(params.getMemberNo())) {
//			params.setMemberNo(Const.NON_MEMBER_NO);
//		}
//		params.setAdminSendYn(Const.BOOLEAN_TRUE);
		params.setRgsterNo(rgsterNo);

		return insertCmMessageSendHistory(params);
	}

	/**
	 * @Desc : CM_MESSAGE_SEND_HISTORY에 데이터를 저장한다.
	 * @Method Name : insertCmMessageSendHistory
	 * @Date : 2019. 3. 21.
	 * @Author : kiowa
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private int insertCmMessageSendHistory(CmMessageSendHistory params) throws Exception {
		return cmMessageSendHistoryDao.insert(params);
	}

	public CmMessageTemplate getMessageTemplateByMesgId(String mesgId) throws Exception {
		return cmMessageTemplateDao.selectMessageTemplateByMesgId(mesgId);
	}

	public CmMessageTemplate getMessageTemplate(MessageVO messageVO) throws Exception {
		return cmMessageTemplateDao.selectMessageTemplate(messageVO);
	}

	/**
	 * @Desc : SMS 즉시발송 테이블에 insert 한다.
	 * @Method Name : setSendRealTimeSms
	 * @Date : 2019. 4. 23.
	 * @Author : choi
	 * @param messageVO
	 * @return
	 * @throws Exception
	 */
	public int setSendRealTimeSmsNoTrx(MessageVO messageVO) throws Exception {
		int resultCnt = 0;
		if (BaseConst.PROFILE_LOCAL.equalsIgnoreCase(activeProfile)) {
			resultCnt = cmMessageSendHistoryDao.insertSendRealTimeSmsTest(messageVO);
		} else {
			resultCnt = cmMessageSendHistoryDao.insertSendRealTimeSms(messageVO);
		}
		return resultCnt;
	}

	/**
	 * @Desc : LMS 즉시발송 테이블에 insert 한다.
	 * @Method Name : setSendRealTimeSms
	 * @Date : 2019. 4. 23.
	 * @Author : choi
	 * @param messageVO
	 * @return
	 * @throws Exception
	 */
	public int setSendRealTimeLmsNoTrx(MessageVO messageVO) throws Exception {
		int resultCnt = 0;
		if (BaseConst.PROFILE_LOCAL.equalsIgnoreCase(activeProfile)) {
			resultCnt = cmMessageSendHistoryDao.insertSendRealTimeLmsTest(messageVO);
		} else {
			resultCnt = cmMessageSendHistoryDao.insertSendRealTimeLms(messageVO);
		}
		return resultCnt;
	}

	/**
	 * @Desc : KKO 즉시발송 테이블에 insert 한다.
	 * @Method Name : setSendRealTimeKko
	 * @Date : 2019. 4. 23.
	 * @Author : choi
	 * @param messageVO
	 * @return
	 * @throws Exception
	 */
	public int setSendRealTimeKkoNoTrx(MessageVO messageVO) throws Exception {
		int resultCnt = 0;
		if (BaseConst.PROFILE_LOCAL.equalsIgnoreCase(activeProfile)) {
			resultCnt = cmMessageSendHistoryDao.insertSendRealTimeKkoTest(messageVO);
		} else {
			resultCnt = cmMessageSendHistoryDao.insertSendRealTimeKko(messageVO);
		}
		return resultCnt;
	}

}
