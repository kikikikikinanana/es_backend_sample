package kr.co.shop.web.afterService.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.afterService.model.master.OcAsAccept;
import kr.co.shop.web.afterService.repository.master.base.BaseOcAsAcceptDao;

@Mapper
public interface OcAsAcceptDao extends BaseOcAsAcceptDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcAsAcceptDao 클래스에 구현 되어있습니다.
	 * BaseOcAsAcceptDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcAsAccept selectByPrimaryKey(OcAsAccept ocAsAccept) throws Exception;

	public void insertOcAsAccept(OcAsAccept ocAsAccept);

	public OcAsAccept selectOcAsAcceptDetailInfo(OcAsAccept ocAsAccept) throws Exception;

	public void updateOcAsAcceptWithdrawal(OcAsAccept ocAsAccept);

	public void updateOcAsAccept(OcAsAccept ocAsAccept);
}
