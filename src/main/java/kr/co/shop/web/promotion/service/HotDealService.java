package kr.co.shop.web.promotion.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PrPromotionProduct;
import kr.co.shop.web.product.model.master.PrPromotionProductWrapper;
import kr.co.shop.web.promotion.repository.master.PrHotDealDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HotDealService {

	@Autowired
	private PrHotDealDao hotDealDao;

	/**
	 * @Desc : 진행중 핫딜 프로모션 리스트 조회
	 * @Method Name : getRelHotDealList
	 * @Date : 2019. 6. 20.
	 * @Author : 백인천
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getRelHotDealList(Parameter<PrPromotionProduct> parameter) throws Exception {
		Map<String, Object> result = new HashMap<>();
		PageableProduct<PrPromotionProduct, PrPromotionProductWrapper> pageableProduct = new PageableProduct<PrPromotionProduct, PrPromotionProductWrapper>(
				parameter);

		log.debug("parameter.get().getRowsPerPage() > {}", parameter.get().getRowsPerPage());

		Map<String, Integer> totalCount = null;

		// pageableProduct.setRowsPerPage(parameter.get().getRowsPerPage());
		pageableProduct.setRowsPerPage(3);
		pageableProduct.setBean(parameter.get());

		// String pagingSortType = parameter.get().getPagingSortType() != null ?
		// parameter.get().getPagingSortType() :
		// PageableProduct.PAGING_SORT_TYPE_NEWEST;

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_NEWEST, null, null);
		pageableProduct.setPageNum(parameter.get().getPageNum());
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONOFFLINE);

		List<PrPromotionProductWrapper> relHotDealList = hotDealDao.getRelHotDealList(pageableProduct);
		pageableProduct.setContent(relHotDealList);

		// Map<String, Object> brandBestProduct = getBrandProductBest("000003");
		// log.debug("brandBestProduct > {}", brandBestProduct);

		result.put("relHotDealList", relHotDealList);

		return result;
	}

	/**
	 * @Desc : 대기중 핫딜 프로모션 리스트 조회
	 * @Method Name : getWaitHotDealList
	 * @Date : 2019. 6. 20.
	 * @Author : 백인천
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getWaitHotDealList(Parameter<PrPromotionProduct> parameter) throws Exception {
		Map<String, Object> result = new HashMap<>();
		PageableProduct<PrPromotionProduct, PrPromotionProductWrapper> pageableProduct = new PageableProduct<PrPromotionProduct, PrPromotionProductWrapper>(
				parameter);

		log.debug("parameter.get().getRowsPerPage() > {}", parameter.get().getRowsPerPage());

		Map<String, Integer> totalCount = null;

		// pageableProduct.setRowsPerPage(parameter.get().getRowsPerPage());
		pageableProduct.setRowsPerPage(40);
		pageableProduct.setBean(parameter.get());

		// String pagingSortType = parameter.get().getPagingSortType() != null ?
		// parameter.get().getPagingSortType() :
		// PageableProduct.PAGING_SORT_TYPE_NEWEST;

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_NEWEST, null, null);
		pageableProduct.setPageNum(parameter.get().getPageNum());
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONOFFLINE);

		totalCount = hotDealDao.getWaitHotDealCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		List<PrPromotionProductWrapper> waitHotDealList = hotDealDao.getWaitHotDealList(pageableProduct);
		pageableProduct.setContent(waitHotDealList);

		// Map<String, Object> brandBestProduct = getBrandProductBest("000003");
		// log.debug("brandBestProduct > {}", brandBestProduct);

		result.put("waitHotDealList", waitHotDealList);

		return result;
	}
}
