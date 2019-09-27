package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMemberEasyLogin;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberEasyLoginDao;

@Mapper
public interface MbMemberEasyLoginDao extends BaseMbMemberEasyLoginDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberEasyLoginDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberEasyLoginDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public MbMemberEasyLogin selectByPrimaryKey(MbMemberEasyLogin mbMemberEasyLogin) throws Exception;

	public MbMemberEasyLogin selectSnsLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception;

	public void updateEasyLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception;

	public void deleteEasyLoginInfo(MbMemberEasyLogin mbMemberEasyLogin) throws Exception;

}
