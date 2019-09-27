package kr.co.shop.web.cmm.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cmm.model.master.CmMessageSendHistory;
import kr.co.shop.web.cmm.repository.master.base.BaseCmMessageSendHistoryDao;
import kr.co.shop.web.cmm.vo.MessageVO;

@Mapper
public interface CmMessageSendHistoryDao extends BaseCmMessageSendHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmMessageSendHistoryDao 클래스에 구현 되어있습니다.
	 * BaseCmMessageSendHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 *
	 */

	public CmMessageSendHistory selectByPrimaryKey(CmMessageSendHistory cmMessageSendHistory) throws Exception;

	/**
	 *
	 * @Desc : SMS 발송 메세지 등록
	 * @Method Name : insertSendRealTimeSms
	 * @Date : 2019. 4. 12.
	 * @Author : choi
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int insertSendRealTimeSms(MessageVO mailReceiveVO) throws Exception;

	/**
	 *
	 * @Desc : LMS 발송 메세지 등록
	 * @Method Name : insertSendRealTimeLms
	 * @Date : 2019. 4. 23.
	 * @Author : choi
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int insertSendRealTimeLms(MessageVO mailReceiveVO) throws Exception;

	/**
	 *
	 * @Desc : KKO 발송 메세지 등록
	 * @Method Name : insertSendRealTimeKko
	 * @Date : 2019. 4. 23.
	 * @Author : choi
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int insertSendRealTimeKko(MessageVO mailReceiveVO) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : insertSendRealTimeSmsTest
	 * @Date : 2019. 7. 4.
	 * @Author : 유성민
	 * @param messageVO
	 * @return
	 */
	public int insertSendRealTimeSmsTest(MessageVO messageVO) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : insertSendRealTimeLmsTest
	 * @Date : 2019. 7. 4.
	 * @Author : 유성민
	 * @param messageVO
	 * @return
	 */
	public int insertSendRealTimeLmsTest(MessageVO messageVO) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : insertSendRealTimeKkoTest
	 * @Date : 2019. 7. 4.
	 * @Author : 유성민
	 * @param messageVO
	 * @return
	 */
	public int insertSendRealTimeKkoTest(MessageVO messageVO) throws Exception;

}
