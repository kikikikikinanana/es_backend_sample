package kr.co.shop.web.test.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.test.vo.CartPrdtSearchVO;

@Mapper
public interface TestDao {

	public List<PdProductWithAll> selectProductList(List<OcCart> ocCart) throws Exception;

	public List<PrPromotion> selectPromotionDscntList(List<OcCart> ocCart) throws Exception;

	public List<PdProductWithAll> selectProductList1(CartPrdtSearchVO cartPrdSearchVo) throws Exception;

	public List<PrPromotion> selectPromotionDscntList1(CartPrdtSearchVO cartPrdSearchVo) throws Exception;

	public List<DpCategory> selectDpCategoryList(DpCategory dpCategory) throws Exception;

}
