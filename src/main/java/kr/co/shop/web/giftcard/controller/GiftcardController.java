package kr.co.shop.web.giftcard.controller;

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
import kr.co.shop.interfaces.module.payment.nice.model.BalanceRequest;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.giftcard.model.master.OcGiftCardOrder;
import kr.co.shop.web.giftcard.model.master.PdGiftCard;
import kr.co.shop.web.giftcard.model.master.RvGiftCardComparison;
import kr.co.shop.web.giftcard.service.GiftcardService;
import kr.co.shop.web.giftcard.vo.GiftcardVo;
import kr.co.shop.web.member.model.master.MbMemberGiftCard;
import kr.co.shop.web.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class GiftcardController extends BaseController {

	@Autowired
	private GiftcardService giftcardService;

	@Autowired
	private OrderService orderService;

	@ApiOperation(value = "기프트카드 잔액조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cardNo", value = "선불카드번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/giftcard/balance", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBalance(Parameter<BalanceRequest> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getBalance(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "giftCardName", value = "카드명", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "giftCardNo", value = "기프트카드번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "cardPinNoText", value = "인증번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/giftcard/regist-mygiftcard", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registMyGiftcard(Parameter<GiftcardVo> parameter) throws Exception {
		Map<String, Object> map = giftcardService.registMyGiftcard(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "상품기프트카드 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-multiuse-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGiftcardMultiuseList(Parameter<PdGiftCard> parameter) throws Exception {
		Pageable<PdGiftCard, PdGiftCard> pageable = new Pageable<>(parameter);
		PdGiftCard param = parameter.get();

		pageable.setPageNum(param.getPageNum());
		pageable.setRowsPerPage(12);

		Map<String, Object> map = giftcardService.getGiftcardMultiuseList(pageable);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 구매/선물 내용 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-saleagencies-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGiftcardSaleAgenciesInfo(Parameter<OcGiftCardOrder> parameter) throws Exception {
		OcGiftCardOrder param = parameter.get();

		Map<String, Object> map = giftcardService.getGiftCardSaleAgenciesInfo(param);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "상품기프트카드 상세", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "giftCardNo", value = "상품기프트카드 번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/giftcard/pdgiftcard-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPdGiftCardInfo(Parameter<PdGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getPdGiftCardInfo(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 결제 번호 채번", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-get-order-num", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGiftCardOrderNum(Parameter<OcGiftCardOrder> parameter) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
//			String orderNum = orderService.createOrderSeq();
//			map.put("orderNum", orderNum);
			map.put("resCode", "0000");
			map.put("resMsg", "SUCCESS");
			map.put("resData", orderService.createOrderSeq());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "9999");
			map.put("resMsg", "ERROR");
			map.put("resData", null);
		}

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 KCP 구매/선물", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-kcp-payment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> payGiftCardKcp(Parameter<OcGiftCardOrder> parameter) throws Exception {
		Map<String, Object> map = giftcardService.setGiftCardKcp(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "카카오톡 선불번호 체크", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/kakao-coupon-check", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getKakaoCouponCheck(Parameter<GiftcardVo> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getKakaoCouponCheck(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "카카오톡 쿠폰 교환 ", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/kakao-coupon-exchange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getKakaoCouponExchange(Parameter<GiftcardVo> parameter) throws Exception {
		GiftcardVo giftCardVO = parameter.get();
		giftCardVO.setGiftCardOrderNo(orderService.createOrderSeq());

		Map<String, Object> map = giftcardService.setKakaoCouponExchange(giftCardVO);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "카카오톡 쿠폰 교환 정보 조회 ", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/kakao-exchange-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getKakaoExchangeInfo(Parameter<GiftcardVo> parameter) throws Exception {
		GiftcardVo giftCardVO = parameter.get();
		Map<String, Object> map = giftcardService.getKakaoExchangeInfo(giftCardVO);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "my 기프트카드 리스트", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-mycard-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> myGiftCardList(Parameter<MbMemberGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getGiftCardList(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "my 기프트카드 리스트 카드등록해지", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-mycard-termination", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> myGiftCardTermination(Parameter<MbMemberGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.setGiftCardTermination(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 대표카드 구분 변경", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-modify-rprsnt-card", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setRprsntCard(Parameter<MbMemberGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.setRprsntCard(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 이름 변경", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-modify-name", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setGiftCardName(Parameter<MbMemberGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.setGiftCardName(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "my 기프트카드 잔액조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/get-mygiftcard-balance", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyGiftCardBalance(Parameter<BalanceRequest> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getMyGiftCardBalance(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "my 기프트카드 이용내역 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-use-history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUseHistory(Parameter<RvGiftCardComparison> parameter) throws Exception {
		Pageable<RvGiftCardComparison, RvGiftCardComparison> pageable = new Pageable<>(parameter);
		RvGiftCardComparison param = parameter.get();

		pageable.setPageNum(param.getPageNum());
		pageable.setRowsPerPage(10);
		pageable.setBean(param);

		Map<String, Object> map = giftcardService.getUseHistory(pageable);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "내 보유카드 체크", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/get-mygiftcard-check", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyGiftCardCheck(Parameter<MbMemberGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getMyGiftCardCheck(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "내 보유카드 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/get-mygiftcard-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyGiftCardList(Parameter<MbMemberGiftCard> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getMyGiftCardList(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "잔액이전", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/giftcard-amount-escalation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setGiftCardAmountEscalation(Parameter<GiftcardVo> parameter) throws Exception {
		Map<String, Object> map = giftcardService.setGiftCardAmountEscalation(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 구매, 선물, 충전 결제 내역 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/get-giftcard-history-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGiftCardHistoryList(Parameter<OcGiftCardOrder> parameter) throws Exception {
		Pageable<OcGiftCardOrder, OcGiftCardOrder> pageable = new Pageable<>(parameter);
		OcGiftCardOrder param = parameter.get();

		pageable.setPageNum(param.getPageNum());
		pageable.setRowsPerPage(10);
		pageable.setBean(param);

		Map<String, Object> map = giftcardService.getGiftCardHistoryList(pageable);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 충전 완료 후 충전 정보 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/get-giftcard-charge-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGiftCardChargeInfo(Parameter<BalanceRequest> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getGiftCardChargeInfo(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기프트카드 구매, 선물, 충전 결제 내역 취소", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/giftcard/set-giftcard-history-cancel", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setGiftCardHistoryCancel(Parameter<OcGiftCardOrder> parameter) throws Exception {
		Map<String, Object> map = giftcardService.setGiftCardHistoryCancel(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}
	
	@ApiOperation(value = "(구) 디지털상품권 잔액조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParam(name = "cardNo", value = "카드번호", required = true, dataType = "string", paramType = "body")
	@PostMapping(value = "/v1.0/giftcard/get-oldgiftcard-balance", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOldGiftcardBalance(Parameter<?> parameter) throws Exception {
		Map<String, Object> map = giftcardService.getOldGiftCardBalance(parameter.getString("cardNo"));
		
		return UtilsREST.responseOk(parameter, map);
	}
}
