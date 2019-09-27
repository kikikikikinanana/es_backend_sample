package kr.co.shop.web.promotion.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplay;
import kr.co.shop.web.promotion.repository.master.base.BasePrPlanningDisplayDao;

@Mapper
public interface PrPlanningDisplayDao extends BasePrPlanningDisplayDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePrPlanningDisplayDao 클래스에 구현 되어있습니다.
	 * BasePrPlanningDisplayDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PrPlanningDisplay selectByPrimaryKey(PrPlanningDisplay prPlanningDisplay) throws Exception;

	/**
	 * @Desc : 기획전 리스트 조회
	 * @Method Name : selectPrPlanningDisplayList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param pageable
	 * @return
	 */
	public List<PrPlanningDisplay> selectPrPlanningDisplayList(Pageable<PrPlanningDisplay, PrPlanningDisplay> pageable)
			throws Exception;

	/**
	 * @Desc : 기획전 리스트 카운트 조회
	 * @Method Name : selectPrPlanningDisplayListCount
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param pageable
	 * @return
	 */
	public int selectPrPlanningDisplayListCount(Pageable<PrPlanningDisplay, PrPlanningDisplay> pageable)
			throws Exception;

	/**
	 * @Desc : 기획전 상세 조회
	 * @Method Name : selectPrPlanningDisplayDetail
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param prPlanningDisplay
	 * @return
	 */
	public PrPlanningDisplay selectPrPlanningDisplayDetail(PrPlanningDisplay prPlanningDisplay) throws Exception;

}
