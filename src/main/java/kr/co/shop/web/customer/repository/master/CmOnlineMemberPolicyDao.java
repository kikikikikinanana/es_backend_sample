package kr.co.shop.web.customer.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.customer.model.master.CmOnlineMemberPolicy;
import kr.co.shop.web.customer.repository.master.base.BaseCmOnlineMemberPolicyDao;

@Mapper
public interface CmOnlineMemberPolicyDao extends BaseCmOnlineMemberPolicyDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmOnlineMemberPolicyDao 클래스에 구현 되어있습니다.
	 * BaseCmOnlineMemberPolicyDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 *
	 */

	public CmOnlineMemberPolicy selectByPrimaryKey(CmOnlineMemberPolicy cmOnlineMemberPolicy) throws Exception;

	/**
	 *
	 * @Desc : 온라인 회원 혜택 테이블에서 운영중인 번호[plcy_seq]를 가져온다.
	 * @Method Name : selectOnlineMemberPolicySeq
	 * @Date : 2019. 3. 18.
	 * @Author : choi
	 * @return
	 * @throws Exception
	 */
	public String selectOnlineMemberPolicySeq() throws Exception;

	/**
	 *
	 * @Desc : 온라인 회원 혜택의 기본정보를 가져온다.
	 * @Method Name : selectOnlinePolicy
	 * @Date : 2019. 3. 18.
	 * @Author : choi
	 * @return
	 * @throws Exception
	 */
	public CmOnlineMemberPolicy selectOnlinePolicy(CmOnlineMemberPolicy cmOnlineMemberPolicy) throws Exception;

	/**
	 *
	 * @Desc : 온라인 회원 쿠폰 혜택정보를 가져온다.
	 * @Method Name : selectOnlinePolicyCoupon
	 * @Date : 2019. 3. 18.
	 * @Author : choi
	 * @param cmOnlineMemberPolicy
	 * @return
	 * @throws Exception
	 */
	public List<CmOnlineMemberPolicy> selectOnlinePolicyCoupon(String plcySql) throws Exception;

	/**
	 *
	 * @Desc : 온라인 회원 정책 데이터 조회
	 * @Method Name : getPolicyData
	 * @Date : 2019. 5. 23.
	 * @Author : 최경호
	 * @return
	 * @throws Exception
	 */
	public List<CmOnlineMemberPolicy> selectPolicyData() throws Exception;

	/**
	 *
	 * @Desc : 시행중인 온라인 회원 정책 기본 데이터 조회
	 * @Method Name : selectOnlinePolicyData
	 * @Date : 2019. 6. 27.
	 * @Author : 최경호
	 * @return
	 * @throws Exception
	 */
	public CmOnlineMemberPolicy selectOnlinePolicyData(String plcySeq) throws Exception;

}
