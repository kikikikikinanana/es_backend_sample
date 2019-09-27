package kr.co.shop.web.event.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventRouletteJoinMember;
import kr.co.shop.web.event.repository.master.base.BaseEvEventRouletteJoinMemberDao;

@Mapper
public interface EvEventRouletteJoinMemberDao extends BaseEvEventRouletteJoinMemberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventRouletteJoinMemberDao 클래스에 구현
	 * 되어있습니다. BaseEvEventRouletteJoinMemberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventRouletteJoinMember selectByPrimaryKey(EvEventRouletteJoinMember evEventRouletteJoinMember)
			throws Exception;

	/**
	 * @Desc : 이벤트 룰렛 회원 참여 수
	 * @Method Name : selectEventRouletteJoinMemberCount
	 * @Date : 2019. 5. 24.
	 * @Author : easyhun
	 * @param evEventRouletteJoinMember
	 * @return
	 * @throws Exception
	 */
	public int selectEventRouletteJoinMemberCount(EvEventRouletteJoinMember evEventRouletteJoinMember) throws Exception;

}
