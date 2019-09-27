package kr.co.shop.web.order.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderCrtfcHistoryDao;
import kr.co.shop.web.order.model.master.OcOrderCrtfcHistory;

@Mapper
public interface OcOrderCrtfcHistoryDao extends BaseOcOrderCrtfcHistoryDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseOcOrderCrtfcHistoryDao 클래스에 구현 되어있습니다.
     * BaseOcOrderCrtfcHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public OcOrderCrtfcHistory selectByPrimaryKey(OcOrderCrtfcHistory ocOrderCrtfcHistory) throws Exception;

}
