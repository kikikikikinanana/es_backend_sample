package kr.co.shop.web.product.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.product.model.master.BdProductInquiry;
import kr.co.shop.web.product.repository.master.base.BaseBdProductInquiryDao;

@Mapper
public interface BdProductInquiryDao extends BaseBdProductInquiryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdProductInquiryDao 클래스에 구현 되어있습니다.
	 * BaseBdProductInquiryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdProductInquiry selectByPrimaryKey(BdProductInquiry bdProductInquiry) throws Exception;

	/**
	 * @Desc : 상품 문의 목록 갯수
	 * @Method Name : selectProductInquiryCount
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Integer selectProductInquiryCount(Pageable<Object, BdProductInquiry> pageable) throws Exception;

	/**
	 * @Desc : 상품 문의 목록 조회
	 * @Method Name : selectProductInquiry
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<BdProductInquiry> selectProductInquiry(Pageable<Object, BdProductInquiry> pageable) throws Exception;

	/**
	 * @Desc : 상품 문의 수정
	 * @Method Name : updateProductInquiry
	 * @Date : 2019. 5. 24.
	 * @Author : hsjhsj19
	 * @param bdProductInquiry
	 * @return
	 * @throws Exception
	 */
	public int updateProductInquiry(BdProductInquiry bdProductInquiry) throws Exception;

	/**
	 * @Desc : 상품 문의 삭제
	 * @Method Name : deleteProductInquiry
	 * @Date : 2019. 5. 24.
	 * @Author : hsjhsj19
	 * @param bdProductInquiry
	 * @return
	 * @throws Exception
	 */
	public int deleteProductInquiry(BdProductInquiry bdProductInquiry) throws Exception;

	/**
	 * @Desc : 상품 문의 등록
	 * @Method Name : insertProductInquiry
	 * @Date : 2019. 6. 18.
	 * @Author : hsjhsj19
	 * @param bdProductInquiry
	 * @return
	 * @throws Exception
	 */
	public int insertProductInquiry(BdProductInquiry bdProductInquiry) throws Exception;

}
