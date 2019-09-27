package kr.co.shop.web.vendor.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.board.model.master.BdContactUs;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class VendorController extends BaseController {

	@Autowired
	VendorService vendorService;

	@ApiOperation(value = "입점문의 form data 조회", notes = "입점문의 화면에 필요한 data를 조회한다.", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "List")
	@GetMapping(value = "v1.0/vendor/contact-us-form", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getContactUsForm(Parameter<?> parameter) throws Exception {
		Map<String, Object> map = vendorService.getContactUsFormData();

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "입점문의 저장", notes = "입점문의 저장한다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "cnslTypeDtlCode", value = "상담유형상세코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "inqryTitleText", value = "문의제목", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "inqryContText", value = "문의내용", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "indvdlInfoColctAgreeYn", value = "개인정보수집동의여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/vendor/contact-us/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveVendorContactUs(Parameter<BdContactUs> parameter) throws Exception {
		BdContactUs params = parameter.get();
		vendorService.saveVendorContactUs(params);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "입점업체 기본정보조회", httpMethod = "GET")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "vndrNo", value = "입점업체 기본정보조회", required = true, dataType = "string", paramType = "query") })
	@GetMapping(value = "v1.0/vendor/getVendorInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getVendorInfo(Parameter<VdVendor> parameter) throws Exception {
		VdVendor param = parameter.get();
		VdVendor vendorInfo = vendorService.getVendorInfo(param);

		return UtilsREST.responseOk(parameter, vendorInfo);
	}

}