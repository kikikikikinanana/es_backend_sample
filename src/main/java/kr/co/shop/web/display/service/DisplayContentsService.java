package kr.co.shop.web.display.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.display.model.master.DpWebzine;
import kr.co.shop.web.display.model.master.DpWebzineDetailImage;
import kr.co.shop.web.display.repository.master.DpWebzineDao;
import kr.co.shop.web.display.repository.master.DpWebzineDetailImageDao;
import kr.co.shop.web.display.repository.master.DpWebzineProductDao;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DisplayContentsService {

	@Autowired
	private DpWebzineDao dpWebzineDao;

	@Autowired
	private DpWebzineDetailImageDao dpWebzineDetailImageDao;

	@Autowired
	private DpWebzineProductDao dpWebzineProductDao;

	@Autowired
	private ProductService productService;

	/**
	 * 웹진 목록 조회
	 * 
	 * @Desc : 웹진 목록 조회
	 * @Method Name : readDpWebzineList
	 * @Date : 2019. 1. 31.
	 * @Author : SANTA
	 * @param pageable
	 * @return
	 */
	public Page<DpWebzine> getDpWebzineList(Pageable<DpWebzine, DpWebzine> pageable) throws Exception {

		int count = dpWebzineDao.selectDpWebzineCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.setContent(dpWebzineDao.selectDpWebzineList(pageable));
		}

		return pageable.getPage();

	}

	/**
	 * 웹진 조회
	 * 
	 * @Desc : 웹진 조회
	 * @Method Name : readDpWebzine
	 * @Date : 2019. 2. 1.
	 * @Author : SANTA
	 * @param dpWebzine
	 * @return
	 * @throws Exception
	 */
	public DpWebzine getDpWebzine(DpWebzine dpWebzine, Parameter<DpWebzine> parameter) throws Exception {

		DpWebzine result = dpWebzineDao.selectDpWebzine(dpWebzine);

		// TODO: wbznType Const.java 추가 여부 추후
		if (UtilsText.equals(result.getWbznType(), "C")) {
			DpWebzineDetailImage dpWebzineDetailImage = new DpWebzineDetailImage();
			dpWebzineDetailImage.setWbznSeq(result.getWbznSeq());
			List<DpWebzineDetailImage> dpWebzineDetailImages = dpWebzineDetailImageDao.select(dpWebzineDetailImage);
			result.setDpWebzineDetailImages(dpWebzineDetailImages);
		}

		PageableProduct<DpWebzine, PdProductWrapper> pageable = new PageableProduct<DpWebzine, PdProductWrapper>(
				parameter);

		pageable.setCondition(dpWebzine.getSiteNo(), dpWebzine.getChnnlNo());
		pageable.setUseTableMapping("dp_webzine_product", null, new LinkedHashMap<String, Integer>() {
			{
				put("wbzn_seq", result.getWbznSeq());
			}
		});
		pageable.setUsePaging(false, null, null, null);
		Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageable);
//		Page<PdProductWrapper> productList = dpWebzineProductDao.selectDpWebzineProductList(pageable);
		result.setDpWebzineProducts(productList.getContent());

		return result;

	}

	/**
	 * @Desc : OTS 웹진 목록 조회
	 * @Method Name : getOtsWebzineList
	 * @Date : 2019. 6. 5.
	 * @Author : hsjhsj19
	 * @param dpWebzine
	 * @return
	 * @throws Exception
	 */
	public List<DpWebzine> getOtsWebzineList(DpWebzine dpWebzine) throws Exception {
		return this.dpWebzineDao.selectOtsDpWebzine(dpWebzine);
	}
}
