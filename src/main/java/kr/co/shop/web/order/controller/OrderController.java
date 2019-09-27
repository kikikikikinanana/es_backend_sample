package kr.co.shop.web.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentAuthority;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.order.model.master.IfOffDealHistory;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderPayment;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.service.OrderService;
import kr.co.shop.web.order.vo.OrderAddressVo;
import kr.co.shop.web.order.vo.OrderCartVo;
import kr.co.shop.web.order.vo.OrderPaymentVo;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 주문 관련 기능 controller
 * @FileName : OrderController.java
 * @Project : shop.backend
 * @Date : 2019. 4. 8.
 * @Author : ljyoung@3top.co.kr
 */
@Slf4j
@Controller
@RequestMapping("api")
public class OrderController extends BaseController {

	@Autowired
	OrderService orderService;

	/*************************************************************************************************
	 * lks start
	 *************************************************************************************************/
	@ApiOperation(value = "배송주소록/최근배송지목록 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"), })
	@PostMapping(value = "/v1.0/order/getDlvyAddrList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDlvyAddrList(Parameter<MbMemberDeliveryAddress> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderService.getDlvyAddrList(parameter));
	}

	@ApiOperation(value = "클레임신청 대상 주문목록  조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rowsPerPage", value = "페이지당로우갯수", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "pageNum", value = "페이지번호", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "orderStatCode", value = "주문상태코드", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "orderStatCodeList", value = "주문상태코드 목록 배열", required = true, dataType = "String[]", paramType = "body") })
	@PostMapping(value = "/v1.0/order/getClaimRequestOrderList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getClaimRequestOrderList(Parameter<OcOrder> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderService.getClaimRequestOrderList(parameter));
	}

	@ApiOperation(value = "클레임 신청 주문 상세 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderNo", value = "주문번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderStatCode", value = "주문상태코드", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "orderStatCodeList", value = "주문상태코드 목록 배열", required = true, dataType = "String[]", paramType = "body") })
	@PostMapping(value = "/v1.0/order/getClaimRequestOrderDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getClaimRequestOrderDetail(Parameter<OcOrder> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderService.getClaimRequestOrderDetail(parameter.get()));
	}

	@ApiOperation(value = "마이페이지 - 공통영역 최근 주문내역 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/order/getCommonOrderStat", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCommonOrderStat(Parameter<OcOrder> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderService.getCommonOrderStat(parameter.get()));
	}

	/*************************************************************************************************
	 * lks end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * ljyoung start
	 *************************************************************************************************/
	@ApiOperation(value = "주문번호 채번", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/getOrderNo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOrderNo(Parameter<?> parameter) throws Exception {
		String orderNo = orderService.createOrderSeq();
		return UtilsREST.responseOk(parameter, orderNo);
	}

	@ApiOperation(value = "주문서 기본 정보", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/orderMain", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> orderInfoByCart(Parameter<OrderCartVo> parameter) throws Exception {
		Map<String, Object> map = orderService.orderInfoByCart(parameter);
		System.out.println(map);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "비회원 주문 동의 약관", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/guestTerms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> guestTermsList(Parameter<OrderCartVo> parameter) throws Exception {
		Map<String, Object> map = orderService.getGuestTermsList();
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "회원 주소록 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/memberAddressList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberAddressList(Parameter<OrderCartVo> parameter) throws Exception {
		Map<String, Object> map = orderService.getOrderAddressList(parameter);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "기본배송지 설정", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/setDefaultAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setDefaultAddress(Parameter<OrderAddressVo> parameter) throws Exception {
		Map<String, Object> map = orderService.setDefaultAddress(parameter);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "주문 승인", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> confirmOrder(Parameter<OrderPaymentVo> parameter) throws Exception {
		return UtilsREST.responseOk(parameter, orderService.processConfirm(parameter.get()));
	}

	@ApiOperation(value = "주문 정보 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/getOrderByOrderNo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOrderByOrderNo(Parameter<OcOrder> parameter) throws Exception {
		return UtilsREST.responseOk(parameter, orderService.getOrderByOrderNo(parameter.get()));
	}

	@ApiOperation(value = "카카오페이 인증", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/kakaoAuth", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> kakaoAuth(Parameter<KakaoPaymentAuthority> parameter) throws Exception {
		return UtilsREST.responseOk(parameter, orderService.kakaoAuth(parameter.get()));
	}

	/*************************************************************************************************
	 * ljyoung end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * jeon start
	 *************************************************************************************************/
	@ApiOperation(value = "주문 정보 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/getMyPageOrderInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyPageOrderInfo(Parameter<OcOrder> parameter) throws Exception {

		Pageable<OcOrder, OcOrder> pageable = new Pageable<>(parameter);
		pageable.setRowsPerPage(5);
		pageable.setPageNum(parameter.get().getPageNum());

		// 비회원 처리
		String memberTypeCode = pageable.getBean().getMemberTypeCode();
		if (UtilsText.equals(CommonCode.MEMBER_TYPE_CODE_NONMEMBER, memberTypeCode)) {
			pageable.getBean().setOrderNo(pageable.getBean().getNonOrderNo());
		}

		Map<String, Object> resultMap = orderService.getMyPageOrderInfo(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "주문 정보 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/getOrderDetailInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOrderDetailInfo(Parameter<OcOrder> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, orderService.getOrderDetailInfo(parameter.get()));
	}

	/*************************************************************************************************
	 * jeon end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * leesh start
	 *************************************************************************************************/
	@ApiOperation(value = "오프라인 주문 정보 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/getMyPageOrderOfflineInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyPageOrderOfflineInfo(Parameter<OcOrder> parameter) throws Exception {

		Pageable<OcOrder, OcOrder> pageable = new Pageable<>(parameter);
		pageable.setRowsPerPage(5);
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = orderService.getMyPageOrderOfflineInfo(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "오프라인 구매확정", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/setUpdateOfflineOrderBuyFix", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setUpdateOfflineOrderBuyFix(Parameter<IfOffDealHistory> parameter) throws Exception {
		IfOffDealHistory iIfOffDealHistory = parameter.get();
		Map<String, Object> resultMap = orderService.updateOfflineOrderBuyFix(iIfOffDealHistory);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "배송지 변경", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/order/setUpdateDlvrAddrModify", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setUpdateDlvrAddrModify(Parameter<OcOrder> parameter) throws Exception {
		OcOrder ocOrder = parameter.get();
		Map<String, Object> resultMap = orderService.updateDlvrAddrModify(ocOrder);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "결제수단변경", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderNo", value = "주문번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/order/updateOrderPaymentChange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateOrderPaymentChange(Parameter<OcOrderPayment> parameter) throws Exception {
		OcOrderPayment ocOrderPayment = parameter.get();

		Map<String, Object> resultMap = orderService.updateOrderPaymentChange(ocOrderPayment);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "주문 옵션 변경", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderNo", value = "주문번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderPrdtSeq", value = "주문상품시퀀스", required = true, dataType = "int", paramType = "body") })
	@PostMapping(value = "/v1.0/order/updateOrderProductOptionChange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateOrderProductOptionChange(Parameter<OcOrderProduct> parameter) throws Exception {
		OcOrderProduct ocOrderProduct = parameter.get();
		Map<String, Object> resultMap = orderService.updateOrdPrdOptionChange(ocOrderProduct);
		return UtilsREST.responseOk(parameter, resultMap);
	}

	/*************************************************************************************************
	 * leesh end
	 *************************************************************************************************/

	/**
	 * 
	 * @Desc : 배송완료 상품의 구매확정 처리
	 * @Method Name : orderConfirmUpdate
	 * @Date : 2019. 6. 12.
	 * @Author : NKB
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "배송완료 상품의 구매확정 처리", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "orderNo", value = "주문번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/order/updateOrderConfirm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateOrderConfirm(Parameter<OcOrder> parameter) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		OcOrder ocOrder = parameter.get();

		Map<String, Object> resultMap = orderService.updateOrderConfirm(ocOrder);

		return UtilsREST.responseOk(parameter, resultMap);
	}
}