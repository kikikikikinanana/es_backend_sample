package kr.co.shop.web.giftcard.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.giftcard.model.master.OcGiftCardOrderPayment;
import kr.co.shop.web.giftcard.repository.master.base.BaseOcGiftCardOrderPaymentDao;

@Mapper
public interface OcGiftCardOrderPaymentDao extends BaseOcGiftCardOrderPaymentDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcGiftCardOrderPaymentDao 클래스에 구현 되어있습니다.
	 * BaseOcGiftCardOrderPaymentDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public OcGiftCardOrderPayment selectByPrimaryKey(OcGiftCardOrderPayment ocGiftCardOrderPayment) throws Exception;

	public void insertGiftCardKcpPayment(OcGiftCardOrderPayment ocGiftCardOrderPayment) throws Exception;

	public void updateGiftCardPaymentCancelStat(OcGiftCardOrderPayment ocGiftCardOrderPayment) throws Exception;
}
