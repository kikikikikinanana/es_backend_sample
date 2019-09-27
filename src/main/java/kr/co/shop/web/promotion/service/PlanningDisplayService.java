package kr.co.shop.web.promotion.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplay;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplayCorner;
import kr.co.shop.web.promotion.repository.master.PrPlanningDisplayCornerDao;
import kr.co.shop.web.promotion.repository.master.PrPlanningDisplayDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlanningDisplayService {

	@Autowired
	PrPlanningDisplayDao prPlanningDisplayDao;

	@Autowired
	PrPlanningDisplayCornerDao prPlanningDisplayCornerDao;

	@Autowired
	ProductService productService;

	/**
	 * @Desc : 기획전 목록 조회
	 * @Method Name : getPrPlanningDisplayList
	 * @Date : 2019. 4. 29.
	 * @Author : 이가영
	 * @param pageable
	 * @return
	 */
	public Page<PrPlanningDisplay> getPrPlanningDisplayList(Pageable<PrPlanningDisplay, PrPlanningDisplay> pageable)
			throws Exception {

		int count = prPlanningDisplayDao.selectPrPlanningDisplayListCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.setContent(prPlanningDisplayDao.selectPrPlanningDisplayList(pageable));
		}

		return pageable.getPage();
	}

	/**
	 * @Desc : 기획전 상세 조회
	 * @Method Name : getPrPlanningDisplayDetail
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param prPlanningDisplay
	 * @return
	 */
	public PrPlanningDisplay getPrPlanningDisplayDetail(PrPlanningDisplay prPlanningDisplay) throws Exception {

		return prPlanningDisplayDao.selectPrPlanningDisplayDetail(prPlanningDisplay);
	}

	/**
	 * @Desc : 기획전 상세 코너 리스트 조회
	 * @Method Name : getPrPlanningDisplayCornerList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param prPlanningDisplay
	 * @return
	 */
	public List<PrPlanningDisplayCorner> getPrPlanningDisplayCornerList(Parameter<PrPlanningDisplay> parameter)
			throws Exception {

		PrPlanningDisplay prPlanningDisplay = parameter.get();

		List<PrPlanningDisplayCorner> result = prPlanningDisplayCornerDao
				.selectPrPlanningDisplayCornerList(prPlanningDisplay);

		String siteNo = prPlanningDisplay.getSiteNo();
		String chnnlNo = prPlanningDisplay.getChnnlNo();

		for (PrPlanningDisplayCorner corner : result) {

			PageableProduct<PrPlanningDisplay, PdProductWrapper> pageable = new PageableProduct<PrPlanningDisplay, PdProductWrapper>(
					parameter);

			pageable.setCondition(siteNo, chnnlNo);
			pageable.setUseTableMapping("pr_planning_display_corner_product", null,
					new LinkedHashMap<String, Integer>() {
						{
							put("plndp_corner_seq", corner.getPlndpCornerSeq());
						}
					});
			pageable.setUsePaging(false, null, null, null);
			Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageable);
			corner.setPrPlanningDisplayCornerProducts(productList.getContent());
		}

		return result;
	}

	/**
	 * @Desc : 기획전 상세 코너 리스트 조회
	 * @Method Name : getPrPlanningDisplayCornerList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param prPlanningDisplay
	 * @return
	 */
	public Page<PdProductWrapper> getPrPlanningDisplayProductList(Parameter<PrPlanningDisplay> parameter)
			throws Exception {

		PrPlanningDisplay prPlanningDisplay = parameter.get();

		String siteNo = prPlanningDisplay.getSiteNo();
		String chnnlNo = prPlanningDisplay.getChnnlNo();

		PageableProduct<PrPlanningDisplay, PdProductWrapper> pageable = new PageableProduct<PrPlanningDisplay, PdProductWrapper>(
				parameter);

		pageable.setCondition(siteNo, chnnlNo);
		pageable.setUseTableMapping("pr_planning_display_product", new LinkedHashMap<String, String>() {
			{
				put("plndp_no", prPlanningDisplay.getPlndpNo());
			}
		}, null);
		pageable.setUsePaging(false, null, null, null);
		Page<PdProductWrapper> result = this.productService.getDisplayProductList(pageable);

		return result;
	}
}
