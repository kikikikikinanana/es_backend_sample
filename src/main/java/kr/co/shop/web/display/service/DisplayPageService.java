package kr.co.shop.web.display.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.display.model.master.DpDisplayPage;
import kr.co.shop.web.display.model.master.DpDisplayPageCorner;
import kr.co.shop.web.display.model.master.DpDisplayPageCornerName;
import kr.co.shop.web.display.model.master.DpDisplayPageCornerSet;
import kr.co.shop.web.display.model.master.DpDisplayTemplate;
import kr.co.shop.web.display.model.master.DpDisplayTemplateCorner;
import kr.co.shop.web.display.model.master.DpDisplayTemplateCornerSet;
import kr.co.shop.web.display.repository.master.DpDisplayPageCornerDao;
import kr.co.shop.web.display.repository.master.DpDisplayPageCornerNameDao;
import kr.co.shop.web.display.repository.master.DpDisplayPageCornerSetDao;
import kr.co.shop.web.display.repository.master.DpDisplayPageDao;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateCornerDao;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateCornerSetDao;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateDao;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DisplayPageService {

	@Autowired
	private DpDisplayPageDao dpDisplayPageDao;

	@Autowired
	private DpDisplayPageCornerDao dpDisplayPageCornerDao;

	@Autowired
	private DpDisplayPageCornerNameDao dpDisplayPageCornerNameDao;

	@Autowired
	private DpDisplayPageCornerSetDao dpDisplayPageCornerSetDao;

	@Autowired
	private DpDisplayTemplateDao dpDisplayTemplateDao;

	@Autowired
	private DpDisplayTemplateCornerDao DpDisplayPageCornerDao;

	@Autowired
	private DpDisplayTemplateCornerSetDao dpDisplayTemplateCornerSetDao;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private DpDisplayTemplateCornerDao dpDisplayTemplateCornerDao;

//	@Cacheable(value = "displayPageService.getPageInfo", key = "#dpDisplayPage.dispPageNo")
	public Map<String, Object> getPageInfo(DpDisplayPage dpDisplayPage, Parameter<?> parameter) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 페이지
		DpDisplayPage page = dpDisplayPageDao.selectDpDisplayPage(dpDisplayPage);

		if (page == null)
			return resultMap;

		// 템플릿
		DpDisplayTemplate dpDisplayTemplate = new DpDisplayTemplate();
		BeanUtils.copyProperties(dpDisplayTemplate, page);
		if (UtilsText.equals(dpDisplayPage.getDeviceCode(), CommonCode.DEVICE_MOBILE)) {
			dpDisplayTemplate.setDispTmplNo(page.getMobileDispTmplNo());
		} else {
			dpDisplayTemplate.setDispTmplNo(page.getPcDispTmplNo());
		}
		DpDisplayTemplate template = dpDisplayTemplateDao.selectDpDisplayTemplate(dpDisplayTemplate);

		// 코너
//		DpDisplayPageCorner dpDisplayPageCorner = new DpDisplayPageCorner();
//		BeanUtils.copyProperties(dpDisplayPageCorner, page);
//		dpDisplayPageCorner.setDispTmplNo(dpDisplayTemplate.getDispTmplNo());
//		List<DpDisplayPageCorner> cornerList = dpDisplayPageCornerDao
//				.selectDpDisplayPageCornerList(dpDisplayPageCorner);

		DpDisplayTemplateCorner dpDisplayTemplateCorner = new DpDisplayTemplateCorner();
		dpDisplayTemplateCorner.setDispTmplNo(dpDisplayTemplate.getDispTmplNo());
		List<DpDisplayTemplateCorner> dpDisplayTemplateCornerList = dpDisplayTemplateCornerDao
				.selectTemplateCornerList(dpDisplayTemplateCorner);

		Map<String, Object> cornerMap = new LinkedHashMap<String, Object>();
		for (DpDisplayTemplateCorner templateCorner : dpDisplayTemplateCornerList) {

			DpDisplayPageCorner corner = new DpDisplayPageCorner();
			corner.setDispPageNo(page.getDispPageNo());
			corner.setDispTmplNo(templateCorner.getDispTmplNo());
			corner.setDispTmplCornerSeq(templateCorner.getDispTmplCornerSeq());

			// 코너명
			DpDisplayPageCornerName name = new DpDisplayPageCornerName();
			BeanUtils.copyProperties(name, corner);
			name = dpDisplayPageCornerNameDao.selectDpDisplayPageCornerName(name);
			corner.setDpDisplayPageCornerName(name);

			DpDisplayTemplateCornerSet dpDisplayTemplateCornerSet = new DpDisplayTemplateCornerSet();
			BeanUtils.copyProperties(dpDisplayTemplateCornerSet, corner);
			List<DpDisplayTemplateCornerSet> dpDisplayTemplateCornerSetList = dpDisplayTemplateCornerSetDao
					.selectDpDisplayTemplateCornerSetListByDispContCountGreaterThanZero(dpDisplayTemplateCornerSet);

			Map<String, Object> setMap = new LinkedHashMap<String, Object>();
			Map<String, Object> setListMap = new LinkedHashMap<String, Object>();
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

				DpDisplayPageCornerSet set = new DpDisplayPageCornerSet();
				BeanUtils.copyProperties(set, templateSet);
				List<DpDisplayPageCornerSet> setList = dpDisplayPageCornerSetDao.selectDpDisplayPageCornerSetList(set);

				if (setList.size() > 0) {

					// 상품 타입일 경우
					if (UtilsText.equals("10001", templateSet.getContTypeCode())) {
						// 상품 리스트 조회

						List<String> prdtNoList = new LinkedList<>();
						for (DpDisplayPageCornerSet productSet : setList) {
							prdtNoList.add(productSet.getAddInfo1());
						}

						PageableProduct<DpDisplayPage, PdProductWrapper> pageableProduct = new PageableProduct<DpDisplayPage, PdProductWrapper>(
								(Parameter<DpDisplayPage>) parameter);

						pageableProduct.setCondition(page.getSiteNo(), page.getChnnlNo());
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
				corner.setDpDisplayPageCornerSetMap(setListMap);
				cornerMap.put(Integer.toString(corner.getDispTmplCornerSeq()), corner);
			}

		}

		if (cornerMap.size() > 0) {
			resultMap.put("cornerMap", cornerMap);
		}

		resultMap.put("page", page);
		resultMap.put("template", template);

		return resultMap;
	}

	public Map<String, Object> getAppSplashList(String siteNo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		DpDisplayPage dpDisplayPage = new DpDisplayPage();

		if (UtilsText.equals(siteNo, "10000")) {
			dpDisplayPage.setDispPageNo("1000031");
		} else {
			dpDisplayPage.setDispPageNo("1000038");
		}

		// 페이지
		DpDisplayPage page = dpDisplayPageDao.selectDpDisplayPage(dpDisplayPage);

		if (page == null)
			return resultMap;

		DpDisplayTemplateCorner dpDisplayTemplateCorner = new DpDisplayTemplateCorner();
		dpDisplayTemplateCorner.setDispTmplNo(page.getMobileDispTmplNo());
		List<DpDisplayTemplateCorner> dpDisplayTemplateCornerList = dpDisplayTemplateCornerDao
				.selectTemplateCornerList(dpDisplayTemplateCorner);

		Map<String, Object> cornerMap = new LinkedHashMap<String, Object>();
		for (DpDisplayTemplateCorner templateCorner : dpDisplayTemplateCornerList) {

			DpDisplayPageCorner corner = new DpDisplayPageCorner();
			corner.setDispPageNo(page.getDispPageNo());
			corner.setDispTmplNo(templateCorner.getDispTmplNo());
			corner.setDispTmplCornerSeq(templateCorner.getDispTmplCornerSeq());

			DpDisplayTemplateCornerSet dpDisplayTemplateCornerSet = new DpDisplayTemplateCornerSet();
			BeanUtils.copyProperties(dpDisplayTemplateCornerSet, corner);
			List<DpDisplayTemplateCornerSet> dpDisplayTemplateCornerSetList = dpDisplayTemplateCornerSetDao
					.selectDpDisplayTemplateCornerSetListByDispContCountGreaterThanZero(dpDisplayTemplateCornerSet);

			for (DpDisplayTemplateCornerSet templateSet : dpDisplayTemplateCornerSetList) {

				DpDisplayPageCornerSet set = new DpDisplayPageCornerSet();
				BeanUtils.copyProperties(set, templateSet);
				List<DpDisplayPageCornerSet> setList = dpDisplayPageCornerSetDao.selectDpDisplayPageCornerSetList(set);

				List<Map<String, Object>> setMapList = new ArrayList<>();
				for (DpDisplayPageCornerSet item : setList) {
					Map<String, Object> setMap = new HashMap<String, Object>();
					setMap.put("key", UtilsText.concat(Integer.toString(item.getDispTmplCornerSeq()), "-",
							Integer.toString(item.getContTypeSeq())));
					setMap.put("name", item.getAddInfo2());
					setMap.put("url", item.getAddInfo4());
					setMapList.add(setMap);
				}

				resultMap.put("splashList", setMapList);
			}

		}

		return resultMap;
	}
}
