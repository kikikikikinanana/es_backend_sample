package kr.co.shop.web.product.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsNumber;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.product.service.ProductOptionService;
import kr.co.shop.web.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ProductOptionController extends BaseController {

	@Autowired
	private ProductOptionService productOptionService;

	@Autowired
	private ProductService productService;

	@ApiOperation(value = "매장별 상품 재고 조회", notes = "매장별 상품 재고 목록 조회", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "searchOptnName", value = "상품 사이즈", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "searchStoreType", value = "매장 타입", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "searchArea", value = "지역(시)", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "searchAreaDetail", value = "지역(시/군/구)", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "searchKeyword", value = "매장명", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호(단골매장 검색용)", required = false, dataType = "String", paramType = "body") })
	@PostMapping(value = "v1.0/product/stock/get-store-stock-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStockList(Parameter<Object> parameter) throws Exception {
		log.debug("매장별 상품 재고 목록 조회");

		Map<String, Object> param = (Map<String, Object>) parameter.get();
		Pageable<Object, Map<String, Object>> pageable = new Pageable<Object, Map<String, Object>>(parameter);
		pageable.setPageNum(UtilsNumber.toInt("" + param.get("pageNum")));
		pageable.setRowsPerPage(UtilsNumber.toInt("" + param.get("rowsPerPage")));

		Page<Map<String, Object>> page = this.productOptionService.selectProductStoreStockList(pageable);
		return UtilsREST.responseOk(parameter, page.getGrid());
	}

}
