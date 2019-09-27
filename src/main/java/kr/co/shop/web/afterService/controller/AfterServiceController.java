package kr.co.shop.web.afterService.controller;

import java.util.HashMap;
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
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.afterService.model.master.OcAsAccept;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProduct;
import kr.co.shop.web.afterService.service.AfterServiceMessageService;
import kr.co.shop.web.afterService.service.AfterServiceService;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class AfterServiceController extends BaseController {

	@Autowired
	AfterServiceService afterServiceService;

	@Autowired
	AfterServiceMessageService afterServiceMessageService;

	@Autowired
	OrderService orderService;

	@ApiOperation(value = "A/S 신청 목록 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "isPageable", value = "pageable여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rowsPerPage", value = "페이지당로우갯수", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "pageNum", value = "페이지번호", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "fromDate", value = "검색시작날짜", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "toDate", value = "검색종료날짜", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtRltnFileSeq", value = "상품관련파일순번", required = true, dataType = "int", paramType = "body") })
	@PostMapping(value = "/v1.0/afterservice/getAfterserviceRequestList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAfterserviceRequestList(Parameter<OcAsAcceptProduct> parameter) throws Exception {

		Pageable<OcAsAcceptProduct, OcAsAcceptProduct> pageable = new Pageable<>(parameter);

		// list를 page단위로 일부 뽑을때 : Y / list를 전체 뽑을때 : N
		pageable.getBean().setIsPageable(parameter.get().getIsPageable());

		if (parameter.get().getRowsPerPage() != null && parameter.get().getPageNum() != null) {
			// 페이지당 로우 갯수 설정
			pageable.setRowsPerPage(parameter.get().getRowsPerPage());
			// set 페이지 넘버
			pageable.setPageNum(parameter.get().getPageNum());
		}

		// 상품관련파일순번 1 (대표)
		pageable.getBean().setPrdtRltnFileSeq(parameter.get().getPrdtRltnFileSeq());

		return UtilsREST.responseOk(parameter, afterServiceService.getAfterserviceRequestList(pageable));
	}

	@ApiOperation(value = "AS신청 페이지를 위한 사용중인 공통코드 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/afterservice/getUseCodeForAsRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUseCodeForAsRequest(Parameter<?> parameter) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(CommonCode.AS_GBN_CODE, afterServiceService.getUseCodeForAsRequest(CommonCode.AS_GBN_CODE));
		resultMap.put(CommonCode.AS_RSN_CODE, afterServiceService.getUseCodeForAsRequest(CommonCode.AS_RSN_CODE));
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "주문 상품 목록 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "isPageable", value = "pageable여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rowsPerPage", value = "페이지당로우갯수", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "pageNum", value = "페이지번호", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "fromDate", value = "검색시작날짜", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "toDate", value = "검색종료날짜", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderPrdtStatCodeList", value = "검색종료날짜", required = true, dataType = "String[]", paramType = "body"),
			@ApiImplicitParam(name = "prdtRltnFileSeq", value = "상품관련파일순번", required = true, dataType = "int", paramType = "body") })
	@PostMapping(value = "/v1.0/afterservice/getOrderProductList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOrderProductList(Parameter<OcOrderProduct> parameter) throws Exception {

		Pageable<OcOrderProduct, OcOrderProduct> pageable = new Pageable<>(parameter);

		// list를 page단위로 일부 뽑을때 : Y / list를 전체 뽑을때 : N
		pageable.getBean().setIsPageable(parameter.get().getIsPageable());

		if (parameter.get().getRowsPerPage() != null && parameter.get().getPageNum() != null) {
			// 페이지당 로우 갯수 설정
			pageable.setRowsPerPage(parameter.get().getRowsPerPage());
			// set 페이지 넘버
			pageable.setPageNum(parameter.get().getPageNum());
		}

		// 상품관련파일순번 1 (대표)
		pageable.getBean().setPrdtRltnFileSeq(parameter.get().getPrdtRltnFileSeq());

		return UtilsREST.responseOk(parameter, afterServiceService.getOrderProductList(pageable));
	}

	/***************************************************************************************
	 * E : 이강수
	 **************************************************************************************/

	@ApiOperation(value = "AS 접수", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/afterservice/createOcAsAccept", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createOcAsAccept(Parameter<OcAsAccept> parameter) throws Exception {
		OcAsAccept ocAsAccept = parameter.get();

		Map<String, Object> resultMap = afterServiceService.setOcAsAccept(ocAsAccept);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "AS신청상세", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/afterservice/getAsAcceptDetailInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAsAcceptDetailInfo(Parameter<OcAsAccept> parameter) throws Exception {
		OcAsAccept ocAsAccept = parameter.get();

		Map<String, Object> resultMap = afterServiceService.getAsAcceptDetailInfo(ocAsAccept);
		resultMap.put(CommonCode.AS_WTHDRAW_RSN_CODE,
				afterServiceService.getUseCodeForAsRequest(CommonCode.AS_WTHDRAW_RSN_CODE));

		resultMap.put(CommonCode.AS_GBN_CODE, afterServiceService.getUseCodeForAsRequest(CommonCode.AS_GBN_CODE));
		resultMap.put(CommonCode.AS_RSN_CODE, afterServiceService.getUseCodeForAsRequest(CommonCode.AS_RSN_CODE));

		log.debug("##### asAcceptInfo: {}", resultMap.get("asAcceptInfo").toString());
		// afterServiceMessageService.asRepairImpsbltSendBack(ocAsAccept);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "AS접수 철회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/afterservice/setWithdrawalAccept", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setWithdrawalAccept(Parameter<OcAsAccept> parameter) throws Exception {
		OcAsAccept ocAsAccept = parameter.get();
		Map<String, Object> resultMap = afterServiceService.setWithdrawalAccept(ocAsAccept);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "AS 수정", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/afterservice/updateOcAsAccept", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateOcAsAccept(Parameter<OcAsAccept> parameter) throws Exception {
		OcAsAccept ocAsAccept = parameter.get();
		Map<String, Object> resultMap = afterServiceService.updateOcAsAccept(ocAsAccept);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "접수번호 번호 채번", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/afterservice/afterservice-get-accept-num", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAsAcceptNum(Parameter<OcAsAccept> parameter) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("asAcceptNo", orderService.createOrderSeq());

		return UtilsREST.responseOk(parameter, map);
	}
}