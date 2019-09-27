package kr.co.shop.web.order.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcCashReceipt;
import kr.co.shop.web.order.repository.master.base.BaseOcCashReceiptDao;

@Mapper
public interface OcCashReceiptDao extends BaseOcCashReceiptDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcCashReceiptDao 클래스에 구현 되어있습니다.
	 * BaseOcCashReceiptDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcCashReceipt selectByPrimaryKey(OcCashReceipt ocCashReceipt) throws Exception;

	/**
	 * @Desc : 현금영수증 정보 insert
	 * @Method Name : insertCashReceipt
	 * @Date : 2019. 7. 12.
	 * @Author : KTH
	 * @param ocCashReceipt
	 * @return
	 * @throws Exception
	 */
	public void insertCashReceipt(OcCashReceipt ocCashReceipt) throws Exception;

}
