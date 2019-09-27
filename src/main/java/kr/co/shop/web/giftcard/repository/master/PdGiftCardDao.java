package kr.co.shop.web.giftcard.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.giftcard.model.master.PdGiftCard;
import kr.co.shop.web.giftcard.repository.master.base.BasePdGiftCardDao;

@Mapper
public interface PdGiftCardDao extends BasePdGiftCardDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdGiftCardDao 클래스에 구현 되어있습니다.
	 * BasePdGiftCardDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PdGiftCard selectByPrimaryKey(PdGiftCard pdGiftCard) throws Exception;

	public List<PdGiftCard> selectPdGiftCardList(Pageable<PdGiftCard, PdGiftCard> pageable) throws Exception;

	public int selectPdGiftCardListTotal() throws Exception;

	public PdGiftCard selectPdGiftCardInfo(PdGiftCard pdGiftCard) throws Exception;

	public PdGiftCard selectPdGiftCardInfoForSale(PdGiftCard pdGiftCard) throws Exception;
}
