package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.event.model.master.EvEventAnswer;
import kr.co.shop.web.event.repository.master.base.BaseEvEventAnswerDao;

@Mapper
public interface EvEventAnswerDao extends BaseEvEventAnswerDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventAnswerDao 클래스에 구현 되어있습니다.
	 * BaseEvEventAnswerDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventAnswer selectByPrimaryKey(EvEventAnswer evEventAnswer) throws Exception;

	/**
	 * @Desc : 이벤트 회원 댓글 카운트
	 * @Method Name : selectEventAnswerCount
	 * @Date : 2019. 5. 16.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectEventAnswerCount(Pageable<EvEventAnswer, EvEventAnswer> pageable) throws Exception;

	/**
	 * @Desc : 이벤트 회원 댓글 리스트
	 * @Method Name : selectEventAnswerList
	 * @Date : 2019. 5. 16.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<EvEventAnswer> selectEventAnswerList(Pageable<EvEventAnswer, EvEventAnswer> pageable) throws Exception;

	/**
	 * 이벤트 회원 참여 카운트
	 * 
	 * @Desc : 이벤트 회원 참여 카운트
	 * @Method Name : selectEventAnswerJoinCount
	 * @Date : 2019. 5. 17
	 * @Author : easyhun
	 * @param eventNo
	 * @return
	 */
	public int selectEventAnswerJoinCount(EvEventAnswer evEventAnswer) throws Exception;

	/**
	 * 이벤트 댓글 수정
	 * 
	 * @Desc : 이벤트 댓글 수정
	 * @Method Name : updateEventAnswer
	 * @Date : 2019. 5. 20
	 * @Author : easyhun
	 * @param eventNo
	 * @return
	 */
	public void updateEventAnswer(EvEventAnswer evEventAnswer) throws Exception;
}
