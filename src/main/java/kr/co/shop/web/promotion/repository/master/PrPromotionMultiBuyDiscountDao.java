package kr.co.shop.web.promotion.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrPromotionMultiBuyDiscountDao;
import kr.co.shop.web.promotion.model.master.PrPromotionMultiBuyDiscount;

@Mapper
public interface PrPromotionMultiBuyDiscountDao extends BasePrPromotionMultiBuyDiscountDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePrPromotionMultiBuyDiscountDao 클래스에 구현 되어있습니다.
     * BasePrPromotionMultiBuyDiscountDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PrPromotionMultiBuyDiscount selectByPrimaryKey(PrPromotionMultiBuyDiscount prPromotionMultiBuyDiscount) throws Exception;

}
