package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.event.model.master.EvEvent;
import kr.co.shop.web.event.repository.master.base.BaseEvEventDao;

@Mapper
public interface EvEventDao extends BaseEvEventDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventDao 클래스에 구현 되어있습니다. BaseEvEventDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEvent selectByPrimaryKey(EvEvent evEvent) throws Exception;

	/**
	 * @Desc : 이벤트 카운트
	 * @Method Name : selectEventCount
	 * @Date : 2019. 5. 10.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectEventCount(Pageable<EvEvent, EvEvent> pageable) throws Exception;

	/**
	 * @Desc : 이벤트 리스트
	 * @Method Name : selectEventList
	 * @Date : 2019. 5. 10.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<EvEvent> selectEventList(Pageable<EvEvent, EvEvent> pageable) throws Exception;

	/**
	 * 이벤트 상세 조회
	 * 
	 * @Desc :
	 * @Method Name : selectEvent
	 * @Date : 2019. 5. 10
	 * @Author : easyhun
	 * @param pageable
	 * @throws Exception
	 */
	public EvEvent selectEvent(EvEvent evEvent) throws Exception;

	/**
	 * @Desc : 회원 참여 이벤트 카운트
	 * @Method Name : selectMemberEventCount
	 * @Date : 2019. 5. 08.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectMemberEventCount(Pageable<EvEvent, EvEvent> pageable) throws Exception;

	/**
	 * @Desc : 회원 참여 이벤트
	 * @Method Name : selectMemberEventList
	 * @Date : 2019. 5. 08.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<EvEvent> selectMemberEventList(Pageable<EvEvent, EvEvent> pageable) throws Exception;

}
