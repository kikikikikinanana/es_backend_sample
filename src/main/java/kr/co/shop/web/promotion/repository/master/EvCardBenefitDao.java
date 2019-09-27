package kr.co.shop.web.promotion.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.promotion.model.master.EvCardBenefit;
import kr.co.shop.web.promotion.repository.master.base.BaseEvCardBenefitDao;

@Mapper
public interface EvCardBenefitDao extends BaseEvCardBenefitDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvCardBenefitDao 클래스에 구현 되어있습니다.
	 * BaseEvCardBenefitDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvCardBenefit selectByPrimaryKey(EvCardBenefit evCardBenefit) throws Exception;

	/**
	 * @Desc : 현재 날짜로 적용중인 카드사 관리 혜택을 조회
	 * @Method Name : selectApplyingCardBenefitList
	 * @Date : 2019. 5. 23.
	 * @Author : tennessee
	 * @return
	 * @throws Exception
	 */
	public List<EvCardBenefit> selectApplyingCardBenefitList() throws Exception;

}
