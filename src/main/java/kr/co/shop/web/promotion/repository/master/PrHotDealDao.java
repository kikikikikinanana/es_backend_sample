package kr.co.shop.web.promotion.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PrPromotionProduct;
import kr.co.shop.web.product.model.master.PrPromotionProductWrapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrPromotionDao;

@Mapper
public interface PrHotDealDao extends BasePrPromotionDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePrPromotionDao 클래스에 구현 되어있습니다.
	 * BasePrPromotionDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	/**
	 * @Desc : 타임특가할인 진행중 상품 목록 조회
	 * @Method Name : getRelHotDealList
	 * @Date : 2019. 6. 20.
	 * @Author : 백인천
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotionProductWrapper> getRelHotDealList(
			PageableProduct<PrPromotionProduct, PrPromotionProductWrapper> pageableProduct) throws Exception;

	/**
	 * @Desc : 타임특가할인 대기중 상품 목록 카운트
	 * @Method Name : getWaitHotDealCount
	 * @Date : 2019. 6. 20.
	 * @Author : 백인천
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> getWaitHotDealCount(
			PageableProduct<PrPromotionProduct, PrPromotionProductWrapper> pageableProduct) throws Exception;

	/**
	 * @Desc : 타임특가할인 대기중 상품 목록 조회
	 * @Method Name : getWaitHotDealList
	 * @Date : 2019. 6. 20.
	 * @Author : 백인천
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotionProductWrapper> getWaitHotDealList(
			PageableProduct<PrPromotionProduct, PrPromotionProductWrapper> pageableProduct) throws Exception;
}
