package kr.co.shop.web.promotion.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.CartProductSearchVO;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductPromotion;
import kr.co.shop.web.product.model.master.PrPromotionProduct;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.promotion.repository.master.base.BasePrPromotionDao;

@Mapper
public interface PrPromotionDao extends BasePrPromotionDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePrPromotionDao 클래스에 구현 되어있습니다.
	 * BasePrPromotionDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PrPromotion selectByPrimaryKey(PrPromotion prPromotion) throws Exception;

	/**
	 * @Desc : 즉시할인,타임특가할인,제휴사할인 목록 조회(상품,카테고리,브랜드)
	 * @Method Name : getPromotionDscntList
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param promotion
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotion> getPromotionDscntList(PrPromotion promotion) throws Exception;

	/**
	 * @Desc : 다족구매할인 목록 조회
	 * @Method Name : getPromotionMultiBuyDiscountList
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param promotion
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotion> getPromotionMultiBuyDiscountList(PrPromotion promotion) throws Exception;

	/**
	 * @Desc : 적용상품 프로모션과 관련있는 사은품 목록 조회
	 * @Method Name : getPromotionFreeGiftList
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param promotion
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotion> getPromotionFreeGiftList(PrPromotion promotion) throws Exception;

	/**
	 * @Desc : 프로모션 조회 가격 계산 포함
	 * @Method Name : selectPromotionByPrimaryKey
	 * @Date : 2019. 5. 21.
	 * @Author : hsjhsj19
	 * @param promotion
	 * @return
	 * @throws Exception
	 */
	public PrPromotion selectPromotionByPrimaryKey(PrPromotion promotion) throws Exception;

	/**
	 * @Desc : 상품 프로모션 가격비교를 위한 상품 목록 조회
	 * @Method Name : selectPromotiontWithAllList
	 * @Date : 2019. 6. 7.
	 * @Author : hsjhsj19
	 * @param cartPrdSearchVo
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotion> selectPromotiontWithAllList(CartProductSearchVO cartPrdSearchVo) throws Exception;

	/**
	 * @Desc : 한 상품에 대한 적용 프로모션 조회. 최저가순으로 조회.(중복되는 경우, 프로모션기간 긴 것이 우선됨)
	 * @Method Name : selectPromotionByPrdtNo
	 * @Date : 2019. 6. 12.
	 * @Author : tennessee
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductPromotion> selectPromotionByPrdtNo(String prdtNo) throws Exception;

	/**
	 * @Desc : 타임특가할인 상품 목록 조회
	 * @Method Name : getPromotionDscntList
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param promotion
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotionProduct> getHotDealList(PrPromotionProduct promotion) throws Exception;

	/**
	 * @Desc : 상품번호에 해당하는 사은픔 프로모션정보 및 사은품정보 조회
	 * @Method Name : selectPromotionOfGiftByPrdtNo
	 * @Date : 2019. 7. 10.
	 * @Author : tennessee
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProduct> selectPromotionOfGiftByPrdtNo(String prdtNo) throws Exception;
}
