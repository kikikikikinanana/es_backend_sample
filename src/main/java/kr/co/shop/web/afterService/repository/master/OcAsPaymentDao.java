package kr.co.shop.web.afterService.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.afterService.model.master.OcAsPayment;
import kr.co.shop.web.afterService.repository.master.base.BaseOcAsPaymentDao;

@Mapper
public interface OcAsPaymentDao extends BaseOcAsPaymentDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcAsPaymentDao 클래스에 구현 되어있습니다.
	 * BaseOcAsPaymentDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcAsPayment selectByPrimaryKey(OcAsPayment ocAsPayment) throws Exception;

	public int insertOcAsPayment(OcAsPayment ocAsPayment) throws Exception;

	public int updateOcAsPaymentAccount(OcAsPayment ocAsPayment) throws Exception;

	public OcAsPayment selectAsPymntDetailInfo(OcAsPayment ocAsPayment) throws Exception;

	public int updateOcAsPaymentAmtCancel(OcAsPayment ocAsPayment) throws Exception;

}
