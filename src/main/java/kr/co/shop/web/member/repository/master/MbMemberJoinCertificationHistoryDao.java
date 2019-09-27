package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMemberJoinCertificationHistory;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberJoinCertificationHistoryDao;

@Mapper
public interface MbMemberJoinCertificationHistoryDao extends BaseMbMemberJoinCertificationHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberJoinCertificationHistoryDao 클래스에
	 * 구현 되어있습니다. BaseMbMemberJoinCertificationHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실
	 * 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public MbMemberJoinCertificationHistory selectByPrimaryKey(
			MbMemberJoinCertificationHistory mbMemberJoinCertificationHistory) throws Exception;

	public int selectCertificationCount(MbMemberJoinCertificationHistory memberJoinCertificationHistory);

	public void insertCertification(MbMemberJoinCertificationHistory memberJoinCertificationHistory);

	public MbMemberJoinCertificationHistory selectCertification(
			MbMemberJoinCertificationHistory memberJoinCertificationHistory);

	public void updateCertificationNumber(MbMemberJoinCertificationHistory memberJoinCertificationHistory);

}
