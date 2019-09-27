package kr.co.shop.web.board.controller;

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
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.board.model.master.BdFaq;
import kr.co.shop.web.board.service.BdFaqService;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class BdFaqController extends BaseController {

	@Autowired
	BdFaqService faqService;

	/**
	 * @Desc : FAQ 메인페이지 호출
	 * @Method Name : getInqryCreateForm
	 * @Date : 2019. 3. 21.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 메인 ", notes = "FAQ메인 BEST TOP10리스트 호출", httpMethod = "GET", protocols = "http", response = BdFaq.class, responseContainer = "Map")
	@GetMapping(value = { "/v1.0/board/faq/read-faq-main" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqMain(Parameter<BdFaq> parameter) throws Exception {
		Map<String, Object> faqMap = faqService.getFaqBest10();
		return UtilsREST.responseOk(parameter, faqMap);
	}

	/**
	 * @Desc : parameter가 있을시에 관련 유형 호출
	 * @Method Name : readFaqMainByParam
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 메인(parameter 있을시에) ", notes = "FAQ", httpMethod = "GET", protocols = "http", response = BdFaq.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeCode", value = "상담유형코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "cnslTypeDtlCode", value = "상담유형상세코드", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/board/faq/read-faq-main-byparam" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqMainByParam(Parameter<BdFaq> parameter) throws Exception {
		Pageable<BdFaq, BdFaq> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());
		pageable.getBean().setCnslTypeCode(parameter.get().getCnslTypeCode());
		pageable.getBean().setCnslTypeDtlCode(parameter.get().getCnslTypeDtlCode());

		Map<String, Object> faqMap = faqService.getFaqMain(pageable);

		return UtilsREST.responseOk(parameter, faqMap);
	}

	/**
	 * @Desc : FAQ 대분류 클릭시 리스트 조회
	 * @Method Name : readCnslTypeCode
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 대분류 ", notes = "대분류 클릭시 소분류 가져오기", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeCode", value = "상담유형코드", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/board/faq/read-faq-cnsltype" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readCnslTypeCode(Parameter<BdFaq> parameter) throws Exception {
		Pageable<BdFaq, BdFaq> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> faqMap = faqService.getFaqByCnslTypeCode(pageable);

		return UtilsREST.responseOk(parameter, faqMap);
	}

	/**
	 * @Desc : FAQ 소분류 클릭시 리스트 조회
	 * @Method Name : readFaqCnslTypeDtlCode
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 소분류 ", notes = "소분류 클릭시 FAQ리스트 가져오기", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeCode", value = "상담유형코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "cnslTypeDtlCode", value = "상담유형상세코드", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/board/faq/read-faq-dtlcode" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqByDtlCode(Parameter<BdFaq> parameter) throws Exception {
		Pageable<BdFaq, BdFaq> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> faqMap = faqService.getFaqByCnslDtlCode(pageable);

		return UtilsREST.responseOk(parameter, faqMap);
	}

	/**
	 * @Desc : FAQ 검색
	 * @Method Name : readFaqSearch
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 검색", notes = "검색어로 FAQ리스트 가져오기", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "searchValue", value = "FAQ 검색어", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/board/faq/read-faq-search" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqSearch(Parameter<BdFaq> parameter) throws Exception {
		Pageable<BdFaq, BdFaq> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());
		pageable.getBean().setSearchValue(parameter.get().getSearchValue());

		Map<String, Object> faqMap = faqService.getFaqBySearchValue(pageable);

		return UtilsREST.responseOk(parameter, faqMap);
	}

	/**
	 * @Desc : 상담상세 코드 가져오기
	 * @Method Name : readFaqDtlCode
	 * @Date : 2019. 4. 5.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상담 상세코드 가져오기", notes = "cnslTypeCode로 상세코드 가져오기", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeCode", value = "상담유형코드", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = { "/v1.0/board/faq/read-faq-dtlCode" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqDtlCode(Parameter<BdFaq> parameter) throws Exception {
		Map<String, Object> faqDtlCode = faqService.getFaqDtlCode(parameter.get());

		return UtilsREST.responseOk(parameter, faqDtlCode);
	}

	/**
	 * @Desc : MO고객센터 메인에서 대분류 클릭시
	 * @Method Name : readFaqCnslTypeCodeMO
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 대분류 MO ", notes = "대분류 클릭시 소분류 가져오기", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeCode", value = "상담유형코드", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = { "/v1.0/board/faq/read-faq-top10-cnsltype-mo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqCnslTypeCodeMO(Parameter<BdFaq> parameter) throws Exception {
		Pageable<BdFaq, BdFaq> pageable = new Pageable<>(parameter);

		Map<String, Object> faqMap = faqService.getFaqByCnslTypeCodeMO(pageable);

		return UtilsREST.responseOk(parameter, faqMap);
	}

	/**
	 * @Desc : MO FAQ 파라메터 있을시에
	 * @Method Name : readFaqMainByParamMO
	 * @Date : 2019. 5. 16.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 메인MO(parameter 있을시에) ", notes = "FAQ", httpMethod = "GET", protocols = "http", response = BdFaq.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cnslTypeCode", value = "상담유형코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "cnslTypeDtlCode", value = "상담유형상세코드", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = { "/v1.0/board/faq/read-faq-main-by-param-mo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFaqMainByParamMO(Parameter<BdFaq> parameter) throws Exception {
		Pageable<BdFaq, BdFaq> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());
		pageable.getBean().setCnslTypeCode(parameter.get().getCnslTypeCode());
		pageable.getBean().setCnslTypeDtlCode(parameter.get().getCnslTypeDtlCode());

		Map<String, Object> faqMap = faqService.getFaqMainByParamMO(pageable);

		return UtilsREST.responseOk(parameter, faqMap);
	}

}