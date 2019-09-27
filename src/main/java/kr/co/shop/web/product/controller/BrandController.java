package kr.co.shop.web.product.controller;

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
import kr.co.shop.web.product.model.master.DpBrand;
import kr.co.shop.web.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class BrandController extends BaseController {

	@Autowired
	BrandService brandService;

	@ApiOperation(value = "매장전용 상품존 브랜드 리스트", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/brand/store/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStoreBrandList(Parameter<DpBrand> parameter) throws Exception {
		Map<String, Object> list = brandService.getStoreBrandList(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 페이지 데이터 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/page/load" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandPageLoad(Parameter<DpBrand> parameter) throws Exception {
		Map<String, Object> list = brandService.getBrandPageLoad(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 리스트 데이터 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandList(Parameter<DpBrand> parameter) throws Exception {
		Map<String, Object> list = brandService.getBrandNameList(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 리스트 데이터 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/count" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandCountList(Parameter<DpBrand> parameter) throws Exception {
		Map<String, Object> list = brandService.getBrandCountList(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "브랜드 세일 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/brand/sale/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrandSaleList(Parameter<DpBrand> parameter) throws Exception {
		Map<String, Object> list = brandService.getBrandSaleList(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

}