package kr.co.shop.web.claim.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.shop.common.account.AccountAuth;
import kr.co.shop.common.config.Config;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsArray;
import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.common.util.UtilsObject;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.member.MembershipPointService;
import kr.co.shop.interfaces.module.payment.PaymentEntrance;
import kr.co.shop.interfaces.module.payment.base.PaymentResult;
import kr.co.shop.interfaces.module.payment.base.model.PaymentInfo;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApprovalReturn;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancel;
import kr.co.shop.interfaces.zinterfacesdb.service.InterfacesClaimService;
import kr.co.shop.web.claim.model.master.OcClaim;
import kr.co.shop.web.claim.model.master.OcClaimCertificationHistory;
import kr.co.shop.web.claim.model.master.OcClaimPayment;
import kr.co.shop.web.claim.model.master.OcClaimProduct;
import kr.co.shop.web.claim.model.master.OcClaimProductApplyPromotion;
import kr.co.shop.web.claim.model.master.OcClaimStatusHistory;
import kr.co.shop.web.claim.repository.master.OcClaimCertificationHistoryDao;
import kr.co.shop.web.claim.repository.master.OcClaimDao;
import kr.co.shop.web.claim.repository.master.OcClaimPaymentDao;
import kr.co.shop.web.claim.repository.master.OcClaimProductApplyPromotionDao;
import kr.co.shop.web.claim.repository.master.OcClaimProductDao;
import kr.co.shop.web.claim.repository.master.OcClaimStatusHistoryDao;
import kr.co.shop.web.claim.vo.OcClaimAmountVO;
import kr.co.shop.web.customer.repository.master.CmOnlineMemberPolicyDao;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.repository.master.MbMemberCouponDao;
import kr.co.shop.web.member.repository.master.MbMemberDao;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.mypage.service.MypageService;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderDeliveryHistory;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.model.master.OcOrderProductHistory;
import kr.co.shop.web.order.model.master.OcOrderUseCoupon;
import kr.co.shop.web.order.repository.master.OcOrderDao;
import kr.co.shop.web.order.repository.master.OcOrderDeliveryHistoryDao;
import kr.co.shop.web.order.repository.master.OcOrderProductDao;
import kr.co.shop.web.order.repository.master.OcOrderProductHistoryDao;
import kr.co.shop.web.order.repository.master.OcOrderUseCouponDao;
import kr.co.shop.web.order.service.OrderService;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.service.ProductOptionService;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.repository.master.SySiteDao;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClaimService {

	@Autowired
	OcClaimDao ocClaimDao;

	@Autowired
	OcOrderDao ocOrderDao;

	@Autowired
	OcOrderProductDao ocOrderProductDao;

	@Autowired
	OcOrderProductHistoryDao ocOrderProductHistoryDao;

	@Autowired
	OcOrderDeliveryHistoryDao ocOrderDeliveryHistoryDao;

	@Autowired
	OcClaimProductDao ocClaimProductDao;

	@Autowired
	OcClaimStatusHistoryDao ocClaimStatusHistoryDao;

	@Autowired
	CommonCodeService commonCodeService;

	@Autowired
	private MypageService mypageService;

	@Autowired
	private OcClaimCertificationHistoryDao ocClaimCertificationHistoryDao;

	@Autowired
	private OcClaimPaymentDao ocClaimPaymentDao;

	@Autowired
	private MbMemberCouponDao mbMemberCouponDao;

	@Autowired
	private CmOnlineMemberPolicyDao cmOnlineMemberPolicyDao;

	@Autowired
	private ClaimProcService claimProcService;

	@Autowired
	private ClaimMessageService claimMessageService;

	@Autowired
	private PaymentEntrance payment;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductOptionService productOptionService;

	@Autowired
	private OrderService orderService;

	@Autowired
	InterfacesClaimService interfacesClaimService;

	@Autowired
	SySiteDao sySiteDao;

	@Autowired
	OcOrderUseCouponDao ocOrderUseCouponDao;

	@Autowired
	MembershipPointService membershipPointService;

	@Autowired
	MemberService memberService;

	@Autowired
	MbMemberDao mbMemberDao;

	@Autowired
	OcClaimProductApplyPromotionDao ocClaimProductApplyPromotionDao;

	/**
	 * @Desc : 주문취소/교환/반품 목록 클레임 상품 목록 조회
	 * @Method Name : getClaimProductList
	 * @Date : 2019. 4. 16.
	 * @Author : 이강수
	 * @param Parameter<OcClaim> parameter
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClaimProductList(Parameter<OcClaim> parameter) throws Exception {

		Pageable<OcClaim, OcClaim> pageable = new Pageable<>(parameter);

		// 페이지 세팅
		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> map = new HashMap<>();

		int totalCount = ocClaimDao.selectClaimListCountForPaging(pageable);

		// MO 데이터 없을때
		if (totalCount < 1) {
			map.put("noData", true);
		}

		if (totalCount > 0) {

			List<OcClaim> claimList = ocClaimDao.selectClaimListForPaging(pageable);

			// 사은품, 배송비 제외
			String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };

			for (OcClaim ocClaim : claimList) {

				OcClaim param = new OcClaim();
				param.setClmNo(ocClaim.getClmNo());
				// 사은품, 배송비 제외
				param.setPrdtTypeCodeList(prdtTypeCodeList);
				// 상품관련파일순번 1 (대표)
				param.setPrdtRltnFileSeq(parameter.get().getPrdtRltnFileSeq());

				List<OcClaimProduct> claimProductList = ocClaimProductDao.selectClaimProductList(param);

				if (!UtilsArray.isEmpty(claimProductList)) {

					ocClaim.setOcClaimProductList(claimProductList);
					ocClaim.setOcClaimProductListSize(claimProductList.size());
				}
			}

			map.put("content", claimList);

			// MO 스크롤
			if (claimList.size() < parameter.get().getRowsPerPage()) {
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
	 * @Desc : 클레임 접수철회 업데이트
	 * @Method Name : updateClaimWthdraw
	 * @Date : 2019. 4. 19.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public Map<String, Object> updateClaimWthdraw(OcClaim ocClaim) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		String clmStatCode = "";
		String clmPrdtStatCode = "";

		switch (ocClaim.getClmGbnCode()) {

		// 취소
		case CommonCode.CLM_GBN_CODE_CANCEL:

			// 클레임 상태 코드(취소) - 취소접수철회
			clmStatCode = CommonCode.CLM_STAT_CODE_CANCEL_WITHDRAWAL_ACCEPT;
			// 클레임상품 상태 코드(취소) - 취소접수철회
			clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_CANCEL_WITHDRAWAL_ACCEPT;

			break;

		// 교환
		case CommonCode.CLM_GBN_CODE_EXCHANGE:

			// 클레임 상태 코드(교환) - 교환접수철회
			clmStatCode = CommonCode.CLM_STAT_CODE_EXCHANGE_WITHDRAWAL_ACCEPT;
			// 클레임상품 상태 코드(교환) - 교환접수철회
			clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_EXCHANGE_WITHDRAWAL_ACCEPT;

			break;

		// 반품
		case CommonCode.CLM_GBN_CODE_RETURN:

			// 클레임 상태 코드(반품) - 반품접수철회
			clmStatCode = CommonCode.CLM_STAT_CODE_RETURN_WITHDRAWAL_ACCEPT;
			// 클레임상품 상태 코드(반품) - 반품접수철회
			clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_RETURN_WITHDRAWAL_ACCEPT;

			break;

		default:
			break;
		}

		/**
		 * 클레임 상품 상태코드 업데이트
		 */
		OcClaimProduct ocClaimProduct = new OcClaimProduct();
		// 클레임상품 수정자번호
		ocClaimProduct.setModerNo(ocClaim.getMemberNo());
		// 클레임번호
		ocClaimProduct.setClmNo(ocClaim.getClmNo());
		// 클레임상품 상태코드
		ocClaimProduct.setClmPrdtStatCode(clmPrdtStatCode);

		ocClaimProductDao.updateClaimProductStatCode(ocClaimProduct);

		/**
		 * 클레임상태이력 등록
		 */
		OcClaimStatusHistory ocClaimStatusHistory = new OcClaimStatusHistory();
		ocClaimStatusHistory.setClmNo(ocClaim.getClmNo());
		ocClaimStatusHistory.setClmPrdtStatCode(clmPrdtStatCode); // 클레임상품상태코드
		ocClaimStatusHistory.setStockGbnCode(null);
		ocClaimStatusHistory.setNoteText(null);
		ocClaimStatusHistory.setRgsterNo(ocClaim.getMemberNo()); // 등록자

		for (OcClaimProduct cp : ocClaim.getOcClaimProducts()) {

			ocClaimStatusHistory.setClmPrdtSeq(cp.getClmPrdtSeq());
			ocClaimStatusHistoryDao.insertClaimStatusHistory(ocClaimStatusHistory);
		}

		/**
		 * 클레임 상태코드 업데이트
		 */
		ocClaim.setModerNo(ocClaim.getMemberNo());
		ocClaim.setClmStatCode(clmStatCode);

		ocClaimDao.updateClaimWthdraw(ocClaim);

		return resultMap;
	}

	/**
	 * @Desc : 클레임 상세 업데이트
	 * @Method Name : updateClaimDetail
	 * @Date : 2019. 4. 29.
	 * @Author : 이강수
	 * @param OcClaim
	 * @throws Exception
	 */
	public Map<String, Object> updateClaimDetail(OcClaim ocClaim) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		ocClaimDao.updateClaimDetail(ocClaim);

		for (OcClaimProduct claimProduct : ocClaim.getOcClaimProducts()) {

			ocClaimProductDao.updateClmRsnCodeText(claimProduct); // 회수상품 수정

			OcClaimProduct exchangeClaimProduct = new OcClaimProduct(); // 재배송 상품
			// TODO-KTH : 재고체크
			BeanUtils.copyProperties(claimProduct, exchangeClaimProduct);
			exchangeClaimProduct.setUpClmPrdtSeq(claimProduct.getClmPrdtSeq()); // 회수 상품 기준의 클레임상품순번

			if (UtilsText.equals(claimProduct.getClmRsnCode(), CommonCode.CLM_RSN_CODE_EXCHANGE_OPTION_CHANGE)) { // 옵션변경
				exchangeClaimProduct.setPrdtOptnNo(claimProduct.getChangePrdtOptnNo()); // 변경옵션번호
				exchangeClaimProduct.setOptnName(claimProduct.getChangeOptnName()); // 변경옵션명
			} else {
				exchangeClaimProduct.setPrdtOptnNo(claimProduct.getPrdtOptnNo()); // 회수상품 기준 옵션번호
				exchangeClaimProduct.setOptnName(claimProduct.getOptnName()); // 회수상품 기준 옵션명
			}

			ocClaimProductDao.updateClmChangeOptn(exchangeClaimProduct); // 재배송 상품 수정

		}

		return resultMap;
	}

	/**
	 * @Desc : 상품 판매종료인지 아닌지 Y/N 리턴
	 * @Method Name : getProductSellEndYn
	 * @Date : 2019. 6. 13.
	 * @Author : 이강수
	 * @param OcClaimProduct
	 * @return PdProduct
	 * @throws Exception
	 */
	public PdProduct getProductSellEndYn(OcClaimProduct param) throws Exception {

		PdProduct pdProduct = productService.getProduct(param.getPrdtNo());

		if (pdProduct != null) {

			if (pdProduct.getSellStatCode().equals(CommonCode.SELL_STAT_CODE_END)) {
				// 해당 상품이 '판매종료'인 경우
				pdProduct.setIsSellEndYn(Const.BOOLEAN_TRUE);
			} else {
				// 해당 상품이 '판매종료'가 아닌 경우
				pdProduct.setIsSellEndYn(Const.BOOLEAN_FALSE);
			}
		}

		return pdProduct;
	}

	/**
	 * @Desc : 취소상품 장바구니 담기
	 * @Method Name : setCancelPrdtToCart
	 * @Date : 2019. 6. 20.
	 * @Author : 이강수
	 * @param Parameter<?>
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setCancelPrdtToCart(OcClaim param) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		List<PdProductWithAll> powspList = null;

		/**
		 * 상품 판매상태 체크
		 */
		List<OcClaimProduct> cartPrdtList = new ArrayList<OcClaimProduct>();

		if (!UtilsArray.isEmpty(param.getOcClaimProducts())) {

			// 해당 상품이 판매가능한지 validate
			for (OcClaimProduct ocClaimProduct : param.getOcClaimProducts()) {

				powspList = productOptionService.getProductOptionListWithStock(ocClaimProduct.getSiteNo(),
						ocClaimProduct.getPrdtNo(), ocClaimProduct.getPrdtOptnNo());

				for (PdProductWithAll pdProductWithAll : powspList) {

					if (pdProductWithAll.getPrdtOptnNo().equals(ocClaimProduct.getPrdtOptnNo())) {

						log.debug(" ========================================= ");
						log.debug(" ======== " + pdProductWithAll.getPrdtNo());
						log.debug(" ======== " + pdProductWithAll.getPrdtName());
						log.debug(" ======== " + pdProductWithAll.getPrdtOptnNo());
						log.debug(" ======== " + pdProductWithAll.getSellStatCode());
						log.debug(" ======== " + pdProductWithAll.getTotalStockQty());
						log.debug(" ======== " + pdProductWithAll.getUseYn());
						log.debug(" ========================================= ");

						String optnSellStatCode = pdProductWithAll.getSellStatCode();
						int optnStockQty = pdProductWithAll.getTotalStockQty();
						String optnUseYn = pdProductWithAll.getUseYn();

						// 해당 상품 해당 옵션이 판매가능한지 validate : 통과라면 "Y" 아니면 "N"
						if (optnSellStatCode.equals(CommonCode.SELL_STAT_CODE_SALE) && optnStockQty > 0
								&& optnUseYn.equals(Const.BOOLEAN_TRUE)) {
							// 판매상태코드 : 판매중 / 재고 > 0 / 사용여부 : Y / 이라면
							ocClaimProduct.setIsSellYn(Const.BOOLEAN_TRUE);
						} else {
							// 판매상태코드 : 판매중 / 재고 > 0 / 사용여부 : Y / 아니라면
							ocClaimProduct.setIsSellYn(Const.BOOLEAN_FALSE);
						}
					}
				}

				cartPrdtList.add(ocClaimProduct);

				if (!UtilsArray.isEmpty(cartPrdtList)) {
					resultMap.put("cartPrdtList", cartPrdtList);
				}
			}
		}

		return resultMap;
	}

	/*********************************************************************************************************************
	 * E : 이강수
	 *********************************************************************************************************************/

	/*********************************************************************************************************************
	 * S : kth
	 *********************************************************************************************************************/

	/**
	 * @Desc : 클레임 상세 조회
	 * @Method Name : getClaimDetail
	 * @Date : 2019. 4. 22.
	 * @Author : 이강수, KTH
	 * @param OcClaim
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClaimDetailInfo(OcClaim param) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		param.setPymntMeansCodeVirtualAccount(CommonCode.PYMNT_MEANS_CODE_VIRTUAL_ACCOUNT);

		/**
		 * 클레임 마스터
		 */
		OcClaim detail = ocClaimDao.selectClaimDetail(param);

		// 사이트 정책 정보
		SySite sySite = sySiteDao.selectSite(detail.getSiteNo());
		resultMap.put("siteInfo", sySite);

		/**
		 * 클레임 상품 정보
		 */
		// 사은품, 배송비 제외
		String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };
		param.setPrdtTypeCodeList(prdtTypeCodeList);

		// 클레임 상품에서 주문배송비를 추출하기 위해 배송비 유형 set
		param.setPrdtTypeCode(CommonCode.PRDT_TYPE_CODE_DELIVERY);

		List<OcClaimProduct> clmPrdtList = ocClaimProductDao.selectClaimProductList(param);

		if (!UtilsArray.isEmpty(clmPrdtList)) {

			detail.setClmRsnCodeName(clmPrdtList.get(0).getClmRsnCodeName());
			detail.setClmEtcRsnText(clmPrdtList.get(0).getClmEtcRsnText());

			// 클레임 상품 리스트(자사/업체 구분 없는 모든 클레임 상품)
			resultMap.put("prdtList", clmPrdtList);

			// 취소 클레임인 경우 자사클레임상품과 업체클레임상품 구분
			if (UtilsText.equals(detail.getClmGbnCode(), CommonCode.CLM_GBN_CODE_CANCEL)) {
				// 자사 클레임상품
				resultMap.put("mmnyList",
						clmPrdtList.stream().filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_TRUE))
								.collect(Collectors.toList()));

				// 업체 클레임상품 분리
				List<OcClaimProduct> vendorClaimProductList = clmPrdtList.stream()
						.filter(x -> UtilsText.equals(x.getMmnyPrdtYn(), Const.BOOLEAN_FALSE))
						.collect(Collectors.toList());

				// 업체 기준으로 중복제거
				List<String> vendorList = vendorClaimProductList.stream().map(OcClaimProduct::getVndrNo).distinct()
						.collect(Collectors.toCollection(ArrayList::new));

				List<OcClaimProduct> vendorGroupClaimProductList = new ArrayList<OcClaimProduct>();

				for (String vendorNo : vendorList) {
					OcClaimProduct claimProduct = new OcClaimProduct();

					claimProduct.setVndrNo(vendorNo);
					claimProduct.setClaimProductList(vendorClaimProductList.stream()
							.filter(o -> UtilsText.equals(o.getVndrNo(), vendorNo)).collect(Collectors.toList()));

					vendorGroupClaimProductList.add(claimProduct);
				}

				// 업체 클레임상품
				resultMap.put("vndrList", vendorGroupClaimProductList);

			}
		}

		/**
		 * 클레임철회사유, 클레임사유 코드 조회
		 */
		String[] codeFields = { CommonCode.CLM_WTHDRAW_RSN_CODE, CommonCode.CLM_RSN_CODE, CommonCode.BANK_CODE };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);

		resultMap.put(CommonCode.CLM_WTHDRAW_RSN_CODE, codeList.get(CommonCode.CLM_WTHDRAW_RSN_CODE));
		resultMap.put(CommonCode.CLM_RSN_CODE, codeList.get(CommonCode.CLM_RSN_CODE));
		resultMap.put(CommonCode.BANK_CODE, codeList.get(CommonCode.BANK_CODE));

		/**
		 * 클레임 결제 정보
		 */
		OcClaimPayment ocClaimPayment = new OcClaimPayment();
		ocClaimPayment.setClmNo(param.getClmNo());
		List<OcClaimPayment> claimPaymentList = ocClaimPaymentDao.selectClaimPaymentList(ocClaimPayment);

		if (!UtilsArray.isEmpty(claimPaymentList)) {
			/*
			 * 교환/반품 배송비 결제 정보
			 */
			// 환수환불구분 : 환수
			// 자사처리대상여부 : N
			// 이력구분타입 : 일반결제
			// 결제발생사유코드 : (클레임)배송비
			List<OcClaimPayment> dlvyAmtPymntList = claimPaymentList.stream()
					.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP))
					.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE))
					.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC))
					.filter(x -> UtilsText.equals(x.getOcrncRsnCode(), CommonCode.OCRNC_RSN_CODE_DELIVERY_AMT))
					.collect(Collectors.toList());

			// 신용카드정보 마스킹
			for (OcClaimPayment claimPymnt : dlvyAmtPymntList) {
				if (claimPymnt != null) {
					if (claimPymnt.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
						// 결제수단이 '신용카드/체크카드'라면 마스킹
						claimPymnt.setCardMask(
								UtilsMasking.cardNumber(claimPymnt.getPymntOrganNoText().replaceAll("-", ""), false));
					}
				}
			}

			resultMap.put("dlvyAmtPymntList", dlvyAmtPymntList);

			/*
			 * 취소/교환/반품 클레임 - 환수정보(추가결제금액)
			 */
			// 환수환불구분 : 환수
			// 자사처리대상여부 : N
			// 이력구분타입 : 일반결제
			List<OcClaimPayment> redempPymntAmtList = claimPaymentList.stream()
					.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP))
					.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE))
					.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_PUBLIC))
					.filter(x -> !UtilsText.equals(x.getOcrncRsnCode(), CommonCode.OCRNC_RSN_CODE_DELIVERY_AMT))
					.collect(Collectors.toList());

			// 신용카드정보 마스킹
			for (OcClaimPayment claimPymnt : redempPymntAmtList) {
				if (claimPymnt != null) {
					if (claimPymnt.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
						// 결제수단이 '신용카드/체크카드'라면 마스킹
						claimPymnt.setCardMask(
								UtilsMasking.cardNumber(claimPymnt.getPymntOrganNoText().replaceAll("-", ""), false));
					}
				}
			}

			resultMap.put("redempPymntAmtList", redempPymntAmtList);

			int redempPymntAmtSum = redempPymntAmtList.stream().mapToInt(x -> x.getPymntAmt()).sum();

			detail.setTotalRedempAmt(redempPymntAmtSum);

			/*
			 * 클레임 환불 영역 정보
			 */
			// 환수환불구분 : 환불
			// 자사처리대상여부 : N
			// 이력구분타입 : 이력근거용(클레임 배송비는 제외)
			List<OcClaimPayment> refundPymntAmtList = claimPaymentList.stream()
					.filter(x -> UtilsText.equals(x.getRedempRfndGbnType(), CommonCode.REDEMP_RFND_GBN_TYPE_REFUND))
					.filter(x -> UtilsText.equals(x.getMmnyProcTrgtYn(), Const.BOOLEAN_FALSE))
					.filter(x -> UtilsText.equals(x.getHistGbnType(), CommonCode.HIST_GBN_TYPE_HISTORY))
					.collect(Collectors.toList());

			resultMap.put("refundPymntAmtList", refundPymntAmtList);

			// 신용카드정보 마스킹
			for (OcClaimPayment claimPymnt : refundPymntAmtList) {
				if (claimPymnt != null) {
					if (claimPymnt.getPymntMeansCode().equals(CommonCode.PYMNT_MEANS_CODE_CARD)) {
						// 결제수단이 '신용카드/체크카드'라면 마스킹
						claimPymnt.setCardMask(
								UtilsMasking.cardNumber(claimPymnt.getPymntOrganNoText().replaceAll("-", ""), false));
					}
				}
			}

			int refundPymntAmtSum = refundPymntAmtList.stream().mapToInt(x -> x.getPymntAmt()).sum();

			detail.setTotalRfndAmt(refundPymntAmtSum);
		}

		/**
		 * 취소/반품 클레임 - 환불정보 - 취소금액영역 (주문금액/환불배송비)
		 */
		OcClaimProduct ocClaimProduct = new OcClaimProduct();
		ocClaimProduct.setClmNo(param.getClmNo());
		ocClaimProduct.setPrdtTypeCodeGift(CommonCode.PRDT_TYPE_CODE_GIFT);
		ocClaimProduct.setPrdtTypeCodeDelivery(CommonCode.PRDT_TYPE_CODE_DELIVERY);

		OcClaim cancelAmtModel = ocClaimDao.selectSumOrderAmtSumDlvyAmt(ocClaimProduct);

		if (cancelAmtModel != null) {
			resultMap.put("cancelAmtModel", cancelAmtModel);
		}

		/**
		 * 최종적으로 클레임 마스터를 담는다.
		 */
		if (detail != null) {
			resultMap.put("detail", detail);
		}

		return resultMap;
	}

	/**
	 * @Desc : 클레임 환불계좌 인증
	 * @Method Name : setClaimAccountAuthProcess
	 * @Date : 2019. 5. 3.
	 * @Author : KTH
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setClaimAccountAuthProc(OcClaimCertificationHistory ocClaimCertificationHistory)
			throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		AccountAuth accountAuth = new AccountAuth();

		paramMap = mypageService.setAccountParam(ocClaimCertificationHistory.getMbMember()); // 계좌인증에 필요한 파라미터를 세팅
		Map<String, Object> result = accountAuth.start(paramMap);

		/*
		 * 인증이력 생성(실패, 성공 구분 없이 생성)
		 */
		String authSuccessYn = "N";
		if (UtilsObject.isNotEmpty(result.get("flag")) && !UtilsText.isEmpty((String) result.get("flag"))) {
			authSuccessYn = (String) result.get("flag");
		}

		ocClaimCertificationHistory.setCrtfcSuccessYn(authSuccessYn); // 인증성공여부 set

		ocClaimCertificationHistoryDao.insert(ocClaimCertificationHistory); // 인증이력 생성

		return result;
	}

	/**
	 * @Desc : 클레임 취소 처리
	 * @Method Name : setClaimCancelProc
	 * @Date : 2019. 5. 9.
	 * @Author : KTH
	 * @param ocClaim
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setClaimCancelProc(OcClaim ocClaim) throws Exception {
		OcClaimAmountVO ocClaimAmountVO = new OcClaimAmountVO();

		// TODO-KTH : 기본 체크 및 적립예정 포인트 조회

		// 클레임 금액계산 정보(전체/부분 취소 여부만 확인)
		claimProcService.setAllCancelCheck(ocClaim, ocClaimAmountVO);

		if (ocClaimAmountVO.isAllCancelYn()) {
			return this.setClaimAllCancelProc(ocClaim, ocClaimAmountVO);
		} else {
			return this.setClaimPartCancelProc(ocClaim, ocClaimAmountVO);
		}

	}

	/**
	 * @Desc : 클레임 전체 취소 처리
	 * @Method Name : setClaimAllCancelProc
	 * @Date : 2019. 7. 1.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocClaimAmountVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setClaimAllCancelProc(OcClaim ocClaim, OcClaimAmountVO ocClaimAmountVO)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		/*
		 * [원 주문정보 get]
		 */
		OcOrder ocOrder = new OcOrder();
		ocOrder.setOrderNo(ocClaim.getOrgOrderNo());
		ocOrder = ocOrderDao.selectOrderDetail(ocOrder); // 주문기본정보

		/*
		 * [주문자 정보]
		 */
		MbMember mbMemberParam = new MbMember();
		mbMemberParam.setMemberNo(ocOrder.getMemberNo());
		MbMember mbMember = memberService.selectMemberInfo(mbMemberParam);

		/*
		 * [기본 등록정보 set]
		 */
		ocClaim.setMemberNo(ocOrder.getMemberNo()); // 인서트 시 주문정보를 이용하나 ocClaim 재 사용을 위해 set
		ocClaim.setClmGbnCode(CommonCode.CLM_GBN_CODE_CANCEL); // 클레임구분코드-취소
		ocClaim.setDeviceCode(ocOrder.getDeviceCode()); // 디바이스코드

		ocClaim.setRtrvlGbnType(CommonCode.RTRVL_GBN_TYPE_ONLINE); // 회수지구분(O:온라인)
		ocClaim.setRtrvlStoreNo(null); // 회수매장번호
		ocClaim.setRtrvlReqYn(Const.BOOLEAN_FALSE); // 회수신청여부

		ocClaim.setBuyerName(ocOrder.getBuyerName());
		ocClaim.setBuyerTelNoText(ocOrder.getBuyerTelNoText());
		ocClaim.setBuyerHdphnNoText(ocOrder.getBuyerHdphnNoText());
		ocClaim.setBuyerPostCodeText(ocOrder.getBuyerPostCodeText());
		ocClaim.setBuyerPostAddrText(ocOrder.getBuyerPostAddrText());
		ocClaim.setBuyerDtlAddrText(ocOrder.getBuyerDtlAddrText());

		ocClaim.setRcvrName(ocOrder.getRcvrName());
		ocClaim.setRcvrTelNoText(ocOrder.getRcvrTelNoText());
		ocClaim.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());
		ocClaim.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());
		ocClaim.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());
		ocClaim.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());

		ocClaim.setDlvyMemoText(ocOrder.getDlvyMemoText());
		ocClaim.setDlvyTypeCode(ocOrder.getDlvyTypeCode()); // 배송유형코드

		ocClaim.setVndrNo(ocClaim.getOcClaimProductList().get(0).getVndrNo()); // 업체번호(클레임상품 중 한개 발췌-업체단위로 클레임 걸림)

		ocClaim.setTotalRfndAmt(ocOrder.getPymntAmt()); // 총환불금액
		ocClaim.setTotalRedempAmt(0); // 총환수금액

		ocClaim.setUnProcYn(Const.BOOLEAN_FALSE); // 미처리 여부 set
		ocClaim.setAdminAcceptYn(Const.BOOLEAN_FALSE); // 관리자접수여부
		ocClaim.setOflnAcceptYn(Const.BOOLEAN_FALSE); // 오프라인접수여부

		// 추가배송비 관련 set
		ocClaim.setAddDlvyAmtPymntType(null); // 추가배송비결제방법
		ocClaim.setAddDlvyAmt(0); // 추가배송비
		ocClaim.setHoldCpnSeq(null); // 보유쿠폰순번

		ocClaim.setClmStatCode(CommonCode.CLM_STAT_CODE_CANCEL_ACCEPT); // 클레임상태코드 - 취소접수

		/*
		 * [클레임 등록(마스터)] 등록 시 생성된 clmNo 를 ocClaim 에서 가지고 있음
		 */
		ocClaimDao.insertClaimInfo(ocClaim);

		/*
		 * [클레임상품 등록] [클레임상태이력 등록]
		 */
		for (OcClaimProduct ocClaimProduct : ocClaim.getOcClaimProductList()) {
			// 클레임상품 등록, 클레임상태이력 등록
			this.insertClaimPrdtAndClaimStatusHistory(ocClaim, ocClaimProduct,
					CommonCode.CLM_PRDT_STAT_CODE_CANCEL_ACCEPT);

			// 해당 상품에 사은품이 있는 경우 사은품 한번 더 등록
			if (!ObjectUtils.isEmpty(ocClaimProduct.getOrderGiftPrdtSeq())) {
				ocClaimProduct.setOrderPrdtSeq(ocClaimProduct.getOrderGiftPrdtSeq()); // 사은품 주문상품순번으로 대체
				this.insertClaimPrdtAndClaimStatusHistory(ocClaim, ocClaimProduct,
						CommonCode.CLM_PRDT_STAT_CODE_CANCEL_ACCEPT);
			}
		}

		/*
		 * 클레임 금액 계산(클레임 마스터/상품 등록 후 호출)
		 */
		claimProcService.setClaimAmountCalcForAllCancel(ocClaim, ocOrder, ocClaimAmountVO);

		/*
		 * 클레임 상품에 배송비 등록
		 */
		if (ocClaimAmountVO.isAllCancelYn()) {
			for (OcOrderProduct orderProduct : ocClaimAmountVO.getOrgOrderProductList()) {
				if (UtilsText.equals(orderProduct.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) { // 배송비
					OcClaimProduct dlvyClaimProduct = new OcClaimProduct();

					dlvyClaimProduct.setOrderPrdtSeq(orderProduct.getOrderPrdtSeq());
					dlvyClaimProduct.setClmRsnCode(ocClaim.getOcClaimProductList().get(0).getClmRsnCode());
					dlvyClaimProduct.setClmEtcRsnText(ocClaim.getOcClaimProductList().get(0).getClmEtcRsnText());

					// 클레임상품 등록, 클레임상태이력 등록
					this.insertClaimPrdtAndClaimStatusHistory(ocClaim, dlvyClaimProduct,
							CommonCode.CLM_PRDT_STAT_CODE_CANCEL_ACCEPT);
				}
			}
		}

		/*
		 * 클레임 결제 등록
		 */
		// 주 결제 수단
		if (ocClaimAmountVO.getMainPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getMainPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getMainPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getMainPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소 금액으로 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getMainPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 기프트
		if (ocClaimAmountVO.getGiftPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getGiftPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getGiftPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getGiftPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소 금액으로 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getGiftPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 이벤트 포인트
		if (ocClaimAmountVO.getEventPointPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getEventPointPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getEventPointPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getEventPointPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소
																									// 금액으로 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getEventPointPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 포인트
		if (ocClaimAmountVO.getPointPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getPointPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getPointPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getPointPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소 금액으로
																								// 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getPointPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		/*
		 * 원 주문 마스터/상품 상태 값 변경
		 */
		OcOrderProduct updateOrderProduct = new OcOrderProduct();
		updateOrderProduct.setOrderNo(ocOrder.getOrderNo());
		updateOrderProduct.setModerNo(ocClaim.getMemberNo());
		updateOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_ACCEPT); // 주문상품상태코드 : 취소접수

		ocOrderProductDao.updateOrderProduct(updateOrderProduct); // 원 주문상품 상태 값 변경

		OcOrder updateOrder = new OcOrder();
		updateOrder.setOrderNo(ocOrder.getOrderNo());
		updateOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_CANCEL_ACCEPT); // 주문상태코드 : 전체취소접수
		updateOrder.setCnclAmt(ocOrder.getPymntAmt());
		updateOrder.setClmNo(ocClaim.getClmNo());
		updateOrder.setModerNo(ocClaim.getMemberNo());

		ocOrderDao.updateOrderByClaim(updateOrder); // 원 주문 상태 값 변경

		/*
		 * 주문 마스터/상품 신규(매출취소 리오더) 등록
		 */
		OcOrder reOrder = new OcOrder();
		BeanUtils.copyProperties(ocOrder, reOrder); // 내용 복사

		String reOrderNo = orderService.createOrderSeq(); // 신규 주문번호

		ocClaim.setReOrderNo(reOrderNo); // 클레임 정보에도 신규주문번호 set

		reOrder.setOrderNo(reOrderNo); // 신규주문번호
		reOrder.setClmNo(ocClaim.getClmNo()); // 클레임번호 set
		reOrder.setSalesCnclGbnType(CommonCode.SALES_CNCL_GBN_TYPE_CANCEL); // 매출취소구분 : 취소
		reOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_CANCEL_COMPLETE); // 주문상태코드 : 전체취소완료

		// 신규 주문(매출취소 리오더) 생성
		ocOrderDao.insertOrder(reOrder);

		// 신규 주문상품(매출취소 리오더) 생성
		for (OcOrderProduct orderProduct : ocClaimAmountVO.getOrgOrderProductList()) {
			OcOrderProduct reOrderProduct = new OcOrderProduct();
			BeanUtils.copyProperties(orderProduct, reOrderProduct); // 내용 복사

			reOrderProduct.setOrderNo(reOrderNo); // 신규 생성 주문번호
			reOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_ACCEPT); // 주문상품상태코드:취소접수

			// 신규 리오더 주문상품 생성
			ocOrderProductDao.insertOrderProduct(reOrderProduct);

			// 신규 리오더 주문상품이력 등록
//			OcOrderProductHistory reOrderProductHistory = new OcOrderProductHistory();
//			reOrderProductHistory.setOrderNo(reOrderProduct.getOrderNo());
//			reOrderProductHistory.setOrderPrdtSeq(reOrderProduct.getOrderPrdtSeq());
//			reOrderProductHistory.setPrdtNo(reOrderProduct.getPrdtNo());
//			reOrderProductHistory.setPrdtOptnNo(reOrderProduct.getPrdtOptnNo());
//			reOrderProductHistory.setPrdtName(reOrderProduct.getPrdtName());
//			reOrderProductHistory.setEngPrdtName(reOrderProduct.getEngPrdtName());
//			reOrderProductHistory.setOptnName(reOrderProduct.getOptnName());
//			reOrderProductHistory.setOrderPrdtStatCode(reOrderProduct.getOrderPrdtStatCode());
//			reOrderProductHistory.setNoteText(null);
//			reOrderProductHistory.setRgsterNo(ocClaim.getMemberNo());
//
//			ocOrderProductHistoryDao.insertProductHistory(reOrderProductHistory); // 주문상품이력 생성

			// 원 주문의 주문상품이력 등록(취소)
			OcOrderProductHistory orgOrderProductHistory = new OcOrderProductHistory();
			orgOrderProductHistory.setOrderNo(orderProduct.getOrderNo());
			orgOrderProductHistory.setOrderPrdtSeq(reOrderProduct.getOrderPrdtSeq());
			orgOrderProductHistory.setPrdtNo(reOrderProduct.getPrdtNo());
			orgOrderProductHistory.setPrdtOptnNo(reOrderProduct.getPrdtOptnNo());
			orgOrderProductHistory.setPrdtName(reOrderProduct.getPrdtName());
			orgOrderProductHistory.setEngPrdtName(reOrderProduct.getEngPrdtName());
			orgOrderProductHistory.setOptnName(reOrderProduct.getOptnName());
			orgOrderProductHistory.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_ACCEPT); // 주문상품상태코드:취소접수
			orgOrderProductHistory.setNoteText(null);
			orgOrderProductHistory.setRgsterNo(ocClaim.getMemberNo());

			ocOrderProductHistoryDao.insertProductHistory(orgOrderProductHistory); // 주문상품이력 생성

			// 예약 주문이 아닌 경우 재고 변경
			if (UtilsText.equals(ocOrder.getRsvOrderYn(), Const.BOOLEAN_FALSE)) {
				// TODO-KTH : 재고변경
			}

			// 주문 취소 인터페이스 호출(사은품, 배송비 제외 - 사은품은 본 상품과 join된 데이터 사용)
			if (!UtilsText.equals(orderProduct.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
					&& !UtilsText.equals(orderProduct.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) {

				claimProcService.callCancelOrderInterface(orderProduct);
			}
		}

		/*
		 * 품절보상 쿠폰 지급 처리
		 */
		Date orderDtm = new Date(ocOrder.getOrderDtm().getTime());
		Date compareDate = DateUtils.addDays(new Date(), -3);

		// 주문일 3일 경과 기준, 클레임 사유 품절, 임직원아닌 회원 주문 상품
		if (compareDate.compareTo(orderDtm) >= 0
				&& UtilsText.equals(ocClaim.getOcClaimProductList().get(0).getClmRsnCode(),
						CommonCode.CLM_RSN_CODE_CANCEL_SOLDOUT)
				&& UtilsText.equals(ocOrder.getEmpYn(), Const.BOOLEAN_FALSE)
				&& (UtilsText.equals(ocOrder.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_ONLINE)
						|| UtilsText.equals(ocOrder.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP))) {
			// 클레임 대상 상품 수량만큼 지급
			claimProcService.setGiveSoldOutCmpns(ocClaim, ocClaim.getOcClaimProductList());
		}

		/*
		 * 사용 쿠폰 재 발급
		 */
		List<Integer> holdCpnSeqs = ocClaimAmountVO.getOrderUseCouponList().stream()
				.map(OcOrderUseCoupon::getHoldCpnSeq).distinct().collect(Collectors.toList()); // 중복 쿠폰 제거
		for (Integer holdCpnSeq : holdCpnSeqs) {
			MbMemberCoupon reIssueMemberCoupon = new MbMemberCoupon();
			reIssueMemberCoupon.setMemberNo(ocClaim.getMemberNo());
			reIssueMemberCoupon.setHoldCpnSeq(holdCpnSeq);
			reIssueMemberCoupon.setReIssueRsnText("주문취소 재 발급");
			reIssueMemberCoupon.setCpnStatCode(CommonCode.CPN_STAT_CODE_REISSUANCE);
			reIssueMemberCoupon.setRgsterNo(Const.SYSTEM_ADMIN_NO);
			reIssueMemberCoupon.setModerNo(Const.SYSTEM_ADMIN_NO);

			mbMemberCouponDao.insertMemberCouponReIssue(reIssueMemberCoupon); // 쿠폰 재 발급
		}

		/*
		 * 임직원 주문인 경우 임직원 한도 복원
		 */
		if (UtilsText.equals(ocOrder.getEmpOrderYn(), Const.BOOLEAN_TRUE)) {
			// TODO-KTH 임직원 할인 금액 한도 복원 ocOrder.getTotalEmpDscntAmt()
			// 전체취소 ocOrder.getTotalEmpDscntAmt()
			// 부분취소 상품개별 계산
		}

		/*
		 * 주문배송이력 상태 변경
		 */
		OcOrderDeliveryHistory orderDeliveryHistory = new OcOrderDeliveryHistory();
		orderDeliveryHistory.setOrderNo(ocClaim.getOrderNo());
		orderDeliveryHistory.setDlvyStatCode(CommonCode.DLVY_STAT_CODE_DELIVERY_CANCEL); // 배송 상태 코드 : 배송취소
		orderDeliveryHistory.setRgsterNo(ocClaim.getRgsterNo());
		orderDeliveryHistory.setModerNo(ocClaim.getModerNo());
		ocOrderDeliveryHistoryDao.updateOrderConfirm(orderDeliveryHistory);

		/*
		 * 결제취소
		 */
		// 결제취소시 환불 발생여부(refundOccurrence) ocClaimAmountVO 에 set
		claimProcService.setCancelPayment(ocClaimAmountVO, ocClaim, mbMember);

		/*
		 * 결제취소 처리 후 처리 주문, 클레임 상태 값 후 처리
		 */
		claimProcService.setClaimCancelAfterProc(ocClaim, ocClaimAmountVO, reOrderNo);

//		if (true) {
//			throw new Exception("임의에러");
//		}

		resultMap.put("success", Const.BOOLEAN_TRUE);
		resultMap.put("clmNo", ocClaim.getClmNo());

		return resultMap;
	}

	/**
	 * @Desc : 클레임 부분 취소 처리
	 * @Method Name : setClaimPartCancelProc
	 * @Date : 2019. 7. 1.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocClaimAmountVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setClaimPartCancelProc(OcClaim ocClaim, OcClaimAmountVO ocClaimAmountVO)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		/*
		 * [파라미터로 넘어온 클레임 대상 주문상품 순번 배] - 파라미터 클레임 상품 대상은 사은품과 배송비가 없음
		 */
		Object[] claimOrderPrdtSeqs = ocClaim.getOcClaimProductList().stream().map(OcClaimProduct::getOrderPrdtSeq)
				.toArray();
		;

		/*
		 * [원 주문정보 get]
		 */
		OcOrder ocOrder = new OcOrder();
		ocOrder.setOrderNo(ocClaim.getOrgOrderNo());
		ocOrder = ocOrderDao.selectOrderDetail(ocOrder); // 주문기본정보

		/*
		 * [주문자 정보]
		 */
		MbMember mbMemberParam = new MbMember();
		mbMemberParam.setMemberNo(ocOrder.getMemberNo());
		MbMember mbMember = memberService.selectMemberInfo(mbMemberParam);

		/*
		 * [기본 등록정보 set]
		 */
		ocClaim.setMemberNo(ocOrder.getMemberNo()); // 인서트 시 주문정보를 이용하나 ocClaim 재 사용을 위해 set
		ocClaim.setClmGbnCode(CommonCode.CLM_GBN_CODE_CANCEL); // 클레임구분코드-취소
		ocClaim.setDeviceCode(ocOrder.getDeviceCode()); // 디바이스코드

		ocClaim.setRtrvlGbnType(CommonCode.RTRVL_GBN_TYPE_ONLINE); // 회수지구분(O:온라인)
		ocClaim.setRtrvlStoreNo(null); // 회수매장번호
		ocClaim.setRtrvlReqYn(Const.BOOLEAN_FALSE); // 회수신청여부

		ocClaim.setBuyerName(ocOrder.getBuyerName());
		ocClaim.setBuyerTelNoText(ocOrder.getBuyerTelNoText());
		ocClaim.setBuyerHdphnNoText(ocOrder.getBuyerHdphnNoText());
		ocClaim.setBuyerPostCodeText(ocOrder.getBuyerPostCodeText());
		ocClaim.setBuyerPostAddrText(ocOrder.getBuyerPostAddrText());
		ocClaim.setBuyerDtlAddrText(ocOrder.getBuyerDtlAddrText());

		ocClaim.setRcvrName(ocOrder.getRcvrName());
		ocClaim.setRcvrTelNoText(ocOrder.getRcvrTelNoText());
		ocClaim.setRcvrHdphnNoText(ocOrder.getRcvrHdphnNoText());
		ocClaim.setRcvrPostCodeText(ocOrder.getRcvrPostCodeText());
		ocClaim.setRcvrPostAddrText(ocOrder.getRcvrPostAddrText());
		ocClaim.setRcvrDtlAddrText(ocOrder.getRcvrDtlAddrText());

		ocClaim.setDlvyMemoText(ocOrder.getDlvyMemoText());
		ocClaim.setDlvyTypeCode(ocOrder.getDlvyTypeCode()); // 배송유형코드

		ocClaim.setVndrNo(ocClaim.getOcClaimProductList().get(0).getVndrNo()); // 업체번호(클레임상품 중 한개 발췌-업체단위로 클레임 걸림)

		ocClaim.setTotalRfndAmt(ocOrder.getPymntAmt()); // 총환불금액
		ocClaim.setTotalRedempAmt(0); // 총환수금액

		ocClaim.setUnProcYn(Const.BOOLEAN_FALSE); // 미처리 여부 set
		ocClaim.setAdminAcceptYn(Const.BOOLEAN_FALSE); // 관리자접수여부
		ocClaim.setOflnAcceptYn(Const.BOOLEAN_FALSE); // 오프라인접수여부

		// 추가배송비 관련 set
		ocClaim.setAddDlvyAmtPymntType(null); // 추가배송비결제방법
		ocClaim.setAddDlvyAmt(0); // 추가배송비
		ocClaim.setHoldCpnSeq(null); // 보유쿠폰순번

		ocClaim.setClmStatCode(CommonCode.CLM_STAT_CODE_CANCEL_ACCEPT); // 클레임상태코드 - 취소접수

		/*
		 * [클레임 등록(마스터)] 등록 시 생성된 clmNo 를 ocClaim 에서 가지고 있음
		 */
		ocClaimDao.insertClaimInfo(ocClaim);

		/*
		 * [클레임상품 등록] [클레임상태이력 등록]
		 */
		for (OcClaimProduct ocClaimProduct : ocClaim.getOcClaimProductList()) {
			// 클레임상품 등록, 클레임상태이력 등록
			this.insertClaimPrdtAndClaimStatusHistory(ocClaim, ocClaimProduct,
					CommonCode.CLM_PRDT_STAT_CODE_CANCEL_ACCEPT);

			// 해당 상품에 사은품이 있는 경우 사은품 한번 더 등록
			if (!ObjectUtils.isEmpty(ocClaimProduct.getOrderGiftPrdtSeq())) {
				ocClaimProduct.setOrderPrdtSeq(ocClaimProduct.getOrderGiftPrdtSeq()); // 사은품 주문상품순번으로 대체
				this.insertClaimPrdtAndClaimStatusHistory(ocClaim, ocClaimProduct,
						CommonCode.CLM_PRDT_STAT_CODE_CANCEL_ACCEPT);
			}
		}

		/*************************************
		 * 클레임 금액 계산(클레임 마스터/상품 등록 후 호출)
		 *************************************/
		// ocClaimAmountVO 에 계산된 금액 set
		claimProcService.setClaimAmountCalcForPartCancel(ocClaim, ocOrder, ocClaimAmountVO);

		/*
		 * 환불 배송비 발생된 경우 클레임 상품에 배송비 등록
		 */
		if (!ObjectUtils.isEmpty(ocClaimAmountVO.getCancelDlvyProductList())) {
			for (OcOrderProduct orderProduct : ocClaimAmountVO.getCancelDlvyProductList()) {
				OcClaimProduct dlvyClaimProduct = new OcClaimProduct();

				dlvyClaimProduct.setOrderPrdtSeq(orderProduct.getOrderPrdtSeq());
				dlvyClaimProduct.setClmRsnCode(ocClaim.getOcClaimProductList().get(0).getClmRsnCode());
				dlvyClaimProduct.setClmEtcRsnText(ocClaim.getOcClaimProductList().get(0).getClmEtcRsnText());

				// 클레임상품 등록, 클레임상태이력 등록
				this.insertClaimPrdtAndClaimStatusHistory(ocClaim, dlvyClaimProduct,
						CommonCode.CLM_PRDT_STAT_CODE_CANCEL_ACCEPT);
			}
		}

		/*
		 * 클레임 계산 결과 환수금이 더 많은 경우 취소 불가 처리
		 */
		if (ocClaimAmountVO.getRefundCnclAmt() < 0) {
			resultMap.put("success", Const.BOOLEAN_FALSE);
			resultMap.put("message", "환수금이 발생하여 취소가 불가합니다.");
			resultMap.put("clmNo", ocClaim.getClmNo());

			return resultMap;
		}

		/*************************************
		 * 환불 배송비까지 등록된 이후 목록 재 조회
		 *************************************/
		/*
		 * [원 주문에 걸린 모든 클레임 - 현재 클레임으로 인해 등록된 사은품/배송비 모두 포함]
		 */
		OcClaimProduct ocClaimProduct = new OcClaimProduct();
		ocClaimProduct.setOrgOrderNo(ocClaim.getOrgOrderNo()); // 원주문번호 기준
		ocClaimProduct.setPrdtTypeCodeGift(CommonCode.PRDT_TYPE_CODE_GIFT); // 사은품 상품 코드
		List<OcClaimProduct> orderAllClaimProductList = ocClaimProductDao.selectOrgClaimProductList(ocClaimProduct);

		ocClaimAmountVO.setOrderAllClaimProductList(orderAllClaimProductList); // 다시 set

		/*
		 * [현재 클레임 상품 목록 - 현재 클레임으로 인해 등록된 사은품/배송비 모두 포함]
		 */
		List<OcClaimProduct> thisTimeClaimProductList = orderAllClaimProductList.stream()
				.filter(x -> UtilsText.equals(x.getClmNo(), ocClaim.getClmNo())).collect(Collectors.toList());

		ocClaimAmountVO.setThisTimeClaimProductList(thisTimeClaimProductList); // 다시 set

		/*
		 * 클레임 결제 등록
		 */
		// 주 결제 수단
		if (ocClaimAmountVO.getMainPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getMainPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getMainCnclAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getMainPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소 금액으로 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getMainPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 기프트
		if (ocClaimAmountVO.getGiftPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getGiftPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getGiftPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getGiftPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소 금액으로 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getGiftPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 이벤트 포인트
		if (ocClaimAmountVO.getEventPointPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getEventPointPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getEventPointPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getEventPointPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소
																									// 금액으로 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getEventPointPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		// 포인트
		if (ocClaimAmountVO.getPointPayment() != null) {
			OcClaimPayment claimPayment = new OcClaimPayment();
			BeanUtils.copyProperties(ocClaimAmountVO.getPointPayment(), claimPayment); // 내용 복사

			if (ocClaimAmountVO.getPointPayment().getPymntAmt() > 0) { // 실제 취소금이 있는 경우
				claimPayment.setPymntTodoAmt(ocClaimAmountVO.getPointPayment().getPymntAmt()); // 결제예정금액 - 클레임 취소 금액으로
																								// 변경
				claimPayment.setPymntAmt(ocClaimAmountVO.getPointPayment().getPymntAmt()); // 결제금액 - 클레임 취소 금액으로 변경

				// 클레임 결제 이력데이터 등록
				claimProcService.setClaimPaymentHistory(ocClaim, claimPayment, CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			}
		}

		/*
		 * 원 주문 마스터/상품 상태 값 변경
		 */
		for (OcClaimProduct claimProduct : thisTimeClaimProductList) {
			OcOrderProduct updateOrderProduct = new OcOrderProduct();
			updateOrderProduct.setOrderNo(claimProduct.getOrderNo());
			updateOrderProduct.setOrderPrdtSeq(claimProduct.getOrderPrdtSeq());
			updateOrderProduct.setModerNo(ocClaim.getMemberNo());
			updateOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_ACCEPT); // 주문상품상태코드 : 취소접수

			ocOrderProductDao.updateOrderProduct(updateOrderProduct); // 원 주문상품 상태 값 변경
		}

		// 남은 상품 모두 취소인 경우 원 주문 마스터 상태 업데이트
		if (ocClaimAmountVO.isRemainAllCancelYn()) {
			OcOrder updateOrder = new OcOrder();
			updateOrder.setOrderNo(ocOrder.getOrderNo());
			updateOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_CANCEL_ACCEPT); // 주문상태코드 : 전체취소접수
			updateOrder.setCnclAmt(ocOrder.getPymntAmt());
			updateOrder.setClmNo(ocClaim.getClmNo());
			updateOrder.setModerNo(ocClaim.getMemberNo());

			ocOrderDao.updateOrderByClaim(updateOrder); // 원 주문 상태 값 변경
		}

		/*
		 * 주문 마스터/상품 신규(매출취소 리오더) 등록
		 */
		OcOrder reOrder = new OcOrder();
		BeanUtils.copyProperties(ocOrder, reOrder); // 내용 복사

		String reOrderNo = orderService.createOrderSeq(); // 신규 주문번호

		ocClaim.setReOrderNo(reOrderNo); // 클레임 정보에도 신규주문번호 set

		reOrder.setOrderNo(reOrderNo); // 신규주문번호
		reOrder.setClmNo(ocClaim.getClmNo()); // 클레임번호 set
		reOrder.setSalesCnclGbnType(CommonCode.SALES_CNCL_GBN_TYPE_CANCEL); // 매출취소구분 : 취소
		reOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_CANCEL_COMPLETE); // 주문상태코드 : 전체취소완료

		// 신규 주문(매출취소 리오더) 생성
		ocOrderDao.insertOrder(reOrder);

		// 신규 주문상품(매출취소 리오더) 생성
		for (OcClaimProduct claimProduct : thisTimeClaimProductList) {
			OcOrderProduct reOrderProduct = new OcOrderProduct();

			OcOrderProduct orderProduct = (OcOrderProduct) ocClaimAmountVO.getOrgOrderProductList().stream()
					.filter(x -> UtilsText.equals(claimProduct.getOrderNo(), x.getOrderNo()) && UtilsText.equals(
							String.valueOf(claimProduct.getOrderPrdtSeq()), String.valueOf(x.getOrderPrdtSeq())))
					.findFirst().orElse(null);

			BeanUtils.copyProperties(orderProduct, reOrderProduct); // 내용 복사

			reOrderProduct.setOrderNo(reOrderNo); // 신규 생성 주문번호
			reOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_ACCEPT); // 주문상품상태코드:취소접수

			// 신규 리오더 주문상품 생성
			ocOrderProductDao.insertOrderProduct(reOrderProduct);

			// 원 주문의 주문상품이력 등록(취소)
			OcOrderProductHistory orgOrderProductHistory = new OcOrderProductHistory();
			orgOrderProductHistory.setOrderNo(orderProduct.getOrderNo());
			orgOrderProductHistory.setOrderPrdtSeq(reOrderProduct.getOrderPrdtSeq());
			orgOrderProductHistory.setPrdtNo(reOrderProduct.getPrdtNo());
			orgOrderProductHistory.setPrdtOptnNo(reOrderProduct.getPrdtOptnNo());
			orgOrderProductHistory.setPrdtName(reOrderProduct.getPrdtName());
			orgOrderProductHistory.setEngPrdtName(reOrderProduct.getEngPrdtName());
			orgOrderProductHistory.setOptnName(reOrderProduct.getOptnName());
			orgOrderProductHistory.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_ACCEPT); // 주문상품상태코드:취소접수
			orgOrderProductHistory.setNoteText(null);
			orgOrderProductHistory.setRgsterNo(ocClaim.getMemberNo());

			ocOrderProductHistoryDao.insertProductHistory(orgOrderProductHistory); // 주문상품이력 생성

			// 예약 주문이 아닌 경우 재고 변경
			if (UtilsText.equals(ocOrder.getRsvOrderYn(), Const.BOOLEAN_FALSE)) {
				// TODO-KTH : 재고변경
			}

			// 주문 취소 인터페이스 호출(사은품, 배송비 제외 - 사은품은 본 상품과 join된 데이터 사용)
			if (!UtilsText.equals(orderProduct.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_GIFT)
					&& !UtilsText.equals(orderProduct.getPrdtTypeCode(), CommonCode.PRDT_TYPE_CODE_DELIVERY)) {

				claimProcService.callCancelOrderInterface(orderProduct);
			}
		}

		/*
		 * 다족구매로 인해 재조정 대상 상품이 있는 경우 신규(매출취소 리오더) 등록
		 */
		if (!ObjectUtils.isEmpty(ocClaimAmountVO.getBeforeAdjustOrderProductByMultiBuyList())) {
			for (OcOrderProduct cancelMultiBuyOrderProduct : ocClaimAmountVO
					.getBeforeAdjustOrderProductByMultiBuyList()) {
				cancelMultiBuyOrderProduct.setOrderNo(reOrderNo); // 신규 생성 주문번호
				cancelMultiBuyOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_COMPLETE); // 주문상품상태코드:취소완료

				// 신규 리오더 주문상품 생성
				ocOrderProductDao.insertOrderProduct(cancelMultiBuyOrderProduct);

				// 원 주문의 주문상품이력 등록(취소)
				OcOrderProductHistory orgOrderProductHistory = new OcOrderProductHistory();
				orgOrderProductHistory.setOrderNo(cancelMultiBuyOrderProduct.getOrgOrderNo());
				orgOrderProductHistory.setOrderPrdtSeq(cancelMultiBuyOrderProduct.getOrderPrdtSeq());
				orgOrderProductHistory.setPrdtNo(cancelMultiBuyOrderProduct.getPrdtNo());
				orgOrderProductHistory.setPrdtOptnNo(cancelMultiBuyOrderProduct.getPrdtOptnNo());
				orgOrderProductHistory.setPrdtName(cancelMultiBuyOrderProduct.getPrdtName());
				orgOrderProductHistory.setEngPrdtName(cancelMultiBuyOrderProduct.getEngPrdtName());
				orgOrderProductHistory.setOptnName(cancelMultiBuyOrderProduct.getOptnName());
				orgOrderProductHistory.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_CANCEL_COMPLETE); // 주문상품상태코드:취소완료
				orgOrderProductHistory.setNoteText("다족구매 매출 재조정");
				orgOrderProductHistory.setRgsterNo(ocClaim.getMemberNo());

				ocOrderProductHistoryDao.insertProductHistory(orgOrderProductHistory); // 주문상품이력 생성
			}
		}

		/*
		 * 다족구매로 인해 재조정 된 대상 상품이 있는 경우 새로운 매출용 주문 리오더 등록
		 */
		if (!ObjectUtils.isEmpty(ocClaimAmountVO.getAdjustedOrderProductByMultiBuyList())) {
			/*
			 * 주문 마스터 등록(다족구매 주문금액 변경으로 인한 매출)
			 */
			OcOrder reSalesOrder = new OcOrder();
			BeanUtils.copyProperties(ocOrder, reSalesOrder); // 내용 복사

			String reSalesOrderNo = orderService.createOrderSeq(); // 신규 주문번호

			reSalesOrder.setOrderNo(reSalesOrderNo); // 신규주문번호
			reSalesOrder.setClmNo(ocClaim.getClmNo()); // 클레임번호 set
			reSalesOrder.setSalesCnclGbnType(CommonCode.SALES_CNCL_GBN_TYPE_CANCEL); // 매출취소구분 : 취소
			reSalesOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_CANCEL_COMPLETE); // 주문상태코드 : 전체취소완료

			// 신규 주문(매출 리오더) 생성
			ocOrderDao.insertOrder(reOrder);

			/*
			 * 다족구매로 인해 재조정 되는 대상 상품이 있는 경우 신규(매출 리오더) 등록
			 */
			for (OcOrderProduct salesMultiBuyOrderProduct : ocClaimAmountVO.getAdjustedOrderProductByMultiBuyList()) {
				salesMultiBuyOrderProduct.setOrderNo(reSalesOrderNo); // 신규 생성 주문번호
				salesMultiBuyOrderProduct.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_COMPLETE); // 주문상품상태코드:결제완료

				// 신규 리오더 주문상품 생성
				ocOrderProductDao.insertOrderProduct(salesMultiBuyOrderProduct);

				// 원 주문의 주문상품이력 등록(매출기준)
				OcOrderProductHistory orgOrderProductHistory = new OcOrderProductHistory();
				orgOrderProductHistory.setOrderNo(salesMultiBuyOrderProduct.getOrgOrderNo());
				orgOrderProductHistory.setOrderPrdtSeq(salesMultiBuyOrderProduct.getOrderPrdtSeq());
				orgOrderProductHistory.setPrdtNo(salesMultiBuyOrderProduct.getPrdtNo());
				orgOrderProductHistory.setPrdtOptnNo(salesMultiBuyOrderProduct.getPrdtOptnNo());
				orgOrderProductHistory.setPrdtName(salesMultiBuyOrderProduct.getPrdtName());
				orgOrderProductHistory.setEngPrdtName(salesMultiBuyOrderProduct.getEngPrdtName());
				orgOrderProductHistory.setOptnName(salesMultiBuyOrderProduct.getOptnName());
				orgOrderProductHistory.setOrderPrdtStatCode(CommonCode.ORDER_PRDT_STAT_CODE_COMPLETE); // 주문상품상태코드:결제완료
				orgOrderProductHistory.setNoteText("다족구매 매출 재조정");
				orgOrderProductHistory.setRgsterNo(ocClaim.getMemberNo());

				ocOrderProductHistoryDao.insertProductHistory(orgOrderProductHistory); // 주문상품이력 생성
			}
		}

		/*
		 * 다족구매로 인해 재조정 되는 프로모션 정보 등록
		 */
		if (!ObjectUtils.isEmpty(ocClaimAmountVO.getAdjustedClaimProductApplyPromotionList())) {
			for (OcClaimProductApplyPromotion claimProductApplyPromotion : ocClaimAmountVO
					.getAdjustedClaimProductApplyPromotionList()) {

				// 클레임상품적용프로모션 등록
				ocClaimProductApplyPromotionDao.insert(claimProductApplyPromotion);
			}
		}

		/*
		 * 구매적립 포인트 계산
		 */
		claimProcService.setReCalcSvaePoint(ocClaim, ocClaimAmountVO);

		int thisClaimVariationSavePoint = ocClaimAmountVO.getThisClaimVariationSavePoint(); // 현재 클레임 증감 적립포인트

		// 더블적립 쿠폰이 사용된 경우
		if (ocClaimAmountVO.getOrderDoubleDscntCpnInfo() != null) {
			thisClaimVariationSavePoint = thisClaimVariationSavePoint * 2;
		}

		/*
		 * 구매적립 포인트 환수 체크(임직원이 아닌 멤버쉽 회원인 경우만 체크)
		 */
		if (UtilsText.equals(ocOrder.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP)
				&& UtilsText.equals(ocOrder.getEmpYn(), Const.BOOLEAN_FALSE)) {

			// 환수할 포인트가 발생한 경우
			if (thisClaimVariationSavePoint < 0) {
				String safeKey = mbMember.getSafeKey();

				// 환수포인트 조회 인터페이스
				int clawbackPoint = membershipPointService.getClawbackPoint(safeKey, ocOrder.getOrderNo(),
						thisClaimVariationSavePoint);

				// 환수포인트 발생
				if (clawbackPoint > 0) {
					resultMap.put("success", Const.BOOLEAN_FALSE);
					resultMap.put("message", "구매적립 환수포인트가 발생하여 취소가 불가합니다.");
					resultMap.put("clmNo", ocClaim.getClmNo());

					return resultMap;
				}
			}
		}

		/*
		 * 품절보상 쿠폰 지급 처리
		 */
		Date orderDtm = new Date(ocOrder.getOrderDtm().getTime());
		Date compareDate = DateUtils.addDays(new Date(), -3);

		// 주문일 3일 경과 기준, 클레임 사유 품절, 임직원아닌 회원 주문 상품
		if (compareDate.compareTo(orderDtm) >= 0
				&& UtilsText.equals(ocClaim.getOcClaimProductList().get(0).getClmRsnCode(),
						CommonCode.CLM_RSN_CODE_CANCEL_SOLDOUT)
				&& UtilsText.equals(ocOrder.getEmpYn(), Const.BOOLEAN_FALSE)
				&& (UtilsText.equals(ocOrder.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_ONLINE)
						|| UtilsText.equals(ocOrder.getMemberTypeCode(), CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP))) {
			// 클레임 대상 상품 수량만큼 지급
			claimProcService.setGiveSoldOutCmpns(ocClaim, ocClaim.getOcClaimProductList());
		}

		/*
		 * 사용 쿠폰 재 발급, 주문 사용쿠폰 클레임번호 업데이트
		 */
		// 원주문 사용 쿠폰 목록에서 현재 클레임 대상 주문상품에 해당하는 쿠폰사용 정보 목록(이전 클레임으로 인한 항목 제외)
		List<OcOrderUseCoupon> claimProductUserCouponList = ocClaimAmountVO.getOrderUseCouponList().stream()
				.filter(x -> Arrays.asList(claimOrderPrdtSeqs).contains(x.getOrderPrdtSeq()))
				.filter(x -> UtilsText.equals(x.getClmNo(), null)).collect(Collectors.toList());

		// 원주문 사용 쿠폰 목록에서 현재 클레임 대상 주문상품에을 제회한 남은 상품 쿠폰사용 정보 목록(이전 클레임으로 인한 항목 제외)
		List<OcOrderUseCoupon> excludeClaimProductUserCouponList = ocClaimAmountVO.getOrderUseCouponList().stream()
				.filter(x -> !Arrays.asList(claimOrderPrdtSeqs).contains(x.getOrderPrdtSeq()))
				.filter(x -> UtilsText.equals(x.getClmNo(), null)).collect(Collectors.toList());

		// 현재 클레임 대상 중복 쿠폰 제거
		List<Integer> claimHoldCpnSeqs = claimProductUserCouponList.stream().map(OcOrderUseCoupon::getHoldCpnSeq)
				.distinct().collect(Collectors.toList());

		// 재 발급한 쿠폰 목록(체크용)
		List<Integer> reIssueHoldCpnSeq = new ArrayList<Integer>();

		// 이전 클레임 부분 취소 후 남은 상품 모두 취소인 경우
		if (ocClaimAmountVO.isRemainAllCancelYn()) {
			// 더블적립 쿠폰 처리(주문에 걸린 쿠폰이므로 상품기준과 다르게 별도 처리)
			if (ocClaimAmountVO.getOrderDoubleDscntCpnInfo() != null) {
				OcOrderUseCoupon orderUseCoupon = new OcOrderUseCoupon();
				orderUseCoupon.setOrderNo(ocClaim.getOrderNo());
				orderUseCoupon.setHoldCpnSeq(ocClaimAmountVO.getOrderDoubleDscntCpnInfo().getHoldCpnSeq());

				ocOrderUseCouponDao.updateOrderUseCouponClmNo(orderUseCoupon); // 주문 사용쿠폰 클레임번호 업데이트

				MbMemberCoupon reIssueMemberCoupon = new MbMemberCoupon();
				reIssueMemberCoupon.setMemberNo(ocClaim.getMemberNo());
				reIssueMemberCoupon.setHoldCpnSeq(ocClaimAmountVO.getOrderDoubleDscntCpnInfo().getHoldCpnSeq());
				reIssueMemberCoupon.setReIssueRsnText("주문취소 재 발급");
				reIssueMemberCoupon.setCpnStatCode(CommonCode.CPN_STAT_CODE_REISSUANCE);
				reIssueMemberCoupon.setRgsterNo(Const.SYSTEM_ADMIN_NO);
				reIssueMemberCoupon.setModerNo(Const.SYSTEM_ADMIN_NO);

				mbMemberCouponDao.insertMemberCouponReIssue(reIssueMemberCoupon); // 쿠폰 재 발급
			}

			// 상품단위에 걸린 holdCpnSeq 기준으로 재 발급
			for (Integer holdCpnSeq : claimHoldCpnSeqs) {
				OcOrderUseCoupon orderUseCoupon = new OcOrderUseCoupon();
				orderUseCoupon.setOrderNo(ocClaim.getOrderNo());
				orderUseCoupon.setHoldCpnSeq(holdCpnSeq);

				ocOrderUseCouponDao.updateOrderUseCouponClmNo(orderUseCoupon); // 주문 사용쿠폰 클레임번호 업데이트

				MbMemberCoupon reIssueMemberCoupon = new MbMemberCoupon();
				reIssueMemberCoupon.setMemberNo(ocClaim.getMemberNo());
				reIssueMemberCoupon.setHoldCpnSeq(holdCpnSeq);
				reIssueMemberCoupon.setReIssueRsnText("주문취소 재 발급");
				reIssueMemberCoupon.setCpnStatCode(CommonCode.CPN_STAT_CODE_REISSUANCE);
				reIssueMemberCoupon.setRgsterNo(Const.SYSTEM_ADMIN_NO);
				reIssueMemberCoupon.setModerNo(Const.SYSTEM_ADMIN_NO);

				mbMemberCouponDao.insertMemberCouponReIssue(reIssueMemberCoupon); // 쿠폰 재 발급
			}
		} else {
			for (OcOrderUseCoupon claimProductUserCoupon : claimProductUserCouponList) {
				// 취소대상 상품에 적용된 쿠폰이 할인유형 쿠폰인 경우
				if (UtilsText.equals(claimProductUserCoupon.getCpnTypeCode(), CommonCode.CPN_TYPE_CODE_DISCOUNT)
						|| UtilsText.equals(claimProductUserCoupon.getCpnTypeCode(),
								CommonCode.CPN_TYPE_CODE_DISCOUNT_AFFILIATES)
						|| UtilsText.equals(claimProductUserCoupon.getCpnTypeCode(),
								CommonCode.CPN_TYPE_CODE_DOUBLE_POINT)) {

					// 취소대상이 아닌 상품에 적용된 동일 할인유형 쿠폰번호 존재 확인
					long existCpnCnt = excludeClaimProductUserCouponList.stream()
							.filter(x -> UtilsText.equals(String.valueOf(claimProductUserCoupon.getHoldCpnSeq()),
									String.valueOf(x.getHoldCpnSeq())))
							.count();

					OcOrderUseCoupon orderUseCoupon = new OcOrderUseCoupon();
					orderUseCoupon.setOrderNo(ocClaim.getOrderNo());
					orderUseCoupon.setOrderUseCpnSeq(claimProductUserCoupon.getOrderUseCpnSeq());

					ocOrderUseCouponDao.updateOrderUseCouponClmNo(orderUseCoupon); // 주문 사용쿠폰 클레임번호 업데이트

					// 남아있는 상품에 동일 할인쿠폰이 없는 경우에만 쿠폰 재 발급
					if (existCpnCnt == 0) {
						// 재 발급한 쿠폰 목록에 없다면 재 발급 진행
						if (!reIssueHoldCpnSeq.contains(claimProductUserCoupon.getHoldCpnSeq())) {
							MbMemberCoupon reIssueMemberCoupon = new MbMemberCoupon();
							reIssueMemberCoupon.setMemberNo(ocClaim.getMemberNo());
							reIssueMemberCoupon.setHoldCpnSeq(claimProductUserCoupon.getHoldCpnSeq());
							reIssueMemberCoupon.setReIssueRsnText("주문취소 재 발급");
							reIssueMemberCoupon.setCpnStatCode(CommonCode.CPN_STAT_CODE_REISSUANCE);
							reIssueMemberCoupon.setRgsterNo(Const.SYSTEM_ADMIN_NO);
							reIssueMemberCoupon.setModerNo(Const.SYSTEM_ADMIN_NO);

							mbMemberCouponDao.insertMemberCouponReIssue(reIssueMemberCoupon); // 쿠폰 재 발급
							reIssueHoldCpnSeq.add(claimProductUserCoupon.getHoldCpnSeq()); // 재 발급 쿠폰 목록에 추가
						}
					}
				}
			}
		}

		// 환불 대상 배송비가 발생한 경우 해당 배송비 상품에 적용된 쿠폰 재 발급(업체번호 확인을 위해 별도로 처리)
		if (!ObjectUtils.isEmpty(ocClaimAmountVO.getCancelDlvyProductList())) {
			for (OcOrderProduct orderProduct : ocClaimAmountVO.getCancelDlvyProductList()) {
				// 배송비무료타입, 업체번호를 확인하여 업체별로 적용된 쿠폰을 추출
				OcOrderUseCoupon dlvyFreeCpn = claimProductUserCouponList.stream()
						.filter(x -> UtilsText.equals(x.getCpnTypeCode(), CommonCode.CPN_TYPE_CODE_FREE_DELIVERY))
						.filter(x -> UtilsText.equals(x.getVndrNo(), orderProduct.getVndrNo())).findFirst()
						.orElse(null);

				if (dlvyFreeCpn != null) {
					OcOrderUseCoupon orderUseCoupon = new OcOrderUseCoupon();
					orderUseCoupon.setOrderNo(ocClaim.getOrderNo());
					orderUseCoupon.setHoldCpnSeq(dlvyFreeCpn.getHoldCpnSeq());

					ocOrderUseCouponDao.updateOrderUseCouponClmNo(orderUseCoupon); // 주문 사용쿠폰 클레임번호 업데이트

					MbMemberCoupon reIssueMemberCoupon = new MbMemberCoupon();
					reIssueMemberCoupon.setMemberNo(ocClaim.getMemberNo());
					reIssueMemberCoupon.setHoldCpnSeq(dlvyFreeCpn.getHoldCpnSeq());
					reIssueMemberCoupon.setReIssueRsnText("주문취소 재 발급");
					reIssueMemberCoupon.setCpnStatCode(CommonCode.CPN_STAT_CODE_REISSUANCE);
					reIssueMemberCoupon.setRgsterNo(Const.SYSTEM_ADMIN_NO);
					reIssueMemberCoupon.setModerNo(Const.SYSTEM_ADMIN_NO);

					mbMemberCouponDao.insertMemberCouponReIssue(reIssueMemberCoupon); // 쿠폰 재 발급
				}
			}
		}

		/*
		 * 임직원 주문인 경우 임직원 한도 복원
		 */
		if (UtilsText.equals(ocOrder.getEmpOrderYn(), Const.BOOLEAN_TRUE)) {
			// TODO-KTH 임직원 할인 금액 한도 복원 ocOrder.getTotalEmpDscntAmt()
			// 전체취소 ocOrder.getTotalEmpDscntAmt()
			// 부분취소 상품개별 계산
		}

		/*
		 * 주문배송이력 상태 변경
		 */
		for (Object claimOrderPrdtSeq : claimOrderPrdtSeqs) {
			OcOrderDeliveryHistory orderDeliveryHistory = new OcOrderDeliveryHistory();
			orderDeliveryHistory.setOrderNo(ocClaim.getOrderNo());
			orderDeliveryHistory.setOrderPrdtSeq((Short) claimOrderPrdtSeq);
			orderDeliveryHistory.setDlvyStatCode(CommonCode.DLVY_STAT_CODE_DELIVERY_CANCEL); // 배송 상태 코드 : 배송취소
			orderDeliveryHistory.setRgsterNo(ocClaim.getRgsterNo());
			orderDeliveryHistory.setModerNo(ocClaim.getModerNo());
			ocOrderDeliveryHistoryDao.updateOrderConfirm(orderDeliveryHistory);
		}

//		if (true) {
//			throw new Exception("임의에러");
//		}

		/*
		 * 결제취소
		 */
		// 결제취소 진행 시 환불 발생여부(refundOccurrence) ocClaimAmountVO 에 set
		claimProcService.setCancelPayment(ocClaimAmountVO, ocClaim, mbMember);

		/*
		 * 결제취소 처리 후 처리 주문, 클레임 상태 값 후 처리
		 */
		claimProcService.setClaimCancelAfterProc(ocClaim, ocClaimAmountVO, reOrderNo);

//		if (true) {
//			throw new Exception("임의에러");
//		}

		resultMap.put("success", Const.BOOLEAN_TRUE);
		resultMap.put("message", "주문이 정상적으로 취소되었습니다.");
		resultMap.put("clmNo", ocClaim.getClmNo());

		return resultMap;
	}

	/**
	 * @Desc : 클레임 교환/반품 접수 처리
	 * @Method Name : setClaimExchangeReturnProc
	 * @Date : 2019. 5. 10.
	 * @Author : KTH
	 * @param ocClaim
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setClaimExchangeReturnProc(OcClaim ocClaim) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		OcClaimProduct[] ocClaimProducts = ocClaim.getOcClaimProductList()
				.toArray(new OcClaimProduct[ocClaim.getOcClaimProductList().size()]);

		KcpPaymentApproval kcpPaymentApproval = ocClaim.getKcpPaymentApproval();
		OcClaimPayment ocClaimPayment = ocClaim.getOcClaimPayment();

		String clmStatCode = "";
		String clmPrdtStatCode = "";

		if (UtilsText.equals(ocClaim.getClmGbnCode(), CommonCode.CLM_GBN_CODE_EXCHANGE)) {
			clmStatCode = CommonCode.CLM_STAT_CODE_EXCHANGE_ACCEPT; // 클레임상태코드 - 교환접수
			clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_EXCHANGE_ACCEPT; // 클레임상품상태코드 - 교환접수
		} else if (UtilsText.equals(ocClaim.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN)) {
			clmStatCode = CommonCode.CLM_STAT_CODE_RETURN_ACCEPT; // 클레임상태코드 - 반품접수
			clmPrdtStatCode = CommonCode.CLM_PRDT_STAT_CODE_RETURN_ACCEPT; // 클레임상품상태코드 - 반품접수
		}

		/*
		 * [주문정보 get]
		 */
		OcOrder ocOrder = new OcOrder();
		ocOrder.setOrderNo(ocClaim.getOrderNo());
		ocOrder = ocOrderDao.selectOrderDetail(ocOrder); // 주문기본정보

		/*
		 * [기본 등록정보 set]
		 */
		ocClaim.setClmGbnCode(ocClaim.getClmGbnCode()); // 클레임구분코드

		ocClaim.setRtrvlGbnType(CommonCode.RTRVL_GBN_TYPE_ONLINE); // 회수지구분(O:온라인)
		ocClaim.setRtrvlStoreNo(null); // 회수매장번호
		ocClaim.setRtrvlReqYn(Const.BOOLEAN_FALSE); // 회수신청여부

		ocClaim.setDlvyTypeCode(CommonCode.DLVY_TYPE_CODE_NORMAL_LOGISTICS); // 배송유형코드-일반택배
		ocClaim.setVndrNo(ocClaimProducts[0].getVndrNo()); // 업체번호(클레임상품 중 한개 발췌-업체단위로 클레임 걸림)

		ocClaim.setTotalRfndAmt(0); // 총환불금액
		ocClaim.setTotalRedempAmt(0); // 총환수금액

		ocClaim.setUnProcYn(Const.BOOLEAN_FALSE); // 미처리 여부 set
		ocClaim.setAdminAcceptYn(Const.BOOLEAN_FALSE); // 관리자접수여부
		ocClaim.setOflnAcceptYn(Const.BOOLEAN_FALSE); // 오프라인접수여부

		// 추가배송비 관련 set
		if (ocClaim.getAddDlvyAmt() > 0) {
			// form에서 클레임 배송비 결제방법을 무료쿠폰 선택이 아니면 null
			if (!UtilsText.equals(ocClaim.getAddDlvyAmtPymntType(), CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
				ocClaim.setHoldCpnSeq(null); // 보유쿠폰순번
			}
		} else {
			ocClaim.setAddDlvyAmtPymntType(null); // 추가배송비결제방법
			ocClaim.setAddDlvyAmt(0); // 추가배송비
			ocClaim.setHoldCpnSeq(null); // 보유쿠폰순번
		}

		ocClaim.setClmStatCode(clmStatCode); // 클레임상태코드

		/*
		 * [클레임 등록(마스터)] 등록 시 생성된 clmNo 를 ocClaim 에서 가지고 있음
		 */
		ocClaimDao.insertClaimInfo(ocClaim);

		/*
		 * [클레임 마스터 정보] 등록 후 생성된 key를 이용하여 기본정보를 조회
		 */
		ocClaim = ocClaimDao.selectByPrimaryKey(ocClaim);

		/*
		 * [클레임상품 등록] [클레임상태이력 등록]
		 */
		for (OcClaimProduct ocClaimProduct : ocClaimProducts) {
			// 교환인 경우 교환대상 상품 처리
			OcClaimProduct exchangeClaimProduct = new OcClaimProduct(); // 재배송 상품
			BeanUtils.copyProperties(ocClaimProduct, exchangeClaimProduct);

			if (UtilsText.equals(ocClaim.getClmGbnCode(), CommonCode.CLM_GBN_CODE_EXCHANGE)) {
				if (UtilsText.equals(ocClaimProduct.getClmRsnCode(), CommonCode.CLM_RSN_CODE_EXCHANGE_OPTION_CHANGE)) { // 옵션변경
					ocClaimProduct.setChangePrdtOptnNo(null); // 등록 시 영향받지 않게 회수상품은 null 처리
					ocClaimProduct.setChangeOptnName(null); // 등록 시 영향받지 않게 회수상품은 null 처리
				}
			}

			// 클레임상품 등록, 클레임상태이력 등록
			this.insertClaimPrdtAndClaimStatusHistory(ocClaim, ocClaimProduct, clmPrdtStatCode);

			// 교환인 경우 교환대상 상품 등록
			if (UtilsText.equals(ocClaim.getClmGbnCode(), CommonCode.CLM_GBN_CODE_EXCHANGE)) {
				// TODO-KTH : 재고체크

				exchangeClaimProduct.setUpClmPrdtSeq(ocClaimProduct.getClmPrdtSeq()); // 회수 상품 기준의 클레임상품순번

				this.insertClaimPrdtAndClaimStatusHistory(ocClaim, exchangeClaimProduct, clmPrdtStatCode);
			}

			// 반품인 경우 사은품 상품 등록
			if (UtilsText.equals(ocClaim.getClmGbnCode(), CommonCode.CLM_GBN_CODE_RETURN)) {
				// 해당 상품에 사은품이 있는 경우 사은품 한번 더 등록
				if (!ObjectUtils.isEmpty(ocClaimProduct.getOrderGiftPrdtSeq())) {
					OcClaimProduct giftClaimProduct = new OcClaimProduct();

					BeanUtils.copyProperties(ocClaimProduct, giftClaimProduct);

					giftClaimProduct.setOrderPrdtSeq(ocClaimProduct.getOrderGiftPrdtSeq()); // 사은품 주문상품순번으로 대체

					this.insertClaimPrdtAndClaimStatusHistory(ocClaim, giftClaimProduct, clmPrdtStatCode);
				}
			}
		}

		/*
		 * 클레임배송비 처리
		 */
		if (ocClaim.getAddDlvyAmt() > 0) {
			if (UtilsText.equals(ocClaim.getAddDlvyAmtPymntType(), CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_ADD_PAYMENT)) {
				/*
				 * [클레임결제 등록] 클레임배송비 결제
				 */
				ocClaimPayment.setClmNo(ocClaim.getClmNo()); // 클레임번호
				ocClaimPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP); // 환수환불구분-환수
				// ocClaimPayment.setPymntDtm(new Timestamp(new Date().getTime())); // 결제일시
				ocClaimPayment.setPymntDtm(null); // 결제일시
				ocClaimPayment.setDeviceCode(CommonCode.DEVICE_PC); // 디바이스코드
				ocClaimPayment.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // 주결제수단여부
				// ocClaimPayment.setPymntMeansCode(null); // 결제수단코드-결제 폼에서 넘어옴
				ocClaimPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP); // 결제업체코드
				ocClaimPayment.setPymntOrganCodeText(null); // 결제기관코드
				ocClaimPayment.setPymntOrganName(null); // 결제기관명
				ocClaimPayment.setPymntOrganNoText(null); // 결제기관번호(카드번호, 은행계좌번호)
				ocClaimPayment.setIntrstFreeYn(Const.BOOLEAN_FALSE); // 무이자여부
				ocClaimPayment.setInstmtTermCount((short) 0); // 할부기간
				ocClaimPayment.setCardGbnType(null); // 카드구분
				ocClaimPayment.setCardType(null); // 카드유형
				ocClaimPayment.setPymntAprvNoText(null); // 결제승인번호
				ocClaimPayment.setPymntTodoAmt(ocClaim.getAddDlvyAmt()); // 결제예정금액
				ocClaimPayment.setPymntAmt(ocClaim.getAddDlvyAmt()); // 결제금액
				ocClaimPayment.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부
				ocClaimPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부
				ocClaimPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부
				ocClaimPayment.setEscrAprvNoText(null); // 에스크로승인번호
				ocClaimPayment.setBankCode(null); // 환불:회원환불계좌, 환수:가상계좌발급
				ocClaimPayment.setAcntNoText(null); // 계좌번호
				ocClaimPayment.setAcntHldrName(null); // 예금주명
				ocClaimPayment.setAcntCrtfcYn(Const.BOOLEAN_FALSE); // 계좌인증여부
				ocClaimPayment.setVrtlAcntIssueDtm(null); // 가상계좌발급일시
				ocClaimPayment.setVrtlAcntExprDtm(null); // 가상계좌만료일시
				ocClaimPayment.setGiftCardPinNoText(null); // 상품권PIN번호
				ocClaimPayment.setRedempRfndMemoText(null); // 환수환불메모
				ocClaimPayment.setProcImpsbltYn(Const.BOOLEAN_FALSE); // 처리불가여부
				ocClaimPayment.setProcImpsbltRsnText(null); // 처리불가사유
				ocClaimPayment.setProcImpsbltCmlptDtm(null); // 처리불가완료일시
				ocClaimPayment.setRedempRfndOpetrNo(null); // 환수환불처리자번호
				ocClaimPayment.setRedempRfndOpetrDtm(null); // 환수환불처리일시
				ocClaimPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 자사처리대상여부(클레임 배송비는 재경팀 확인 N)
				ocClaimPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_DELIVERY_AMT); // 발생사유코드 - (클레임)배송비
				ocClaimPayment.setPymntStatCode(null); // 결제상태코드
				ocClaimPayment.setRspnsCodeText(null); // 응답코드
				ocClaimPayment.setRspnsMesgText(null); // 응답메시지
				ocClaimPayment.setPymntLogInfo(null); // 결제로그
				ocClaimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_HISTORY); // 이력구분 - 이력근거용
				ocClaimPayment.setRgsterNo(ocClaim.getRgsterNo()); // 등록자
				ocClaimPayment.setModerNo(ocClaim.getModerNo()); // 수정자

				/*
				 * 클레임결제 이력용 등록
				 */
				ocClaimPaymentDao.insertClaimPayment(ocClaimPayment); // 클레임결제 등록 : 이력용

				/*
				 * 클레임결제 일반결제 등록 : 결제 성공 후 업데이트 시 clmPymntSeq 는 일반결제 등록 시 발생된 데이터 사용
				 */
				ocClaimPayment.setHistGbnType(CommonCode.HIST_GBN_TYPE_PUBLIC); // 이력구분 - 일반결제
				ocClaimPaymentDao.insertClaimPayment(ocClaimPayment); // 클레임결제 등록 : 일반결제용

				/*
				 * [클레임 배송비 결제(KCP)] 리턴결과를 이용하여 결제내역 업데이트
				 */
				kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
				PaymentResult paymentResult = payment
						.approval(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentApproval)); // KCP 결제

				// 결제 실패인 경우 exception
				if (UtilsText.equals(paymentResult.getSuccessYn(), Const.BOOLEAN_FALSE)) {
					throw new Exception(paymentResult.getMessage());
				}

				KcpPaymentApprovalReturn kcpPaymentApprovalReturn = ((KcpPaymentApprovalReturn) paymentResult
						.getData());

				List<SyCodeDetail> bankList = commonCodeService.getCode(CommonCode.BANK_CODE); // 은행코드 목록

				// kcp 리턴 bankcode를 이용하여 공통코드의 bankcode 를 추출
				String commonBankCode = bankList.stream()
						.filter(x -> UtilsText.equals(x.getAddInfo1(), kcpPaymentApprovalReturn.getBankCode()))
						.map(SyCodeDetail::getCodeDtlNo).findFirst().orElse(null);

				OcClaimPayment addDlvyPayment = new OcClaimPayment();
				if (UtilsText.equals(ocClaimPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
					addDlvyPayment.setPymntOrganCodeText(kcpPaymentApprovalReturn.getCardCd());
					addDlvyPayment.setPymntOrganName(kcpPaymentApprovalReturn.getCardName());
					addDlvyPayment.setPymntOrganNoText(kcpPaymentApprovalReturn.getCardNo());
					addDlvyPayment.setInstmtTermCount(Short.valueOf(kcpPaymentApprovalReturn.getQuota()));
					// [신용카드]카드 구분정보 (법인카드 여부), 0 : 개인, 1 : 법인
					if ("0".equals(kcpPaymentApprovalReturn.getCardBinType01())) {
						addDlvyPayment.setCardType(CommonCode.CARD_TYPE_NORMAL);
					} else {
						addDlvyPayment.setCardType(CommonCode.CARD_TYPE_CORPORATE);
					}
					// [신용카드]카드 구분정보(체크카드 여부), 0 : 일반, 1 : 체크
					if ("0".equals(kcpPaymentApprovalReturn.getCardBinType02())) {
						addDlvyPayment.setCardGbnType(CommonCode.CARD_GBN_TYPE_NORMAL);
					} else {
						addDlvyPayment.setCardGbnType(CommonCode.CARD_GBN_TYPE_CHECK);
					}

					addDlvyPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
					addDlvyPayment.setCashRcptAprvNo(null);
					addDlvyPayment.setCashRcptDealNo(null);
				} else {
					addDlvyPayment.setPymntOrganCodeText(kcpPaymentApprovalReturn.getBankCode()); // 결제기관코드(카드, 은행코드...)
					addDlvyPayment.setPymntOrganName(kcpPaymentApprovalReturn.getBankName()); // 결제기관명(카드, 은행명...)
					addDlvyPayment.setPymntOrganNoText(kcpPaymentApprovalReturn.getAccount()); // 결제기관번호(카드번호,
																								// 은행계좌번호...)
					addDlvyPayment.setBankCode(commonBankCode); // 계좌 공통코드
					addDlvyPayment.setInstmtTermCount((short) 0);
					if (kcpPaymentApprovalReturn.getCashAuthno() != null
							&& kcpPaymentApprovalReturn.getCashAuthno() != "") {
						addDlvyPayment.setCashRcptIssueYn(Const.BOOLEAN_TRUE);
						addDlvyPayment.setCashRcptAprvNo(kcpPaymentApprovalReturn.getCashAuthno());
						addDlvyPayment.setCashRcptDealNo(kcpPaymentApprovalReturn.getCashNo());
					} else {
						addDlvyPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
					}
				}
				addDlvyPayment.setPymntAprvNoText(kcpPaymentApprovalReturn.getTno()); // 결제승인번호
				addDlvyPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제상태코드
				addDlvyPayment.setRspnsCodeText(kcpPaymentApprovalReturn.getResCd()); // 응답코드
				addDlvyPayment.setRspnsMesgText(kcpPaymentApprovalReturn.getResMsg()); // 응답메시지
				addDlvyPayment.setPymntLogInfo(new ObjectMapper().writeValueAsString(kcpPaymentApprovalReturn)); // 결제로그
				addDlvyPayment.setModerNo(ocClaim.getModerNo()); // 수정자
				addDlvyPayment.setClmNo(ocClaim.getClmNo());
				addDlvyPayment.setClmPymntSeq(ocClaimPayment.getClmPymntSeq());

				try {
					// 클레임결제 수정(kcp 리턴 값 사용)
					ocClaimPaymentDao.updateClaimPaymentAddDlvyAmt(addDlvyPayment);
				} catch (Exception e) {
					// 클레임결제 수정이 실패할 경우 kcp 결제 취소
					KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
					kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
					kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
					kcpPaymentCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP 원거래 거래번호
					kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
					kcpPaymentCancel.setCustIp(kcpPaymentApproval.getCustIp()); // 변경 요청자 IP
					kcpPaymentCancel.setModDesc("가맹점 처리 실패"); // 취소 사유

					// 가상계좌 취소(가상계좌 사용중지-환불아님)
					payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));

					throw new Exception(e.getMessage());
				}

			}
			// 클레임배송비 무료쿠폰 사용 시
			else if (UtilsText.equals(ocClaim.getAddDlvyAmtPymntType(),
					CommonCode.ADD_DLVY_AMT_PYMNT_TYPE_FREE_COUPON)) {
				/*
				 * [회원 보유쿠폰 사용 업데이트]
				 */
				MbMemberCoupon mbMemberCoupon = new MbMemberCoupon();
				mbMemberCoupon.setMemberNo(ocOrder.getMemberNo());
				mbMemberCoupon.setHoldCpnSeq(ocClaim.getHoldCpnSeq());
				mbMemberCoupon.setModerNo((ocClaim.getModerNo())); // 수정자

				mbMemberCouponDao.updateMemberCouponUseInfo(mbMemberCoupon); // 회원 보유쿠폰 사용 업데이트
			}
		}

		resultMap.put("clmNo", ocClaim.getClmNo());

		return resultMap;
	}

	/**
	 * @Desc : 클레임상품 등록, 클레임상태이력 등록
	 * @Method Name : insertClaimPrdtAndClaimStatusHistory
	 * @Date : 2019. 4. 10.
	 * @Author : KTH
	 * @param ocClaim
	 * @param ocClaimProduct
	 * @param clmPrdtStatCode
	 * @throws Exception
	 */
	public void insertClaimPrdtAndClaimStatusHistory(OcClaim ocClaim, OcClaimProduct ocClaimProduct,
			String clmPrdtStatCode) throws Exception {
		ocClaimProduct.setClmNo(ocClaim.getClmNo());
		ocClaimProduct.setOrderNo(ocClaim.getOrderNo());
		ocClaimProduct.setRgsterNo(ocClaim.getRgsterNo()); // 등록자
		ocClaimProduct.setModerNo(ocClaim.getModerNo()); // 수정자
		ocClaimProduct.setClmPrdtStatCode(clmPrdtStatCode); // 클레임상품상태코드

		ocClaimProductDao.insertClaimProduct(ocClaimProduct); // 클레임상품 등록

		OcClaimStatusHistory ocClaimStatusHistory = new OcClaimStatusHistory();
		ocClaimStatusHistory.setClmNo(ocClaim.getClmNo());
		ocClaimStatusHistory.setClmPrdtSeq(ocClaimProduct.getClmPrdtSeq());
		ocClaimStatusHistory.setClmPrdtStatCode(clmPrdtStatCode); // 클레임상품상태코드
		ocClaimStatusHistory.setStockGbnCode(null);
		ocClaimStatusHistory.setNoteText(null);
		ocClaimStatusHistory.setRgsterNo(ocClaim.getRgsterNo()); // 등록자

		ocClaimStatusHistoryDao.insertClaimStatusHistory(ocClaimStatusHistory); // 클레임상태이력 등록
	}

	/*********************************************************************************************************************
	 * E : kth
	 *********************************************************************************************************************/

}
