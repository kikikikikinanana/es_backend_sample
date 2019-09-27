package kr.co.shop.web.order.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderPaymentFailureHistoryDao;
import kr.co.shop.web.order.model.master.OcOrderPaymentFailureHistory;

@Mapper
public interface OcOrderPaymentFailureHistoryDao extends BaseOcOrderPaymentFailureHistoryDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseOcOrderPaymentFailureHistoryDao 클래스에 구현 되어있습니다.
     * BaseOcOrderPaymentFailureHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public OcOrderPaymentFailureHistory selectByPrimaryKey(OcOrderPaymentFailureHistory ocOrderPaymentFailureHistory) throws Exception;

}
