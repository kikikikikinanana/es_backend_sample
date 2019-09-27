package kr.co.shop.web.promotion.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.promotion.model.master.PrPlanningDisplay;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplayCorner;
import kr.co.shop.web.promotion.repository.master.base.BasePrPlanningDisplayCornerDao;

@Mapper
public interface PrPlanningDisplayCornerDao extends BasePrPlanningDisplayCornerDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePrPlanningDisplayCornerDao 클래스에 구현 되어있습니다.
	 * BasePrPlanningDisplayCornerDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public PrPlanningDisplayCorner selectByPrimaryKey(PrPlanningDisplayCorner prPlanningDisplayCorner) throws Exception;

	/**
	 * @Desc : 기획전 상세 코너 리스트 조회
	 * @Method Name : selectPrPlanningDisplayCornerList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param prPlanningDisplay
	 * @return
	 */
	public List<PrPlanningDisplayCorner> selectPrPlanningDisplayCornerList(PrPlanningDisplay prPlanningDisplay)
			throws Exception;

}
