package kr.co.shop.web.afterService.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.afterService.model.master.OcAsAcceptProductStatusHistory;
import kr.co.shop.web.afterService.repository.master.base.BaseOcAsAcceptProductStatusHistoryDao;

@Mapper
public interface OcAsAcceptProductStatusHistoryDao extends BaseOcAsAcceptProductStatusHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcAsAcceptProductStatusHistoryDao 클래스에 구현
	 * 되어있습니다. BaseOcAsAcceptProductStatusHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는
	 * 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcAsAcceptProductStatusHistory selectByPrimaryKey(
			OcAsAcceptProductStatusHistory ocAsAcceptProductStatusHistory) throws Exception;

	public String selectAsAcceptPrdtStatHistSeqNextVal(String asAcceptNo, Short asAcceptPrdtSeq) throws Exception;
}
