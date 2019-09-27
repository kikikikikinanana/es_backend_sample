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
import kr.co.shop.web.product.model.master.BdProductInquiry;
import kr.co.shop.web.product.service.ProductInquiryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ProductInquiryController extends BaseController {

	@Autowired
	private ProductInquiryService inquiryService;

	@ApiOperation(value = "상품 문의 조회", notes = "상품 문의 목록 조회", httpMethod = "POST", protocols = "http", response = BdProductInquiry.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "rowsPerPage", value = "페이지당 갯수", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "pageNum", value = "페이지 번호", required = true, dataType = "int", paramType = "body") })
	@PostMapping(value = "v1.0/product/inquiry", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getList(Parameter<Object> parameter) throws Exception {
		log.debug("상품 Q&A 목록 조회");
		Pageable<Object, BdProductInquiry> pageable = new Pageable<Object, BdProductInquiry>(parameter);
		Map<String, Object> param = (Map<String, Object>) parameter.get();
		pageable.setPageNum(UtilsNumber.toInt("" + param.get("pageNum")));
		pageable.setRowsPerPage(UtilsNumber.toInt("" + param.get("rowsPerPage")));

		Page<BdProductInquiry> productInquiryList = this.inquiryService.selectProductInquiry(pageable);

		return UtilsREST.responseOk(parameter, productInquiryList.getGrid());

	}

	@ApiOperation(value = "상품 문의 등록", notes = "입력값을 확인 후에 상품 문의 등록,수정,삭제", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeDtlCode", value = "문의유형", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "inqryTitleText", value = "제목", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "inqryContText", value = "문의내용", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pswdYn", value = "비밀글 설정", required = false, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/product/inquiry/crud", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(Parameter<BdProductInquiry> parameter) throws Exception {
		log.debug("상품 Q&A 등록");

		return UtilsREST.responseOk(parameter, inquiryService.crudProductInquiry(parameter.get()));
	}

}