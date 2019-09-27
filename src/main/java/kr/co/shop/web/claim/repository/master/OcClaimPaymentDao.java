package kr.co.shop.web.claim.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.claim.model.master.OcClaimPayment;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimPaymentDao;

@Mapper
public interface OcClaimPaymentDao extends BaseOcClaimPaymentDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcClaimPaymentDao 클래스에 구현 되어있습니다.
	 * BaseOcClaimPaymentDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcClaimPayment selectByPrimaryKey(OcClaimPayment ocClaimPayment) throws Exception;

	/**
	 * @Desc : 클레임 결제 목록 조회 쿼리
	 * @Method Name : selectClaimPaymentList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param OcClaimPayment
	 * @return
	 * @throws Exception
	 */
	public List<OcClaimPayment> selectClaimPaymentList(OcClaimPayment OcClaimPayment) throws Exception;

	/**
	 * @Desc : 클레임결제 등록
	 * @Method Name : insertClaimPayment
	 * @Date : 2019. 5. 13.
	 * @Author : KTH
	 * @param ocClaimPayment
	 * @return
	 * @throws Exception
	 */
	public int insertClaimPayment(OcClaimPayment ocClaimPayment) throws Exception;

	/**
	 * @Desc : 클레임배송비 결제완료 시 클레임결제 수정
	 * @Method Name : updateClaimPaymentAddDlvyAmt
	 * @Date : 2019. 5. 13.
	 * @Author : KTH
	 * @param ocClaimPayment
	 * @return
	 * @throws Exception
	 */
	public int updateClaimPaymentAddDlvyAmt(OcClaimPayment ocClaimPayment) throws Exception;

	/**
	 * @Desc : 클레임 취소가능 잔여금액 목록
	 * @Method Name : selectOrderClaimPaymentList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param ocClaimPayment
	 * @return
	 * @throws Exception
	 */
	public List<OcClaimPayment> selectOrderClaimPaymentList(OcClaimPayment ocClaimPayment) throws Exception;
}
