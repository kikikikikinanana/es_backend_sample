package kr.co.shop.web.cmm.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cmm.model.master.CmPushAppDownloadMember;
import kr.co.shop.web.cmm.repository.master.base.BaseCmPushAppDownloadMemberDao;

@Mapper
public interface CmPushAppDownloadMemberDao extends BaseCmPushAppDownloadMemberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmPushAppDownloadMemberDao 클래스에 구현 되어있습니다.
	 * BaseCmPushAppDownloadMemberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public CmPushAppDownloadMember selectByPrimaryKey(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception;

	public void insertFcmToken(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception;

	public void updateDeviceToken(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception;

	public void updateReceivePush(CmPushAppDownloadMember cmPushAppDownloadMember) throws Exception;

	public void updateBioLogin(CmPushAppDownloadMember cmPushAppDownloadMember);

}
