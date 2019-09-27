package kr.co.shop.web.board.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.board.model.master.BdBulkBuyInquiry;
import kr.co.shop.web.board.repository.master.base.BaseBdBulkBuyInquiryDao;

@Mapper
public interface BdBulkBuyInquiryDao extends BaseBdBulkBuyInquiryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdBulkBuyInquiryDao 클래스에 구현 되어있습니다.
	 * BaseBdBulkBuyInquiryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdBulkBuyInquiry selectByPrimaryKey(BdBulkBuyInquiry bdBulkBuyInquiry) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : insertBulkBuyInquiry
	 * @Date : 2019. 3. 15.
	 * @Author : Kimyounghyun
	 * @param bdBulkBuyInquiry
	 */
	public void insertBulkBuyInquiry(BdBulkBuyInquiry bdBulkBuyInquiry);

}
