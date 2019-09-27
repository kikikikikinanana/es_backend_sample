package kr.co.shop.web.test.controller;

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
import kr.co.shop.web.cart.service.CartService;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.service.DisplayCategoryService;
import kr.co.shop.web.display.vo.GnbVO;
import kr.co.shop.web.test.service.TestService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class TestController extends BaseController {

	@Autowired
	CartService cartService;

	@Autowired
	TestService testService;

	@Autowired
	DisplayCategoryService displayCategoryService;

	@ApiOperation(value = "장바구니 메인", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "isLogin", value = "로그인여부", required = true, dataType = "boolean", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "sessionId", value = "세션아이디", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/test/cart-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartList(Parameter<OcCart> parameter) throws Exception {

//		Parameter<OcCart> parameter = new

		OcCart ocCart = new OcCart();

		ocCart.setLogin(true);
		ocCart.setSessionId("AF270F37345384B326B03B737C000C49PXXX");
		ocCart.setMemberNo("MB00000002");
		ocCart.setMemberTypeCode("10000");
		ocCart.setSiteNo("10000");
		ocCart.setDeviceCode("10000");
		ocCart.setChnnlNo("10000");

		Map<String, Object> resultMap = testService.getProductDCInfo(ocCart);
		// Map<String, Object> resultMap = cartService.setCartList(ocCart);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "장바구니 메인", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "isLogin", value = "로그인여부", required = true, dataType = "boolean", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "sessionId", value = "세션아이디", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.1/test/cart-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCartList1(Parameter<OcCart> parameter) throws Exception {

//		Parameter<OcCart> parameter = new

		OcCart ocCart = new OcCart();

		ocCart.setLogin(true);
		ocCart.setSessionId("AF270F37345384B326B03B737C000C49PXXX");
		ocCart.setMemberNo("MB00000002");
		ocCart.setMemberTypeCode("10000");
		ocCart.setSiteNo("10000");
		ocCart.setDeviceCode("10000");
		ocCart.setChnnlNo("10000");

		Map<String, Object> resultMap = testService.getProductDCInfo1(ocCart);
		// Map<String, Object> resultMap = cartService.setCartList(ocCart);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "GNB 영역 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dispPageNo", value = "전시페이지번호", required = true, dataType = "string", paramType = "query", defaultValue = "1000011"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "upCtgrNo", value = "상위 전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrLevel", value = "카테고리 레벨", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/test/gnb", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGnb(Parameter<GnbVO> parameter) throws Exception {

		DpCategory dpCategory = new DpCategory();
		dpCategory.setSiteNo("10000");
		dpCategory.setChnnlNo("10000");

		return UtilsREST.responseOk(parameter, testService.getDpCategoryGnb(dpCategory));
	}

	@ApiOperation(value = "GNB 영역 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dispPageNo", value = "전시페이지번호", required = true, dataType = "string", paramType = "query", defaultValue = "1000011"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "upCtgrNo", value = "상위 전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrLevel", value = "카테고리 레벨", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.1/test/gnb", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGnb1(Parameter<GnbVO> parameter) throws Exception {

		DpCategory dpCategory = new DpCategory();
		dpCategory.setSiteNo("10000");
		dpCategory.setChnnlNo("10000");

		return UtilsREST.responseOk(parameter, displayCategoryService.getAllDpCategoryList(dpCategory));
	}
}
