package kr.co.shop.web.claim.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimCertificationHistoryDao;
import kr.co.shop.web.claim.model.master.OcClaimCertificationHistory;

@Mapper
public interface OcClaimCertificationHistoryDao extends BaseOcClaimCertificationHistoryDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseOcClaimCertificationHistoryDao 클래스에 구현 되어있습니다.
     * BaseOcClaimCertificationHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public OcClaimCertificationHistory selectByPrimaryKey(OcClaimCertificationHistory ocClaimCertificationHistory) throws Exception;

}
