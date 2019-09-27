package kr.co.shop.web.event.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventTargetGrade;
import kr.co.shop.web.event.repository.master.base.BaseEvEventTargetGradeDao;

@Mapper
public interface EvEventTargetGradeDao extends BaseEvEventTargetGradeDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventTargetGradeDao 클래스에 구현 되어있습니다.
	 * BaseEvEventTargetGradeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventTargetGrade selectByPrimaryKey(EvEventTargetGrade evEventTargetGrade) throws Exception;

	/**
	 * 이벤트 대상 회원 조회
	 * 
	 * @Desc :
	 * @Method Name : selectEventTargetGrade
	 * @Date : 2019. 5. 16
	 * @Author : easyhun
	 * @param pageable
	 * @throws Exception
	 */
	public EvEventTargetGrade selectEventTargetGrade(EvEventTargetGrade evEventTargetGrade) throws Exception;

}
