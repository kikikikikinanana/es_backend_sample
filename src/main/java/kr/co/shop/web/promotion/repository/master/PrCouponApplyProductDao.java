package kr.co.shop.web.promotion.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrCouponApplyProductDao;
import kr.co.shop.web.promotion.model.master.PrCouponApplyProduct;

@Mapper
public interface PrCouponApplyProductDao extends BasePrCouponApplyProductDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePrCouponApplyProductDao 클래스에 구현 되어있습니다.
     * BasePrCouponApplyProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PrCouponApplyProduct selectByPrimaryKey(PrCouponApplyProduct prCouponApplyProduct) throws Exception;

}
