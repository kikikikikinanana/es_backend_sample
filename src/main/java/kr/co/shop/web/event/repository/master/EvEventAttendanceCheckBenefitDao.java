package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventAttendanceCheckBenefit;
import kr.co.shop.web.event.repository.master.base.BaseEvEventAttendanceCheckBenefitDao;

@Mapper
public interface EvEventAttendanceCheckBenefitDao extends BaseEvEventAttendanceCheckBenefitDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventAttendanceCheckBenefitDao 클래스에 구현
	 * 되어있습니다. BaseEvEventAttendanceCheckBenefitDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는
	 * 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventAttendanceCheckBenefit selectByPrimaryKey(EvEventAttendanceCheckBenefit evEventAttendanceCheckBenefit)
			throws Exception;

	/**
	 * @Desc : 이벤트 출석체크 혜택
	 * @Method Name : selectAttendanceBenefitByEventNo
	 * @Date : 2019. 5. 21.
	 * @Author : easyhun
	 * @param eventNo
	 * @return
	 * @throws Exception
	 */
	public List<EvEventAttendanceCheckBenefit> selectAttendanceBenefitByEventNo(String eventNo) throws Exception;

}
