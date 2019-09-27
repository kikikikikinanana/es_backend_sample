package kr.co.shop.web.event.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventJoinMember;
import kr.co.shop.web.event.repository.master.base.BaseEvEventJoinMemberDao;

@Mapper
public interface EvEventJoinMemberDao extends BaseEvEventJoinMemberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventJoinMemberDao 클래스에 구현 되어있습니다.
	 * BaseEvEventJoinMemberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventJoinMember selectByPrimaryKey(EvEventJoinMember evEventJoinMember) throws Exception;

	/**
	 * @Desc : 이벤트 회원 참여 수
	 * @Method Name : selectEventJoinMemberCount
	 * @Date : 2019. 5. 24.
	 * @Author : easyhun
	 * @param evEventRouletteJoinMember
	 * @return
	 * @throws Exception
	 */
	public int selectEventJoinMemberCount(EvEventJoinMember evEventJoinMember) throws Exception;

	/**
	 * @Desc : 이벤트 드로우검증 (eventNo, memberNo, prdtNo)
	 * @Method Name : selectDrawOrderCheckCount
	 * @Date : 2019. 6. 25.
	 * @Author : easyhun
	 * @param evEventJoinMember
	 * @return
	 * @throws Exception
	 */
	public int selectDrawOrderCheckCount(EvEventJoinMember evEventJoinMember) throws Exception;

	/**
	 * @Desc : 이벤트 드로우 참여 당첨자 정보 (eventNo, memberNo)
	 * @Method Name : selectEventJoinWinMemberDetail
	 * @Date : 2019. 6. 25.
	 * @Author : easyhun
	 * @param evEventJoinMember
	 * @return
	 * @throws Exception
	 */
	public EvEventJoinMember selectEventJoinWinMemberDetail(EvEventJoinMember evEventJoinMember) throws Exception;

	/**
	 * @Desc : 이벤트 드로우 참여 회원 정보(주문)
	 * @Method Name : selectDrawOrderCheckInfo
	 * @Date : 2019. 6. 25.
	 * @Author : easyhun
	 * @param evEventJoinMember
	 * @return
	 * @throws Exception
	 */
	public EvEventJoinMember selectDrawOrderCheckInfo(EvEventJoinMember evEventJoinMember) throws Exception;

}
