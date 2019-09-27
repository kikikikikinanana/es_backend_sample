package kr.co.shop.web.claim.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.member.MembershipPointService;
import kr.co.shop.interfaces.module.payment.PaymentEntrance;
import kr.co.shop.interfaces.module.payment.base.PaymentException;
import kr.co.shop.interfaces.module.payment.base.PaymentResult;
import kr.co.shop.interfaces.module.payment.base.model.PaymentInfo;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentCancel;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancel;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentRefund;
import kr.co.shop.interfaces.module.payment.naver.model.NaverPaymentCancel;
import kr.co.shop.interfaces.module.payment.nice.NiceGiftService;
import kr.co.shop.interfaces.module.payment.nice.model.ChargeRequest;
import kr.co.shop.interfaces.module.payment.nice.model.ChargeResponse;
import kr.co.shop.interfaces.module.payment.nice.model.CommNiceRes;
import kr.co.shop.interfaces.zinterfacesdb.service.InterfacesClaimService;
import kr.co.shop.web.claim.model.master.OcClaim;
import kr.co.shop.web.claim.model.master.OcClaimPayment;
import kr.co.shop.web.claim.model.master.OcClaimProduct;
import kr.co.shop.web.claim.model.master.OcClaimProductApplyPromotion;
import kr.co.shop.web.claim.model.master.OcClaimStatusHistory;
import kr.co.shop.web.claim.repository.master.OcClaimDao;
import kr.co.shop.web.claim.repository.master.OcClaimPaymentDao;
import kr.co.shop.web.claim.repository.master.OcClaimProductApplyPromotionDao;
import kr.co.shop.web.claim.repository.master.OcClaimProductDao;
import kr.co.shop.web.claim.repository.master.OcClaimStatusHistoryDao;
import kr.co.shop.web.claim.vo.OcClaimAmountVO;
import kr.co.shop.web.claim.vo.OcClaimPromoVO;
import kr.co.shop.web.customer.repository.master.CmOnlineMemberPolicyDao;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.repository.master.MbMemberCouponDao;
import kr.co.shop.web.order.model.master.OcCashReceipt;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderPayment;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.model.master.OcOrderProductApplyPromotion;
import kr.co.shop.web.order.model.master.OcOrderProductHistory;
import kr.co.shop.web.order.model.master.OcOrderUseCoupon;
import kr.co.shop.web.order.repository.master.OcCashReceiptDao;
import kr.co.shop.web.order.repository.master.OcOrderDao;
import kr.co.shop.web.order.repository.master.OcOrderPaymentDao;
import kr.co.shop.web.order.repository.master.OcOrderProductApplyPromotionDao;
import kr.co.shop.web.order.repository.master.OcOrderProductDao;
import kr.co.shop.web.order.repository.master.OcOrderProductHistoryDao;
import kr.co.shop.web.order.repository.master.OcOrderUseCouponDao;
import kr.co.shop.web.order.service.OrderService;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.repository.master.PdProductDao;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.model.master.PrPromotionMultiBuyDiscount;
import kr.co.shop.web.promotion.repository.master.PrCouponDao;
import kr.co.shop.web.promotion.repository.master.PrPromotionMultiBuyDiscountDao;
import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.repository.master.SySiteDao;
import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.repository.master.VdVendorDao;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 클레임 처리(취소,교환,반품)와 관련된 기능 및 비즈니스 프로세스 관련 Service
 * @FileName : ClaimProcService.java
 * @Project : shop.bo
 * @Date : 2019. 2. 20.
 * @Author : KTH
 */
@Slf4j
@Service
public class ClaimProcService extends BaseController {
	@Autowired
	private PaymentEntrance paymentEntrance;

	@Autowired
	private OcClaimProductDao ocClaimProductDao;

	@Autowired
	private OcClaimPaymentDao ocClaimPaymentDao;

	@Autowired
	private OcOrderPaymentDao ocOrderPaymentDao;

	@Autowired
	private OcOrderDao ocOrderDao;

	@Autowired
	private OcOrderProductDao ocOrderProductDao;

	@Autowired
	private OcOrderUseCouponDao ocOrderUseCouponDao;

	@Autowired
	private OcOrderProductApplyPromotionDao ocOrderProductApplyPromotionDao;

	@Autowired
	private VdVendorDao vdVendorDao;

	@Autowired
	private SySiteDao sySiteDao;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CmOnlineMemberPolicyDao cmOnlineMemberPolicyDao;

	@Autowired
	private PrCouponDao prCouponDao;

	@Autowired
	private MbMemberCouponDao mbMemberCouponDao;

	@Autowired
	MembershipPointService membershipPointService;

	@Autowired
	InterfacesClaimService interfacesClaimService;

	@Autowired
	OcOrderProductHistoryDao ocOrderProductHistoryDao;

	@Autowired
	OcClaimDao ocClaimDao;

	@Autowired
	OcClaimStatusHistoryDao ocClaimStatusHistoryDao;

	@Autowired
	PdProductDao pdProductDao;

	@Autowired
	OcClaimProductApplyPromotionDao ocClaimProductApplyPromotionDao;

	@Autowired
	PrPromotionMultiBuyDiscountDao prPromotionMultiBuyDiscountDao;

	@Autowired
	NiceGiftService niceGiftService;

	@Autowired
	OcCashReceiptDao ocCashReceiptDao;

	/**
	 * <pre>
	 * 환불 불가능 코드(KCP)
	 * - 해당 코드를 반환할 경우 예치금 환불 처리함
	 * </pre>
	 */
	@SuppressWarnings("serial")
	private final static Set<String> nonRefundableCodeKCP = new HashSet<String>() {
		{
			add("8220");
			add("AC94");
			add("8223"); // 휴대폰결제
							// 당월이전거래
							// 취소불가
			add("8142"); // 부분매입취소 불가 카드
			add("MCAL"); // 취소불가 거래입니다(승인시 정보와 불일치)
			add("8999"); // KCP로 문의
		}
	};

	public void setAllCancelCheck(OcClaim ocClaim, OcClaimAmountVO ocClaimAmountVO) throws Exception {
		/*
		 * [원 주문상품 목록 - 사은품/배송비 포함]
		 */
		OcOrderProduct ocOrderProduct = new OcOrderProduct();
		ocOrderProduct.setPrdtTypeCodeGift(CommonCode.PRDT_TYPE_CODE_GIFT); // 사은품 추출용
		ocOrderProduct.setOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준

		List<OcOrderProduct> orgOrderProductList = ocOrderProductDao.selectOrgOrderProductList(ocOrderProduct);

		ocClaimAmountVO.setOrgOrderProductList(orgOrderProductList);

		/*
		 * [원 주문에 걸린 모든 클레임 - 사은품/배송비 포함]
		 */
		OcClaimProduct ocClaimProduct = new OcClaimProduct();
		ocClaimProduct.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		ocClaimProduct.setPrdtTypeCodeGift(CommonCode.PRDT_TYPE_CODE_GIFT); // 사은품 상품 코드

		List<OcClaimProduct> orderAllClaimProductList = ocClaimProductDao.selectOrgClaimProductList(ocClaimProduct);

		ocClaimAmountVO.setOrderAllClaimProductList(orderAllClaimProductList);

		/*
		 * 전체취소 여부
		 */
		// 원 주문 상품 개수(사은품/배송비 제외)
		long orgOrderProductCnt = orgOrderProductList.stream()
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.count();

		// 완료된 클레임 개수(반품/취소 기준, 사은품/배송비 제외)
		long finishClaimCnt = orderAllClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getClmGbnCode(), CommonCode.CLM_GBN_CODE_CANCEL)
						|| UtilsText.equals(x.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.filter(x -> UtilsText.equals(x.getClmPrdtStatCode(),
						CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REDEPTION_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REFUND_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_CANCEL_FINISH)
						|| UtilsText.equals(x.getClmPrdtStatCode(),
								CommonCode.CLM_PRDT_STAT_CODE_RETURN_REDEPTION_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_RETURN_REFUND_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_RETURN_FINISH))
				.count();

		// 현재 클레임 개수(사은품/배송비 제외)
		long thisTimeClaimCnt = ocClaim.getOcClaimProductList().size();

		// 주문 전체 취소 여부
		if (orgOrderProductCnt == thisTimeClaimCnt) {
			ocClaimAmountVO.setAllCancelYn(true);
		} else {
			ocClaimAmountVO.setAllCancelYn(false);

			// 주문 전체 취소가 아닌 경우 이전 부분 취소 후 남은 상품 모두 취소인지 여부
			if (orgOrderProductCnt == finishClaimCnt + thisTimeClaimCnt) {
				ocClaimAmountVO.setRemainAllCancelYn(true);
			} else {
				ocClaimAmountVO.setRemainAllCancelYn(false);
			}
		}
	}

	/**
	 * @Desc : 전체취소 클레임 금액 계산
	 * @Method Name : setClaimAmountCalcForAllCancel
	 * @Date : 2019. 4. 11.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocOrder
	 * @param ocClaimProducts
	 * @return
	 */
	public OcClaimAmountVO setClaimAmountCalcForAllCancel(OcClaim ocClaim, OcOrder ocOrder,
			OcClaimAmountVO ocClaimAmountVO) throws Exception {

		/*
		 * [원 주문에 걸린 모든 클레임] - 클레임 상품 등록 후 내용을 다시 갱신
		 */
		OcClaimProduct ocClaimProduct = new OcClaimProduct();
		ocClaimProduct.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		ocClaimProduct.setPrdtTypeCodeGift(CommonCode.PRDT_TYPE_CODE_GIFT); // 사은품 상품 코드

		List<OcClaimProduct> orderAllClaimProductList = ocClaimProductDao.selectOrgClaimProductList(ocClaimProduct);

		ocClaimAmountVO.setOrderAllClaimProductList(orderAllClaimProductList); // 다시 set

		/*
		 * [현재 클레임 상품 목록] - 결제 취소 후처리 부분취소와 같이 쓰기 위해 set
		 */
		ocClaimAmountVO.setThisTimeClaimProductList(orderAllClaimProductList);

		/*
		 * [원 주문 상품 쿠폰 사용 정보]
		 */
		OcOrderUseCoupon useCoupon = new OcOrderUseCoupon();
		useCoupon.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준

		List<OcOrderUseCoupon> orderUseCouponList = ocOrderUseCouponDao.selectOrderUserCouponForClaimList(useCoupon);

		ocClaimAmountVO.setOrderUseCouponList(orderUseCouponList);

		/*
		 * [현재 클레임 상품 목록]
		 */
		List<OcClaimProduct> claimProductList = orderAllClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getClmNo(), ocClaim.getClmNo())).collect(Collectors.toList());

		/*
		 * [클레임 취소가능 잔여금액 목록]
		 */
		OcClaimPayment ocOrderClaimPayment = new OcClaimPayment();
		ocOrderClaimPayment.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		ocOrderClaimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 재경팀처리 제외
		ocOrderClaimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_PUBLIC); // 일반결제
		ocOrderClaimPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REFUND); // 환불

		List<OcClaimPayment> orderClaimPaymentList = ocClaimPaymentDao.selectOrderClaimPaymentList(ocOrderClaimPayment);

		ocClaimAmountVO.setOrderClaimPaymentList(orderClaimPaymentList);

		/*
		 * 클레임 금액 기본 계산
		 */
		// 현재 클레임으로 인해 취소될 금액(=상품 클레임 금액)
		int expectCnclAmt = claimProductList.stream().mapToInt(o -> o.getOrderAmt()).sum();

		ocClaimAmountVO.setExpectCnclAmt(expectCnclAmt);

		OcClaimPayment mainPayment = null;
		OcClaimPayment giftPayment = null;
		OcClaimPayment pointPayment = null;
		OcClaimPayment eventPointPayment = null;

		for (OcClaimPayment claimPayment : orderClaimPaymentList) {
			if (UtilsText.equals(claimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_MOBILE_GIFT)) { // 기프트
				giftPayment = claimPayment;
			} else if (UtilsText.equals(claimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_BUY_POINT)) { // 구매포인트
				pointPayment = claimPayment;
			} else if (UtilsText.equals(claimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_EVENT_POINT)) { // 이벤트포인트
				eventPointPayment = claimPayment;
			} else {
				mainPayment = claimPayment;
			}
		}

		ocClaimAmountVO.setMainPayment(mainPayment);
		ocClaimAmountVO.setGiftPayment(giftPayment);
		ocClaimAmountVO.setPointPayment(pointPayment);
		ocClaimAmountVO.setEventPointPayment(eventPointPayment);

		return ocClaimAmountVO;
	}

	/**
	 * @Desc : 부분취소 클레임 금액 계산
	 * @Method Name : setClaimAmountCalcForPartCancel
	 * @Date : 2019. 4. 11.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocOrder
	 * @param ocClaimProducts
	 * @return
	 */
	public OcClaimAmountVO setClaimAmountCalcForPartCancel(OcClaim ocClaim, OcOrder ocOrder,
			OcClaimAmountVO ocClaimAmountVO) throws Exception {

		/*
		 * 원 주문 상품 목록(사은품/배송비 포함) - 이전 전체취소 여부 확인에서 담겨옴
		 */
		List<OcOrderProduct> orgOrderProductList = ocClaimAmountVO.getOrgOrderProductList();

		/*
		 * [원 주문에 걸린 모든 클레임] - 클레임 상품 등록 후 내용을 다시 갱신, 현재 클레임은 사은품만 포함, 배송비는 이후 별도 저장
		 */
		OcClaimProduct ocClaimProduct = new OcClaimProduct();
		ocClaimProduct.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		ocClaimProduct.setPrdtTypeCodeGift(CommonCode.PRDT_TYPE_CODE_GIFT); // 사은품 상품 코드

		List<OcClaimProduct> orderAllClaimProductList = ocClaimProductDao.selectOrgClaimProductList(ocClaimProduct);

		ocClaimAmountVO.setOrderAllClaimProductList(orderAllClaimProductList); // 다시 set

		/*
		 * [원 주문 상품 결제 정보]
		 */
		OcOrderPayment ocOrderPayment = new OcOrderPayment();
		ocOrderPayment.setOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준

		List<OcOrderPayment> orderPlaymentList = ocOrderPaymentDao.selectPaymentList(ocOrderPayment);

		/*
		 * [원 주문 상품 쿠폰 사용 정보]
		 */
		OcOrderUseCoupon useCoupon = new OcOrderUseCoupon();
		useCoupon.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준

		List<OcOrderUseCoupon> orderUseCouponList = ocOrderUseCouponDao.selectOrderUserCouponForClaimList(useCoupon);

		ocClaimAmountVO.setOrderUseCouponList(orderUseCouponList);

		/*
		 * [더블적립 쿠폰 사용 내역]
		 */
		OcOrderUseCoupon doubleDscntCpn = orderUseCouponList.stream()
				.filter(x -> UtilsText.equals(x.getCpnTypeCode(), CommonCode.CPN_TYPE_CODE_DOUBLE_POINT)).findFirst()
				.orElse(null);

		ocClaimAmountVO.setOrderDoubleDscntCpnInfo(doubleDscntCpn);

		/*
		 * [원 주문 상품 프로모션 정보]
		 */
		OcOrderProductApplyPromotion orderApplyPromotion = new OcOrderProductApplyPromotion();
		orderApplyPromotion.setOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준

		List<OcOrderProductApplyPromotion> orderApplyPromotionList = ocOrderProductApplyPromotionDao
				.select(orderApplyPromotion);

		/*
		 * [기존 완료 클레임 상품 목록 - 사은품/배송비 포함]
		 */
		List<OcClaimProduct> finishedClaimProductList = orderAllClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getClmGbnCode(), CommonCode.CLM_GBN_CODE_CANCEL)
						|| UtilsText.equals(x.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN))
				.filter(x -> UtilsText.equals(x.getClmPrdtStatCode(),
						CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REDEPTION_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REFUND_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_CANCEL_FINISH)
						|| UtilsText.equals(x.getClmPrdtStatCode(),
								CommonCode.CLM_PRDT_STAT_CODE_RETURN_REDEPTION_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_RETURN_REFUND_ACCEPT)
						|| UtilsText.equals(x.getClmPrdtStatCode(), CommonCode.CLM_PRDT_STAT_CODE_RETURN_FINISH))
				.collect(Collectors.toList());

		/*
		 * [현재 클레임 상품 목록 - 사은품 포함] - 현재 클레임은 사은품만 포함, 배송비는 환불 배송비 판별 후 포함
		 */
		List<OcClaimProduct> thisTimeClaimProductList = orderAllClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getClmNo(), ocClaim.getClmNo())).collect(Collectors.toList());

		ocClaimAmountVO.setThisTimeClaimProductList(thisTimeClaimProductList);
		log.debug("######################### claimProductList : {} ", thisTimeClaimProductList);

		/*
		 * [자사/업체별 원주문 상품 목록]
		 */
		// 자사 원주문 상품 목록
		List<OcOrderProduct> mmnyOrgOrderProductList = orgOrderProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).collect(Collectors.toList());

		// 업체 원주문 상품 목록
		List<OcOrderProduct> vndrOrgOrderProductList = orgOrderProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE)).collect(Collectors.toList());

		/*
		 * [자사/업체별 기존 완료 클레임 상품 목록]
		 */
		// 자사 기존 완료 클레임 상품 목록
		List<OcClaimProduct> mmnyFinishedClaimProductList = finishedClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).collect(Collectors.toList());

		// 업체 기존 완료 클레임 상품 목록
		List<OcClaimProduct> vndrFinishedClaimProductList = finishedClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE)).collect(Collectors.toList());

		/*
		 * [자사/업체별 현재 클레임 상품 목록]
		 */
		// 자사 현재 클레임 상품 목록
		List<OcClaimProduct> mmnyThisTimeClaimProductList = thisTimeClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).collect(Collectors.toList());

		// 현재 클레임 대상 자사 업체코드 목록
		List<String> mmnyVndrNoList = mmnyThisTimeClaimProductList.stream().map(OcClaimProduct::getVndrNo).distinct()
				.collect(Collectors.toCollection(ArrayList::new));

		// 업체 현재 클레임 상품 목록
		List<OcClaimProduct> vndrThisTimeClaimProductList = thisTimeClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE)).collect(Collectors.toList());

		// 현재 클레임 대상 업체코드 목록
		List<String> vndrNoList = vndrThisTimeClaimProductList.stream().map(OcClaimProduct::getVndrNo).distinct()
				.collect(Collectors.toCollection(ArrayList::new));

		/*
		 * [클레임 결제 정보 - 원주문 기준 전체]
		 */
		OcClaimPayment ocClaimPayment = new OcClaimPayment();
		ocClaimPayment.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준

		List<OcClaimPayment> claimAllPaymentList = ocClaimPaymentDao.selectClaimPaymentList(ocClaimPayment);

		/*
		 * [클레임 취소가능 잔여금액 목록]
		 */
		OcClaimPayment ocOrderClaimPayment = new OcClaimPayment();
		ocOrderClaimPayment.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		ocOrderClaimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 재경팀처리 제외
		ocOrderClaimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_PUBLIC); // 일반결제
		ocOrderClaimPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REFUND); // 환불
		ocOrderClaimPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_AMT); // 주문금
		ocOrderClaimPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL); // 주문취소(결제취소)

		List<OcClaimPayment> orderClaimPaymentList = ocClaimPaymentDao.selectOrderClaimPaymentList(ocOrderClaimPayment);

		ocClaimAmountVO.setOrderClaimPaymentList(orderClaimPaymentList);

		/*
		 * [사이트 정책 정보]
		 */
		SySite sySite = sySiteDao.selectSite(ocOrder.getSiteNo());

		/*
		 * [배송비상품 조회]
		 */
		List<PdProduct> dlvyProductList = pdProductDao.selectProductDlvy(CommonCode.PRDT_TYPE_CODE_DELIVERY);

		// 자사 배송비 상품
		PdProduct mmnyDlvyProduct = (PdProduct) dlvyProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).findFirst().orElse(null);

		// 업체 배송비 상품
		PdProduct vndrDlvyProduct = (PdProduct) dlvyProductList.stream()
				.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE)).findFirst().orElse(null);

		// 기존 총 취소된 금액(클레임으로 인해 누적된 취소금)
		int accumulatedCnclAmt = claimAllPaymentList.stream()
				.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REFUND)) // 환불
				.filter(x -> !UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE)) // 재경팀처리 제외
				.filter(x -> !UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC)) // 일반결제
				.mapToInt(o -> o.getPymntAmt()).sum();

		ocClaimAmountVO.setTotalAccumulatedCnclAmt(accumulatedCnclAmt);
		log.debug("######################### accumulatedCnclAmt : {} ", accumulatedCnclAmt);

		/*
		 * [원 주문에 걸린 다족구매 프로모션 현황 조회(현재 클레임 포함)]
		 */
		String[] clmPrdtStatCodesForMultiBuy = { CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REDEPTION_ACCEPT,
				CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REFUND_ACCEPT, CommonCode.CLM_PRDT_STAT_CODE_CANCEL_FINISH };
		OcClaimProduct multiBuyProduct = new OcClaimProduct();
		multiBuyProduct.setClmNo(ocClaim.getClmNo());
		multiBuyProduct.setOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		multiBuyProduct.setPromoTypeCode(CommonCode.PROMO_TYPE_CODE_DISCOUNT_MULTI_SHOUES); // 프로모션유형 : 다족구매
		multiBuyProduct.setClmPrdtStatCodes(clmPrdtStatCodesForMultiBuy);

		List<OcClaimPromoVO> orderMultiBuyPromoCheckList = ocClaimProductDao
				.selectOrderMultiBuyPromoCheckList(multiBuyProduct);
		log.debug("######################### orderMultiBuyPromoCheckList : {} ", orderMultiBuyPromoCheckList);

		/*
		 * 다족구매 프로모션 체크
		 */
		// 주문기준 총 결제금액(=최초결제금액)
		ocClaimAmountVO.setTotalOrderPymntAmt(ocOrder.getPymntAmt());
		// 주문기준 총 취소된 금액
		ocClaimAmountVO.setTotalOrderCnclAmt(ocOrder.getCnclAmt());
		// 총 잔여 취소가능금액
		ocClaimAmountVO.setTotalCnclAbleRemainAmt(
				ocClaimAmountVO.getTotalOrderPymntAmt() - ocClaimAmountVO.getTotalOrderCnclAmt());
		log.debug("######################### PymntAmt : {} ", ocOrder.getPymntAmt());
		log.debug("######################### CnclAmt : {} ", ocOrder.getCnclAmt());

		// 다족구매로 인한 금액 재조정 되기 이전의 주문상품 목록
		List<OcOrderProduct> beforeAdjustOrderProductByMultiBuyList = new ArrayList<>();
		// 다족구매로 인한 금액 재조정 된 주문상품 목록
		List<OcOrderProduct> adjustedOrderProductByMultiBuyList = new ArrayList<>();
		// 다족구매로 인한 금액 재조정 된 주문상품 프로모션 변경 목록
		List<OcClaimProductApplyPromotion> adjustedClaimProductApplyPromotionList = new ArrayList<>();
		// 다족구매 클레임으로 인해 클레임 진행 후 주문에 남아있어야 할 결제금액
		int totalOrderRemainAmtByMultiBuy = 0;
		// 다족구매 클레임으로 발생된 환수금
		int redempAmtByMultiBuy = 0;

		// 자사상품 다족구매 프로모션 체크
		if (!ObjectUtils.isEmpty(mmnyThisTimeClaimProductList)) {
			// 현재 클레임 다족구매 프로모션 상품 목록
			List<OcClaimPromoVO> _thisTimeMultiBuyProductList = orderMultiBuyPromoCheckList.stream()
					.filter(x -> UtilsText.equals(x.getNowClmNo(), ocClaim.getClmNo()))
					.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE)).collect(Collectors.toList());

			// 다족구매 프로모션이 적용된 클레임인 경우
			if (!ObjectUtils.isEmpty(_thisTimeMultiBuyProductList)) {
				// 현재 클레임 상품(사은품/배송비 제외)
				List<OcClaimProduct> _thisTimeclaimProductList = mmnyThisTimeClaimProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.collect(Collectors.toList());

				// 현재 클레임 개수(사은품/배송비 제외)
				long _thisTimeClaimCnt = _thisTimeclaimProductList.size();

				// 원 주문 상품 개수(사은품/배송비 제외)
				long _orderProductCnt = mmnyOrgOrderProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.count();

				// 완료된 클레임 개수(사은품/배송비 제외)
				long _finishClaimCnt = mmnyFinishedClaimProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.count();

				// 클레임 상품에 적용된 다족구매 프로모션 할인률 정보 목록
				PrPromotionMultiBuyDiscount _prPromotionMultiBuyDiscount = new PrPromotionMultiBuyDiscount();
				_prPromotionMultiBuyDiscount.setPromoNo(_thisTimeMultiBuyProductList.get(0).getPromoNo());

				List<PrPromotionMultiBuyDiscount> _multiBuyDiscountList = prPromotionMultiBuyDiscountDao
						.select(_prPromotionMultiBuyDiscount);

				log.debug("######################### _multiBuyDiscountList : {} ", _multiBuyDiscountList);

				// 다족구매 프로모션에 등록된 최대 개수의 할인 정보
				PrPromotionMultiBuyDiscount _maxMultiBuyDiscountInfo = (PrPromotionMultiBuyDiscount) _multiBuyDiscountList
						.stream().max(Comparator.comparing(PrPromotionMultiBuyDiscount::getBuyQty)).orElse(null);

				// 다족구매 할인 재적용 상품 목록(기존 클레임, 현재 클레임 상품 제외)
				List<OcClaimPromoVO> _adjustMultiBuyProductTargetList = orderMultiBuyPromoCheckList.stream()
						.filter(x -> UtilsText.isEmpty(x.getNowClmNo()))
						.filter(x -> UtilsText.isEmpty(x.getCanceledClmNo())).collect(Collectors.toList());

				// 다족구매 할인 재적용 상품 개수
				int _reApplyMultiBuyProductCnt = ObjectUtils.isEmpty(_adjustMultiBuyProductTargetList) ? 0
						: _adjustMultiBuyProductTargetList.size();

				// 새로 적용해야 할 다족구매 프로모션 할인 정보 확인
				PrPromotionMultiBuyDiscount _reApplyMultiBuyDiscountInfo = (PrPromotionMultiBuyDiscount) _multiBuyDiscountList
						.stream().filter(x -> x.getBuyQty() == _reApplyMultiBuyProductCnt).findFirst().orElse(null);

				// 현재 클레임 기준 자사상품 전체 취소가 아니며, 이전 부분 취소 후 남은 상품 모두 취소가 아닌 경우(부분 취소 클레임)
				if ((_orderProductCnt != _thisTimeClaimCnt)
						&& (_orderProductCnt != _finishClaimCnt + _thisTimeClaimCnt)) {
					// 주문 상품 중 다족구매 할인 재적용 상품 존재(없는 경우는 현재 상품 금액 그대로 취소)
					if (_reApplyMultiBuyProductCnt > 0) {
						// 다족구매 할인 재적용 상품이 한 개인 경우 다족구매 할인금액 변경(다족구매 적용되지 않은 금액으로)
						if (_reApplyMultiBuyProductCnt == 1) {
							// 다족구매 재적용 상품이 1개인 경우는 할인 정보도 없으므로 할인정보 임의로 0 set
							_reApplyMultiBuyDiscountInfo = new PrPromotionMultiBuyDiscount();
							_reApplyMultiBuyDiscountInfo.setBuyQty(1);
							_reApplyMultiBuyDiscountInfo.setDscntRate((short) 0);
						} else {
							// 등록된 다족구매 프로모션 최대 지정 개수보다 재 적용해야 할 다족구매 할인 적용 상품 개수가 많은 경우
							if (_reApplyMultiBuyProductCnt > _maxMultiBuyDiscountInfo.getBuyQty()) {
								_reApplyMultiBuyDiscountInfo = _maxMultiBuyDiscountInfo;
							}
						}

						for (OcClaimPromoVO targetProduct : _adjustMultiBuyProductTargetList) {
							// 이전 클레임에 의해 재 조정된 다족구매 리오더 매출 주문 확인
							OcOrderProduct _beforeAdjustProduct = new OcOrderProduct();
							_beforeAdjustProduct.setOrderNo(targetProduct.getOrderNo());
							_beforeAdjustProduct.setOrderPrdtSeq(targetProduct.getOrderPrdtSeq());
							_beforeAdjustProduct.setSalesCnclGbnType(CommonCode.SALES_CNCL_GBN_TYPE_SALE);

							_beforeAdjustProduct = ocOrderProductDao
									.selectRecentMultiBuyReApplyProduct(_beforeAdjustProduct);

							// 이전 클레임으로 재 조정된 다족구매 매출 상품이 없으면 처음 조정 대상이므로 원 주문에서 해당 상품 추출
							if (_beforeAdjustProduct == null) {
								_beforeAdjustProduct = mmnyOrgOrderProductList.stream()
										.filter(x -> UtilsText.equals(x.getOrderNo(), targetProduct.getOrderNo()))
										.filter(x -> UtilsText.equals(String.valueOf(x.getOrderPrdtSeq()),
												String.valueOf(targetProduct.getOrderPrdtSeq())))
										.findFirst().orElse(null);
							}

							// 이전 클레임에 의해 재 조정된 다족구매 할인 금액 정보 확인
							OcClaimProductApplyPromotion _recentClaimProductApplyPromotion = new OcClaimProductApplyPromotion();
							_recentClaimProductApplyPromotion.setOrderPrdtSeq(targetProduct.getOrderPrdtSeq());

							_recentClaimProductApplyPromotion = ocClaimProductApplyPromotionDao
									.selectRecentClaimProductApplyPromotion(_recentClaimProductApplyPromotion);

							// 이전 클레임으로 재 조정된 다족구매 할인 금액 정보가 없으면 처음 조정 대상이므로 원 주문 기준의 프로모션 할인금 정보 추출
							if (_recentClaimProductApplyPromotion == null) {

								OcOrderProductApplyPromotion _recentOrderProductApplyPromotion = orderApplyPromotionList
										.stream()
										.filter(x -> UtilsText.equals(x.getOrderNo(), targetProduct.getOrderNo()))
										.filter(x -> UtilsText.equals(String.valueOf(x.getOrderPrdtSeq()),
												String.valueOf(targetProduct.getOrderPrdtSeq())))
										.filter(x -> UtilsText.equals(x.getPromoTypeCode(),
												CommonCode.PROMO_TYPE_CODE_DISCOUNT_MULTI_SHOUES))
										.findFirst().orElse(null);

								_recentClaimProductApplyPromotion = new OcClaimProductApplyPromotion();

								BeanUtils.copyProperties(_recentOrderProductApplyPromotion,
										_recentClaimProductApplyPromotion); // 내용 복사

								_recentClaimProductApplyPromotion.setClmNo(targetProduct.getClmNo());
								_recentClaimProductApplyPromotion.setClmPrdtSeq(targetProduct.getClmPrdtSeq());
							}

							int _prdtNormalAmt = _beforeAdjustProduct.getPrdtNormalAmt(); // 조정 대상상품 상품정상가
							int _prdtSellAmt = _beforeAdjustProduct.getPrdtSellAmt(); // 조정 대상상품 상품판매가
							int _optnAddAmt = _beforeAdjustProduct.getOptnAddAmt(); // 조정 대상상품 옵션추가금액
							int _sellAmt = _beforeAdjustProduct.getSellAmt(); // 조정 대상상품 판매가(상품판매가 + 옵션추가금)
							int _totalDscntAmt = _beforeAdjustProduct.getTotalDscntAmt(); // 조정 대상상품 할인금액합계
							int _cpnDscntAmt = _beforeAdjustProduct.getCpnDscntAmt(); // 조정 대상상품 쿠폰할인금액
							int _orderAmt = _beforeAdjustProduct.getOrderAmt(); // 조정 대상상품 주문금액
							int _multiBuyDscntAmt = _recentClaimProductApplyPromotion.getDscntAmt(); // 조정 대상상품 다족구매 할인금

							int _adjustMultiBuyDscntAmt = 0; // 조정된 할인금액
							int _adjustDscntPrdtAmt = 0; // 조정된 다족구매 할인이 적용된 상품 금액
							int _adjustTotalDscntAmt = 0; // 조정된 할인금액합계
							int _adjustOrderAmt = 0; // 조정된 주문금액
							int _differDscntAmt = 0; // 새롭게 적용된 상품할인금액과 기존 할인금액 차이

							int _newDscntRate = _reApplyMultiBuyDiscountInfo.getDscntRate(); // 새롭게 적용할 할인율

							// 새로 적용해야 할 할인율 퍼센티지를 이용하여 다족구매 할인금액 계산 - 옵션 추가금 포함
							_adjustMultiBuyDscntAmt = (int) Math
									.ceil((double) (_prdtNormalAmt + _optnAddAmt) * _newDscntRate / 10000) * 100;

							// 조정된 할인 금액이 조정 대상상품 할인금액과 같으면 대상 아님
							if (_adjustMultiBuyDscntAmt == _multiBuyDscntAmt) {
								continue;
							}

							// 조정된 할인금액 합계(조정된 할인금 + 정액쿠폰 할인금)
							_adjustTotalDscntAmt = _adjustMultiBuyDscntAmt + _cpnDscntAmt;

							// 조정된 주문금액(주문금 대상이므로 할인금 차이 금액을 증감)
							_adjustOrderAmt = _prdtNormalAmt - _totalDscntAmt;

							// 다족구매로 인한 금액 재조정 되기 이전의 주문상품 목록
							beforeAdjustOrderProductByMultiBuyList.add(_beforeAdjustProduct);

							OcOrderProduct _adjustedProduct = new OcOrderProduct();
							BeanUtils.copyProperties(_beforeAdjustProduct, _adjustedProduct); // 내용 복사

							_adjustedProduct.setTotalDscntAmt(_adjustTotalDscntAmt);
							_adjustedProduct.setOrderAmt(_adjustOrderAmt);

							// 다족구매로 인한 금액 재조정 된 주문상품 목록
							adjustedOrderProductByMultiBuyList.add(_adjustedProduct);

							// 다족구매로 인한 금액 재조정 된 주문상품 프로모션 변경 목록
							OcClaimProductApplyPromotion _adjustedClaimProductApplyPromotion = new OcClaimProductApplyPromotion();
							_adjustedClaimProductApplyPromotion.setClmNo(targetProduct.getClmNo());
							_adjustedClaimProductApplyPromotion.setClmPrdtSeq(targetProduct.getClmPrdtSeq());
							_adjustedClaimProductApplyPromotion
									.setApplyPromoSeq(_recentClaimProductApplyPromotion.getApplyPromoSeq());
							_adjustedClaimProductApplyPromotion
									.setPromoNo(_recentClaimProductApplyPromotion.getPromoNo());
							_adjustedClaimProductApplyPromotion
									.setPromoTypeCode(_recentClaimProductApplyPromotion.getPromoTypeCode());
							_adjustedClaimProductApplyPromotion
									.setDscntType(_recentClaimProductApplyPromotion.getDscntType());
							_adjustedClaimProductApplyPromotion.setDscntValue(_newDscntRate);
							_adjustedClaimProductApplyPromotion.setDscntAmt(_adjustMultiBuyDscntAmt);

							adjustedClaimProductApplyPromotionList.add(_adjustedClaimProductApplyPromotion);

							// 다족구매 클레임으로 발생된 환수금
							redempAmtByMultiBuy += (_adjustOrderAmt - _orderAmt);
						}
					}
				}
			}
		}

		// 업체상품 다족구매 프로모션 체크
		if (!ObjectUtils.isEmpty(vndrThisTimeClaimProductList)) {
			for (String vndrNo : vndrNoList) {
				// 현재 클레임 다족구매 프로모션 상품 목록
				List<OcClaimPromoVO> _thisTimeMultiBuyProductList = orderMultiBuyPromoCheckList.stream()
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).collect(Collectors.toList());

				// 다족구매 프로모션이 적용된 클레임인 경우
				if (!ObjectUtils.isEmpty(_thisTimeMultiBuyProductList)) {
					// 현재 클레임 상품(사은품/배송비 제외)
					List<OcClaimProduct> _thisTimeclaimProductList = vndrThisTimeClaimProductList.stream()
							.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
									&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
							.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).collect(Collectors.toList());

					// 현재 클레임 개수(사은품/배송비 제외)
					long _thisTimeClaimCnt = _thisTimeclaimProductList.size();

					// 원 주문 상품 개수(사은품/배송비 제외)
					long _orderProductCnt = vndrOrgOrderProductList.stream()
							.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
									&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
							.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

					// 완료된 클레임 개수(사은품/배송비 제외)
					long _finishClaimCnt = vndrFinishedClaimProductList.stream()
							.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
									&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
							.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

					// 클레임 상품에 적용된 다족구매 프로모션 할인률 정보 목록
					PrPromotionMultiBuyDiscount _prPromotionMultiBuyDiscount = new PrPromotionMultiBuyDiscount();
					_prPromotionMultiBuyDiscount.setPromoNo(_thisTimeMultiBuyProductList.get(0).getPromoNo());

					List<PrPromotionMultiBuyDiscount> _multiBuyDiscountList = prPromotionMultiBuyDiscountDao
							.select(_prPromotionMultiBuyDiscount);

					log.debug("######################### _multiBuyDiscountList : {} ", _multiBuyDiscountList);

					// 다족구매 프로모션에 등록된 최대 개수의 할인 정보
					PrPromotionMultiBuyDiscount _maxMultiBuyDiscountInfo = (PrPromotionMultiBuyDiscount) _multiBuyDiscountList
							.stream().max(Comparator.comparing(PrPromotionMultiBuyDiscount::getBuyQty)).orElse(null);

					// 다족구매 할인 재적용 상품 목록(기존 클레임, 현재 클레임 상품 제외)
					List<OcClaimPromoVO> _adjustMultiBuyProductTargetList = orderMultiBuyPromoCheckList.stream()
							.filter(x -> UtilsText.isEmpty(x.getNowClmNo()))
							.filter(x -> UtilsText.isEmpty(x.getCanceledClmNo())).collect(Collectors.toList());

					// 다족구매 할인 재적용 상품 개수
					int _reApplyMultiBuyProductCnt = ObjectUtils.isEmpty(_adjustMultiBuyProductTargetList) ? 0
							: _adjustMultiBuyProductTargetList.size();

					// 새로 적용해야 할 다족구매 프로모션 할인 정보 확인
					PrPromotionMultiBuyDiscount _reApplyMultiBuyDiscountInfo = (PrPromotionMultiBuyDiscount) _multiBuyDiscountList
							.stream().filter(x -> x.getBuyQty() == _reApplyMultiBuyProductCnt).findFirst().orElse(null);

					// 현재 클레임 기준 업체상품 전체 취소가 아니며, 이전 부분 취소 후 남은 상품 모두 취소가 아닌 경우(부분 취소 클레임)
					if ((_orderProductCnt != _thisTimeClaimCnt)
							&& (_orderProductCnt != _finishClaimCnt + _thisTimeClaimCnt)) {
						// 주문 상품 중 다족구매 할인 재적용 상품 존재(없는 경우는 현재 상품 금액 그대로 취소)
						if (_reApplyMultiBuyProductCnt > 0) {
							// 다족구매 할인 재적용 상품이 한 개인 경우 다족구매 할인금액 변경(다족구매 적용되지 않은 금액으로)
							if (_reApplyMultiBuyProductCnt == 1) {
								// 다족구매 재적용 상품이 1개인 경우는 할인 정보도 없으므로 할인정보 임의로 0 set
								_reApplyMultiBuyDiscountInfo = new PrPromotionMultiBuyDiscount();
								_reApplyMultiBuyDiscountInfo.setBuyQty(1);
								_reApplyMultiBuyDiscountInfo.setDscntRate((short) 0);
							} else {
								// 등록된 다족구매 프로모션 최대 지정 개수보다 재 적용해야 할 다족구매 할인 적용 상품 개수가 많은 경우
								if (_reApplyMultiBuyProductCnt > _maxMultiBuyDiscountInfo.getBuyQty()) {
									_reApplyMultiBuyDiscountInfo = _maxMultiBuyDiscountInfo;
								}
							}

							for (OcClaimPromoVO targetProduct : _adjustMultiBuyProductTargetList) {
								// 이전 클레임에 의해 재 조정된 다족구매 리오더 매출 주문 확인
								OcOrderProduct _beforeAdjustProduct = new OcOrderProduct();
								_beforeAdjustProduct.setOrderNo(targetProduct.getOrderNo());
								_beforeAdjustProduct.setOrderPrdtSeq(targetProduct.getOrderPrdtSeq());
								_beforeAdjustProduct.setSalesCnclGbnType(CommonCode.SALES_CNCL_GBN_TYPE_SALE);

								_beforeAdjustProduct = ocOrderProductDao
										.selectRecentMultiBuyReApplyProduct(_beforeAdjustProduct);

								// 이전 클레임으로 재 조정된 다족구매 매출 상품이 없으면 처음 조정 대상이므로 원 주문에서 해당 상품 추출
								if (_beforeAdjustProduct == null) {
									_beforeAdjustProduct = mmnyOrgOrderProductList.stream()
											.filter(x -> UtilsText.equals(x.getOrderNo(), targetProduct.getOrderNo()))
											.filter(x -> UtilsText.equals(String.valueOf(x.getOrderPrdtSeq()),
													String.valueOf(targetProduct.getOrderPrdtSeq())))
											.findFirst().orElse(null);
								}

								// 이전 클레임에 의해 재 조정된 다족구매 할인 금액 정보 확인
								OcClaimProductApplyPromotion _recentClaimProductApplyPromotion = new OcClaimProductApplyPromotion();
								_recentClaimProductApplyPromotion.setOrderPrdtSeq(targetProduct.getOrderPrdtSeq());

								_recentClaimProductApplyPromotion = ocClaimProductApplyPromotionDao
										.selectRecentClaimProductApplyPromotion(_recentClaimProductApplyPromotion);

								// 이전 클레임으로 재 조정된 다족구매 할인 금액 정보가 없으면 처음 조정 대상이므로 원 주문 기준의 프로모션 할인금 정보 추출
								if (_recentClaimProductApplyPromotion == null) {

									OcOrderProductApplyPromotion _recentOrderProductApplyPromotion = orderApplyPromotionList
											.stream()
											.filter(x -> UtilsText.equals(x.getOrderNo(), targetProduct.getOrderNo()))
											.filter(x -> UtilsText.equals(String.valueOf(x.getOrderPrdtSeq()),
													String.valueOf(targetProduct.getOrderPrdtSeq())))
											.filter(x -> UtilsText.equals(x.getPromoTypeCode(),
													CommonCode.PROMO_TYPE_CODE_DISCOUNT_MULTI_SHOUES))
											.findFirst().orElse(null);

									_recentClaimProductApplyPromotion = new OcClaimProductApplyPromotion();

									BeanUtils.copyProperties(_recentOrderProductApplyPromotion,
											_recentClaimProductApplyPromotion); // 내용 복사

									_recentClaimProductApplyPromotion.setClmNo(targetProduct.getClmNo());
									_recentClaimProductApplyPromotion.setClmPrdtSeq(targetProduct.getClmPrdtSeq());
								}

								int _prdtNormalAmt = _beforeAdjustProduct.getPrdtNormalAmt(); // 조정 대상상품 상품정상가
								int _prdtSellAmt = _beforeAdjustProduct.getPrdtSellAmt(); // 조정 대상상품 상품판매가
								int _optnAddAmt = _beforeAdjustProduct.getOptnAddAmt(); // 조정 대상상품 옵션추가금액
								int _sellAmt = _beforeAdjustProduct.getSellAmt(); // 조정 대상상품 판매가(상품판매가 + 옵션추가금)
								int _totalDscntAmt = _beforeAdjustProduct.getTotalDscntAmt(); // 조정 대상상품 할인금액합계
								int _cpnDscntAmt = _beforeAdjustProduct.getCpnDscntAmt(); // 조정 대상상품 쿠폰할인금액
								int _orderAmt = _beforeAdjustProduct.getOrderAmt(); // 조정 대상상품 주문금액
								int _multiBuyDscntAmt = _recentClaimProductApplyPromotion.getDscntAmt(); // 조정 대상상품 다족구매
																											// 할인금

								int _adjustMultiBuyDscntAmt = 0; // 조정된 할인금액
								int _adjustDscntPrdtAmt = 0; // 조정된 다족구매 할인이 적용된 상품 금액
								int _adjustTotalDscntAmt = 0; // 조정된 할인금액합계
								int _adjustOrderAmt = 0; // 조정된 주문금액
								int _differDscntAmt = 0; // 새롭게 적용된 상품할인금액과 기존 할인금액 차이

								int _newDscntRate = _reApplyMultiBuyDiscountInfo.getDscntRate(); // 새롭게 적용할 할인율

								// 새로 적용해야 할 할인율 퍼센티지를 이용하여 다족구매 할인금액 계산 - 옵션 추가금 포함
								_adjustMultiBuyDscntAmt = (int) Math
										.ceil((double) (_prdtNormalAmt + _optnAddAmt) * _newDscntRate / 10000) * 100;

								// 조정된 할인 금액이 조정 대상상품 할인금액과 같으면 대상 아님
								if (_adjustMultiBuyDscntAmt == _multiBuyDscntAmt) {
									continue;
								}

								// 조정된 할인금액 합계(조정된 할인금 + 정액쿠폰 할인금)
								_adjustTotalDscntAmt = _adjustMultiBuyDscntAmt + _cpnDscntAmt;

								// 조정된 주문금액(주문금 대상이므로 할인금 차이 금액을 증감)
								_adjustOrderAmt = _prdtNormalAmt - _totalDscntAmt;

								// 다족구매로 인한 금액 재조정 되기 이전의 주문상품 목록
								beforeAdjustOrderProductByMultiBuyList.add(_beforeAdjustProduct);

								OcOrderProduct _adjustedProduct = new OcOrderProduct();
								BeanUtils.copyProperties(_beforeAdjustProduct, _adjustedProduct); // 내용 복사

								_adjustedProduct.setTotalDscntAmt(_adjustTotalDscntAmt);
								_adjustedProduct.setOrderAmt(_adjustOrderAmt);

								// 다족구매로 인한 금액 재조정 된 주문상품 목록
								adjustedOrderProductByMultiBuyList.add(_adjustedProduct);

								// 다족구매로 인한 금액 재조정 된 주문상품 프로모션 변경 목록
								OcClaimProductApplyPromotion _adjustedClaimProductApplyPromotion = new OcClaimProductApplyPromotion();
								_adjustedClaimProductApplyPromotion.setClmNo(targetProduct.getClmNo());
								_adjustedClaimProductApplyPromotion.setClmPrdtSeq(targetProduct.getClmPrdtSeq());
								_adjustedClaimProductApplyPromotion
										.setApplyPromoSeq(_recentClaimProductApplyPromotion.getApplyPromoSeq());
								_adjustedClaimProductApplyPromotion
										.setPromoNo(_recentClaimProductApplyPromotion.getPromoNo());
								_adjustedClaimProductApplyPromotion
										.setPromoTypeCode(_recentClaimProductApplyPromotion.getPromoTypeCode());
								_adjustedClaimProductApplyPromotion
										.setDscntType(_recentClaimProductApplyPromotion.getDscntType());
								_adjustedClaimProductApplyPromotion.setDscntValue(_newDscntRate);
								_adjustedClaimProductApplyPromotion.setDscntAmt(_adjustMultiBuyDscntAmt);

								adjustedClaimProductApplyPromotionList.add(_adjustedClaimProductApplyPromotion);

								// 다족구매 클레임으로 발생된 환수금
								redempAmtByMultiBuy += (_adjustOrderAmt - _orderAmt);
							}
						}
					}
				}
			}
		}

		// 다족구매로 인한 금액 재조정 되기 이전의 주문상품 목록 vo set
		ocClaimAmountVO.setBeforeAdjustOrderProductByMultiBuyList(beforeAdjustOrderProductByMultiBuyList);
		// 다족구매로 인한 금액 재조정 된 주문상품 목록 vo set
		ocClaimAmountVO.setAdjustedOrderProductByMultiBuyList(adjustedOrderProductByMultiBuyList);
		// 다족구매로 인한 금액 재조정 된 주문상품 프로모션 변경 목록
		ocClaimAmountVO.setAdjustedClaimProductApplyPromotionList(adjustedClaimProductApplyPromotionList);
		// 환수프로모션비(다족구매로 인해 발생) vo set
		ocClaimAmountVO.setRedempAmtByMultiBuy(redempAmtByMultiBuy);
		log.debug("######################### beforeAdjustOrderProductByMultiBuyList : {} ",
				beforeAdjustOrderProductByMultiBuyList);
		log.debug("######################### adjustedOrderProductByMultiBuyList : {} ",
				adjustedOrderProductByMultiBuyList);
		log.debug("######################### redempAmtByMultiBuy : {} ", redempAmtByMultiBuy);

//		if (true) {
//			throw new Exception("임의에러");
//		}

		/*
		 * 배송비 체크
		 */
		int refundAmtByDlvyProduct = 0; // 환불배송비
		int redempAmtByFreeDlvyProduct = 0; // 무료배송상품 취소로 인한 배송비 발생금(환수배송비)
		List<OcOrderProduct> cancelDlvyProductList = new ArrayList<OcOrderProduct>(); // 취소 대상 배송비 상품
		List<OcOrderProduct> redempDlvyProductList = new ArrayList<OcOrderProduct>(); // 환수할 배송비 상품
		List<OcClaimPayment> vndrRedempDlvyPaymentList = new ArrayList<OcClaimPayment>(); // 업체별 환수 주문배송비

		// 자사상품 배송비 체크
		if (!ObjectUtils.isEmpty(mmnyThisTimeClaimProductList)) {
			// 현재 클레임 상품(사은품/배송비 제외)
			List<OcClaimProduct> _thisTimeclaimProductList = mmnyThisTimeClaimProductList.stream()
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
							&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
					.collect(Collectors.toList());

			// 현재 클레임 개수(사은품/배송비 제외)
			long _thisTimeClaimCnt = _thisTimeclaimProductList.size();

			// 배송비 상품
			OcOrderProduct _dlvyProduct = mmnyOrgOrderProductList.stream()
					.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)).findFirst()
					.orElse(null);

			// 기존 취소된 배송비 상품
			OcClaimProduct _canceledDlvyProduct = mmnyFinishedClaimProductList.stream()
					.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)).findFirst()
					.orElse(null);

			// 기존 환수된 배송비 여부
			long _redemptDlvyCnt = claimAllPaymentList.stream()
					.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP)) // 환수
					.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE)) // 재경팀처리 제외
					.filter(x -> UtilsText.equals(x.getOcrncRsnCode(), CommonCode.OCRNC_RSN_CODE_ORDER_DELIVERY_AMT)) // 주문배송비
					.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC)) // 일반결제
					.filter(x -> mmnyVndrNoList.contains(x.getVndrNo())) // 자사 업체번호
					.count();

			// 원 주문 상품 개수(사은품/배송비 제외)
			long _orderProductCnt = mmnyOrgOrderProductList.stream()
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
							&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
					.count();

			// 완료된 클레임 개수(사은품/배송비 제외)
			long _finishClaimCnt = mmnyFinishedClaimProductList.stream()
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
							&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
					.count();

			// 원 주문에 포함된 무료배송상품 개수
			long _orderClaimFreeDlvyProductCnt = mmnyOrgOrderProductList.stream()
					.filter(x -> UtilsText.equals(x.getFreeDlvyYn(), Const.BOOLEAN_TRUE)).count();

			// 완료된 클레임에 포함된 무료배송상품 개수
			long _finishClaimFreeDlvyProductCnt = mmnyFinishedClaimProductList.stream()
					.filter(x -> UtilsText.equals(x.getFreeDlvyYn(), Const.BOOLEAN_TRUE)).count();

			// 현재 클레임 무료배송상품 개수
			long _thisClaimFreeDlvyProductCnt = mmnyThisTimeClaimProductList.stream()
					.filter(x -> UtilsText.equals(x.getFreeDlvyYn(), Const.BOOLEAN_TRUE)).count();

			// 완료/진행 클레임의 무료배송상품 남은 개수를 확인
			long _remainFreeDlvyProductCnt = _orderClaimFreeDlvyProductCnt - _finishClaimFreeDlvyProductCnt
					- _thisClaimFreeDlvyProductCnt;

			// 원 주문 금액 sum(사은품/배송비 제외)
			int _orderAmtSum = mmnyOrgOrderProductList.stream()
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
							&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
					.mapToInt(x -> x.getOrderAmt()).sum();

			// 완료 클레임 금액 sum(사은품/배송비 제외)
			int _canceledAmtSum = mmnyFinishedClaimProductList.stream()
					.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
							&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
					.mapToInt(x -> x.getOrderAmt()).sum();

			// 현재 클레임 금액 sum(사은품/배송비 제외)
			int _thisTiemCancelAmtSum = _thisTimeclaimProductList.stream().mapToInt(x -> x.getOrderAmt()).sum();

			// 환불배송비 처리
			// 현재 클레임 기준 자사상품 전체 취소, 이전 부분 취소 후 남은 상품 모두 취소인 경우
			if ((_orderProductCnt == _thisTimeClaimCnt) || (_orderProductCnt == _finishClaimCnt + _thisTimeClaimCnt)) {
				// 지불한 배송비가 있으면 클레임 상품 추가 및 환불 대상(배송비 쿠폰을 사용하던 안하던 주문 배송비 행 처리)
				if (_dlvyProduct != null) {
					cancelDlvyProductList.add(_dlvyProduct); // 취소될 배송비 상품 set
					refundAmtByDlvyProduct += _dlvyProduct.getOrderAmt(); // 환불배송비 set
				}
			}

			// 환수배송비 체크
			// 1.현재 클레임을 제외한 남은 무료배송상품이 없고
			// 2.기 환수된 배송비 없고
			// 3.남은상품 모두 취소 아니고
			// 4.배송비 면제금액보다 낮으면 발생
			if (_remainFreeDlvyProductCnt == 0 && _redemptDlvyCnt == 0
					&& (_orderProductCnt != _finishClaimCnt + _thisTimeClaimCnt)
					&& (_orderAmtSum - _canceledAmtSum - _thisTiemCancelAmtSum < sySite.getFreeDlvyStdrAmt())) {

				OcOrderProduct _redempDlvyProduct = new OcOrderProduct(); // 상품
				OcClaimPayment _redempDlvyAmt = new OcClaimPayment(); // 결제

				// 환수 배송비 상품정보 set
				_redempDlvyProduct.setOrderNo(ocClaim.getOrderNo()); // 주문번호
				_redempDlvyProduct.setPrdtNo(mmnyDlvyProduct.getPrdtNo()); // 상품번호
				_redempDlvyProduct.setPrdtOptnNo("001"); // 상품옵션번호
				_redempDlvyProduct.setOptnName(mmnyDlvyProduct.getPrdtName()); // 옵션명:상품명 대체
				_redempDlvyProduct.setPrdtName(mmnyDlvyProduct.getPrdtName()); // 상품명
				_redempDlvyProduct.setEngPrdtName(mmnyDlvyProduct.getEngPrdtName()); // 영문상품명
				_redempDlvyProduct.setPrdtTypeCode(mmnyDlvyProduct.getPrdtTypeCode()); // 상품유형코드
				_redempDlvyProduct.setStyleInfo(mmnyDlvyProduct.getStyleInfo()); // 스타일정보
				_redempDlvyProduct.setRsvPrdtYn(Const.BOOLEAN_FALSE); // 예약상품여부
				_redempDlvyProduct.setVndrNo(_thisTimeclaimProductList.get(0).getVndrNo()); // 업체번호:첫번째 상품의업체번호 기준
				_redempDlvyProduct.setVndrName(_thisTimeclaimProductList.get(0).getVndrName()); // 업체명:첫번째 상품의 업체번호기준
				_redempDlvyProduct.setChnnlNo(mmnyDlvyProduct.getChnnlNo()); // 채널번호
				_redempDlvyProduct.setMmnyPrdtYn(Const.BOOLEAN_TRUE); // 자사상품여부
				_redempDlvyProduct.setEmpDscntYn(Const.BOOLEAN_FALSE); // 임직원할인여부
				_redempDlvyProduct.setOrderMnfctYn(Const.BOOLEAN_FALSE); // 주문제작여부
				_redempDlvyProduct.setDprcExceptYn(Const.BOOLEAN_FALSE); // 감가제외여부
				_redempDlvyProduct.setFreeDlvyYn(Const.BOOLEAN_FALSE); // 무료배송여부
				_redempDlvyProduct.setOrderQty(1); // 주문수량
				_redempDlvyProduct.setPrdtNormalAmt(sySite.getDlvyAmt()); // 상품정상가
				_redempDlvyProduct.setPrdtSellAmt(sySite.getDlvyAmt()); // 상품판매가
				_redempDlvyProduct.setOptnAddAmt(0); // 옵션추가금액
				_redempDlvyProduct.setSellAmt(sySite.getDlvyAmt()); // 판매가
				_redempDlvyProduct.setEmpDscntRate((short) 0); // 임직원할인율
				_redempDlvyProduct.setEmpAmt(0); // 임직원가
				_redempDlvyProduct.setTotalDscntAmt(0); // 할인금액합계
				_redempDlvyProduct.setCpnDscntAmt(0); // 쿠폰할인금액
				_redempDlvyProduct.setOrderAmt(sySite.getDlvyAmt()); // 주문금액
				_redempDlvyProduct.setVndrCmsnRate((short) 0); // 업체수수료율
				_redempDlvyProduct.setSellCnclReqYn(Const.BOOLEAN_FALSE); // 판매취소요청여부
				_redempDlvyProduct.setLogisCnvrtYn(Const.BOOLEAN_FALSE); // 택배전환여부
				_redempDlvyProduct.setPickupExtsnYn(Const.BOOLEAN_FALSE); // 픽업연장여부
				_redempDlvyProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_COMPLETE); // 주문상품상태코드:결제완료
				_redempDlvyProduct.setGenderGbnCode(mmnyDlvyProduct.getGenderGbnCode());
				_redempDlvyProduct.setBuyPointSaveRate((short) 0); // 구매포인트적립율
				_redempDlvyProduct.setRgsterNo(ocClaim.getMemberNo());
				_redempDlvyProduct.setModerNo(ocClaim.getMemberNo());

				redempDlvyProductList.add(_redempDlvyProduct); // 환수할 배송비 상품 set

				// 결제정보 set(히스토리용 이기 때문에 실결제와 관련된 내용은 의미없어도 무방)
				_redempDlvyAmt.setClmNo(ocClaim.getClmNo());
				_redempDlvyAmt.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP); // 환수환불구분:환수
				_redempDlvyAmt.setPymntDtm(new Timestamp(new Date().getTime())); // 결제일시
				_redempDlvyAmt.setDeviceCode(orderPlaymentList.get(0).getDeviceCode()); // 디바이스코드
				_redempDlvyAmt.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // 주결제수단여부
				_redempDlvyAmt.setPymntMeansCode(orderPlaymentList.get(0).getPymntMeansCode()); // 결제수단코드
				_redempDlvyAmt.setPymntVndrCode(null); // 결제업체코드
				_redempDlvyAmt.setInstmtTermCount((short) 0); // 할부기간
				_redempDlvyAmt.setPymntTodoAmt(sySite.getDlvyAmt()); // 결제예정금액
				_redempDlvyAmt.setPymntAmt(sySite.getDlvyAmt()); // 결제금액
				_redempDlvyAmt.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부
				_redempDlvyAmt.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부
				_redempDlvyAmt.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부
				_redempDlvyAmt.setAcntCrtfcYn(Const.BOOLEAN_FALSE); // 계좌인증여부
				_redempDlvyAmt.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 자사처리대상여부
				_redempDlvyAmt.setHistGbnType(CommonCode.HIST_GBN_TYPE_HISTORY); // 이력구분 - 이력근거용
				_redempDlvyAmt.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_DELIVERY_AMT); // 발생사유코드:주문배송비
				_redempDlvyAmt.setVndrNo(mmnyThisTimeClaimProductList.get(0).getVndrNo()); // 업체번호:자사업체코드
				_redempDlvyAmt.setRgsterNo(ocClaim.getRgsterNo()); // 등록자
				_redempDlvyAmt.setModerNo(ocClaim.getModerNo()); // 수정자

				vndrRedempDlvyPaymentList.add(_redempDlvyAmt); // 업체별 환수 주문배송비 이력 set
				redempAmtByFreeDlvyProduct += sySite.getDlvyAmt(); // 환수배송비 set
			}
		}

		// 업체상품 배송비 체크
		if (!ObjectUtils.isEmpty(vndrNoList)) {
			for (String vndrNo : vndrNoList) {
				// 해당 업체 현재 클레임 상품(사은품/배송비 제외)
				List<OcClaimProduct> _thisTimeclaimProductList = vndrThisTimeClaimProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).collect(Collectors.toList());

				// 현재 클레임 개수(사은품/배송비 제외)
				long _thisTimeClaimCnt = _thisTimeclaimProductList.size();

				// 배송비 상품
				OcOrderProduct _dlvyProduct = vndrOrgOrderProductList.stream()
						.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).findFirst().orElse(null);

				// 기존 취소된 배송비 상품
				OcClaimProduct _canceledDlvyProduct = vndrFinishedClaimProductList.stream()
						.filter(x -> UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).findFirst().orElse(null);

				// 기존 환수된 배송비 여부
				long _redemptDlvyCnt = claimAllPaymentList.stream()
						.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP)) // 환수
						.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE)) // 재경팀처리 제외
						.filter(x -> UtilsText.equals(x.getOcrncRsnCode(),
								CommonCode.OCRNC_RSN_CODE_ORDER_DELIVERY_AMT)) // 주문배송비
						.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC)) // 일반결제
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)) // 업체번호
						.count();

				// 원 주문 상품 개수(사은품/배송비 제외)
				long _orderProductCnt = vndrOrgOrderProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

				// 완료된 클레임 개수(사은품/배송비 제외)
				long _finishClaimCnt = vndrFinishedClaimProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

				// 원 주문에 포함된 무료배송상품 개수
				long _orderClaimFreeDlvyProductCnt = vndrOrgOrderProductList.stream()
						.filter(x -> UtilsText.equals(x.getFreeDlvyYn(), Const.BOOLEAN_TRUE))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

				// 완료된 클레임에 포함된 무료배송상품 개수
				long _finishClaimFreeDlvyProductCnt = vndrFinishedClaimProductList.stream()
						.filter(x -> UtilsText.equals(x.getFreeDlvyYn(), Const.BOOLEAN_TRUE))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

				// 현재 클레임 무료배송상품 개수
				long _thisClaimFreeDlvyProductCnt = vndrThisTimeClaimProductList.stream()
						.filter(x -> UtilsText.equals(x.getFreeDlvyYn(), Const.BOOLEAN_TRUE))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).count();

				// 완료/진행 클레임의 무료배송상품 남은 개수를 확인
				long _remainFreeDlvyProductCnt = _orderClaimFreeDlvyProductCnt - _finishClaimFreeDlvyProductCnt
						- _thisClaimFreeDlvyProductCnt;

				// 원 주문 금액 sum(사은품/배송비 제외)
				int _orderAmtSum = vndrOrgOrderProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).mapToInt(x -> x.getOrderAmt()).sum();

				// 완료 클레임 금액 sum(사은품/배송비 제외)
				int _canceledAmtSum = vndrFinishedClaimProductList.stream()
						.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
								&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).mapToInt(x -> x.getOrderAmt()).sum();

				// 현재 클레임 금액 sum(사은품/배송비 제외)
				int _thisTiemCancelAmtSum = _thisTimeclaimProductList.stream()
						.filter(x -> UtilsText.equals(x.getVndrNo(), vndrNo)).mapToInt(x -> x.getOrderAmt()).sum();

				// 환불배송비 처리
				// 현재 클레임 기준 업체상품 전체 취소, 이전 부분 취소 후 남은 상품 모두 취소인 경우
				if ((_orderProductCnt == _thisTimeClaimCnt)
						|| (_orderProductCnt == _finishClaimCnt + _thisTimeClaimCnt)) {
					// 지불한 배송비가 있으면 클레임 상품 추가 및 환불 대상(배송비 쿠폰을 사용하던 안하던 주문 배송비 행 처리)
					if (_dlvyProduct != null) {
						cancelDlvyProductList.add(_dlvyProduct); // 취소될 배송비 상품 set
						refundAmtByDlvyProduct += _dlvyProduct.getOrderAmt(); // 환불배송비 set
					}
				}

				VdVendor vdVendor = new VdVendor();
				vdVendor.setVndrNo(vndrNo);
				vdVendor = vdVendorDao.selectByPrimaryKey(vdVendor);

				// 환수배송비 체크
				// 1.현재 클레임을 제외한 남은 무료배송상품이 없고
				// 2.기 환수된 배송비 없고
				// 3.남은상품 모두 취소 아니고
				// 4.배송비 면제금액보다 낮으면 발생
				if (_remainFreeDlvyProductCnt == 0 && _redemptDlvyCnt == 0
						&& (_orderProductCnt != _finishClaimCnt + _thisTimeClaimCnt)
						&& (_orderAmtSum - _canceledAmtSum - _thisTiemCancelAmtSum < sySite.getFreeDlvyStdrAmt())) {

					OcOrderProduct _redempDlvyProduct = new OcOrderProduct(); // 상품
					OcClaimPayment _redempDlvyAmt = new OcClaimPayment(); // 결제

					// 환수 배송비 상품정보 set
					_redempDlvyProduct.setOrderNo(ocClaim.getOrderNo()); // 주문번호
					_redempDlvyProduct.setPrdtNo(vndrDlvyProduct.getPrdtNo()); // 상품번호
					_redempDlvyProduct.setPrdtOptnNo("001"); // 상품옵션번호
					_redempDlvyProduct.setOptnName(vndrDlvyProduct.getPrdtName()); // 옵션명:상품명 대체
					_redempDlvyProduct.setPrdtName(vndrDlvyProduct.getPrdtName()); // 상품명
					_redempDlvyProduct.setEngPrdtName(vndrDlvyProduct.getEngPrdtName()); // 영문상품명
					_redempDlvyProduct.setPrdtTypeCode(vndrDlvyProduct.getPrdtTypeCode()); // 상품유형코드
					_redempDlvyProduct.setStyleInfo(vndrDlvyProduct.getStyleInfo()); // 스타일정보
					_redempDlvyProduct.setRsvPrdtYn(Const.BOOLEAN_FALSE); // 예약상품여부
					_redempDlvyProduct.setVndrNo(_thisTimeclaimProductList.get(0).getVndrNo()); // 업체번호:첫번째 상품의 업체번호 기준
					_redempDlvyProduct.setVndrName(_thisTimeclaimProductList.get(0).getVndrName()); // 업체명:첫번째 상품의
																									// 업체번호기준
					_redempDlvyProduct.setChnnlNo(vndrDlvyProduct.getChnnlNo()); // 채널번호
					_redempDlvyProduct.setMmnyPrdtYn(Const.BOOLEAN_TRUE); // 자사상품여부
					_redempDlvyProduct.setEmpDscntYn(Const.BOOLEAN_FALSE); // 임직원할인여부
					_redempDlvyProduct.setOrderMnfctYn(Const.BOOLEAN_FALSE); // 주문제작여부
					_redempDlvyProduct.setDprcExceptYn(Const.BOOLEAN_FALSE); // 감가제외여부
					_redempDlvyProduct.setFreeDlvyYn(Const.BOOLEAN_FALSE); // 무료배송여부
					_redempDlvyProduct.setOrderQty(1); // 주문수량
					_redempDlvyProduct.setPrdtNormalAmt(vdVendor.getDlvyAmt()); // 상품정상가
					_redempDlvyProduct.setPrdtSellAmt(vdVendor.getDlvyAmt()); // 상품판매가
					_redempDlvyProduct.setOptnAddAmt(0); // 옵션추가금액
					_redempDlvyProduct.setSellAmt(vdVendor.getDlvyAmt()); // 판매가
					_redempDlvyProduct.setEmpDscntRate((short) 0); // 임직원할인율
					_redempDlvyProduct.setEmpAmt(0); // 임직원가
					_redempDlvyProduct.setTotalDscntAmt(0); // 할인금액합계
					_redempDlvyProduct.setCpnDscntAmt(0); // 쿠폰할인금액
					_redempDlvyProduct.setOrderAmt(vdVendor.getDlvyAmt()); // 주문금액
					_redempDlvyProduct.setVndrCmsnRate((short) 0); // 업체수수료율
					_redempDlvyProduct.setSellCnclReqYn(Const.BOOLEAN_FALSE); // 판매취소요청여부
					_redempDlvyProduct.setLogisCnvrtYn(Const.BOOLEAN_FALSE); // 택배전환여부
					_redempDlvyProduct.setPickupExtsnYn(Const.BOOLEAN_FALSE); // 픽업연장여부
					_redempDlvyProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_COMPLETE); // 주문상품상태코드:결제완료
					_redempDlvyProduct.setGenderGbnCode(vndrDlvyProduct.getGenderGbnCode());
					_redempDlvyProduct.setBuyPointSaveRate((short) 0); // 구매포인트적립율
					_redempDlvyProduct.setRgsterNo(ocClaim.getMemberNo());
					_redempDlvyProduct.setModerNo(ocClaim.getMemberNo());

					redempDlvyProductList.add(_redempDlvyProduct); // 환수할 배송비 상품 set

					// 결제정보 set(히스토리용 이기 때문에 실결제와 관련된 내용은 의미없어도 무방)
					_redempDlvyAmt.setClmNo(ocClaim.getClmNo());
					_redempDlvyAmt.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP); // 환수환불구분:환수
					_redempDlvyAmt.setPymntDtm(new Timestamp(new Date().getTime())); // 결제일시
					_redempDlvyAmt.setDeviceCode(orderPlaymentList.get(0).getDeviceCode()); // 디바이스코드
					_redempDlvyAmt.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // 주결제수단여부
					_redempDlvyAmt.setPymntMeansCode(orderPlaymentList.get(0).getPymntMeansCode()); // 결제수단코드
					_redempDlvyAmt.setPymntVndrCode(null); // 결제업체코드
					_redempDlvyAmt.setInstmtTermCount((short) 0); // 할부기간
					_redempDlvyAmt.setPymntTodoAmt(vdVendor.getDlvyAmt()); // 결제예정금액
					_redempDlvyAmt.setPymntAmt(vdVendor.getDlvyAmt()); // 결제금액
					_redempDlvyAmt.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부
					_redempDlvyAmt.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부
					_redempDlvyAmt.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부
					_redempDlvyAmt.setAcntCrtfcYn(Const.BOOLEAN_FALSE); // 계좌인증여부
					_redempDlvyAmt.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 자사처리대상여부
					_redempDlvyAmt.setHistGbnType(CommonCode.HIST_GBN_TYPE_HISTORY); // 이력구분 - 이력근거용
					_redempDlvyAmt.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_DELIVERY_AMT); // 발생사유코드:주문배송비
					_redempDlvyAmt.setVndrNo(vndrThisTimeClaimProductList.get(0).getVndrNo()); // 업체번호:자사업체코드
					_redempDlvyAmt.setRgsterNo(ocClaim.getRgsterNo()); // 등록자
					_redempDlvyAmt.setModerNo(ocClaim.getModerNo()); // 수정자

					vndrRedempDlvyPaymentList.add(_redempDlvyAmt); // 업체별 환수 주문배송비 이력 set
					redempAmtByFreeDlvyProduct += vdVendor.getDlvyAmt(); // 환수배송비 set
				}
			}
		}

		// 환수할 배송비 상품 set
		ocClaimAmountVO.setRedempDlvyProductList(redempDlvyProductList);
		// 취소시킬(환불대상) 배송비 상품
		ocClaimAmountVO.setCancelDlvyProductList(cancelDlvyProductList);
		// 환불배송비 vo set
		ocClaimAmountVO.setRefundAmtByDlvyProduct(refundAmtByDlvyProduct);
		// 환수배송비 vo set
		ocClaimAmountVO.setRedempAmtByFreeDlvyProduct(redempAmtByFreeDlvyProduct);

		/*
		 * 클레임 금액 기본 계산
		 */
		// 현재 클레임으로 인해 취소될 금액(클레임상품금액 + 환불배송비)
		int expectCnclAmt = thisTimeClaimProductList.stream().mapToInt(o -> o.getOrderAmt()).sum()
				+ refundAmtByDlvyProduct;
		int refundCnclAmt = 0; // 실제 취소로 인해 환불될 금액

		// 현재 취소 클레임으로 인해 환불될 금액 : 다족구매, 배송비 환수금 제외
		refundCnclAmt = expectCnclAmt - redempAmtByMultiBuy - redempAmtByFreeDlvyProduct;

		ocClaimAmountVO.setExpectCnclAmt(expectCnclAmt);
		log.debug("######################### expectCnclAmt : {} ", expectCnclAmt);

		ocClaimAmountVO.setRefundCnclAmt(refundCnclAmt);
		log.debug("######################### refundCnclAmt : {} ", refundCnclAmt);

		// ocClaimAmountVO.setTotalCnclAmt(expectCnclAmt);

		ocClaimAmountVO.setTotalRedempAmt(redempAmtByMultiBuy + redempAmtByFreeDlvyProduct);
		log.debug("######################### TotalRedempAmt : {} ", redempAmtByMultiBuy + redempAmtByFreeDlvyProduct);

		OcClaimPayment mainPayment = null;
		OcClaimPayment mainTempPayment = null;
		OcClaimPayment giftPayment = null;
		OcClaimPayment pointPayment = null;
		OcClaimPayment eventPointPayment = null;

		for (OcClaimPayment claimPayment : orderClaimPaymentList) {
			if (UtilsText.equals(claimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_MOBILE_GIFT)) { // 기프트
				giftPayment = claimPayment;
			} else if (UtilsText.equals(claimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_BUY_POINT)) { // 구매포인트
				pointPayment = claimPayment;
			} else if (UtilsText.equals(claimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_EVENT_POINT)) { // 이벤트포인트
				eventPointPayment = claimPayment;
			} else {
				mainPayment = claimPayment;
				mainTempPayment = claimPayment;
			}
		}

		int mainPymntAmt = (mainPayment == null) ? 0 : mainPayment.getPymntAmt();
		int giftPymntAmt = (giftPayment == null) ? 0 : giftPayment.getPymntAmt();
		int pointPymntAmt = (pointPayment == null) ? 0 : pointPayment.getPymntAmt();
		int eventPointPymntAmt = (eventPointPayment == null) ? 0 : eventPointPayment.getPymntAmt();

		ocClaimAmountVO.setMainPayment(mainPayment);
		ocClaimAmountVO.setGiftPayment(giftPayment);
		ocClaimAmountVO.setPointPayment(eventPointPayment);
		ocClaimAmountVO.setEventPointPayment(eventPointPayment);

		int mainRfndAmt = 0;
		int giftRfndAmt = 0;
		int pointRfndAmt = 0;
		int eventPointRfndAmt = 0;

		/*
		 * 이벤트포인트>주결제수단>기프트카드>구매포인트>쿠폰 순으로 환불
		 */
		// 클레임 상품 외 이벤트포인트 적용될 상품이 없고 취소할 이벤트 포인트 있는 경우
		if (eventPointPymntAmt > 0) {
			if (eventPointPymntAmt > refundCnclAmt) {
				eventPointRfndAmt = refundCnclAmt;
				refundCnclAmt = 0;
			} else {
				eventPointRfndAmt = eventPointPymntAmt;
				refundCnclAmt -= eventPointRfndAmt;
			}
		}

		// 취소 할 주결제
		if (refundCnclAmt > 0 && mainPymntAmt > 0) {
			if (mainPymntAmt > refundCnclAmt) {
				mainRfndAmt = refundCnclAmt;
				refundCnclAmt = 0;
			} else {
				mainRfndAmt = mainPymntAmt;
				refundCnclAmt -= mainRfndAmt;
			}
		}

		// 취소 할 기프트카드
		if (refundCnclAmt > 0 && giftPymntAmt > 0) {
			if (giftPymntAmt > refundCnclAmt) {
				giftRfndAmt = refundCnclAmt;
				refundCnclAmt = 0;
			} else {
				giftRfndAmt = giftPymntAmt;
				refundCnclAmt -= giftRfndAmt;
			}
		}

		// 취소 할 구매포인트
		if (refundCnclAmt > 0 && pointPymntAmt > 0) {
			if (pointRfndAmt > refundCnclAmt) {
				pointRfndAmt = refundCnclAmt;
				refundCnclAmt = 0;
			} else {
				pointRfndAmt = pointPymntAmt;
				refundCnclAmt -= pointRfndAmt;
			}
		}

		if (ocClaimAmountVO.getEventPointPayment() != null) {
			ocClaimAmountVO.getEventPointPayment().setPymntAmt(eventPointRfndAmt);
			ocClaimAmountVO.getEventPointPayment().setPymntTodoAmt(eventPointRfndAmt);
		}

		if (ocClaimAmountVO.getGiftPayment() != null) {
			ocClaimAmountVO.getGiftPayment().setPymntAmt(giftRfndAmt);
			ocClaimAmountVO.getGiftPayment().setPymntTodoAmt(giftRfndAmt);
		}

		if (ocClaimAmountVO.getMainPayment() != null) {
			ocClaimAmountVO.getMainPayment().setPymntAmt(mainRfndAmt);
			ocClaimAmountVO.getMainPayment().setPymntTodoAmt(mainRfndAmt);
		}

		if (ocClaimAmountVO.getPointPayment() != null) {
			ocClaimAmountVO.getPointPayment().setPymntAmt(pointRfndAmt);
			ocClaimAmountVO.getPointPayment().setPymntTodoAmt(pointRfndAmt);
		}

		ocClaimAmountVO.setEventPointCnclAmt(eventPointRfndAmt);
		ocClaimAmountVO.setGiftCnclAmt(giftRfndAmt);
		ocClaimAmountVO.setMainCnclAmt(mainRfndAmt);
		ocClaimAmountVO.setPointCnclAmt(pointRfndAmt);
//		if (true) {
//			throw new Exception("임의에러");
//		}
		return ocClaimAmountVO;
	}

	/**
	 * @Desc : 결제 취소
	 * @Method Name : setCancelPayment
	 * @Date : 2019. 5. 31.
	 * @Author : KTH
	 * @param paymentCancelInfo
	 * @param ocClaim
	 * @param useEscr
	 * @throws Exception
	 */
	public OcClaimAmountVO setCancelPayment(OcClaimAmountVO paymentCancelInfo, OcClaim ocClaim, MbMember mbMember) {

		ObjectMapper mapper = new ObjectMapper();
		PaymentResult paymentResult = null; // 결제취소 결과(KCP, NAVER, KAKAO)
		boolean pointCancelSuccess = false; // 포인트 취소 성공여부
		boolean isRefundOccurrence = false; // 환불접수 상황 발생 여부

		/*
		 * 결제 취소 정보 저장
		 */
		OcClaimPayment mainPayment = paymentCancelInfo.getMainPayment();
		OcClaimPayment giftPayment = paymentCancelInfo.getGiftPayment();
		OcClaimPayment pointPayment = paymentCancelInfo.getPointPayment();
		OcClaimPayment eventPointPayment = paymentCancelInfo.getEventPointPayment();

		/*
		 * 구매/이벤트 포인트 사용 취소
		 */
		if (pointPayment != null || eventPointPayment != null) {
			// 포인트 결제 취소 처리
			pointCancelSuccess = this.setCancelOrderUsePoint(ocClaim, pointPayment, eventPointPayment, mbMember);

			/*
			 * 구매/이벤트 포인트 사용 취소 결과 등록
			 */
			OcClaimPayment pointCancelResult = new OcClaimPayment();

			if (pointCancelSuccess) {
				if (pointPayment != null) {
					BeanUtils.copyProperties(pointPayment, pointCancelResult); // 내용 복사

					pointCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL); // 결제취소
					this.setClaimPayment(ocClaim, pointCancelResult, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
				}

				if (eventPointPayment != null) {
					BeanUtils.copyProperties(eventPointPayment, pointCancelResult); // 내용 복사

					pointCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL); // 결제취소
					this.setClaimPayment(ocClaim, eventPointPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
				}
			} else {
				if (pointPayment != null) {
					BeanUtils.copyProperties(pointPayment, pointCancelResult); // 내용 복사
					isRefundOccurrence = true; // 환불접수 상황 발생

					pointCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 환불접수
					this.setClaimPayment(ocClaim, pointCancelResult, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
				}

				if (eventPointPayment != null) {
					BeanUtils.copyProperties(eventPointPayment, pointCancelResult); // 내용 복사
					isRefundOccurrence = true; // 환불접수 상황 발생

					pointCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 환불접수
					this.setClaimPayment(ocClaim, eventPointPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
				}
			}
		}

		// 이미 수기로 환불을 진행한 경우이므로 포인트는 환불하되, PG사 호출을 통한 환불처리는 skip
		if (paymentCancelInfo.isCancelByForce()) {
			return paymentCancelInfo;
		}

		/*
		 * 기프트 사용 취소
		 */
		if (giftPayment != null) {
			// 기프트 결제 취소
			Map<String, Object> chargeResultMap = this.setCancelNicePayment(ocClaim, giftPayment,
					paymentCancelInfo.isAllCancelYn());

			/*
			 * 기프트 사용 취소 결과 등록
			 */
			OcClaimPayment giftCancelResult = new OcClaimPayment();
			BeanUtils.copyProperties(giftPayment, giftCancelResult); // 내용 복사

			if (chargeResultMap.get("chargeResult") == null) {
				isRefundOccurrence = true; // 환불접수 상황 발생

				giftCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 환불접수
				giftCancelResult.setRspnsCodeText("9999");
				giftCancelResult.setRspnsMesgText("결제취소 실패");

				this.setClaimPayment(ocClaim, giftCancelResult, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			} else {
				@SuppressWarnings("unchecked")
				CommNiceRes<ChargeResponse> chargeResult = (CommNiceRes<ChargeResponse>) chargeResultMap
						.get("chargeResult");

				String giftCardNo = (String) chargeResultMap.get("giftCardNo");
				int chargeGiftAmt = (Integer) chargeResultMap.get("chargeGiftAmt");

				if (UtilsText.equals(chargeResult.getResCode(), "0000")) {
					giftCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL); // 결제취소

					// 결제취소가 성공한경우 기프트카드 현금 영수증 테이블 인서트
					OcCashReceipt giftCashReceipt = new OcCashReceipt();
					giftCashReceipt.setRcptType(CommonCode.RCPT_TYPE_DEDUCTION); // 소득공제
					giftCashReceipt.setRcptIssueInfo(mbMember.getHdphnNoText()); // 휴대폰번호
					giftCashReceipt.setOrderNo(ocClaim.getReOrderNo()); // 리오더주문번호
					giftCashReceipt.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호
					giftCashReceipt.setClmNo(ocClaim.getClmNo()); // 클레임 번호
					giftCashReceipt.setPymntOrganNoText(giftCardNo); // 카드번호
					giftCashReceipt.setPymntAprvNoText(chargeResult.getResData().getApprovalNo()); // 결제승인번호
					giftCashReceipt.setRcptProcStatCode(CommonCode.RCPT_PROC_STAT_CODE_ACCEPT);
					giftCashReceipt.setOcrncAmt(chargeGiftAmt); // 발생금액
					giftCashReceipt.setRgsterNo(ocClaim.getMemberNo()); // 등록자

					try {
						ocCashReceiptDao.insertCashReceipt(giftCashReceipt);
					} catch (Exception e) {
						log.error("기프트 현금영수증 등록 에러 - giftCashReceipt : {}", giftCashReceipt);
						e.printStackTrace();
					}
				} else {
					isRefundOccurrence = true; // 환불접수 상황 발생
					giftCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 환불접수
				}

				giftCancelResult.setRspnsCodeText(chargeResult.getResCode());
				giftCancelResult.setRspnsMesgText(chargeResult.getResMsg());

				try {
					giftCancelResult.setPymntLogInfo(mapper.writeValueAsString(chargeResult.getResData()));
				} catch (JsonProcessingException e) {
					log.error("결제처리 데이터 변환 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
					giftCancelResult.setPymntLogInfo(null);
					e.printStackTrace();
				}

				this.setClaimPayment(ocClaim, giftCancelResult, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 메인 결제수단 취소 내용이 없으면 skip
		if (mainPayment == null) {
			return paymentCancelInfo;
		}

		/*
		 * 주결제 결제 취소
		 */
		if (paymentCancelInfo.isAllCancelYn()) { // 전체취소
			if (UtilsText.equals(mainPayment.getPymntVndrCode(), CommonCode.PYMNT_VNDR_CODE_KCP)) {
				if (UtilsText.equals(mainPayment.getEscrApplyYn(), Const.BOOLEAN_TRUE)) { // 에스크로 결제
					String escrowModType = "";

					if (paymentCancelInfo.isEscrSendYn()) {
						if (UtilsText.equals(mainPayment.getPymntMeansCode(),
								CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER)) { // 계좌이체
							escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE9_A;
						} else {
							escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE9_V;
						}
					} else {
						if (UtilsText.equals(mainPayment.getPymntMeansCode(),
								CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)
								&& UtilsText.equals(paymentCancelInfo.getPymntStatCode(),
										CommonCode.PYMNT_STAT_CODE_WAIT_DEPOSIT)) { // 가상계좌 입금대기
							escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE5;
						} else {
							escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE2;
						}
					}

					paymentResult = this.setCancelKcpEscrowPayment(ocClaim, mainPayment, escrowModType, "cancel");
				} else {
					if (UtilsText.equals(mainPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)
							&& UtilsText.equals(paymentCancelInfo.getPymntStatCode(),
									CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH)) { // 가상계좌 결제완료

						this.setVcntRefundKcpPayment(ocClaim, mainPayment, true);
					} else {
						// 무통장입금 결제 선택후 입금하지 않은 상태에서 취소시 계좌 폐쇄하지않고 열어둠
						// 무통장입금 거래 주문후에 고객이 입금 후 kcp에서 계좌확인 코드가 리턴되기전에 고객이 주문취소한경우 예치금으로 돌리기 위해 계좌 열어둠
						if (UtilsText.equals(mainPayment.getPymntMeansCode(),
								CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)
								&& UtilsText.equals(paymentCancelInfo.getPymntStatCode(),
										CommonCode.PYMNT_STAT_CODE_WAIT_DEPOSIT)) { // 가상계좌 입금대기
							// none
						} else {
							paymentResult = this.setCancelKcpPayment(ocClaim, mainPayment, true);
						}
					}
				}
			} else if (UtilsText.equals(mainPayment.getPymntVndrCode(), CommonCode.PYMNT_VNDR_CODE_NAVER)) {
				paymentResult = this.setCancelNaverPayment(mainPayment, ocClaim, true);
			} else if (UtilsText.equals(mainPayment.getPymntVndrCode(), CommonCode.PYMNT_VNDR_CODE_KAKAO)) {
				paymentResult = this.setCancelKakaoPayment(ocClaim, mainPayment, true);
			}
		} else {
			// 결제부분취소
			if (UtilsText.equals(mainPayment.getPymntVndrCode(), CommonCode.PYMNT_VNDR_CODE_KCP)) {
				if (UtilsText.equals(mainPayment.getEscrApplyYn(), Const.BOOLEAN_TRUE)) { // 에스크로 결제
					String escrowModType = "";

					if (paymentCancelInfo.isEscrSendYn()) {
						if (UtilsText.equals(mainPayment.getPymntMeansCode(),
								CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER)) { // 계좌이체
							escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE9_AP;
						} else {
							escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE9_VP;
						}
					} else {
						escrowModType = CommonCode.KCP_ESCROW_MOD_TYPE_STE2;

						paymentResult = new PaymentResult("N", "9999", null, null, "에스크로 전송 전 입금 취소처리 불가", null);
					}

					this.setCancelKcpEscrowPayment(ocClaim, mainPayment, escrowModType, "cancel");
				} else {
					if (UtilsText.equals(mainPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT)
							&& UtilsText.equals(paymentCancelInfo.getPymntStatCode(),
									CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH)) { // 가상계좌 결제완료
						paymentResult = this.setVcntRefundKcpPayment(ocClaim, mainPayment, false);
					} else {
						paymentResult = this.setCancelKcpPayment(ocClaim, mainPayment, false);
					}
				}
			} else if (UtilsText.equals(mainPayment.getPymntVndrCode(), CommonCode.PYMNT_VNDR_CODE_NAVER)) {
				paymentResult = this.setCancelNaverPayment(mainPayment, ocClaim, false);
			} else if (UtilsText.equals(mainPayment.getPymntVndrCode(), CommonCode.PYMNT_VNDR_CODE_KAKAO)) {
				paymentResult = this.setCancelKakaoPayment(ocClaim, mainPayment, false);
			}
		}

		/*
		 * 주결제 결제 취소 결과 등록
		 */
		OcClaimPayment mainCancelResult = new OcClaimPayment();
		BeanUtils.copyProperties(mainPayment, mainCancelResult); // 내용 복사

		if (paymentResult == null) {
			isRefundOccurrence = true; // 환불접수 상황 발생
			mainCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 환불접수
			mainCancelResult.setRspnsCodeText("9999");
			mainCancelResult.setRspnsMesgText("결제취소 실패");

			this.setClaimPayment(ocClaim, mainCancelResult, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
		} else {
			if (UtilsText.equals(paymentResult.getSuccessYn(), Const.BOOLEAN_TRUE)) {
				mainCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL); // 결제취소
			} else {
				isRefundOccurrence = true; // 환불접수 상황 발생
				mainCancelResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 환불접수
			}

			mainCancelResult.setRspnsCodeText(paymentResult.getCode());
			mainCancelResult.setRspnsMesgText(paymentResult.getMessage());

			try {
				mainCancelResult.setPymntLogInfo(mapper.writeValueAsString(paymentResult.getData()));
			} catch (JsonProcessingException e) {
				log.error("결제처리 데이터 변환 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
				mainCancelResult.setPymntLogInfo(null);
				e.printStackTrace();
			}

			this.setClaimPayment(ocClaim, mainCancelResult, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
		}

		/*******************
		 * 환불발생 상황 set
		 *******************/
		paymentCancelInfo.setRefundOccurrence(isRefundOccurrence);

		return paymentCancelInfo;
	}

	/**
	 * @Desc : 품절보상 쿠폰 지급
	 * @Method Name : setGiveSoldOutCmpns
	 * @Date : 2019. 6. 15.
	 * @Author : KTH
	 * @param claim
	 * @param claimProductList
	 * @throws Exception
	 */
	public boolean setGiveSoldOutCmpns(OcClaim claim, List<OcClaimProduct> claimProductList) throws Exception {
		// 사이트 정책에 등록된 품절보상 쿠폰
		PrCoupon soldOutCmpnsCpn = prCouponDao.selectSoldOutCmpnsCpnPolicy(); // soldOutCompensation

		// 지급 대상 품절보상 쿠폰이 있는 경우만
		if (soldOutCmpnsCpn != null) {
			MbMemberCoupon mbMemberCoupon = new MbMemberCoupon();
			mbMemberCoupon.setMemberNo(claim.getMemberNo());
			mbMemberCoupon.setCpnNo(soldOutCmpnsCpn.getCpnNo());
			mbMemberCoupon.setValidStartDtm(soldOutCmpnsCpn.getValidStartDtm());
			mbMemberCoupon.setValidEndDtm(soldOutCmpnsCpn.getValidEndDtm());
			mbMemberCoupon.setCpnStatCode(CommonCode.CPN_STAT_CODE_ISSUANCE); // 쿠폰 상태 코드 : 발급
			mbMemberCoupon.setRgsterNo(Const.SYSTEM_ADMIN_NO);
			mbMemberCoupon.setModerNo(Const.SYSTEM_ADMIN_NO);

			int i = 0;
			while (i < claimProductList.size()) {
				mbMemberCouponDao.insertMemberCoupon(mbMemberCoupon);
				i++;
			}
		}

		return true;
	}

	/**
	 * @Desc : 클레임 결제 이력데이터 등록
	 * @Method Name : setClaimPaymentHistory
	 * @Date : 2019. 6. 17.
	 * @Author : KTH
	 * @param ocClaim
	 * @param claimPayment
	 * @throws Exception
	 */
	public void setClaimPaymentHistory(OcClaim ocClaim, OcClaimPayment claimPayment, String redempRfndGbnType)
			throws Exception {

		claimPayment.setClmNo(ocClaim.getClmNo()); // 클레임번호
		claimPayment.setRedempRfndGbnType(redempRfndGbnType); // 환수환불구분
		claimPayment.setPymntDtm(null); // 결제일시
		claimPayment.setAcntCrtfcYn(Const.BOOLEAN_FALSE); // 계좌인증여부
		claimPayment.setRedempRfndMemoText(null); // 환수환불메모
		claimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 자사처리대상여부
		claimPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_AMT); // 발생사유코드 - 주문금
		claimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_HISTORY); // 클레임이력구분 - 이력근거용
		claimPayment.setRgsterNo(ocClaim.getMemberNo()); // 등록자
		claimPayment.setModerNo(ocClaim.getMemberNo()); // 수정자

		ocClaimPaymentDao.insertClaimPayment(claimPayment);
	}

	/**
	 * @Desc : 구매적립 포인트 계산(더블포인트 적립 적용하지 않음)
	 * @Method Name : setReCalcSvaePoint
	 * @Date : 2019. 7. 14.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocClaimAmountVO
	 */
	public void setReCalcSvaePoint(OcClaim ocClaim, OcClaimAmountVO ocClaimAmountVO) throws Exception {
		double orgOrderSavePoint = 0.0; // 원 주문 기준 적립포인트
		double beforeClaimSavePlusPoint = 0.0; // 현재 클레임 이전 기준 증가 적립포인트
		double beforeClaimSaveMinusPoint = 0.0; // 현재 클레임 이전 기준 취소 적립포인트
		double beforeClaimSavePoint = 0.0; // 현재 클레임 이전 기준 적립포인트
		double thisClaimPlusSavePoint = 0.0; // 현재 클레임 기준 증가 적립포인트
		double thisClaimMinusSavePoint = 0.0; // 현재 클레임 기준 취소 적립포인트
		double thisClaimVariationSavePoint = 0.0; // 현재 클레임 증감 적립포인트

		OcOrderProduct paramProduct = new OcOrderProduct();
		paramProduct.setOrgOrderNo(ocClaim.getOrgOrderNo());

		List<OcOrderProduct> reOrderProcutList = ocOrderProductDao.selectReOrderProductList(paramProduct);

		orgOrderSavePoint = ocClaimAmountVO.getOrgOrderProductList().stream()
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.mapToDouble(o -> Math.ceil(o.getOrderAmt() * 3 / 1000.0) * 10).sum();

		beforeClaimSavePlusPoint = reOrderProcutList.stream()
				.filter(x -> UtilsText.equals(x.getSalesCnclGbnType(), CommonCode.SALES_CNCL_GBN_TYPE_SALE))
				.filter(x -> !UtilsText.equals(x.getClmNo(), ocClaim.getClmNo()))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.mapToDouble(o -> Math.ceil(o.getOrderAmt() * 3 / 1000.0) * 10).sum();

		beforeClaimSaveMinusPoint = reOrderProcutList.stream()
				.filter(x -> UtilsText.equals(x.getSalesCnclGbnType(), CommonCode.SALES_CNCL_GBN_TYPE_CANCEL))
				.filter(x -> !UtilsText.equals(x.getClmNo(), ocClaim.getClmNo()))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.mapToDouble(o -> Math.ceil(o.getOrderAmt() * 3 / 1000.0) * 10).sum();

		beforeClaimSavePoint = orgOrderSavePoint - beforeClaimSaveMinusPoint + beforeClaimSavePlusPoint;

		thisClaimPlusSavePoint = reOrderProcutList.stream()
				.filter(x -> UtilsText.equals(x.getSalesCnclGbnType(), CommonCode.SALES_CNCL_GBN_TYPE_SALE))
				.filter(x -> !UtilsText.equals(x.getClmNo(), ocClaim.getClmNo()))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.mapToDouble(o -> Math.ceil(o.getOrderAmt() * 3 / 1000.0) * 10).sum();

		thisClaimMinusSavePoint = reOrderProcutList.stream()
				.filter(x -> UtilsText.equals(x.getSalesCnclGbnType(), CommonCode.SALES_CNCL_GBN_TYPE_CANCEL))
				.filter(x -> !UtilsText.equals(x.getClmNo(), ocClaim.getClmNo()))
				.filter(x -> !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
						&& !UtilsText.equals(x.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY))
				.mapToDouble(o -> Math.ceil(o.getOrderAmt() * 3 / 1000.0) * 10).sum();

		thisClaimVariationSavePoint = thisClaimPlusSavePoint - thisClaimMinusSavePoint;

		ocClaimAmountVO.setOrgOrderSavePoint((int) orgOrderSavePoint);
		ocClaimAmountVO.setBeforeClaimSavePoint((int) beforeClaimSavePoint);
		ocClaimAmountVO.setThisClaimVariationSavePoint((int) thisClaimVariationSavePoint);
	}

	/*********************************************************************************************************
	 *********************************************************************************************************
	 * 이하 Exception throw 하지 않는 메서드
	 *********************************************************************************************************
	 *********************************************************************************************************/

	/**
	 * @Desc : 클레임 결제 결제취소 시 결제처리 데이터 등록
	 * @Method Name : setClaimPaymentForCancelPayment
	 * @Date : 2019. 6. 17.
	 * @Author : KTH
	 * @param ocClaim
	 * @param claimPayment
	 * @throws Exception
	 */
	public void setClaimPayment(OcClaim ocClaim, OcClaimPayment claimPayment, String redempRfndGbnType) {

		try {
			claimPayment.setClmNo(ocClaim.getClmNo()); // 클레임번호
			claimPayment.setRedempRfndGbnType(redempRfndGbnType); // 환수환불구분
			claimPayment.setPymntDtm(new Timestamp(new Date().getTime())); // 결제일시
			claimPayment.setAcntCrtfcYn(Const.BOOLEAN_FALSE); // 계좌인증여부
			claimPayment.setRedempRfndMemoText(null); // 환수환불메모
			claimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 자사처리대상여부
			claimPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_AMT); // 발생사유코드 - 주문금
			claimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_PUBLIC); // 클레임이력구분 - 일반결제처리용
			claimPayment.setRgsterNo(ocClaim.getMemberNo()); // 등록자
			claimPayment.setModerNo(ocClaim.getMemberNo()); // 수정자

			ocClaimPaymentDao.insertClaimPayment(claimPayment);
		} catch (Exception e) {
			log.error("결제처리 데이터 등록 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}
	}

	/**
	 * @Desc : 클레임 처리 후 처리대상 주문, 클레임 상태 값 변경 등 후처리
	 * @Method Name : setClaimCancelAfterProc
	 * @Date : 2019. 6. 20.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocClaimAmountVO
	 * @param reOrderNo
	 */
	public void setClaimCancelAfterProc(OcClaim ocClaim, OcClaimAmountVO ocClaimAmountVO, String reOrderNo) {
		String orderStatCode = CommonCode.ORDER_STAT_CODE_CANCEL_COMPLETE; // 주문 상태코드 : 전체취소완료
		String orderPrdtStatCode = CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_COMPLETE; // 주문상품 상태코드 : 취소완료
		String clmStatCode = CommonCode.CLM_STAT_CODE_CANCEL_FINISH; // 클레임 상태 코드(취소) ; 취소완료
		String clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_CANCEL_FINISH; // 클레임상품 상태 코드(취소) ; 취소완료

		if (ocClaimAmountVO.isRefundOccurrence()) { // 환불 발생 상황
			orderStatCode = CommonCode.ORDER_STAT_CODE_REFUND_ACCEPT; // 주문 상태코드 : 환수/환불접수
			orderPrdtStatCode = CommonCode.ORDER_PRDT_STAT_CODE_REFUND_ACCEPT; // 주문상품 상태코드 : 환수/환불접수
			clmStatCode = CommonCode.CLM_STAT_CODE_CANCEL_REFUND_ACCEPT; // 클레임 상태 코드(취소) : 환불접수
			clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_CANCEL_REFUND_ACCEPT; // 클레임상품 상태 코드(취소) ; 환불접수
		}

		/*
		 * 재경팀 처리용 환수/환불 대상 등록
		 */
		this.setRedempRefundMmnyProc(ocClaim);

		/*
		 * 원주문 상품상태 업데이트
		 */
		for (OcClaimProduct claimProduct : ocClaimAmountVO.getThisTimeClaimProductList()) {
			OcOrderProduct updateOrderProduct = new OcOrderProduct();

			try {
				updateOrderProduct.setOrderNo(ocClaim.getOrgOrderNo());
				updateOrderProduct.setOrderPrdtSeq(claimProduct.getOrderPrdtSeq());
				updateOrderProduct.setOrderPrdtStatCode(orderPrdtStatCode);
				updateOrderProduct.setModerNo(ocClaim.getMemberNo());

				ocOrderProductDao.updateOrderProduct(updateOrderProduct);
			} catch (Exception e) {
				log.error("주문상품상태 업데이트 에러 - updateOrderProduct={}", updateOrderProduct);
				e.printStackTrace();
			}
		}

		/*
		 * 원주문 상태 업데이트 : 전체취소 또는 이전 부분취소 이후 남은 상품 모두 취소인 경우
		 */
		if (ocClaimAmountVO.isAllCancelYn() || ocClaimAmountVO.isRemainAllCancelYn()) {
			OcOrder updateOrder = new OcOrder();

			try {
				updateOrder.setOrderNo(ocClaim.getOrgOrderNo());
				updateOrder.setOrderStatCode(orderStatCode);
				updateOrder.setModerNo(ocClaim.getMemberNo());

				ocOrderDao.updateOrderStat(updateOrder);
			} catch (Exception e) {
				log.error("원주문 상태 업데이트 에러 - updateOrder={}", updateOrder);
				e.printStackTrace();
			}
		}

		/*
		 * 리오더주문 상품상태 업데이트
		 */
		OcOrderProduct updateReOrderProduct = new OcOrderProduct();

		try {
			updateReOrderProduct.setOrderNo(reOrderNo);
			updateReOrderProduct.setOrderPrdtStatCode(orderPrdtStatCode);
			updateReOrderProduct.setModerNo(ocClaim.getMemberNo());

			ocOrderProductDao.updateOrderProduct(updateReOrderProduct);
		} catch (Exception e) {
			log.error("리오더주문 상품상태 업데이트 에러 - updateReOrderProduct={}", updateReOrderProduct);
			e.printStackTrace();
		}

		/*
		 * 리오더주문 상태 업데이트
		 */
		OcOrder updateReOrder = new OcOrder();

		try {
			updateReOrder.setOrderNo(reOrderNo);
			updateReOrder.setOrderStatCode(orderStatCode);
			updateReOrder.setModerNo(ocClaim.getMemberNo());

			ocOrderDao.updateOrderStat(updateReOrder);
		} catch (Exception e) {
			log.error("리오더주문 상태 업데이트 에러 - updateReOrder={}", updateReOrder);
			e.printStackTrace();
		}

		/*
		 * 원주문 이력 생성(취소완료 또는 환수/환불접수)
		 */
		for (OcOrderProduct orderProduct : ocClaimAmountVO.getOrgOrderProductList()) {
			OcOrderProduct reOrderProduct = new OcOrderProduct();
			BeanUtils.copyProperties(orderProduct, reOrderProduct); // 내용 복사

			// 원 주문의 주문상품이력 등록
			OcOrderProductHistory orgOrderProductHistory = new OcOrderProductHistory();

			try {
				orgOrderProductHistory.setOrderNo(orderProduct.getOrderNo());
				orgOrderProductHistory.setOrderPrdtSeq(reOrderProduct.getOrderPrdtSeq());
				orgOrderProductHistory.setPrdtNo(reOrderProduct.getPrdtNo());
				orgOrderProductHistory.setPrdtOptnNo(reOrderProduct.getPrdtOptnNo());
				orgOrderProductHistory.setPrdtName(reOrderProduct.getPrdtName());
				orgOrderProductHistory.setEngPrdtName(reOrderProduct.getEngPrdtName());
				orgOrderProductHistory.setOptnName(reOrderProduct.getOptnName());
				orgOrderProductHistory.setOrderPrdtStatCode(orderPrdtStatCode); // 주문상품상태코드
				orgOrderProductHistory.setNoteText(null);
				orgOrderProductHistory.setRgsterNo(ocClaim.getMemberNo());

				ocOrderProductHistoryDao.insertProductHistory(orgOrderProductHistory); // 주문상품이력 생성
			} catch (Exception e) {
				log.error("주문상품이력 등록 에러 - reOrderProduct={}", reOrderProduct);
				e.printStackTrace();
			}
		}

		/*
		 * 클레임 마스터 상태 업데이트
		 */
		OcClaim updateClaim = new OcClaim();
		updateClaim.setClmNo(ocClaim.getClmNo());
		updateClaim.setClmStatCode(clmStatCode);
		updateClaim.setModerNo(ocClaim.getMemberNo());

		try {
			ocClaimDao.updateClaimStat(updateClaim);
		} catch (Exception e) {
			log.error("클레임 마스터 상태 업데이트 에러 - updateClaim={}", updateClaim);
			e.printStackTrace();
		}

		/*
		 * 클레임상품 상태 업데이트
		 */
		OcClaimProduct updateClaimProduct = new OcClaimProduct();

		try {
			updateClaimProduct.setClmNo(ocClaim.getClmNo());
			updateClaimProduct.setClmPrdtStatCode(clmPrdtStatCode);
			updateClaimProduct.setModerNo(ocClaim.getMemberNo());

			ocClaimProductDao.updateClaimProductStatCode(updateClaimProduct);
		} catch (Exception e) {
			log.error("클레임상품 상태 업데이트 에러 - updateClaimProduct={}", updateClaimProduct);
			e.printStackTrace();
		}

		/*
		 * 클레임상태이력 등록
		 */
		List<OcClaimProduct> claimProdcutList = null;

		try {
			claimProdcutList = ocClaimProductDao.selectClaimProductList(ocClaim);
		} catch (Exception e) {
			log.error("클레임상품 조회 에러 - ocClaim={}", ocClaim);
			e.printStackTrace();
		}

		for (OcClaimProduct ocClaimProduct : claimProdcutList) {
			OcClaimStatusHistory ocClaimStatusHistory = new OcClaimStatusHistory();

			// 클레임상태이력 등록
			try {
				ocClaimStatusHistory.setClmNo(ocClaim.getClmNo());
				ocClaimStatusHistory.setClmPrdtSeq(ocClaimProduct.getClmPrdtSeq());
				ocClaimStatusHistory.setClmPrdtStatCode(clmPrdtStatCode); // 클레임상품상태코드
				ocClaimStatusHistory.setStockGbnCode(null);
				ocClaimStatusHistory.setNoteText(null);
				ocClaimStatusHistory.setRgsterNo(ocClaim.getRgsterNo()); // 등록자

				ocClaimStatusHistoryDao.insertClaimStatusHistory(ocClaimStatusHistory);
			} catch (Exception e) {
				log.error("클레임상태이력 등록 에러 - ocClaimStatusHistory={}", ocClaimStatusHistory);
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Desc : KCP 일반결제 취소/부분취소
	 * @Method Name : setCancelKcpPayment
	 * @Date : 2019. 5. 24.
	 * @Author : KTH
	 * @param claimPayment
	 * @param dpstChngRsnCode
	 * @param clmSeq
	 * @param isAllCancel
	 * @return
	 */
	public PaymentResult setCancelKcpPayment(OcClaim ocClaim, OcClaimPayment claimPayment, boolean isAllCancel) {

		PaymentResult paymentResult;
		long leftAmt = 0; // 부분취소 시 취소가능 잔여금
		String modType = ""; // 전체/부분취소구분
		String modDesc = ""; // 취소사유
		KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();

		if (isAllCancel) {
			modType = CommonCode.KCP_MOD_TYPE_STSC;
			modDesc = "전체취소";
		} else {
			modType = CommonCode.KCP_MOD_TYPE_STPC;
			modDesc = "부분취소";
		}

		kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
		kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));

		kcpPaymentCancel.setReqTx(""); // 요청종류 : 일반취소는 빈 값 처리
		kcpPaymentCancel.setModType(modType); // 전체취소 STSC / 부분취소 STPC
		kcpPaymentCancel.setCustIp("127.0.0.1"); // 요청 IP
		kcpPaymentCancel.setTno(claimPayment.getPymntAprvNoText()); // 거래번호
		kcpPaymentCancel.setModDesc(modDesc); // 취소사유

		if (!isAllCancel) { // 부분취소
			log.debug("############################## 부분취소이전에남은금액 : {}", claimPayment.getRealRemainPymntCnclAmt());
			kcpPaymentCancel.setModMny(String.valueOf(claimPayment.getPymntAmt())); // 취소요청금액
			kcpPaymentCancel.setRemMny(String.valueOf(claimPayment.getRealRemainPymntCnclAmt())); // 부분취소이전에남은금액
		}

		try {
			paymentResult = paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
		} catch (PaymentException e) {
			paymentResult = new PaymentResult("N");
			log.error("KCP 일반결제 취소/부분취소 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}

		return paymentResult;
	}

	/**
	 * <pre>
	 * KCP 에스크로 취소/부분취소
	 * modType
	 * - STE1 : 배송시작
	 * - STE2 : 즉시취소(배송 전 취소)
	 * - STE3 : 정산보류
	 * - STE4 : 취소(배송 후 취소)
	 * - STE5 : 발급계좌해지(가상계좌의 경우에만 사용)
	 * - STE9_C : 신용카드 구매 확인 후 취소
	 * - STE9_CP : 신용카드 구매 확인 후 부분취소
	 * - STE9_A : 계좌이체 구매 확인 후 취소
	 * - STE9_AP : 계좌이체 구매 확인 후 부분취소
	 * - STE9_AR : 계좌이체 구매 확인 후 환불
	 * - STE9_V : 가상계좌 구매 확인 후 환불
	 * - STE9_VP : 가상계좌 구매 확인 후 부분환불
	 * </pre>
	 * 
	 * @Desc : KCP 에스크로 취소/부분취소
	 * @Method Name : commonCancelKcpEscrowPayment
	 * @Date : 2019. 5. 22.
	 * @Author : KTH
	 * @param payment
	 * @param dpstChngRsnCode
	 * @param clmSeq
	 * @param modType
	 * @param criteria
	 * @param gubun
	 * @throws PaymentException
	 */
	public PaymentResult setCancelKcpEscrowPayment(OcClaim ocClaim, OcClaimPayment claimPayment, String modType,
			String gubun) {

		PaymentResult paymentResult;
		long leftAmt = 0;
		KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();

		kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.siteCode"));
		kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.siteKey"));

		kcpPaymentCancel.setReqTx(CommonCode.KCP_REQ_TX_MOD_ESCROW); // 요청종류
		kcpPaymentCancel.setCustIp("127.0.0.1"); // 요청 IP
		kcpPaymentCancel.setTno(claimPayment.getPymntAprvNoText()); // 거래번호
		kcpPaymentCancel.setModType(modType); // 환불타입
		kcpPaymentCancel.setModDesc("취소"); // 취소사유
		kcpPaymentCancel.setModDepositor(ocClaim.getAcntHldrName()); // 환불계좌주명(환불시에만 사용)
		kcpPaymentCancel.setModAccount(ocClaim.getRfndAcntText()); // 환불계좌번호(환불시에만 사용)
		kcpPaymentCancel.setModBankCode(ocClaim.getBankCode()); // 환불은행코드(환불시에만 사용)
		// 가상계좌여부???
		kcpPaymentCancel.setRemMny(String.valueOf(leftAmt)); // 환불 가능 금액(잔여금액)
		kcpPaymentCancel.setModMny(String.valueOf(claimPayment.getPymntAmt())); // 환불 금액

		try {
			paymentResult = paymentEntrance.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
		} catch (PaymentException e) {
			paymentResult = new PaymentResult("N");
			log.error("KCP 에스크로 취소/부분취소 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}

		return paymentResult;
	}

	/**
	 * @Desc : KCP 가상계좌 환불
	 * @Method Name : setVcntRefundKcpPayment
	 * @Date : 2019. 5. 24.
	 * @Author : KTH
	 * @param claimPayment
	 * @param dpstChngRsnCode
	 * @param clmSeq
	 * @param ocClaim
	 * @param isAllCancel
	 * @return
	 * @throws PaymentException
	 */
	public PaymentResult setVcntRefundKcpPayment(OcClaim ocClaim, OcClaimPayment claimPayment, boolean isAllCancel) {

		PaymentResult paymentResult;
		long leftAmt = 0; // 부분환불 시 취소가능 잔여금
		String modType = ""; // 전체/부분환불구분
		String modSubType = ""; // 환불요청타입 설정(전체환불-MDSC00,부분환불-MDSC03,복합과세부분환불-MDSC04)
		String modDesc = ""; // 환불요청에 대한 사유
		KcpPaymentRefund kcpPaymentRefund = new KcpPaymentRefund();

		if (isAllCancel) {
			modType = CommonCode.KCP_MOD_TYPE_STHD;
			modSubType = CommonCode.KCP_MOD_SUB_TYPE_MDSC00;
			modDesc = "취소환불";
		} else {
			modType = CommonCode.KCP_MOD_TYPE_STPD;
			modSubType = CommonCode.KCP_MOD_SUB_TYPE_MDSC03;
			modDesc = "부분환불";
		}

		kcpPaymentRefund.setSiteCd(Config.getString("pg.kcp.siteCode"));
		kcpPaymentRefund.setSiteKey(Config.getString("pg.kcp.siteKey"));

		kcpPaymentRefund.setReqTx(CommonCode.KCP_REQ_TX_MOD); // 요청종류
		kcpPaymentRefund.setModType(modType); // 전체환불
		kcpPaymentRefund.setModCompType(CommonCode.KCP_MOD_COMP_TYPE_MDCP01); // 인증타입(계좌인증+환불등록-MDCP01,(계좌+실명)인증+환불등록-MDCP02)
		kcpPaymentRefund.setModSubType(modSubType); // 환불요청타입설정(전체환불-MDSC00,부분환불-MDSC03,복합과세부분환불-MDSC04)
		kcpPaymentRefund.setTno(claimPayment.getPymntAprvNoText()); // KCP 거래 고유 번호
		kcpPaymentRefund.setModDesc(modDesc); // 환불요청에 대한 사유
		kcpPaymentRefund.setModAccount(ocClaim.getRfndAcntText()); // 계좌인증 및 환불 받을 계좌번호
		kcpPaymentRefund.setModDepositor(ocClaim.getAcntHldrName()); // 예금주
		kcpPaymentRefund.setModBankcode(ocClaim.getBankCode()); // 은행 코드
		kcpPaymentRefund.setCustIp("127.0.0.1"); // 요청 IP

		if (!isAllCancel) { // 부분환불
			kcpPaymentRefund.setModMny(claimPayment.getPymntAmt()); // 취소요청금액
			kcpPaymentRefund.setRemMny(claimPayment.getRealRemainPymntCnclAmt()); // 부분취소이전에남은금액
		}

		try {
			paymentResult = paymentEntrance.refund(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentRefund));
		} catch (PaymentException e) {
			paymentResult = new PaymentResult("N");
			log.error("KCP 가상계좌 환불 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}

		return paymentResult;
	}

	/**
	 * @Desc : 네이버페이 취소/부분취소
	 * @Method Name : setCancelNaverPayment
	 * @Date : 2019. 5. 24.
	 * @Author : KTH
	 * @param claimPayment
	 * @param ocClaim
	 * @param isAllCancel
	 * @return
	 * @throws PaymentException
	 */
	public PaymentResult setCancelNaverPayment(OcClaimPayment claimPayment, OcClaim ocClaim, boolean isAllCancel) {
		PaymentResult paymentResult;
		String cancelReason = "";
		NaverPaymentCancel naverPaymentCancel = new NaverPaymentCancel();

		if (isAllCancel) {
			cancelReason = "주문전체취소";
		} else {
			cancelReason = "주문부분취소";
		}

		naverPaymentCancel.setPaymentId(claimPayment.getPymntAprvNoText());
		naverPaymentCancel.setMerchantPayKey(ocClaim.getOrderNo());
		naverPaymentCancel.setCancelAmount((double) claimPayment.getPymntAmt());
		naverPaymentCancel.setTaxScopeAmount((double) claimPayment.getPymntAmt());
		naverPaymentCancel.setTaxExScopeAmount((double) 0);
		naverPaymentCancel.setCancelRequester("1"); // 취소요청자(1:구매자 2:가맹점관리자)
		naverPaymentCancel.setCancelReason(cancelReason);

		try {
			paymentResult = paymentEntrance
					.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_NAVER, naverPaymentCancel));
		} catch (Exception e) {
			paymentResult = new PaymentResult("N");
			log.error("네이버페이 취소/부분취소 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}

		return paymentResult;
	}

	/**
	 * @Desc : 카카오페이 취소
	 * @Method Name : setCancelKakaoPayment
	 * @Date : 2019. 5. 24.
	 * @Author : KTH
	 * @param claimPayment
	 * @param ocClaim
	 * @param isAllCancel
	 * @return
	 * @throws PaymentException
	 */
	public PaymentResult setCancelKakaoPayment(OcClaim ocClaim, OcClaimPayment claimPayment, boolean isAllCancel) {
		PaymentResult paymentResult = null;
		KakaoPaymentCancel kakaoPaymentCancel = new KakaoPaymentCancel();

		try {
			kakaoPaymentCancel.setCid(Config.getString("kakao.api.cid"));
			kakaoPaymentCancel.setTid(claimPayment.getPymntOrganNoText());
			kakaoPaymentCancel.setCancelAmount(claimPayment.getPymntAmt());
			kakaoPaymentCancel.setCancelTaxFreeAmount(claimPayment.getPymntAmt());

			paymentResult = paymentEntrance
					.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KAKAO, kakaoPaymentCancel));
		} catch (Exception e) {
			paymentResult = new PaymentResult("N");
			log.error("카카오페이 취소 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}

		return paymentResult;
	}

	/**
	 * @Desc : 기프트카드 취소(주문기준 취소, 결제기준 충전) - 취소 금액만큼 충전
	 * @Method Name : setCancelNicePayment
	 * @Date : 2019. 7. 12.
	 * @Author : KTH
	 * @param ocClaim
	 * @param claimPayment
	 * @param allCancelYn
	 * @return
	 */
	public Map<String, Object> setCancelNicePayment(OcClaim ocClaim, OcClaimPayment claimPayment, boolean allCancelYn) {

		Map<String, Object> resultMap = new HashMap<>();
		CommNiceRes<ChargeResponse> chargeResult = null;
		OcOrderPayment giftChargePayment = new OcOrderPayment();
		giftChargePayment.setMemberNo(ocClaim.getMemberNo()); // 회원번호
		giftChargePayment.setPymntOrganNoText(claimPayment.getPymntOrganNoText()); // 결제시 카드번호
		giftChargePayment.setGiftCardPinNoText(claimPayment.getGiftCardPinNoText()); // 결제시 핀번호
		int chargeGiftAmt = 0;

		try {
			// 대표카드 번호 조회
			// 대표카드가 없는 경우는 현재카드 번호(현재카드가 대표카드)
			giftChargePayment = ocOrderPaymentDao.selectByReturnRprsntCard(giftChargePayment);

			// 카드삭제 등의 이유로 조회가 안되는 경우는 임의 set
			if (giftChargePayment == null) {
				giftChargePayment = new OcOrderPayment();
				giftChargePayment.setMemberNo(ocClaim.getMemberNo()); // 회원번호
				giftChargePayment.setPymntOrganNoText(claimPayment.getPymntOrganNoText()); // 결제시 카드번호
				giftChargePayment.setGiftCardPinNoText(claimPayment.getGiftCardPinNoText()); // 결제시 핀번호
			}

			if (allCancelYn) { // 전체취소
				chargeGiftAmt = claimPayment.getPymntAmt();
			} else {
				chargeGiftAmt = claimPayment.getPymntAmt();
			}

			ChargeRequest chargeRequest = new ChargeRequest(giftChargePayment.getPymntOrganNoText(), chargeGiftAmt,
					"01", "3", "01", "1");

			chargeResult = niceGiftService.sendCharge(chargeRequest); // 기프트 충전

		} catch (Exception e) {
			chargeResult = new CommNiceRes<ChargeResponse>();
			chargeResult.setResCode("9999");
			log.error("기프트카드 취소 에러 - claimNo : {} orderNo : {}", ocClaim.getClmNo(), ocClaim.getOrderNo());
			e.printStackTrace();
		}

		resultMap.put("chargeResult", chargeResult);
		resultMap.put("giftCardNo", giftChargePayment.getPymntOrganNoText());
		resultMap.put("chargeGiftAmt", chargeGiftAmt);

		return resultMap;
	}

	/**
	 * @Desc : 포인트 사용 취소(구매/이벤트 포인트)
	 * @Method Name : setCancelOrderUsePoint
	 * @Date : 2019. 6. 17.
	 * @Author : KTH
	 * @param pointPayment
	 * @param eventPointPayment
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public boolean setCancelOrderUsePoint(OcClaim ocClaim, OcClaimPayment pointPayment,
			OcClaimPayment eventPointPayment, MbMember mbMember) {

		boolean success = true;
		Integer pointAmt = 0;
		Integer eventPointAmt = 0;
		String safeKey = "";

		try {
			safeKey = mbMember.getSafeKey();

			if (pointPayment != null) {
				pointAmt = pointPayment.getPymntAmt();
			}

			if (eventPointPayment != null) {
				eventPointAmt = eventPointPayment.getPymntAmt();
			}

			success = membershipPointService.updatePointForMembershipHandler(safeKey, pointAmt, "cancel",
					eventPointAmt);
		} catch (Exception e) {
			success = false;
			log.error("포인트 사용 취소(구매/이벤트 포인트) 에러 - claimNo : {} orderNo : {} ", ocClaim.getClmNo(),
					ocClaim.getOrderNo());
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * @Desc : 재경팀 처리용 환수/환불 대상 등록(취소를 진행하며 발생한 환수 또는 환불 결제금에 대한 sum을 별도 재경처리용으로 등록)
	 * @Method Name : saveDepositForPymntGubunNonRefundableOrder
	 * @Date : 2019. 6. 21.
	 * @Author : KTH
	 * @param ocClaim
	 * @throws Exception
	 */
	public void setRedempRefundMmnyProc(OcClaim ocClaim) {
		OcClaimPayment claimPyment = new OcClaimPayment();
		List<OcClaimPayment> claimPaymentList = null;

		try {
			claimPyment.setClmNo(ocClaim.getClmNo());

			claimPaymentList = ocClaimPaymentDao.selectClaimPaymentList(claimPyment);
		} catch (Exception e) {
			log.error("클레임결제 조회 에러 - claimPyment={}", claimPyment);
			e.printStackTrace();
		}

		/*
		 * 재경처리 환불금 대상 등록
		 */
		// 환불, 재경처리 N, 환수접수상태, 일반결제 대상으로 환불금 sum
		int refundAmtSum = claimPaymentList.stream()
				.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REFUND))
				.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE))
				.filter(x -> UtilsText.equals(x.getPymntStatCode(), CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT))
				.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC))
				.filter(x -> UtilsText.equals(x.getClmNo(), ocClaim.getClmNo())).mapToInt(o -> o.getPymntAmt()).sum();

		OcClaimPayment refundClaimPayment = new OcClaimPayment();

		if (refundAmtSum > 0) {
			// 재경처리 환불금 등록
			try {
				refundClaimPayment.setClmNo(ocClaim.getClmNo()); // 클레임번호
				refundClaimPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REFUND); // 환수환불구분
				refundClaimPayment.setPymntDtm(null); // 결제일시
				refundClaimPayment.setDeviceCode(ocClaim.getDeviceCode());
				refundClaimPayment.setMainPymntMeansYn(Const.BOOLEAN_FALSE); // 주결제 수단 여부 : 재경처리 데이터는 의미없음
				refundClaimPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT); // 결제수단코드 : 재경처리 데이터는
																									// 의미없음
				refundClaimPayment.setInstmtTermCount((short) 0); // 할부기간 : 재경처리 데이터는 의미없음
				refundClaimPayment.setPymntTodoAmt(refundAmtSum); // 결제예정금액
				refundClaimPayment.setPymntAmt(0); // 결제금액
				refundClaimPayment.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부 : 재경처리 데이터는 의미없음
				refundClaimPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부 : 재경처리 데이터는 의미없음
				refundClaimPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부 : 재경처리 데이터는 의미없음
				refundClaimPayment.setBankCode(ocClaim.getBankCode()); // 은행코드 : 환불:회원환불계좌
				refundClaimPayment.setAcntNoText(ocClaim.getRfndAcntText()); // 계좌번호
				refundClaimPayment.setAcntHldrName(ocClaim.getAcntHldrName()); // 예금주명
				refundClaimPayment.setAcntCrtfcYn(Const.BOOLEAN_TRUE); // 계좌인증여부
				refundClaimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_TRUE); // 자사처리대상여부 : 재경처리 Y
				refundClaimPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REFUND_ACCEPT); // 결제상태코드 : 환불접수
				refundClaimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_PUBLIC); // 이력구분 : 일반결제처리용
				refundClaimPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_AMT); // 발생사유코드 : 재경처리 데이터는 의미없음
				refundClaimPayment.setRgsterNo(ocClaim.getMemberNo());
				refundClaimPayment.setModerNo(ocClaim.getMemberNo());

				ocClaimPaymentDao.insertClaimPayment(refundClaimPayment);
			} catch (Exception e) {
				log.error("재경처리 환불금 등록 에러 - refundClaimPayment={}", refundClaimPayment);
				e.printStackTrace();
			}
		}

		/*
		 * 재경처리 환수금 대상 등록
		 */
		OcClaimPayment redempClaimPayment = new OcClaimPayment();

		// 환불, 재경처리 N, 환수접수상태, 일반결제 대상으로 환불금 sum
		int redempAmtSum = claimPaymentList.stream()
				.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP))
				.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE))
				.filter(x -> UtilsText.equals(x.getPymntStatCode(), CommonCode.PYMNT_STAT_CODE_REDEMP_ACCEPT))
				.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC))
				.filter(x -> UtilsText.equals(x.getClmNo(), ocClaim.getClmNo())).mapToInt(o -> o.getPymntAmt()).sum();

		if (redempAmtSum > 0) {
			// 재경처리 환수금 등록
			try {
				redempClaimPayment.setClmNo(ocClaim.getClmNo()); // 클레임번호
				redempClaimPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP); // 환수환불구분
				redempClaimPayment.setPymntDtm(null); // 결제일시
				redempClaimPayment.setDeviceCode(ocClaim.getDeviceCode());
				redempClaimPayment.setMainPymntMeansYn(Const.BOOLEAN_FALSE); // 주결제 수단 여부 : 재경처리 데이터는 의미없음
				redempClaimPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT); // 결제수단코드 : 재경처리 데이터는
																									// 의미없음
				redempClaimPayment.setInstmtTermCount((short) 0); // 할부기간 : 재경처리 데이터는 의미없음
				redempClaimPayment.setPymntTodoAmt(redempAmtSum); // 결제예정금액
				redempClaimPayment.setPymntAmt(0); // 결제금액
				redempClaimPayment.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부 : 재경처리 데이터는 의미없음
				redempClaimPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부 : 재경처리 데이터는 의미없음
				redempClaimPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부 : 재경처리 데이터는 의미없음
				redempClaimPayment.setBankCode(ocClaim.getBankCode()); // 은행코드 : 환수:환수가상계좌
				redempClaimPayment.setAcntNoText(ocClaim.getRfndAcntText()); // 계좌번호
				redempClaimPayment.setAcntHldrName(ocClaim.getAcntHldrName()); // 예금주명
				redempClaimPayment.setAcntCrtfcYn(Const.BOOLEAN_TRUE); // 계좌인증여부
				redempClaimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_TRUE); // 자사처리대상여부 : 재경처리 Y
				redempClaimPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_REDEMP_ACCEPT); // 결제상태코드 : 환불접수
				redempClaimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_PUBLIC); // 이력구분 : 일반결제처리용
				redempClaimPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_ORDER_AMT); // 발생사유코드 : 재경처리 데이터는 의미없음
				redempClaimPayment.setRgsterNo(ocClaim.getMemberNo());
				redempClaimPayment.setModerNo(ocClaim.getMemberNo());

				ocClaimPaymentDao.insertClaimPayment(redempClaimPayment);
			} catch (Exception e) {
				log.error("재경처리 환수금 등록 에러 - redempClaimPayment={}", redempClaimPayment);
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Desc : 주문 취소 인터페이스 호출(사은품, 배송비 제외한 본 상품 대상)
	 * @Method Name : setCancelOrderInterface
	 * @Date : 2019. 6. 18.
	 * @Author : KTH
	 * @param orderProduct
	 */
	public void callCancelOrderInterface(OcOrderProduct orderProduct) {
		try {
			if (ObjectUtils.isEmpty(orderProduct.getOrderGiftPrdtSeq())) { // 사은품이 없는 상품인 경우
				String cbcd = orderProduct.getStockGbnCode(); // B코드
				String maejangCd = orderProduct.getInsdMgmtInfoText(); // 매장코드
				String dlvyId = orderProduct.getDlvyIdText(); // 배송아이디
				String itemSno = String.valueOf(orderProduct.getOrderPrdtSeq()); // 서브키
				String sangpumFullCd = orderProduct.getVndrPrdtNoText().concat("001")
						.concat(orderProduct.getPrdtOptnNo()); // 상품풀코드
				Integer count = 0; // 수량
				interfacesClaimService.updateOrderPrdtNoGiftCancelNoTrx(cbcd, maejangCd, dlvyId, itemSno, sangpumFullCd,
						String.valueOf(count));
			} else { // 사은품이 있는 상품인 경우
				String cbcd = orderProduct.getStockGbnCode(); // B코드
				String maejangCd = orderProduct.getInsdMgmtInfoText(); // 매장코드
				String dlvyId = orderProduct.getDlvyIdText(); // 배송아이디
				String freeGiftItemSno = String.valueOf(orderProduct.getOrderPrdtSeq()).concat("F")
						.concat(String.valueOf(orderProduct.getGiftPrdtNo())); // 서브키
				String sangpumFullCd = orderProduct.getVndrPrdtNoText().concat("001")
						.concat(orderProduct.getPrdtOptnNo()); // 상품풀코드
				Integer count = 0; // 수량
				interfacesClaimService.updateOrderPrdtNoGiftCancelNoTrx(cbcd, maejangCd, dlvyId, freeGiftItemSno,
						sangpumFullCd, String.valueOf(count));
			}
		} catch (Exception e) {
			log.error("주문취소 인터페이스 에러 - orderNo : {}, orderPrdtSeq{} ", orderProduct.getOrderNo(),
					orderProduct.getOrderPrdtSeq());
			e.printStackTrace();
		}
	}

}
