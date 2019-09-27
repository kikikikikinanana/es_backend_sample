package kr.co.shop.web.product.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.BdProductReview;
import kr.co.shop.web.product.model.master.BdProductReviewImage;
import kr.co.shop.web.product.repository.master.base.BaseBdProductReviewImageDao;

@Mapper
public interface BdProductReviewImageDao extends BaseBdProductReviewImageDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdProductReviewImageDao 클래스에 구현 되어있습니다.
	 * BaseBdProductReviewImageDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public BdProductReviewImage selectByPrimaryKey(BdProductReviewImage bdProductReviewImage) throws Exception;

	/**
	 * @Desc : 상품 후기에 담길 이밎 목록 조회
	 * @Method Name : selectProductReviewImage
	 * @Date : 2019. 5. 8.
	 * @Author : hsjhsj19
	 * @param bdProductReview
	 * @return
	 * @throws Exception
	 */
	public BdProductReviewImage[] selectProductReviewImage(BdProductReview bdProductReview) throws Exception;

}
