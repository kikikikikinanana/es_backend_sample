package kr.co.shop.web.board.controller;

import java.util.HashMap;
import java.util.List;
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
import kr.co.shop.web.board.model.master.BdMemberCounsel;
import kr.co.shop.web.board.service.BdMemberCounselService;
import kr.co.shop.web.mypage.vo.MypageVO;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class BdMemberCounselController extends BaseController {

	@Autowired
	BdMemberCounselService counselService;

	@Autowired
	CommonCodeService commoncodeService;

	/**
	 * @Desc : 마이페이지 나의 상담내역 리스트 호출
	 * @Method Name : readInqryList
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 리스트 조회", notes = "문의내역 (목록)을 조회한다.", httpMethod = "POST", protocols = "http", response = BdMemberCounsel.class, responseContainer = "List")
	@PostMapping(value = { "/v1.0/board/member-counsel/inqry-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readInqryList(Parameter<BdMemberCounsel> parameter) throws Exception {
		Pageable<BdMemberCounsel, BdMemberCounsel> pageable = new Pageable<>(parameter);
		BdMemberCounsel counselParam = parameter.get();

		pageable.setRowsPerPage(counselParam.getRowsPerPage());
		pageable.setPageNum(counselParam.getPageNum());
		pageable.getBean().setCnslGbnCode(counselParam.getCnslGbnCode());
		pageable.getBean().setMemberNo(counselParam.getMemberNo());

		Map<String, Object> counselMap = counselService.getInqryList(pageable);

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : 문의 내역 상세 조회
	 * @Method Name : readInqryDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 상세 조회", notes = "문의내역 상세를  조회한다.", httpMethod = "GET", protocols = "http", response = BdMemberCounsel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberCnslSeq", value = "일련번호", required = false, dataType = "string", paramType = "body") })
	@GetMapping(value = { "v1.0/board/member-counsel/read-inqry-Detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readInqryDetail(Parameter<BdMemberCounsel> parameter) throws Exception {
		BdMemberCounsel bdMemberCounsel = parameter.get();

		Map<String, Object> counselMap = counselService.getUpdateInqryForm(bdMemberCounsel);

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : 고객의소리 상세 가져오기
	 * @Method Name : readVocDetail
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "고객의소리 상세 조회", notes = "고객의소리 상세를  조회한다.", httpMethod = "GET", protocols = "http", response = BdMemberCounsel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberCnslSeq", value = "일련번호", required = false, dataType = "string", paramType = "body") })
	@GetMapping(value = { "v1.0/board/member-counsel/read-voc-Detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readVocDetail(Parameter<BdMemberCounsel> parameter) throws Exception {
		BdMemberCounsel bdMemberCounsel = parameter.get();

		Map<String, Object> counselMap = counselService.getUpdateVocForm(bdMemberCounsel);

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : 1:1문의 등록 페이지 호출
	 * @Method Name : readInqryCreateForm
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 등록 폼", notes = "문의내역 등록 페이지 호출", httpMethod = "GET", protocols = "http", response = BdMemberCounsel.class, responseContainer = "Map")
	@GetMapping(value = {
			"v1.0/board/member-counsel/read-inquiry-create-form" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readInqryCreateForm(Parameter<BdMemberCounsel> parameter) throws Exception {
		BdMemberCounsel bdMemberCounsel = parameter.get();

		Map<String, Object> counselMap = counselService.getCreateInqryForm(bdMemberCounsel);

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : 1:1 문의 등록
	 * @Method Name : createInqryDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 등록", notes = "문의내역 을 등록한다.", httpMethod = "POST", protocols = "http")
	@PostMapping(value = {
			"/v1.0/board/member-counsel/create-inquiry-Detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createInqryDetail(Parameter<BdMemberCounsel> parameter) throws Exception {
		BdMemberCounsel bdMemberCounsel = parameter.get();

		Map<String, Object> rsMap = counselService.setInqryDetail(bdMemberCounsel);

		return UtilsREST.responseOk(parameter, rsMap);
	}

	/**
	 * @Desc : 1:1문의 수정
	 * @Method Name : updateInqryDetail
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 수정", notes = "문의내역 을 수정한다.", httpMethod = "POST", protocols = "http")
	@PostMapping(value = {
			"/v1.0/board/member-counsel/update-inquiry-Detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateInqryDetail(Parameter<BdMemberCounsel> parameter) throws Exception {
		BdMemberCounsel bdMemberCounsel = parameter.get();

		Map<String, Object> rsMap = counselService.updateInqryDetail(bdMemberCounsel);

		return UtilsREST.responseOk(parameter, rsMap);
	}

	/**
	 * @Desc : 1:1 문의 삭제
	 * @Method Name : removeInqry
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 삭제", notes = "문의내역 을 삭제한다.", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/board/member-counsel/remove-counsel" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeInqry(Parameter<BdMemberCounsel> parameter) throws Exception {
		BdMemberCounsel bdMemberCounsel = parameter.get();

		Map<String, Object> rsMap = counselService.deleteInqry(bdMemberCounsel);

		return UtilsREST.responseOk(parameter, rsMap);
	}

	/**
	 * @Desc : 고객의 소리 등록 페이지 호출
	 * @Method Name : getVocCreateForm
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "고객의소리 등록 폼", notes = "고객의소리 등록 페이지 호출", httpMethod = "GET", protocols = "http", response = BdMemberCounsel.class, responseContainer = "Map")
	@GetMapping(value = {
			"/v1.0/board/member-counsel/get-voc-create-form" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getVocCreateForm(Parameter<BdMemberCounsel> parameter) throws Exception {
		Map<String, Object> counselMap = counselService.getCreateVocForm(parameter.get());

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : 고객의 소리 등록
	 * @Method Name : setVocDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "고객의 소리 등록", notes = "고객의 소리 등록.", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/board/member-counsel/set-voc-detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setVocDetail(Parameter<BdMemberCounsel> parameter) throws Exception {
		Map<String, Object> rsMap = counselService.setVocDetail(parameter.get());

		return UtilsREST.responseOk(parameter, rsMap);
	}

	/**
	 * @Desc : 고객의소리 수정
	 * @Method Name : setVocDetail
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "고객의 소리 수정", notes = "고객의 소리 수정.", httpMethod = "POST", protocols = "http")
	@PostMapping(value = {
			"/v1.0/board/member-counsel/update-voc-detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateVocDetail(Parameter<BdMemberCounsel> parameter) throws Exception {
		Map<String, Object> rsMap = counselService.updateVocDetail(parameter.get());

		return UtilsREST.responseOk(parameter, rsMap);
	}

	/**
	 * @Desc : 앞의 selectBox의 상담유형 상세코드 가져오기
	 * @Method Name : readCnslDetailList
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상담유형상세 코드를 가져온다.", notes = "codeField로  상담유형상세코드를 가지고 온다.", httpMethod = "POST", protocols = "http")
	@PostMapping(value = {
			"/v1.0/board/member-counsel/read-cnsl-detail-list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readCnslDetailList(Parameter<SyCodeDetail> parameter) throws Exception {
		List<SyCodeDetail> codeList = commoncodeService.getUseCodeByAddInfo1(parameter.get());

		return UtilsREST.responseOk(parameter, codeList);
	}

	/**
	 * @Desc : 회원의 장바구니 개수 가져오기
	 * @Method Name : readCnslDetailList
	 * @Date : 2019. 5. 21.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "회원의 장바구니 개수를 가져온다.", notes = "회원의 장바구니 개수를 가져온다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memeberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/mypage/member-cart-count" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readMemberCartCount(Parameter<MypageVO> parameter) throws Exception {
		// List<SyCodeDetail> codeList =
		// commoncodeService.getUseCodeByAddInfo1(parameter.get());
		Map<String, Object> rsMap = new HashMap<String, Object>();
		rsMap.put("memberCartCount", 5);
		return UtilsREST.responseOk(parameter, rsMap);
	}

	/**
	 * @Desc : 상담리스트에서 상세보기시에 파일리스트 호출
	 * @Method Name : readAttachFile
	 * @Date : 2019. 4. 10.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "첨부파일 가져오기", notes = "마이페이지에서 상세보기시 memberCnslSeq로 첨부파일 가져오기", httpMethod = "GET", protocols = "http", response = BdMemberCounsel.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberCnslSeq", value = "일련번호", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = { "/v1.0/board/member-counsel/read-attach-file" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readAttachFile(Parameter<BdMemberCounsel> parameter) throws Exception {
		Map<String, Object> counselMap = counselService.getAttachFile(parameter.get());

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : MO 상담리스트 페이지 호출
	 * @Method Name : readInqryListMO
	 * @Date : 2019. 5. 28.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 리스트 조회MO", notes = "문의내역 (목록)을 조회한다.", httpMethod = "POST", protocols = "http", response = BdMemberCounsel.class, responseContainer = "List")
	@PostMapping(value = { "/v1.0/board/member-counsel/inqry-list-mo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readInqryListMO(Parameter<BdMemberCounsel> parameter) throws Exception {
		Pageable<BdMemberCounsel, BdMemberCounsel> pageable = new Pageable<>(parameter);
		BdMemberCounsel counselParam = parameter.get();

		pageable.setRowsPerPage(counselParam.getRowsPerPage());
		pageable.setPageNum(counselParam.getPageNum());
		pageable.getBean().setCnslGbnCode(counselParam.getCnslGbnCode());
		pageable.getBean().setMemberNo(counselParam.getMemberNo());

		Map<String, Object> counselMap = counselService.getInqryListMO(pageable);

		return UtilsREST.responseOk(parameter, counselMap);
	}

	/**
	 * @Desc : 마이페이지 상담리스트 스크롤 이벤트
	 * @Method Name : readInqryListScroll
	 * @Date : 2019. 5. 30.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "문의내역 리스트 조회MO", notes = "문의내역 (목록)을 조회한다.", httpMethod = "POST", protocols = "http", response = BdMemberCounsel.class, responseContainer = "List")
	@PostMapping(value = {
			"/v1.0/board/member-counsel/inqry-list-scroll" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readInqryListScroll(Parameter<BdMemberCounsel> parameter) throws Exception {
		Pageable<BdMemberCounsel, BdMemberCounsel> pageable = new Pageable<>(parameter);
		BdMemberCounsel counselParam = parameter.get();

		pageable.setRowsPerPage(counselParam.getRowsPerPage());
		pageable.setPageNum(counselParam.getPageNum());
		pageable.getBean().setCnslGbnCode(counselParam.getCnslGbnCode());
		pageable.getBean().setMemberNo(counselParam.getMemberNo());

		Map<String, Object> counselMap = counselService.getInqryListScroll(pageable);

		return UtilsREST.responseOk(parameter, counselMap);
	}

}