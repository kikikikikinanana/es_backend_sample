package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrderProductApplyPromotion;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderProductApplyPromotionDao;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderPromotion;

@Mapper
public interface OcOrderProductApplyPromotionDao extends BaseOcOrderProductApplyPromotionDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderProductApplyPromotionDao 클래스에 구현
	 * 되어있습니다. BaseOcOrderProductApplyPromotionDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는
	 * 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcOrderProductApplyPromotion selectByPrimaryKey(OcOrderProductApplyPromotion ocOrderProductApplyPromotion)
			throws Exception;

	public void insertPromotionList(List<OrderPromotion> orderPromotion) throws Exception;

}
