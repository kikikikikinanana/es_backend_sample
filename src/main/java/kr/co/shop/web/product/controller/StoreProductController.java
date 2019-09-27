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
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsNumber;
import kr.co.shop.common.util.UtilsREST;
import kr.co.shop.web.product.model.master.DpStoreProduct;
import kr.co.shop.web.product.service.StoreProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class StoreProductController extends BaseController {

	@Autowired
	StoreProductService storeProductService;

	@ApiOperation(value = "매장전용 상품 리스트", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/product/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStoreProductList(Parameter<Object> parameter) throws Exception {
		Pageable<Object, DpStoreProduct> pageable = new Pageable<Object, DpStoreProduct>(parameter);
		Map<String, Object> param = (Map<String, Object>) parameter.get();
		// pageable.setPageNum(UtilsNumber.toInt("" + param.get("pageNum")));
		// pageable.setRowsPerPage(UtilsNumber.toInt("" + param.get("rowsPerPage")));
		pageable.setPageNum(UtilsNumber.toInt("" + 2));
		pageable.setRowsPerPage(UtilsNumber.toInt("" + 40));

		Page<DpStoreProduct> list = storeProductService.getStoreProductList(pageable);

		return UtilsREST.responseOk(parameter, list.getGrid());
	}

	@ApiOperation(value = "매장전용 상품 리스트 테스트", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/product/offline/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStoreProductOffList(Parameter<DpStoreProduct> parameter) throws Exception {
		Map<String, Object> list = storeProductService.getStoreProductOffList(parameter);
		return UtilsREST.responseOk(parameter, list);
	}

}