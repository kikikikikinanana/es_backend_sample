package kr.co.shop.web.product.controller;

import java.util.List;
import java.util.Map;

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
import kr.co.shop.web.product.model.master.DpBrandStyle;
import kr.co.shop.web.product.service.BrandStyleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class BrandStyleController extends BaseController {

	@Autowired
	BrandStyleService brandStyleService;

	@ApiOperation(value = "브랜드 스타일 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/style/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandStyleList(Parameter<DpBrandStyle> parameter) throws Exception {
		log.debug("parameter > {}", parameter);
		Map<String, List<DpBrandStyle>> styleList = brandStyleService.getBrandStyleList(parameter.get());
		return UtilsREST.responseOk(parameter, styleList);
	}

	@ApiOperation(value = "브랜드 카테고리 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/category/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandCategoryList(Parameter<DpBrandStyle> parameter) throws Exception {
		log.debug("parameter > {}", parameter);
		Map<String, List<DpBrandStyle>> categoryList = brandStyleService.getBrandCategoryList(parameter.get());
		return UtilsREST.responseOk(parameter, categoryList);
	}

}