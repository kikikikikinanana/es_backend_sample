package kr.co.shop.web.product.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.product.model.master.BdProductReview;
import kr.co.shop.web.product.repository.master.base.BaseBdProductReviewDao;

@Mapper
public interface BdProductReviewDao extends BaseBdProductReviewDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdProductReviewDao 클래스에 구현 되어있습니다.
	 * BaseBdProductReviewDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdProductReview selectByPrimaryKey(BdProductReview bdProductReview) throws Exception;

	/**
	 * @Desc : 상품 후기 목록 갯수 조회
	 * @Method Name : selectProductReviewCount
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Integer selectProductReviewCount(Pageable<Object, BdProductReview> pageable) throws Exception;

	/**
	 * @Desc : 상품 후기 목록
	 * @Method Name : selectProductReview
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<BdProductReview> selectProductReview(Pageable<Object, BdProductReview> pageable) throws Exception;

	/**
	 * @Desc : 상품 후기 코드별 비율 조회
	 * @Method Name : selectProductReviewPercentageList
	 * @Date : 2019. 5. 7.
	 * @Author : hsjhsj19
	 * @param productReview
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectProductReviewPercentageList(BdProductReview productReview) throws Exception;

}
