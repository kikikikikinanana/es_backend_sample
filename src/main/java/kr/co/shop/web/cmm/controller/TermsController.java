package kr.co.shop.web.cmm.controller;

import java.util.List;

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
import kr.co.shop.web.cmm.model.master.CmTerms;
import kr.co.shop.web.cmm.model.master.CmTermsDetail;
import kr.co.shop.web.cmm.service.TermsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class TermsController extends BaseController {

	@Autowired
	TermsService termsService;

	@ApiOperation(value = "개인정보처리방침 조회", notes = "현재 시행중인 개인정보처리방침과 이력(목록)을 조회한다.", httpMethod = "GET", protocols = "http", response = CmTermsDetail.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "termsSeq", value = "일련번호", required = false, dataType = "string", paramType = "body") })
	@GetMapping(value = { "v1.0/terms/privacy" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrivacyV10(Parameter<CmTerms> parameter) throws Exception {
		CmTerms cmTerms = parameter.get();
		CmTermsDetail privacy = termsService.getPrivacyWithList(cmTerms);

		return UtilsREST.responseOk(parameter, privacy);
	}

	@ApiOperation(value = "사이트이용약관 조회", notes = "현재 시행중인 사이트이용약관과 이력(목록)을 조회한다.", httpMethod = "GET", protocols = "http", response = CmTermsDetail.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "termsSeq", value = "일련번호", required = false, dataType = "string", paramType = "body") })
	@GetMapping(value = { "v1.0/terms/site" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSiteV10(Parameter<CmTerms> parameter) throws Exception {
		CmTerms cmTerms = parameter.get();
		CmTermsDetail privacy = termsService.getSiteWithList(cmTerms);

		return UtilsREST.responseOk(parameter, privacy);
	}

	@ApiOperation(value = "통합회원이용약관 조회", notes = "현재 시행중인 통합회원 이용약관과 이력(목록)을 조회한다.", httpMethod = "GET", protocols = "http", response = CmTermsDetail.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "termsSeq", value = "일련번호", required = false, dataType = "string", paramType = "body") })
	@GetMapping(value = { "v1.0/terms/member" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberV10(Parameter<CmTerms> parameter) throws Exception {
		CmTerms cmTerms = parameter.get();
		CmTermsDetail privacy = termsService.getMemberWithList(cmTerms);

		return UtilsREST.responseOk(parameter, privacy);
	}

	@PostMapping(value = { "/v1.0/terms/singup-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSingupTermsList(Parameter<CmTerms> parameter) throws Exception {
		CmTerms cmTerms = parameter.get();
		List<CmTermsDetail> singupList = termsService.getSingupTermsList(cmTerms);

		return UtilsREST.responseOk(parameter, singupList);
	}

}