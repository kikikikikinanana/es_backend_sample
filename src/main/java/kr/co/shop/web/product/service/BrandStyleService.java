package kr.co.shop.web.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.product.model.master.DpBrandStyle;
import kr.co.shop.web.product.repository.master.DpBrandStyleDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrandStyleService {

	@Autowired
	private DpBrandStyleDao dpBrandStyleDao;

	/**
	 * @Desc : 브랜드 스타일 리스트 조회
	 * @Method Name : getBrandStyleList
	 * @Date : 2019. 6. 17.
	 * @Author : 백인천
	 * @param getBrandStyleList
	 * @return
	 */
	public Map<String, List<DpBrandStyle>> getBrandStyleList(DpBrandStyle dpBrandStyle) throws Exception {
		Map<String, List<DpBrandStyle>> result = new HashMap<>();
		List<DpBrandStyle> brdStyleList = new ArrayList<DpBrandStyle>();

		brdStyleList = dpBrandStyleDao.getBrandStyleList(dpBrandStyle);

		result.put("brdStyleList", brdStyleList);

		return result;
	}

	/**
	 * @Desc : 브랜드 카테고리 리스트 조회
	 * @Method Name : getBrandStyleList
	 * @Date : 2019. 6. 17.
	 * @Author : 백인천
	 * @param getBrandStyleList
	 * @return
	 */
	public Map<String, List<DpBrandStyle>> getBrandCategoryList(DpBrandStyle dpBrandStyle) throws Exception {
		Map<String, List<DpBrandStyle>> result = new HashMap<>();
		List<DpBrandStyle> brdCategoryList = new ArrayList<DpBrandStyle>();

		brdCategoryList = dpBrandStyleDao.getBrandCategoryList(dpBrandStyle);

		result.put("brdCategoryList", brdCategoryList);

		return result;
	}

}
