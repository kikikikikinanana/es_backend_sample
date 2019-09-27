package kr.co.shop.web.claim.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.claim.model.master.OcClaimProductApplyPromotion;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimProductApplyPromotionDao;

@Mapper
public interface OcClaimProductApplyPromotionDao extends BaseOcClaimProductApplyPromotionDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcClaimProductApplyPromotionDao 클래스에 구현
	 * 되어있습니다. BaseOcClaimProductApplyPromotionDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는
	 * 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcClaimProductApplyPromotion selectByPrimaryKey(OcClaimProductApplyPromotion ocClaimProductApplyPromotion)
			throws Exception;

	/**
	 * @Desc : 가장 최근 다족구매 프로모션 재조정으로 인한 금액변경 내용 추출
	 * @Method Name : selectRecentClaimProductApplyPromotion
	 * @Date : 2019. 7. 14.
	 * @Author : KTH
	 * @param ocClaimProductApplyPromotion
	 * @return
	 * @throws Exception
	 */
	public OcClaimProductApplyPromotion selectRecentClaimProductApplyPromotion(
			OcClaimProductApplyPromotion ocClaimProductApplyPromotion) throws Exception;
}
