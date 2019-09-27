package kr.co.shop.web.claim.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.claim.model.master.OcClaimStatusHistory;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimStatusHistoryDao;

@Mapper
public interface OcClaimStatusHistoryDao extends BaseOcClaimStatusHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcClaimStatusHistoryDao 클래스에 구현 되어있습니다.
	 * BaseOcClaimStatusHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public OcClaimStatusHistory selectByPrimaryKey(OcClaimStatusHistory ocClaimStatusHistory) throws Exception;

	/**
	 * @Desc : 클레임상태이력 등록
	 * @Method Name : insertClaimStatusHistory
	 * @Date : 2019. 5. 9.
	 * @Author : KTH
	 * @param ocClaimStatusHistory
	 * @return
	 * @throws Exception
	 */
	public int insertClaimStatusHistory(OcClaimStatusHistory ocClaimStatusHistory) throws Exception;
}
