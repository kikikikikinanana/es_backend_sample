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
import kr.co.shop.web.product.model.master.PrPromotionProduct;
import kr.co.shop.web.promotion.service.HotDealService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class HotDealController extends BaseController {

	@Autowired
	HotDealService hotDealService;

	@ApiOperation(value = "진행중 핫딜 프로모션 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/hotdeal/rel/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHotDealList(Parameter<PrPromotionProduct> parameter) throws Exception {
		Map<String, Object> list = hotDealService.getRelHotDealList(parameter);
		return UtilsREST.responseOk(parameter, list);
	}

	@ApiOperation(value = "대기중 핫딜 프로모션 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/product/hotdeal/wait/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWeeklyList(Parameter<PrPromotionProduct> parameter) throws Exception {
		Map<String, Object> list = hotDealService.getWaitHotDealList(parameter);
		return UtilsREST.responseOk(parameter, list);
	}

}