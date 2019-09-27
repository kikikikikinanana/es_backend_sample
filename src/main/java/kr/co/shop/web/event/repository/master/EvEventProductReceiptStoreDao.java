package kr.co.shop.web.event.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventProductReceiptStore;
import kr.co.shop.web.event.model.master.EvEventTargetProduct;
import kr.co.shop.web.event.repository.master.base.BaseEvEventProductReceiptStoreDao;

@Mapper
public interface EvEventProductReceiptStoreDao extends BaseEvEventProductReceiptStoreDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventProductReceiptStoreDao 클래스에 구현
	 * 되어있습니다. BaseEvEventProductReceiptStoreDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventProductReceiptStore selectByPrimaryKey(EvEventProductReceiptStore evEventProductReceiptStore)
			throws Exception;

	/**
	 * 이벤트 드로우 대상 매장
	 * 
	 * @Desc :
	 * @Method Name : selectEventProductReceiptStoreByEventNo
	 * @Date : 2019. 6. 07
	 * @Author : easyhun
	 * @param
	 * @throws Exception
	 */
	public EvEventTargetProduct selectEventProductReceiptStoreByEventNo(String eventNo) throws Exception;

}
