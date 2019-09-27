package kr.co.shop.web.promotion.controller;

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
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.model.master.PrCouponApplyProduct;
import kr.co.shop.web.promotion.service.CouponService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class CouponController extends BaseController {
	@Autowired
	CouponService couponService;

	@ApiOperation(value = "회원 쿠폰 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/coupon/member-coupon/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberCouponList(Parameter<PrCoupon> parameter) throws Exception {
		Pageable<PrCoupon, PrCoupon> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = couponService.getMemberCouponList(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "다운로드 가능 쿠폰 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/coupon/download-coupon/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDownloadCouponList(Parameter<PrCoupon> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, couponService.getDownloadCouponList(parameter.get()));
	}

	@ApiOperation(value = "회원 쿠폰 다운로드", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cpnNos", value = "쿠폰번호배열", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/coupon/member-coupon/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertMemberCoupon(Parameter<PrCoupon> parameter) throws Exception {
		Map<String, Object> resultMap = couponService.insertMemberCoupon(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "회원 지류쿠폰 다운로드", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "paperNoText", value = "지류번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/coupon/paper/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertCouponPaper(Parameter<PrCoupon> parameter) throws Exception {
		Map<String, Object> resultMap = couponService.insertCouponPaper(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "쿠폰 상품 리스트", httpMethod = "GET", protocols = "http")
	@PostMapping(value = "v1.0/promotion/coupon/product/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cpnNo", value = "쿠폰번호", required = true, dataType = "string", paramType = "query") })
	public ResponseEntity<?> getCouponApplyProductList(Parameter<PrCouponApplyProduct> parameter) throws Exception {
		Map<String, Object> resultMap = couponService.getCouponApplyProductList(parameter);

		return UtilsREST.responseOk(parameter, resultMap);
	}
}