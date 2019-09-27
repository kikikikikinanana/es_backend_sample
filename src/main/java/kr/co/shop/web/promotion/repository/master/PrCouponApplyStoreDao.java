package kr.co.shop.web.promotion.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrCouponApplyStoreDao;
import kr.co.shop.web.promotion.model.master.PrCouponApplyStore;

@Mapper
public interface PrCouponApplyStoreDao extends BasePrCouponApplyStoreDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePrCouponApplyStoreDao 클래스에 구현 되어있습니다.
     * BasePrCouponApplyStoreDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PrCouponApplyStore selectByPrimaryKey(PrCouponApplyStore prCouponApplyStore) throws Exception;

}
