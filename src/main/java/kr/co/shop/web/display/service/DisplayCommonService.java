package kr.co.shop.web.display.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.request.Parameter;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.model.master.DpDisplayPage;
import kr.co.shop.web.display.vo.GnbVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DisplayCommonService {

	@Autowired
	private DisplayPageService displayPageService;

	@Autowired
	private DisplayCategoryService displayCategoryService;

	public Map<String, Object> getGnbData(Parameter<GnbVO> parameter) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();

		// gnb 코너
		DpDisplayPage dpDisplayPage = new DpDisplayPage();
		BeanUtils.copyProperties(dpDisplayPage, parameter.get());
		result.put("gnbCorner", displayPageService.getPageInfo(dpDisplayPage, parameter));
		// 카테고리
		DpCategory dpCategory = new DpCategory();
		BeanUtils.copyProperties(dpCategory, parameter.get());
		result.put("categoryList", displayCategoryService.getAllDpCategoryList(dpCategory));
		// Sale
		result.put("categorySaleList", displayCategoryService.getDpCategorySaleList(dpCategory));

		return result;
	}

	public Map<String, Object> getHeaderBanner(Parameter<GnbVO> parameter) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> banner = new HashMap<String, Object>();
		// 헤더 띠 배너 코너
		DpDisplayPage dpDisplayPage = new DpDisplayPage();

		GnbVO vo = parameter.get();

		for (String dpPageNo : vo.getDispPageNoArr()) {
			vo.setDispPageNo(dpPageNo);
			BeanUtils.copyProperties(dpDisplayPage, vo);
			Map<String, Object> page = displayPageService.getPageInfo(dpDisplayPage, parameter);
			if (page.size() > 0) {
				banner.put(dpPageNo, page);
			}
		}

		result.put("headerBanner", banner);

		return result;
	}
}
