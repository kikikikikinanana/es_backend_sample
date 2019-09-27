package kr.co.shop.web.order.service;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.util.Calendar;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.constant.BaseConst;
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsArray;
import kr.co.shop.common.util.UtilsCrypt;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.member.MembershipPointService;
import kr.co.shop.interfaces.module.member.model.BuyFixProduct;
import kr.co.shop.interfaces.module.member.model.PrivateReport;
import kr.co.shop.interfaces.module.payment.PaymentEntrance;
import kr.co.shop.interfaces.module.payment.base.PaymentConst;
import kr.co.shop.interfaces.module.payment.base.PaymentResult;
import kr.co.shop.interfaces.module.payment.base.model.PaymentInfo;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentApprovalReturn;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentAuthority;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentAuthorityReturn;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentCancel;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApprovalReturn;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancel;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancelReturn;
import kr.co.shop.interfaces.module.payment.naver.model.NaverPaymentApprovalReturn;
import kr.co.shop.interfaces.module.payment.naver.model.NaverPaymentApprovalReturn.NaverPaymentApprovalReturnBodyDetail;
import kr.co.shop.interfaces.module.payment.naver.model.NaverPaymentCancel;
import kr.co.shop.interfaces.module.payment.nice.NiceGiftService;
import kr.co.shop.interfaces.module.payment.nice.model.CollectionRequest;
import kr.co.shop.interfaces.module.payment.nice.model.CollectionResponse;
import kr.co.shop.interfaces.module.payment.nice.model.CommNiceRes;
import kr.co.shop.interfaces.zinterfacesdb.service.InterfacesOrderService;
import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.cart.model.master.OcCartBenefit;
import kr.co.shop.web.cart.repository.master.OcCartBenefitDao;
import kr.co.shop.web.cart.service.CartService;
import kr.co.shop.web.cmm.model.master.CmStore;
import kr.co.shop.web.cmm.service.StoreService;
import kr.co.shop.web.cmm.service.TermsService;
import kr.co.shop.web.giftcard.service.GiftcardService;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.member.model.master.MbMemberGiftCard;
import kr.co.shop.web.member.model.master.MbMemberInterestStore;
import kr.co.shop.web.member.repository.master.MbMemberCouponDao;
import kr.co.shop.web.member.service.MemberCouponService;
import kr.co.shop.web.member.service.MemberPointService;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.mypage.vo.MemberShipErrorCode;
import kr.co.shop.web.mypage.vo.OrderStatVO;
import kr.co.shop.web.order.model.master.IfOffDealHistory;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderConvenienceStore;
import kr.co.shop.web.order.model.master.OcOrderDeliveryHistory;
import kr.co.shop.web.order.model.master.OcOrderPayment;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.model.master.OcOrderProductHistory;
import kr.co.shop.web.order.model.master.OcOrderUseCoupon;
import kr.co.shop.web.order.model.master.OrderValidateProduct;
import kr.co.shop.web.order.repository.master.IfOffDealHistoryDao;
import kr.co.shop.web.order.repository.master.OcOrderConvenienceStoreDao;
import kr.co.shop.web.order.repository.master.OcOrderDao;
import kr.co.shop.web.order.repository.master.OcOrderDeliveryHistoryDao;
import kr.co.shop.web.order.repository.master.OcOrderPaymentDao;
import kr.co.shop.web.order.repository.master.OcOrderProductApplyPromotionDao;
import kr.co.shop.web.order.repository.master.OcOrderProductDao;
import kr.co.shop.web.order.repository.master.OcOrderProductHistoryDao;
import kr.co.shop.web.order.repository.master.OcOrderTermsAgreeDao;
import kr.co.shop.web.order.repository.master.OcOrderUseCouponDao;
import kr.co.shop.web.order.vo.OrderAddressVo;
import kr.co.shop.web.order.vo.OrderCartVo;
import kr.co.shop.web.order.vo.OrderFormVo;
import kr.co.shop.web.order.vo.OrderFormVo.DeleveryAddress;
import kr.co.shop.web.order.vo.OrderFormVo.OrderCartBenefit;
import kr.co.shop.web.order.vo.OrderFormVo.OrderFormMemberVo;
import kr.co.shop.web.order.vo.OrderFormVo.OrderFormProductVo;
import kr.co.shop.web.order.vo.OrderFormVo.OrderFormVendorVo;
import kr.co.shop.web.order.vo.OrderPaymentVo;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderCoupon;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderPayment;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderProduct;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderPromotion;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderTermsAgree;
import kr.co.shop.web.order.vo.OrderSaveVo;
import kr.co.shop.web.product.model.master.PdProductOptionStock;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockOnlyOne;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.service.ProductOptionService;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.repository.master.SySiteDao;
import kr.co.shop.web.system.service.CommonCodeService;
import kr.co.shop.web.system.service.SiteService;
import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 주문 관련 기능 service
 * @FileName : OrderService.java
 * @Project : shop.backend
 * @Date : 2019. 4. 8.
 * @Author : ljyoung@3top.co.kr
 */
@Slf4j
@Service
public class OrderService {

	@Autowired
	CommonCodeService commonCodeService;

	@Autowired
	SiteService siteService;

	@Autowired
	MemberService memberService;

	@Autowired
	TermsService termsService;

	@Autowired
	GiftcardService giftcardService;

	@Autowired
	MemberPointService memberPointService;

	@Autowired
	MemberCouponService memberCouponService;

	@Autowired
	ProductService productService;

	@Autowired
	VendorService vendorService;

	@Autowired
	CartService cartService;

	@Autowired
	StoreService storeService;

	@Autowired
	NiceGiftService niceGiftService;

	@Autowired
	PaymentEntrance paymentEntrance;

	@Autowired
	OcOrderDao ocOrderDao;

	@Autowired
	OcOrderProductDao ocOrderProductDao;

	@Autowired
	OcOrderTermsAgreeDao ocOrderTermsAgreeDao;

	@Autowired
	OcOrderPaymentDao ocOrderPaymentDao;

	@Autowired
	OcCartBenefitDao ocCartBenefitDao;

	@Autowired
	MbMemberCouponDao mbMemberCouponDao;

	@Autowired
	SySiteDao sySiteDao;

	@Autowired
	OcOrderDeliveryHistoryDao ocOrderDeliveryHistoryDao;

	@Autowired
	OcOrderProductHistoryDao ocOrderProductHistoryDao;

	@Autowired
	OcOrderConvenienceStoreDao ocOrderConvenienceStoreDao;

	@Autowired
	OcOrderProductApplyPromotionDao ocOrderProductApplyPromotionDao;

	@Autowired
	IfOffDealHistoryDao ifOffDealHistoryDao;

	@Autowired
	OcOrderUseCouponDao ocOrderUseCouponDao;

	@Autowired
	ProductOptionService productOptionService;

	@Autowired
	MembershipPointService membershipPointService;

	@Autowired
	InterfacesOrderService interfacesOrderService;

	/**
	 * @Desc : 주문번호 신규 채번 , yyyyMMdd + 5자리Seq(5자리가 안될경우 Seq 앞으로 ZeroFill)
	 * @Method Name : createOrderSeq
	 * @Date : 2019. 4. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @return
	 * @throws Exception
	 */
	public String createOrderSeq() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DEFAULT_DATE_PATTERN_NOT_CHARACTERS);
		Date toDay = Calendar.getInstance().getTime();
		String orderNo = dateFormat.format(toDay);
		orderNo = orderNo.concat(String.format("%05d", ocOrderDao.selectOrderNoNextVal()));
		return orderNo;
	}

	/**
	 * 
	 * @Desc : 재고 차감프로세스
	 * @Method Name : updateOrderProductStock
	 * @Date : 2019. 6. 4.
	 * @Author : NKB
	 * @param ocOrderProductList
	 * @param storeYn
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderProductStock(List<OcOrderProduct> ocOrderProductList, String storeYn) throws Exception {

		List<OrderValidateProduct> validateProducts = ocOrderProductList.stream()
				.map(o -> OrderValidateProduct.builder().prdtNoPrdtOptnNo(o.getPrdtNo() + o.getPrdtOptnNo())
						.prdtNo(o.getPrdtNo()).prdtOptnNo(o.getPrdtOptnNo()).sumStockQty(o.getOrderQty())
						.aiQty(o.getAiQty()).awQty(o.getAwQty()).asQty(o.getAsQty()).build())
				.collect(Collectors.toList());

		// 상품 별 주문 수량 Summery
		validateProducts = validateProducts.stream()
				// 단품 번호 ListAgg
				.peek(o1 -> o1.setPrdtOptnNo(ocOrderProductList.stream()
						.filter(o2 -> StringUtils.equals(o2.getPrdtNoPrdtOptnNo(), o1.getPrdtNoPrdtOptnNo()))
						.map(OcOrderProduct::getPrdtOptnNo).distinct().collect(Collectors.joining())))
				// 상품 수량 ( 단품 전체 수량 )
				.peek(o1 -> o1.setSumStockQty(ocOrderProductList.stream()
						.filter(o2 -> StringUtils.equals(o2.getPrdtNoPrdtOptnNo(), o1.getPrdtNoPrdtOptnNo()))
						.mapToInt(OcOrderProduct::getOrderQty).sum()))
				.distinct().collect(Collectors.toList());

		// 점포기준인경우
		if (StringUtils.equals("Y", storeYn)) {
			for (OrderValidateProduct param : validateProducts) {

				PdProductOptionStock prdtStock = new PdProductOptionStock();
				prdtStock.setOrderCount(param.getSumStockQty());
				prdtStock.setPrdtNo(param.getPrdtNo());
				prdtStock.setPrdtOptnNo(param.getPrdtOptnNo());
				prdtStock.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AW); // AW 10002

				ocOrderProductDao.updateProductStock(prdtStock);
			}
		} else {

			for (OrderValidateProduct param : validateProducts) {

				int sumQty = param.getSumStockQty(); // 주문수량

				int aiQty = param.getAiQty(); // AI 재고 수량
				int awQty = param.getAwQty(); // AW 재고 수량
				int asQty = param.getAsQty(); // AS 재고 수량

				// 재고가 충분
				if (aiQty - sumQty > 0) {
					// 재고를 차감 한다.
					PdProductOptionStock prdtStock = new PdProductOptionStock();
					prdtStock.setOrderCount(param.getSumStockQty());
					prdtStock.setPrdtNo(param.getPrdtNo());
					prdtStock.setPrdtOptnNo(param.getPrdtOptnNo());
					prdtStock.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // 온라인 물류 10000

					ocOrderProductDao.updateProductStock(prdtStock);
				} else {
					// 재고가 충분하지 않기에 AiQty만큼 차감 후 나머지를 aw에서 차감
					if (aiQty > 0) {
						int newAwQty = sumQty - aiQty;
						// 재고를 차감 한다.
						PdProductOptionStock prdtStock = new PdProductOptionStock();
						prdtStock.setOrderCount(aiQty);
						prdtStock.setPrdtNo(param.getPrdtNo());
						prdtStock.setPrdtOptnNo(param.getPrdtOptnNo());
						prdtStock.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // 온라인 물류 10000

						ocOrderProductDao.updateProductStock(prdtStock);

						// 남은 수량의 차이가 aw에 있으며 차감
						if (awQty - newAwQty > 0) {

							// 재고를 차감 한다.
							PdProductOptionStock prdtStockAw = new PdProductOptionStock();
							prdtStockAw.setOrderCount(newAwQty);
							prdtStockAw.setPrdtNo(param.getPrdtNo());
							prdtStockAw.setPrdtOptnNo(param.getPrdtOptnNo());
							prdtStockAw.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AW); // 스마트 물류 10001

							ocOrderProductDao.updateProductStock(prdtStockAw);

						} else {
							// aw에도 없으면 as에서 차감

							// 재고를 차감 한다.
							PdProductOptionStock prdtStockAs = new PdProductOptionStock();
							prdtStockAs.setOrderCount(newAwQty);
							prdtStockAs.setPrdtNo(param.getPrdtNo());
							prdtStockAs.setPrdtOptnNo(param.getPrdtOptnNo());
							prdtStockAs.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AS); // 오프라인 물류 10002

							ocOrderProductDao.updateProductStock(prdtStockAs);
						}
					} else {
						// 재고를 aw에서 차감 한다.
						PdProductOptionStock prdtStockAs = new PdProductOptionStock();
						prdtStockAs.setOrderCount(sumQty);
						prdtStockAs.setPrdtNo(param.getPrdtNo());
						prdtStockAs.setPrdtOptnNo(param.getPrdtOptnNo());
						prdtStockAs.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AS); // 오프라인 물류 10002

						ocOrderProductDao.updateProductStock(prdtStockAs);
					}
				}
			}
		}
		return true;
	}

	/**
	 * @Desc :상품재고 update (List)
	 * @Method Name : updateProductStock
	 * @Date : 2019. 5. 31.
	 * @Author : ljyoung@3top.co.kr
	 * @param prdtStockList java.util.List
	 * @throws Exception
	 */
	public void updateProductStock(List<PdProductOptionStock> prdtStockList) throws Exception {
		for (PdProductOptionStock prdtStock : prdtStockList) {
			updateProductStock(prdtStock);
		}
	}

	/**
	 * @Desc : 상품재고 update
	 * @Method Name : updateProductStock
	 * @Date : 2019. 5. 31.
	 * @Author : ljyoung@3top.co.kr
	 * @param prdtStock
	 * @throws Exception
	 */

	public void updateProductStock(PdProductOptionStock prdtStock) throws Exception {
		ocOrderProductDao.updateProductStock(prdtStock);
	}

	/*************************************************************************************************
	 * lks start
	 *************************************************************************************************/
	/**
	 * @Desc : 회원 배송주소록 조회
	 * @Method Name : getDlvyAddrList
	 * @Date : 2019. 4. 1.
	 * @Author : 이강수
	 * @param Parameter<MbMemberDeliveryAddress>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getDlvyAddrList(Parameter<MbMemberDeliveryAddress> parameter) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		Pageable<MbMemberDeliveryAddress, OcOrder> ocPageable = new Pageable<>(parameter);
		ocPageable.setRowsPerPage(parameter.get().getRowsPerPage());
		ocPageable.setPageNum(parameter.get().getPageNum());

		int totalCount = ocOrderDao.selectRecentDlvyAddrListCount(ocPageable);

		if (totalCount > 0) {
			resultMap.put("recentDlvyList", ocOrderDao.selectRecentDlvyAddrList(ocPageable));
		}

		Pageable<MbMemberDeliveryAddress, MbMemberDeliveryAddress> mbPageable = new Pageable<>(parameter);
		mbPageable.setRowsPerPage(parameter.get().getRowsPerPage());
		mbPageable.setPageNum(parameter.get().getPageNum());

		Page<MbMemberDeliveryAddress> page = memberService.selectMemberDeliveryAddress(mbPageable);

		if (!UtilsArray.isEmpty(page.getContent())) {
			resultMap.put("dlvyAddrList", page);
		}

		return resultMap;
	}

	/**
	 * @Desc : 클레임신청 대상 주문목록 조회
	 * @Method Name : selectClaimRequestOrderList
	 * @Date : 2019. 4. 16.
	 * @Author : 이강수
	 * @param Parameter<OcOrder>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClaimRequestOrderList(Parameter<OcOrder> parameter) throws Exception {

		Pageable<OcOrder, OcOrder> pageable = new Pageable<>(parameter);

		// 페이지 세팅
		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> map = new HashMap<>();

		int totalCount = ocOrderDao.selectClaimRequestOrderListCount(pageable);

		// MO 데이터 없을때
		if (totalCount < 1) {
			map.put("noData", true);
		}

		if (totalCount > 0) {

			List<OcOrder> orderList = ocOrderDao.selectClaimRequestOrderList(pageable);

			// 사은품, 배송비 제외
			String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };

			for (OcOrder ocOrder : orderList) {

				OcOrder param = new OcOrder();
				// 조건절을 위한 set
				// 주문번호
				param.setOrderNo(ocOrder.getOrderNo());
				param.setOrderPrdtStatCodeList(pageable.getBean().getOrderPrdtStatCodeList());
				// 상품관련파일순번 1 (대표)
				param.setPrdtRltnFileSeq(1);

				// 사은품, 배송비 제외
				param.setPrdtTypeCodeList(prdtTypeCodeList);

				// 주문상품상태코드 : 배송완료(수령) 완료일 경우
				if (pageable.getBean().getOrderStatCode() != null) {
					param.setOrderPrdtStatCode(pageable.getBean().getOrderStatCode());
				}

				List<OcOrderProduct> orderProductList = ocOrderProductDao.selectClaimRequestOrderProductList(param);

				int orderAmtSum = orderProductList.stream().mapToInt(o -> o.getOrderAmt()).sum();
				ocOrder.setStrPymntAmt(String.valueOf(orderAmtSum)); // 대상 상품의 결제금액을 sum 하여 대입

				// 각각 주문목록에 주문상품목록 담기
				if (!UtilsArray.isEmpty(orderProductList)) {

					ocOrder.setOcOrderProductList(orderProductList);
					ocOrder.setOcOrderProductListSize(orderProductList.size());
				}
			}

			map.put("content", orderList);

			// MO 스크롤
			if (orderList.size() < parameter.get().getRowsPerPage()) {
				map.put("endData", true);
			}
		}

		pageable.setTotalCount(totalCount);

		map.put("totalCount", totalCount);
		map.put("pageNum", pageable.getPage().getPageNum());
		map.put("totalPages", pageable.getPage().getTotalPages());
		map.put("rowsPerPage", pageable.getPage().getRowsPerPage());

		return map;
	}

	/**
	 * @Desc : 클레임 신청 주문 상세 조회
	 * @Method Name : getClaimRequestOrderDetail
	 * @Date : 2019. 4. 23.
	 * @Author : 이강수, KTH
	 * @param OcOrder
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClaimRequestOrderDetail(OcOrder ocOrder) throws Exception {

		Map<String, Object> map = new HashMap<>();

		// 로그인 사용자 정보
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(ocOrder.getLoginMemberNo());
		map.put("memberInfo", memberService.selectMemberInfo(mbMember));

		// 사은품, 배송비 제외
		String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };
		ocOrder.setPrdtTypeCodeList(prdtTypeCodeList);

		// 클레임 대상 주문 상세
		OcOrder orderInfo = ocOrderDao.selectByPrimaryKey(ocOrder);

		if (orderInfo != null) {
			map.put("orderInfo", orderInfo);
		}

		// 원 주문 상품 정보 목록
		OcOrderProduct orgOrderProduct = new OcOrderProduct();
		orgOrderProduct.setOrderNo(ocOrder.getOrderNo());
		List<OcOrderProduct> orgOrderProductList = ocOrderProductDao.selectOrgOrderProductList(orgOrderProduct);

		map.put("orgOrderProductList",
				orgOrderProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.collect(Collectors.toList()));

		// 사이트 정책 정보
		SySite sySite = sySiteDao.selectSite(orderInfo.getSiteNo());
		map.put("siteInfo", sySite);

		// 업체번호와 해당 업체별 주문배송비 조회
		ocOrder.setPrdtTypeCode(CommonCode.PRDT_TYPE_CODE_DELIVERY); // 배송비
		ocOrder.setClmPrdtStatCodes(new String[] { CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REDEPTION_ACCEPT,
				CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REFUND_ACCEPT, CommonCode.CLM_PRDT_STAT_CODE_CANCEL_FINISH,
				CommonCode.CLM_PRDT_STAT_CODE_RETURN_REDEPTION_ACCEPT,
				CommonCode.CLM_PRDT_STAT_CODE_RETURN_REFUND_ACCEPT, CommonCode.CLM_PRDT_STAT_CODE_RETURN_FINISH,
				CommonCode.CLM_PRDT_STAT_CODE_RETURN_IMPOSSIBLE }); // 취소, 반품 기준

		List<OcOrder> vndrOrderList = ocOrderProductDao.selectVndrNoGroupByOrderNo(ocOrder); // 주문상품 업체번호별 그룹목록(자사,입점업체)

		int mmnyDlvyAmt = 0; // 자사 주문배송비
		int mmnyCanceledDlvyAmt = 0; // 자사 기 취소된 주문배송비
		long mmnyVendorCnt = vndrOrderList.stream().filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE))
				.count(); // 자사

		if (mmnyVendorCnt > 0) { // 자사 상품이 있는 경우
			mmnyDlvyAmt = vndrOrderList.stream().filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE))
					.mapToInt(x -> x.getVndrDlvyAmt()).sum();
			mmnyCanceledDlvyAmt = vndrOrderList.stream()
					.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE))
					.mapToInt(x -> x.getCanceledDlvyAmt()).sum();
		}

		map.put("mmnyDlvyAmt", mmnyDlvyAmt);
		map.put("mmnyCanceledDlvyAmt", mmnyCanceledDlvyAmt);

		if (!UtilsArray.isEmpty(vndrOrderList)) {

			for (OcOrder vndrOrder : vndrOrderList) {

				// 조건절을 위한 set
				vndrOrder.setOrderNo(ocOrder.getOrderNo());
				vndrOrder.setOrderPrdtStatCodeList(ocOrder.getOrderPrdtStatCodeList());
				// 사은품, 배송비 제외
				vndrOrder.setPrdtTypeCodeList(prdtTypeCodeList);
				// 상품관련파일순번 1 (대표)
				vndrOrder.setPrdtRltnFileSeq(1);

				// 주문상품상태코드 : 배송완료(수령) 완료일 경우
				if (ocOrder.getOrderStatCode() != null) {
					vndrOrder.setOrderPrdtStatCode(ocOrder.getOrderStatCode());
				}

				List<OcOrderProduct> orderProductList = ocOrderProductDao.selectClaimRequestOrderProductList(vndrOrder);

				// TODO: ProductOptionService - getProductOptionValidator

				if (!UtilsArray.isEmpty(orderProductList)) {

					PdProductOptionWithStockOnlyOne prdtOptn = null;

					// 상품옵션 테이블 조회
					for (OcOrderProduct ocOrderProduct : orderProductList) {

						prdtOptn = productOptionService.getProductOptionListWithStockOnlyOne(ocOrderProduct.getPrdtNo(),
								ocOrderProduct.getPrdtOptnNo());

						if (prdtOptn != null) {
							ocOrderProduct.setPdProductOptionWithStockOnlyOne(prdtOptn);
						}
					}

					vndrOrder.setOcOrderProductList(orderProductList); // 해당 클레임 신청 페이지에 노출될 자사,업체별 상품 목록

					// 원 주문 자사,업체별 상품 목록
					vndrOrder.setVndrOrgOrderProductList(orgOrderProductList.stream()
							.filter(x -> UtilsText.equals(x.getVndrNo(), vndrOrder.getVndrNo()))
							.collect(Collectors.toList()));
				}
			}

			// map.put("vndrList", vndrOrderList);
			// 자사 주문 분리
			map.put("mmnyList", vndrOrderList.stream()
					.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).collect(Collectors.toList()));

			// 업체 주문 분리
			map.put("vndrList",
					vndrOrderList.stream().filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE))
							.collect(Collectors.toList()));
		}

		/*
		 * 교환/반품 인 경우 무료사용 쿠폰 정보를 가져옴
		 */
		if (UtilsText.equals(ocOrder.getClmGbnCode(), CommonCode.CLM_GBN_CODE_EXCHANGE)
				|| UtilsText.equals(ocOrder.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN)) {
			/*
			 * 회원 교환/반품 무료사용 가능 보유쿠폰
			 */
			List<MbMemberCoupon> memberCouponList = mbMemberCouponDao
					.selectAvailableCouponList(ocOrder.getLoginMemberNo());

			if (UtilsText.equals(ocOrder.getClmGbnCode(), CommonCode.CLM_GBN_CODE_EXCHANGE)) {
				map.put("usableCouponList",
						memberCouponList.stream()
								.filter(x -> UtilsText.equals(x.getCpnTypeCode(), CommonCode.CPN_TYPE_CODE_FREE_CHANGE))
								.collect(Collectors.toList()));
			} else if (UtilsText.equals(ocOrder.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN)) {
				map.put("usableCouponList",
						memberCouponList.stream()
								.filter(x -> UtilsText.equals(x.getCpnTypeCode(), CommonCode.CLM_GBN_CODE_RETURN))
								.collect(Collectors.toList()));
			}

		}

		// 클레임사유, 은행 코드 조회
		String[] codeFields = { CommonCode.CLM_RSN_CODE, CommonCode.BANK_CODE, };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);

		map.put(CommonCode.CLM_RSN_CODE, codeList.get(CommonCode.CLM_RSN_CODE));
		map.put(CommonCode.BANK_CODE, codeList.get(CommonCode.BANK_CODE));

		return map;
	}

	/**
	 * @Desc : 마이페이지 - 공통영역 최근 주문내역 조회
	 * @Method Name : getCommonOrderStat
	 * @Date : 2019. 5. 13.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return OrderStatVO
	 * @throws Exception
	 */
	public OrderStatVO getCommonOrderStat(OcOrder param) throws Exception {
		// 비회원 처리
		String memberTypeCode = param.getMemberTypeCode();
		if (UtilsText.equals(CommonCode.MEMBER_TYPE_CODE_NONMEMBER, memberTypeCode)) {
			param.setOrderNo(param.getNonOrderNo());
		}

		return ocOrderDao.selectCommonOrderStat(param);
	}

	/**
	 * @Desc : 1:1 상담내역 수정 페이지에서 주문상품 조회
	 * @Method Name : getOrderPrdtInMemberCnsl
	 * @Date : 2019. 5. 24.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return OcOrderProduct
	 * @throws Exception
	 */
	public OcOrderProduct getOrderPrdtInMemberCnsl(OcOrderProduct param) throws Exception {

		return ocOrderProductDao.selectOrderPrdtInMemberCnsl(param);
	}

	/*************************************************************************************************
	 * lks end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * ljyoung start
	 *************************************************************************************************/
	/**
	 * @Desc : 주문서 FORM 호출 시 데이터 조회
	 * @Method Name : orderInfoByCart
	 * @Date : 2019. 5. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> orderInfoByCart(Parameter<OrderCartVo> parameter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		OrderCartVo orderCartVo = parameter.get();
		OrderFormVo orderFormVo = getOrderForm(orderCartVo);

		// 장바구니 상품 정보
		map.put("order", orderFormVo);
		map.put("prdtCouponList", mapper.writeValueAsString(orderFormVo.getCouponList()));

		// 사용 코드정보
		String[] codeFields = { CommonCode.PROMO_TYPE_CODE };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);
		map.put("siteCodeList", codeList);

		// 주문 동의 약관
		map.put("memberOrderTerms", termsService.getMemberOrderTerms());

		// 픽업 배송 여부

		// 할인 정보 (프로모션,쿠폰)

		// 결제 수단 정보
		Map<String, Object> paymentMeansMap = getOrderPaymentMeans(orderCartVo);
		map.put("mainPaymentsMeans", paymentMeansMap.get("mainPaymentsMeans"));
		map.put("subPaymentMeans", paymentMeansMap.get("subPaymentMeans"));
		map.put("useDlvyType", siteService.getUseDeliveryType(orderCartVo.getSiteNo()));

		// 로그인 확인
		if (orderCartVo.isLogin()) {
			MbMemberInterestStore memberInterestParam = new MbMemberInterestStore();
			memberInterestParam.setMemberNo(orderCartVo.getMemberNo());
			memberInterestParam.setSiteNo(orderCartVo.getSiteNo());

			OrderFormMemberVo memberInfo = orderFormVo.getMemberInfo();

			map.put("memberInfo", mapper.writeValueAsString(memberInfo));

			// 사용자 관심 매장
			map.put("interestStore", memberService.getInterestStore(memberInterestParam));

			// 최근배송매장
			OcOrder ocOrder = new OcOrder();
			ocOrder.setMemberNo(orderCartVo.getMemberNo());
			ocOrder.setSiteNo(orderCartVo.getSiteNo());
			ocOrder.setDlvyTypeCode(CommonCode.DLVY_TYPE_CODE_STORE_PICKUP);
			ocOrder = ocOrderDao.selectRecentPickupStore(ocOrder);
			if (ocOrder != null && ocOrder.getStoreNo() != null) {
				CmStore recentStore = new CmStore();
				recentStore.setStoreNo(ocOrder.getStoreNo());
				recentStore.setSiteNo(ocOrder.getSiteNo());
				map.put("recentPickupStore", storeService.getStoreDetail(recentStore));
			}

			// 사용자 배송지 정보 (기본, 최근)
			parameter.get().setRowsPerPage(1);
			parameter.get().setPageNum(1);
			Map<String, Object> addressMap = getOrderAddressList(parameter);
			Page<OcOrder> recentDlvyList = (Page<OcOrder>) addressMap.get("recentDlvyList");
			Page<MbMemberDeliveryAddress> memberDlvyList = (Page<MbMemberDeliveryAddress>) addressMap
					.get("memberDlvyList");

			map.put("recentDlvyCount", recentDlvyList.getTotalCount());
			map.put("recentDlvyList", mapper.writeValueAsString(recentDlvyList.getContent()));
			map.put("memberDlvyCount", memberDlvyList.getTotalCount());
			map.put("memberDlvyList", mapper.writeValueAsString(memberDlvyList.getContent()));
			MbMemberGiftCard mbMemberGiftCard = new MbMemberGiftCard();
			mbMemberGiftCard.setMemberNo(memberInfo.getMemberNo());
			map.put("myGiftCardList", giftcardService.getGiftCardList(mbMemberGiftCard));
			// 사용자 보유 쿠폰 정보
			map.put("memberCoupon", mapper
					.writeValueAsString(memberCouponService.getAvailableCouponListForOrder(memberInfo.getMemberNo())));

			// 사용자 가용 보유 포인트
			PrivateReport privateReport = new PrivateReport(); // memberPointService.getMemberPointInfo(member.getMemberNo());
			if (UtilsText.equals(CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, memberInfo.getMemberTypeCode())
					&& UtilsText.equals(Const.BOOLEAN_FALSE, orderCartVo.getEmpYn())) {
				map.put("memberPoint", mapper.writeValueAsString(privateReport));
			}
			// 사용자 최종 결제수단

		}
		System.out.println("getOrderInfoByCart :::::: " + map.toString());
		return map;
	}

	/**
	 * @Desc : 장바구니 기준 업체, 상품 정보 조회
	 * @Method Name : getOrderForm
	 * @Date : 2019. 5. 23.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderCartVo
	 * @return
	 * @throws Exception
	 */
	public OrderFormVo getOrderForm(OrderCartVo orderCartVo) throws Exception {

		List<OcCart> cartList = cartService.selectCartList(orderCartVo);
		if (cartList.size() == 0) {
			throw new Exception();
		}
		ObjectMapper mapper = new ObjectMapper();
		// 상품 기본 정보 추가 변수 등록 20190613
		cartList = cartList.stream().map(cart -> {
			cart.setDeviceCode(orderCartVo.getDeviceCode());
			cart.setMemberTypeCode(orderCartVo.getMemberTypeCode());
			cart.setMbshpGradeCode(orderCartVo.getMbshpGradeCode());
			cart.setEmpYn(orderCartVo.getEmpYn());
			return cart;
		}).collect(Collectors.toList());
		// 상품 기본 정보 추가 변수 등록 20190613

		List<PdProductWithAll> productList = productService.getProductListWithAll(cartList);
		List<VdVendor> vendorList = getVendorInfoList(productList);

		/** 총 정상가 */
		Integer totalNormalAmt = new Integer(0);

		/** 총 판매가 */
		Integer totalSellAmt = new Integer(0);

		/** 총 할인금액 */
		Integer totalDscntAmt = new Integer(0);

		/** 총 배송비 */
		Integer totalDlvyAddAmt = new Integer(0);

		OrderFormVo orderFormVo = new OrderFormVo();
		orderFormVo.setCartDeliveryType(orderCartVo.getCartDeliveryType());
		orderFormVo.setOrderCartType("C");
		orderFormVo.setDeviceCode(orderCartVo.getDeviceCode());

		// 회원정보 설정
		if (orderCartVo.isLogin()) {
			MbMember memberInfo = new MbMember();
			memberInfo.setMemberNo(orderCartVo.getMemberNo());
			memberInfo.setLoginId(orderCartVo.getLoginId());

			MbMember mbMember = memberService.getMemberByLoginId(memberInfo);

			OrderFormMemberVo member = new OrderFormMemberVo();

			member.setMemberNo(mbMember.getMemberNo());
			member.setMemberName(mbMember.getMemberName());
			member.setHdphnNoText(mbMember.getHdphnNoText());
			member.setEmailAddrText(mbMember.getEmailAddrText());
			member.setMemberTypeCode(mbMember.getMemberTypeCode());
			member.setMbshpGradeCode(mbMember.getMbshpGradeCode());
			member.setEmpYn(mbMember.getEmpYn());
			if (UtilsText.equals(mbMember.getEmpYn(), Const.BOOLEAN_TRUE))
				orderFormVo.setCsvPickupDlvy(false);

//			PrivateReport privateReport = membershipPointService.getPrivateReportBySafeKey(mbMember.getSafeKey());
//			member.setTotalPoint(privateReport.getTotalPoint());
//			member.setAccessPoint(privateReport.getAccessPoint());
//			member.setEventPoint(privateReport.getEventPoint());

			member.setTotalPoint(500000);
			member.setAccessPoint(200000);
			member.setEventPoint(300000);

			member.setPymntMeansCode(mbMember.getPymntMeansCode());
			if (UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP))
				orderFormVo.setMemberShip(true);
			orderFormVo.setMemberInfo(member);
		} else {
			OrderFormMemberVo member = new OrderFormMemberVo();
			member.setMemberNo(Const.NON_MEMBER_NO);
			member.setEmpYn(Const.BOOLEAN_FALSE);
			orderFormVo.setMemberInfo(member);
		}

		List<OrderFormVendorVo> vendorVoList = new ArrayList<OrderFormVendorVo>();
		// 자사 데이터
		OrderFormVendorVo abcVendorVo = new OrderFormVendorVo();

		VdVendor abcVendor = vendorList.stream().filter(vendor -> UtilsText.equals(vendor.getVndrGbnType(), "C"))
				.findFirst().orElse(null);

		List<OrderFormVo.OrderCoupon> prdtCouponList = new ArrayList<OrderFormVo.OrderCoupon>();

		if (abcVendor != null) {
			abcVendorVo.setVndrNo(abcVendor.getVndrNo());
			abcVendorVo.setVndrGbnType(abcVendor.getVndrGbnType());
			abcVendorVo.setVndrName(abcVendor.getVndrName());
			abcVendorVo.setDlvyAmt(abcVendor.getDlvyAmt());
			abcVendorVo.setFreeDlvyStdrAmt(abcVendor.getFreeDlvyStdrAmt());
			vendorVoList.add(abcVendorVo);

			Integer abcVendorProductAmt = new Integer(0);
			List<OrderFormProductVo> abcProductVoList = new ArrayList<OrderFormProductVo>();

			for (PdProductWithAll product : productList) {
				// 예약주문여부 확인.
				if (UtilsText.equals(product.getRsvPrdtYn(), Const.BOOLEAN_TRUE))
					orderFormVo.setRsvOrderYn(Const.BOOLEAN_TRUE);

				if (UtilsText.equals(product.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)) {
					if (UtilsText.equals(product.getStorePickupPsbltYn(), Const.BOOLEAN_FALSE))
						orderFormVo.setStorePickupDlvy(false);

					OrderFormProductVo productVo = new OrderFormProductVo();
					productVo.setOrderQty(cartList.stream().filter(x -> x != null)
							.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getOrderQty)
							.findFirst().orElse(0));

					productVo.setCtgrNo(cartList.stream().filter(x -> x.getCtgrNo() != null)
							.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getCtgrNo)
							.findFirst().orElse(null));

					productVo.setPlndpNo(cartList.stream().filter(x -> x.getPlndpNo() != null)
							.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getPlndpNo)
							.findFirst().orElse(null));

					productVo.setEventNo(cartList.stream().filter(x -> x.getEventNo() != null)
							.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getEventNo)
							.findFirst().orElse(null));

					abcVendorVo.setTotalProductCount(abcVendorVo.getTotalProductCount() + productVo.getOrderQty());

					/**
					 * 쿠폰 정보조회
					 */
					int cpnTotDcAmt = 0;
					OcCartBenefit benefitCpnInfo = new OcCartBenefit();
					OcCartBenefit benefit = new OcCartBenefit();
					benefit.setCartSeq(product.getCartSeq());
					benefit.setDscntAmt(product.getDscntAmt());

					benefitCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(benefit);

					if (benefitCpnInfo != null) {
						OrderFormVo.OrderCoupon orderCoupon = new OrderFormVo.OrderCoupon();
						orderCoupon.setCartSeq(product.getCartSeq());
						List<OrderCartBenefit> couponList = new ArrayList<OrderCartBenefit>();
						// 쿠폰 할인 정보 set
						if (!UtilsText.equals(benefitCpnInfo.getNomalCpnNo(), "0")) {
							productVo.setNomalCpnNo(benefitCpnInfo.getNomalCpnNo());
							productVo.setNomalHoldCpnSeq(benefitCpnInfo.getNomalHoldCpnSeq());
							productVo.setNomalCpnDcAmt(benefitCpnInfo.getNomalCpnApplyDcAmt());
							cpnTotDcAmt = cpnTotDcAmt + benefitCpnInfo.getNomalCpnApplyDcAmt();

							OrderCartBenefit coupon = new OrderCartBenefit();
							coupon.setCpnNo(benefitCpnInfo.getNomalCpnNo());
							coupon.setHoldCpnSeq(Integer.parseInt(benefitCpnInfo.getNomalHoldCpnSeq()));
							coupon.setCpnApplyDcAmt(benefitCpnInfo.getNomalCpnApplyDcAmt());
							coupon.setCpnApplyQty(benefitCpnInfo.getNomalCpnApplyQty());
							couponList.add(coupon);
						}

						if (!UtilsText.equals(benefitCpnInfo.getPlusCpnNo(), "0")) {
							productVo.setPlusCpnNo(benefitCpnInfo.getPlusCpnNo());
							productVo.setPlusHoldCpnSeq(benefitCpnInfo.getPlusHoldCpnSeq());
							productVo.setPlusCpnDcAmt(benefitCpnInfo.getPlusCpnApplyDcAmt());
							cpnTotDcAmt = cpnTotDcAmt + benefitCpnInfo.getPlusCpnApplyDcAmt();

							OrderCartBenefit coupon = new OrderCartBenefit();
							coupon.setCpnNo(benefitCpnInfo.getPlusCpnNo());
							coupon.setHoldCpnSeq(Integer.parseInt(benefitCpnInfo.getPlusHoldCpnSeq()));
							coupon.setCpnApplyDcAmt(benefitCpnInfo.getPlusCpnApplyDcAmt());
							coupon.setCpnApplyQty(benefitCpnInfo.getPlusCpnApplyQty());
							couponList.add(coupon);
						}
						// 포인트 계산 금액 및 쿠폰 할인 적용된 판매가 set
						productVo.setSavePoint(String.valueOf(benefitCpnInfo.getCpnApplyTotPoint()));
						orderFormVo.setTotalSavePoint(
								orderFormVo.getTotalSavePoint() + benefitCpnInfo.getCpnApplyTotPoint());
						product.setTotalSellAmt(benefitCpnInfo.getCpnApplyTotSellAmt()); // 총 판매가(상품기준 totalSellAmt)
						orderCoupon.setCouponList(couponList);
						prdtCouponList.add(orderCoupon);
					} else {

						// 적립 포인트 계산
						double savePoint = getSavePoint(product.getDscntAmt()) * product.getOrderQty();
						productVo.setSavePoint(String.valueOf(savePoint));
						orderFormVo.setTotalSavePoint(orderFormVo.getTotalSavePoint() + savePoint);
					}

					productVo.setCpnDscntAmt(cpnTotDcAmt); // 상품 쿠폰 할인 금액
					orderFormVo.setCouponDscntAmt(orderFormVo.getCouponDscntAmt() + cpnTotDcAmt); // 전체 상품 쿠폰 할인 금액

					productVo.setProduct(product);
//					abcProductVoList.add(productVo);
					if (product.getFreeDlvyYn().equals(Const.BOOLEAN_TRUE))
						abcVendorVo.setFreeDlvy(true);

					// 프로모션 할인가
					int manyShoesDcAmt = 0; // 다족구매 할인 금액 ( 쿠폰 적용 위함)
					int promoDcAmt = 0; // 프로모션 할인 금액

					if (product.getPromotion().length > 0) {
						for (PrPromotion promo : product.getPromotion()) {
							if (promo.getDcAmt() != null) {
								if (product.getPrdtDscntAmt() > promo.getDcAmt()) {
									// 상품 할인가
									orderFormVo.setProductDscntAmt(
											orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
								}
								orderFormVo.setPromotionDscntAmt(orderFormVo.getPromotionDscntAmt()
										+ promo.getDcAmt() * productVo.getOrderQty());
								if (UtilsText.equals(promo.getPromoTypeCode(), "10001")) { // 다족구매
									manyShoesDcAmt = promo.getDcAmt();
								}
								promoDcAmt = promoDcAmt + (promo.getDcAmt() * productVo.getOrderQty());
							}
						}
					} else {
						// 상품 할인가
						orderFormVo.setProductDscntAmt(orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
					}

					productVo.setManyShoesDcAmt(manyShoesDcAmt);// 다족구매
					productVo.setPromoDscntAmt(promoDcAmt);// 프로모션 합계 금액
					productVo.setTotalDscntAmt(promoDcAmt + cpnTotDcAmt);// (프로모션 할인금액 + 쿠폰 할인금액)
					abcProductVoList.add(productVo); // 이동처리
					abcVendorProductAmt = abcVendorProductAmt + product.getTotalSellAmt();

					totalNormalAmt += product.getNormalAmt() * productVo.getOrderQty();

					if (product.getDscntAmt() != null)
						totalDscntAmt += product.getDscntAmt() * productVo.getOrderQty();
					totalSellAmt += (product.getSellAmt() + product.getOptnAddAmt()) * productVo.getOrderQty();
				}
			}

			if (!abcVendorVo.isFreeDlvy()) {
				if (abcVendorProductAmt > abcVendor.getFreeDlvyStdrAmt()) {
					abcVendorVo.setFreeDlvy(true);
				} else {
					abcVendorVo.setFreeDlvy(false);
					totalDlvyAddAmt += abcVendor.getDlvyAmt();
				}
			}
			abcVendorVo.setProductCount(abcProductVoList.size());
			abcVendorVo.setProductList(abcProductVoList);
		}

		// 입점사 데이터
		for (VdVendor vendor : vendorList) {
			if (UtilsText.equals(vendor.getVndrGbnType(), "V")) {
				orderFormVo.setStorePickupDlvy(false);// 입점사 상품 매장 픽업 불가.
				orderFormVo.setCsvPickupDlvy(false);// 임점사 상품 편의점 픽업 불가.
				OrderFormVendorVo vendorVo = new OrderFormVendorVo();
				vendorVo.setVndrNo(vendor.getVndrNo());
				vendorVo.setVndrGbnType(vendor.getVndrGbnType());
				vendorVo.setVndrName(vendor.getVndrName());
				vendorVo.setDlvyAmt(vendor.getDlvyAmt());
				vendorVo.setFreeDlvyStdrAmt(vendor.getFreeDlvyStdrAmt());
				vendorVoList.add(vendorVo);
				Integer vendorProductAmt = new Integer(0);
				List<OrderFormProductVo> productVoList = new ArrayList<OrderFormProductVo>();
				for (PdProductWithAll product : productList) {
					if (UtilsText.equals(product.getRsvPrdtYn(), Const.BOOLEAN_TRUE))
						orderFormVo.setRsvOrderYn(Const.BOOLEAN_TRUE);

					if (product.getVndrNo().equals(vendor.getVndrNo())) {
						OrderFormProductVo productVo = new OrderFormProductVo();
						productVo.setOrderQty(cartList.stream().filter(x -> x != null)
								.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getOrderQty)
								.findFirst().orElse(0));

						productVo.setCtgrNo(cartList.stream().filter(x -> x.getCtgrNo() != null)
								.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getCtgrNo)
								.findFirst().orElse(null));

						productVo.setPlndpNo(cartList.stream().filter(x -> x.getPlndpNo() != null)
								.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getPlndpNo)
								.findFirst().orElse(null));

						productVo.setEventNo(cartList.stream().filter(x -> x.getEventNo() != null)
								.filter(cart -> product.getCartSeq().equals(cart.getCartSeq())).map(OcCart::getEventNo)
								.findFirst().orElse(null));
						vendorVo.setTotalProductCount(vendorVo.getTotalProductCount() + productVo.getOrderQty());

						/**
						 * 쿠폰 정보조회
						 */
						int cpnTotDcAmt = 0;
						OcCartBenefit benefitCpnInfo = new OcCartBenefit();
						OcCartBenefit benefit = new OcCartBenefit();
						benefit.setCartSeq(product.getCartSeq());
						benefit.setDscntAmt(product.getDscntAmt());

						benefitCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(benefit);

						if (benefitCpnInfo != null) {
							OrderFormVo.OrderCoupon orderCoupon = new OrderFormVo.OrderCoupon();
							orderCoupon.setCartSeq(product.getCartSeq());
							List<OrderCartBenefit> couponList = new ArrayList<OrderCartBenefit>();

							// 쿠폰 할인 정보 set
							if (!UtilsText.equals(benefitCpnInfo.getNomalCpnNo(), "0")) {
								productVo.setNomalCpnNo(benefitCpnInfo.getNomalCpnNo());
								productVo.setNomalHoldCpnSeq(benefitCpnInfo.getNomalHoldCpnSeq());
								productVo.setNomalCpnDcAmt(benefitCpnInfo.getNomalCpnApplyDcAmt());
								cpnTotDcAmt = cpnTotDcAmt + benefitCpnInfo.getNomalCpnApplyDcAmt();

								OrderCartBenefit coupon = new OrderCartBenefit();
								coupon.setCpnNo(benefitCpnInfo.getNomalCpnNo());
								coupon.setHoldCpnSeq(Integer.parseInt(benefitCpnInfo.getNomalHoldCpnSeq()));
								coupon.setCpnApplyDcAmt(benefitCpnInfo.getNomalCpnApplyDcAmt());
								coupon.setCpnApplyQty(benefitCpnInfo.getNomalCpnApplyQty());
								couponList.add(coupon);
							}

							if (!UtilsText.equals(benefitCpnInfo.getPlusCpnNo(), "0")) {
								productVo.setPlusCpnNo(benefitCpnInfo.getPlusCpnNo());
								productVo.setPlusHoldCpnSeq(benefitCpnInfo.getPlusHoldCpnSeq());
								productVo.setPlusCpnDcAmt(benefitCpnInfo.getPlusCpnApplyDcAmt());
								cpnTotDcAmt = cpnTotDcAmt + benefitCpnInfo.getPlusCpnApplyDcAmt();

								OrderCartBenefit coupon = new OrderCartBenefit();
								coupon.setCpnNo(benefitCpnInfo.getPlusCpnNo());
								coupon.setHoldCpnSeq(Integer.parseInt(benefitCpnInfo.getPlusHoldCpnSeq()));
								coupon.setCpnApplyDcAmt(benefitCpnInfo.getPlusCpnApplyDcAmt());
								coupon.setCpnApplyQty(benefitCpnInfo.getPlusCpnApplyQty());
								couponList.add(coupon);
							}
							// 포인트 계산 금액 및 쿠폰 할인 적용된 판매가 set
							productVo.setSavePoint(String.valueOf(benefitCpnInfo.getCpnApplyTotPoint()));
							orderFormVo.setTotalSavePoint(
									orderFormVo.getTotalSavePoint() + benefitCpnInfo.getCpnApplyTotPoint());
							product.setTotalSellAmt(benefitCpnInfo.getCpnApplyTotSellAmt()); // 총 판매가(상품기준 totalSellAmt)
							orderCoupon.setCouponList(couponList);
							prdtCouponList.add(orderCoupon);
						} else {

							// 적립 포인트 계산
							double savePoint = getSavePoint(product.getDscntAmt()) * product.getOrderQty();
							productVo.setSavePoint(String.valueOf(savePoint));
							orderFormVo.setTotalSavePoint(orderFormVo.getTotalSavePoint() + savePoint);
						}

						productVo.setCpnDscntAmt(cpnTotDcAmt); // 상품 포인트 할인 금액
						orderFormVo.setCouponDscntAmt(orderFormVo.getCouponDscntAmt() + cpnTotDcAmt); // 전체 상품 쿠폰 할인 금액

						// 적립 포인트 계산
//						double savePoint = getSavePoint(product.getDscntAmt()) * product.getOrderQty();
//						orderFormVo.setTotalSavePoint(orderFormVo.getTotalSavePoint() + savePoint);
//						productVo.setSavePoint(String.valueOf(savePoint));
						productVo.setProduct(product);
//						productVoList.add(productVo);
						if (product.getFreeDlvyYn().equals(Const.BOOLEAN_TRUE))
							vendorVo.setFreeDlvy(true);

						vendorProductAmt = vendorProductAmt + product.getTotalSellAmt();

						// 프로모션 할인가
						int manyShoesDcAmt = 0;
						int promoDcAmt = 0; // 프로모션 할인 금액

						if (product.getPromotion().length > 0) {
							for (PrPromotion promo : product.getPromotion()) {
								if (promo.getDcAmt() != null) {
									if (product.getPrdtDscntAmt() > promo.getDcAmt()) {
										// 상품 할인가
										orderFormVo.setProductDscntAmt(
												orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
									}
									orderFormVo.setPromotionDscntAmt(orderFormVo.getPromotionDscntAmt()
											+ promo.getDcAmt() * productVo.getOrderQty());
									if (UtilsText.equals(promo.getPromoTypeCode(), "10001")) { // 다족구매
										manyShoesDcAmt = promo.getDcAmt();
									}
									promoDcAmt = promoDcAmt + (promo.getDcAmt() * productVo.getOrderQty());
								}
							}
						} else {
							// 상품 할인가
							orderFormVo
									.setProductDscntAmt(orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
						}

						productVo.setManyShoesDcAmt(manyShoesDcAmt);// 다족구매
						productVo.setPromoDscntAmt(promoDcAmt);// 프로모션 합계 금액
						productVo.setTotalDscntAmt(promoDcAmt + cpnTotDcAmt);// (프로모션 할인금액 + 쿠폰 할인금액)

						productVoList.add(productVo);// 이동처리

						totalNormalAmt += product.getNormalAmt() * productVo.getOrderQty();

						if (product.getDscntAmt() != null)
							totalDscntAmt += product.getDscntAmt() * productVo.getOrderQty();
						totalSellAmt += product.getSellAmt() * productVo.getOrderQty();
					}
				}
				if (!vendorVo.isFreeDlvy()) {
					if (vendorProductAmt > vendor.getFreeDlvyStdrAmt()) {
						vendorVo.setFreeDlvy(true);
					} else {
						vendorVo.setFreeDlvy(false);
						totalDlvyAddAmt += vendor.getDlvyAmt();
					}
				}
				vendorVo.setProductCount(productVoList.size());
				vendorVo.setProductList(productVoList);
			}
		}
		orderFormVo.setVendorList(vendorVoList);
		orderFormVo.setCouponList(prdtCouponList);
		orderFormVo.setTotalNormalAmt(totalNormalAmt);
		orderFormVo.setTotalSellAmt(totalSellAmt);
		orderFormVo.setTotalDscntAmt(totalDscntAmt);
		orderFormVo.setTotalDlvyAmt(totalDlvyAddAmt);
		System.out.println("orderFormVo :::::::::::::: " + mapper.writeValueAsString(orderFormVo));
		return orderFormVo;

	}

	/**
	 * @Desc : 장바구니 상품 목록 조회
	 * @Method Name : getCartProductList
	 * @Date : 2019. 5. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderCartVo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWithAll> getCartProductList(OrderCartVo orderCartVo) throws Exception {
		List<OcCart> cartList = cartService.selectCartList(orderCartVo);
		if (cartList.size() == 0) {
			throw new Exception();
		}

		List<PdProductWithAll> productList = productService.getProductListWithAll(cartList);

		return productList;
	}

	/**
	 * @Desc : 상품 목록 기준 판매처 조회
	 * @Method Name : getVendorInfoList
	 * @Date : 2019. 5. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @param prdtList
	 * @return
	 * @throws Exception
	 */
	public List<VdVendor> getVendorInfoList(List<PdProductWithAll> prdtList) throws Exception {
		// 조회할 업체 번호 조회
		List<String> vndrNoList = prdtList.stream().filter(x -> x != null).map(PdProductWithAll::getVndrNo).distinct()
				.collect(Collectors.toList());

		List<VdVendor> vendorInfoList = vendorService.getVendorInfoList(vndrNoList);
		return vendorInfoList;

	}

	/**
	 * @Desc : 장바구니 혜택 조회
	 * @Method Name : getCartBenefit
	 * @Date : 2019. 5. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @param cartSeq
	 * @return
	 */
	public List<OcCartBenefit> getCartBenefit(Long cartSeq) {
		try {
			OcCartBenefit ocCartBenefit = new OcCartBenefit();
			ocCartBenefit.setCartSeq(cartSeq);
			return ocCartBenefitDao.select(ocCartBenefit);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @Desc : 주문 정보 조회
	 * @Method Name : getOrderByOrderNo
	 * @Date : 2019. 5. 16.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrderByOrderNo(OcOrder ocOrder) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		OcOrder order = ocOrderDao.selectOrderDetail(ocOrder);
		Integer pointAmt = order.getPymntAmt() - order.getMmnyDlvyAmt() - order.getTotalVndrDlvyAmt();
		order.setTotalSavePoint(getSavePoint(pointAmt));
		result.put("order", order);

		OcOrderPayment ocOrderPayment = new OcOrderPayment();
		ocOrderPayment.setOrderNo(ocOrder.getOrderNo());
		result.put("orderPayment", ocOrderPaymentDao.selectDetail(ocOrderPayment));

//		OcOrderDiscountVO orderPromo = new OcOrderDiscountVO();
//		orderPromo.setOrderNo(ocOrder.getOrderNo());
//		result.put("orderPromotionList", ocOrderProductApplyPromotionDao.selectOrderPromoList(orderPromo));

		OcOrderUseCoupon useCoupon = new OcOrderUseCoupon();
		useCoupon.setOrderNo(ocOrder.getOrderNo()); // 원주문번호 기준
		result.put("orderCouponList", ocOrderUseCouponDao.couponListByOrder(useCoupon));

		OrderCartVo orderCartVo = new OrderCartVo();
		orderCartVo.setSiteNo(order.getSiteNo());
		Map<String, Object> paymentMeansMap = getOrderPaymentMeans(orderCartVo);
		result.put("mainPaymentsMeans", paymentMeansMap.get("mainPaymentsMeans"));
		result.put("subPaymentMeans", paymentMeansMap.get("subPaymentMeans"));

		return result;
	}

	/**
	 * @Desc : 기본 배송지, 최근배송지 조회
	 * @Method Name : getOrderAddressList
	 * @Date : 2019. 4. 24.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrderAddressList(Parameter<OrderCartVo> parameter) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		OrderCartVo orderCart = parameter.get();

		Pageable<DeleveryAddress, DeleveryAddress> orderPageable = new Pageable<>(parameter, DeleveryAddress.class);
		orderPageable.getBean().setMemberNo(orderCart.getMemberNo());
		orderPageable.setRowsPerPage(orderCart.getRowsPerPage());
		orderPageable.setPageNum(orderCart.getPageNum());
		resultMap.put("recentDlvyList", getOrderRecentAddrList(orderPageable));

		Pageable<MbMemberDeliveryAddress, MbMemberDeliveryAddress> mbPageable = new Pageable<>(parameter,
				MbMemberDeliveryAddress.class);
		mbPageable.getBean().setMemberNo(orderCart.getMemberNo());
		mbPageable.setRowsPerPage(orderCart.getRowsPerPage());
		mbPageable.setPageNum(orderCart.getPageNum());
		resultMap.put("memberDlvyList", memberService.selectMemberDeliveryAddress(mbPageable));

		return resultMap;
	}

	/**
	 * @Desc : 최근배송지 조회
	 * @Method Name : getOrderRecentAddrList
	 * @Date : 2019. 4. 26.
	 * @Author : ljyoung@3top.co.kr
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Page<DeleveryAddress> getOrderRecentAddrList(Pageable<DeleveryAddress, DeleveryAddress> pageable)
			throws Exception {
		int count = ocOrderDao.selectRecentDlvyAddreesCount(pageable);

		pageable.setTotalCount(count);
		if (count > 0)
			pageable.setContent(ocOrderDao.selectRecentDlvyAddrees(pageable));

		return pageable.getPage();
	}

	/**
	 * @Desc : 결제수단 정보 조회
	 * @Method Name : getOrderPaymentMeans
	 * @Date : 2019. 4. 26.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderCartVo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrderPaymentMeans(OrderCartVo orderCartVo) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String siteNo = orderCartVo.getSiteNo();
		if (orderCartVo.isLogin()) {
			if (UtilsText.equals(orderCartVo.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)) {
				resultMap.put("mainPaymentsMeans", siteService.getUseMemberNormalMainPaymentMeans(siteNo));
				resultMap.put("subPaymentMeans", siteService.getUseMemberNormalSubPaymentMeans(siteNo));
			}
			if (UtilsText.equals(orderCartVo.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_ONLINE)) {
				resultMap.put("mainPaymentsMeans", siteService.getUseOnlineMainPaymentMeans(siteNo));
				resultMap.put("subPaymentMeans", siteService.getUseOnlineSubPaymentMeans(siteNo));
			}
		} else {
			resultMap.put("mainPaymentsMeans", siteService.getUseGuestMainPaymentMeans(siteNo));
			resultMap.put("subPaymentMeans", siteService.getUseGuestSubPaymentMeans(siteNo));
		}

		return resultMap;
	}

	/**
	 * @Desc : 비회원 약관 목록 조회
	 * @Method Name : getGuestTermsList
	 * @Date : 2019. 4. 26.
	 * @Author : ljyoung@3top.co.kr
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGuestTermsList() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("termsList", termsService.getNonMemberOrderTerms());
		return resultMap;
	}

	/**
	 * @Desc : 기본 배송지 설정
	 * @Method Name : setDefaultAddress
	 * @Date : 2019. 5. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setDefaultAddress(Parameter<OrderAddressVo> parameter) throws Exception {
		OrderAddressVo address = parameter.get();
		MbMemberDeliveryAddress memberDelivery = new MbMemberDeliveryAddress();
		memberDelivery.setMemberNo(address.getMemberNo());
		memberDelivery.setDlvyAddrName("기본배송지");
		memberDelivery.setRcvrName(address.getRcvrName());
		memberDelivery.setHdphnNoText(address.getHdphnNoText());
		memberDelivery.setPostCodeText(address.getPostCodeText());
		memberDelivery.setPostAddrText(address.getPostAddrText());
		memberDelivery.setDtlAddrText(address.getDtlAddrText());
		List<MbMemberDeliveryAddress> returnList = memberService.setMemberDeliveryAddress(memberDelivery);

		OrderAddressVo returnAddr = new OrderAddressVo();

		for (MbMemberDeliveryAddress mbMemberDeliveryAddress : returnList) {
			if (mbMemberDeliveryAddress.getDfltDlvyAddrYn().equals("Y")) {
				returnAddr.setDfltDlvyAddrYn(mbMemberDeliveryAddress.getDfltDlvyAddrYn());
				returnAddr.setRcvrName(mbMemberDeliveryAddress.getRcvrName());
				returnAddr.setHdphnNoText(mbMemberDeliveryAddress.getHdphnNoText());
				returnAddr.setPostCodeText(mbMemberDeliveryAddress.getPostCodeText());
				returnAddr.setPostAddrText(mbMemberDeliveryAddress.getPostAddrText());
				returnAddr.setDtlAddrText(mbMemberDeliveryAddress.getDtlAddrText());
			}
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("returnAddr", returnAddr);
		return returnMap;
	}

	/**
	 * @Desc : 주문 승인 프로세스 진행
	 * @Method Name : processConfirm
	 * @Date : 2019. 5. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 */
	public Map<String, Object> processConfirm(OrderPaymentVo parameter) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderNo", parameter.getOrderNo());
//		resultMap.put("result", Const.RESULT_FAIL);
		resultMap.put("result", Const.RESULT_SUCCESS);

		OrderSaveVo orderSaveVo = makeOrder(parameter);

		// TODO : 주문 정보 validation
		validOrder(orderSaveVo);

		// TODO : 주문 정보 저장
		resultMap = saveOrder(orderSaveVo, resultMap);

		// TODO : PG 승인 요청
		resultMap = paymentConfirm(orderSaveVo, resultMap);

		// 결제 인증 후 문제 발생시 승인된 결제 취소.
		if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_FAIL)) {
			paymentCancel(orderSaveVo, resultMap);
		} else {
			// TODO : 승인 후 재고처리
			// resultMap = updateProductStock(orderSaveVo, resultMap);

			// 재고 처리 후 문제 발생시 승인된 결제 취소.
			if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_FAIL)) {
				paymentCancel(orderSaveVo, resultMap);
			} else { // TODO : 승인 후 주문 완료 처리
				completeOrderStatus(orderSaveVo);
			}
		}

		// TODO : RETURN 결과
		return resultMap;
	}

	private OrderSaveVo makeOrder(OrderPaymentVo parameter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// 데이터 저장개체 선언
		OrderSaveVo orderSave = new OrderSaveVo();

		// 주문
		OcOrder ocOrder = new OcOrder();
		ocOrder.setOrderNo(parameter.getOrderNo());
		ocOrder.setOrgOrderNo(parameter.getOrderNo());
		ocOrder.setSiteNo(parameter.getSiteNo());
		ocOrder.setDeviceCode(parameter.getDeviceCode());
		ocOrder.setRsvOrderYn(parameter.getRsvOrderYn());
		// 임시주문으로 저장
		ocOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_TEMP);
		// 작성자 정보 (주문자와 동일)
		ocOrder.setRgsterNo(parameter.getMemberNo());

		// 주문자 정보
		ocOrder.setMemberNo(parameter.getMemberNo());
		ocOrder.setMemberTypeCode(parameter.getMemberTypeCode());
		ocOrder.setEmpYn(parameter.getEmpYn());
		// TODO : VIP 없어질경우 삭제 처리.
		ocOrder.setMbshpGradeCode(CommonCode.MBSHP_GRADE_CODE_NORMAL);
		// TODO : OTS VIP 확인 처리.
		ocOrder.setOtsVipYn(BaseConst.BOOLEAN_FALSE);
		ocOrder.setBuyerName(parameter.getBuyerName());
		ocOrder.setBuyerHdphnNoText(parameter.getBuyerHdphnNoText());
		ocOrder.setBuyerEmailAddrText(parameter.getBuyerEmailAddrText());

		if (parameter.getPswdText() != null) {
			Pair<String, String> pair = UtilsCrypt.getEncryptPassword(parameter.getPswdText());
			ocOrder.setPswdText(pair.getFirst());
			ocOrder.setPswdSaltText(pair.getSecond());
		}
		ocOrder.setEmpOrderYn(parameter.getEmpYn());

		// 주문 배송 정보
		ocOrder.setDlvyTypeCode(parameter.getDlvyTypeCode());
		ocOrder.setUnRecptTermExtsnYn(Const.BOOLEAN_FALSE);

		// 배송비
		ocOrder.setMmnyDlvyAmt(parameter.getMmnyDlvyAmt());
		ocOrder.setTotalVndrDlvyAmt(parameter.getTotalVndrDlvyAmt());

		// 일반 배송
		ocOrder.setRcvrName(parameter.getRcvrName());
		ocOrder.setRcvrHdphnNoText(parameter.getRcvrHdphnNoText());
		ocOrder.setRcvrPostCodeText(parameter.getRcvrPostCodeText());
		ocOrder.setRcvrPostAddrText(parameter.getRcvrPostAddrText());
		ocOrder.setRcvrDtlAddrText(parameter.getRcvrDtlAddrText());
		ocOrder.setDlvyMemoText(parameter.getDlvyMemoText());
		// 편의점 배송
		// 픽업 배송

		// TODO : 가격 정보
		ocOrder.setTotalNormalAmt(parameter.getTotalNormalAmt());
		ocOrder.setTotalSellAmt(parameter.getTotalSellAmt());
		ocOrder.setTotalPromoDscntAmt(parameter.getTotalPromoDscntAmt());
		ocOrder.setTotalCpnDscntAmt(parameter.getTotalCpnDscntAmt());
		ocOrder.setTotalEmpDscntAmt(parameter.getTotalEmpDscntAmt());

		ocOrder.setPointUseAmt(parameter.getPointUseAmt());
		ocOrder.setEventPointUseAmt(parameter.getEventPointUseAmt());
		ocOrder.setPymntTodoAmt(parameter.getPymntTodoAmt());
		ocOrder.setPymntAmt(parameter.getPymntAmt());
		ocOrder.setCnclAmt(0);
		ocOrder.setSalesCnclGbnType(CommonCode.SALES_CNCL_GBN_TYPE_SALE);
		// TODO : 현금영수증(not null 조건이 빠져야 할것으로 판단됨. )
		ocOrder.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
		ocOrder.setTaxBillIssueStatCode(CommonCode.TAX_BILL_ISSUE_STAT_CODE_APPLY);

		// TODO : 필요없는 컬럼 정리 할때 제거
		ocOrder.setBuyerTelNoText(parameter.getBuyerHdphnNoText());
		ocOrder.setBuyerPostCodeText(parameter.getRcvrPostCodeText());
		ocOrder.setBuyerPostAddrText(parameter.getRcvrPostAddrText());
		ocOrder.setBuyerDtlAddrText(parameter.getRcvrDtlAddrText());
		ocOrder.setRcvrTelNoText(parameter.getRcvrHdphnNoText());
		orderSave.setOcOrder(ocOrder);

		MbMember member = new MbMember();
		member.setMemberNo(parameter.getMemberNo());
		member.setMemberTypeCode(parameter.getMemberTypeCode());
		member.setEmpYn(parameter.getEmpYn());
		member.setSafeKey(parameter.getSafeKey());
		orderSave.setOrderMember(member);

		// 주문 편의점
		if (UtilsText.equals(ocOrder.getDlvyTypeCode(), CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP)) {
			parameter.getOrderConvenienceStore().setOrderNo(ocOrder.getOrderNo());
			parameter.getOrderConvenienceStore().setCvnstrSeq((short) 1);
			parameter.getOrderConvenienceStore().setDlvyPlaceYn(Const.BOOLEAN_TRUE);
			orderSave.setCvsStore(parameter.getOrderConvenienceStore());
		}

		// 주문 상품
		// 주문 상품 정보 저장
		List<OrderProduct> prdtList = new ArrayList<OrderProduct>();

		// 사은품 정보 저장
		List<OrderProduct> giftPrdtList = new ArrayList<OrderProduct>();

		// 배송비 상품 저장
		List<OrderProduct> dlvyPrdtList = new ArrayList<OrderProduct>();

		// 주문 상품 프로모션 정보 저장
		List<OrderPromotion> prdtPromotionList = new ArrayList<OrderPromotion>();

		// 주문 쿠폰 정보 저장
		List<OcOrderUseCoupon> couponList = new ArrayList<OcOrderUseCoupon>();
		short couponSeq = 1;

		short productSeq = 1;
		for (OrderProduct productParam : parameter.getOrderProduct()) {
			if (UtilsText.equals(productParam.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) {
				OrderProduct orderProduct = new OrderProduct();
				orderProduct = mapper.readValue(mapper.writeValueAsString(productParam), OrderProduct.class);
				orderProduct.setOrderPrdtSeq(productSeq);
				orderProduct.setOrderQty(1);// 주문 수량은 1로 고정
				orderProduct.setRsvPrdtYn(ocOrder.getRsvOrderYn()); // 예약주문여부
				orderProduct.setOrderPrdtStatCode(CommonCode.ORDER_STAT_CODE_TEMP);// 주문상품상태코드(임시주문)
				orderProduct.setRgsterNo(parameter.getMemberNo());
				orderProduct.setPrdtTypeCode(CommonCode.PRDT_TYPE_CODE_DELIVERY);
				orderProduct.setVndrName(productParam.getVndrName());
				if (parameter.getDlvyCouponList() != null && parameter.getDlvyCouponList().length > 0) {
					// 배송비 쿠폰
					for (OcOrderUseCoupon couponParam : parameter.getDlvyCouponList()) {
						if (UtilsText.equals(couponParam.getVndrNo(), orderProduct.getVndrNo())) {
							orderProduct.setCpnDscntAmt(orderProduct.getCpnDscntAmt() + couponParam.getDscntAmt());
							orderProduct.setOrderAmt(orderProduct.getOrderAmt() - couponParam.getDscntAmt());
							couponParam.setOrderNo(ocOrder.getOrderNo());
							couponParam.setOrderPrdtSeq(orderProduct.getOrderPrdtSeq());
							couponParam.setOrderUseCpnSeq(couponSeq);
							couponList.add(couponParam);
							couponSeq++;
						}
					}
				}

				dlvyPrdtList.add(orderProduct);
				productSeq++;
			} else {
				for (int i = 0; i < productParam.getOrderQty(); i++) {
					OrderProduct orderProduct = new OrderProduct();
					orderProduct = mapper.readValue(mapper.writeValueAsString(productParam), OrderProduct.class);
					orderProduct.setOrderPrdtSeq(productSeq);
					orderProduct.setOrderQty(1);// 주문 수량은 1로 고정
					orderProduct.setOrderAmt(productParam.getOrderAmt() / productParam.getOrderQty());
					orderProduct.setRsvPrdtYn(ocOrder.getRsvOrderYn()); // 예약주문여부
					// TODO : 임직원 할인율 계산
					if (UtilsText.equals(parameter.getEmpOrderYn(), Const.BOOLEAN_TRUE)) {
						// orderProduct.setEmpDscntRate(productParam.getEmpDscntRate()); // 할인율
						// orderProduct.setEmpAmt(productParam.getEmpAmt()); // 임직원가
						orderProduct.setEmpDscntRate((short) 0); // 할인율
						orderProduct.setEmpAmt(0); // 임직원가
					}
					// TODO : 할인 정보 처리
					orderProduct.setTotalDscntAmt(0);
					orderProduct.setCpnDscntAmt(0);

					// TODO : 입점사 수수료
					orderProduct.setVndrCmsnRate((short) 0);

					orderProduct.setSellCnclReqYn(Const.BOOLEAN_FALSE); // 취소요청여부
					orderProduct.setLogisCnvrtYn(Const.BOOLEAN_FALSE); // 택배전환여부
					orderProduct.setPickupExtsnYn(Const.BOOLEAN_FALSE); // 픽업연장여부
					orderProduct.setOrderPrdtStatCode(CommonCode.ORDER_STAT_CODE_TEMP);// 주문상품상태코드(임시주문)
					orderProduct.setRgsterNo(parameter.getMemberNo());
					orderProduct.setBuyPointSaveRate((short) 0);

					orderProduct.setVndrName(productParam.getVndrName());

					if (parameter.getPromotionList() != null) {
						for (OrderPromotion promotionParam : parameter.getPromotionList()) {
							if (UtilsText.equals(promotionParam.getCartSeq().toString(),
									orderProduct.getCartSeq().toString())) {
								OrderPromotion promotion = new OrderPromotion();
								promotion.setOrderNo(promotionParam.getOrderNo());
								promotion.setOrderPrdtSeq(orderProduct.getOrderPrdtSeq());
								promotion.setCartSeq(promotionParam.getCartSeq());
								promotion.setApplyPromoSeq(promotionParam.getApplyPromoSeq());
								promotion.setPromoNo(promotionParam.getPromoNo());
								promotion.setPromoTypeCode(promotionParam.getPromoTypeCode());
								promotion.setDscntType(promotionParam.getDscntType());
								promotion.setDscntValue(promotionParam.getDscntValue());

								if (promotionParam.getDscntAmt() > 0)
									promotion.setDscntAmt(promotionParam.getDscntAmt() / productParam.getOrderQty());
								if (promotion.getDscntAmt() > 0) { // 프로모션 할인 받은 경우
									orderProduct.setTotalDscntAmt(
											orderProduct.getTotalDscntAmt() + promotion.getDscntAmt());// 상품 할인 정보
								}
								// 상품 쿠폰 정보
								if (parameter.getPrdtCouponList() != null) {
									for (OrderCoupon prdtCouponList : parameter.getPrdtCouponList()) {
										if (prdtCouponList.getCartSeq().equals(orderProduct.getCartSeq())) {
											List<OcCartBenefit> prdtCoupon = prdtCouponList.getCouponList();
											for (OcCartBenefit coupon : prdtCoupon) {
												if (i < coupon.getCpnApplyQty()) {
													OcOrderUseCoupon ocOrderCoupon = new OcOrderUseCoupon();
													ocOrderCoupon.setOrderNo(orderProduct.getOrderNo());
													ocOrderCoupon.setOrderUseCpnSeq(couponSeq);
													ocOrderCoupon.setOrderPrdtSeq(orderProduct.getOrderPrdtSeq());
													ocOrderCoupon.setMemberNo(parameter.getMemberNo());
													ocOrderCoupon.setHoldCpnSeq(coupon.getHoldCpnSeq());
													ocOrderCoupon.setCpnNo(coupon.getCpnNo());
													ocOrderCoupon.setDscntAmt(
															coupon.getCpnApplyDcAmt() / productParam.getOrderQty());
													orderProduct.setTotalDscntAmt(orderProduct.getTotalDscntAmt()
															+ ocOrderCoupon.getDscntAmt());// 상품 할인 정보
													couponList.add(ocOrderCoupon);
													couponSeq++;
												}
											}
										}
									}
								}
								orderProduct
										.setOrderAmt(orderProduct.getPrdtNormalAmt() - orderProduct.getTotalDscntAmt());
								prdtList.add(orderProduct);
								productSeq++;

								prdtPromotionList.add(promotion);
								// 사은품 상품 생성.
								if (UtilsText.equals(promotion.getPromoTypeCode(),
										CommonCode.PROMO_TYPE_CODE_GIFT_PAYMENT)) {
									OrderProduct giftProduct = new OrderProduct();
									giftProduct.setOrderNo(promotion.getOrderNo());
									giftProduct.setUpOrderPrdtSeq(promotion.getOrderPrdtSeq());
									giftProduct.setOrderPrdtSeq(productSeq);
									giftProduct.setPrdtNo(promotion.getPrdtNo());
									giftProduct.setPrdtOptnNo(promotion.getPrdtOptnNo());
									giftProduct.setPrdtTypeCode(CommonCode.PRDT_TYPE_CODE_GIFT);
									giftProduct.setEventNo(orderProduct.getEventNo());
									giftProduct.setPlndpNo(orderProduct.getPlndpNo());
									giftProduct.setOrderPrdtStatCode(CommonCode.ORDER_STAT_CODE_TEMP);// 주문상품상태코드(임시주문)
									giftProduct.setRsvPrdtYn(ocOrder.getRsvOrderYn()); // 예약주문여부
									giftProduct.setRgsterNo(parameter.getMemberNo());
									giftProduct.setVndrName(productParam.getVndrName());
									giftPrdtList.add(giftProduct);
									productSeq++;
								}
							}
						}
					} else {
						// 상품 쿠폰 정보
						if (parameter.getPrdtCouponList() != null) {
							for (OrderCoupon prdtCouponList : parameter.getPrdtCouponList()) {
								if (prdtCouponList.getCartSeq().equals(orderProduct.getCartSeq())) {
									List<OcCartBenefit> prdtCoupon = prdtCouponList.getCouponList();
									for (OcCartBenefit coupon : prdtCoupon) {
										if (i < coupon.getCpnApplyQty()) {
											OcOrderUseCoupon ocOrderCoupon = new OcOrderUseCoupon();
											ocOrderCoupon.setOrderNo(orderProduct.getOrderNo());
											ocOrderCoupon.setOrderUseCpnSeq(couponSeq);
											ocOrderCoupon.setOrderPrdtSeq(orderProduct.getOrderPrdtSeq());
											ocOrderCoupon.setMemberNo(parameter.getMemberNo());
											ocOrderCoupon.setHoldCpnSeq(coupon.getHoldCpnSeq());
											ocOrderCoupon.setCpnNo(coupon.getCpnNo());
											ocOrderCoupon.setDscntAmt(
													coupon.getCpnApplyDcAmt() / productParam.getOrderQty());
											// 상품 할인 정보
											orderProduct.setTotalDscntAmt(
													orderProduct.getTotalDscntAmt() + ocOrderCoupon.getDscntAmt());
											couponList.add(ocOrderCoupon);
											couponSeq++;
										}
									}
								}
							}
						}
						orderProduct.setOrderAmt(orderProduct.getPrdtNormalAmt() - orderProduct.getTotalDscntAmt());
						prdtList.add(orderProduct);
						productSeq++;
					}

				}
			}
		}

		orderSave.setPrdtList(prdtList);
		orderSave.setGiftPrdtList(giftPrdtList);
		orderSave.setDlvyPrdtList(dlvyPrdtList);
		orderSave.setPrdtPromotionList(prdtPromotionList);

		// 더블 포인트 쿠폰
		if (parameter.getDoublePointCoupon() != null) {
			// TODO: 포인트 계산 후 주문에 savepoint 추가.
			OcOrderUseCoupon doubleCoupon = parameter.getDoublePointCoupon();
			doubleCoupon.setOrderUseCpnSeq(couponSeq);
			couponList.add(parameter.getDoublePointCoupon());
			couponSeq++;
		}

		// 상품 쿠폰
		orderSave.setCouponList(couponList);
		// 주문 상품 배송 이력
		List<OcOrderDeliveryHistory> dlvyList = new ArrayList<OcOrderDeliveryHistory>();

		short deliverySeq = 1; // 첫번째 생성이기 때문에 배송이력 순번, 배송차수 1로 고정
		for (OrderProduct product : prdtList) {
			OcOrderDeliveryHistory delivery = new OcOrderDeliveryHistory();
			delivery.setOrderNo(product.getOrderNo()); // 주문 번호
			delivery.setOrderPrdtSeq(product.getOrderPrdtSeq()); // 주문 상품 순번
			delivery.setPrdtNo(product.getPrdtNo());// 상품 번호
			delivery.setOrderDlvyHistSeq(deliverySeq); // 주문 배송 이력 순번
			delivery.setDlvyIdText(getDeliveryId(product.getOrderNo(), product.getOrderPrdtSeq(), deliverySeq));
			delivery.setDlvyDgreCount(deliverySeq); // 배송차수
			delivery.setRcvrName(ocOrder.getRcvrName());// 수취인명
			delivery.setRcvrTelNoText(ocOrder.getRcvrHdphnNoText());// 수취인전화번호
			delivery.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());// 수취인핸드폰번호
			delivery.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());// 수취인우편번호
			delivery.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());// 수취인우편주소
			delivery.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());// 수취인상세주소
			delivery.setRgsterNo(product.getRgsterNo()); // 등록자번호
			if (UtilsText.equals(ocOrder.getRsvOrderYn(), Const.BOOLEAN_TRUE)) {
				delivery.setRsvDlvyDtm(parameter.getRsvDlvyYmd());
			}

			delivery.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // TODO : 재고 구분 AI로 강제 입력 , 재고 처리 후 정상 처리 필요

			dlvyList.add(delivery);
		}

		for (OrderProduct product : giftPrdtList) {
			OcOrderDeliveryHistory delivery = new OcOrderDeliveryHistory();
			delivery.setOrderNo(product.getOrderNo()); // 주문 번호
			delivery.setOrderPrdtSeq(product.getOrderPrdtSeq()); // 주문 상품 순번
			delivery.setOrderDlvyHistSeq(deliverySeq); // 주문 배송 이력 순번
			delivery.setDlvyIdText(getDeliveryId(product.getOrderNo(), product.getOrderPrdtSeq(), deliverySeq));
			delivery.setDlvyDgreCount(deliverySeq); // 배송차수
			delivery.setStoreChngDgreCount((short) 0);// 매장변경차수 0
			delivery.setDlvyDscntcYn(Const.BOOLEAN_FALSE);// 배송중단여부
			delivery.setMissProcYn(Const.BOOLEAN_FALSE);// 분실처리여부
			delivery.setWmsSendYn(Const.BOOLEAN_FALSE); // WMS전송여부
			delivery.setRcvrName(ocOrder.getRcvrName());// 수취인명
			delivery.setRcvrTelNoText(ocOrder.getRcvrHdphnNoText());// 수취인전화번호
			delivery.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());// 수취인핸드폰번호
			delivery.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());// 수취인우편번호
			delivery.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());// 수취인우편주소
			delivery.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());// 수취인상세주소
			delivery.setLogisPymntPrdtAmt(0); // 택배사결제상품금액
			delivery.setLogisPymntDlvyAmt(0); // 택배사결제배송비
			delivery.setRgsterNo(product.getRgsterNo()); // 등록자번호
			if (UtilsText.equals(ocOrder.getRsvOrderYn(), Const.BOOLEAN_TRUE)) {
				delivery.setRsvDlvyDtm(parameter.getRsvDlvyYmd());
			}

			delivery.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // TODO : 재고 구분 AI로 강제 입력 , 재고 처리 후 정상 처리 필요

			dlvyList.add(delivery);
		}
		orderSave.setDlvyList(dlvyList);
		// 주문 약관
		OrderTermsAgree[] termsList = parameter.getOrderTermsAgree();

		orderSave.setTermsList(termsList);

		// 기프트카드
		OrderPayment giftCard = parameter.getOrderGiftPayment();
		// 주 결제수단
		OrderPayment mainPayment = parameter.getOrderMainPayment();

		short orderPymntSeq = 1;
		if (mainPayment != null && mainPayment.getPayAmt() > 0) {
			mainPayment.setOrderNo(ocOrder.getOrderNo());
			mainPayment.setOrderPymntSeq(orderPymntSeq);
			mainPayment.setMainPymntMeansYn(Const.BOOLEAN_TRUE);
			orderSave.setMainPayment(mainPayment);
			orderPymntSeq++;
		}

		if (giftCard != null && giftCard.getPayAmt() > 0) {
			giftCard.setOrderNo(ocOrder.getOrderNo());
			giftCard.setOrderPymntSeq(orderPymntSeq);
			if (mainPayment != null && mainPayment.getPayAmt() == 0)
				giftCard.setMainPymntMeansYn(Const.BOOLEAN_TRUE);
			else
				giftCard.setMainPymntMeansYn(Const.BOOLEAN_FALSE);
			orderSave.setGiftPayment(giftCard);
			orderPymntSeq++;
		}

		// 구매 포인트
		if (parameter.getAccessPointUseAmt() > 0) {
			OrderPayment accessPoint = new OrderPayment();
			accessPoint.setOrderNo(ocOrder.getOrderNo());
			accessPoint.setOrderPymntSeq(orderPymntSeq);
			accessPoint.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_BUY_POINT);
			accessPoint.setPayAmt(parameter.getAccessPointUseAmt());
			if ((mainPayment == null && giftCard == null)
					|| (mainPayment.getPayAmt() == 0 && giftCard.getPayAmt() == 0))
				accessPoint.setMainPymntMeansYn(Const.BOOLEAN_TRUE);
			else
				accessPoint.setMainPymntMeansYn(Const.BOOLEAN_FALSE);
			orderSave.setAccessPointPayment(accessPoint);
			orderPymntSeq++;
		}

		// 이벤트 포인트
		if (parameter.getEventPointUseAmt() > 0) {
			OrderPayment eventPoint = new OrderPayment();
			eventPoint.setOrderNo(ocOrder.getOrderNo());
			eventPoint.setOrderPymntSeq(orderPymntSeq);
			eventPoint.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_EVENT_POINT);
			eventPoint.setPayAmt(parameter.getEventPointUseAmt());
			if ((mainPayment == null && giftCard == null && parameter.getAccessPointUseAmt() == 0)
					|| (mainPayment.getPayAmt() == 0 && giftCard.getPayAmt() == 0
							&& parameter.getAccessPointUseAmt() == 0))
				eventPoint.setMainPymntMeansYn(Const.BOOLEAN_TRUE);
			else
				eventPoint.setMainPymntMeansYn(Const.BOOLEAN_FALSE);
			orderSave.setEventPointPayment(eventPoint);
			orderPymntSeq++;
		}

		System.out.println("order :::::::::: " + mapper.writeValueAsString(orderSave));

		return orderSave;
	}

	/**
	 * @Desc : 상품 배송 번호 설정.
	 * @Method Name : getDeliveryId
	 * @Date : 2019. 7. 1.
	 * @Author : ljyoung@3top.co.kr
	 * @param _orderNo
	 * @param _prdtSeq
	 * @param _deliverySeq
	 * @return
	 * @throws Exception
	 */
	public String getDeliveryId(String _orderNo, Short _prdtSeq, short _deliverySeq) throws Exception {
		return _orderNo + String.format("%02d", _prdtSeq) + String.format("%02d", _deliverySeq);
	}

	/**
	 * @Desc : 주문 승인 validation
	 * @Method Name : validOrder
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @throws Exception
	 */
	private void validOrder(OrderSaveVo parameter) throws Exception {

	}

	/**
	 * @Desc : 주문 데이터 저장
	 * @Method Name : saveOrder
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @throws Exception
	 */
	private Map<String, Object> saveOrder(OrderSaveVo parameter, Map<String, Object> resultMap) throws Exception {
		// 주문 마스터 저장
		ocOrderDao.insert(parameter.getOcOrder());

		// 주문 CVS 정보 저장
		// 주문 편의점
		if (UtilsText.equals(parameter.getOcOrder().getDlvyTypeCode(), CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP)) {
			ocOrderConvenienceStoreDao.insertCVS(parameter.getCvsStore());
		}

		// 주문 상품 정보 저장
		if (!parameter.getPrdtList().isEmpty() && parameter.getPrdtList().size() > 0) {
			List<OrderProduct> savePrdtList = new ArrayList<OrderProduct>();
			int cnt = 0;
			for (OrderProduct savePrdt : parameter.getPrdtList()) {
				cnt++;
				savePrdtList.add(savePrdt);

				if (cnt % 10 == 0) { // MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
					ocOrderProductDao.insertProductList(savePrdtList);
					savePrdtList = new ArrayList<OrderProduct>();
				}
			}
			if (cnt % 10 > 0) {
				ocOrderProductDao.insertProductList(savePrdtList);
			}
		}

		// 주문 사은품 상품 정보 저장
		if (parameter.getDlvyList() != null && parameter.getGiftPrdtList().size() > 0) {
			List<OrderProduct> savePrdtList = new ArrayList<OrderProduct>();
			int cnt = 0;
			for (OrderProduct savePrdt : parameter.getGiftPrdtList()) {
				cnt++;
				savePrdtList.add(savePrdt);

				if (cnt % 10 == 0) { // MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
					ocOrderProductDao.insertGiftProduct(savePrdtList);
					savePrdtList = new ArrayList<OrderProduct>();
				}
			}
			if (cnt % 10 > 0) {
				ocOrderProductDao.insertGiftProduct(savePrdtList);
			}
		}

		// 주문 배송비 상품 정보 저장
		if (parameter.getDlvyPrdtList() != null && parameter.getDlvyPrdtList().size() > 0) {
			List<OrderProduct> savePrdtList = new ArrayList<OrderProduct>();
			int cnt = 0;
			for (OrderProduct savePrdt : parameter.getDlvyPrdtList()) {
				cnt++;
				savePrdtList.add(savePrdt);
				if (cnt % 10 == 0) { // MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
					ocOrderProductDao.insertDlvyProduct(savePrdtList);
					savePrdtList = new ArrayList<OrderProduct>();
				}
			}
			if (cnt % 10 > 0) { // MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
				ocOrderProductDao.insertDlvyProduct(savePrdtList);
			}
		}

		if (parameter.getDlvyList() != null && parameter.getDlvyList().size() > 0) {
			// 주문 배송 이력 정보 저장
			List<OcOrderDeliveryHistory> saveDlvyList = new ArrayList<OcOrderDeliveryHistory>();
			int cnt = 0;
			for (OcOrderDeliveryHistory saveDlvy : parameter.getDlvyList()) {
				cnt++;
				saveDlvyList.add(saveDlvy);
				if (cnt % 10 == 0) {// MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
					ocOrderDeliveryHistoryDao.insertDeliveryList(saveDlvyList);
					saveDlvyList = new ArrayList<OcOrderDeliveryHistory>();
				}

			}
			if (cnt % 10 > 0) {// MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
				ocOrderDeliveryHistoryDao.insertDeliveryList(saveDlvyList);
			}
		}

		// 주문 상품 프로모션 저장
		if (parameter.getPrdtPromotionList() != null && parameter.getPrdtPromotionList().size() > 0) {
			List<OrderPromotion> savePromoList = new ArrayList<OrderPromotion>();
			int cnt = 0;
			for (OrderPromotion savePromo : parameter.getPrdtPromotionList()) {
				cnt++;
				savePromoList.add(savePromo);
				if (cnt % 10 == 0) {// MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
					ocOrderProductApplyPromotionDao.insertPromotionList(savePromoList);
					savePromoList = new ArrayList<OrderPromotion>();
				}
			}
			if (cnt % 10 > 0) {// MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
				ocOrderProductApplyPromotionDao.insertPromotionList(savePromoList);
			}
		}

		// 주문 쿠폰 정보 저장
		if (parameter.getCouponList() != null && parameter.getCouponList().size() > 0) {
			List<OcOrderUseCoupon> saveCouponList = new ArrayList<OcOrderUseCoupon>();
			int cnt = 0;
			for (OcOrderUseCoupon saveCoupon : parameter.getCouponList()) {
				cnt++;
				saveCouponList.add(saveCoupon);
				if (cnt % 10 == 0) {// MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
					ocOrderUseCouponDao.insertList(parameter.getCouponList());
					saveCouponList = new ArrayList<OcOrderUseCoupon>();
				}
			}
			if (cnt % 10 > 0) {// MSSQL 프로시져 변수 처리 갯수 문제로 인해 10건 단위로 끊어 저장한다.
				ocOrderUseCouponDao.insertList(parameter.getCouponList());
			}
		}

		// 주문 동의 정보 저장
		ocOrderTermsAgreeDao.insertOrderTermsAgree(parameter.getTermsList());

		return resultMap;
	}

	/**
	 * @Desc : 주문 결제 승인 요청(PG)
	 * @Method Name : paymentConfirm
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @param resultMap
	 * @throws Exception
	 */
	public Map<String, Object> paymentConfirm(OrderSaveVo parameter, Map<String, Object> resultMap) throws Exception {

		// 포인트 사용
		if (parameter.getOcOrder().getPointUseAmt() > 0) {
			// 회원정보를 조회하여 SafeKey를 세팅
			boolean result = false;
			int eventPoint = 0;// 이벤트포인트
			int accessPoint = 0;// 구매포인트

			if (parameter.getEventPointPayment() != null && parameter.getEventPointPayment().getPayAmt() > 0)
				eventPoint = parameter.getEventPointPayment().getPayAmt();

			if (parameter.getAccessPointPayment() != null && parameter.getAccessPointPayment().getPayAmt() > 0)
				accessPoint = parameter.getAccessPointPayment().getPayAmt();

			MbMember member = parameter.getOrderMember();
//			result = membershipPointService.updatePointForMembershipHandler(member.getSafeKey(), accessPoint,
//					BaseConst.POINT_TYPE_USE, eventPoint);
			result = true;
			if (result) {
				// 이벤트 포인트 저장
				if (eventPoint > 0) {
					resultMap = savePointPayment(parameter, parameter.getEventPointPayment(), resultMap);
				}

				// 구매 포인트 저장
				if (accessPoint > 0) {
					resultMap = savePointPayment(parameter, parameter.getAccessPointPayment(), resultMap);
				}
			} else {
				resultMap.put("result", Const.RESULT_FAIL);
			}

		}

		// 기프트카드결제
		if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_SUCCESS)
				&& parameter.getGiftPayment() != null && parameter.getGiftPayment().getPayAmt() > 0) {
			CommNiceRes<CollectionResponse> giftApproval = giftCardApproval(parameter.getGiftPayment().getGiftCardNo(),
					parameter.getGiftPayment().getPayAmt(), parameter.getGiftPayment().getGiftCardPinNo());
			if (UtilsText.equals(giftApproval.getResCode(), "0000")) { // TODO : const로 보내는 것에 대한 생각 필요.
				resultMap = saveGiftPayment(parameter, giftApproval, resultMap);
				resultMap.put("giftApproval", giftApproval);
			} else {
				// TODO: 주문 rollback
				// giftResult;
				CommNiceRes<CollectionResponse> giftCancel = giftCardCancel(parameter.getGiftPayment().getGiftCardNo(),
						parameter.getGiftPayment().getPayAmt(), parameter.getGiftPayment().getGiftCardPinNo(),
						giftApproval.getResData().getApprovalNo(), giftApproval.getResData().getApprovalDate());
				resultMap.put("result", Const.RESULT_FAIL);
			}
		}

		if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_SUCCESS)
				&& parameter.getMainPayment() != null) {
			OrderPayment payment = parameter.getMainPayment();
			payment.setOrderNo(parameter.getOcOrder().getOrderNo());
			// 핸드폰 결제는 실결제가 일어나서 테스트 중에는 승인처리 하지 않는다.
			if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_HANDPHONE)) {
				KcpPaymentApprovalReturn hpReturn = new KcpPaymentApprovalReturn();

				hpReturn.setTno(CommonCode.PYMNT_MEANS_CODE_HANDPHONE + " TEST");
				hpReturn.setAmount(payment.getPayAmt().toString());
				hpReturn.setResCd("0000");
				hpReturn.setResMsg("{핸드폰테스트}");
				hpReturn.setCommid("KT");
				hpReturn.setVanCd("KT");
				hpReturn.setMobileNo("010-1234-5678");

				PaymentResult result = new PaymentResult("Y", "0000", "", hpReturn,
						PaymentConst.PAYMENT_APPROVAL_SUCCESS_MESSAGE, "");
				System.out.println("휴대폰 결제는 실결제라 테스트중 승인 패스 합니다 !!!!!!!!!!!!!!!!!!!!!!");
				resultMap = saveMainPayment(parameter, result, resultMap);
			} else {

				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_KAKAO_PAY))
					payment.getKakaoPaymentApproval().setPartnerUserId(parameter.getOcOrder().getMemberNo());

				System.out.println("결제 승인!!!!!!!!!!!!");
				PaymentResult result = paymentEntrance
						.approval(new PaymentInfo(payment.getAddInfo2(), payment.returnPayment()));
				if (result.getSuccessYn().equals(Const.BOOLEAN_TRUE)) {
					resultMap = saveMainPayment(parameter, result, resultMap);
					resultMap.put("mainPaymentApproval", result);
				} else {
					// TODO: 주문 rollback
					resultMap.put("result", Const.RESULT_FAIL);
				}
			}
		}
		return resultMap;
	}

	/**
	 * @Desc : 결제중 문제 발생시 결제 수단 승인 취소 처리.
	 * @Method Name : paymentCancel
	 * @Date : 2019. 6. 21.
	 * @Author : ljyoung@3top.co.kr
	 * @param resultMap
	 * @throws Exception
	 */
	public void paymentCancel(OrderSaveVo parameter, Map<String, Object> resultMap) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String cancelReason = "결제 처리 실패";
		// 주결제수단 취소.
		if (resultMap.get("mainPaymentApproval") != null) {
			PaymentResult mainPaymentApproval = (PaymentResult) resultMap.get("mainPaymentApproval");
			OrderPayment payment = parameter.getMainPayment();
			if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_HANDPHONE)) {
				System.out.println("휴대폰 결제는 실결제라 테스트중 취소 패스 합니다 !!!!!!!!!!!!!!!!!!!!!!");
			} else {
				switch (payment.getAddInfo2()) {
				case Const.PAYMENT_VENDOR_NAME_KCP:
					InetAddress ip = InetAddress.getLocalHost();
					String custIp = ip.getHostAddress();

					KcpPaymentApprovalReturn kcpResult = (KcpPaymentApprovalReturn) mapper.readValue(
							mapper.writeValueAsString(mainPaymentApproval.getData()),
							mainPaymentApproval.getData().getClass());

					KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
					kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
					kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));

					kcpPaymentCancel.setReqTx(""); // 요청종류 : 일반취소는 빈 값 처리
					kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
					kcpPaymentCancel.setCustIp(custIp); // 요청 IP
					kcpPaymentCancel.setTno(kcpResult.getTno()); // 거래번호
					kcpPaymentCancel.setModDesc(cancelReason); // 취소사유

					paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
					break;
				case Const.PAYMENT_VENDOR_NAME_NAVER:
					NaverPaymentApprovalReturn naverResult = (NaverPaymentApprovalReturn) mapper.readValue(
							mapper.writeValueAsString(mainPaymentApproval.getData()),
							mainPaymentApproval.getData().getClass());
					NaverPaymentApprovalReturnBodyDetail naverpayDeatail = naverResult.getBody().getDetail();
					NaverPaymentCancel naverPaymentCancel = new NaverPaymentCancel();

					naverPaymentCancel.setPaymentId(naverpayDeatail.getPaymentId());
					naverPaymentCancel.setCancelAmount(naverpayDeatail.getTotalPayAmount());
					naverPaymentCancel.setCancelReason(cancelReason);

					paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_NAVER, naverPaymentCancel));
					break;
				case Const.PAYMENT_VENDOR_NAME_KAKAO:
					KakaoPaymentApprovalReturn kakaoResult = (KakaoPaymentApprovalReturn) mapper.readValue(
							mapper.writeValueAsString(mainPaymentApproval.getData()),
							mainPaymentApproval.getData().getClass());
					KakaoPaymentCancel kakaoPaymentCancel = new KakaoPaymentCancel();

					kakaoPaymentCancel.setCid(kakaoResult.getCid());
					kakaoPaymentCancel.setTid(kakaoResult.getTid());
					kakaoPaymentCancel.setCancelAmount(kakaoResult.getAmount().getTotal());
					kakaoPaymentCancel.setCancelTaxFreeAmount(kakaoResult.getAmount().getTaxFree());

					paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KAKAO, kakaoPaymentCancel));
					break;

				}
			}
		}

		// 기프트카드 취소.
		if (resultMap.get("giftApproval") != null) {
			OrderPayment giftApproval = parameter.getGiftPayment();
			CommNiceRes<CollectionResponse> giftCancel = (CommNiceRes<CollectionResponse>) resultMap
					.get("giftApproval");

			giftCardCancel(giftApproval.getGiftCardNo(), parameter.getGiftPayment().getPayAmt(),
					parameter.getGiftPayment().getGiftCardPinNo(), giftCancel.getResData().getApprovalNo(),
					giftCancel.getResData().getApprovalDate());
		}

		// 포인트 사용 취소.
		if (parameter.getOcOrder().getPointUseAmt() > 0) {
			// 회원정보를 조회하여 SafeKey를 세팅
			boolean result = false;
			int eventPoint = 0;// 이벤트포인트
			int accessPoint = 0;// 구매포인트

			if (parameter.getEventPointPayment() != null && parameter.getEventPointPayment().getPayAmt() > 0)
				eventPoint = parameter.getEventPointPayment().getPayAmt();

			if (parameter.getAccessPointPayment() != null && parameter.getAccessPointPayment().getPayAmt() > 0)
				accessPoint = parameter.getAccessPointPayment().getPayAmt();

			MbMember member = parameter.getOrderMember();
			result = membershipPointService.updatePointForMembershipHandler(member.getSafeKey(), accessPoint,
					BaseConst.POINT_TYPE_CANCEL, eventPoint);
		}
	}

	/**
	 * @Desc : 기프트 카드 사용 전문
	 * @Method Name : giftCardApproval
	 * @Date : 2019. 6. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param cardNo
	 * @param amountVal
	 * @param conPin
	 * @return
	 */
	public CommNiceRes<CollectionResponse> giftCardApproval(String cardNo, int amountVal, String conPin)
			throws Exception {
		CollectionRequest collectionRequest = new CollectionRequest("A4", "1", "37" + cardNo + "=4912", amountVal,
				conPin);
		CommNiceRes<CollectionResponse> collectionResult = niceGiftService.sendCollection(collectionRequest);
		return collectionResult;

	}

	/**
	 * @Desc : 기프트 카드 망취소
	 * @Method Name : giftCardCancel
	 * @Date : 2019. 6. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param oApprovalNo
	 * @param oApprovalDate
	 * @return
	 * @throws Exception
	 */
	public CommNiceRes<CollectionResponse> giftCardCancel(String cardNo, int amountVal, String conPin,
			String oApprovalNo, String oApprovalDate) throws Exception {
		return giftCardCancel(cardNo, amountVal, conPin, oApprovalNo, oApprovalDate, true);
	}

	/**
	 * @Desc : 기프트카드 취소
	 * @Method Name : giftCardCancel
	 * @Date : 2019. 6. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param cardNo        카드 번호
	 * @param amountVal     취소 금액
	 * @param conPin        인증번호
	 * @param oApprovalNo   결제승인번호
	 * @param oApprovalDate 결제승인일자 ex) 20190610
	 * @param orderCancel   주문 오류로 인한 망취소 일 경우 아닐경우 false
	 * @return
	 * @throws Exception
	 */
	public CommNiceRes<CollectionResponse> giftCardCancel(String cardNo, int amountVal, String conPin,
			String oApprovalNo, String oApprovalDate, boolean orderCancel) throws Exception {
		CollectionRequest collectionRequest = new CollectionRequest("A4", "1", "37" + cardNo + "=4912", amountVal,
				conPin);
		collectionRequest.setMessageType("0420");
		if (!orderCancel)
			collectionRequest.setServiceCode("C4");// 회수취소시만 사용 -> 망취소일때는 생략
		collectionRequest.setOApprovalNo(oApprovalNo);
		collectionRequest.setOApprovalDate(oApprovalDate);

		CommNiceRes<CollectionResponse> collectionResult = niceGiftService.sendCollection(collectionRequest);
		return collectionResult;
	}

	/**
	 * @Desc : 포인트 사용 정보 저장
	 * @Method Name : savePointPayment
	 * @Date : 2019. 6. 28.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @param pointPayment
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> savePointPayment(OrderSaveVo parameter, OrderPayment pointPayment,
			Map<String, Object> resultMap) throws Exception {
		try {
			OcOrderPayment ocOrderPayment = new OcOrderPayment();
			ocOrderPayment.setOrderNo(pointPayment.getOrderNo());
			ocOrderPayment.setOrderPymntSeq(pointPayment.getOrderPymntSeq());

			ocOrderPayment.setDeviceCode(parameter.getOcOrder().getDeviceCode());
			ocOrderPayment.setMainPymntMeansYn(pointPayment.getMainPymntMeansYn());
			ocOrderPayment.setPymntMeansCode(pointPayment.getPymntMeansCode());

			ocOrderPayment.setPymntTodoAmt(pointPayment.getPayAmt()); // 결제예정금액
			ocOrderPayment.setPymntAmt(pointPayment.getPayAmt()); // 결제금액

			ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료
			ocOrderPayment.setRgsterNo(parameter.getOcOrder().getMemberNo());

			ocOrderPaymentDao.insertPointPayment(ocOrderPayment);
		} catch (Exception e) {
			resultMap.put("result", Const.RESULT_FAIL);
		}

		return resultMap;
	}

	/**
	 * @Desc : 기프트 카드 결제 정보 저장
	 * @Method Name : saveGiftPayment
	 * @Date : 2019. 6. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveGiftPayment(OrderSaveVo parameter, CommNiceRes<CollectionResponse> collectionResult,
			Map<String, Object> resultMap) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			OrderPayment payment = parameter.getGiftPayment();
			CollectionResponse resData = collectionResult.getResData();

			OcOrderPayment ocOrderPayment = new OcOrderPayment();
			ocOrderPayment.setOrderNo(payment.getOrderNo());
			ocOrderPayment.setOrderPymntSeq(payment.getOrderPymntSeq());
			ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_NICE);
			ocOrderPayment.setPymntOrganCodeText("NICE");
			ocOrderPayment.setPymntOrganName("기프트카드");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
			Date apprDate = dateFormat.parse(resData.getApprovalDate());

			ocOrderPayment.setPymntDtm(new java.sql.Timestamp(apprDate.getTime()));// 기프트 카드 승인 날짜 저장
			ocOrderPayment.setDeviceCode(parameter.getOcOrder().getDeviceCode());
			ocOrderPayment.setMainPymntMeansYn(payment.getMainPymntMeansYn());
			ocOrderPayment.setPymntMeansCode(payment.getPymntMeansCode());
			ocOrderPayment.setPymntLogInfo(mapper.writeValueAsString(resData));
			ocOrderPayment.setRgsterNo(parameter.getOcOrder().getMemberNo());

			ocOrderPayment.setPymntAprvNoText(resData.getApprovalNo()); // 거래고유번호
			ocOrderPayment.setPymntTodoAmt(payment.getPayAmt()); // 결제예정금액
			ocOrderPayment.setPymntAmt(payment.getPayAmt()); // 결제금액
			ocOrderPayment.setRspnsCodeText(collectionResult.getResCode());
			ocOrderPayment.setRspnsMesgText(collectionResult.getResMsg());

			ocOrderPayment.setPymntOrganNoText(payment.getGiftCardNo()); // 사용 카드 번호
			ocOrderPayment.setGiftCardPinNoText(payment.getGiftCardPinNo());// 카드 인증번호

			ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료

			ocOrderPaymentDao.insertGiftPayment(ocOrderPayment);

		} catch (Exception e) {
			resultMap.put("result", Const.RESULT_FAIL);
		}

		return resultMap;
	}

	/**
	 * @Desc : 주문 주결제 정보 저장
	 * @Method Name : savePayment
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter, result
	 * @throws Exception
	 */
	public Map<String, Object> saveMainPayment(OrderSaveVo parameter, PaymentResult result,
			Map<String, Object> resultMap) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			OrderPayment payment = parameter.getMainPayment();

			OcOrderPayment ocOrderPayment = new OcOrderPayment();

			ocOrderPayment.setOrderNo(payment.getOrderNo());
			ocOrderPayment.setOrderPymntSeq(payment.getOrderPymntSeq());
			ocOrderPayment.setDeviceCode(parameter.getOcOrder().getDeviceCode());
			ocOrderPayment.setMainPymntMeansYn(payment.getMainPymntMeansYn());
			ocOrderPayment.setPymntMeansCode(payment.getPymntMeansCode());
			ocOrderPayment.setPymntLogInfo(mapper.writeValueAsString(result.getData()));
			ocOrderPayment.setRgsterNo(parameter.getOcOrder().getMemberNo());
			ocOrderPayment.setPymntMeansChngPsbltYn(Const.BOOLEAN_FALSE); // 결제수단변경가능여부

			switch (payment.getAddInfo2()) {

			case Const.PAYMENT_VENDOR_NAME_KCP:
				KcpPaymentApprovalReturn kcpResult = (KcpPaymentApprovalReturn) mapper
						.readValue(mapper.writeValueAsString(result.getData()), result.getData().getClass());
				ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP);
				ocOrderPayment.setPymntAprvNoText(kcpResult.getTno()); // KCP 거래고유번호
				ocOrderPayment.setPymntTodoAmt(Integer.parseInt(kcpResult.getAmount())); // 결제예정금액
				ocOrderPayment.setPymntAmt(Integer.parseInt(kcpResult.getAmount())); // 결제금액
				ocOrderPayment.setDpstPressEmailSendYn(Const.BOOLEAN_FALSE); // 입금독촉메일발송여부
				ocOrderPayment.setRspnsCodeText(kcpResult.getResCd());
				ocOrderPayment.setRspnsMesgText(kcpResult.getResMsg());

				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getCardCd());
					ocOrderPayment.setPymntOrganName(kcpResult.getCardName());
					ocOrderPayment.setPymntOrganNoText(kcpResult.getCardNo());// [신용카드]카드번호, 16자리중 세번째구간 마스킹처리
					if (kcpResult.getCardBinType02().equals("0")) // [신용카드]카드 구분정보(체크카드 여부), 0 : 일반, 1 : 체크
						ocOrderPayment.setCardGbnType("N");
					else
						ocOrderPayment.setCardGbnType("C");
					if (kcpResult.getCardBinType01().equals("0")) // [신용카드]카드 구분정보 (법인카드 여부), 0 : 개인, 1 : 법인
						ocOrderPayment.setCardType("N");
					else
						ocOrderPayment.setCardType("C");
					ocOrderPayment.setCardPartCnclPsbltYn(kcpResult.getPartcancYn());// [신용카드]부분취소 가능 유무
					ocOrderPayment.setIntrstFreeYn(kcpResult.getNoinf());// [신용카드]무이자여부
					ocOrderPayment.setInstmtTermCount(Short.parseShort(kcpResult.getQuota()));// [신용카드]할부기간, 00 : 일시불
					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료

					ocOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부
					ocOrderPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부
				}
				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getBankCode());
					ocOrderPayment.setPymntOrganName(kcpResult.getBankName());
					ocOrderPayment.setPymntOrganNoText(kcpResult.getAccount());

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
					Date appTime = dateFormat.parse(kcpResult.getAppTime());
					Date vaDate = dateFormat.parse(kcpResult.getVaDate());
					ocOrderPayment.setVrtlAcntIssueDtm(new java.sql.Timestamp(appTime.getTime()));
					ocOrderPayment.setVrtlAcntExprDtm(new java.sql.Timestamp(vaDate.getTime()));
					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_WAIT_DEPOSIT); // 입금대기
				}
				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getBankCode());
					ocOrderPayment.setPymntOrganName(kcpResult.getBankName());
//				ocOrderPayment.setPymntOrganNoText(kcpResult.getAccount()); //계좌번호 넘어오지 않음

					if (kcpResult.getCashAuthno() != null && kcpResult.getCashNo() != null) {
						ocOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_TRUE);
					} else {
						ocOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
					}
					if (kcpResult.getCashAuthno() != null)
						ocOrderPayment.setCashRcptAprvNo(kcpResult.getCashAuthno());
					if (kcpResult.getCashNo() != null)
						ocOrderPayment.setCashRcptDealNo(kcpResult.getCashNo());
					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료
				}

				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_HANDPHONE)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getCommid());
					ocOrderPayment.setPymntOrganName(kcpResult.getVanCd());
					ocOrderPayment.setPymntOrganNoText(kcpResult.getMobileNo());

					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료
				}
				break;
			case Const.PAYMENT_VENDOR_NAME_NAVER:
				NaverPaymentApprovalReturn naverResult = (NaverPaymentApprovalReturn) mapper
						.readValue(mapper.writeValueAsString(result.getData()), result.getData().getClass());

				NaverPaymentApprovalReturnBodyDetail naverpayDeatail = naverResult.getBody().getDetail();
				ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_NAVER);
				ocOrderPayment.setPymntAprvNoText(naverResult.getBody().getPaymentId()); // 네이버페이 거래고유번호
				ocOrderPayment.setPymntTodoAmt(naverpayDeatail.getTotalPayAmount().intValue()); // 결제예정금액
				ocOrderPayment.setPymntAmt(naverpayDeatail.getTotalPayAmount().intValue()); // 결제금액
				ocOrderPayment.setDpstPressEmailSendYn(Const.BOOLEAN_FALSE); // 입금독촉메일발송여부
				ocOrderPayment.setRspnsCodeText(naverResult.getCode());
				ocOrderPayment.setRspnsMesgText(naverResult.getMessage());

				if (UtilsText.equals(naverpayDeatail.getPrimaryPayMeans(), "CARD")) {
					ocOrderPayment.setPymntOrganCodeText(naverpayDeatail.getCardCorpCode());
					ocOrderPayment.setPymntOrganNoText(naverpayDeatail.getCardNo()); // 신용카드번호
					ocOrderPayment.setPymntOrganName(naverpayDeatail.getCardCorpCode());
					ocOrderPayment.setInstmtTermCount(naverpayDeatail.getCardInstCount().shortValue());// [신용카드]할부기간, 0
																										// :
				} else {
					ocOrderPayment.setPymntOrganCodeText(naverpayDeatail.getBankCorpCode());
					ocOrderPayment.setPymntOrganNoText(naverpayDeatail.getBankAccountNo()); // 신용카드번호
					ocOrderPayment.setPymntOrganName(naverpayDeatail.getBankCorpCode());
				}
				ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료
				break;
			case Const.PAYMENT_VENDOR_NAME_KAKAO:
				KakaoPaymentApprovalReturn kakaoResult = (KakaoPaymentApprovalReturn) mapper
						.readValue(mapper.writeValueAsString(result.getData()), result.getData().getClass());
				ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KAKAO);
				ocOrderPayment.setPymntAprvNoText(kakaoResult.getAid()); // 카카오 Request 고유 번호
				ocOrderPayment.setPymntOrganNoText(kakaoResult.getTid()); // 카카오 결제 고유 번호
				ocOrderPayment.setPymntOrganCodeText(kakaoResult.getPaymentMethodType()); // 카카오 결제 고유 번호
				ocOrderPayment.setPymntTodoAmt(kakaoResult.getAmount().getTotal()); // 결제예정금액
				ocOrderPayment.setPymntAmt(kakaoResult.getAmount().getTotal()); // 결제금액
				ocOrderPayment.setDpstPressEmailSendYn(Const.BOOLEAN_FALSE); // 입금독촉메일발송여부
				ocOrderPayment.setRspnsCodeText("0000"); // 0000
				ocOrderPayment.setRspnsMesgText("성공"); // 성공
				ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제 상태코드 : 결제완료
				break;
			}

			ocOrderPaymentDao.insertMainPayment(ocOrderPayment);
			// 주결제수단 저장 여부
			if (UtilsText.equals(payment.getSavePaymentYN(), Const.BOOLEAN_TRUE)) {
				MbMember member = new MbMember();
				member.setMemberNo(parameter.getOcOrder().getMemberNo());
				member.setPymntMeansCode(payment.getPymntMeansCode());
				memberService.setPaymentMeansCode(member);
			}
		} catch (Exception e) {
			resultMap.put("result", Const.RESULT_FAIL);
		}
		return resultMap;

	}

	private void completeOrderStatus(OrderSaveVo order) throws Exception {
		OcOrder ocOrder = order.getOcOrder();
		OrderPayment mainPayment = order.getMainPayment(); // 주결제수단
		// 주결제 수단이 무통장입금일경우 결제 대기 상태로 처리
		if (mainPayment != null
				&& UtilsText.equals(mainPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)) {
			// TODO : 주문 상태 update
			ocOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_STAND_BY);
			ocOrderDao.update(ocOrder);

			// TODO : 주문 상품 상태 update
			ocOrder.setOrderStatCode(CommonCode.ORDER_PRDT_STAT_CODE_STAND_BY);
			ocOrderProductDao.updateOrderStat(ocOrder);

			// TODO : 주문 상품 이력 상태 update

			// 무통장입금은 주문 배송 이력 상태를 update 하지 않음

		} else {
			// TODO : 주문 상태 update
			ocOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_COMPLETE);
			ocOrderDao.update(ocOrder);

			// TODO : 주문 상품 상태 update
			ocOrder.setOrderStatCode(CommonCode.ORDER_PRDT_STAT_CODE_COMPLETE);
			ocOrderProductDao.updateOrderStat(ocOrder);

			// TODO : 주문 상품 이력 상태 update

			// TODO : 주문 배송 이력 상태 update
			ocOrder.setOrderStatCode(CommonCode.DLVY_STAT_CODE_PAYMENT_FINISH);
			ocOrderDeliveryHistoryDao.updateOrderStat(ocOrder);

		}
	}

	/**
	 * @Desc : 상품 재고 update
	 * @Method Name : updateProductStock
	 * @Date : 2019. 5. 31.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderSaveVo
	 * @param resultMap
	 * @return
	 */
	private Map<String, Object> updateProductStock(OrderSaveVo orderSaveVo, Map<String, Object> resultMap) {
		List<OrderProduct> prdtList = orderSaveVo.getPrdtList();
		List<PdProductOptionStock> optionStockList = new ArrayList<PdProductOptionStock>();
		prdtList.addAll(orderSaveVo.getGiftPrdtList());

		Map<String, Map<String, Long>> prdtGroupList = prdtList.stream().collect(Collectors.groupingBy(
				OrderProduct::getPrdtNo, Collectors.groupingBy(OrderProduct::getPrdtOptnNo, Collectors.counting())));

//		prdtGroupList.forEach((prdtNo, value) -> value.forEach((prdtOptnNo, orderQty) -> optionStock = new PdProductOptionStock(), optionStock.setPrdtNo(prdtNo), optionStock.setPrdtOptnNo(prdtOptnNo), optionStock.setOrderCount(orderQty)));

		while (prdtGroupList.entrySet().iterator().hasNext()) {
			Map.Entry<String, Map<String, Long>> prdt = prdtGroupList.entrySet().iterator().next();
			Map<String, Long> optnEntry = prdt.getValue();
			while (optnEntry.entrySet().iterator().hasNext()) {
				Map.Entry<String, Long> optn = optnEntry.entrySet().iterator().next();
				PdProductOptionStock prdtOptnStock = new PdProductOptionStock();
				prdtOptnStock.setPrdtNo(prdt.getValue().toString());
				prdtOptnStock.setPrdtOptnNo(optn.getKey().toString());
				prdtOptnStock.setOrderCount(optn.getValue().intValue());
				optionStockList.add(prdtOptnStock);
			}
		}
		return resultMap;
	}

	/**
	 * @Desc : 카카오페이 인증
	 * @Method Name : kakaoAuth
	 * @Date : 2019. 6. 12.
	 * @Author : ljyoung@3top.co.kr
	 * @param authority
	 * @throws Exception
	 */
	public Map<String, Object> kakaoAuth(KakaoPaymentAuthority authority) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PaymentResult result = paymentEntrance
				.authority(new PaymentInfo(PaymentConst.PAYMENT_METHOD_TYPE_KAKAO, authority));

		KakaoPaymentAuthorityReturn reponse = (KakaoPaymentAuthorityReturn) result.getData();

		resultMap.put("nextRedirectPcUrl", reponse.getNextRedirectPcUrl());
		resultMap.put("nextRedirectMobileUrl", reponse.getNextRedirectMobileUrl());
		resultMap.put("nextRedirectAppUrl", reponse.getNextRedirectAppUrl());
		resultMap.put("tid", reponse.getTid());
		resultMap.put("nextRedirectPcUrl", reponse.getNextRedirectPcUrl());

		resultMap.put("authData", reponse);

		return resultMap;
	}

	/**
	 * @Desc : 상품 적립 포인트 계산
	 * @Method Name : getSavePoint
	 * @Date : 2019. 6. 14.
	 * @Author : ljyoung@3top.co.kr
	 * @param amt
	 * @return
	 * @throws Exception
	 */
	public double getSavePoint(Integer amt) throws Exception {
		// 적립 포인트 계산
		// (금액 * 적립율) / 1000
		double savePoint = (amt * 3 / 1000.0);
		// 올림(계산포인트) * 10 -- 포인트 올림 처리
		savePoint = Math.ceil(savePoint) * 10;
		return savePoint;
	}

	/*************************************************************************************************
	 * ljyoung end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * jeon start
	 *************************************************************************************************/

	/**
	 * 
	 * @Desc : 마이페이지 메인(주문/배송 현황 조회)
	 * @Method Name : getMyPageOrderInfo
	 * @Date : 2019. 5. 23.
	 * @Author : flychani@3top.co.kr
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMyPageOrderInfo(Pageable<OcOrder, OcOrder> pageable) throws Exception {
		Map<String, Object> orderMap = new HashMap<>();

		int totalCount = ocOrderDao.selectMyPageOrderInfoCount(pageable);

		if (totalCount > 0) {
			List<OcOrder> orderList = ocOrderDao.selectMyPageOrderList(pageable);

			OcOrderProduct orderProduct = new OcOrderProduct();

			for (int i = 0; i < orderList.size(); i++) {

				String orderAllConfirmYn = "N"; // 구매확정 여부
				String orderAllCancelYn = "N"; // 전체 취소 여부

				Map<String, Object> resultMap = this.orderStatActionValue(orderList.get(i).getOrderNo());

				// 구매확정 , 전체 취소 여부
				if (!resultMap.isEmpty()) {
					orderAllConfirmYn = resultMap.get("orderAllConfirmYn").toString();
					orderAllCancelYn = resultMap.get("orderAllCancelYn").toString();
				}

				orderProduct.setOrderPrdtStatCodeClick(pageable.getBean().getOrderPrdtStatCodeClick()); // 마이페이지 최근 주문내역
																										// 클릭여부
				orderProduct.setOrderNo(orderList.get(i).getOrderNo());
				orderProduct.setMemberNo(orderList.get(i).getMemberNo());
				orderList.get(i).setOcOrderProductList(ocOrderProductDao.selectOrderProductByOrderNo(orderProduct));
				orderList.get(i).setOcOrderProductListSize(orderList.get(i).getOcOrderProductList().size());
				orderList.get(i).setBuyDcsnYn(orderAllConfirmYn); // 구매확정 여부
				orderList.get(i).setOrderCancelAllYn(orderAllCancelYn); // 전체 취소 여부
				// 적립 포인트 계산
				long point = orderList.get(i).getOcOrderProductList().stream().mapToLong(o -> o.getPrdtPoint()).sum();
				orderList.get(i).setPrdtPoint(point);

			}

			orderMap.put("orderList", orderList);
		}
		orderMap.put("totalCount", totalCount);

		return orderMap;
	}

	/**
	 * 
	 * @Desc :마이페이지 주문 상세
	 * @Method Name : getOrderDetailInfo
	 * @Date : 2019. 5. 24.
	 * @Author : flychani@3top.co.kr
	 * @param orderParam
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrderDetailInfo(OcOrder orderParam) throws Exception {
		Map<String, Object> orderMap = new HashMap<>();

		OcOrder orderDtail = ocOrderDao.selectOrderDetail(orderParam); // 주문마스터 정보 조회

		OcOrderProduct orderProduct = new OcOrderProduct();
		orderProduct.setOrderNo(orderParam.getOrderNo());
		orderProduct.setMemberNo(orderDtail.getMemberNo());
		// 자사 배송비 기준 정보 조회
		SySite sysite = siteService.getSite(orderDtail.getSiteNo());

		// 상품정보
		List<OcOrderProduct> productList = ocOrderProductDao.selectOrderProductAllByOrder(orderProduct); // 주문상품 정보 조회

		// 적립 포인트 계산
		long point = productList.stream().mapToLong(o -> o.getPrdtPoint()).sum();
		orderDtail.setPrdtPoint(point);

		// 자사상품기준 -전체
		List<OcOrderProduct> productABCListAll = productList.stream().filter(x -> "Y".equals(x.getMmnyPrdtYn()))
				.collect(Collectors.toList());

		// 자사상품기준 - 배송비 사은품제외
		List<OcOrderProduct> productABCList = productList.stream().filter(x -> "Y".equals(x.getMmnyPrdtYn()))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) // 배송비 상품
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)) // 사은품
				.collect(Collectors.toList());

		long abcDeliveryCnt = productABCListAll.stream()
				.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)).count();

		if (abcDeliveryCnt > 0) {
			// 배송비 부과 기준 / 배송비 금액
			orderDtail.setFreeDlvyStdrAmt(sysite.getFreeDlvyStdrAmt());
			orderDtail.setStdrDlvyAmt(sysite.getDlvyAmt());
		}

		// 업체 상품
		List<OcOrderProduct> productVendorList = productList.stream().filter(x -> "N".equals(x.getMmnyPrdtYn()))
				.collect(Collectors.toList());

		// 업체 기준으로 중복제거
		List<String> vendorList = productVendorList.stream().map(OcOrderProduct::getVndrNo).distinct()
				.collect(Collectors.toCollection(ArrayList::new));

		List<OcOrderProduct> vendorGroupList = new ArrayList<OcOrderProduct>();

		for (int k = 0; k < vendorList.size(); k++) {

			OcOrderProduct ocOrderProduct = new OcOrderProduct();
			String vendorNo = vendorList.get(k);

			VdVendor param = new VdVendor();
			param.setVndrNo(vendorNo);
			VdVendor vdVendor = vendorService.getVendorInfo(param);

			ocOrderProduct.setVndrName(vdVendor.getVndrName());
			ocOrderProduct.setVndrNo(vendorNo);
			ocOrderProduct.setVendorProdList(productList.stream().filter(x -> vendorNo.equals(x.getVndrNo()))
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) // 배송비 상품
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)) // 사은품
					.collect(Collectors.toList()));

			// 배송비 상품 여부 체크
			long deliveryAmtCnt = productVendorList.stream()
					.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)
							&& vendorNo.equals(x.getVndrNo()))
					.count();

			if (deliveryAmtCnt > 0) {
				// 배송비 부과 기준 / 배송비 금액
				ocOrderProduct.setFreeDlvyStdrAmt(vdVendor.getFreeDlvyStdrAmt());
				ocOrderProduct.setDlvyAmt(Integer.toString(vdVendor.getDlvyAmt()));
			}

			/*
			 * // 사은품 상품 long giftProdCnt = productVendorList.stream() .filter(x ->
			 * UtilsText.equals(x.getPrdtTypeCode(), "10003") &&
			 * vendorNo.equals(x.getVndrNo())) .count(); if (giftProdCnt > 0) {
			 * ocOrderProduct.setGiftProdList(productList.stream() .filter(o ->
			 * vendorNo.equals(o.getVndrNo()) && UtilsText.equals(o.getPrdtTypeCode(),
			 * "10003")) .collect(Collectors.toList())); }
			 */

			log.debug(k + "::::::::vendorGroupList:::::" + ocOrderProduct);

			// 배송비 정책 여부
			vendorGroupList.add(ocOrderProduct);

		}
		String dlvyTypeCode = orderDtail.getDlvyTypeCode();

		OcOrderConvenienceStore cvsInfo = new OcOrderConvenienceStore();
		CmStore storeInfo = new CmStore();
		if (UtilsText.equals(dlvyTypeCode, CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP)) { // 편의점 정보 조회
			// OC_ORDER_CONVENIENCE_STORE
			OcOrderConvenienceStore cvs = new OcOrderConvenienceStore();
			cvs.setOrderNo(orderParam.getOrderNo());
			List<OcOrderConvenienceStore> cvsList = this.selectConvenienceStore(cvs);

			// 주문번호 기준 편의점 테이블에서 최대값으로 추출
			cvsInfo = cvsList.stream().sorted(Comparator.comparing(OcOrderConvenienceStore::getCvnstrSeq).reversed())
					.findFirst().orElse(null);
		} else if (UtilsText.equals(dlvyTypeCode, CommonCode.DLVY_TYPE_CODE_STORE_PICKUP)) { // 매장 상세 정보 조회 ?
			CmStore cmStore = new CmStore();
			// 주문 마스터의 상점번호 기준으로 조회함
			cmStore.setStoreNo(orderDtail.getStoreNo());
			storeInfo = storeService.getCmStoreDetail(cmStore);
		}

		OcOrderPayment orderPayment = new OcOrderPayment();
		orderPayment.setOrderNo(orderParam.getOrderNo());

		// 결제 정보 조회
		OcOrderPayment orderPaymentData = ocOrderPaymentDao.selectPaymentInfo(orderPayment);

		// 결제정보에 수단 상세 조회
		List<OcOrderPayment> orderPaymentInfo = ocOrderPaymentDao.selectPaymentDetail(orderPayment);

		// 결제 변경 이력 노출 리스트 조회
		List<OcOrderPayment> orderPaymentListHistory = ocOrderPaymentDao.selectPaymentListHistory(orderPayment);

		// 결제 수단 영수증 조회 데이타
		OcOrderPayment orderReceiptPayment = ocOrderPaymentDao.selectReceiptPayment(orderPayment);
		// 상품정보 리스트 조회시 상품할인 ,

		// 구매확정 전체 취소 여부 버튼 제어 S
		String orderAllConfirmYn = "N"; // 구매확정 여부
		String orderAllCancelYn = "N"; // 전체 취소 여부

		Map<String, Object> resultMap = this.orderStatActionValue(orderParam.getOrderNo());

		// 구매확정 , 전체 취소 여부
		if (!resultMap.isEmpty()) {
			orderAllConfirmYn = resultMap.get("orderAllConfirmYn").toString();
			orderAllCancelYn = resultMap.get("orderAllCancelYn").toString();
		}

		orderDtail.setBuyDcsnYn(orderAllConfirmYn); // 구매확정 여부
		orderDtail.setOrderCancelAllYn(orderAllCancelYn); // 전체 취소 여부
		// 구매확정 전체 취소 여부 버튼 제어 E

		// 주문 정보
		orderMap.put("orderParam", orderDtail);
		// 자사 상품 리스트
		orderMap.put("productABCList", productABCList);
		// 업체 상품 리스트
		orderMap.put("vendorGroupList", vendorGroupList);
		// 매장 정보
		orderMap.put("storeInfo", storeInfo);
		// 편의점 정보
		orderMap.put("cvsInfo", cvsInfo);
		// 결제 정보
		orderMap.put("orderPaymentData", orderPaymentData);
		// 결제 수단 리스트
		orderMap.put("orderPaymentInfo", orderPaymentInfo);
		// 결제 변경이력 리스트
		orderMap.put("orderPaymentListHistory", orderPaymentListHistory);
		// 결제 수단별 영수증 데이타 조회
		orderMap.put("orderReceiptPayment", orderReceiptPayment);

		return orderMap;
	}

	/**
	 * @Desc :
	 * @Method Name : selectConvenienceStore
	 * @Date : 2019. 5. 24.
	 * @Author : flychani@3top.co.kr
	 * @param cvs
	 * @return
	 */
	private List<OcOrderConvenienceStore> selectConvenienceStore(OcOrderConvenienceStore cvs) throws Exception {
		List<OcOrderConvenienceStore> cvsList = ocOrderConvenienceStoreDao.selectConvenienceStore(cvs);
		return cvsList;
	}

	/*************************************************************************************************
	 * jeon end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * leesh start
	 *************************************************************************************************/
	/**
	 * @Desc : 오프라인 주문내역 리스트
	 * @Method Name : getMyPageOrderOfflineInfo
	 * @Date : 2019. 5. 31.
	 * @Author : lee
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMyPageOrderOfflineInfo(Pageable<OcOrder, OcOrder> pageable) throws Exception {
		Map<String, Object> orderMap = new HashMap<>();

		int totalCount = ocOrderDao.selectMyPageOrderOfflineInfoCount(pageable);

		if (totalCount > 0) {
			List<OcOrder> orderList = ocOrderDao.selectMyPageOrderOfflineList(pageable);

			IfOffDealHistory ifOffDealHistory = new IfOffDealHistory();
			for (int i = 0; i < orderList.size(); i++) {

				ifOffDealHistory.setOrderNo(orderList.get(i).getOrderNo());
				ifOffDealHistory.setMemberNo(pageable.getBean().getMemberNo());
				orderList.get(i)
						.setIfOffDealHistoryList(ifOffDealHistoryDao.selectOfflineOrderProductList(ifOffDealHistory));
//				orderList.get(i).setOcOrderProductListSize(orderList.get(i).getOcOrderProductList().size());

			}

			orderMap.put("orderList", orderList);
		}
		orderMap.put("totalCount", totalCount);

		return orderMap;
	}

	public Map<String, Object> updateOfflineOrderBuyFix(IfOffDealHistory ifOffDealHistory) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// 매장 비회원 구매 사후적립 , 오프라인구매확정 포인트적립 [memB300a]
		String failCode = membershipPointService.updatePointAfterPurchase(ifOffDealHistory.getSafeKey(),
				ifOffDealHistory.getStoreCd(), ifOffDealHistory.getPosNo(), ifOffDealHistory.getDealNo(), 0,
				ifOffDealHistory.getSaleDate());

		int resultCnt = 0;
		if (UtilsText.isNotBlank(failCode)) {
			map.put("successYn", Const.BOOLEAN_FALSE);
			map.put("resultMsg", MemberShipErrorCode.getByCode(failCode).getDesc());
		} else {
			ifOffDealHistory.setConfFlag(Const.BOOLEAN_TRUE);
			resultCnt = ifOffDealHistoryDao.updateOfflineOrderBuyFix(ifOffDealHistory);
		}

		if (resultCnt > 0) {
			map.put("successYn", Const.BOOLEAN_TRUE);
		} else {
			map.put("successYn", Const.BOOLEAN_FALSE);
		}

		return map;
	}

	/**
	 * @Desc :배송지 변경
	 * @Method Name : updateDlvrAddrModify
	 * @Date : 2019. 6. 11.
	 * @Author : lee
	 * @param ocOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateDlvrAddrModify(OcOrder ocOrder) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// IF SEND
		String[] arrNum = getTelPhoneSpliter(ocOrder.getRcvrHdphnNoText());
		boolean returnFlag = false;
		try {
			OcOrderDeliveryHistory ocOrderDeliveryHistoryParamData = new OcOrderDeliveryHistory();
			ocOrderDeliveryHistoryParamData.setOrderNo(ocOrder.getOrderNo());

			List<OcOrderDeliveryHistory> ocOrderDeliveryHistory = ocOrderDeliveryHistoryDao
					.selectOcOrderDeliveryHistoryList(ocOrderDeliveryHistoryParamData);

			for (OcOrderDeliveryHistory ocOrderDeliveryHistory2 : ocOrderDeliveryHistory) {
				// IF SEND

				returnFlag = interfacesOrderService.updateOrderChangeAddrNoTrx("AI",
						ocOrderDeliveryHistory2.getDlvyIdText(), "null", "0072", ocOrder.getRcvrPostAddrText(),
						ocOrder.getRcvrDtlAddrText(), ocOrder.getRcvrPostCodeText(), arrNum[0], arrNum[1], arrNum[2],
						ocOrder.getRcvrName(), "", "", "");

				if (!returnFlag) {
					throw new Exception("updateOrderChangeOptnNoTrx Fail");
				}
			}
		} catch (Exception e) {
			map.put("successYn", Const.BOOLEAN_FALSE);
			map.put("errorMsg", "배송지 interface 실패" + e.getMessage());
			return map;
			// TODO: handle exception
		}

		int resultCnt = 0;

		// 주문 마스터 RCVR 업데이트
		resultCnt = ocOrderDao.updateOcOrderAddrModify(ocOrder);

		// 주문 배송이력 업데이트
		OcOrderDeliveryHistory ocOrderDeliveryHistoryParam = new OcOrderDeliveryHistory();
		ocOrderDeliveryHistoryParam.setOrderNo(ocOrder.getOrderNo());
		ocOrderDeliveryHistoryParam.setRcvrName(ocOrder.getRcvrName());
		ocOrderDeliveryHistoryParam.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());
		ocOrderDeliveryHistoryParam.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());
		ocOrderDeliveryHistoryParam.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());
		ocOrderDeliveryHistoryParam.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());
		ocOrderDeliveryHistoryParam.setModerNo(ocOrder.getModerNo());

		resultCnt = ocOrderDeliveryHistoryDao.updateOcOrderDeliveryHistoryModify(ocOrderDeliveryHistoryParam);

		if (resultCnt > 0) {
			map.put("successYn", Const.BOOLEAN_TRUE);
		} else {
			map.put("successYn", Const.BOOLEAN_FALSE);
		}

		return map;
	}

	public String[] getTelPhoneSpliter(String noStr) throws Exception {
		Pattern tellPattern = Pattern.compile("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");

		if (noStr == null)
			return new String[] { "", "", "" };

		Matcher matcher = tellPattern.matcher(noStr);
		if (matcher.matches()) {
			return new String[] { matcher.group(1), matcher.group(2), matcher.group(3) };
		} else {
			String str1 = noStr.substring(0, 3);
			String str2 = noStr.substring(3, 7);
			String str3 = noStr.substring(7, 11);
			return new String[] { str1, str2, str3 };
		}
	}

	public Map<String, Object> updateOrderPaymentChange(OcOrderPayment ocOrderPayment) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 현재의 order_no , order_pymnt_seq로 조회. 취소할떄 승인 번호
		OcOrderPayment oldOcOrderPaymentData = ocOrderPaymentDao.selectByPrimaryKey(ocOrderPayment);

		// 결제 에 필요한 정보를 받음.
		KcpPaymentApproval kcpPaymentApproval = ocOrderPayment.getKcpPaymentApproval();

		// 1. OC_ORDER_PAYMENT INSERT 후 시작
		OcOrderPayment ocOrderPaymentInsert = new OcOrderPayment();
		ocOrderPaymentInsert.setOrderNo(ocOrderPayment.getOrderNo());
		ocOrderPaymentInsert.setOrderPymntSeq(ocOrderPayment.getOrderPymntSeq());
		ocOrderPaymentInsert.setDeviceCode(oldOcOrderPaymentData.getDeviceCode());
		ocOrderPaymentInsert.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // 주결제수단여부
		if (UtilsText.equals(ocOrderPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			ocOrderPaymentInsert.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_CARD); // 결제수단코드-신용카드
		} else {
			ocOrderPaymentInsert.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER); // 결제수단코드-실시간 계좌이체

		}
		ocOrderPaymentInsert.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP); // 결제업체코드
		ocOrderPaymentInsert.setPymntOrganCodeText(null); // 결제기관코드
		ocOrderPaymentInsert.setPymntOrganNoText(null); // 결제기관번호(카드번호, 은행계좌번호)
		ocOrderPaymentInsert.setPymntOrganName(null); // 결제기관명
		ocOrderPaymentInsert.setInstmtTermCount((short) 0); // 할부기간
		ocOrderPaymentInsert.setPymntAprvNoText(null); // 결제승인번호
		ocOrderPaymentInsert.setPymntTodoAmt(ocOrderPayment.getPymntAmt()); // 결제예정금액
		ocOrderPaymentInsert.setPymntAmt(ocOrderPayment.getPymntAmt()); // 결제금액
		ocOrderPaymentInsert.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부
		ocOrderPaymentInsert.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부 리턴값으로 UPDATE
		ocOrderPaymentInsert.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부
		ocOrderPaymentInsert.setPymntMeansChngPsbltYn(Const.BOOLEAN_FALSE); // 결제수단변경가능여부
		ocOrderPaymentInsert.setEscrAprvNoText(null); // 에스크로승인번호
		ocOrderPaymentInsert.setRspnsCodeText(null); // 응답코드
		ocOrderPaymentInsert.setRspnsMesgText(null); // 응답메시지
		ocOrderPaymentInsert.setPymntStatCode(null); // 결제상태코드
		ocOrderPaymentInsert.setPymntLogInfo(null); // 결제로그
		ocOrderPaymentInsert.setRgsterNo(ocOrderPayment.getRgsterNo()); // 등록자

		ocOrderPaymentDao.insertPaymentChange(ocOrderPaymentInsert);

		kcpPaymentApproval.setSiteCd(Config.getString("pg.kcp.siteCode"));
		kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.siteKey"));
		PaymentResult paymentResult = paymentEntrance
				.approval(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentApproval)); // KCP 결제

		// 결제 실패인 경우 exception
		if (UtilsText.equals(paymentResult.getSuccessYn(), Const.BOOLEAN_FALSE)) {
			throw new Exception(paymentResult.getMessage() + "step:3");
		}

		KcpPaymentApprovalReturn kcpPaymentApprovalReturn = ((KcpPaymentApprovalReturn) paymentResult.getData());
		log.info("#################### :: " + paymentResult.toString());
		log.info("#################### :: " + kcpPaymentApprovalReturn.toString());

		// 결제 성공이면 OC_AS_PAYMENT 업데이트
		OcOrderPayment ocOrderPaymentResult = new OcOrderPayment();
		if (UtilsText.equals(ocOrderPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			ocOrderPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getCardCd());
			ocOrderPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getCardName());
			ocOrderPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getCardNo());
			ocOrderPaymentResult.setInstmtTermCount(Short.valueOf(kcpPaymentApprovalReturn.getQuota()));
			// [신용카드]카드 구분정보 (법인카드 여부), 0 : 개인, 1 : 법인
			if ("0".equals(kcpPaymentApprovalReturn.getCardBinType01())) {
				ocOrderPaymentResult.setCardType(CommonCode.CARD_TYPE_NORMAL);
			} else {
				ocOrderPaymentResult.setCardType(CommonCode.CARD_TYPE_CORPORATE);
			}
			// [신용카드]카드 구분정보(체크카드 여부), 0 : 일반, 1 : 체크
			if ("0".equals(kcpPaymentApprovalReturn.getCardBinType02())) {
				ocOrderPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_NORMAL);
			} else {
				ocOrderPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_CHECK);
			}

			ocOrderPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
			ocOrderPaymentResult.setCashRcptAprvNo(UtilsText.EMPTY);
			ocOrderPaymentResult.setCashRcptDealNo(UtilsText.EMPTY);
		} else {
			ocOrderPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getBankCode()); // 결제기관코드(카드, 은행코드...)
			ocOrderPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getBankName()); // 결제기관명(카드, 은행명...)
			ocOrderPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getAccount()); // 결제기관번호(카드번호, 은행계좌번호...)
			ocOrderPaymentResult.setInstmtTermCount(Short.valueOf("0"));
			if (kcpPaymentApprovalReturn.getCashAuthno() != null && kcpPaymentApprovalReturn.getCashAuthno() != "") {
				ocOrderPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_TRUE);
				ocOrderPaymentResult.setCashRcptAprvNo(kcpPaymentApprovalReturn.getCashAuthno());
				ocOrderPaymentResult.setCashRcptDealNo(kcpPaymentApprovalReturn.getCashNo());
			} else {
				ocOrderPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
			}
			ocOrderPaymentResult.setEscrApplyYn(kcpPaymentApprovalReturn.getEscwYn()); // 에스크로 응답 결과 값
		}
		ocOrderPaymentResult.setPymntAprvNoText(kcpPaymentApprovalReturn.getTno()); // 결제승인번호
		ocOrderPaymentResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제상태코드
		ocOrderPaymentResult.setRspnsCodeText(kcpPaymentApprovalReturn.getResCd()); // 응답코드
		ocOrderPaymentResult.setRspnsMesgText(kcpPaymentApprovalReturn.getResMsg()); // 응답메시지
		ocOrderPaymentResult.setPymntLogInfo(new ObjectMapper().writeValueAsString(kcpPaymentApprovalReturn)); // 결제로그
		ocOrderPaymentResult.setModerNo(ocOrderPayment.getRgsterNo()); // 수정자
		ocOrderPaymentResult.setOrderNo(ocOrderPaymentInsert.getOrderNo());
		ocOrderPaymentResult.setOrderPymntSeq(ocOrderPaymentInsert.getOrderPymntSeq());

		try {
			// 결제수단 변경
			int newPaymntCnt = ocOrderPaymentDao.updateOcOrderPaymentAccount(ocOrderPaymentResult);
			// 결제 승인 후 업뎅트 건수 0이면 실패로 간주함.
			if (newPaymntCnt == 0) {
				throw new Exception("결제 승인후 업데이트 건수가 존재하지 않습니다. step : 4");
			}
		} catch (Exception e) {
			// 결제수단 변경 실패할 경우 kcp card 요청 취소
			KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
			kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
			kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));
			kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
			kcpPaymentCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP 원거래 거래번호
			kcpPaymentCancel.setCustIp(kcpPaymentApproval.getCustIp()); // 변경 요청자 IP
			kcpPaymentCancel.setModDesc("결제수단 변경  실패"); // 취소 사유

			PaymentResult result = paymentEntrance
					.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
			KcpPaymentCancelReturn kcpPaymentCancelReturn = ((KcpPaymentCancelReturn) result.getData());

			// 결제 실패한 로그
			ocOrderPaymentResult.setOrderNo(ocOrderPaymentInsert.getOrderNo());
			ocOrderPaymentResult.setOrderPymntSeq(ocOrderPaymentInsert.getOrderPymntSeq());
			ocOrderPaymentResult.setRspnsCodeText(kcpPaymentCancelReturn.getResCd()); // 응답코드
			ocOrderPaymentResult.setRspnsMesgText(kcpPaymentCancelReturn.getResMsg()); // 응답메시지
			ocOrderPaymentResult.setPymntLogInfo(
					new ObjectMapper().writeValueAsString(kcpPaymentCancelReturn + "::결제실패 데이타 ::" + result.getData()));
			ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentResult);

			throw new Exception(e.getMessage() + "step::5  결제수단 변경 실패 ");
		}

		// 기존 결제 에 대한 UPDATE OC_ORDER_PAYMENT후 취소 전문
		OcOrderPayment ocOrderPaymentCancel = new OcOrderPayment();
		ocOrderPaymentCancel.setOrderNo(oldOcOrderPaymentData.getOrderNo());
		ocOrderPaymentCancel.setOrderPymntSeq(oldOcOrderPaymentData.getOrderPymntSeq());
		ocOrderPaymentCancel.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL);
		ocOrderPaymentCancel.setModerNo(ocOrderPayment.getRgsterNo()); // 수정자

		try {
			// 결제수단 변경 수정(kcp 리턴 값 사용)
			int resultPayOldCnt = ocOrderPaymentDao.updateOcOrderOldPaymentCancel(ocOrderPaymentCancel);

			// 기존 결제에 대한 업데이트 성공시 취소 전문 보냄.
			if (resultPayOldCnt > 0) {
				KcpPaymentCancel kcpPaymentOldCancel = new KcpPaymentCancel();
				kcpPaymentOldCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
				kcpPaymentOldCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));
				kcpPaymentOldCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
				kcpPaymentOldCancel.setTno(oldOcOrderPaymentData.getPymntAprvNoText()); // 기존 거래번호
				kcpPaymentOldCancel.setCustIp(kcpPaymentApproval.getCustIp()); // 변경 요청자 IP
				kcpPaymentOldCancel.setModDesc("결제수단 변경으로 인한 취소"); // 취소 사유

				PaymentResult result = paymentEntrance
						.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentOldCancel));
				KcpPaymentCancelReturn kcpPaymentOldCancelReturn = ((KcpPaymentCancelReturn) result.getData());

				if (UtilsText.equals(result.getSuccessYn(), Const.BOOLEAN_FALSE)) {
					ocOrderPaymentCancel.setOrderNo(oldOcOrderPaymentData.getOrderNo());
					ocOrderPaymentCancel.setOrderPymntSeq(oldOcOrderPaymentData.getOrderPymntSeq());
					ocOrderPaymentCancel.setRspnsCodeText(kcpPaymentOldCancelReturn.getResCd()); // 응답코드
					ocOrderPaymentCancel.setRspnsMesgText(kcpPaymentOldCancelReturn.getResMsg()); // 응답메시지
					ocOrderPaymentCancel.setPymntLogInfo(new ObjectMapper()
							.writeValueAsString(kcpPaymentOldCancelReturn + "::결제실패 데이타 ::" + result.getData()));
					ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentCancel);

					throw new Exception("기 주문 취소 실패 ");
				} else {
					ocOrderPaymentCancel.setOrderNo(oldOcOrderPaymentData.getOrderNo());
					ocOrderPaymentCancel.setOrderPymntSeq(oldOcOrderPaymentData.getOrderPymntSeq());
					ocOrderPaymentCancel.setRspnsCodeText(kcpPaymentOldCancelReturn.getResCd()); // 응답코드
					ocOrderPaymentCancel.setRspnsMesgText(kcpPaymentOldCancelReturn.getResMsg()); // 응답메시지
					ocOrderPaymentCancel.setPymntLogInfo(
							new ObjectMapper().writeValueAsString(kcpPaymentOldCancelReturn + (result.getMessage())));
					ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentCancel);
				}

			} else {
				// 업데이트 0건 실패로 간주함.
				throw new Exception("기존 업데이트 건수가 존재하지 않습니다. step::6 ");
			}
		} catch (Exception e) {
			// 기존 결제수단 변경 실패할 경우 신규 결제 kcp 요청 취소
			KcpPaymentCancel kcpPaymentNewCancel = new KcpPaymentCancel();
			kcpPaymentNewCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
			kcpPaymentNewCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));
			kcpPaymentNewCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
			kcpPaymentNewCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP 원거래 거래번호
			kcpPaymentNewCancel.setCustIp(kcpPaymentApproval.getCustIp()); // 변경 요청자 IP
			kcpPaymentNewCancel.setModDesc("기존 결제 수단 변경  실패로 인한 실패"); // 취소 사유

			// 익셉션 발생하면 결제한 금액 취소
//			paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentNewCancel));

			PaymentResult result = paymentEntrance
					.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentNewCancel));
			KcpPaymentCancelReturn kcpPaymentNewCancelReturn = ((KcpPaymentCancelReturn) result.getData());

			// 결제 실패한 로그
			ocOrderPaymentResult.setOrderNo(ocOrderPaymentInsert.getOrderNo());
			ocOrderPaymentResult.setOrderPymntSeq(ocOrderPaymentInsert.getOrderPymntSeq());
			ocOrderPaymentResult.setRspnsCodeText(kcpPaymentNewCancelReturn.getResCd()); // 응답코드
			ocOrderPaymentResult.setRspnsMesgText(kcpPaymentNewCancelReturn.getResMsg()); // 응답메시지
			ocOrderPaymentResult.setPymntLogInfo(new ObjectMapper()
					.writeValueAsString(kcpPaymentNewCancelReturn + "::결제실패 데이타 ::" + result.getData()));
			ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentResult);

			throw new Exception(e.getMessage() + "step::7 신규 결제 수단 취소");
		}

		map.put("successYn", Const.BOOLEAN_TRUE);
		map.put("step", "0");
		return map;
	}

	public Map<String, Object> updateOrdPrdOptionChange(OcOrderProduct ocOrderProduct) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// 현재의 order_no , order_prdt_seq로 배송 히스토리 조회
		OcOrderDeliveryHistory ocOrderDeliveryHistoryParam = new OcOrderDeliveryHistory();

		ocOrderDeliveryHistoryParam.setOrderNo(ocOrderProduct.getOrderNo());
		ocOrderDeliveryHistoryParam.setOrderPrdtSeq(ocOrderProduct.getOrderPrdtSeq());

		OcOrderDeliveryHistory ocOrderDeliveryHistoryData = ocOrderDeliveryHistoryDao
				.selectOcOrderDeliveryHistory(ocOrderDeliveryHistoryParam);

		if (ocOrderDeliveryHistoryData == null) {
			throw new Exception("배송 히스토리 데이타가 없습니다. : step : 1");

		}
		// 변경전 상품 조회
		OcOrderProduct ocOrderProductOldData = ocOrderProductDao.selectByPrimaryKey(ocOrderProduct);

		if (ocOrderProductOldData == null) {
			throw new Exception("기존 데이타가 없습니다. : step : 2");
		}

		// OC_ORDER_PRODUCT UPDATE
		OcOrderProduct ocOrderProductUpdata = new OcOrderProduct();
		ocOrderProductUpdata.setOrderNo(ocOrderProduct.getOrderNo());
		ocOrderProductUpdata.setOrderPrdtSeq(ocOrderProduct.getOrderPrdtSeq());
		ocOrderProductUpdata.setPrdtOptnNo(ocOrderProduct.getPrdtOptnNo());
		ocOrderProductUpdata.setOptnName(ocOrderProduct.getOptnName());
//		ocOrderProductUpdata.setVndrPrdtNoText(ocOrderProduct.getVndrPrdtNoText());
		ocOrderProductUpdata.setModerNo(ocOrderProduct.getModerNo());

		int updateCnt = ocOrderProductDao.updateOrderProductOptionChange(ocOrderProductUpdata);

		if (updateCnt == 0) {
			throw new Exception("주문 상품 update 실패 step : 3");
		}

		// 히스토리 ROW INSERT
		OcOrderProductHistory prdtHistory = new OcOrderProductHistory();
		prdtHistory.setOrderNo(ocOrderProduct.getOrderNo());
		prdtHistory.setOrderPrdtSeq(ocOrderProduct.getOrderPrdtSeq());
		prdtHistory.setPrdtNo(ocOrderProductOldData.getPrdtNo());
		prdtHistory.setPrdtOptnNo(ocOrderProduct.getPrdtOptnNo());
		prdtHistory.setPrdtName(ocOrderProductOldData.getPrdtName());
		prdtHistory.setEngPrdtName(ocOrderProductOldData.getEngPrdtName());
		prdtHistory.setOptnName(ocOrderProduct.getOptnName());
		prdtHistory.setOrderPrdtStatCode(ocOrderProductOldData.getOrderPrdtStatCode()); // 변경전 상태 코드 그대로 진행.
		prdtHistory.setNoteText("옵션 변경"); // NOTE_TEXT
		prdtHistory.setRgsterNo(ocOrderProduct.getModerNo());

		int insertCnt = ocOrderProductHistoryDao.insertProductHistory(prdtHistory);

		if (insertCnt == 0) {
			throw new Exception("주문 상품 히스토리 insert 실패 step : 4 ");
		}

		if ("Y".equals(ocOrderDeliveryHistoryData.getWmsSendYn())) {

			boolean isSucc = interfacesOrderService.updateOrderChangeOptnNoTrx("AI",
					ocOrderDeliveryHistoryData.getDlvyIdText(), String.valueOf(ocOrderProduct.getOrderPrdtSeq()),
					ocOrderProductOldData.getInsdMgmtInfoText(),
					ocOrderProductOldData.getVndrPrdtNoText() + "001" + ocOrderProductOldData.getPrdtOptnNo(),
					ocOrderProductOldData.getVndrPrdtNoText() + "001" + ocOrderProduct.getPrdtOptnNo());

			if (!isSucc) {
				throw new Exception("interFace 실패 staep : 5");
			}
		}

		map.put("successYn", Const.BOOLEAN_TRUE);
		map.put("step", "0");
		return map;
	}

	/*************************************************************************************************
	 * leesh end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * nbk s
	 *************************************************************************************************/
	public Map<String, Object> orderStatActionValue(String orderNo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		if (orderNo != null && !orderNo.isEmpty()) {
			OcOrder ocOrder = new OcOrder();
			// 주문번호 별 Count체크

			ocOrder.setOrderNo(orderNo);
			OrderStatVO orderStatParam = ocOrderDao.selectOrderStatCount(ocOrder);

			int orderTotalCnt = orderStatParam.getOrderTotalCnt(); // 전체 주문상품수
			int orderConfirmCnt = orderStatParam.getOrderConfirmCnt(); // 전체 배송완료 건수
			int orderCancelCnt = orderStatParam.getOrderCancelCnt(); // 전체 취소 건수
			int orderReadlyCnt = orderStatParam.getOrderReadlyCnt(); // 결제완료,입금대기 건수
			int orderClaimCnt = orderStatParam.getOrderClaimTotalCnt(); // 클레임 건수

			// 구매확정은 배송완료 건수와 취소 완료 건수가 동일 한경우 구매 확정 가능
			if (orderTotalCnt == (orderConfirmCnt + orderCancelCnt)) {
				map.put("orderAllConfirmYn", "Y"); // 구매확정 가능
			} else {
				map.put("orderAllConfirmYn", "N");
			}

			// 전체 상품 수와 결제완료(입금대기 포함)건수가 동일한경우만 전체 취소 가능
			if ((orderTotalCnt == orderReadlyCnt) && orderClaimCnt == 0) {
				map.put("orderAllCancelYn", "Y"); // 전체 취소 가능
			} else {
				map.put("orderAllCancelYn", "N");
			}

		}

		return map;

	}

	/**
	 * 
	 * @Desc : 구매확정 처리 - I/F포함
	 * @Method Name : updateOrderConfirm
	 * @Date : 2019. 6. 12.
	 * @Author : NKB
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateOrderConfirm(OcOrder ocOrder) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String rtnValue = Const.BOOLEAN_FALSE;

		// 주문정보
		OcOrder ocOrderDetail = ocOrderDao.selectOrderDetail(ocOrder);

		// 회원 정보 조회
		MbMember mbMemberParam = new MbMember();
		mbMemberParam.setMemberNo(ocOrderDetail.getMemberNo());
		MbMember mbMember = memberService.selectMemberInfo(mbMemberParam);

		// 상품정보
		OcOrderProduct ocOrderProduct = new OcOrderProduct();
		ocOrderProduct.setOrderNo(ocOrder.getOrderNo());
		List<OcOrderProduct> productList = ocOrderProductDao.selectOrderProductByOrderNo(ocOrderProduct); // 주문상품 정보 조회

		Map<String, Object> resultMap = this.orderStatActionValue(ocOrder.getOrderNo());

		// 구매확정 여부
		String orderAllConfirmYn = "N";

		// 구매확정 체크
		if (!resultMap.isEmpty()) {
			orderAllConfirmYn = resultMap.get("orderAllConfirmYn").toString();

			// 구매확정 불가인경우
			if (StringUtils.equals(orderAllConfirmYn, "N")) {
				throw new Exception("구매확정 불가 상태");
			}
		}

		ocOrderProduct.setModerNo(ocOrder.getModerNo()); // 수정자번호
		ocOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_BUY_CONFIRM); // 구매확정
		ocOrderProduct.setWhereOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_DELIVERY_FINISH); // 배송완료

		try {

			// 상품 상테변경
			int prdUpdateCnt = ocOrderProductDao.updateOrderConfirm(ocOrderProduct);

			if (prdUpdateCnt > 0) {

				OcOrderDeliveryHistory ocOrderDeliveryHistory = new OcOrderDeliveryHistory();
				ocOrderDeliveryHistory.setOrderNo(ocOrder.getOrderNo());
				ocOrderDeliveryHistory.setModerNo(ocOrder.getModerNo()); //
				ocOrderDeliveryHistory.setDlvyStatCode(CommonCode.DLVY_STAT_CODE_BUY_CONFIRM); // 구매확정
				ocOrderDeliveryHistory.setWhereDlvyStatCode(CommonCode.DLVY_STAT_CODE_MISS_DELIVERY); // 배송완료

				// 배송 상테변경
				int dlyUpdateCnt = ocOrderDeliveryHistoryDao.updateOrderConfirm(ocOrderDeliveryHistory);

				// 주문 마스터 일자 변경
				int mstCnt = ocOrderDao.updateOrderDcsnDtm(ocOrder);

				if (dlyUpdateCnt > 0) {
					// 상품 이력 저장
					for (OcOrderProduct param : productList) {

						if (CommonCode.ORDER_PRDT_STAT_CODE_DELIVERY_FINISH.equals(param.getOrderPrdtStatCode())) {

							OcOrderProductHistory prdtHistory = new OcOrderProductHistory();
							prdtHistory.setOrderNo(param.getOrderNo());
							prdtHistory.setOrderPrdtSeq(param.getOrderPrdtSeq());
							prdtHistory.setPrdtNo(param.getPrdtNo());
							prdtHistory.setPrdtOptnNo(param.getPrdtOptnNo());
							prdtHistory.setPrdtName(param.getPrdtName());
							prdtHistory.setEngPrdtName(param.getEngPrdtName());
							prdtHistory.setOptnName(param.getOptnName());
							prdtHistory.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_BUY_CONFIRM); // 구매확정
							prdtHistory.setNoteText("구매확정 변경"); // NOTE_TEXT
							prdtHistory.setRgsterNo(ocOrder.getModerNo());

							ocOrderProductHistoryDao.insertProductHistory(prdtHistory);
						}
					} // end for
				}
			}

			// 구매확정시 포인트지급 [memA890a]
			List<BuyFixProduct> buyFixProduct = membershipPointService.buyFixRequest(mbMember.getSafeKey(),
					ocOrderDetail.getOrderNo());

			/*
			 * 구매 확정 메일
			 */

			/*
			 * 구매확정 SMS
			 */

			rtnValue = Const.BOOLEAN_TRUE;

		} catch (Exception e) {
			rtnValue = Const.BOOLEAN_FALSE;
			map.put("successYn", rtnValue);
			map.put("message", "구매확정 상태변경 실패" + e.getMessage());
			return map;

		}

		map.put("successYn", rtnValue);
		return map;
	}

	/*************************************************************************************************
	 * nbk end
	 *************************************************************************************************/

}
