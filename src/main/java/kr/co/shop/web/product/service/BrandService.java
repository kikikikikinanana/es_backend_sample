package kr.co.shop.web.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.product.model.master.DpBrand;
import kr.co.shop.web.product.repository.master.DpBrandDao;
import kr.co.shop.web.product.repository.master.DpBrandStyleDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrandService {

	@Autowired
	private DpBrandDao dpBrandDao;

	@Autowired
	private DpBrandStyleDao dpBrandStyleDao;

	/**
	 * @Desc : 매장전용상품 브랜드 조회
	 * @Method Name : getStoreProductList
	 * @Date : 2019. 5. 27.
	 * @Author : 백인천
	 * @param dpBrand
	 * @return
	 */
	public Map<String, Object> getStoreBrandList(DpBrand dpBrand) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 매장전용 상품존 브랜드 리스트
		List<DpBrand> brdList = dpBrandDao.getStoreBrandList(dpBrand);

		result.put("brdList", brdList);

		return result;
	}

	/**
	 * @Desc : 브랜드 페이지 조회
	 * @Method Name : getBrandPageLoad
	 * @Date : 2019. 6. 13.
	 * @Author : 백인천
	 * @param dpBrand
	 * @return
	 */
	public Map<String, Object> getBrandPageLoad(DpBrand dpBrand) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 브렌드 페이지 조회
		DpBrand brdList = dpBrandDao.selectByPrimaryKey(dpBrand);

		result.put("brdList", brdList);

		return result;
	}

	/**
	 * @Desc : 브랜드 리스트 조회
	 * @Method Name : getBrandNameList
	 * @Date : 2019. 6. 18.
	 * @Author : 김영진
	 * @param dpBrand
	 * @return
	 */
	public Map<String, Object> getBrandNameList(DpBrand dpBrand) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 브랜드 목록 리스트 조회
		List<DpBrand> brdList = dpBrandDao.getBrandList(dpBrand);

		result.put("brdList", brdList);

		return result;
	}

	public Map<String, Object> getBrandCountList(DpBrand dpBrand) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 브랜드 목록 리스트 조회
		List<DpBrand> brdList = dpBrandDao.getBrandListCount(dpBrand);

		result.put("brdList", brdList);

		return result;
	}

	/**
	 * 브랜드 세일 리스트 조회
	 * 
	 * @Desc :
	 * @Method Name : getBrandPageLoad
	 * @Date : 2019. 7. 1.
	 * @Author : SANTA
	 * @param dpBrand
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBrandSaleList(DpBrand dpBrand) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 브렌드 페이지 조회
		List<DpBrand> brdList = dpBrandDao.selectBrandSaleList(dpBrand);
		result.put("brdList", brdList);

		return result;
	}

}
