package kr.co.shop.web.cmm.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cmm.model.master.CmEmailSendHistory;
import kr.co.shop.web.cmm.repository.master.base.BaseCmEmailSendHistoryDao;

@Mapper
public interface CmEmailSendHistoryDao extends BaseCmEmailSendHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmEmailSendHistoryDao 클래스에 구현 되어있습니다.
	 * BaseCmEmailSendHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public CmEmailSendHistory selectByPrimaryKey(CmEmailSendHistory cmEmailSendHistory) throws Exception;

	/**
	 * @Desc : 실시간 발송여부에 다른 send_dtm 처리 insert
	 * @Method Name : insertMail
	 * @Date : 2019. 5. 10.
	 * @Author : Kimyounghyun
	 * @param cmEmailSendHistory
	 * @return
	 */
	public int insertMail(CmEmailSendHistory cmEmailSendHistory);

}
