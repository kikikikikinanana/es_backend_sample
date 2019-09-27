/**
 * 
 */
package kr.co.shop.web.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.board.model.master.BdBulkBuyInquiry;
import kr.co.shop.web.board.repository.master.BdBulkBuyInquiryDao;

/**
 * @Desc :
 * @FileName : BulkBuyInquiryService.java
 * @Project : shop.backend
 * @Date : 2019. 3. 15.
 * @Author : Kimyounghyun
 */
@Service
public class BulkBuyInquiryService {

	@Autowired
	BdBulkBuyInquiryDao bdBulkBuyInquiryDao;

	/**
	 * @Desc : 단체주문문의 저장
	 * @Method Name : setBulkBuyInquiry
	 * @Date : 2019. 3. 15.
	 * @Author : Kimyounghyun
	 * @param bdBulkBuyInquiry
	 * @throws Exception
	 */
	public void setBulkBuyInquiry(BdBulkBuyInquiry bdBulkBuyInquiry) throws Exception {
		bdBulkBuyInquiryDao.insertBulkBuyInquiry(bdBulkBuyInquiry);
	}

}
