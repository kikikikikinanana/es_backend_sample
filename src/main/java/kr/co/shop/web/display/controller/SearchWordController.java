package kr.co.shop.web.display.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsREST;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.model.master.DpSearchWord;
import kr.co.shop.web.display.model.master.DpSearchWordHistory;
import kr.co.shop.web.display.service.DisplayCategoryService;
import kr.co.shop.web.display.service.SearchWordService;
import kr.co.shop.web.display.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class SearchWordController extends BaseController {

	@Autowired
	SearchWordService searchWordService;

	@Autowired
	DisplayCategoryService displayCategoryService;

	@ApiOperation(value = "검색어 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/search-word/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSearchWordList(Parameter<DpSearchWord> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, searchWordService.getSearchWordList(parameter.get()));
	}

	@ApiOperation(value = "인기 검색어 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/search-word/popular/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPopularSearchWordList(Parameter<DpSearchWordHistory> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, searchWordService.getPopularSearchWordList(parameter.get()));
	}

	@ApiOperation(value = "자동 완성 검색어 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/search-word/auto-complete/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAutoCompleteSearchWordList(Parameter<SearchVO> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, searchWordService.getAutoComplete(parameter.get()));
	}

	@ApiOperation(value = "스마트 서치·필터 옵션 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/search-word/smart-option/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSmartOptionList(Parameter<SearchVO> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, searchWordService.getSmartSearchResult(parameter.get()));
	}

	@ApiOperation(value = "스마트 서치 카테고리 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = {
			"v1.0/display/search-word/smart-option/category/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryList(Parameter<DpCategory> parameter) throws Exception {

		return UtilsREST.responseOk(parameter,
				displayCategoryService.getAllDpCategoryListForSmartSearch(parameter.get()));
	}

}