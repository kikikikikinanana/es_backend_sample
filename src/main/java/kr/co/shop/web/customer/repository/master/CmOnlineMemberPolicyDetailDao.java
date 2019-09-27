package kr.co.shop.web.customer.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.customer.repository.master.base.BaseCmOnlineMemberPolicyDetailDao;
import kr.co.shop.web.customer.model.master.CmOnlineMemberPolicyDetail;

@Mapper
public interface CmOnlineMemberPolicyDetailDao extends BaseCmOnlineMemberPolicyDetailDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseCmOnlineMemberPolicyDetailDao 클래스에 구현 되어있습니다.
     * BaseCmOnlineMemberPolicyDetailDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public CmOnlineMemberPolicyDetail selectByPrimaryKey(CmOnlineMemberPolicyDetail cmOnlineMemberPolicyDetail) throws Exception;

}
