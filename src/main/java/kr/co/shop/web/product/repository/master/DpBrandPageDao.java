package kr.co.shop.web.product.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.DpBrandPage;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdBrandProductWrapper;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.repository.master.base.BaseDpBrandPageDao;

@Mapper
public interface DpBrandPageDao extends BaseDpBrandPageDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpBrandDao 클래스에 구현 되어있습니다. BaseDpBrandDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpBrandPage selectByPrimaryKey(DpBrandPage dpBrandPage) throws Exception;

	public List<DpBrandPage> getBrandPageVisualList(DpBrandPage dpBrandPage) throws Exception;

	public Map<String, Integer> getBrandProductCount(
			PageableProduct<DpBrandPage, PdBrandProductWrapper> pageableProduct) throws Exception;

	public List<PdBrandProductWrapper> getBrandProductList(
			PageableProduct<DpBrandPage, PdBrandProductWrapper> pageableProduct) throws Exception;

	public List<PdProductWrapper> getWeeklyBestProduct(PageableProduct<PdProduct, PdProductWrapper> pageableProduct)
			throws Exception;

	public List<PdBrandProductWrapper> getBrandProductBest(DpBrandPage param) throws Exception;

}
