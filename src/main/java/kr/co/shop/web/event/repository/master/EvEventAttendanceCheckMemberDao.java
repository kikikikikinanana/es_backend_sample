package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventAttendanceCheckMember;
import kr.co.shop.web.event.repository.master.base.BaseEvEventAttendanceCheckMemberDao;

@Mapper
public interface EvEventAttendanceCheckMemberDao extends BaseEvEventAttendanceCheckMemberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventAttendanceCheckMemberDao 클래스에 구현
	 * 되어있습니다. BaseEvEventAttendanceCheckMemberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는
	 * 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventAttendanceCheckMember selectByPrimaryKey(EvEventAttendanceCheckMember evEventAttendanceCheckMember)
			throws Exception;

	/**
	 * @Desc : 이벤트 출석체크 달력
	 * @Method Name : selectEventCount
	 * @Date : 2019. 5. 21.
	 * @Author : easyhun
	 * @param evEventAttendanceCheckMember
	 * @return
	 * @throws Exception
	 */
	public List<EvEventAttendanceCheckMember> selectAttendanceCheckCalendar(
			EvEventAttendanceCheckMember evEventAttendanceCheckMember) throws Exception;

	/**
	 * @Desc : 이벤트 출석체크 회원 참여수
	 * @Method Name : selectAttendanceCheckMemberCnt
	 * @Date : 2019. 5. 21.
	 * @Author : easyhun
	 * @param evEventAttendanceCheckMember
	 * @return
	 * @throws Exception
	 */
	public int selectAttendanceCheckMemberCnt(EvEventAttendanceCheckMember evEventAttendanceCheckMember)
			throws Exception;

}
