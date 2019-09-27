package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventRouletteBenefit;
import kr.co.shop.web.event.repository.master.base.BaseEvEventRouletteBenefitDao;

@Mapper
public interface EvEventRouletteBenefitDao extends BaseEvEventRouletteBenefitDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventRouletteBenefitDao 클래스에 구현 되어있습니다.
	 * BaseEvEventRouletteBenefitDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public EvEventRouletteBenefit selectByPrimaryKey(EvEventRouletteBenefit evEventRouletteBenefit) throws Exception;

	/**
	 * @Desc : 이벤트 룰렛 혜택
	 * @Method Name : selectEventRouletteBenefitByEventNo
	 * @Date : 2019. 5. 23.
	 * @Author : easyhun
	 * @param eventNo
	 * @return
	 * @throws Exception
	 */
	public List<EvEventRouletteBenefit> selectEventRouletteBenefitByEventNo(String eventNo) throws Exception;

	/**
	 * @Desc : 제한수량이 넘었을 경우, 당첨 확률 0 update
	 * @Method Name : updateLimitWinRate
	 * @Date : 2019. 5. 23.
	 * @Author : easyhun
	 * @param evEventRouletteBenefit
	 * @return
	 * @throws Exception
	 */
	public void updateLimitWinRate(EvEventRouletteBenefit evEventRouletteBenefit) throws Exception;

	/**
	 * @Desc : 꽝, 혜택 조회
	 * @Method Name : selectByBenefitGbnCode
	 * @Date : 2019. 5. 27.
	 * @Author : easyhun
	 * @param evEventRouletteBenefit
	 * @return
	 * @throws Exception
	 */
	public EvEventRouletteBenefit selectByBenefitGbnCode(EvEventRouletteBenefit evEventRouletteBenefit)
			throws Exception;

}
