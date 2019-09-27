package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberMemoDao;
import kr.co.shop.web.member.model.master.MbMemberMemo;

@Mapper
public interface MbMemberMemoDao extends BaseMbMemberMemoDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseMbMemberMemoDao 클래스에 구현 되어있습니다.
     * BaseMbMemberMemoDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public MbMemberMemo selectByPrimaryKey(MbMemberMemo mbMemberMemo) throws Exception;

}
