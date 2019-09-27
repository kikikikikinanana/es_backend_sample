package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventResultBenefitMember;
import kr.co.shop.web.event.repository.master.base.BaseEvEventResultBenefitMemberDao;

@Mapper
public interface EvEventResultBenefitMemberDao extends BaseEvEventResultBenefitMemberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventResultBenefitMemberDao 클래스에 구현
	 * 되어있습니다. BaseEvEventResultBenefitMemberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventResultBenefitMember selectByPrimaryKey(EvEventResultBenefitMember evEventResultBenefitMember)
			throws Exception;

	/**
	 * @Desc : 이벤트 결과 회원 리스트
	 * @Method Name : selectEventResultBeneftMemberList
	 * @Date : 2019. 5. 14.
	 * @Author : easyhun
	 * @param evEventResultBenefitMember
	 * @return
	 * @throws Exception
	 */
	public List<EvEventResultBenefitMember> selectEventResultBeneftMemberList(
			EvEventResultBenefitMember evEventResultBenefitMember) throws Exception;

}
