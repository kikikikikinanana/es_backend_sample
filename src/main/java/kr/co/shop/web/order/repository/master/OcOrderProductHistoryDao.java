package kr.co.shop.web.order.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrderProductHistory;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderProductHistoryDao;

@Mapper
public interface OcOrderProductHistoryDao extends BaseOcOrderProductHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderProductHistoryDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderProductHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public OcOrderProductHistory selectByPrimaryKey(OcOrderProductHistory ocOrderProductHistory) throws Exception;

	/**
	 * 
	 * @Desc : 주문상품 이력 변경 등록
	 * @Method Name : insertProductHistory
	 * @Date : 2019. 6. 13.
	 * @Author : NKB
	 * @param prodHistoryList
	 * @return
	 * @throws Exception
	 */
	public int insertProductHistory(OcOrderProductHistory prodHistoryList) throws Exception;

}
