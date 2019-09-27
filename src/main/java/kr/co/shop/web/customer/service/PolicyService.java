package kr.co.shop.web.customer.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.customer.model.master.CmOnlineMemberPolicy;
import kr.co.shop.web.customer.repository.master.CmOnlineMemberPolicyDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicyService{
	@Autowired
	CmOnlineMemberPolicyDao cmOnlineMemberPolicyDao;

	@Autowired
	PolicyService policyService ;

	/**
	 *
	 * @Desc      	: 온라인 회원 혜택 테이블에서 시행중인 번호[plcy_seq]를 가져온다.
	 * @Method Name : getOnlineMemberSeq
	 * @Date  	  	: 2019. 3. 18.
	 * @Author    	: choi
	 * @return
	 * @throws Exception
	 */
	public String getOnlineMemberPolicySeq() throws Exception {
		return cmOnlineMemberPolicyDao.selectOnlineMemberPolicySeq();
	}

	/**
	 * @Desc      	: 온라인 회원 혜택 기본 정보 조회
	 * @Method Name : getOnlinePolicyData
	 * @Date  	  	: 2019. 6. 26.
	 * @Author    	: choi
	 * @return
	 * @throws Exception
	 */
	public CmOnlineMemberPolicy getOnlinePolicyData(String plcySeq) throws Exception{
		return cmOnlineMemberPolicyDao.selectOnlinePolicyData(plcySeq);
	}

	/**
	 *
	 * @Desc      	: 온라인 회원 혜택 쿠폰 정보
	 * @Method Name : getOnlinePolicyCoupon
	 * @Date  	  	: 2019. 3. 18.
	 * @Author    	: choi
	 * @param cmOnlineMemberPolicy
	 * @return
	 */
	public List<CmOnlineMemberPolicy> getOnlinePolicyCoupon(String plcySql) throws Exception {
		return cmOnlineMemberPolicyDao.selectOnlinePolicyCoupon(plcySql);
	}

	/**
	 *
	 * @Desc      	: 온라인 회원 혜택 기본정보와 쿠폰정보를 Map으로 리턴
	 * @Method Name : getOnlinePolicyDataProcess
	 * @Date  	  	: 2019. 6. 27.
	 * @Author    	: choi
	 * @param
	 * @return
	 */
	public Map<String, Object> getOnlinePolicyDataProcess() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		String plcySeq = policyService.getOnlineMemberPolicySeq();

		result.put("onlinePolicyData", policyService.getOnlinePolicyData(plcySeq));	// 온라인 혜택 기본정보
		result.put("onlinePolicyDetail", policyService.getOnlinePolicyCoupon(plcySeq));	// 온라인 혜택 상세정보
		return result;
	}

}
