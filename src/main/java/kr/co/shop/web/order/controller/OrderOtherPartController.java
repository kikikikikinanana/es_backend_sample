package kr.co.shop.web.order.controller;

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
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.service.OrderOtherPartService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class OrderOtherPartController extends BaseController {

	@Autowired
	OrderOtherPartService orderOtherPartService;

	@ApiOperation(value = "주문, 클레임, AS의 갯수 - 진행중 대상건수", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"), })
	@PostMapping(value = "/v1.0/orderOtherPart/getProgressOrderClmAsCount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProgressOrderClmAsCount(Parameter<OcOrder> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderOtherPartService.getProgressOrderClmAsCount(parameter.get()));
	}

	@ApiOperation(value = "구매확정 후 30 진행 여부 유효성 검사", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderNo", value = "주문번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/orderOtherPart/getOverDayYnAfterBuyDecision", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOverDayYnAfterBuyDecision(Parameter<OcOrder> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderOtherPartService.getOverDayYnAfterBuyDecision(parameter.get()));
	}
}
