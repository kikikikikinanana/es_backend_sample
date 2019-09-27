package kr.co.shop.web.promotion.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrPlanningDisplayCornerProductDao;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplayCornerProduct;

@Mapper
public interface PrPlanningDisplayCornerProductDao extends BasePrPlanningDisplayCornerProductDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePrPlanningDisplayCornerProductDao 클래스에 구현 되어있습니다.
     * BasePrPlanningDisplayCornerProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PrPlanningDisplayCornerProduct selectByPrimaryKey(PrPlanningDisplayCornerProduct prPlanningDisplayCornerProduct) throws Exception;

}
