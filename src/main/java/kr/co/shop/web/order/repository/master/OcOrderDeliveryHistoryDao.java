package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderDeliveryHistory;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderDeliveryHistoryDao;

@Mapper
public interface OcOrderDeliveryHistoryDao extends BaseOcOrderDeliveryHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderDeliveryHistoryDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderDeliveryHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public OcOrderDeliveryHistory selectByPrimaryKey(OcOrderDeliveryHistory ocOrderDeliveryHistory) throws Exception;

	/**
	 * @Desc : 주문 배송 이력 저장
	 * @Method Name : insertDeliveryList
	 * @Date : 2019. 5. 28.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrderDeliveryHistory
	 * @throws Exception
	 */
	public void insertDeliveryList(List<OcOrderDeliveryHistory> ocOrderDeliveryHistory) throws Exception;

	/**
	 * @Desc : 주문 배송 이력 상태 일괄 update
	 * @Method Name : updateOrderStat
	 * @Date : 2019. 6. 4.
	 * @Author : ljyoung@3top.co.kr
	 * @param order
	 * @throws Exception
	 */
	public void updateOrderStat(OcOrder order) throws Exception;

	/**
	 * @Desc :배송지 변경
	 * @Method Name : updateOcOrderDeliveryHistoryModify
	 * @Date : 2019. 6. 11.
	 * @Author : lee
	 * @param ocOrderDeliveryHistory
	 * @return
	 */
	public int updateOcOrderDeliveryHistoryModify(OcOrderDeliveryHistory ocOrderDeliveryHistory);

	/**
	 * @Desc : 배송지 변경 히스토리 조회 orderNo 기준으로 전체조회
	 * @Method Name : selectOcOrderDeliveryHistoryList
	 * @Date : 2019. 6. 11.
	 * @Author : lee
	 * @param ocOrderDeliveryHistory
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderDeliveryHistory> selectOcOrderDeliveryHistoryList(OcOrderDeliveryHistory ocOrderDeliveryHistory)
			throws Exception;

	/**
	 * 
	 * @Desc :배송상태 구매확정으로 변경 처리
	 * @Method Name : updateOrderConfirm
	 * @Date : 2019. 6. 12.
	 * @Author : NKB
	 * @param order
	 * @throws Exception
	 */
	public int updateOrderConfirm(OcOrderDeliveryHistory ocOrderDeliveryHistory) throws Exception;

	/**
	 * @Desc : 배송상태 일괄/개별 update 쿼리
	 * @Method Name : updateOrderDeliveryHistoryStat
	 * @Date : 2019. 7. 7.
	 * @Author : KTH
	 * @param ocOrderDeliveryHistory
	 * @return
	 * @throws Exception
	 */
	public int updateOrderDeliveryHistoryStat(OcOrderDeliveryHistory ocOrderDeliveryHistory) throws Exception;

	public OcOrderDeliveryHistory selectOcOrderDeliveryHistory(OcOrderDeliveryHistory ocOrderDeliveryHistory)
			throws Exception;

}
