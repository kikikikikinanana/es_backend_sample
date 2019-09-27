/**
 * 
 */
package kr.co.shop.web.board.controller;

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
import kr.co.shop.web.board.model.master.BdBulkBuyInquiry;
import kr.co.shop.web.board.service.BulkBuyInquiryService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : BulkBuyInquiry.java
 * @Project : shop.backend
 * @Date : 2019. 3. 15.
 * @Author : Kimyounghyun
 */
@Slf4j
@RestController
@RequestMapping("api")
public class BulkBuyInquiryController extends BaseController {

	@Autowired
	BulkBuyInquiryService bulkBuyInquiryService;

	@ApiOperation(value = "단체주문문의 등록", notes = "단체주문문의를 등록한다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "문의자번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "inqryTitleText", value = "문의제목", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "inqryContText", value = "문의내용", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "indvdlInfoColctAgreeYn", value = "개인정보수집동의여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "v1.0/board/bulk-buy-inquiry/create" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBulkBuyInquiry(Parameter<BdBulkBuyInquiry> parameter) throws Exception {
		BdBulkBuyInquiry bdBulkBuyInquiry = parameter.get();
		bulkBuyInquiryService.setBulkBuyInquiry(bdBulkBuyInquiry);

		return UtilsREST.responseOk(parameter);
	}

}
