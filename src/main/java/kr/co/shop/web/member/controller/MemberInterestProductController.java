package kr.co.shop.web.member.controller;

import java.util.LinkedHashMap;
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
import kr.co.shop.web.member.model.master.MbMemberInterestProduct;
import kr.co.shop.web.member.service.MemberInterestProductService;
import kr.co.shop.web.product.model.master.MbMemberInterestProductWrapper;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class MemberInterestProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private MemberInterestProductService memberInterestProductService;

	@ApiOperation(value = "회원 관심상품 목록 조회", notes = "회원 쇼핑수첩 항목 찜한상품, 알림상품 목록 조회", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "wrhsAlertReqYn", value = "입고알림신청여부", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "String", paramType = "body") })
	@PostMapping(value = "/v1.0/member/interest-product/get-interest-product-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getInterestProductList(Parameter<MbMemberInterestProduct> parameter) throws Exception {
		log.debug("회원 관심상품 목록 조회");
		MbMemberInterestProduct interestProduct = parameter.get();
		PageableProduct<MbMemberInterestProduct, MbMemberInterestProductWrapper> pageableProduct = new PageableProduct<MbMemberInterestProduct, MbMemberInterestProductWrapper>(
				parameter);
		pageableProduct.setRowsPerPage(interestProduct.getRowsPerPage());
		pageableProduct.setCondition(interestProduct.getSiteNo(), interestProduct.getChnnlNo());
		pageableProduct.setMemberInterest(interestProduct.getMemberNo(), interestProduct.getWrhsAlertReqYn());
		// pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_CUSTOM,
		// "RGST_DTM", "DESC");
		pageableProduct.setUseTableMapping("mb_member_interest_product", new LinkedHashMap<String, String>() {
			{
				put("member_no", interestProduct.getMemberNo());
				put("wrhs_alert_req_yn", interestProduct.getWrhsAlertReqYn());
			}
		}, null);

		return UtilsREST.responseOk(parameter,
				this.memberInterestProductService.getDisplayInterestProductList(pageableProduct));
	}

	@ApiOperation(value = "회원 관심상품 목록 조회", notes = "회원 쇼핑수첩 항목 찜한상품, 알림상품 목록 조회(회원관심상품만 조회)", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "wrhsAlertReqYn", value = "입고알림신청여부", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "String", paramType = "body") })
	@PostMapping(value = "/v1.0/member/interest-product/get-only-member-interest-product-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOnlyMemberInterestProductList(Parameter<MbMemberInterestProduct> parameter)
			throws Exception {
		log.debug("회원 관심상품 목록 조회");
		return UtilsREST.responseOk(parameter,
				this.memberInterestProductService.getOnlyMemberInterestProductList(parameter.get()));
	}

	@ApiOperation(value = "회원 관심상품 목록 삭제", notes = "회원 쇼핑수첩 항목 찜한상품, 알림상품 선택 상품 삭제", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "wrhsAlertReqYn", value = "입고알림신청여부", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "prdtOptnNo", value = "상품옵션번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "String", paramType = "body") })
	@PostMapping(value = "/v1.0/member/interest-product/delete-interest-product-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteInterestProductList(Parameter<MbMemberInterestProduct[]> parameter)
			throws Exception {
		log.debug("회원 관심상품 리스트 삭제");
		return UtilsREST.responseOk(parameter,
				this.memberInterestProductService.deleteInterestProductList(parameter.get()));
	}

	@ApiOperation(value = "회원 관심상품 등록", notes = "회원 쇼핑수첩 항목 찜한상품, 알림상품 등록", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "ctgrNo", value = "카테고리번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "prdtOptnNo", value = "상품옵션번호", required = false, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "wrhsAlertReqYn", value = "입고알림신청여부", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "wrhsAlertSendYn", value = "입고알림발송여부", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "hdphnNoText", value = "휴대폰번호", required = false, dataType = "String", paramType = "body") })
	@PostMapping(value = "/v1.0/member/interest-product/crud-interest-product", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertInterestProduct(Parameter<MbMemberInterestProduct> parameter) throws Exception {
		log.debug("회원 관심상품 추가");
		return UtilsREST.responseOk(parameter, this.memberInterestProductService.crudInterestProduct(parameter.get()));
	}

	@ApiOperation(value = "회원 관심상품 여부 조회", notes = "회원 관심상품 여부 조회", httpMethod = "POST", protocols = "http", response = Map.class)
	@PostMapping(value = "/v1.0/member/interest-product/get-interest-product-yn", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getInterestProductYn(Parameter<MbMemberInterestProduct> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, this.memberInterestProductService.getInterestProductYn(parameter.get()));
	}
}
