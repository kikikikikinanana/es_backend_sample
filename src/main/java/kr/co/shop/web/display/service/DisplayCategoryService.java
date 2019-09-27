package kr.co.shop.web.display.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.model.master.DpCategoryCorner;
import kr.co.shop.web.display.model.master.DpCategoryCornerName;
import kr.co.shop.web.display.model.master.DpCategoryCornerSet;
import kr.co.shop.web.display.model.master.DpDisplayPage;
import kr.co.shop.web.display.model.master.DpDisplayTemplate;
import kr.co.shop.web.display.model.master.DpDisplayTemplateCornerSet;
import kr.co.shop.web.display.repository.master.DpCategoryCornerDao;
import kr.co.shop.web.display.repository.master.DpCategoryCornerNameDao;
import kr.co.shop.web.display.repository.master.DpCategoryCornerSetDao;
import kr.co.shop.web.display.repository.master.DpCategoryDao;
import kr.co.shop.web.display.repository.master.DpCategoryProductDao;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateCornerDao;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateCornerSetDao;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateDao;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DisplayCategoryService {

	@Autowired
	private DpCategoryDao dpCategoryDao;

	@Autowired
	private DpDisplayTemplateDao dpDisplayTemplateDao;

	@Autowired
	private DpCategoryCornerDao dpCategoryCornerDao;

	@Autowired
	private DpCategoryCornerNameDao dpCategoryCornerNameDao;

	@Autowired
	private DpDisplayTemplateCornerSetDao dpDisplayTemplateCornerSetDao;

	@Autowired
	private DpCategoryCornerSetDao dpCategoryCornerSetDao;

	@Autowired
	private ProductService productService;

	@Autowired
	private DpDisplayTemplateCornerDao dpDisplayTemplateCornerDao;

	@Autowired
	private DpCategoryProductDao dpCategoryProductDao;

	@Cacheable(value = "displayCategoryService.getDpCategoryList", key = "#dpCategory.chnnlNo")
	public List<DpCategory> getDpCategoryList(DpCategory dpCategory) throws Exception {

		List<DpCategory> list = dpCategoryDao.selectDpCategoryList(dpCategory);

		return list;
	}

	public List<DpCategory> getAllDpCategoryList(DpCategory dpCategory) throws Exception {

		// 1depth 조회
		dpCategory.setCtgrLevel((short) 1);
		List<DpCategory> list = this.getDpCategoryList(dpCategory);

		for (DpCategory dc : list) {

			if (UtilsText.equals(dc.getLeafCtgrYn(), "N")) {
				dpCategory.setCtgrLevel((short) 2);
				dpCategory.setUpCtgrNo(dc.getCtgrNo());
				List<DpCategory> subList = this.getDpCategoryList(dpCategory);

				for (DpCategory subDc : subList) {
					if (UtilsText.equals(subDc.getLeafCtgrYn(), "N")) {
						dpCategory.setCtgrLevel((short) 3);
						dpCategory.setUpCtgrNo(subDc.getCtgrNo());
						List<DpCategory> subSubList = this.getDpCategoryList(dpCategory);
						subDc.setSubCategoryList(subSubList);
						for (DpCategory subSubDc : subSubList) {
							if (UtilsText.equals(subSubDc.getLeafCtgrYn(), "N")) {
								dpCategory.setCtgrLevel((short) 4);
								dpCategory.setUpCtgrNo(subSubDc.getCtgrNo());
								List<DpCategory> subSubSubList = this.getDpCategoryList(dpCategory);
								subSubDc.setSubCategoryList(subSubSubList);
							}
						}
					}
				}

				dc.setSubCategoryList(subList);
			}
		}

		return list;
	}

	public List<DpCategory> getAllDpCategoryListForSmartSearch(DpCategory dpCategory) throws Exception {

		String chnnlNo = dpCategory.getChnnlNo();

		// 1depth 조회
		dpCategory.setCtgrLevel((short) 1);
		List<DpCategory> list = this.getDpCategoryList(dpCategory);

		for (DpCategory dc : list) {

			if (UtilsText.equals(dc.getLeafCtgrYn(), "N")) {
				dpCategory.setCtgrLevel((short) 2);
				dpCategory.setUpCtgrNo(dc.getCtgrNo());
				List<DpCategory> subList = this.getDpCategoryList(dpCategory);

				if (UtilsText.equals(chnnlNo, "10000")) {
					for (DpCategory subDc : subList) {
						if (UtilsText.equals(subDc.getLeafCtgrYn(), "N")) {
							dpCategory.setCtgrLevel((short) 3);
							dpCategory.setUpCtgrNo(subDc.getCtgrNo());
							List<DpCategory> subSubList = this.getDpCategoryList(dpCategory);
							subDc.setSubCategoryList(subSubList);
						}
					}
				}

				dc.setSubCategoryList(subList);
			}
		}

		return list;
	}

	public Map<String, Object> getDpCategory(DpCategory dpCategory) throws Exception {

		Map<String, Object> result = new HashMap<>();

		// 코너 템플릿 유형 2개

		// 현재 카테고리 정보
		DpCategory category = dpCategoryDao.selectDpCategory(dpCategory);
		// 같은 레벨의 카테고리 목록

		List<List<DpCategory>> breadcrumb = new LinkedList<>();
		String upCtgrNo = category.getUpCtgrNo();
		DpCategory checkedCategory = category;

		for (int i = category.getCtgrLevel(); i > 0; i--) {

			// 상위 카테고리
			DpCategory upCategory = new DpCategory();
			if (!UtilsText.isBlank(upCtgrNo)) {
				upCategory.setCtgrNo(upCtgrNo);
				upCategory = dpCategoryDao.selectDpCategory(upCategory);
			} else {
				dpCategory.setCtgrLevel(Short.parseShort("1"));
			}

			// 상위 카테고리의 하위 카테고리 조회
			dpCategory.setCtgrNo(null);
			dpCategory.setUpCtgrNo(upCategory.getCtgrNo());
			List<DpCategory> list = this.getDpCategoryList(dpCategory);

			for (DpCategory dc : list) {
				if (UtilsText.equals(dc.getCtgrNo(), checkedCategory.getCtgrNo())) {
					dc.setCheckedCategory(true);
					break;
				}
			}

			checkedCategory = upCategory;
			upCtgrNo = upCategory.getUpCtgrNo();

			breadcrumb.add(list);

			if (UtilsText.equals(upCategory.getChnnlNo(), "10000")
					&& (upCategory.getCtgrLevel() == 1 || UtilsText.isBlank(upCtgrNo))) {
				break;
			}

			/*
			 * if (UtilsText.isBlank(upCtgrNo) && UtilsText.equals("10000",
			 * dpCategory.getChnnlNo())) { break; }
			 */
		}

		breadcrumb = Lists.reverse(breadcrumb);

		// 하위 카테고리가 있을 경우
		if (UtilsText.equals(category.getLeafCtgrYn(), "N")) {
			// 하위 카테고리 목록
			dpCategory.setCtgrNo(null);
			dpCategory.setCtgrLevel((short) (category.getCtgrLevel() + 1));
			dpCategory.setUpCtgrNo(category.getCtgrNo());
			List<DpCategory> subList = this.getDpCategoryList(dpCategory);
			result.put("subList", subList);

		}

		// TODO: 베스트 브랜드
		// TODO: 하위 카테고리 별 베스트 상품
		// TODO: 베스트 브랜드 별 베스트 상품
		// TODO: 대카테고리 상품 리스트

		result.put("category", category);
		result.put("breadcrumb", breadcrumb);

		return result;
	}

	public Map<String, Object> getCategoryInfo(DpCategory dpCategory, Parameter<?> parameter) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		DpCategory category = dpCategoryDao.selectDpCategory(dpCategory);

		// 템플릿
		DpDisplayTemplate dpDisplayTemplate = new DpDisplayTemplate();
		if (UtilsText.equals(dpCategory.getDeviceCode(), CommonCode.DEVICE_MOBILE)) {
			dpDisplayTemplate.setDispTmplNo(category.getMobileDispTmplNo());
		} else {
			dpDisplayTemplate.setDispTmplNo(category.getPcDispTmplNo());
		}
		DpDisplayTemplate template = dpDisplayTemplateDao.selectDpDisplayTemplate(dpDisplayTemplate);

		// 코너
		DpCategoryCorner dpCategoryCorner = new DpCategoryCorner();
		BeanUtils.copyProperties(dpCategoryCorner, category);
		dpCategoryCorner.setDispTmplNo(dpDisplayTemplate.getDispTmplNo());
		List<DpCategoryCorner> cornerList = dpCategoryCornerDao.selectDpCategoryCornerList(dpCategoryCorner);

		Map<String, Object> cornerMap = new HashMap<String, Object>();
		for (DpCategoryCorner corner : cornerList) {
			// 코너명
			DpCategoryCornerName name = new DpCategoryCornerName();
			BeanUtils.copyProperties(name, corner);
			name = dpCategoryCornerNameDao.selectDpCategoryCornerName(name);
			corner.setDpCategoryCornerName(name);

			DpDisplayTemplateCornerSet dpDisplayTemplateCornerSet = new DpDisplayTemplateCornerSet();
			BeanUtils.copyProperties(dpDisplayTemplateCornerSet, corner);
			List<DpDisplayTemplateCornerSet> dpDisplayTemplateCornerSetList = dpDisplayTemplateCornerSetDao
					.selectDpDisplayTemplateCornerSetListByDispContCountGreaterThanZero(dpDisplayTemplateCornerSet);

			Map<String, Object> setMap = new HashMap<String, Object>();
			Map<String, Object> setListMap = new HashMap<String, Object>();
			int i = 0;
			Integer sortSeq = null;
			for (DpDisplayTemplateCornerSet templateSet : dpDisplayTemplateCornerSetList) {

				// 쎄트 별 저장
				if (sortSeq != templateSet.getSortSeq()) {
					if (sortSeq != null) {
						if (setMap.size() > 0) {
							setListMap.put(Integer.toString(i), setMap);
							setMap = new HashMap<String, Object>();
							i++;
						}
					}

					sortSeq = templateSet.getSortSeq();
				}

				DpCategoryCornerSet set = new DpCategoryCornerSet();
				BeanUtils.copyProperties(set, templateSet);
				set.setCtgrNo(dpCategory.getCtgrNo());
				List<DpCategoryCornerSet> setList = dpCategoryCornerSetDao.selectDpCategoryCornerSetList(set);

				if (setList.size() > 0) {

					// 상품 타입일 경우
					if (UtilsText.equals("10001", templateSet.getContTypeCode())) {
						// 상품 리스트 조회

						List<String> prdtNoList = new LinkedList<>();
						for (DpCategoryCornerSet productSet : setList) {
							prdtNoList.add(productSet.getAddInfo1());
						}

						PageableProduct<DpDisplayPage, PdProductWrapper> pageableProduct = new PageableProduct<DpDisplayPage, PdProductWrapper>(
								(Parameter<DpDisplayPage>) parameter);

						pageableProduct.setCondition(dpCategory.getSiteNo(), dpCategory.getChnnlNo());
						pageableProduct.setUsePaging(false, null, null, null);
						pageableProduct.setNotUseTableMapping(prdtNoList);
						Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageableProduct);
						setMap.put(templateSet.getContTypeCode(), productList);
					} else {
						setMap.put(templateSet.getContTypeCode(), setList);
					}

				}

			}

			if (setMap.size() > 0) {
				setListMap.put(Integer.toString(i), setMap);
			}

			if (setListMap.size() > 0) {
				corner.setDpCategoryCornerSetMap(setListMap);
				cornerMap.put(Integer.toString(corner.getDispTmplCornerSeq()), corner);
			}

		}

		resultMap.put("template", template);

		if (cornerMap.size() > 0) {
			resultMap.put("category", category);
			resultMap.put("cornerList", cornerList);
			resultMap.put("cornerMap", cornerMap);
		}

		return resultMap;
	}

	public Map<String, Object> getCategoryProduct(Parameter<DpCategory> parameter) throws Exception {

		Map<String, Object> result = new HashMap<>();
		PageableProduct<DpCategory, PdProductWrapper> pageable = new PageableProduct<DpCategory, PdProductWrapper>(
				parameter);

		pageable.setCondition(parameter.get().getSiteNo(), parameter.get().getChnnlNo());
		pageable.setUseTableMapping("DP_CATEGORY_PRODUCT", null, null);
		if (UtilsText.isNotBlank(parameter.get().getCtgrNo())) {
			pageable.setConditionCategoryNo(parameter.get().getCtgrNo());
		}
		if (UtilsText.isNotBlank(parameter.get().getCtgrName())) {
			pageable.setConditionCategoryName(parameter.get().getCtgrName());
		}
		pageable.setConditionDiscountRateGreaterThan(parameter.get().getDiscountRate());

		if (UtilsText.equals(parameter.get().getNewYn(), "Y")) {
			pageable.setConditionApprovedProductWithinDateRange(PageableProduct.CONDITION_DATE_RANGE_MONTH, -1);
		}

		if (UtilsText.isNotBlank(parameter.get().getBrandNo())) {
			pageable.setConditionBrandNo(parameter.get().getBrandNo());
		}

		String pagingSortType = parameter.get().getPagingSortType() != null ? parameter.get().getPagingSortType()
				: PageableProduct.PAGING_SORT_TYPE_NEWEST;

		pageable.setUsePaging(true, pagingSortType, null, null);
		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());
		Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageable);

		productList.setTotalCount(
				this.productService.getDisplayProductTotalCount(pageable).get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		result.put("productList", productList);

		return result;
	}

	public int getDpCategoryProductTotalCount(Parameter<DpCategory> parameter) throws Exception {

		return dpCategoryProductDao.selectDpCategoryProductTotalCount(parameter.get());
	}

	public List<DpCategory> getDpCategorySaleList(DpCategory dpCategory) throws Exception {
		return dpCategoryDao.selectDpCategorySaleList(dpCategory);
	}

	public List<DpCategory> getDpCategoryNameList(DpCategory dpCategory) throws Exception {
		return dpCategoryDao.selectDpCategoryNameList(dpCategory);
	}
}
