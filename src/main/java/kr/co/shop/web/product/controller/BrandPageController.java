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
import kr.co.shop.web.product.model.master.DpBrandPage;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.service.BrandPageService;
import kr.co.shop.web.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class BrandPageController extends BaseController {

	@Autowired
	BrandPageService brandPageService;

	@Autowired
	ProductService productService;

	@ApiOperation(value = "브랜드 페이지 프로모션 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/page/promotion" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandPromotion(Parameter<DpBrandPage> parameter) throws Exception {
		Map<String, Object> list = brandPageService.getBrandPromotion(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 페이지 비주얼 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/page/visual/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandPageVisualList(Parameter<DpBrandPage> parameter) throws Exception {
		Map<String, List<DpBrandPage>> list = brandPageService.getBrandPageVisualList(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 페이지 상품 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/page/item/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandProductList(Parameter<DpBrandPage> parameter) throws Exception {
		Map<String, Object> list = brandPageService.getBrandProductList(parameter);
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "WEEKLY BEST 상품 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/page/weekly" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWeeklyList(Parameter<PdProduct> parameter) throws Exception {
		Map<String, Object> list = brandPageService.getWeeklyList(parameter);
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 BEST4 상품 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/page/best" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandProductBest(Parameter<PdProduct> parameter) throws Exception {
		String brandNo = "000003";
		Map<String, Object> list = brandPageService.getBrandProductBest(brandNo);
		return UtilsREST.responseOk(parameter, list);
	}

}