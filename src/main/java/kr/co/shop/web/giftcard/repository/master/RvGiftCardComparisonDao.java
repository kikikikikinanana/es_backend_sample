package kr.co.shop.web.giftcard.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.giftcard.model.master.RvGiftCardComparison;
import kr.co.shop.web.giftcard.repository.master.base.BaseRvGiftCardComparisonDao;

@Mapper
public interface RvGiftCardComparisonDao extends BaseRvGiftCardComparisonDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseRvGiftCardComparisonDao 클래스에 구현 되어있습니다.
	 * BaseRvGiftCardComparisonDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public RvGiftCardComparison selectByPrimaryKey(RvGiftCardComparison rvGiftCardComparison) throws Exception;

	public List<RvGiftCardComparison> selectUseHistory(Pageable<RvGiftCardComparison, RvGiftCardComparison> pageable)
			throws Exception;
}
