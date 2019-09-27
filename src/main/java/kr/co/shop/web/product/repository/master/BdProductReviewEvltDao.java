package kr.co.shop.web.product.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.BdProductReview;
import kr.co.shop.web.product.model.master.BdProductReviewEvlt;
import kr.co.shop.web.product.repository.master.base.BaseBdProductReviewEvltDao;

@Mapper
public interface BdProductReviewEvltDao extends BaseBdProductReviewEvltDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdProductReviewEvltDao 클래스에 구현 되어있습니다.
	 * BaseBdProductReviewEvltDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public BdProductReviewEvlt selectByPrimaryKey(BdProductReviewEvlt bdProductReviewEvlt) throws Exception;

	/**
	 * @Desc : 상품 후기에 담길 평가 점수 목록
	 * @Method Name : selectProductReviewEvlt
	 * @Date : 2019. 5. 8.
	 * @Author : hsjhsj19
	 * @param bdProductReview
	 * @return
	 * @throws Exception
	 */
	public BdProductReviewEvlt[] selectProductReviewEvlt(BdProductReview bdProductReview) throws Exception;

}
