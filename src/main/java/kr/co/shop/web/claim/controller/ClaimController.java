package kr.co.shop.web.claim.controller;

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
import kr.co.shop.web.claim.model.master.OcClaim;
import kr.co.shop.web.claim.model.master.OcClaimCertificationHistory;
import kr.co.shop.web.claim.model.master.OcClaimProduct;
import kr.co.shop.web.claim.service.ClaimMessageService;
import kr.co.shop.web.claim.service.ClaimService;
import kr.co.shop.web.mypage.service.MypageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ClaimController extends BaseController {

	@Autowired
	ClaimService claimService;

	@Autowired
	private ClaimMessageService claimMessageService;

	@Autowired
	private MypageService mypageService;

	@ApiOperation(value = "주문취소/교환/반품 목록 클레임 상품 목록 조회 ", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rowsPerPage", value = "페이지당로우갯수", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "pageNum", value = "페이지번호", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "clmGbnCode", value = "클레임구분코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "fromDate", value = "검색시작날짜", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "toDate", value = "검색종료날짜", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/claim/getClaimProductList" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getClaimProductList(Parameter<OcClaim> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, claimService.getClaimProductList(parameter));
	}

	@ApiOperation(value = "클레임 접수철회 업데이트 ", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "clmNo", value = "클레임번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "clmWthdrawRsnCode", value = "클레임철회사유코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "clmWthdrawContText", value = "클레임철회사유내용", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "clmStatCode", value = "클레임상태코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "수정자번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "/v1.0/claim/updateClaimWthdraw" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateClaimWthdraw(Parameter<OcClaim> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, claimService.updateClaimWthdraw(parameter.get()));
	}

	@ApiOperation(value = "클레임 상세 업데이트 ", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/claim/updateClaimDetail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateClaimDetail(Parameter<OcClaim> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, claimService.updateClaimDetail(parameter.get()));
	}

	@ApiOperation(value = "상품 판매종료인지 아닌지 Y/N 리턴", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/claim/getProductSellEndYn" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductSellEndYn(Parameter<OcClaimProduct> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, claimService.getProductSellEndYn(parameter.get()));
	}

	@ApiOperation(value = "취소상품 장바구니 담기", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "/v1.0/claim/setCancelPrdtToCart" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setCancelPrdtToCart(Parameter<OcClaim> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, claimService.setCancelPrdtToCart(parameter.get()));
	}

	/*********************************************************************************************************************
	 * E : 이강수
	 *********************************************************************************************************************/

	/*********************************************************************************************************************
	 * S : kth
	 *********************************************************************************************************************/

	@ApiOperation(value = "클레임 상세 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "clmNo", value = "클레임번호", required = true, dataType = "string", paramType = "body"), })
	@PostMapping(value = { "/v1.0/claim/getClaimDetailInfo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getClaimDetailInfo(Parameter<OcClaim> parameter) throws Exception {
		// claimMessageService.redempAmtOccurrence(parameter.get());
		return UtilsREST.responseOk(parameter, claimService.getClaimDetailInfo(parameter.get()));
	}

	/**
	 * @Desc : 클레임 환불계좌 인증
	 * @Method Name : claimAccountAuth
	 * @Date : 2019. 5. 3.
	 * @Author : KTH
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "클레임 환불계좌 인증", notes = "클레임 환불계좌 인증", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "acntNoText", value = "계좌번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "bankCode", value = "은행코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "acntHldrName", value = "예금주", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/claim/claim-account-auth", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimAccountAuth(Parameter<OcClaimCertificationHistory> parameter) throws Exception {
		OcClaimCertificationHistory ocClaimCertificationHistory = parameter.get();

		// 공통 프로젝트의 계좌인증에서 인증을 처리하고 결과를 리턴받음
		return UtilsREST.responseOk(parameter, claimService.setClaimAccountAuthProc(ocClaimCertificationHistory));
	}

	@ApiOperation(value = "클레임 취소 처리", notes = "클레임 취소 처리", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderPrdtSeqs", value = "주문상품순번", required = true, dataType = "array", paramType = "body") })
	@PostMapping(value = "/v1.0/claim/claim-cancel-proc", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimCancelProc(Parameter<OcClaim> parameter) throws Exception {
		OcClaim ocClaim = parameter.get();

		return UtilsREST.responseOk(parameter, claimService.setClaimCancelProc(ocClaim));
	}

	@ApiOperation(value = "클레임 교환/반품 접수 처리", notes = "클레임 교환/반품 접수 처리", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderPrdtSeqs", value = "주문상품순번", required = true, dataType = "array", paramType = "body") })
	@PostMapping(value = "/v1.0/claim/claim-exchange-return-proc", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimExchangeProc(Parameter<OcClaim> parameter) throws Exception {
		OcClaim ocClaim = parameter.get();

		return UtilsREST.responseOk(parameter, claimService.setClaimExchangeReturnProc(ocClaim));
	}
	/*********************************************************************************************************************
	 * E : kth
	 *********************************************************************************************************************/
}