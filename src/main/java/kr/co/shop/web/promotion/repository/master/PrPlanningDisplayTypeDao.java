package kr.co.shop.web.promotion.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrPlanningDisplayTypeDao;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplayType;

@Mapper
public interface PrPlanningDisplayTypeDao extends BasePrPlanningDisplayTypeDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePrPlanningDisplayTypeDao 클래스에 구현 되어있습니다.
     * BasePrPlanningDisplayTypeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PrPlanningDisplayType selectByPrimaryKey(PrPlanningDisplayType prPlanningDisplayType) throws Exception;

}
