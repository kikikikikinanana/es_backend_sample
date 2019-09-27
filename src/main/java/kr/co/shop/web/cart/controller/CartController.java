package kr.co.shop.web.cart.controller;

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
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.cart.model.master.OcCartBenefit;
import kr.co.shop.web.cart.service.CartService;
import kr.co.shop.web.cart.vo.OcCartInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class CartController extends BaseController {

	@Autowired
	CartService cartService;

	@ApiOperation(value = "장바구니 메인", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "isLogin", value = "로그인여부", required = true, dataType = "boolean", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "sessionId", value = "세션아이디", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartList(Parameter<OcCart> parameter) throws Exception {

		OcCart ocCart = parameter.get();
		Map<String, Object> resultMap = cartService.setCartList(ocCart);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 정보 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "isLogin", value = "로그인여부", required = true, dataType = "boolean", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "sessionId", value = "세션아이디", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-load-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrdtLoadList(Parameter<OcCart> parameter) throws Exception {

		OcCart ocCart = parameter.get();
		Map<String, Object> resultMap = cartService.getPrdtLoadList(ocCart);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품담기", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "boolean", paramType = "body"),
			@ApiImplicitParam(name = "prdtOptnNo", value = "옵션번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderQty", value = "주문수량", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "ctgrNo", value = "카테고리번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "plndpNo", value = "기획전번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "eventTypeCode", value = "참여형 (draw) 만", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtRecptStoreType", value = "참여형 (draw) 만 o(온라인), a(abcmart매장), t(ots매장), g(gs매장)", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "storeNo", value = "참여형 (draw) & 매장일때만 점포코드 ", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-add" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCartAdd(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.setCartAdd(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품삭제", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번 배열", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-del" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCartPrdtDelete(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.setCartPrdtDelete(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 재고 수량 확인", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번 배열", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-qty-validate" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartPrtdQtyValidate(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.getCartPrtdQtyValidate(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품  수량 등록 처리", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번 배열", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-qty-save" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCartPrtdQtySave(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.setCartPrtdQtySave(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 옵션 수정 레이어팝업", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-option-layer" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartPrdtOPtionLayer(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.getCartPrdtOPtionLayer(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 옵션 수정 저장", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtOptnNo", value = "상품옵션번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderQty", value = "상품수량", required = false, dataType = "string", paramType = "body"), })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-option-save" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCartPrdtOptionSave(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.setCartPrdtOptionSave(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 쿠폰 레이어팝업", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-coupon-layer" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartPrdtCouponLayer(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.getCartPrdtCouponLayer(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 쿠폰 다운로드 후 재 조회 추가", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-prdt-coupon-append" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartPrdtCouponAppend(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
//		Map<String, Object> resultMap = cartService.getCartPrdtCouponLayer(params);
		Map<String, Object> resultMap = cartService.getCartPrdtCouponAppend(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 상품 적용", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cartSeqs", value = "장바구니 순번", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/cart/cart-apply-coupon" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCartApplyCoupon(Parameter<OcCartBenefit> parameter) throws Exception {

		OcCartBenefit params = parameter.get();
		Map<String, Object> resultMap = cartService.setCartApplyCoupon(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 카운트", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/cart/cart-counting" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartCounting(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		Map<String, Object> resultMap = cartService.getCartCounting(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 merge", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/cart/cart-merge" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCartMerge(Parameter<OcCart> parameter) throws Exception {

		OcCart params = parameter.get();
		cartService.setCartMerge(params);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "장바구니 merge", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = { "/v1.0/cart/searching-prdt" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWishPrdtList(Parameter<OcCartInfo> parameter) throws Exception {

		OcCartInfo params = parameter.get();
		Map<String, Object> resultMap = cartService.getWishPrdtList(params);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "상품 옵션변경 레이어", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "/v1.0/cart/order-option-layer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrdtOptionInfo(Parameter<?> parameter) throws Exception {
		Map<String, Object> resultMap = cartService.getPrdtOptionInfo(parameter);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "상품 옵션변경 레이어", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "/v1.0/cart/order-option-add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrdtOptionAdd(Parameter<?> parameter) throws Exception {
		Map<String, Object> resultMap = cartService.getPrdtOptionAdd(parameter);
		return UtilsREST.responseOk(parameter, resultMap);
	}

}