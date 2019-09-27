package kr.co.shop.web.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.product.model.master.DpStoreProduct;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.repository.master.DpStoreProductDao;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreProductService {

	@Autowired
	private DpStoreProductDao dpStoreProductDao;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private ProductService productService;

	/**
	 * @Desc : 매장전용 상품리스트 조회
	 * @Method Name : getStoreProductList
	 * @Date : 2019. 5. 27.
	 * @Author : 백인천
	 * @param pageable
	 * @return
	 */
	public Page<DpStoreProduct> getStoreProductList(Pageable<Object, DpStoreProduct> pageable) throws Exception {

		// 매장전용 상품 총 수량
		int prodTotalCount = dpStoreProductDao.getStoreProductTotalCount(pageable);
		pageable.setTotalCount(prodTotalCount);

		// 매장전용 상품 리스트
		List<DpStoreProduct> prodList = dpStoreProductDao.getStoreProductList(pageable);
		pageable.setContent(prodList);

		return pageable.getPage();

	}

	/**
	 * @Desc : 매장전용 상품리스트 조회
	 * @Method Name : getStoreProductOffList
	 * @Date : 2019. 5. 27.
	 * @Author : 백인천
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> getStoreProductOffList(Parameter<DpStoreProduct> parameter) throws Exception {
		Map<String, Object> result = new HashMap<>();
		PageableProduct<DpStoreProduct, PdProductWrapper> pageableProduct = new PageableProduct<DpStoreProduct, PdProductWrapper>(
				parameter);

		log.debug("parameter.get().getRowsPerPage() > {}", parameter.get().getRowsPerPage());

		Map<String, Integer> totalCount = null;

		pageableProduct.setRowsPerPage(parameter.get().getRowsPerPage());

		String pagingSortType = parameter.get().getPagingSortType() != null ? parameter.get().getPagingSortType()
				: PageableProduct.PAGING_SORT_TYPE_NEWEST;

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setUsePaging(true, pagingSortType, null, null);
		pageableProduct.setPageNum(parameter.get().getPageNum());
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONLY_OFFLINE);

		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageableProduct);

		result.put("productList", productList);

		return result;
	}

}