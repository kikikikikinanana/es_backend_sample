package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberGradeHistoryDao;
import kr.co.shop.web.member.model.master.MbMemberGradeHistory;

@Mapper
public interface MbMemberGradeHistoryDao extends BaseMbMemberGradeHistoryDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseMbMemberGradeHistoryDao 클래스에 구현 되어있습니다.
     * BaseMbMemberGradeHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public MbMemberGradeHistory selectByPrimaryKey(MbMemberGradeHistory mbMemberGradeHistory) throws Exception;

}
