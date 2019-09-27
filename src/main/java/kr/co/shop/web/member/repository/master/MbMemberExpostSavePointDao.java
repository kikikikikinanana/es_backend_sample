package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMemberExpostSavePoint;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberExpostSavePointDao;

@Mapper
public interface MbMemberExpostSavePointDao extends BaseMbMemberExpostSavePointDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberExpostSavePointDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberExpostSavePointDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public MbMemberExpostSavePoint selectByPrimaryKey(MbMemberExpostSavePoint mbMemberExpostSavePoint) throws Exception;

	public int getLatedSavePointRecent1MonthCount(MbMemberExpostSavePoint params) throws Exception;

	public String checkPossibleLatedSavePoint(MbMemberExpostSavePoint params) throws Exception;

	public int selectExpostSavePointSeqNextVal(MbMemberExpostSavePoint params) throws Exception;

}
