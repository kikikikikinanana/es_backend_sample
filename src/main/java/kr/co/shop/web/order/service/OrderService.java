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
 * @Desc : ?????? ?????? ?????? service
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
	 * @Desc : ???????????? ?????? ?????? , yyyyMMdd + 5??????Seq(5????????? ???????????? Seq ????????? ZeroFill)
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
	 * @Desc : ?????? ??????????????????
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

		// ?????? ??? ?????? ?????? Summery
		validateProducts = validateProducts.stream()
				// ?????? ?????? ListAgg
				.peek(o1 -> o1.setPrdtOptnNo(ocOrderProductList.stream()
						.filter(o2 -> StringUtils.equals(o2.getPrdtNoPrdtOptnNo(), o1.getPrdtNoPrdtOptnNo()))
						.map(OcOrderProduct::getPrdtOptnNo).distinct().collect(Collectors.joining())))
				// ?????? ?????? ( ?????? ?????? ?????? )
				.peek(o1 -> o1.setSumStockQty(ocOrderProductList.stream()
						.filter(o2 -> StringUtils.equals(o2.getPrdtNoPrdtOptnNo(), o1.getPrdtNoPrdtOptnNo()))
						.mapToInt(OcOrderProduct::getOrderQty).sum()))
				.distinct().collect(Collectors.toList());

		// ?????????????????????
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

				int sumQty = param.getSumStockQty(); // ????????????

				int aiQty = param.getAiQty(); // AI ?????? ??????
				int awQty = param.getAwQty(); // AW ?????? ??????
				int asQty = param.getAsQty(); // AS ?????? ??????

				// ????????? ??????
				if (aiQty - sumQty > 0) {
					// ????????? ?????? ??????.
					PdProductOptionStock prdtStock = new PdProductOptionStock();
					prdtStock.setOrderCount(param.getSumStockQty());
					prdtStock.setPrdtNo(param.getPrdtNo());
					prdtStock.setPrdtOptnNo(param.getPrdtOptnNo());
					prdtStock.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // ????????? ?????? 10000

					ocOrderProductDao.updateProductStock(prdtStock);
				} else {
					// ????????? ???????????? ????????? AiQty?????? ?????? ??? ???????????? aw?????? ??????
					if (aiQty > 0) {
						int newAwQty = sumQty - aiQty;
						// ????????? ?????? ??????.
						PdProductOptionStock prdtStock = new PdProductOptionStock();
						prdtStock.setOrderCount(aiQty);
						prdtStock.setPrdtNo(param.getPrdtNo());
						prdtStock.setPrdtOptnNo(param.getPrdtOptnNo());
						prdtStock.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // ????????? ?????? 10000

						ocOrderProductDao.updateProductStock(prdtStock);

						// ?????? ????????? ????????? aw??? ????????? ??????
						if (awQty - newAwQty > 0) {

							// ????????? ?????? ??????.
							PdProductOptionStock prdtStockAw = new PdProductOptionStock();
							prdtStockAw.setOrderCount(newAwQty);
							prdtStockAw.setPrdtNo(param.getPrdtNo());
							prdtStockAw.setPrdtOptnNo(param.getPrdtOptnNo());
							prdtStockAw.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AW); // ????????? ?????? 10001

							ocOrderProductDao.updateProductStock(prdtStockAw);

						} else {
							// aw?????? ????????? as?????? ??????

							// ????????? ?????? ??????.
							PdProductOptionStock prdtStockAs = new PdProductOptionStock();
							prdtStockAs.setOrderCount(newAwQty);
							prdtStockAs.setPrdtNo(param.getPrdtNo());
							prdtStockAs.setPrdtOptnNo(param.getPrdtOptnNo());
							prdtStockAs.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AS); // ???????????? ?????? 10002

							ocOrderProductDao.updateProductStock(prdtStockAs);
						}
					} else {
						// ????????? aw?????? ?????? ??????.
						PdProductOptionStock prdtStockAs = new PdProductOptionStock();
						prdtStockAs.setOrderCount(sumQty);
						prdtStockAs.setPrdtNo(param.getPrdtNo());
						prdtStockAs.setPrdtOptnNo(param.getPrdtOptnNo());
						prdtStockAs.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AS); // ???????????? ?????? 10002

						ocOrderProductDao.updateProductStock(prdtStockAs);
					}
				}
			}
		}
		return true;
	}

	/**
	 * @Desc :???????????? update (List)
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
	 * @Desc : ???????????? update
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
	 * @Desc : ?????? ??????????????? ??????
	 * @Method Name : getDlvyAddrList
	 * @Date : 2019. 4. 1.
	 * @Author : ?????????
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
	 * @Desc : ??????????????? ?????? ???????????? ??????
	 * @Method Name : selectClaimRequestOrderList
	 * @Date : 2019. 4. 16.
	 * @Author : ?????????
	 * @param Parameter<OcOrder>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClaimRequestOrderList(Parameter<OcOrder> parameter) throws Exception {

		Pageable<OcOrder, OcOrder> pageable = new Pageable<>(parameter);

		// ????????? ??????
		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> map = new HashMap<>();

		int totalCount = ocOrderDao.selectClaimRequestOrderListCount(pageable);

		// MO ????????? ?????????
		if (totalCount < 1) {
			map.put("noData", true);
		}

		if (totalCount > 0) {

			List<OcOrder> orderList = ocOrderDao.selectClaimRequestOrderList(pageable);

			// ?????????, ????????? ??????
			String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };

			for (OcOrder ocOrder : orderList) {

				OcOrder param = new OcOrder();
				// ???????????? ?????? set
				// ????????????
				param.setOrderNo(ocOrder.getOrderNo());
				param.setOrderPrdtStatCodeList(pageable.getBean().getOrderPrdtStatCodeList());
				// ???????????????????????? 1 (??????)
				param.setPrdtRltnFileSeq(1);

				// ?????????, ????????? ??????
				param.setPrdtTypeCodeList(prdtTypeCodeList);

				// ???????????????????????? : ????????????(??????) ????????? ??????
				if (pageable.getBean().getOrderStatCode() != null) {
					param.setOrderPrdtStatCode(pageable.getBean().getOrderStatCode());
				}

				List<OcOrderProduct> orderProductList = ocOrderProductDao.selectClaimRequestOrderProductList(param);

				int orderAmtSum = orderProductList.stream().mapToInt(o -> o.getOrderAmt()).sum();
				ocOrder.setStrPymntAmt(String.valueOf(orderAmtSum)); // ?????? ????????? ??????????????? sum ?????? ??????

				// ?????? ??????????????? ?????????????????? ??????
				if (!UtilsArray.isEmpty(orderProductList)) {

					ocOrder.setOcOrderProductList(orderProductList);
					ocOrder.setOcOrderProductListSize(orderProductList.size());
				}
			}

			map.put("content", orderList);

			// MO ?????????
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
	 * @Desc : ????????? ?????? ?????? ?????? ??????
	 * @Method Name : getClaimRequestOrderDetail
	 * @Date : 2019. 4. 23.
	 * @Author : ?????????, KTH
	 * @param OcOrder
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClaimRequestOrderDetail(OcOrder ocOrder) throws Exception {

		Map<String, Object> map = new HashMap<>();

		// ????????? ????????? ??????
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(ocOrder.getLoginMemberNo());
		map.put("memberInfo", memberService.selectMemberInfo(mbMember));

		// ?????????, ????????? ??????
		String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };
		ocOrder.setPrdtTypeCodeList(prdtTypeCodeList);

		// ????????? ?????? ?????? ??????
		OcOrder orderInfo = ocOrderDao.selectByPrimaryKey(ocOrder);

		if (orderInfo != null) {
			map.put("orderInfo", orderInfo);
		}

		// ??? ?????? ?????? ?????? ??????
		OcOrderProduct orgOrderProduct = new OcOrderProduct();
		orgOrderProduct.setOrderNo(ocOrder.getOrderNo());
		List<OcOrderProduct> orgOrderProductList = ocOrderProductDao.selectOrgOrderProductList(orgOrderProduct);

		map.put("orgOrderProductList",
				orgOrderProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.collect(Collectors.toList()));

		// ????????? ?????? ??????
		SySite sySite = sySiteDao.selectSite(orderInfo.getSiteNo());
		map.put("siteInfo", sySite);

		// ??????????????? ?????? ????????? ??????????????? ??????
		ocOrder.setPrdtTypeCode(CommonCode.PRDT_TYPE_CODE_DELIVERY); // ?????????
		ocOrder.setClmPrdtStatCodes(new String[] { CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REDEPTION_ACCEPT,
				CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REFUND_ACCEPT, CommonCode.CLM_PRDT_STAT_CODE_CANCEL_FINISH,
				CommonCode.CLM_PRDT_STAT_CODE_RETURN_REDEPTION_ACCEPT,
				CommonCode.CLM_PRDT_STAT_CODE_RETURN_REFUND_ACCEPT, CommonCode.CLM_PRDT_STAT_CODE_RETURN_FINISH,
				CommonCode.CLM_PRDT_STAT_CODE_RETURN_IMPOSSIBLE }); // ??????, ?????? ??????

		List<OcOrder> vndrOrderList = ocOrderProductDao.selectVndrNoGroupByOrderNo(ocOrder); // ???????????? ??????????????? ????????????(??????,????????????)

		int mmnyDlvyAmt = 0; // ?????? ???????????????
		int mmnyCanceledDlvyAmt = 0; // ?????? ??? ????????? ???????????????
		long mmnyVendorCnt = vndrOrderList.stream().filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE))
				.count(); // ??????

		if (mmnyVendorCnt > 0) { // ?????? ????????? ?????? ??????
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

				// ???????????? ?????? set
				vndrOrder.setOrderNo(ocOrder.getOrderNo());
				vndrOrder.setOrderPrdtStatCodeList(ocOrder.getOrderPrdtStatCodeList());
				// ?????????, ????????? ??????
				vndrOrder.setPrdtTypeCodeList(prdtTypeCodeList);
				// ???????????????????????? 1 (??????)
				vndrOrder.setPrdtRltnFileSeq(1);

				// ???????????????????????? : ????????????(??????) ????????? ??????
				if (ocOrder.getOrderStatCode() != null) {
					vndrOrder.setOrderPrdtStatCode(ocOrder.getOrderStatCode());
				}

				List<OcOrderProduct> orderProductList = ocOrderProductDao.selectClaimRequestOrderProductList(vndrOrder);

				// TODO: ProductOptionService - getProductOptionValidator

				if (!UtilsArray.isEmpty(orderProductList)) {

					PdProductOptionWithStockOnlyOne prdtOptn = null;

					// ???????????? ????????? ??????
					for (OcOrderProduct ocOrderProduct : orderProductList) {

						prdtOptn = productOptionService.getProductOptionListWithStockOnlyOne(ocOrderProduct.getPrdtNo(),
								ocOrderProduct.getPrdtOptnNo());

						if (prdtOptn != null) {
							ocOrderProduct.setPdProductOptionWithStockOnlyOne(prdtOptn);
						}
					}

					vndrOrder.setOcOrderProductList(orderProductList); // ?????? ????????? ?????? ???????????? ????????? ??????,????????? ?????? ??????

					// ??? ?????? ??????,????????? ?????? ??????
					vndrOrder.setVndrOrgOrderProductList(orgOrderProductList.stream()
							.filter(x -> UtilsText.equals(x.getVndrNo(), vndrOrder.getVndrNo()))
							.collect(Collectors.toList()));
				}
			}

			// map.put("vndrList", vndrOrderList);
			// ?????? ?????? ??????
			map.put("mmnyList", vndrOrderList.stream()
					.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).collect(Collectors.toList()));

			// ?????? ?????? ??????
			map.put("vndrList",
					vndrOrderList.stream().filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE))
							.collect(Collectors.toList()));
		}

		/*
		 * ??????/?????? ??? ?????? ???????????? ?????? ????????? ?????????
		 */
		if (UtilsText.equals(ocOrder.getClmGbnCode(), CommonCode.CLM_GBN_CODE_EXCHANGE)
				|| UtilsText.equals(ocOrder.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN)) {
			/*
			 * ?????? ??????/?????? ???????????? ?????? ????????????
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

		// ???????????????, ?????? ?????? ??????
		String[] codeFields = { CommonCode.CLM_RSN_CODE, CommonCode.BANK_CODE, };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);

		map.put(CommonCode.CLM_RSN_CODE, codeList.get(CommonCode.CLM_RSN_CODE));
		map.put(CommonCode.BANK_CODE, codeList.get(CommonCode.BANK_CODE));

		return map;
	}

	/**
	 * @Desc : ??????????????? - ???????????? ?????? ???????????? ??????
	 * @Method Name : getCommonOrderStat
	 * @Date : 2019. 5. 13.
	 * @Author : ?????????
	 * @param OcOrder
	 * @return OrderStatVO
	 * @throws Exception
	 */
	public OrderStatVO getCommonOrderStat(OcOrder param) throws Exception {
		// ????????? ??????
		String memberTypeCode = param.getMemberTypeCode();
		if (UtilsText.equals(CommonCode.MEMBER_TYPE_CODE_NONMEMBER, memberTypeCode)) {
			param.setOrderNo(param.getNonOrderNo());
		}

		return ocOrderDao.selectCommonOrderStat(param);
	}

	/**
	 * @Desc : 1:1 ???????????? ?????? ??????????????? ???????????? ??????
	 * @Method Name : getOrderPrdtInMemberCnsl
	 * @Date : 2019. 5. 24.
	 * @Author : ?????????
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
	 * @Desc : ????????? FORM ?????? ??? ????????? ??????
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

		// ???????????? ?????? ??????
		map.put("order", orderFormVo);
		map.put("prdtCouponList", mapper.writeValueAsString(orderFormVo.getCouponList()));

		// ?????? ????????????
		String[] codeFields = { CommonCode.PROMO_TYPE_CODE };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);
		map.put("siteCodeList", codeList);

		// ?????? ?????? ??????
		map.put("memberOrderTerms", termsService.getMemberOrderTerms());

		// ?????? ?????? ??????

		// ?????? ?????? (????????????,??????)

		// ?????? ?????? ??????
		Map<String, Object> paymentMeansMap = getOrderPaymentMeans(orderCartVo);
		map.put("mainPaymentsMeans", paymentMeansMap.get("mainPaymentsMeans"));
		map.put("subPaymentMeans", paymentMeansMap.get("subPaymentMeans"));
		map.put("useDlvyType", siteService.getUseDeliveryType(orderCartVo.getSiteNo()));

		// ????????? ??????
		if (orderCartVo.isLogin()) {
			MbMemberInterestStore memberInterestParam = new MbMemberInterestStore();
			memberInterestParam.setMemberNo(orderCartVo.getMemberNo());
			memberInterestParam.setSiteNo(orderCartVo.getSiteNo());

			OrderFormMemberVo memberInfo = orderFormVo.getMemberInfo();

			map.put("memberInfo", mapper.writeValueAsString(memberInfo));

			// ????????? ?????? ??????
			map.put("interestStore", memberService.getInterestStore(memberInterestParam));

			// ??????????????????
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

			// ????????? ????????? ?????? (??????, ??????)
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
			// ????????? ?????? ?????? ??????
			map.put("memberCoupon", mapper
					.writeValueAsString(memberCouponService.getAvailableCouponListForOrder(memberInfo.getMemberNo())));

			// ????????? ?????? ?????? ?????????
			PrivateReport privateReport = new PrivateReport(); // memberPointService.getMemberPointInfo(member.getMemberNo());
			if (UtilsText.equals(CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, memberInfo.getMemberTypeCode())
					&& UtilsText.equals(Const.BOOLEAN_FALSE, orderCartVo.getEmpYn())) {
				map.put("memberPoint", mapper.writeValueAsString(privateReport));
			}
			// ????????? ?????? ????????????

		}
		System.out.println("getOrderInfoByCart :::::: " + map.toString());
		return map;
	}

	/**
	 * @Desc : ???????????? ?????? ??????, ?????? ?????? ??????
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
		// ?????? ?????? ?????? ?????? ?????? ?????? 20190613
		cartList = cartList.stream().map(cart -> {
			cart.setDeviceCode(orderCartVo.getDeviceCode());
			cart.setMemberTypeCode(orderCartVo.getMemberTypeCode());
			cart.setMbshpGradeCode(orderCartVo.getMbshpGradeCode());
			cart.setEmpYn(orderCartVo.getEmpYn());
			return cart;
		}).collect(Collectors.toList());
		// ?????? ?????? ?????? ?????? ?????? ?????? 20190613

		List<PdProductWithAll> productList = productService.getProductListWithAll(cartList);
		List<VdVendor> vendorList = getVendorInfoList(productList);

		/** ??? ????????? */
		Integer totalNormalAmt = new Integer(0);

		/** ??? ????????? */
		Integer totalSellAmt = new Integer(0);

		/** ??? ???????????? */
		Integer totalDscntAmt = new Integer(0);

		/** ??? ????????? */
		Integer totalDlvyAddAmt = new Integer(0);

		OrderFormVo orderFormVo = new OrderFormVo();
		orderFormVo.setCartDeliveryType(orderCartVo.getCartDeliveryType());
		orderFormVo.setOrderCartType("C");
		orderFormVo.setDeviceCode(orderCartVo.getDeviceCode());

		// ???????????? ??????
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
		// ?????? ?????????
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
				// ?????????????????? ??????.
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
					 * ?????? ????????????
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
						// ?????? ?????? ?????? set
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
						// ????????? ?????? ?????? ??? ?????? ?????? ????????? ????????? set
						productVo.setSavePoint(String.valueOf(benefitCpnInfo.getCpnApplyTotPoint()));
						orderFormVo.setTotalSavePoint(
								orderFormVo.getTotalSavePoint() + benefitCpnInfo.getCpnApplyTotPoint());
						product.setTotalSellAmt(benefitCpnInfo.getCpnApplyTotSellAmt()); // ??? ?????????(???????????? totalSellAmt)
						orderCoupon.setCouponList(couponList);
						prdtCouponList.add(orderCoupon);
					} else {

						// ?????? ????????? ??????
						double savePoint = getSavePoint(product.getDscntAmt()) * product.getOrderQty();
						productVo.setSavePoint(String.valueOf(savePoint));
						orderFormVo.setTotalSavePoint(orderFormVo.getTotalSavePoint() + savePoint);
					}

					productVo.setCpnDscntAmt(cpnTotDcAmt); // ?????? ?????? ?????? ??????
					orderFormVo.setCouponDscntAmt(orderFormVo.getCouponDscntAmt() + cpnTotDcAmt); // ?????? ?????? ?????? ?????? ??????

					productVo.setProduct(product);
//					abcProductVoList.add(productVo);
					if (product.getFreeDlvyYn().equals(Const.BOOLEAN_TRUE))
						abcVendorVo.setFreeDlvy(true);

					// ???????????? ?????????
					int manyShoesDcAmt = 0; // ???????????? ?????? ?????? ( ?????? ?????? ??????)
					int promoDcAmt = 0; // ???????????? ?????? ??????

					if (product.getPromotion().length > 0) {
						for (PrPromotion promo : product.getPromotion()) {
							if (promo.getDcAmt() != null) {
								if (product.getPrdtDscntAmt() > promo.getDcAmt()) {
									// ?????? ?????????
									orderFormVo.setProductDscntAmt(
											orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
								}
								orderFormVo.setPromotionDscntAmt(orderFormVo.getPromotionDscntAmt()
										+ promo.getDcAmt() * productVo.getOrderQty());
								if (UtilsText.equals(promo.getPromoTypeCode(), "10001")) { // ????????????
									manyShoesDcAmt = promo.getDcAmt();
								}
								promoDcAmt = promoDcAmt + (promo.getDcAmt() * productVo.getOrderQty());
							}
						}
					} else {
						// ?????? ?????????
						orderFormVo.setProductDscntAmt(orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
					}

					productVo.setManyShoesDcAmt(manyShoesDcAmt);// ????????????
					productVo.setPromoDscntAmt(promoDcAmt);// ???????????? ?????? ??????
					productVo.setTotalDscntAmt(promoDcAmt + cpnTotDcAmt);// (???????????? ???????????? + ?????? ????????????)
					abcProductVoList.add(productVo); // ????????????
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

		// ????????? ?????????
		for (VdVendor vendor : vendorList) {
			if (UtilsText.equals(vendor.getVndrGbnType(), "V")) {
				orderFormVo.setStorePickupDlvy(false);// ????????? ?????? ?????? ?????? ??????.
				orderFormVo.setCsvPickupDlvy(false);// ????????? ?????? ????????? ?????? ??????.
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
						 * ?????? ????????????
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

							// ?????? ?????? ?????? set
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
							// ????????? ?????? ?????? ??? ?????? ?????? ????????? ????????? set
							productVo.setSavePoint(String.valueOf(benefitCpnInfo.getCpnApplyTotPoint()));
							orderFormVo.setTotalSavePoint(
									orderFormVo.getTotalSavePoint() + benefitCpnInfo.getCpnApplyTotPoint());
							product.setTotalSellAmt(benefitCpnInfo.getCpnApplyTotSellAmt()); // ??? ?????????(???????????? totalSellAmt)
							orderCoupon.setCouponList(couponList);
							prdtCouponList.add(orderCoupon);
						} else {

							// ?????? ????????? ??????
							double savePoint = getSavePoint(product.getDscntAmt()) * product.getOrderQty();
							productVo.setSavePoint(String.valueOf(savePoint));
							orderFormVo.setTotalSavePoint(orderFormVo.getTotalSavePoint() + savePoint);
						}

						productVo.setCpnDscntAmt(cpnTotDcAmt); // ?????? ????????? ?????? ??????
						orderFormVo.setCouponDscntAmt(orderFormVo.getCouponDscntAmt() + cpnTotDcAmt); // ?????? ?????? ?????? ?????? ??????

						// ?????? ????????? ??????
//						double savePoint = getSavePoint(product.getDscntAmt()) * product.getOrderQty();
//						orderFormVo.setTotalSavePoint(orderFormVo.getTotalSavePoint() + savePoint);
//						productVo.setSavePoint(String.valueOf(savePoint));
						productVo.setProduct(product);
//						productVoList.add(productVo);
						if (product.getFreeDlvyYn().equals(Const.BOOLEAN_TRUE))
							vendorVo.setFreeDlvy(true);

						vendorProductAmt = vendorProductAmt + product.getTotalSellAmt();

						// ???????????? ?????????
						int manyShoesDcAmt = 0;
						int promoDcAmt = 0; // ???????????? ?????? ??????

						if (product.getPromotion().length > 0) {
							for (PrPromotion promo : product.getPromotion()) {
								if (promo.getDcAmt() != null) {
									if (product.getPrdtDscntAmt() > promo.getDcAmt()) {
										// ?????? ?????????
										orderFormVo.setProductDscntAmt(
												orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
									}
									orderFormVo.setPromotionDscntAmt(orderFormVo.getPromotionDscntAmt()
											+ promo.getDcAmt() * productVo.getOrderQty());
									if (UtilsText.equals(promo.getPromoTypeCode(), "10001")) { // ????????????
										manyShoesDcAmt = promo.getDcAmt();
									}
									promoDcAmt = promoDcAmt + (promo.getDcAmt() * productVo.getOrderQty());
								}
							}
						} else {
							// ?????? ?????????
							orderFormVo
									.setProductDscntAmt(orderFormVo.getProductDscntAmt() + product.getPrdtDscntAmt());
						}

						productVo.setManyShoesDcAmt(manyShoesDcAmt);// ????????????
						productVo.setPromoDscntAmt(promoDcAmt);// ???????????? ?????? ??????
						productVo.setTotalDscntAmt(promoDcAmt + cpnTotDcAmt);// (???????????? ???????????? + ?????? ????????????)

						productVoList.add(productVo);// ????????????

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
	 * @Desc : ???????????? ?????? ?????? ??????
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
	 * @Desc : ?????? ?????? ?????? ????????? ??????
	 * @Method Name : getVendorInfoList
	 * @Date : 2019. 5. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @param prdtList
	 * @return
	 * @throws Exception
	 */
	public List<VdVendor> getVendorInfoList(List<PdProductWithAll> prdtList) throws Exception {
		// ????????? ?????? ?????? ??????
		List<String> vndrNoList = prdtList.stream().filter(x -> x != null).map(PdProductWithAll::getVndrNo).distinct()
				.collect(Collectors.toList());

		List<VdVendor> vendorInfoList = vendorService.getVendorInfoList(vndrNoList);
		return vendorInfoList;

	}

	/**
	 * @Desc : ???????????? ?????? ??????
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
	 * @Desc : ?????? ?????? ??????
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
		useCoupon.setOrderNo(ocOrder.getOrderNo()); // ??????????????? ??????
		result.put("orderCouponList", ocOrderUseCouponDao.couponListByOrder(useCoupon));

		OrderCartVo orderCartVo = new OrderCartVo();
		orderCartVo.setSiteNo(order.getSiteNo());
		Map<String, Object> paymentMeansMap = getOrderPaymentMeans(orderCartVo);
		result.put("mainPaymentsMeans", paymentMeansMap.get("mainPaymentsMeans"));
		result.put("subPaymentMeans", paymentMeansMap.get("subPaymentMeans"));

		return result;
	}

	/**
	 * @Desc : ?????? ?????????, ??????????????? ??????
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
	 * @Desc : ??????????????? ??????
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
	 * @Desc : ???????????? ?????? ??????
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
	 * @Desc : ????????? ?????? ?????? ??????
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
	 * @Desc : ?????? ????????? ??????
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
		memberDelivery.setDlvyAddrName("???????????????");
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
	 * @Desc : ?????? ?????? ???????????? ??????
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

		// TODO : ?????? ?????? validation
		validOrder(orderSaveVo);

		// TODO : ?????? ?????? ??????
		resultMap = saveOrder(orderSaveVo, resultMap);

		// TODO : PG ?????? ??????
		resultMap = paymentConfirm(orderSaveVo, resultMap);

		// ?????? ?????? ??? ?????? ????????? ????????? ?????? ??????.
		if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_FAIL)) {
			paymentCancel(orderSaveVo, resultMap);
		} else {
			// TODO : ?????? ??? ????????????
			// resultMap = updateProductStock(orderSaveVo, resultMap);

			// ?????? ?????? ??? ?????? ????????? ????????? ?????? ??????.
			if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_FAIL)) {
				paymentCancel(orderSaveVo, resultMap);
			} else { // TODO : ?????? ??? ?????? ?????? ??????
				completeOrderStatus(orderSaveVo);
			}
		}

		// TODO : RETURN ??????
		return resultMap;
	}

	private OrderSaveVo makeOrder(OrderPaymentVo parameter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// ????????? ???????????? ??????
		OrderSaveVo orderSave = new OrderSaveVo();

		// ??????
		OcOrder ocOrder = new OcOrder();
		ocOrder.setOrderNo(parameter.getOrderNo());
		ocOrder.setOrgOrderNo(parameter.getOrderNo());
		ocOrder.setSiteNo(parameter.getSiteNo());
		ocOrder.setDeviceCode(parameter.getDeviceCode());
		ocOrder.setRsvOrderYn(parameter.getRsvOrderYn());
		// ?????????????????? ??????
		ocOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_TEMP);
		// ????????? ?????? (???????????? ??????)
		ocOrder.setRgsterNo(parameter.getMemberNo());

		// ????????? ??????
		ocOrder.setMemberNo(parameter.getMemberNo());
		ocOrder.setMemberTypeCode(parameter.getMemberTypeCode());
		ocOrder.setEmpYn(parameter.getEmpYn());
		// TODO : VIP ??????????????? ?????? ??????.
		ocOrder.setMbshpGradeCode(CommonCode.MBSHP_GRADE_CODE_NORMAL);
		// TODO : OTS VIP ?????? ??????.
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

		// ?????? ?????? ??????
		ocOrder.setDlvyTypeCode(parameter.getDlvyTypeCode());
		ocOrder.setUnRecptTermExtsnYn(Const.BOOLEAN_FALSE);

		// ?????????
		ocOrder.setMmnyDlvyAmt(parameter.getMmnyDlvyAmt());
		ocOrder.setTotalVndrDlvyAmt(parameter.getTotalVndrDlvyAmt());

		// ?????? ??????
		ocOrder.setRcvrName(parameter.getRcvrName());
		ocOrder.setRcvrHdphnNoText(parameter.getRcvrHdphnNoText());
		ocOrder.setRcvrPostCodeText(parameter.getRcvrPostCodeText());
		ocOrder.setRcvrPostAddrText(parameter.getRcvrPostAddrText());
		ocOrder.setRcvrDtlAddrText(parameter.getRcvrDtlAddrText());
		ocOrder.setDlvyMemoText(parameter.getDlvyMemoText());
		// ????????? ??????
		// ?????? ??????

		// TODO : ?????? ??????
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
		// TODO : ???????????????(not null ????????? ????????? ???????????? ?????????. )
		ocOrder.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
		ocOrder.setTaxBillIssueStatCode(CommonCode.TAX_BILL_ISSUE_STAT_CODE_APPLY);

		// TODO : ???????????? ?????? ?????? ?????? ??????
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

		// ?????? ?????????
		if (UtilsText.equals(ocOrder.getDlvyTypeCode(), CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP)) {
			parameter.getOrderConvenienceStore().setOrderNo(ocOrder.getOrderNo());
			parameter.getOrderConvenienceStore().setCvnstrSeq((short) 1);
			parameter.getOrderConvenienceStore().setDlvyPlaceYn(Const.BOOLEAN_TRUE);
			orderSave.setCvsStore(parameter.getOrderConvenienceStore());
		}

		// ?????? ??????
		// ?????? ?????? ?????? ??????
		List<OrderProduct> prdtList = new ArrayList<OrderProduct>();

		// ????????? ?????? ??????
		List<OrderProduct> giftPrdtList = new ArrayList<OrderProduct>();

		// ????????? ?????? ??????
		List<OrderProduct> dlvyPrdtList = new ArrayList<OrderProduct>();

		// ?????? ?????? ???????????? ?????? ??????
		List<OrderPromotion> prdtPromotionList = new ArrayList<OrderPromotion>();

		// ?????? ?????? ?????? ??????
		List<OcOrderUseCoupon> couponList = new ArrayList<OcOrderUseCoupon>();
		short couponSeq = 1;

		short productSeq = 1;
		for (OrderProduct productParam : parameter.getOrderProduct()) {
			if (UtilsText.equals(productParam.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) {
				OrderProduct orderProduct = new OrderProduct();
				orderProduct = mapper.readValue(mapper.writeValueAsString(productParam), OrderProduct.class);
				orderProduct.setOrderPrdtSeq(productSeq);
				orderProduct.setOrderQty(1);// ?????? ????????? 1??? ??????
				orderProduct.setRsvPrdtYn(ocOrder.getRsvOrderYn()); // ??????????????????
				orderProduct.setOrderPrdtStatCode(CommonCode.ORDER_STAT_CODE_TEMP);// ????????????????????????(????????????)
				orderProduct.setRgsterNo(parameter.getMemberNo());
				orderProduct.setPrdtTypeCode(CommonCode.PRDT_TYPE_CODE_DELIVERY);
				orderProduct.setVndrName(productParam.getVndrName());
				if (parameter.getDlvyCouponList() != null && parameter.getDlvyCouponList().length > 0) {
					// ????????? ??????
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
					orderProduct.setOrderQty(1);// ?????? ????????? 1??? ??????
					orderProduct.setOrderAmt(productParam.getOrderAmt() / productParam.getOrderQty());
					orderProduct.setRsvPrdtYn(ocOrder.getRsvOrderYn()); // ??????????????????
					// TODO : ????????? ????????? ??????
					if (UtilsText.equals(parameter.getEmpOrderYn(), Const.BOOLEAN_TRUE)) {
						// orderProduct.setEmpDscntRate(productParam.getEmpDscntRate()); // ?????????
						// orderProduct.setEmpAmt(productParam.getEmpAmt()); // ????????????
						orderProduct.setEmpDscntRate((short) 0); // ?????????
						orderProduct.setEmpAmt(0); // ????????????
					}
					// TODO : ?????? ?????? ??????
					orderProduct.setTotalDscntAmt(0);
					orderProduct.setCpnDscntAmt(0);

					// TODO : ????????? ?????????
					orderProduct.setVndrCmsnRate((short) 0);

					orderProduct.setSellCnclReqYn(Const.BOOLEAN_FALSE); // ??????????????????
					orderProduct.setLogisCnvrtYn(Const.BOOLEAN_FALSE); // ??????????????????
					orderProduct.setPickupExtsnYn(Const.BOOLEAN_FALSE); // ??????????????????
					orderProduct.setOrderPrdtStatCode(CommonCode.ORDER_STAT_CODE_TEMP);// ????????????????????????(????????????)
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
								if (promotion.getDscntAmt() > 0) { // ???????????? ?????? ?????? ??????
									orderProduct.setTotalDscntAmt(
											orderProduct.getTotalDscntAmt() + promotion.getDscntAmt());// ?????? ?????? ??????
								}
								// ?????? ?????? ??????
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
															+ ocOrderCoupon.getDscntAmt());// ?????? ?????? ??????
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
								// ????????? ?????? ??????.
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
									giftProduct.setOrderPrdtStatCode(CommonCode.ORDER_STAT_CODE_TEMP);// ????????????????????????(????????????)
									giftProduct.setRsvPrdtYn(ocOrder.getRsvOrderYn()); // ??????????????????
									giftProduct.setRgsterNo(parameter.getMemberNo());
									giftProduct.setVndrName(productParam.getVndrName());
									giftPrdtList.add(giftProduct);
									productSeq++;
								}
							}
						}
					} else {
						// ?????? ?????? ??????
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
											// ?????? ?????? ??????
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

		// ?????? ????????? ??????
		if (parameter.getDoublePointCoupon() != null) {
			// TODO: ????????? ?????? ??? ????????? savepoint ??????.
			OcOrderUseCoupon doubleCoupon = parameter.getDoublePointCoupon();
			doubleCoupon.setOrderUseCpnSeq(couponSeq);
			couponList.add(parameter.getDoublePointCoupon());
			couponSeq++;
		}

		// ?????? ??????
		orderSave.setCouponList(couponList);
		// ?????? ?????? ?????? ??????
		List<OcOrderDeliveryHistory> dlvyList = new ArrayList<OcOrderDeliveryHistory>();

		short deliverySeq = 1; // ????????? ???????????? ????????? ???????????? ??????, ???????????? 1??? ??????
		for (OrderProduct product : prdtList) {
			OcOrderDeliveryHistory delivery = new OcOrderDeliveryHistory();
			delivery.setOrderNo(product.getOrderNo()); // ?????? ??????
			delivery.setOrderPrdtSeq(product.getOrderPrdtSeq()); // ?????? ?????? ??????
			delivery.setPrdtNo(product.getPrdtNo());// ?????? ??????
			delivery.setOrderDlvyHistSeq(deliverySeq); // ?????? ?????? ?????? ??????
			delivery.setDlvyIdText(getDeliveryId(product.getOrderNo(), product.getOrderPrdtSeq(), deliverySeq));
			delivery.setDlvyDgreCount(deliverySeq); // ????????????
			delivery.setRcvrName(ocOrder.getRcvrName());// ????????????
			delivery.setRcvrTelNoText(ocOrder.getRcvrHdphnNoText());// ?????????????????????
			delivery.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());// ????????????????????????
			delivery.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());// ?????????????????????
			delivery.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());// ?????????????????????
			delivery.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());// ?????????????????????
			delivery.setRgsterNo(product.getRgsterNo()); // ???????????????
			if (UtilsText.equals(ocOrder.getRsvOrderYn(), Const.BOOLEAN_TRUE)) {
				delivery.setRsvDlvyDtm(parameter.getRsvDlvyYmd());
			}

			delivery.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // TODO : ?????? ?????? AI??? ?????? ?????? , ?????? ?????? ??? ?????? ?????? ??????

			dlvyList.add(delivery);
		}

		for (OrderProduct product : giftPrdtList) {
			OcOrderDeliveryHistory delivery = new OcOrderDeliveryHistory();
			delivery.setOrderNo(product.getOrderNo()); // ?????? ??????
			delivery.setOrderPrdtSeq(product.getOrderPrdtSeq()); // ?????? ?????? ??????
			delivery.setOrderDlvyHistSeq(deliverySeq); // ?????? ?????? ?????? ??????
			delivery.setDlvyIdText(getDeliveryId(product.getOrderNo(), product.getOrderPrdtSeq(), deliverySeq));
			delivery.setDlvyDgreCount(deliverySeq); // ????????????
			delivery.setStoreChngDgreCount((short) 0);// ?????????????????? 0
			delivery.setDlvyDscntcYn(Const.BOOLEAN_FALSE);// ??????????????????
			delivery.setMissProcYn(Const.BOOLEAN_FALSE);// ??????????????????
			delivery.setWmsSendYn(Const.BOOLEAN_FALSE); // WMS????????????
			delivery.setRcvrName(ocOrder.getRcvrName());// ????????????
			delivery.setRcvrTelNoText(ocOrder.getRcvrHdphnNoText());// ?????????????????????
			delivery.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());// ????????????????????????
			delivery.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());// ?????????????????????
			delivery.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());// ?????????????????????
			delivery.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());// ?????????????????????
			delivery.setLogisPymntPrdtAmt(0); // ???????????????????????????
			delivery.setLogisPymntDlvyAmt(0); // ????????????????????????
			delivery.setRgsterNo(product.getRgsterNo()); // ???????????????
			if (UtilsText.equals(ocOrder.getRsvOrderYn(), Const.BOOLEAN_TRUE)) {
				delivery.setRsvDlvyDtm(parameter.getRsvDlvyYmd());
			}

			delivery.setStockGbnCode(CommonCode.STOCK_GBN_CODE_AI); // TODO : ?????? ?????? AI??? ?????? ?????? , ?????? ?????? ??? ?????? ?????? ??????

			dlvyList.add(delivery);
		}
		orderSave.setDlvyList(dlvyList);
		// ?????? ??????
		OrderTermsAgree[] termsList = parameter.getOrderTermsAgree();

		orderSave.setTermsList(termsList);

		// ???????????????
		OrderPayment giftCard = parameter.getOrderGiftPayment();
		// ??? ????????????
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

		// ?????? ?????????
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

		// ????????? ?????????
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
	 * @Desc : ?????? ?????? ?????? ??????.
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
	 * @Desc : ?????? ?????? validation
	 * @Method Name : validOrder
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @throws Exception
	 */
	private void validOrder(OrderSaveVo parameter) throws Exception {

	}

	/**
	 * @Desc : ?????? ????????? ??????
	 * @Method Name : saveOrder
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @throws Exception
	 */
	private Map<String, Object> saveOrder(OrderSaveVo parameter, Map<String, Object> resultMap) throws Exception {
		// ?????? ????????? ??????
		ocOrderDao.insert(parameter.getOcOrder());

		// ?????? CVS ?????? ??????
		// ?????? ?????????
		if (UtilsText.equals(parameter.getOcOrder().getDlvyTypeCode(), CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP)) {
			ocOrderConvenienceStoreDao.insertCVS(parameter.getCvsStore());
		}

		// ?????? ?????? ?????? ??????
		if (!parameter.getPrdtList().isEmpty() && parameter.getPrdtList().size() > 0) {
			List<OrderProduct> savePrdtList = new ArrayList<OrderProduct>();
			int cnt = 0;
			for (OrderProduct savePrdt : parameter.getPrdtList()) {
				cnt++;
				savePrdtList.add(savePrdt);

				if (cnt % 10 == 0) { // MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
					ocOrderProductDao.insertProductList(savePrdtList);
					savePrdtList = new ArrayList<OrderProduct>();
				}
			}
			if (cnt % 10 > 0) {
				ocOrderProductDao.insertProductList(savePrdtList);
			}
		}

		// ?????? ????????? ?????? ?????? ??????
		if (parameter.getDlvyList() != null && parameter.getGiftPrdtList().size() > 0) {
			List<OrderProduct> savePrdtList = new ArrayList<OrderProduct>();
			int cnt = 0;
			for (OrderProduct savePrdt : parameter.getGiftPrdtList()) {
				cnt++;
				savePrdtList.add(savePrdt);

				if (cnt % 10 == 0) { // MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
					ocOrderProductDao.insertGiftProduct(savePrdtList);
					savePrdtList = new ArrayList<OrderProduct>();
				}
			}
			if (cnt % 10 > 0) {
				ocOrderProductDao.insertGiftProduct(savePrdtList);
			}
		}

		// ?????? ????????? ?????? ?????? ??????
		if (parameter.getDlvyPrdtList() != null && parameter.getDlvyPrdtList().size() > 0) {
			List<OrderProduct> savePrdtList = new ArrayList<OrderProduct>();
			int cnt = 0;
			for (OrderProduct savePrdt : parameter.getDlvyPrdtList()) {
				cnt++;
				savePrdtList.add(savePrdt);
				if (cnt % 10 == 0) { // MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
					ocOrderProductDao.insertDlvyProduct(savePrdtList);
					savePrdtList = new ArrayList<OrderProduct>();
				}
			}
			if (cnt % 10 > 0) { // MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
				ocOrderProductDao.insertDlvyProduct(savePrdtList);
			}
		}

		if (parameter.getDlvyList() != null && parameter.getDlvyList().size() > 0) {
			// ?????? ?????? ?????? ?????? ??????
			List<OcOrderDeliveryHistory> saveDlvyList = new ArrayList<OcOrderDeliveryHistory>();
			int cnt = 0;
			for (OcOrderDeliveryHistory saveDlvy : parameter.getDlvyList()) {
				cnt++;
				saveDlvyList.add(saveDlvy);
				if (cnt % 10 == 0) {// MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
					ocOrderDeliveryHistoryDao.insertDeliveryList(saveDlvyList);
					saveDlvyList = new ArrayList<OcOrderDeliveryHistory>();
				}

			}
			if (cnt % 10 > 0) {// MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
				ocOrderDeliveryHistoryDao.insertDeliveryList(saveDlvyList);
			}
		}

		// ?????? ?????? ???????????? ??????
		if (parameter.getPrdtPromotionList() != null && parameter.getPrdtPromotionList().size() > 0) {
			List<OrderPromotion> savePromoList = new ArrayList<OrderPromotion>();
			int cnt = 0;
			for (OrderPromotion savePromo : parameter.getPrdtPromotionList()) {
				cnt++;
				savePromoList.add(savePromo);
				if (cnt % 10 == 0) {// MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
					ocOrderProductApplyPromotionDao.insertPromotionList(savePromoList);
					savePromoList = new ArrayList<OrderPromotion>();
				}
			}
			if (cnt % 10 > 0) {// MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
				ocOrderProductApplyPromotionDao.insertPromotionList(savePromoList);
			}
		}

		// ?????? ?????? ?????? ??????
		if (parameter.getCouponList() != null && parameter.getCouponList().size() > 0) {
			List<OcOrderUseCoupon> saveCouponList = new ArrayList<OcOrderUseCoupon>();
			int cnt = 0;
			for (OcOrderUseCoupon saveCoupon : parameter.getCouponList()) {
				cnt++;
				saveCouponList.add(saveCoupon);
				if (cnt % 10 == 0) {// MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
					ocOrderUseCouponDao.insertList(parameter.getCouponList());
					saveCouponList = new ArrayList<OcOrderUseCoupon>();
				}
			}
			if (cnt % 10 > 0) {// MSSQL ???????????? ?????? ?????? ?????? ????????? ?????? 10??? ????????? ?????? ????????????.
				ocOrderUseCouponDao.insertList(parameter.getCouponList());
			}
		}

		// ?????? ?????? ?????? ??????
		ocOrderTermsAgreeDao.insertOrderTermsAgree(parameter.getTermsList());

		return resultMap;
	}

	/**
	 * @Desc : ?????? ?????? ?????? ??????(PG)
	 * @Method Name : paymentConfirm
	 * @Date : 2019. 5. 13.
	 * @Author : ljyoung@3top.co.kr
	 * @param parameter
	 * @param resultMap
	 * @throws Exception
	 */
	public Map<String, Object> paymentConfirm(OrderSaveVo parameter, Map<String, Object> resultMap) throws Exception {

		// ????????? ??????
		if (parameter.getOcOrder().getPointUseAmt() > 0) {
			// ??????????????? ???????????? SafeKey??? ??????
			boolean result = false;
			int eventPoint = 0;// ??????????????????
			int accessPoint = 0;// ???????????????

			if (parameter.getEventPointPayment() != null && parameter.getEventPointPayment().getPayAmt() > 0)
				eventPoint = parameter.getEventPointPayment().getPayAmt();

			if (parameter.getAccessPointPayment() != null && parameter.getAccessPointPayment().getPayAmt() > 0)
				accessPoint = parameter.getAccessPointPayment().getPayAmt();

			MbMember member = parameter.getOrderMember();
//			result = membershipPointService.updatePointForMembershipHandler(member.getSafeKey(), accessPoint,
//					BaseConst.POINT_TYPE_USE, eventPoint);
			result = true;
			if (result) {
				// ????????? ????????? ??????
				if (eventPoint > 0) {
					resultMap = savePointPayment(parameter, parameter.getEventPointPayment(), resultMap);
				}

				// ?????? ????????? ??????
				if (accessPoint > 0) {
					resultMap = savePointPayment(parameter, parameter.getAccessPointPayment(), resultMap);
				}
			} else {
				resultMap.put("result", Const.RESULT_FAIL);
			}

		}

		// ?????????????????????
		if (UtilsText.equals(resultMap.get("result").toString(), Const.RESULT_SUCCESS)
				&& parameter.getGiftPayment() != null && parameter.getGiftPayment().getPayAmt() > 0) {
			CommNiceRes<CollectionResponse> giftApproval = giftCardApproval(parameter.getGiftPayment().getGiftCardNo(),
					parameter.getGiftPayment().getPayAmt(), parameter.getGiftPayment().getGiftCardPinNo());
			if (UtilsText.equals(giftApproval.getResCode(), "0000")) { // TODO : const??? ????????? ?????? ?????? ?????? ??????.
				resultMap = saveGiftPayment(parameter, giftApproval, resultMap);
				resultMap.put("giftApproval", giftApproval);
			} else {
				// TODO: ?????? rollback
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
			// ????????? ????????? ???????????? ???????????? ????????? ????????? ???????????? ?????? ?????????.
			if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_HANDPHONE)) {
				KcpPaymentApprovalReturn hpReturn = new KcpPaymentApprovalReturn();

				hpReturn.setTno(CommonCode.PYMNT_MEANS_CODE_HANDPHONE + " TEST");
				hpReturn.setAmount(payment.getPayAmt().toString());
				hpReturn.setResCd("0000");
				hpReturn.setResMsg("{??????????????????}");
				hpReturn.setCommid("KT");
				hpReturn.setVanCd("KT");
				hpReturn.setMobileNo("010-1234-5678");

				PaymentResult result = new PaymentResult("Y", "0000", "", hpReturn,
						PaymentConst.PAYMENT_APPROVAL_SUCCESS_MESSAGE, "");
				System.out.println("????????? ????????? ???????????? ???????????? ?????? ?????? ????????? !!!!!!!!!!!!!!!!!!!!!!");
				resultMap = saveMainPayment(parameter, result, resultMap);
			} else {

				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_KAKAO_PAY))
					payment.getKakaoPaymentApproval().setPartnerUserId(parameter.getOcOrder().getMemberNo());

				System.out.println("?????? ??????!!!!!!!!!!!!");
				PaymentResult result = paymentEntrance
						.approval(new PaymentInfo(payment.getAddInfo2(), payment.returnPayment()));
				if (result.getSuccessYn().equals(Const.BOOLEAN_TRUE)) {
					resultMap = saveMainPayment(parameter, result, resultMap);
					resultMap.put("mainPaymentApproval", result);
				} else {
					// TODO: ?????? rollback
					resultMap.put("result", Const.RESULT_FAIL);
				}
			}
		}
		return resultMap;
	}

	/**
	 * @Desc : ????????? ?????? ????????? ?????? ?????? ?????? ?????? ??????.
	 * @Method Name : paymentCancel
	 * @Date : 2019. 6. 21.
	 * @Author : ljyoung@3top.co.kr
	 * @param resultMap
	 * @throws Exception
	 */
	public void paymentCancel(OrderSaveVo parameter, Map<String, Object> resultMap) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String cancelReason = "?????? ?????? ??????";
		// ??????????????? ??????.
		if (resultMap.get("mainPaymentApproval") != null) {
			PaymentResult mainPaymentApproval = (PaymentResult) resultMap.get("mainPaymentApproval");
			OrderPayment payment = parameter.getMainPayment();
			if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_HANDPHONE)) {
				System.out.println("????????? ????????? ???????????? ???????????? ?????? ?????? ????????? !!!!!!!!!!!!!!!!!!!!!!");
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

					kcpPaymentCancel.setReqTx(""); // ???????????? : ??????????????? ??? ??? ??????
					kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
					kcpPaymentCancel.setCustIp(custIp); // ?????? IP
					kcpPaymentCancel.setTno(kcpResult.getTno()); // ????????????
					kcpPaymentCancel.setModDesc(cancelReason); // ????????????

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

		// ??????????????? ??????.
		if (resultMap.get("giftApproval") != null) {
			OrderPayment giftApproval = parameter.getGiftPayment();
			CommNiceRes<CollectionResponse> giftCancel = (CommNiceRes<CollectionResponse>) resultMap
					.get("giftApproval");

			giftCardCancel(giftApproval.getGiftCardNo(), parameter.getGiftPayment().getPayAmt(),
					parameter.getGiftPayment().getGiftCardPinNo(), giftCancel.getResData().getApprovalNo(),
					giftCancel.getResData().getApprovalDate());
		}

		// ????????? ?????? ??????.
		if (parameter.getOcOrder().getPointUseAmt() > 0) {
			// ??????????????? ???????????? SafeKey??? ??????
			boolean result = false;
			int eventPoint = 0;// ??????????????????
			int accessPoint = 0;// ???????????????

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
	 * @Desc : ????????? ?????? ?????? ??????
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
	 * @Desc : ????????? ?????? ?????????
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
	 * @Desc : ??????????????? ??????
	 * @Method Name : giftCardCancel
	 * @Date : 2019. 6. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param cardNo        ?????? ??????
	 * @param amountVal     ?????? ??????
	 * @param conPin        ????????????
	 * @param oApprovalNo   ??????????????????
	 * @param oApprovalDate ?????????????????? ex) 20190610
	 * @param orderCancel   ?????? ????????? ?????? ????????? ??? ?????? ???????????? false
	 * @return
	 * @throws Exception
	 */
	public CommNiceRes<CollectionResponse> giftCardCancel(String cardNo, int amountVal, String conPin,
			String oApprovalNo, String oApprovalDate, boolean orderCancel) throws Exception {
		CollectionRequest collectionRequest = new CollectionRequest("A4", "1", "37" + cardNo + "=4912", amountVal,
				conPin);
		collectionRequest.setMessageType("0420");
		if (!orderCancel)
			collectionRequest.setServiceCode("C4");// ?????????????????? ?????? -> ?????????????????? ??????
		collectionRequest.setOApprovalNo(oApprovalNo);
		collectionRequest.setOApprovalDate(oApprovalDate);

		CommNiceRes<CollectionResponse> collectionResult = niceGiftService.sendCollection(collectionRequest);
		return collectionResult;
	}

	/**
	 * @Desc : ????????? ?????? ?????? ??????
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

			ocOrderPayment.setPymntTodoAmt(pointPayment.getPayAmt()); // ??????????????????
			ocOrderPayment.setPymntAmt(pointPayment.getPayAmt()); // ????????????

			ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????
			ocOrderPayment.setRgsterNo(parameter.getOcOrder().getMemberNo());

			ocOrderPaymentDao.insertPointPayment(ocOrderPayment);
		} catch (Exception e) {
			resultMap.put("result", Const.RESULT_FAIL);
		}

		return resultMap;
	}

	/**
	 * @Desc : ????????? ?????? ?????? ?????? ??????
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
			ocOrderPayment.setPymntOrganName("???????????????");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
			Date apprDate = dateFormat.parse(resData.getApprovalDate());

			ocOrderPayment.setPymntDtm(new java.sql.Timestamp(apprDate.getTime()));// ????????? ?????? ?????? ?????? ??????
			ocOrderPayment.setDeviceCode(parameter.getOcOrder().getDeviceCode());
			ocOrderPayment.setMainPymntMeansYn(payment.getMainPymntMeansYn());
			ocOrderPayment.setPymntMeansCode(payment.getPymntMeansCode());
			ocOrderPayment.setPymntLogInfo(mapper.writeValueAsString(resData));
			ocOrderPayment.setRgsterNo(parameter.getOcOrder().getMemberNo());

			ocOrderPayment.setPymntAprvNoText(resData.getApprovalNo()); // ??????????????????
			ocOrderPayment.setPymntTodoAmt(payment.getPayAmt()); // ??????????????????
			ocOrderPayment.setPymntAmt(payment.getPayAmt()); // ????????????
			ocOrderPayment.setRspnsCodeText(collectionResult.getResCode());
			ocOrderPayment.setRspnsMesgText(collectionResult.getResMsg());

			ocOrderPayment.setPymntOrganNoText(payment.getGiftCardNo()); // ?????? ?????? ??????
			ocOrderPayment.setGiftCardPinNoText(payment.getGiftCardPinNo());// ?????? ????????????

			ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????

			ocOrderPaymentDao.insertGiftPayment(ocOrderPayment);

		} catch (Exception e) {
			resultMap.put("result", Const.RESULT_FAIL);
		}

		return resultMap;
	}

	/**
	 * @Desc : ?????? ????????? ?????? ??????
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
			ocOrderPayment.setPymntMeansChngPsbltYn(Const.BOOLEAN_FALSE); // ??????????????????????????????

			switch (payment.getAddInfo2()) {

			case Const.PAYMENT_VENDOR_NAME_KCP:
				KcpPaymentApprovalReturn kcpResult = (KcpPaymentApprovalReturn) mapper
						.readValue(mapper.writeValueAsString(result.getData()), result.getData().getClass());
				ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP);
				ocOrderPayment.setPymntAprvNoText(kcpResult.getTno()); // KCP ??????????????????
				ocOrderPayment.setPymntTodoAmt(Integer.parseInt(kcpResult.getAmount())); // ??????????????????
				ocOrderPayment.setPymntAmt(Integer.parseInt(kcpResult.getAmount())); // ????????????
				ocOrderPayment.setDpstPressEmailSendYn(Const.BOOLEAN_FALSE); // ??????????????????????????????
				ocOrderPayment.setRspnsCodeText(kcpResult.getResCd());
				ocOrderPayment.setRspnsMesgText(kcpResult.getResMsg());

				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getCardCd());
					ocOrderPayment.setPymntOrganName(kcpResult.getCardName());
					ocOrderPayment.setPymntOrganNoText(kcpResult.getCardNo());// [????????????]????????????, 16????????? ??????????????? ???????????????
					if (kcpResult.getCardBinType02().equals("0")) // [????????????]?????? ????????????(???????????? ??????), 0 : ??????, 1 : ??????
						ocOrderPayment.setCardGbnType("N");
					else
						ocOrderPayment.setCardGbnType("C");
					if (kcpResult.getCardBinType01().equals("0")) // [????????????]?????? ???????????? (???????????? ??????), 0 : ??????, 1 : ??????
						ocOrderPayment.setCardType("N");
					else
						ocOrderPayment.setCardType("C");
					ocOrderPayment.setCardPartCnclPsbltYn(kcpResult.getPartcancYn());// [????????????]???????????? ?????? ??????
					ocOrderPayment.setIntrstFreeYn(kcpResult.getNoinf());// [????????????]???????????????
					ocOrderPayment.setInstmtTermCount(Short.parseShort(kcpResult.getQuota()));// [????????????]????????????, 00 : ?????????
					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????

					ocOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // ???????????????????????????
					ocOrderPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // ????????????????????????
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
					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_WAIT_DEPOSIT); // ????????????
				}
				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getBankCode());
					ocOrderPayment.setPymntOrganName(kcpResult.getBankName());
//				ocOrderPayment.setPymntOrganNoText(kcpResult.getAccount()); //???????????? ???????????? ??????

					if (kcpResult.getCashAuthno() != null && kcpResult.getCashNo() != null) {
						ocOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_TRUE);
					} else {
						ocOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
					}
					if (kcpResult.getCashAuthno() != null)
						ocOrderPayment.setCashRcptAprvNo(kcpResult.getCashAuthno());
					if (kcpResult.getCashNo() != null)
						ocOrderPayment.setCashRcptDealNo(kcpResult.getCashNo());
					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????
				}

				if (payment.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_HANDPHONE)) {
					ocOrderPayment.setPymntOrganCodeText(kcpResult.getCommid());
					ocOrderPayment.setPymntOrganName(kcpResult.getVanCd());
					ocOrderPayment.setPymntOrganNoText(kcpResult.getMobileNo());

					ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????
				}
				break;
			case Const.PAYMENT_VENDOR_NAME_NAVER:
				NaverPaymentApprovalReturn naverResult = (NaverPaymentApprovalReturn) mapper
						.readValue(mapper.writeValueAsString(result.getData()), result.getData().getClass());

				NaverPaymentApprovalReturnBodyDetail naverpayDeatail = naverResult.getBody().getDetail();
				ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_NAVER);
				ocOrderPayment.setPymntAprvNoText(naverResult.getBody().getPaymentId()); // ??????????????? ??????????????????
				ocOrderPayment.setPymntTodoAmt(naverpayDeatail.getTotalPayAmount().intValue()); // ??????????????????
				ocOrderPayment.setPymntAmt(naverpayDeatail.getTotalPayAmount().intValue()); // ????????????
				ocOrderPayment.setDpstPressEmailSendYn(Const.BOOLEAN_FALSE); // ??????????????????????????????
				ocOrderPayment.setRspnsCodeText(naverResult.getCode());
				ocOrderPayment.setRspnsMesgText(naverResult.getMessage());

				if (UtilsText.equals(naverpayDeatail.getPrimaryPayMeans(), "CARD")) {
					ocOrderPayment.setPymntOrganCodeText(naverpayDeatail.getCardCorpCode());
					ocOrderPayment.setPymntOrganNoText(naverpayDeatail.getCardNo()); // ??????????????????
					ocOrderPayment.setPymntOrganName(naverpayDeatail.getCardCorpCode());
					ocOrderPayment.setInstmtTermCount(naverpayDeatail.getCardInstCount().shortValue());// [????????????]????????????, 0
																										// :
				} else {
					ocOrderPayment.setPymntOrganCodeText(naverpayDeatail.getBankCorpCode());
					ocOrderPayment.setPymntOrganNoText(naverpayDeatail.getBankAccountNo()); // ??????????????????
					ocOrderPayment.setPymntOrganName(naverpayDeatail.getBankCorpCode());
				}
				ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????
				break;
			case Const.PAYMENT_VENDOR_NAME_KAKAO:
				KakaoPaymentApprovalReturn kakaoResult = (KakaoPaymentApprovalReturn) mapper
						.readValue(mapper.writeValueAsString(result.getData()), result.getData().getClass());
				ocOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KAKAO);
				ocOrderPayment.setPymntAprvNoText(kakaoResult.getAid()); // ????????? Request ?????? ??????
				ocOrderPayment.setPymntOrganNoText(kakaoResult.getTid()); // ????????? ?????? ?????? ??????
				ocOrderPayment.setPymntOrganCodeText(kakaoResult.getPaymentMethodType()); // ????????? ?????? ?????? ??????
				ocOrderPayment.setPymntTodoAmt(kakaoResult.getAmount().getTotal()); // ??????????????????
				ocOrderPayment.setPymntAmt(kakaoResult.getAmount().getTotal()); // ????????????
				ocOrderPayment.setDpstPressEmailSendYn(Const.BOOLEAN_FALSE); // ??????????????????????????????
				ocOrderPayment.setRspnsCodeText("0000"); // 0000
				ocOrderPayment.setRspnsMesgText("??????"); // ??????
				ocOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ?????? ???????????? : ????????????
				break;
			}

			ocOrderPaymentDao.insertMainPayment(ocOrderPayment);
			// ??????????????? ?????? ??????
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
		OrderPayment mainPayment = order.getMainPayment(); // ???????????????
		// ????????? ????????? ???????????????????????? ?????? ?????? ????????? ??????
		if (mainPayment != null
				&& UtilsText.equals(mainPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)) {
			// TODO : ?????? ?????? update
			ocOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_STAND_BY);
			ocOrderDao.update(ocOrder);

			// TODO : ?????? ?????? ?????? update
			ocOrder.setOrderStatCode(CommonCode.ORDER_PRDT_STAT_CODE_STAND_BY);
			ocOrderProductDao.updateOrderStat(ocOrder);

			// TODO : ?????? ?????? ?????? ?????? update

			// ?????????????????? ?????? ?????? ?????? ????????? update ?????? ??????

		} else {
			// TODO : ?????? ?????? update
			ocOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_COMPLETE);
			ocOrderDao.update(ocOrder);

			// TODO : ?????? ?????? ?????? update
			ocOrder.setOrderStatCode(CommonCode.ORDER_PRDT_STAT_CODE_COMPLETE);
			ocOrderProductDao.updateOrderStat(ocOrder);

			// TODO : ?????? ?????? ?????? ?????? update

			// TODO : ?????? ?????? ?????? ?????? update
			ocOrder.setOrderStatCode(CommonCode.DLVY_STAT_CODE_PAYMENT_FINISH);
			ocOrderDeliveryHistoryDao.updateOrderStat(ocOrder);

		}
	}

	/**
	 * @Desc : ?????? ?????? update
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
	 * @Desc : ??????????????? ??????
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
	 * @Desc : ?????? ?????? ????????? ??????
	 * @Method Name : getSavePoint
	 * @Date : 2019. 6. 14.
	 * @Author : ljyoung@3top.co.kr
	 * @param amt
	 * @return
	 * @throws Exception
	 */
	public double getSavePoint(Integer amt) throws Exception {
		// ?????? ????????? ??????
		// (?????? * ?????????) / 1000
		double savePoint = (amt * 3 / 1000.0);
		// ??????(???????????????) * 10 -- ????????? ?????? ??????
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
	 * @Desc : ??????????????? ??????(??????/?????? ?????? ??????)
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

				String orderAllConfirmYn = "N"; // ???????????? ??????
				String orderAllCancelYn = "N"; // ?????? ?????? ??????

				Map<String, Object> resultMap = this.orderStatActionValue(orderList.get(i).getOrderNo());

				// ???????????? , ?????? ?????? ??????
				if (!resultMap.isEmpty()) {
					orderAllConfirmYn = resultMap.get("orderAllConfirmYn").toString();
					orderAllCancelYn = resultMap.get("orderAllCancelYn").toString();
				}

				orderProduct.setOrderPrdtStatCodeClick(pageable.getBean().getOrderPrdtStatCodeClick()); // ??????????????? ?????? ????????????
																										// ????????????
				orderProduct.setOrderNo(orderList.get(i).getOrderNo());
				orderProduct.setMemberNo(orderList.get(i).getMemberNo());
				orderList.get(i).setOcOrderProductList(ocOrderProductDao.selectOrderProductByOrderNo(orderProduct));
				orderList.get(i).setOcOrderProductListSize(orderList.get(i).getOcOrderProductList().size());
				orderList.get(i).setBuyDcsnYn(orderAllConfirmYn); // ???????????? ??????
				orderList.get(i).setOrderCancelAllYn(orderAllCancelYn); // ?????? ?????? ??????
				// ?????? ????????? ??????
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
	 * @Desc :??????????????? ?????? ??????
	 * @Method Name : getOrderDetailInfo
	 * @Date : 2019. 5. 24.
	 * @Author : flychani@3top.co.kr
	 * @param orderParam
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrderDetailInfo(OcOrder orderParam) throws Exception {
		Map<String, Object> orderMap = new HashMap<>();

		OcOrder orderDtail = ocOrderDao.selectOrderDetail(orderParam); // ??????????????? ?????? ??????

		OcOrderProduct orderProduct = new OcOrderProduct();
		orderProduct.setOrderNo(orderParam.getOrderNo());
		orderProduct.setMemberNo(orderDtail.getMemberNo());
		// ?????? ????????? ?????? ?????? ??????
		SySite sysite = siteService.getSite(orderDtail.getSiteNo());

		// ????????????
		List<OcOrderProduct> productList = ocOrderProductDao.selectOrderProductAllByOrder(orderProduct); // ???????????? ?????? ??????

		// ?????? ????????? ??????
		long point = productList.stream().mapToLong(o -> o.getPrdtPoint()).sum();
		orderDtail.setPrdtPoint(point);

		// ?????????????????? -??????
		List<OcOrderProduct> productABCListAll = productList.stream().filter(x -> "Y".equals(x.getMmnyPrdtYn()))
				.collect(Collectors.toList());

		// ?????????????????? - ????????? ???????????????
		List<OcOrderProduct> productABCList = productList.stream().filter(x -> "Y".equals(x.getMmnyPrdtYn()))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) // ????????? ??????
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)) // ?????????
				.collect(Collectors.toList());

		long abcDeliveryCnt = productABCListAll.stream()
				.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)).count();

		if (abcDeliveryCnt > 0) {
			// ????????? ?????? ?????? / ????????? ??????
			orderDtail.setFreeDlvyStdrAmt(sysite.getFreeDlvyStdrAmt());
			orderDtail.setStdrDlvyAmt(sysite.getDlvyAmt());
		}

		// ?????? ??????
		List<OcOrderProduct> productVendorList = productList.stream().filter(x -> "N".equals(x.getMmnyPrdtYn()))
				.collect(Collectors.toList());

		// ?????? ???????????? ????????????
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
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) // ????????? ??????
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)) // ?????????
					.collect(Collectors.toList()));

			// ????????? ?????? ?????? ??????
			long deliveryAmtCnt = productVendorList.stream()
					.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)
							&& vendorNo.equals(x.getVndrNo()))
					.count();

			if (deliveryAmtCnt > 0) {
				// ????????? ?????? ?????? / ????????? ??????
				ocOrderProduct.setFreeDlvyStdrAmt(vdVendor.getFreeDlvyStdrAmt());
				ocOrderProduct.setDlvyAmt(Integer.toString(vdVendor.getDlvyAmt()));
			}

			/*
			 * // ????????? ?????? long giftProdCnt = productVendorList.stream() .filter(x ->
			 * UtilsText.equals(x.getPrdtTypeCode(), "10003") &&
			 * vendorNo.equals(x.getVndrNo())) .count(); if (giftProdCnt > 0) {
			 * ocOrderProduct.setGiftProdList(productList.stream() .filter(o ->
			 * vendorNo.equals(o.getVndrNo()) && UtilsText.equals(o.getPrdtTypeCode(),
			 * "10003")) .collect(Collectors.toList())); }
			 */

			log.debug(k + "::::::::vendorGroupList:::::" + ocOrderProduct);

			// ????????? ?????? ??????
			vendorGroupList.add(ocOrderProduct);

		}
		String dlvyTypeCode = orderDtail.getDlvyTypeCode();

		OcOrderConvenienceStore cvsInfo = new OcOrderConvenienceStore();
		CmStore storeInfo = new CmStore();
		if (UtilsText.equals(dlvyTypeCode, CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP)) { // ????????? ?????? ??????
			// OC_ORDER_CONVENIENCE_STORE
			OcOrderConvenienceStore cvs = new OcOrderConvenienceStore();
			cvs.setOrderNo(orderParam.getOrderNo());
			List<OcOrderConvenienceStore> cvsList = this.selectConvenienceStore(cvs);

			// ???????????? ?????? ????????? ??????????????? ??????????????? ??????
			cvsInfo = cvsList.stream().sorted(Comparator.comparing(OcOrderConvenienceStore::getCvnstrSeq).reversed())
					.findFirst().orElse(null);
		} else if (UtilsText.equals(dlvyTypeCode, CommonCode.DLVY_TYPE_CODE_STORE_PICKUP)) { // ?????? ?????? ?????? ?????? ?
			CmStore cmStore = new CmStore();
			// ?????? ???????????? ???????????? ???????????? ?????????
			cmStore.setStoreNo(orderDtail.getStoreNo());
			storeInfo = storeService.getCmStoreDetail(cmStore);
		}

		OcOrderPayment orderPayment = new OcOrderPayment();
		orderPayment.setOrderNo(orderParam.getOrderNo());

		// ?????? ?????? ??????
		OcOrderPayment orderPaymentData = ocOrderPaymentDao.selectPaymentInfo(orderPayment);

		// ??????????????? ?????? ?????? ??????
		List<OcOrderPayment> orderPaymentInfo = ocOrderPaymentDao.selectPaymentDetail(orderPayment);

		// ?????? ?????? ?????? ?????? ????????? ??????
		List<OcOrderPayment> orderPaymentListHistory = ocOrderPaymentDao.selectPaymentListHistory(orderPayment);

		// ?????? ?????? ????????? ?????? ?????????
		OcOrderPayment orderReceiptPayment = ocOrderPaymentDao.selectReceiptPayment(orderPayment);
		// ???????????? ????????? ????????? ???????????? ,

		// ???????????? ?????? ?????? ?????? ?????? ?????? S
		String orderAllConfirmYn = "N"; // ???????????? ??????
		String orderAllCancelYn = "N"; // ?????? ?????? ??????

		Map<String, Object> resultMap = this.orderStatActionValue(orderParam.getOrderNo());

		// ???????????? , ?????? ?????? ??????
		if (!resultMap.isEmpty()) {
			orderAllConfirmYn = resultMap.get("orderAllConfirmYn").toString();
			orderAllCancelYn = resultMap.get("orderAllCancelYn").toString();
		}

		orderDtail.setBuyDcsnYn(orderAllConfirmYn); // ???????????? ??????
		orderDtail.setOrderCancelAllYn(orderAllCancelYn); // ?????? ?????? ??????
		// ???????????? ?????? ?????? ?????? ?????? ?????? E

		// ?????? ??????
		orderMap.put("orderParam", orderDtail);
		// ?????? ?????? ?????????
		orderMap.put("productABCList", productABCList);
		// ?????? ?????? ?????????
		orderMap.put("vendorGroupList", vendorGroupList);
		// ?????? ??????
		orderMap.put("storeInfo", storeInfo);
		// ????????? ??????
		orderMap.put("cvsInfo", cvsInfo);
		// ?????? ??????
		orderMap.put("orderPaymentData", orderPaymentData);
		// ?????? ?????? ?????????
		orderMap.put("orderPaymentInfo", orderPaymentInfo);
		// ?????? ???????????? ?????????
		orderMap.put("orderPaymentListHistory", orderPaymentListHistory);
		// ?????? ????????? ????????? ????????? ??????
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
	 * @Desc : ???????????? ???????????? ?????????
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

		// ?????? ????????? ?????? ???????????? , ???????????????????????? ??????????????? [memB300a]
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
	 * @Desc :????????? ??????
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
			map.put("errorMsg", "????????? interface ??????" + e.getMessage());
			return map;
			// TODO: handle exception
		}

		int resultCnt = 0;

		// ?????? ????????? RCVR ????????????
		resultCnt = ocOrderDao.updateOcOrderAddrModify(ocOrder);

		// ?????? ???????????? ????????????
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
		// ????????? order_no , order_pymnt_seq??? ??????. ???????????? ?????? ??????
		OcOrderPayment oldOcOrderPaymentData = ocOrderPaymentDao.selectByPrimaryKey(ocOrderPayment);

		// ?????? ??? ????????? ????????? ??????.
		KcpPaymentApproval kcpPaymentApproval = ocOrderPayment.getKcpPaymentApproval();

		// 1. OC_ORDER_PAYMENT INSERT ??? ??????
		OcOrderPayment ocOrderPaymentInsert = new OcOrderPayment();
		ocOrderPaymentInsert.setOrderNo(ocOrderPayment.getOrderNo());
		ocOrderPaymentInsert.setOrderPymntSeq(ocOrderPayment.getOrderPymntSeq());
		ocOrderPaymentInsert.setDeviceCode(oldOcOrderPaymentData.getDeviceCode());
		ocOrderPaymentInsert.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // ?????????????????????
		if (UtilsText.equals(ocOrderPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			ocOrderPaymentInsert.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_CARD); // ??????????????????-????????????
		} else {
			ocOrderPaymentInsert.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER); // ??????????????????-????????? ????????????

		}
		ocOrderPaymentInsert.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP); // ??????????????????
		ocOrderPaymentInsert.setPymntOrganCodeText(null); // ??????????????????
		ocOrderPaymentInsert.setPymntOrganNoText(null); // ??????????????????(????????????, ??????????????????)
		ocOrderPaymentInsert.setPymntOrganName(null); // ???????????????
		ocOrderPaymentInsert.setInstmtTermCount((short) 0); // ????????????
		ocOrderPaymentInsert.setPymntAprvNoText(null); // ??????????????????
		ocOrderPaymentInsert.setPymntTodoAmt(ocOrderPayment.getPymntAmt()); // ??????????????????
		ocOrderPaymentInsert.setPymntAmt(ocOrderPayment.getPymntAmt()); // ????????????
		ocOrderPaymentInsert.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // ??????????????????????????????
		ocOrderPaymentInsert.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // ??????????????????????????? ??????????????? UPDATE
		ocOrderPaymentInsert.setEscrApplyYn(Const.BOOLEAN_FALSE); // ????????????????????????
		ocOrderPaymentInsert.setPymntMeansChngPsbltYn(Const.BOOLEAN_FALSE); // ??????????????????????????????
		ocOrderPaymentInsert.setEscrAprvNoText(null); // ????????????????????????
		ocOrderPaymentInsert.setRspnsCodeText(null); // ????????????
		ocOrderPaymentInsert.setRspnsMesgText(null); // ???????????????
		ocOrderPaymentInsert.setPymntStatCode(null); // ??????????????????
		ocOrderPaymentInsert.setPymntLogInfo(null); // ????????????
		ocOrderPaymentInsert.setRgsterNo(ocOrderPayment.getRgsterNo()); // ?????????

		ocOrderPaymentDao.insertPaymentChange(ocOrderPaymentInsert);

		kcpPaymentApproval.setSiteCd(Config.getString("pg.kcp.siteCode"));
		kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.siteKey"));
		PaymentResult paymentResult = paymentEntrance
				.approval(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentApproval)); // KCP ??????

		// ?????? ????????? ?????? exception
		if (UtilsText.equals(paymentResult.getSuccessYn(), Const.BOOLEAN_FALSE)) {
			throw new Exception(paymentResult.getMessage() + "step:3");
		}

		KcpPaymentApprovalReturn kcpPaymentApprovalReturn = ((KcpPaymentApprovalReturn) paymentResult.getData());
		log.info("#################### :: " + paymentResult.toString());
		log.info("#################### :: " + kcpPaymentApprovalReturn.toString());

		// ?????? ???????????? OC_AS_PAYMENT ????????????
		OcOrderPayment ocOrderPaymentResult = new OcOrderPayment();
		if (UtilsText.equals(ocOrderPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			ocOrderPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getCardCd());
			ocOrderPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getCardName());
			ocOrderPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getCardNo());
			ocOrderPaymentResult.setInstmtTermCount(Short.valueOf(kcpPaymentApprovalReturn.getQuota()));
			// [????????????]?????? ???????????? (???????????? ??????), 0 : ??????, 1 : ??????
			if ("0".equals(kcpPaymentApprovalReturn.getCardBinType01())) {
				ocOrderPaymentResult.setCardType(CommonCode.CARD_TYPE_NORMAL);
			} else {
				ocOrderPaymentResult.setCardType(CommonCode.CARD_TYPE_CORPORATE);
			}
			// [????????????]?????? ????????????(???????????? ??????), 0 : ??????, 1 : ??????
			if ("0".equals(kcpPaymentApprovalReturn.getCardBinType02())) {
				ocOrderPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_NORMAL);
			} else {
				ocOrderPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_CHECK);
			}

			ocOrderPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
			ocOrderPaymentResult.setCashRcptAprvNo(UtilsText.EMPTY);
			ocOrderPaymentResult.setCashRcptDealNo(UtilsText.EMPTY);
		} else {
			ocOrderPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getBankCode()); // ??????????????????(??????, ????????????...)
			ocOrderPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getBankName()); // ???????????????(??????, ?????????...)
			ocOrderPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getAccount()); // ??????????????????(????????????, ??????????????????...)
			ocOrderPaymentResult.setInstmtTermCount(Short.valueOf("0"));
			if (kcpPaymentApprovalReturn.getCashAuthno() != null && kcpPaymentApprovalReturn.getCashAuthno() != "") {
				ocOrderPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_TRUE);
				ocOrderPaymentResult.setCashRcptAprvNo(kcpPaymentApprovalReturn.getCashAuthno());
				ocOrderPaymentResult.setCashRcptDealNo(kcpPaymentApprovalReturn.getCashNo());
			} else {
				ocOrderPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
			}
			ocOrderPaymentResult.setEscrApplyYn(kcpPaymentApprovalReturn.getEscwYn()); // ???????????? ?????? ?????? ???
		}
		ocOrderPaymentResult.setPymntAprvNoText(kcpPaymentApprovalReturn.getTno()); // ??????????????????
		ocOrderPaymentResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ??????????????????
		ocOrderPaymentResult.setRspnsCodeText(kcpPaymentApprovalReturn.getResCd()); // ????????????
		ocOrderPaymentResult.setRspnsMesgText(kcpPaymentApprovalReturn.getResMsg()); // ???????????????
		ocOrderPaymentResult.setPymntLogInfo(new ObjectMapper().writeValueAsString(kcpPaymentApprovalReturn)); // ????????????
		ocOrderPaymentResult.setModerNo(ocOrderPayment.getRgsterNo()); // ?????????
		ocOrderPaymentResult.setOrderNo(ocOrderPaymentInsert.getOrderNo());
		ocOrderPaymentResult.setOrderPymntSeq(ocOrderPaymentInsert.getOrderPymntSeq());

		try {
			// ???????????? ??????
			int newPaymntCnt = ocOrderPaymentDao.updateOcOrderPaymentAccount(ocOrderPaymentResult);
			// ?????? ?????? ??? ????????? ?????? 0?????? ????????? ?????????.
			if (newPaymntCnt == 0) {
				throw new Exception("?????? ????????? ???????????? ????????? ???????????? ????????????. step : 4");
			}
		} catch (Exception e) {
			// ???????????? ?????? ????????? ?????? kcp card ?????? ??????
			KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
			kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
			kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));
			kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
			kcpPaymentCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP ????????? ????????????
			kcpPaymentCancel.setCustIp(kcpPaymentApproval.getCustIp()); // ?????? ????????? IP
			kcpPaymentCancel.setModDesc("???????????? ??????  ??????"); // ?????? ??????

			PaymentResult result = paymentEntrance
					.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
			KcpPaymentCancelReturn kcpPaymentCancelReturn = ((KcpPaymentCancelReturn) result.getData());

			// ?????? ????????? ??????
			ocOrderPaymentResult.setOrderNo(ocOrderPaymentInsert.getOrderNo());
			ocOrderPaymentResult.setOrderPymntSeq(ocOrderPaymentInsert.getOrderPymntSeq());
			ocOrderPaymentResult.setRspnsCodeText(kcpPaymentCancelReturn.getResCd()); // ????????????
			ocOrderPaymentResult.setRspnsMesgText(kcpPaymentCancelReturn.getResMsg()); // ???????????????
			ocOrderPaymentResult.setPymntLogInfo(
					new ObjectMapper().writeValueAsString(kcpPaymentCancelReturn + "::???????????? ????????? ::" + result.getData()));
			ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentResult);

			throw new Exception(e.getMessage() + "step::5  ???????????? ?????? ?????? ");
		}

		// ?????? ?????? ??? ?????? UPDATE OC_ORDER_PAYMENT??? ?????? ??????
		OcOrderPayment ocOrderPaymentCancel = new OcOrderPayment();
		ocOrderPaymentCancel.setOrderNo(oldOcOrderPaymentData.getOrderNo());
		ocOrderPaymentCancel.setOrderPymntSeq(oldOcOrderPaymentData.getOrderPymntSeq());
		ocOrderPaymentCancel.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL);
		ocOrderPaymentCancel.setModerNo(ocOrderPayment.getRgsterNo()); // ?????????

		try {
			// ???????????? ?????? ??????(kcp ?????? ??? ??????)
			int resultPayOldCnt = ocOrderPaymentDao.updateOcOrderOldPaymentCancel(ocOrderPaymentCancel);

			// ?????? ????????? ?????? ???????????? ????????? ?????? ?????? ??????.
			if (resultPayOldCnt > 0) {
				KcpPaymentCancel kcpPaymentOldCancel = new KcpPaymentCancel();
				kcpPaymentOldCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
				kcpPaymentOldCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));
				kcpPaymentOldCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
				kcpPaymentOldCancel.setTno(oldOcOrderPaymentData.getPymntAprvNoText()); // ?????? ????????????
				kcpPaymentOldCancel.setCustIp(kcpPaymentApproval.getCustIp()); // ?????? ????????? IP
				kcpPaymentOldCancel.setModDesc("???????????? ???????????? ?????? ??????"); // ?????? ??????

				PaymentResult result = paymentEntrance
						.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentOldCancel));
				KcpPaymentCancelReturn kcpPaymentOldCancelReturn = ((KcpPaymentCancelReturn) result.getData());

				if (UtilsText.equals(result.getSuccessYn(), Const.BOOLEAN_FALSE)) {
					ocOrderPaymentCancel.setOrderNo(oldOcOrderPaymentData.getOrderNo());
					ocOrderPaymentCancel.setOrderPymntSeq(oldOcOrderPaymentData.getOrderPymntSeq());
					ocOrderPaymentCancel.setRspnsCodeText(kcpPaymentOldCancelReturn.getResCd()); // ????????????
					ocOrderPaymentCancel.setRspnsMesgText(kcpPaymentOldCancelReturn.getResMsg()); // ???????????????
					ocOrderPaymentCancel.setPymntLogInfo(new ObjectMapper()
							.writeValueAsString(kcpPaymentOldCancelReturn + "::???????????? ????????? ::" + result.getData()));
					ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentCancel);

					throw new Exception("??? ?????? ?????? ?????? ");
				} else {
					ocOrderPaymentCancel.setOrderNo(oldOcOrderPaymentData.getOrderNo());
					ocOrderPaymentCancel.setOrderPymntSeq(oldOcOrderPaymentData.getOrderPymntSeq());
					ocOrderPaymentCancel.setRspnsCodeText(kcpPaymentOldCancelReturn.getResCd()); // ????????????
					ocOrderPaymentCancel.setRspnsMesgText(kcpPaymentOldCancelReturn.getResMsg()); // ???????????????
					ocOrderPaymentCancel.setPymntLogInfo(
							new ObjectMapper().writeValueAsString(kcpPaymentOldCancelReturn + (result.getMessage())));
					ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentCancel);
				}

			} else {
				// ???????????? 0??? ????????? ?????????.
				throw new Exception("?????? ???????????? ????????? ???????????? ????????????. step::6 ");
			}
		} catch (Exception e) {
			// ?????? ???????????? ?????? ????????? ?????? ?????? ?????? kcp ?????? ??????
			KcpPaymentCancel kcpPaymentNewCancel = new KcpPaymentCancel();
			kcpPaymentNewCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
			kcpPaymentNewCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));
			kcpPaymentNewCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
			kcpPaymentNewCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP ????????? ????????????
			kcpPaymentNewCancel.setCustIp(kcpPaymentApproval.getCustIp()); // ?????? ????????? IP
			kcpPaymentNewCancel.setModDesc("?????? ?????? ?????? ??????  ????????? ?????? ??????"); // ?????? ??????

			// ????????? ???????????? ????????? ?????? ??????
//			paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentNewCancel));

			PaymentResult result = paymentEntrance
					.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentNewCancel));
			KcpPaymentCancelReturn kcpPaymentNewCancelReturn = ((KcpPaymentCancelReturn) result.getData());

			// ?????? ????????? ??????
			ocOrderPaymentResult.setOrderNo(ocOrderPaymentInsert.getOrderNo());
			ocOrderPaymentResult.setOrderPymntSeq(ocOrderPaymentInsert.getOrderPymntSeq());
			ocOrderPaymentResult.setRspnsCodeText(kcpPaymentNewCancelReturn.getResCd()); // ????????????
			ocOrderPaymentResult.setRspnsMesgText(kcpPaymentNewCancelReturn.getResMsg()); // ???????????????
			ocOrderPaymentResult.setPymntLogInfo(new ObjectMapper()
					.writeValueAsString(kcpPaymentNewCancelReturn + "::???????????? ????????? ::" + result.getData()));
			ocOrderPaymentDao.updateOcOrderPaymenKcpLog(ocOrderPaymentResult);

			throw new Exception(e.getMessage() + "step::7 ?????? ?????? ?????? ??????");
		}

		map.put("successYn", Const.BOOLEAN_TRUE);
		map.put("step", "0");
		return map;
	}

	public Map<String, Object> updateOrdPrdOptionChange(OcOrderProduct ocOrderProduct) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// ????????? order_no , order_prdt_seq??? ?????? ???????????? ??????
		OcOrderDeliveryHistory ocOrderDeliveryHistoryParam = new OcOrderDeliveryHistory();

		ocOrderDeliveryHistoryParam.setOrderNo(ocOrderProduct.getOrderNo());
		ocOrderDeliveryHistoryParam.setOrderPrdtSeq(ocOrderProduct.getOrderPrdtSeq());

		OcOrderDeliveryHistory ocOrderDeliveryHistoryData = ocOrderDeliveryHistoryDao
				.selectOcOrderDeliveryHistory(ocOrderDeliveryHistoryParam);

		if (ocOrderDeliveryHistoryData == null) {
			throw new Exception("?????? ???????????? ???????????? ????????????. : step : 1");

		}
		// ????????? ?????? ??????
		OcOrderProduct ocOrderProductOldData = ocOrderProductDao.selectByPrimaryKey(ocOrderProduct);

		if (ocOrderProductOldData == null) {
			throw new Exception("?????? ???????????? ????????????. : step : 2");
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
			throw new Exception("?????? ?????? update ?????? step : 3");
		}

		// ???????????? ROW INSERT
		OcOrderProductHistory prdtHistory = new OcOrderProductHistory();
		prdtHistory.setOrderNo(ocOrderProduct.getOrderNo());
		prdtHistory.setOrderPrdtSeq(ocOrderProduct.getOrderPrdtSeq());
		prdtHistory.setPrdtNo(ocOrderProductOldData.getPrdtNo());
		prdtHistory.setPrdtOptnNo(ocOrderProduct.getPrdtOptnNo());
		prdtHistory.setPrdtName(ocOrderProductOldData.getPrdtName());
		prdtHistory.setEngPrdtName(ocOrderProductOldData.getEngPrdtName());
		prdtHistory.setOptnName(ocOrderProduct.getOptnName());
		prdtHistory.setOrderPrdtStatCode(ocOrderProductOldData.getOrderPrdtStatCode()); // ????????? ?????? ?????? ????????? ??????.
		prdtHistory.setNoteText("?????? ??????"); // NOTE_TEXT
		prdtHistory.setRgsterNo(ocOrderProduct.getModerNo());

		int insertCnt = ocOrderProductHistoryDao.insertProductHistory(prdtHistory);

		if (insertCnt == 0) {
			throw new Exception("?????? ?????? ???????????? insert ?????? step : 4 ");
		}

		if ("Y".equals(ocOrderDeliveryHistoryData.getWmsSendYn())) {

			boolean isSucc = interfacesOrderService.updateOrderChangeOptnNoTrx("AI",
					ocOrderDeliveryHistoryData.getDlvyIdText(), String.valueOf(ocOrderProduct.getOrderPrdtSeq()),
					ocOrderProductOldData.getInsdMgmtInfoText(),
					ocOrderProductOldData.getVndrPrdtNoText() + "001" + ocOrderProductOldData.getPrdtOptnNo(),
					ocOrderProductOldData.getVndrPrdtNoText() + "001" + ocOrderProduct.getPrdtOptnNo());

			if (!isSucc) {
				throw new Exception("interFace ?????? staep : 5");
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
			// ???????????? ??? Count??????

			ocOrder.setOrderNo(orderNo);
			OrderStatVO orderStatParam = ocOrderDao.selectOrderStatCount(ocOrder);

			int orderTotalCnt = orderStatParam.getOrderTotalCnt(); // ?????? ???????????????
			int orderConfirmCnt = orderStatParam.getOrderConfirmCnt(); // ?????? ???????????? ??????
			int orderCancelCnt = orderStatParam.getOrderCancelCnt(); // ?????? ?????? ??????
			int orderReadlyCnt = orderStatParam.getOrderReadlyCnt(); // ????????????,???????????? ??????
			int orderClaimCnt = orderStatParam.getOrderClaimTotalCnt(); // ????????? ??????

			// ??????????????? ???????????? ????????? ?????? ?????? ????????? ?????? ????????? ?????? ?????? ??????
			if (orderTotalCnt == (orderConfirmCnt + orderCancelCnt)) {
				map.put("orderAllConfirmYn", "Y"); // ???????????? ??????
			} else {
				map.put("orderAllConfirmYn", "N");
			}

			// ?????? ?????? ?????? ????????????(???????????? ??????)????????? ?????????????????? ?????? ?????? ??????
			if ((orderTotalCnt == orderReadlyCnt) && orderClaimCnt == 0) {
				map.put("orderAllCancelYn", "Y"); // ?????? ?????? ??????
			} else {
				map.put("orderAllCancelYn", "N");
			}

		}

		return map;

	}

	/**
	 * 
	 * @Desc : ???????????? ?????? - I/F??????
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

		// ????????????
		OcOrder ocOrderDetail = ocOrderDao.selectOrderDetail(ocOrder);

		// ?????? ?????? ??????
		MbMember mbMemberParam = new MbMember();
		mbMemberParam.setMemberNo(ocOrderDetail.getMemberNo());
		MbMember mbMember = memberService.selectMemberInfo(mbMemberParam);

		// ????????????
		OcOrderProduct ocOrderProduct = new OcOrderProduct();
		ocOrderProduct.setOrderNo(ocOrder.getOrderNo());
		List<OcOrderProduct> productList = ocOrderProductDao.selectOrderProductByOrderNo(ocOrderProduct); // ???????????? ?????? ??????

		Map<String, Object> resultMap = this.orderStatActionValue(ocOrder.getOrderNo());

		// ???????????? ??????
		String orderAllConfirmYn = "N";

		// ???????????? ??????
		if (!resultMap.isEmpty()) {
			orderAllConfirmYn = resultMap.get("orderAllConfirmYn").toString();

			// ???????????? ???????????????
			if (StringUtils.equals(orderAllConfirmYn, "N")) {
				throw new Exception("???????????? ?????? ??????");
			}
		}

		ocOrderProduct.setModerNo(ocOrder.getModerNo()); // ???????????????
		ocOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_BUY_CONFIRM); // ????????????
		ocOrderProduct.setWhereOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_DELIVERY_FINISH); // ????????????

		try {

			// ?????? ????????????
			int prdUpdateCnt = ocOrderProductDao.updateOrderConfirm(ocOrderProduct);

			if (prdUpdateCnt > 0) {

				OcOrderDeliveryHistory ocOrderDeliveryHistory = new OcOrderDeliveryHistory();
				ocOrderDeliveryHistory.setOrderNo(ocOrder.getOrderNo());
				ocOrderDeliveryHistory.setModerNo(ocOrder.getModerNo()); //
				ocOrderDeliveryHistory.setDlvyStatCode(CommonCode.DLVY_STAT_CODE_BUY_CONFIRM); // ????????????
				ocOrderDeliveryHistory.setWhereDlvyStatCode(CommonCode.DLVY_STAT_CODE_MISS_DELIVERY); // ????????????

				// ?????? ????????????
				int dlyUpdateCnt = ocOrderDeliveryHistoryDao.updateOrderConfirm(ocOrderDeliveryHistory);

				// ?????? ????????? ?????? ??????
				int mstCnt = ocOrderDao.updateOrderDcsnDtm(ocOrder);

				if (dlyUpdateCnt > 0) {
					// ?????? ?????? ??????
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
							prdtHistory.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_BUY_CONFIRM); // ????????????
							prdtHistory.setNoteText("???????????? ??????"); // NOTE_TEXT
							prdtHistory.setRgsterNo(ocOrder.getModerNo());

							ocOrderProductHistoryDao.insertProductHistory(prdtHistory);
						}
					} // end for
				}
			}

			// ??????????????? ??????????????? [memA890a]
			List<BuyFixProduct> buyFixProduct = membershipPointService.buyFixRequest(mbMember.getSafeKey(),
					ocOrderDetail.getOrderNo());

			/*
			 * ?????? ?????? ??????
			 */

			/*
			 * ???????????? SMS
			 */

			rtnValue = Const.BOOLEAN_TRUE;

		} catch (Exception e) {
			rtnValue = Const.BOOLEAN_FALSE;
			map.put("successYn", rtnValue);
			map.put("message", "???????????? ???????????? ??????" + e.getMessage());
			return map;

		}

		map.put("successYn", rtnValue);
		return map;
	}

	/*************************************************************************************************
	 * nbk end
	 *************************************************************************************************/

}
