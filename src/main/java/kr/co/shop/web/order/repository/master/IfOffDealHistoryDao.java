package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.IfOffDealHistory;
import kr.co.shop.web.order.repository.master.base.BaseIfOffDealHistoryDao;

@Mapper
public interface IfOffDealHistoryDao extends BaseIfOffDealHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseIfOffDealHistoryDao 클래스에 구현 되어있습니다.
	 * BaseIfOffDealHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public IfOffDealHistory selectByPrimaryKey(IfOffDealHistory ifOffDealHistory) throws Exception;

	/**
	 * 
	 * @Desc : 오프라인 주문 목록
	 * @Method Name : selectOfflineOrderProductList
	 * @Date : 2019. 5. 31.
	 * @Author : lee
	 * @param ifOffDealHistory
	 * @return
	 * @throws Exception
	 */
	public List<IfOffDealHistory> selectOfflineOrderProductList(IfOffDealHistory ifOffDealHistory) throws Exception;

	public int updateOfflineOrderBuyFix(IfOffDealHistory ifOffDealHistory);
}
