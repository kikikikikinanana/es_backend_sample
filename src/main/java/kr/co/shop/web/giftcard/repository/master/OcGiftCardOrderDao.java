package kr.co.shop.web.giftcard.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.giftcard.model.master.OcGiftCardOrder;
import kr.co.shop.web.giftcard.repository.master.base.BaseOcGiftCardOrderDao;

@Mapper
public interface OcGiftCardOrderDao extends BaseOcGiftCardOrderDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcGiftCardOrderDao 클래스에 구현 되어있습니다.
	 * BaseOcGiftCardOrderDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcGiftCardOrder selectByPrimaryKey(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public void insertGiftCardPayment(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public void updateGiftCardPayment(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public List<OcGiftCardOrder> selectGiftCardHistoryList(Pageable<OcGiftCardOrder, OcGiftCardOrder> pageable)
			throws Exception;

	public int selectGiftCardHistoryCount(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public OcGiftCardOrder selectGiftCardHistoryForCancel(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public void updateGiftCardOrderCancelStat(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public void updateGiftCardStat(OcGiftCardOrder ocGiftCardOrder) throws Exception;

	public OcGiftCardOrder selectGiftCardSaleAgenciesInfo(OcGiftCardOrder ocGiftCardOrder) throws Exception;
}
