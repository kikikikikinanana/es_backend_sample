package kr.co.shop.web.display.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.service.DisplayCategoryService;
import kr.co.shop.web.display.service.DisplayPageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class DisplayCategoryController extends BaseController {

	@Autowired
	private DisplayPageService displayPageService;

	@Autowired
	private DisplayCategoryService displayCategoryService;

	@ApiOperation(value = "전시 카테고리 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "upCtgrNo", value = "상위 전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrLevel", value = "카테고리 레벨", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryList(Parameter<DpCategory> parameter) throws Exception {

		List<DpCategory> list = displayCategoryService.getDpCategoryList(parameter.get());

		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "전시 카테고리 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategory(Parameter<DpCategory> parameter) throws Exception {

		Map<String, Object> result = displayCategoryService.getDpCategory(parameter.get());

		return UtilsREST.responseOk(parameter, result);

	}

	@ApiOperation(value = "전시 카테고리 영역 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "deviceCode", value = "디바이스코드", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryData(Parameter<DpCategory> parameter) throws Exception {

		Map<String, Object> result = displayCategoryService.getCategoryInfo(parameter.get(), parameter);

		return UtilsREST.responseOk(parameter, result);

	}

	@ApiOperation(value = "전시 카테고리 상품 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/product", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryProduct(Parameter<DpCategory> parameter) throws Exception {

		Map<String, Object> result = displayCategoryService.getCategoryProduct(parameter);

		return UtilsREST.responseOk(parameter, result);

	}

	@ApiOperation(value = "전시 카테고리 상품 총 개수 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/product/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryProductTotalCount(Parameter<DpCategory> parameter) throws Exception {

		int result = displayCategoryService.getDpCategoryProductTotalCount(parameter);

		return UtilsREST.responseOk(parameter, result);

	}

	@ApiOperation(value = "전체 전시 카테고리 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "upCtgrNo", value = "상위 전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrLevel", value = "카테고리 레벨", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/list/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllDpCategoryList(Parameter<DpCategory> parameter) throws Exception {

		List<DpCategory> list = displayCategoryService.getAllDpCategoryList(parameter.get());

		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "전시 카테고리 sale 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/list/sale", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategorySaleList(Parameter<DpCategory> parameter) throws Exception {

		List<DpCategory> list = displayCategoryService.getDpCategorySaleList(parameter.get());

		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "전시 카테고리 이름 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/category/list/name", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryNameList(Parameter<DpCategory> parameter) throws Exception {

		List<DpCategory> list = displayCategoryService.getDpCategoryNameList(parameter.get());

		return UtilsREST.responseOk(parameter, list);
	}
}