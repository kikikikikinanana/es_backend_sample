package kr.co.shop.web.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.product.model.master.DpBrandPage;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdBrandProductWrapper;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.repository.master.DpBrandPageDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrandPageService {

	@Autowired
	private DpBrandPageDao dpBrandPageDao;

	/**
	 * @Desc : 브랜드 페이지 프로모션 조회
	 * @Method Name : getBrandPromotion
	 * @Date : 2019. 6. 13.
	 * @Author : 백인천
	 * @param dpBrandPage
	 * @return
	 */
	public Map<String, Object> getBrandPromotion(DpBrandPage dpBrandPage) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 브렌드 페이지 조회
		DpBrandPage brandPromotion = dpBrandPageDao.selectByPrimaryKey(dpBrandPage);

		result.put("brandPromotion", brandPromotion);

		return result;
	}

	/**
	 * @Desc : 브랜드 페이지 비주얼 리스트 조회
	 * @Method Name : getBrandPageVisualList
	 * @Date : 2019. 6. 17.
	 * @Author : 백인천
	 * @param dpBrandPage
	 * @return
	 */
	public Map<String, List<DpBrandPage>> getBrandPageVisualList(DpBrandPage dpBrandPage) throws Exception {
		Map<String, List<DpBrandPage>> result = new HashMap<>();
		List<DpBrandPage> brandVisual = new ArrayList<DpBrandPage>();
		// 브렌드 페이지 조회
		brandVisual = dpBrandPageDao.getBrandPageVisualList(dpBrandPage);

		result.put("brandVisual", brandVisual);

		return result;
	}

	/**
	 * @Desc : 브랜드 페이지 상품 조회
	 * @Method Name : getBrandProductList
	 * @Date : 2019. 6. 18.
	 * @Author : 백인천
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBrandProductList(Parameter<DpBrandPage> parameter) throws Exception {
		Map<String, Object> result = new HashMap<>();
		PageableProduct<DpBrandPage, PdBrandProductWrapper> pageableProduct = new PageableProduct<DpBrandPage, PdBrandProductWrapper>(
				parameter);

		log.debug("parameter.get().getRowsPerPage() > {}", parameter.get().getRowsPerPage());

		// DpCategory category = dpCategoryDao.selectDpCategory(dpCategory);

		Map<String, Integer> totalCount = null;

		pageableProduct.setRowsPerPage(parameter.get().getRowsPerPage());
		// pageableProduct.setRowsPerPage(5);
		pageableProduct.setBean(parameter.get());

		String pagingSortType = parameter.get().getPagingSortType() != null ? parameter.get().getPagingSortType()
				: PageableProduct.PAGING_SORT_TYPE_NEWEST;

		log.debug("Condition >> " + parameter.get().getSiteNo() + " / " + parameter.get().getChnnlNo());
		pageableProduct.setCondition(parameter.get().getSiteNo(), parameter.get().getChnnlNo());
		pageableProduct.setUsePaging(true, pagingSortType, null, null);
		pageableProduct.setPageNum(parameter.get().getPageNum());
		log.debug("CtgrNo >> {}", parameter.get().getCtgrNo());
		if (parameter.get().getCtgrNo() != null) {
			pageableProduct.setConditionCategoryNo(parameter.get().getCtgrNo());
		}

		totalCount = dpBrandPageDao.getBrandProductCount(pageableProduct);
		log.debug("totalCount : " + totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		List<PdBrandProductWrapper> brandItemList = dpBrandPageDao.getBrandProductList(pageableProduct);
		pageableProduct.setContent(brandItemList);

		result.put("totalCount", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		result.put("brandItemList", brandItemList);

		return result;
	}

	/**
	 * @Desc : 브랜드 위클리 베스트 조회
	 * @Method Name : getWeeklyList
	 * @Date : 2019. 6. 27.
	 * @Author : 백인천
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> getWeeklyList(Parameter<PdProduct> parameter) throws Exception {
		Map<String, Object> result = new HashMap<>();
		PageableProduct<PdProduct, PdProductWrapper> pageableProduct = new PageableProduct<PdProduct, PdProductWrapper>(
				parameter);

		log.debug("parameter.get().getRowsPerPage() > {}", parameter.get().getRowsPerPage());

		pageableProduct.setRowsPerPage(5);
		pageableProduct.setBean(parameter.get());

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_NEWEST, null, null);
		pageableProduct.setPageNum(parameter.get().getPageNum());
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONOFFLINE);

		List<PdProductWrapper> weeklyList = dpBrandPageDao.getWeeklyBestProduct(pageableProduct);
		pageableProduct.setContent(weeklyList);

		result.put("weeklyList", weeklyList);

		return result;
	}

	/**
	 * @Desc : 브랜드 베스트4 상품 조회
	 * @Method Name : getBrandProductBest
	 * @Date : 2019. 6. 18.
	 * @Author : 백인천
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBrandProductBest(String brandNo) throws Exception {
		Map<String, Object> result = new HashMap<>();
		DpBrandPage param = new DpBrandPage();
		param.setBrandNo(brandNo);

		List<PdBrandProductWrapper> bestProductList = dpBrandPageDao.getBrandProductBest(param);

		result.put("bestProductList", bestProductList);

		return result;
	}

}
