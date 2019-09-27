package kr.co.shop.web.claim.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimMemoDao;
import kr.co.shop.web.claim.model.master.OcClaimMemo;

@Mapper
public interface OcClaimMemoDao extends BaseOcClaimMemoDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseOcClaimMemoDao 클래스에 구현 되어있습니다.
     * BaseOcClaimMemoDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public OcClaimMemo selectByPrimaryKey(OcClaimMemo ocClaimMemo) throws Exception;

}
