package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMemberLoginHistory;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberLoginHistoryDao;

@Mapper
public interface MbMemberLoginHistoryDao extends BaseMbMemberLoginHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberLoginHistoryDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberLoginHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public MbMemberLoginHistory selectByPrimaryKey(MbMemberLoginHistory mbMemberLoginHistory) throws Exception;

	public void insertMemberHistory(MbMemberLoginHistory mbMemberLoginHistory) throws Exception;

}
