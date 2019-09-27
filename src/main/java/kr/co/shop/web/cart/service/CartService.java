package kr.co.shop.web.cart.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.shop.common.message.Message;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.cart.model.master.OcCartBenefit;
import kr.co.shop.web.cart.repository.master.OcCartBenefitDao;
import kr.co.shop.web.cart.repository.master.OcCartDao;
import kr.co.shop.web.cart.vo.OcCartInfo;
import kr.co.shop.web.cart.vo.OcCartVendorVo;
import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.service.MemberInterestProductService;
import kr.co.shop.web.order.vo.OrderCartVo;
import kr.co.shop.web.product.model.master.PdProductOption;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockAndPrice;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.repository.master.PdProductOptionDao;
import kr.co.shop.web.product.service.ProductOptionService;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.promotion.model.master.UpperCoupon;
import kr.co.shop.web.promotion.service.CouponService;
import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartService {

	@Autowired
	OcCartDao ocCartDao;

	@Autowired
	CartService cartService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductOptionService productOptionService;

	@Autowired
	VendorService vendorService;

	@Autowired
	CouponService couponService;

	@Autowired
	OcCartBenefitDao ocCartBenefitDao;

	@Autowired
	PdProductOptionDao pdProductOptionDao;

	@Autowired
	private MemberInterestProductService memberInterestProductService;

	/*************************************************************************************************
	 * jeon start
	 *************************************************************************************************/

	/**
	 * @param ocCart
	 * @Desc : set 트랜젝션 관리를 위해
	 * @Method Name : setCartList
	 * @Date : 2019. 4. 9.
	 * @Author : flychani@3top.co.kr
	 * @return
	 */
	public Map<String, Object> setCartList(OcCart ocCart) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		boolean cart_check = false; //
		boolean isLogin = ocCart.isLogin(); // 로그인 여부

		List<OcCart> totalCartList = new ArrayList<>();

		ocCart.setCartType(Const.CART_TYPE_NOMAL);

		/**
		 * 1. 장바구니 사은품 , 배송비 , 행사 초기화
		 */
		int memCartCnt = cartService.setOcCartResetForMember(ocCart); // 회원 장바구니 장바구니 사은품 , 배송비 초기화
		int nonMemCartCnt = cartService.setOcCartResetForNonMember(ocCart); // 비회원 장바구니 장바구니 사은품 , 배송비 초기화

		cartService.setOcCartBenefitResetForMember(ocCart); // 행사 테이블 초기화
		cartService.setOcCartBenefitResetForNonMember(ocCart); // 행사 테이블 초기화

		if (isLogin) {// 비회원 로그인시 처리 필요 //
			// 회원
			List<OcCart> memberCartList = cartService.getMemberCartList(ocCart); // 회원 장바구니 상품
			List<OcCart> nonMemberCartList = cartService.getNonMemberCartList(ocCart);// 비회원 장바구니 상품

			if (nonMemberCartList.size() > 0) { // 비회원 장바구니 상품이 있을 경우

				// 동일 상품 & 옵션 상품 조회 list
				List<OcCart> equalsList = memberCartList.stream()
						.filter(mem -> nonMemberCartList.stream()
								.anyMatch(non -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				// 회원 기준 동일 상품 & 옵션 상품 제외 상품 조회 list
				List<OcCart> memExcludeList = memberCartList.stream()
						.filter(mem -> nonMemberCartList.stream()
								.noneMatch(non -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				// 비회원 기준 동일 상품 & 옵션 상품 제외 상품 조회 list
				List<OcCart> nonMemExcludeList = nonMemberCartList.stream()
						.filter(non -> memberCartList.stream()
								.noneMatch(mem -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				/**
				 * 동일상품 존재시 회원기준 장바구니에 현재 날짜로 update
				 * 
				 */
				cart_check = cartService.setCartPrdtMergeUpdate(equalsList);

				/**
				 * 동일 상품 비회원 장바구니 삭제 처리시 sessionid추가 처리 equalsList sessionid 기준 동일상품 삭제 처리
				 */
				equalsList = equalsList.stream().map(prdt -> {
					prdt.setSessionId(ocCart.getSessionId()); // session id 추가
					prdt.setNonMemberNo(Const.NON_MEMBER_NO); // 비회원 회원번호 추가
					return prdt;
				}).collect(Collectors.toList());

				equalsList.forEach(i -> {
					log.info(" session id add   cartSeq > " + i.getCartSeq() + " anyMatch  getPrdtNo > " + i.getPrdtNo()
							+ " anyMatch getPrdtOptnNo > " + i.getPrdtOptnNo() + " anyMatch session > "
							+ i.getSessionId());
				});
				cart_check = cartService.setCartPrdtMergeDelete(equalsList);

				/**
				 * 2. session id 기준 추가 상품 회원 장바구니에 담기 처리 update 비회원 장바구니에 회원번호 추가하여 update
				 */

				nonMemExcludeList = nonMemExcludeList.stream().peek(non -> non.setMemberNo(ocCart.getMemberNo()))
						.collect(Collectors.toList());

				nonMemExcludeList.forEach(i -> {
					log.info(" nonMemExcludeList memberNo add  cartSeq > " + i.getCartSeq() + " noneMatch  getPrdtNo > "
							+ i.getPrdtNo() + " noneMatch getPrdtOptnNo > " + i.getPrdtOptnNo() + " add getMemberNo > "
							+ i.getMemberNo());
				});
				cart_check = cartService.setCartPrdtAddUpdate(nonMemExcludeList);

				totalCartList.addAll(equalsList); //
				totalCartList.addAll(memExcludeList);
				totalCartList.addAll(nonMemExcludeList);
			} else { // 비회원 장바구니 상품이 없을 경우
				// 회원 장바구니
				totalCartList.addAll(memberCartList); //
			}

		} else {
			// 비회원 장바구니
			List<OcCart> nonMemberCartList = cartService.getNonMemberCartList(ocCart);// 비회원 장바구니 상품
			totalCartList.addAll(nonMemberCartList);//
		}

		log.info("totalCartList >>>>>>>>>>>>>>" + totalCartList);

		// 디바이스 정보 추가 하여 상품 조회
		totalCartList = totalCartList.stream().map(cart -> {
			cart.setDeviceCode(ocCart.getDeviceCode());
			cart.setMemberTypeCode(ocCart.getMemberTypeCode());
			cart.setMbshpGradeCode(ocCart.getMbshpGradeCode());
			cart.setEmpYn(ocCart.getEmpYn());
			return cart;
		}).collect(Collectors.toList());

		/**
		 * 상품 서비스 호출
		 */
		// ==> totalCartList 상품조회 리스트 내에 회원유형 , 회원등급 , 임직원여부 추가
		List<PdProductWithAll> prdtInfoList = new ArrayList<PdProductWithAll>();

		prdtInfoList = productService.getProductListWithAll(totalCartList);

		log.info(" 상품 서비스 호출 >>>>>>>>>>>>  " + prdtInfoList);
		if (prdtInfoList != null) {
			for (PdProductWithAll prdt : prdtInfoList) {
				if (UtilsText.equals(prdt.getSellStatCode(), "10001")) {
					int stockCnt = 0;
					if (UtilsText.equals(prdt.getMmnyPrdtYn(), "Y")) {
						stockCnt = prdt.getStockAiQty() + prdt.getStockAsQty() + prdt.getStockAsQty();
					} else {
						stockCnt = prdt.getStockVdQty();
					}

					if (stockCnt > 0) {
						prdt.setDlvyCalAmt(prdt.getTotalSellAmt()); // 배송비노출ㅁ
					} else {
						prdt.setDlvyCalAmt(0); //
					}
				} else {
					prdt.setDlvyCalAmt(0); //
				}
			}
			log.info(" 상품 서비스 호출 22 >>>>>>>>>>>>  " + prdtInfoList);
			List<OcCart> cartPrdtList = cartService.getCartProductList(totalCartList, prdtInfoList);

			long mmnyPrdtCnt = cartPrdtList.stream().filter(p -> UtilsText.equals(p.getMmnyPrdtYn(), "Y")).count();

			/**
			 * 업체 정보 및 배송비 호출
			 */

			String dlvyTypeCode = CommonCode.DLVY_TYPE_CODE_NORMAL_LOGISTICS; // 일반배송으로 조회
			List<OcCartVendorVo> vendorInfoList = cartService.getvendorInfoList(prdtInfoList, dlvyTypeCode);

			cartPrdtList.forEach(cart -> vendorInfoList.forEach(vndr -> {
				if (UtilsText.equals(cart.getVndrNo(), vndr.getVndrNo())) {
					cart.setVndrGbnType(vndr.getVndrGbnType()); // 업체 구분
					cart.setVndrName(vndr.getVndrName()); // 업체 명
					cart.setFreeDlvyStdrAmt(vndr.getFreeDlvyStdrAmt()); // 업체 무료배송 기준금액
					cart.setDlvyAmt(vndr.getDlvyAmt()); // 업체 배송비

					if (UtilsText.equals(vndr.getVndrGbnType(), "C")) {
						cart.setVndrTotalPrdtCnt(mmnyPrdtCnt); // 자사 상품 갯수
					} else {
						cart.setVndrTotalPrdtCnt(vndr.getVndrTotalPrdtCnt()); // 업체별 상품 갯수
					}

					cart.setFreeDlvyFlag(vndr.getFreeDlvyFlag()); // 업체 무료배송여부
				}
			}));

			long carTotal = cartPrdtList.size();

			long abcPickupCnt = cartPrdtList.stream().filter(p -> UtilsText.equals(p.getVndrNo(), "VD10000001")
					&& UtilsText.equals(p.getStorePickupPsbltYn(), "Y")).count();

			long gsPickupCnt = cartPrdtList.stream().filter(p -> UtilsText.equals(p.getVndrNo(), "VD10000002")
					&& UtilsText.equals(p.getStorePickupPsbltYn(), "Y")).count();

			long otsPickupCnt = cartPrdtList.stream().filter(p -> UtilsText.equals(p.getVndrNo(), "VD10000003")
					&& UtilsText.equals(p.getStorePickupPsbltYn(), "Y")).count();

			// 장바구니 상품정보 자사기준 정렬
			resultMap.put("cartList", cartPrdtList.stream().filter(f -> f.getLoopflag() != null)
					.sorted(Comparator.comparing(OcCart::getLoopflag)).collect(Collectors.toList())); // 장바구니상품
			resultMap.put("vendorInfoList", vendorInfoList); // 장바구니 상품
			resultMap.put("carTotal", carTotal); // 장바구니 전체 상품수
			resultMap.put("abcPickupCnt", abcPickupCnt); // ABC 픽업 상품수
			resultMap.put("gsPickupCnt", gsPickupCnt); // GS 픽업 상품수
			resultMap.put("otsPickupCnt", otsPickupCnt); // OTS 픽업 상품수

		} else {
			resultMap.put("cartList", null); // 장바구니상품
			resultMap.put("vendorInfoList", null); // 장바구니 상품
			resultMap.put("carTotal", 0); // 장바구니 전체 상품수
			resultMap.put("abcPickupCnt", 0); // ABC 픽업 상품수
			resultMap.put("gsPickupCnt", 0); // GS 픽업 상품수
			resultMap.put("otsPickupCnt", 0); // OTS 픽업 상품수
		}

		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getPrdtLoadList
	 * @Date : 2019. 5. 29.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public Map<String, Object> getPrdtLoadList(OcCart ocCart) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		boolean cart_check = false; //
		boolean isLogin = ocCart.isLogin(); // 로그인 여부

		List<OcCart> totalCartList = new ArrayList<>();

		ocCart.setCartType(Const.CART_TYPE_NOMAL); // 일반 장바구니

		String clickTabId = ocCart.getClickTabId();
		String reloadFlag = ocCart.getReloadFlag();
		if (!UtilsText.equals(clickTabId, "All")) {
			ocCart.setStorePickupPsbltYn("Y");
			if (UtilsText.equals(clickTabId, "ABC")) {
				ocCart.setVndrNo("VD10000001");
			} else if (UtilsText.equals(clickTabId, "GS")) {
				ocCart.setVndrNo("VD10000002");
			} else if (UtilsText.equals(clickTabId, "OTS")) {
				ocCart.setVndrNo("VD10000003");
			}
		} else {
			ocCart.setStorePickupPsbltYn("N");
		}

		/**
		 * 1. 장바구니 사은품 , 배송비 , 행사 초기화
		 */
		int memCartCnt = cartService.setOcCartResetForMember(ocCart); // 회원 장바구니 장바구니 사은품 , 배송비 초기화
		int nonMemCartCnt = cartService.setOcCartResetForNonMember(ocCart); // 비회원 장바구니 장바구니 사은품 , 배송비 초기화

		if (reloadFlag.equals("Y")) {
			cartService.setOcCartBenefitResetForMember(ocCart); // 행사 테이블 초기화
			cartService.setOcCartBenefitResetForNonMember(ocCart); // 행사 테이블 초기화
		}

		if (isLogin) {
			// 회원
			List<OcCart> memberCartList = cartService.getMemberCartList(ocCart); // 회원 장바구니 상품
			totalCartList.addAll(memberCartList);//
		} else {
			// 비회원 장바구니
			List<OcCart> nonMemberCartList = cartService.getNonMemberCartList(ocCart);// 비회원 장바구니 상품
			totalCartList.addAll(nonMemberCartList);//
		}

		// 쿠폰 정보 조회

		totalCartList = totalCartList.stream().map(cart -> {
			cart.setDeviceCode(ocCart.getDeviceCode());
			cart.setMemberTypeCode(ocCart.getMemberTypeCode());
			cart.setMbshpGradeCode(ocCart.getMbshpGradeCode());
			cart.setEmpYn(ocCart.getEmpYn());
			cart.setStorePickupPsbltYn(ocCart.getStorePickupPsbltYn());
			return cart;
		}).collect(Collectors.toList());

		/**
		 * 상품 서비스 호출
		 */
		// ==> totalCartList 상품조회 리스트 내에 회원유형 , 회원등급 , 임직원여부 추가
		List<PdProductWithAll> prdtInfoList = productService.getProductListWithAll(totalCartList);
		if (prdtInfoList != null) {

			for (PdProductWithAll prdt : prdtInfoList) {
				if (UtilsText.equals(prdt.getSellStatCode(), "10001")) {
					int stockCnt = prdt.getStockAiQty() + prdt.getStockAsQty() + prdt.getStockAsQty();
					if (stockCnt > 0) {
						prdt.setDlvyCalAmt(prdt.getTotalSellAmt()); // 배송비노출ㅁ
					} else {
						prdt.setDlvyCalAmt(0); //
					}
				} else {
					prdt.setDlvyCalAmt(0); //
				}
			}

			List<OcCart> cartPrdtList = cartService.getCartProductList(totalCartList, prdtInfoList);

			long mmnyPrdtCnt = cartPrdtList.stream().filter(p -> UtilsText.equals(p.getMmnyPrdtYn(), "Y")).count();

			/**
			 * 업체 정보 및 배송비 호출
			 */
			String dlvyTypeCode = CommonCode.DLVY_TYPE_CODE_NORMAL_LOGISTICS; // 일반배송으로 조회
			List<OcCartVendorVo> vendorInfoList = cartService.getvendorInfoList(prdtInfoList, dlvyTypeCode);

			cartPrdtList.forEach(cart -> vendorInfoList.forEach(vndr -> {
				if (UtilsText.equals(cart.getVndrNo(), vndr.getVndrNo())) {
					cart.setVndrGbnType(vndr.getVndrGbnType()); // 업체 구분
					cart.setVndrName(vndr.getVndrName()); // 업체 명
					cart.setFreeDlvyStdrAmt(vndr.getFreeDlvyStdrAmt()); // 업체 무료배송 기준금액
					cart.setDlvyAmt(vndr.getDlvyAmt()); // 업체 배송비

					if (UtilsText.equals(vndr.getVndrGbnType(), "C")) {
						cart.setVndrTotalPrdtCnt(mmnyPrdtCnt); // 자사 상품 갯수
					} else {
						cart.setVndrTotalPrdtCnt(vndr.getVndrTotalPrdtCnt()); // 업체별 상품 갯수
					}

					cart.setFreeDlvyFlag(vndr.getFreeDlvyFlag()); // 업체 무료배송여부
				}
			}));

			for (OcCart aa : cartPrdtList) {
				// System.out.println("+++++++++++++++++++++getPrdtNo++++++++++++++++++++" +
				// aa.getPrdtNo());
				// System.out.println("+++++++++++++++++++++getLoopflag++++++++++++++++++++" +
				// aa.getLoopflag());
			}

			if (!UtilsText.equals(clickTabId, "All")) {
				cartPrdtList = cartPrdtList.stream().filter(f -> f.getLoopflag() != null)
						.filter(p -> UtilsText.equals(p.getStorePickupPsbltYn(), "Y"))
						.sorted(Comparator.comparing(OcCart::getLoopflag)).collect(Collectors.toList());
			} else {
				cartPrdtList = cartPrdtList.stream().filter(f -> f.getLoopflag() != null)
						.sorted(Comparator.comparing(OcCart::getLoopflag)).collect(Collectors.toList());
			}
			// 장바구니 상품정보 자사기준 정렬
			resultMap.put("cartList", cartPrdtList); // 장바구니상품
			resultMap.put("vendorInfoList", vendorInfoList); // 장바구니 상품
		} else {
			resultMap.put("cartList", null); // 장바구니상품
			resultMap.put("vendorInfoList", null); // 장바구니 상품
		}

		return resultMap;
	}

	/**
	 * @Desc : 장바구니 상품 정보 조회
	 * @Method Name : getCartProductList
	 * @Date : 2019. 5. 3.
	 * @Author : flychani@3top.co.kr
	 * @param totalCartList
	 * @param prdtInfoList
	 * @return
	 */
	public List<OcCart> getCartProductList(List<OcCart> totalCartList, List<PdProductWithAll> prdtInfoList)
			throws Exception {
		List<OcCart> cartPrdtList = new ArrayList<OcCart>();

		cartPrdtList.addAll(totalCartList); // 장바구니 상품 조회 ( 회원 or 비회원)

		for (OcCart cart : cartPrdtList) {
			// log.info(" seq " + cart.getCartSeq());
			// log.info(" prdtNo " + cart.getPrdtNo());
		}

		for (PdProductWithAll cart : prdtInfoList) {
			// log.info(" prdtInfoList seq " + cart.getCartSeq());
			// log.info(" prdtInfoList prdtNo " + cart.getPrdtNo());
		}

		// 쿠폰 정보 호출 각 할인 금액 추출
		// 각각 set dcamt 도 추가
		// 프로모션에 set 처리

		for (OcCart cart : cartPrdtList) {
			for (PdProductWithAll prdt : prdtInfoList) {
				if (cart.getCartSeq().equals(prdt.getCartSeq())) { //

					cart.setPrdtName(prdt.getPrdtName()); // 상품명
					cart.setOptnName(prdt.getOptnName()); // 상품옵션명
					cart.setBrandName(prdt.getBrandName()); // 브랜드명
					cart.setPrdtColorInfo(prdt.getPrdtColorInfo()); // 상품색상코드
					cart.setVndrNo(prdt.getVndrNo()); // 업체정보
					cart.setFreeDlvyYn(prdt.getFreeDlvyYn()); // 무료배송여부
					cart.setMmnyPrdtYn(prdt.getMmnyPrdtYn()); // 자사상품여부
					cart.setStockIntgrYn(prdt.getStockIntgrYn()); // 재고통합여부
					cart.setStockMgmtYn(prdt.getStockMgmtYn()); // 재고관리여부
					cart.setBuyLimitYn(prdt.getBuyLimitYn()); // 구매제한여부
					cart.setMinBuyPsbltQty(prdt.getMinBuyPsbltQty()); // 최소구매가능수량
					cart.setMaxBuyPsbltQty(prdt.getMaxBuyPsbltQty()); // 최대구매가능수량

					cart.setTotalStockQty(prdt.getTotalStockQty()); // 총재고수량
					cart.setTotalOrderQty(prdt.getTotalOrderQty()); // 총주문수량

					/**
					 * 재고 수량 확인 필요 기간계 재고 수량조회시 .... 기간계 상품재고 수량으로 제어
					 */
					// log.info(" ai / as / aw " + "/" + prdt.getStockAiQty() + "/" +
					// prdt.getStockAsQty() + "/"
					// + prdt.getStockAsQty());
					if (UtilsText.equals(prdt.getMmnyPrdtYn(), "Y")) {
						cart.setStockTotalQty(prdt.getStockAiQty() + prdt.getStockAsQty() + prdt.getStockAsQty()); // as+ai+aw
						// cart.setStockTotalQty(99999); // as+ai+aw 임시
					} else {
						cart.setStockTotalQty(prdt.getStockVdQty()); // 업체 재고 수량
					}

					// 픽업 가능 상품일경우 as 재고 수량까지 확인필요
					if (UtilsText.equals(prdt.getStorePickupPsbltYn(), "Y")) {
						if (prdt.getStockAsQty() <= 0) {
							cart.setStorePickupPsbltYn("N"); // 픽업가능여부
						} else {
							cart.setStorePickupPsbltYn(prdt.getStorePickupPsbltYn()); // 픽업가능여부 Y
						}
					} else {
						cart.setStorePickupPsbltYn("N"); // 픽업가능여부 else
					}

					cart.setStockAiQty(prdt.getStockAiQty()); // 온라인 재고
					cart.setStockAwQty(prdt.getStockAwQty()); // 창고 재고
					cart.setStockAsQty(prdt.getStockAsQty()); // 매장재고
					cart.setStockVdQty(prdt.getStockVdQty()); // 업체상품 재고

					cart.setNormalAmt(prdt.getNormalAmt()); // 정상가
					cart.setSellAmt(prdt.getSellAmt()); // 판매가
					cart.setDscntAmt(prdt.getDscntAmt()); // 할인가
					cart.setOrgDscntAmt(prdt.getDscntAmt()); // 할인가
					cart.setPrdtDscntAmt(prdt.getPrdtDscntAmt()); // 상품할인

					cart.setSellStatCode(prdt.getSellStatCode()); // 판매상태코드

					cart.setImagePathText(prdt.getImagePathText());
					cart.setImageUrl(prdt.getImageUrl());

					// loopflag set SELL_STAT_CODE

					String mmnyPrdtYn = prdt.getMmnyPrdtYn(); // 자사상품여부 ( abc , gs ,ots = Y else N)
					// System.out.println(
					// " getPrdtNo >>>> " + prdt.getPrdtNo() + " getMmnyPrdtYn ????" +
					// prdt.getMmnyPrdtYn());

					if (UtilsText.equals(prdt.getMmnyPrdtYn(), "Y")) {
						cart.setLoopflag("NOLOOP"); // 자사 상품
					} else {
						cart.setLoopflag(prdt.getVndrNo()); // 입점사 상품의 경우 업체 번호
					}
					// System.out.println(" cart getLoopflag >>>> " + cart.getLoopflag());

					int cpnTotDcAmt = 0;

					// 쿠폰
					OcCartBenefit benefitCpnInfo = new OcCartBenefit();
					OcCartBenefit benefit = new OcCartBenefit();
					benefit.setCartSeq(prdt.getCartSeq());
					benefit.setDscntAmt(prdt.getDscntAmt());

					benefitCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(benefit);

//					System.out.println("========================= benefitCpnInfo" + benefitCpnInfo);
					if (benefitCpnInfo != null) {
//						System.out.println("========================= benefitCpnInfo" + benefitCpnInfo);

						// 쿠폰 할인 정보 set
						if (!UtilsText.equals(benefitCpnInfo.getNomalCpnNo(), "0")) {
							cart.setNomalCpnNo(benefitCpnInfo.getNomalCpnNo());
							cart.setNomalHoldCpnSeq(benefitCpnInfo.getNomalHoldCpnSeq());
							cart.setNomalCpnDcAmt(benefitCpnInfo.getNomalCpnApplyDcAmt());
							cpnTotDcAmt = cpnTotDcAmt + benefitCpnInfo.getNomalCpnApplyDcAmt();
						}

						if (!UtilsText.equals(benefitCpnInfo.getPlusCpnNo(), "0")) {
							cart.setPlusCpnNo(benefitCpnInfo.getPlusCpnNo());
							cart.setPlusHoldCpnSeq(benefitCpnInfo.getPlusHoldCpnSeq());
							cart.setPlusCpnDcAmt(benefitCpnInfo.getPlusCpnApplyDcAmt());
							cpnTotDcAmt = cpnTotDcAmt + benefitCpnInfo.getPlusCpnApplyDcAmt();
						}
						// 포인트 계산 금액 및 쿠폰 할인 적용된 판매가 set
						cart.setSavePoint(benefitCpnInfo.getCpnApplyTotPoint());
						cart.setTotalSellAmt(benefitCpnInfo.getCpnApplyTotSellAmt());

					} else {
						double savePoint = (prdt.getDscntAmt() * 3 / 1000.0);
						savePoint = Math.ceil(savePoint) * 10;
						cart.setSavePoint((int) (savePoint * cart.getOrderQty()));
						cart.setTotalSellAmt(prdt.getTotalSellAmt());
					}
//					System.out.println("========================= cpnTotDcAmt" + cpnTotDcAmt);

					List<PrPromotion> cartPromo = new ArrayList<PrPromotion>();
					int promoTotDcAmt = 0;
					if (prdt.getPromotion() != null) {
						PrPromotion cpn = new PrPromotion();
						cartPromo = new ArrayList<>(Arrays.asList(prdt.getPromotion()));

						for (PrPromotion promo : cartPromo) {
							if (promo.getDcAmt() != null) {
								promoTotDcAmt = promoTotDcAmt + promo.getDcAmt();
							}
						}

						if (cpnTotDcAmt > 0) {
							cpn.setPromoTypeCode("99999");
							cpn.setPromoTypeName("쿠폰 할인");
							cpn.setDcAmt(cpnTotDcAmt);
							cartPromo.add(cpn);
						}

					}

					// 프로모션 정보 중 다족구매 할인 금액 조회
					int manyShoesDcAmt = 0;
					PrPromotion manyShoesDcInfo = cartPromo.stream()
							.filter(c -> UtilsText.equals(c.getPromoTypeCode(), "10001")).findFirst().orElse(null);

					if (manyShoesDcInfo != null) {
						manyShoesDcAmt = manyShoesDcInfo.getDcAmt();
					}

					cart.setManyShoesDcAmt(manyShoesDcAmt);

					cart.setPromoTotDcAmt(promoTotDcAmt);
					cart.setPromoList(cartPromo);
					cart.setCpnTotDcAmt(cpnTotDcAmt); // mobile
//					System.out.println(">>>>>>>>>>>>>>>>> cartPromo " + cartPromo);
//					System.out.println(">>>>>>>>>>>>>>>>>  " + cartPromo);
					for (PrPromotion promo2 : cartPromo) {
//						System.out.println(" promo2 cartseq " + promo2.getCartSeq());
//						System.out.println(" promo2 getPrdtNo " + promo2.getPrdtNo());
//						System.out.println(" promo2 getPrdtOptnNo " + promo2.getPrdtOptnNo());
//						System.out.println(" promo2 PromoTypeCode " + promo2.getPromoTypeCode());
//						System.out.println(" promo2 PromoTypeName " + promo2.getPromoTypeName());
//						System.out.println(" promo2 setDcAmt " + promo2.getDcAmt());
					}
				}
			}
		}

		return cartPrdtList;
	}

	/**
	 * @Desc : 업체 정보 조회
	 * @Method Name : getvendorInfoList
	 * @Date : 2019. 5. 2.
	 * @Author : flychani@3top.co.kr
	 * @param prdtInfoList
	 * @return
	 */
	public List<OcCartVendorVo> getvendorInfoList(List<PdProductWithAll> prdtInfoList, String dlvyTypeCode)
			throws Exception {
		List<OcCartVendorVo> cartVendorInfoList = new ArrayList<>();

		List<VdVendor> vendorInfoList = new ArrayList<>();

		// 조회할 업체 번호 조회
		List<String> vndrNoList = prdtInfoList.stream().filter(x -> x != null).map(PdProductWithAll::getVndrNo)
				.distinct().collect(Collectors.toList());

		if (vndrNoList.size() <= 0) {
			return null;
		}

		// 업체 정보 조회
		vendorInfoList = vendorService.getVendorInfoList(vndrNoList);

		// 업체별 장바구니 상품 총금액 조회
		Map<String, IntSummaryStatistics> prdtSummary = prdtInfoList.stream().filter(x -> x != null).collect(Collectors
				.groupingBy(PdProductWithAll::getVndrNo, Collectors.summarizingInt(PdProductWithAll::getDlvyCalAmt)));

		for (VdVendor vndr : vendorInfoList) {
			for (Entry<String, IntSummaryStatistics> data : prdtSummary.entrySet()) {
				if (UtilsText.equals(data.getKey(), vndr.getVndrNo())) {

					OcCartVendorVo cartVndrVo = new OcCartVendorVo();

					cartVndrVo.setVndrNo(vndr.getVndrNo()); // 업체 번호
					cartVndrVo.setVndrGbnType(vndr.getVndrGbnType()); // 업체구분
					cartVndrVo.setVndrName(vndr.getVndrName()); // 업체명
					cartVndrVo.setLogisVndrCode(vndr.getLogisVndrCode()); // 택배사코드
					cartVndrVo.setDlvyDayCount(vndr.getDlvyDayCount()); // 배송소요기간
					cartVndrVo.setFreeDlvyStdrAmt(vndr.getFreeDlvyStdrAmt()); // 무료배송기준금액
					cartVndrVo.setDlvyAmt(vndr.getDlvyAmt()); // 배송비 금액

					cartVndrVo.setVndrTotalPrdtAmt(data.getValue().getSum()); // 업체별 상품금액 총합
					cartVndrVo.setVndrTotalPrdtCnt(data.getValue().getCount()); // 업체별 상품 수

					// 업체 상품별 무료배송 상품 count
					long vndrPrdtFreeDlvyCnt = prdtInfoList.stream()
							.filter(f -> UtilsText.equals(f.getVndrNo(), data.getKey()))
							.filter(f -> UtilsText.equals(f.getFreeDlvyYn(), "Y")).count();

					if (UtilsText.equals(dlvyTypeCode, CommonCode.DLVY_TYPE_CODE_STORE_PICKUP)) {
						cartVndrVo.setFreeDlvyFlag("Y");
					} else {

						if (vndrPrdtFreeDlvyCnt > 0) {
							// 무료배송
							cartVndrVo.setFreeDlvyFlag("Y");
						} else {
							// 유료배송
							int vndrTotPrdtSum = (int) data.getValue().getSum(); // int 업체별 상품금액 총합
							int vndrDlvyStdrAmt = vndr.getFreeDlvyStdrAmt(); // 무료배송기준금액

							if (vndrTotPrdtSum >= vndrDlvyStdrAmt) {
								// 무료배송
								cartVndrVo.setFreeDlvyFlag("Y");
							} else {
								// 유료배송
								cartVndrVo.setFreeDlvyFlag("N");
								if (UtilsText.equals(vndr.getVndrGbnType(), "C")) { // 자사
									cartVndrVo.setDlvyPrdtNo(Const.ART_DLVY_PRDT_NO);
									cartVndrVo.setDlvyPrdtOptnNo(Const.ART_DLVY_PRDT_OPTN_NO);
								} else {
									cartVndrVo.setDlvyPrdtNo(Const.VNDR_DLVY_PRDT_NO);
									cartVndrVo.setDlvyPrdtOptnNo(Const.VNDR_DLVY_PRDT_OPTN_NO);
								}

							}
						}
					}

					cartVendorInfoList.add(cartVndrVo);
				}
			}
		}

		return cartVendorInfoList;
	}

	/**
	 * @Desc : 회원 행사 정보 초기화
	 * @Method Name : setOcCartBenefitResetForMember
	 * @Date : 2019. 4. 19.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 */
	public int setOcCartBenefitResetForMember(OcCart ocCart) throws Exception {
		OcCart param = new OcCart();

		param.setMemberNo(ocCart.getMemberNo());
		param.setCartType(ocCart.getCartType());
		param.setSiteNo(ocCart.getSiteNo());

		int cnt = ocCartDao.setOcCartBenefitResetForMember(param);
		return cnt;
	}

	/**
	 * @Desc : 비회원 행사 정보 초기화
	 * @Method Name : setOcCartBenefitResetForNonMember
	 * @Date : 2019. 4. 19.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 */
	public int setOcCartBenefitResetForNonMember(OcCart ocCart) throws Exception {
		OcCart param = new OcCart();

		param.setMemberNo(Const.NON_MEMBER_NO); // 비회원 회원번호
		param.setSessionId(ocCart.getSessionId());
		param.setCartType(ocCart.getCartType());
		param.setSiteNo(ocCart.getSiteNo());

		int cnt = ocCartDao.setOcCartBenefitResetForNonMember(param);
		return cnt;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartPrdtAddUpdate
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param nonMemExcludeList
	 * @return
	 */
	public boolean setCartPrdtAddUpdate(List<OcCart> nonMemExcludeList) throws Exception {
		boolean flag = false;

		int cnt = ocCartDao.setCartPrdtAddUpdate(nonMemExcludeList);

		if (cnt > 0) {
			flag = true;
		}

		return flag;
	}

	/**
	 * @param ocCart
	 * @Desc :
	 * @Method Name : setCartPrdeMergeDelete
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param equalsList
	 * @return
	 */
	public boolean setCartPrdtMergeDelete(List<OcCart> equalsList) throws Exception {
		boolean flag = false;

		int cnt = ocCartDao.setCartPrdtMergeDelete(equalsList);

		if (cnt > 0) {
			flag = true;
		}

		return flag;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartPrdeMergeUpdate
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param equalsList
	 * @return
	 */
	public boolean setCartPrdtMergeUpdate(List<OcCart> equalsList) throws Exception {
		boolean flag = false;

		int cnt = ocCartDao.setCartPrdtMergeUpdate(equalsList);

		if (cnt > 0) {
			flag = true;
		}

		return flag;
	}

	/**
	 * @Desc : 회원 장바구니 장바구니 사은품 , 배송비 초기화
	 * @Method Name : setOcCartResetForMember
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public int setOcCartResetForMember(OcCart ocCart) throws Exception {

		OcCart param = new OcCart();

		String[] prdtTypeCodes = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY }; // 상품유형(사은품,배송비)

		param.setPrdtTypeCodes(prdtTypeCodes);
		param.setMemberNo(ocCart.getMemberNo());
		param.setCartType(ocCart.getCartType());
		param.setSiteNo(ocCart.getSiteNo());

		return ocCartDao.setOcCartReset(param);
	}

	/**
	 * @Desc : 비회원 장바구니 장바구니 사은품 , 배송비 초기화
	 * @Method Name : setOcCartResetForNonMember
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public int setOcCartResetForNonMember(OcCart ocCart) throws Exception {
		OcCart param = new OcCart();

		param.setMemberNo(Const.NON_MEMBER_NO); // 비회원 회원번호
		param.setSessionId(ocCart.getSessionId());
		String[] prdtTypeCodes = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY }; // 상품유형(사은품,배송비)
		param.setPrdtTypeCodes(prdtTypeCodes);
		param.setCartType(ocCart.getCartType());
		param.setSiteNo(ocCart.getSiteNo());

		return ocCartDao.setOcCartReset(param);
	}

	/**
	 * @Desc : 회원 장바구니 상품 조회 ( 로그인 후)
	 * @Method Name : getMemberCartList
	 * @Date : 2019. 4. 17.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public List<OcCart> getMemberCartList(OcCart ocCart) throws Exception {
		List<OcCart> memberCartList = ocCartDao.getMemberCartList(ocCart);
		return memberCartList;
	}

	/**
	 * @Desc :
	 * @Method Name : getNonMemberCartList
	 * @Date : 2019. 4. 15.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public List<OcCart> getNonMemberCartList(OcCart ocCart) throws Exception {
		OcCart param = new OcCart();

		param.setMemberNo(Const.NON_MEMBER_NO); // 비회원 회원번호
		param.setSessionId(ocCart.getSessionId());
		param.setCartType(ocCart.getCartType());
		param.setSiteNo(ocCart.getSiteNo());
		param.setVndrNo(ocCart.getVndrNo());

		List<OcCart> list = ocCartDao.getNonMemberCartList(param);
		return list;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartAdd
	 * @Date : 2019. 4. 19.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> setCartAdd(OcCart params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_TRUE;
		String resultMsg = Message.getMessage("cart.msg.cartPrdtSave"); // 상품이 장바구니에 담겼습니다.
		String[] cartSeqs = null;

		boolean isLogin = params.isLogin(); // 로그인 여부

		List<OcCart> addCartList = new ArrayList<>(); // 추가할 장바구니 상품

		OcCart[] prdtList = params.getPrdtList();

		for (OcCart cart : prdtList) {

		}

		/*
		 * 장바구니 상품 list
		 */
		List<OcCart> prdtLists = Arrays.asList(prdtList);
		// 바로 구매 상품 담기게 처리 +
		boolean cart_check = true;

		if (UtilsText.equals(params.getCartType(), "D")) { // 바로구매

			if (isLogin) {
				addCartList = prdtLists.stream().map(prdt -> {
					prdt.setSessionId(params.getSessionId()); // session id
					prdt.setMemberNo(params.getMemberNo()); // 회원번호
					return prdt;
				}).collect(Collectors.toList());
			} else {

				addCartList = prdtLists.stream().map(prdt -> {
					prdt.setSessionId(params.getSessionId()); // session id
					prdt.setMemberNo(Const.NON_MEMBER_NO); // 회원번호
					return prdt;
				}).collect(Collectors.toList());
			}
		} else { // 일반

			// 장바구니 행사 정보 초기화
			int memCartCnt = cartService.setOcCartResetForMember(params); // 회원 장바구니 장바구니 사은품 , 배송비 초기화
			int nonMemCartCnt = cartService.setOcCartResetForNonMember(params); // 비회원 장바구니 장바구니 사은품 , 배송비 초기화

			cartService.setOcCartBenefitResetForMember(params); // 행사 테이블 초기화
			cartService.setOcCartBenefitResetForNonMember(params); // 행사 테이블 초기화

			List<OcCart> memberCartList = cartService.getMemberCartList(params); // 회원 장바구니 상품
			List<OcCart> nonMemberCartList = cartService.getNonMemberCartList(params);// 비회원 장바구니 상품

			if (isLogin) {

				// 회원 기준 장바구니 상품 = 장바구니 추가 상품 목록 추출 ==> 등록 제외 상품
				List<OcCart> memEqualsList = memberCartList.stream()
						.filter(mem -> prdtLists.stream()
								.anyMatch(non -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				memEqualsList.forEach(i -> {
					log.info(" anyMatch  cartSeq > " + i.getCartSeq() + " anyMatch  getPrdtNo > " + i.getPrdtNo()
							+ " anyMatch getPrdtOptnNo > " + i.getPrdtOptnNo());
				});

				// 장바구니 추가 상품 목록 추출 != 회원 기준 장바구니 상품 list ==> 추가 등록 상품
				List<OcCart> memExcludeList = prdtLists.stream().filter(
						prdt -> memberCartList.stream().noneMatch(non -> non.getPrdtNo().equals(prdt.getPrdtNo())
								&& non.getPrdtOptnNo().equals(prdt.getPrdtOptnNo())))
						.map(prdt -> {
							prdt.setSessionId(params.getSessionId()); // session id
							prdt.setMemberNo(params.getMemberNo()); // 회원번호
							return prdt;
						}).collect(Collectors.toList());

				memExcludeList.forEach(i -> {
					log.info(" noneMatch  cartSeq > " + i.getCartSeq() + " noneMatch  getPrdtNo > " + i.getPrdtNo()
							+ " noneMatch getPrdtOptnNo > " + i.getPrdtOptnNo());
				});

				addCartList.addAll(memExcludeList); //
			} else {

				// 비회원 기준 장바구니 상품 = 장바구니 추가 상품 목록 추출 ==> 등록 제외 상품1
				List<OcCart> nonMemEqualsList = nonMemberCartList.stream()
						.filter(mem -> prdtLists.stream()
								.anyMatch(non -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				nonMemEqualsList.forEach(i -> {
					log.info(" nonMemEqualsList nonanyMatch  cartSeq > " + i.getCartSeq() + " anyMatch  getPrdtNo > "
							+ i.getPrdtNo() + " anyMatch getPrdtOptnNo > " + i.getPrdtOptnNo());
				});

				// 장바구니 추가 상품 목록 추출 != 비회원 기준 장바구니 상품 list ==> 추가 등록 상품
				List<OcCart> nonMemExcludeList = prdtLists.stream().filter(
						prdt -> nonMemberCartList.stream().noneMatch(non -> non.getPrdtNo().equals(prdt.getPrdtNo())
								&& non.getPrdtOptnNo().equals(prdt.getPrdtOptnNo())))
						.map(prdt -> {
							prdt.setSessionId(params.getSessionId()); // session id
							prdt.setMemberNo(Const.NON_MEMBER_NO); // 회원번호
							return prdt;
						}).collect(Collectors.toList());

				nonMemExcludeList.forEach(i -> {
					log.info(" nonMemExcludeList noneMatch  cartSeq > " + i.getCartSeq() + " noneMatch  getPrdtNo > "
							+ i.getPrdtNo() + " noneMatch getPrdtOptnNo > " + i.getPrdtOptnNo()
							+ " noneMatch getCartType > " + i.getCartType());
				});

				addCartList.addAll(nonMemExcludeList); //
			}
		}

		// 필수 항목 체크
		for (OcCart cart : addCartList) {
			if (cart.getPrdtNo() == "" || cart.getPrdtNo() == null) {
				resultCode = Const.BOOLEAN_FALSE;
				resultMsg = "상품정보가 부족합니다.(상품번호)";
				cart_check = false;
			}
			if (cart.getPrdtOptnNo() == "" || cart.getPrdtOptnNo() == null) {
				resultCode = Const.BOOLEAN_FALSE;
				resultMsg = "상품정보가 부족합니다.(상품옵션)";
				cart_check = false;
			}
			if (String.valueOf(cart.getOrderQty()) == "" || cart.getOrderQty() == null) {
				resultCode = Const.BOOLEAN_FALSE;
				resultMsg = "상품정보가 부족합니다.(수량)";
				cart_check = false;
			} else {
				if (cart.getOrderQty() <= 0) {
					resultCode = Const.BOOLEAN_FALSE;
					resultMsg = "상품정보가 부족합니다.(수량)";
					cart_check = false;
				}
			}
		}

		addCartList = addCartList.stream().map(prdt -> {
			prdt.setSiteNo(params.getSiteNo());
			prdt.setMbshpGradeCode(params.getMbshpGradeCode());
			return prdt;
		}).collect(Collectors.toList());

		if (cart_check) {
			if (addCartList.size() > 0) {
				// 장바구니 상품 추가 처리
				cartSeqs = cartService.setCartInsert(addCartList);
			} else {
				resultCode = "N";
				resultMsg = "등록 상품 없음 ";
			}
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		resultMap.put("cartSeqs", cartSeqs);

		return resultMap;
	}

	/**
	 * @Desc : 장바구니 상품 등록
	 * @Method Name : setCartInsert
	 * @Date : 2019. 4. 22.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @param addCartList
	 */
	public String[] setCartInsert(List<OcCart> addCartList) throws Exception {
		String[] cartSeqs;

		List<OcCart> addCartSeqList = new ArrayList<OcCart>(); // 장바구니 상품 return 정보

		for (OcCart cartPrdt : addCartList) {
			ocCartDao.setCartPrdtInsert(cartPrdt);

			addCartSeqList.add(cartPrdt);
		}

		// String[] cartSeqs = addCartSeqList.stream().map(m -> ((Long)
		// m.getCartSeq()).toString())
		// .collect(Collectors.<String>toList()).toArray(new
		// String[addCartSeqList.size()]);

		/**
		 * 등록한 상품 배열로 주문쪽 전달 위해 ( 바로구매시 )
		 */
		cartSeqs = addCartSeqList.stream().map(m -> String.valueOf(m.getCartSeq())).collect(Collectors.<String>toList())
				.toArray(new String[addCartSeqList.size()]);

		return cartSeqs;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartPrdtDelete
	 * @Date : 2019. 4. 26.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> setCartPrdtDelete(OcCart params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String resultCode = Const.BOOLEAN_TRUE;
		String resultMsg = "상품이 삭제되었습니다.";

		int cnt = ocCartDao.setCartPrdtDelete(params);

		if (cnt <= 0) {
			resultCode = Const.BOOLEAN_FALSE;
			resultMsg = "상품이 삭제되지 않았습니다.";
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getCartPrtdQtyValidate
	 * @Date : 2019. 4. 28.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCartPrtdQtyValidate(OcCart params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_TRUE;
		String resultMsg = "수정되었습니다.";

		String clickTabId = params.getClickTabId();
		String storePickupYn = "N";

		if (!UtilsText.equals(clickTabId, "All")) { // 픽업일경우
			storePickupYn = "Y";
		}

		int totStockQty = productOptionService.getProductOptionStock(params.getPrdtNo(), params.getPrdtOptnNo(),
				storePickupYn);

		log.info("totStockQty >> " + totStockQty);

		if (totStockQty < 0) {
			resultCode = Const.BOOLEAN_FALSE;
			resultMsg = "재고 수량이 부족합니다.";
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartPrtdQtySave
	 * @Date : 2019. 4. 28.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> setCartPrtdQtySave(OcCart params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_TRUE;
		String resultMsg = "상품 수량이 변경되었습니다.";

		System.out.println("params >> " + params);

		int cnt = ocCartDao.setCartPrtdQtySave(params);

		if (cnt <= 0) {
			resultCode = Const.BOOLEAN_FALSE;
			resultMsg = "상품 수량이 변경되지 않았습니다.";
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getCartPrdtOPtionLayer
	 * @Date : 2019. 5. 10.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCartPrdtOPtionLayer(OcCart params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 상품번호 및 옵션 정보 조회 ocCart
		List<OcCart> cartPrdtList = ocCartDao.getCartProduct(params);

		// 상품 조회 조건 추가
		cartPrdtList = cartPrdtList.stream().map(prdt -> {
			prdt.setMemberTypeCode(params.getMemberTypeCode());
			prdt.setMbshpGradeCode(params.getMbshpGradeCode());
			prdt.setEmpYn(params.getEmpYn());
			prdt.setDeviceCode(params.getDeviceCode());
			return prdt;
		}).collect(Collectors.toList());

		// 상품 기본정보 조회

		List<PdProductWithAll> prdtInfoList = productService.getProductListWithAll(cartPrdtList);

		// 상품 옵션 목록 조회
		String prdtNo = prdtInfoList.get(0).getPrdtNo(); // 상품번호
		String mmnyPrdtYn = prdtInfoList.get(0).getMmnyPrdtYn(); // 자사상품여부

		List<PdProductOptionWithStockAndPrice> prdtOptList = productOptionService
				.getProductOptionListWithStockAndPrice(prdtNo);

		long sizeOptn = prdtOptList.stream().filter(p -> !UtilsText.isEmpty(p.getOptnName())).count(); // 사이즈
		long addOptn = prdtOptList.stream().filter(p -> !UtilsText.isEmpty(p.getAddOptn2Text())).count(); // 추가옵션

		resultMap.put("prdtInfo", prdtInfoList); // 상품 기본 정보
		resultMap.put("prdtOptList", prdtOptList); // 상품 옵션 리스트
		resultMap.put("sizeOptn", sizeOptn); // 상품 옵션 리스트
		resultMap.put("addOptn", addOptn); // 상품 옵션 리스트

		return resultMap;
	}

	/**
	 * @Desc : 상품 옵션 수정 저장
	 * @Method Name : setCartPrdtOptionSave
	 * @Date : 2019. 5. 16.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> setCartPrdtOptionSave(OcCart params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String resultCode = Const.BOOLEAN_TRUE;
		String resultMsg = "상품이 수정되었습니다.";
		String clickTabId = params.getClickTabId();

		boolean isExcept = true;

//		List<OcCart> cartPrdtList = ocCartDao.getCartProduct(params);
//
//		// 상품 조회 조건 추가
//		cartPrdtList = cartPrdtList.stream().map(prdt -> {
//			prdt.setMemberTypeCode(params.getMemberTypeCode());
//			prdt.setMbshpGradeCode(params.getMbshpGradeCode());
//			prdt.setEmpYn(params.getEmpYn());
//			prdt.setDeviceCode(params.getDeviceCode());
//			return prdt;
//		}).collect(Collectors.toList());
//
//		// 상품 기본정보 조회
//
//		List<PdProductWithAll> prdtInfoList = productService.getProductListWithAll(cartPrdtList);
//
//		if (!UtilsText.equals(clickTabId, "All")) {
//			// 수량 제어는 AS 재고 수량으로 제어 처리
//
//			for (OcCart cart : cartPrdtList) {
//				for (PdProductWithAll prdt : prdtInfoList) {
//					if (cart.getCartSeq().equals(prdt.getCartSeq())) {
//						if (cart.getOrderQty() > prdt.getStockAsQty()) {
//							resultCode = Const.BOOLEAN_FALSE;
//							resultMsg = "매장재고 수량이 부족합니다.";
//							isExcept = false;
//							break;
//						}
//					}
//				}
//			}
//
//		} else { // all
//			for (OcCart cart : cartPrdtList) {
//				for (PdProductWithAll prdt : prdtInfoList) {
//					if (cart.getCartSeq().equals(prdt.getCartSeq())) {
//						int totStockQty = prdt.getStockAsQty() + prdt.getStockAiQty() + prdt.getStockAwQty();
//						if (cart.getOrderQty() > totStockQty) {
//							resultCode = Const.BOOLEAN_FALSE;
//							resultMsg = "재고 수량이 부족합니다.";
//							isExcept = false;
//							break;
//						}
//					}
//				}
//			}
//		}

		String storePickupYn = "N";

		if (!UtilsText.equals(clickTabId, "All")) { // 픽업일경우
			storePickupYn = "Y";
		}

		int totStockQty = productOptionService.getProductOptionStock(params.getPrdtNo(), params.getPrdtOptnNo(),
				storePickupYn);

		log.info("totStockQty >> " + totStockQty);

		if (totStockQty < 0) {
			resultCode = Const.BOOLEAN_FALSE;
			resultMsg = "재고 수량이 부족합니다.";
			isExcept = false;
		}

		if (isExcept) {

			int cnt = ocCartDao.setCartPrdtOptionSave(params);

			if (cnt <= 0) {
				resultCode = Const.BOOLEAN_FALSE;
				resultMsg = "상품이 수정되지 않았습니다.";
			}
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getCartPrdtCouponLayer
	 * @Date : 2019. 5. 16.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCartPrdtCouponLayer(OcCart params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 상품번호 및 옵션 정보 조회 ocCart
		List<OcCart> cartPrdtList = ocCartDao.getCartProduct(params);

		OcCartBenefit ocCartBenefit = new OcCartBenefit();
		ocCartBenefit.setCartSeq(params.getCartSeq());

		// 적용된 상품 쿠폰 정보 조회
		List<OcCartBenefit> cartCouponList = ocCartBenefitDao.getCartCoupon(ocCartBenefit);

		// 상품 조회 조건 추가
		cartPrdtList = cartPrdtList.stream().map(prdt -> {
			prdt.setMemberTypeCode(params.getMemberTypeCode());
			prdt.setMbshpGradeCode(params.getMbshpGradeCode());
			prdt.setEmpYn(params.getEmpYn());
			prdt.setDeviceCode(params.getDeviceCode());
			return prdt;
		}).collect(Collectors.toList());

		// 상품 기본정보 조회
		List<PdProductWithAll> prdtInfoList = productService.getProductListWithAll(cartPrdtList);

		String memberNo = params.getMemberNo();
		String cartSeq = Long.toString(params.getCartSeq());
		int sellPrice = params.getDscntAmt(); // 판매가
		String siteNo = params.getSiteNo(); //
		String prdtNo = params.getPrdtNo();
		String prdtOptnNo = params.getPrdtOptnNo();
		int qty = params.getOrderQty(); // 주문 수량
		String mbshpGradeCode = params.getMbshpGradeCode();
		int manyShoesDcAmt = params.getManyShoesDcAmt();

		Map<String, String> couponInfo = new HashMap<>();
		couponInfo.put("memberNo", memberNo);
		couponInfo.put("cartSeq", cartSeq);
		couponInfo.put("sellPrice", String.valueOf(sellPrice));
		couponInfo.put("siteNo", siteNo);
		couponInfo.put("prdtNo", prdtNo);
		couponInfo.put("prdtOptnNo", prdtOptnNo);
		couponInfo.put("qty", String.valueOf(qty));
		couponInfo.put("mbshpGradeCode", mbshpGradeCode);
		couponInfo.put("manyShoesDcAmt", String.valueOf(manyShoesDcAmt));

		UpperCoupon couponList = couponService.getCanDownloadCouponList(memberNo, cartSeq, prdtNo, prdtOptnNo, qty,
				sellPrice, siteNo, mbshpGradeCode);

		for (MbMemberCoupon m : couponList.getMemberCoupon()) {
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + m);
		}

		List<MbMemberCoupon> memberPrdtCouponList = Arrays.asList(couponList.getMemberCoupon()); // 회원 발급 쿠폰
		List<PrCoupon> downloadableCouponList = Arrays.asList(couponList.getCoupon()); // 다운로드 가능 쿠폰

		// 적용된 쿠폰 정보 확인 노출 처리
		memberPrdtCouponList.forEach(mpc -> cartCouponList.forEach(ccp -> {
			if (UtilsText.equals(mpc.getCpnNo(), ccp.getCpnNo())
					&& UtilsText.equals(String.valueOf(mpc.getHoldCpnSeq()), String.valueOf(ccp.getHoldCpnSeq()))) {
				mpc.setCartCpnNo(ccp.getCpnNo());
				mpc.setCartCpnSelected(true);
			}
		}));

		OcCartBenefit benefitCpnInfo = null;
		if (cartCouponList.size() > 0) {

			OcCartBenefit benefit = new OcCartBenefit();
			benefit.setCartSeq(params.getCartSeq());
			benefit.setDscntAmt(params.getDscntAmt());

			benefitCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(benefit);
		}

		// 제휴사 인입시 쿠폰 사용에 대한 내용 정리 필요
		// 일반 쿠폰 ( CPN_TYPE_CODE 할인쿠폰 10000 + 제휴사 쿠폰 10001 ) & NORMAL_CPN_YN = Y
		// 다족구매 할인 적용 상품의 경우 정률 쿠폰 제외
		List<MbMemberCoupon> nomalCpnList = memberPrdtCouponList.stream().filter(
				x -> UtilsText.equals(x.getCpnTypeCode(), "10000") || UtilsText.equals(x.getCpnTypeCode(), "10001"))
				.filter(c -> UtilsText.equals(c.getNormalCpnYn(), "Y")).collect(Collectors.toList());

		log.info("nomal >>>" + nomalCpnList);
		// 플러스 쿠폰 ( CPN_TYPE_CODE 할인쿠폰 10000 + 제휴사 쿠폰 10001 ) & NORMAL_CPN_YN = N
		// 다족구매 할인 적용 상품의 경우 정률 쿠폰 제외
		List<MbMemberCoupon> plusCpnList = memberPrdtCouponList.stream().filter(
				x -> UtilsText.equals(x.getCpnTypeCode(), "10000") || UtilsText.equals(x.getCpnTypeCode(), "10001"))
				.filter(c -> UtilsText.equals(c.getNormalCpnYn(), "N")).collect(Collectors.toList());

		// 다족구매 할인이 있는 경우 정률 쿠폰은 제외 처리
		if (manyShoesDcAmt > 0) {
			nomalCpnList = nomalCpnList.stream().filter(c -> !UtilsText.equals(c.getDscntType(), "R"))
					.collect(Collectors.toList());
			plusCpnList = plusCpnList.stream().filter(c -> !UtilsText.equals(c.getDscntType(), "R"))
					.collect(Collectors.toList());
		}

		int nomalCpnApplyDcAmt = 0;
		int plusCpnApplyDcAmt = 0;

		if (benefitCpnInfo != null) {
			// 쿠폰 할인 정보 set
			if (!UtilsText.equals(benefitCpnInfo.getNomalCpnNo(), "0")) {
				nomalCpnApplyDcAmt = benefitCpnInfo.getNomalCpnApplyDcAmt();
			}
			if (!UtilsText.equals(benefitCpnInfo.getPlusCpnNo(), "0")) {
				plusCpnApplyDcAmt = benefitCpnInfo.getPlusCpnApplyDcAmt();
			}
		}

		resultMap.put("prdtInfo", prdtInfoList); // 상품 기본 정보
		resultMap.put("downloadableCouponList", downloadableCouponList); // 다운로드 가능한 상품 쿠폰 목록
		resultMap.put("nomalCpnList", nomalCpnList); // 일반 쿠폰 목록
		resultMap.put("plusCpnList", plusCpnList); // 플러스 쿠폰 목록
		resultMap.put("manyShoesDcAmt", manyShoesDcAmt); // 다족구매 프로모션 할인 금액
		resultMap.put("benefitCpnInfo", benefitCpnInfo); // 쿠폰 적용 정보 금액
		resultMap.put("nomalCpnApplyDcAmt", nomalCpnApplyDcAmt); // 일반쿠폰 적용 금액
		resultMap.put("plusCpnApplyDcAmt", plusCpnApplyDcAmt); // 플러스 쿠폰 적용 금액
		resultMap.put("couponInfo", couponInfo); // 플러스 쿠폰 적용 금액

		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getCartPrdtCouponAppend
	 * @Date : 2019. 6. 25.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCartPrdtCouponAppend(OcCart params) throws Exception {
		System.out.println("backend  " + params);
		// manyShoesDcAmt=0, memberNo=MB00000417, mbshpGradeCode=10000, siteNo=10000,
		// cartSeq=883, prdtNo=1000004532, qty=3, sellPrice=63200, prdtOptnNo=210
//		System.out.println("backend manyShoesDcAmt " + params.getManyShoesDcAmt());
//		System.out.println("backend getMemberNo " + params.getMemberNo());
//		System.out.println("backend mbshpGradeCode " + params.getMbshpGradeCode());
//		System.out.println("backend siteNo " + params.getSiteNo());
//		System.out.println("backend cartSeq " + params.getCartSeq());
//		System.out.println("backend prdtNo " + params.getPrdtNo());
//		System.out.println("backend prdtOptnNo " + params.getPrdtOptnNo());
//		System.out.println("backend getOrderQty " + params.getOrderQty());
//		System.out.println("backend sellPrice " + params.getDscntAmt());

		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 상품번호 및 옵션 정보 조회 ocCart
		List<OcCart> cartPrdtList = ocCartDao.getCartProduct(params);

		OcCartBenefit ocCartBenefit = new OcCartBenefit();
		ocCartBenefit.setCartSeq(params.getCartSeq());

		// 적용된 상품 쿠폰 정보 조회
		List<OcCartBenefit> cartCouponList = ocCartBenefitDao.getCartCoupon(ocCartBenefit);

		String memberNo = params.getMemberNo();
		String cartSeq = Long.toString(params.getCartSeq());
		int sellPrice = params.getDscntAmt(); // 판매가
		String siteNo = params.getSiteNo(); //
		String prdtNo = params.getPrdtNo();
		String prdtOptnNo = params.getPrdtOptnNo();
		int qty = params.getOrderQty(); // 주문 수량
		String mbshpGradeCode = params.getMbshpGradeCode();
		int manyShoesDcAmt = params.getManyShoesDcAmt();

		UpperCoupon couponList = couponService.getCanDownloadCouponList(memberNo, cartSeq, prdtNo, prdtOptnNo, qty,
				sellPrice, siteNo, mbshpGradeCode);

		for (MbMemberCoupon m : couponList.getMemberCoupon()) {
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + m);
		}

		List<MbMemberCoupon> memberPrdtCouponList = Arrays.asList(couponList.getMemberCoupon()); // 회원 발급 쿠폰

		// 적용된 쿠폰 정보 확인 노출 처리
		memberPrdtCouponList.forEach(mpc -> cartCouponList.forEach(ccp -> {
			if (UtilsText.equals(mpc.getCpnNo(), ccp.getCpnNo())
					&& UtilsText.equals(String.valueOf(mpc.getHoldCpnSeq()), String.valueOf(ccp.getHoldCpnSeq()))) {
				mpc.setCartCpnNo(ccp.getCpnNo());
				mpc.setCartCpnSelected(true);
			}
		}));

		OcCartBenefit benefitCpnInfo = null;
		if (cartCouponList.size() > 0) {

			OcCartBenefit benefit = new OcCartBenefit();
			benefit.setCartSeq(params.getCartSeq());
			benefit.setDscntAmt(params.getDscntAmt());

			benefitCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(benefit);
		}

		// 제휴사 인입시 쿠폰 사용에 대한 내용 정리 필요
		// 일반 쿠폰 ( CPN_TYPE_CODE 할인쿠폰 10000 + 제휴사 쿠폰 10001 ) & NORMAL_CPN_YN = Y
		// 다족구매 할인 적용 상품의 경우 정률 쿠폰 제외
		List<MbMemberCoupon> nomalCpnList = memberPrdtCouponList.stream().filter(
				x -> UtilsText.equals(x.getCpnTypeCode(), "10000") || UtilsText.equals(x.getCpnTypeCode(), "10001"))
				.filter(c -> UtilsText.equals(c.getNormalCpnYn(), "Y")).collect(Collectors.toList());

		log.info("nomal >>>" + nomalCpnList);
		// 플러스 쿠폰 ( CPN_TYPE_CODE 할인쿠폰 10000 + 제휴사 쿠폰 10001 ) & NORMAL_CPN_YN = N
		// 다족구매 할인 적용 상품의 경우 정률 쿠폰 제외
		List<MbMemberCoupon> plusCpnList = memberPrdtCouponList.stream().filter(
				x -> UtilsText.equals(x.getCpnTypeCode(), "10000") || UtilsText.equals(x.getCpnTypeCode(), "10001"))
				.filter(c -> UtilsText.equals(c.getNormalCpnYn(), "N")).collect(Collectors.toList());

		// 다족구매 할인이 있는 경우 정률 쿠폰은 제외 처리
		if (manyShoesDcAmt > 0) {
			nomalCpnList = nomalCpnList.stream().filter(c -> !UtilsText.equals(c.getDscntType(), "R"))
					.collect(Collectors.toList());
			plusCpnList = plusCpnList.stream().filter(c -> !UtilsText.equals(c.getDscntType(), "R"))
					.collect(Collectors.toList());
		}

		int nomalCpnApplyDcAmt = 0;
		int plusCpnApplyDcAmt = 0;

		if (benefitCpnInfo != null) {
			// 쿠폰 할인 정보 set
			if (!UtilsText.equals(benefitCpnInfo.getNomalCpnNo(), "0")) {
				nomalCpnApplyDcAmt = benefitCpnInfo.getNomalCpnApplyDcAmt();
			}
			if (!UtilsText.equals(benefitCpnInfo.getPlusCpnNo(), "0")) {
				plusCpnApplyDcAmt = benefitCpnInfo.getPlusCpnApplyDcAmt();
			}
		}

		resultMap.put("nomalCpnList", nomalCpnList); // 일반 쿠폰 목록
		resultMap.put("plusCpnList", plusCpnList); // 플러스 쿠폰 목록
		resultMap.put("manyShoesDcAmt", manyShoesDcAmt); // 다족구매 프로모션 할인 금액
		resultMap.put("benefitCpnInfo", benefitCpnInfo); // 쿠폰 적용 정보 금액
		resultMap.put("nomalCpnApplyDcAmt", nomalCpnApplyDcAmt); // 일반쿠폰 적용 금액
		resultMap.put("plusCpnApplyDcAmt", plusCpnApplyDcAmt); // 플러스 쿠폰 적용 금액

		return resultMap;
	}

	/**
	 * 
	 * @Desc : 할인금액 return
	 * @Method Name : discountAmt
	 * @Date : 2019. 5. 31.
	 * @Author : flychani@3top.co.kr
	 * @param price
	 * @param rate
	 * @return
	 */
	public static int discountAmt(int price, int rate) {

		double discountRate = (rate * 100.0) / 100.0;
		int minusPrice = (int) (price * (discountRate / 100));

		int discountAmt = ((int) Math.ceil((double) minusPrice / 100)) * 100;
		return discountAmt;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartApplyCoupon
	 * @Date : 2019. 6. 7.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> setCartApplyCoupon(OcCartBenefit params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCode = Const.BOOLEAN_TRUE;
		String resultMsg = "적용되었습니다."; //

		boolean isLogin = params.isLogin(); // 로그인 여부

		OcCartBenefit[] applyCouponList = null;
		OcCartBenefit applyCpnInfo = new OcCartBenefit();
		List<OcCartBenefit> delCpnList = new ArrayList<>(); // 쿠폰 적용 제외 처리

		System.out.println("params.getApplyCouponList().length >>>>>>>>>>>>>" + params.getApplyCouponList().length);
		System.out.println("params.getApplyCouponFlag() >>>>>>>>>>>>>" + params.getApplyCouponFlag());

		// 기 적용된 쿠폰 목록
		OcCartBenefit ocCartBenefit = new OcCartBenefit();
		ocCartBenefit.setMemberNo(params.getMemberNo());
		ocCartBenefit.setCartSeq(params.getCartSeq());

		List<OcCartBenefit> appliedCpnList = ocCartBenefitDao.getCartCoupon(ocCartBenefit); // 기 적용된 쿠폰 목록

		if (params.getApplyCouponList().length > 0) { // 쿠폰 적용 목록이 있을 경우

			applyCouponList = params.getApplyCouponList();
			List<OcCartBenefit> applyCpnList = Arrays.asList(applyCouponList); // 인입된 쿠폰 리스트

			AtomicInteger counter = new AtomicInteger(0); // 적용 순번 추가
			applyCpnList = applyCpnList.stream().map(cpn -> {
				cpn.setMemberNo(params.getMemberNo()); // 회원번호 추가
				cpn.setCartBenefitSeq(counter.incrementAndGet()); // 적용 순번 추가
				return cpn;
			}).collect(Collectors.toList());

			for (OcCartBenefit del : applyCpnList) {
				OcCartBenefit delCpn = new OcCartBenefit();
				// if (String.valueOf(del.getDelCartSeq()) != "") {
				if (del.getDelCartSeq() != null) {
					delCpn.setMemberNo(params.getMemberNo());
					delCpn.setCartSeq(del.getDelCartSeq());
					delCpn.setCpnYn(del.getCpnYn());
					delCpn.setHoldCpnSeq(del.getDelHoldCpnSeq());
					delCpn.setDelCartSeq(del.getDelCartSeq());
					delCpn.setDelHoldCpnSeq(del.getDelHoldCpnSeq());
					delCpn.setDelDcamt(del.getDelDcamt());
					delCpn.setDelDscntAmt(del.getDelDscntAmt());
					delCpn.setDscntAmt(del.getDelDscntAmt());
					delCpnList.add(delCpn);
				}
			}

			if (delCpnList.size() > 0) {
				for (OcCartBenefit del : delCpnList) {

					int delcnt = cartService.deleteCartPrdtCouponReset(del);

					// 삭제된 쿠폰 정보 금액 조회
					OcCartBenefit delCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(del);
					if (delCpnInfo != null) {
						del.setNomalCpnNo(delCpnInfo.getNomalCpnNo());
						del.setNomalHoldCpnSeq(delCpnInfo.getNomalHoldCpnSeq());
						del.setNomalCpnApplyDcAmt(delCpnInfo.getNomalCpnApplyDcAmt());
						del.setPlusCpnNo(delCpnInfo.getPlusCpnNo());
						del.setPlusHoldCpnSeq(delCpnInfo.getPlusHoldCpnSeq());
						del.setPlusCpnApplyDcAmt(delCpnInfo.getPlusCpnApplyDcAmt());

						del.setCpnApplyTotSellAmt(delCpnInfo.getCpnApplyTotSellAmt());
						del.setCpnApplyTotPoint(delCpnInfo.getCpnApplyTotPoint());

					}
				}
			}

			cartService.deleteCartPrdtCouponReset(ocCartBenefit);
			cartService.setCartPrdtCouponInsert(applyCpnList);

			// 적용 쿠폰은 cart seq 1개

			if (applyCpnList.size() > 0) {
				int applyDscntAmt = applyCpnList.get(0).getDscntAmt();

				OcCartBenefit applyParams = new OcCartBenefit();
				applyParams.setCartSeq(params.getCartSeq());
				applyParams.setDscntAmt(applyDscntAmt);
				applyCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(applyParams);
			}

		} else {

			if (UtilsText.equals(params.getApplyCouponFlag(), "Y")) { // 선택된 장바구니번호에 쿠폰이 있을 경우
				int applyDscntAmt = params.getDscntAmt();

				OcCartBenefit applyParams = new OcCartBenefit();
				applyParams.setCartSeq(params.getCartSeq());
				applyParams.setDscntAmt(applyDscntAmt);
				applyCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(applyParams);
			} else { // 선택된 장바구니번호에 쿠폰이 없을 경우
				if (appliedCpnList.size() > 0) {
					cartService.deleteCartPrdtCouponReset(ocCartBenefit);
				}
			}

		}

//		OcCartBenefit[] applyCouponList = params.getApplyCouponList();
//
//		List<OcCartBenefit> applyCpnList = Arrays.asList(applyCouponList); // 인입된 쿠폰 리스트
//
//		AtomicInteger counter = new AtomicInteger(0);
//
//		applyCpnList = applyCpnList.stream().map(cpn -> {
//			cpn.setMemberNo(params.getMemberNo()); // 회원번호 추가
//			cpn.setCartBenefitSeq(counter.incrementAndGet()); // 적용 순번 추가
//			return cpn;
//		}).collect(Collectors.toList());
//
//		List<OcCartBenefit> delCpnList = new ArrayList<>();
//
//		for (OcCartBenefit del : applyCpnList) {
//			OcCartBenefit delCpn = new OcCartBenefit();
//			// if (String.valueOf(del.getDelCartSeq()) != "") {
//			if (del.getDelCartSeq() != null) {
//				delCpn.setMemberNo(params.getMemberNo());
//				delCpn.setCartSeq(del.getDelCartSeq());
//				delCpn.setCpnYn(del.getCpnYn());
//				delCpn.setHoldCpnSeq(del.getDelHoldCpnSeq());
//				delCpn.setDelCartSeq(del.getDelCartSeq());
//				delCpn.setDelHoldCpnSeq(del.getDelHoldCpnSeq());
//				delCpn.setDelDcamt(del.getDelDcamt());
//				delCpn.setDelDscntAmt(del.getDelDscntAmt());
//				delCpn.setDscntAmt(del.getDelDscntAmt());
//				delCpnList.add(delCpn);
//			}
//		}
//
//		if (delCpnList.size() > 0) {
//			for (OcCartBenefit del : delCpnList) {
//
//				int delcnt = cartService.deleteCartPrdtCouponReset(del);
//
//				// 삭제된 쿠폰 정보 금액 조회
//				OcCartBenefit delCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(del);
//				if (delCpnInfo != null) {
//					del.setNomalCpnNo(delCpnInfo.getNomalCpnNo());
//					del.setNomalHoldCpnSeq(delCpnInfo.getNomalHoldCpnSeq());
//					del.setNomalCpnApplyDcAmt(delCpnInfo.getNomalCpnApplyDcAmt());
//					del.setPlusCpnNo(delCpnInfo.getPlusCpnNo());
//					del.setPlusHoldCpnSeq(delCpnInfo.getPlusHoldCpnSeq());
//					del.setPlusCpnApplyDcAmt(delCpnInfo.getPlusCpnApplyDcAmt());
//
//					del.setCpnApplyTotSellAmt(delCpnInfo.getCpnApplyTotSellAmt());
//					del.setCpnApplyTotPoint(delCpnInfo.getCpnApplyTotPoint());
//
//				} else {
//
//					// del.setCpnApplyTotSellAmt();
//					// del.setCpnApplyTotPoint(delCpnInfo.getCpnApplyTotPoint());
//				}
//
//			}
//		}
//
////		System.out.println("delCpnList >>>> after " + delCpnList);
//		for (OcCartBenefit del : delCpnList) {
//
//		}
//
//		// 적용 쿠폰 삭제 처리 --> 들어온 순서대로 등록 처리
//		System.out.println("params.getApplyCouponFlag() >>>>>>>>>>>>>" + params.getApplyCouponFlag());
//
//		OcCartBenefit ocCartBenefit = new OcCartBenefit();
//		ocCartBenefit.setMemberNo(params.getMemberNo());
//		ocCartBenefit.setCartSeq(params.getCartSeq());
//
//		List<OcCartBenefit> appliedCpnList = ocCartBenefitDao.getCartCoupon(ocCartBenefit); // 기 적용된 쿠폰 목록
//
//		if (applyCpnList.size() < 1) {
//			if (appliedCpnList.size() > 0) {
//				// 해당 상품에 대한 쿠폰 전체 미적용 처리
//				if (UtilsText.equals(params.getApplyCouponFlag(), "N")) {
//					cartService.deleteCartPrdtCouponReset(ocCartBenefit);
//				}
//			}
//		} else {
//			cartService.deleteCartPrdtCouponReset(ocCartBenefit);
//			cartService.setCartPrdtCouponInsert(applyCpnList);
//		}
//
//		// 적용 쿠폰은 cart seq 1개
//		OcCartBenefit applyCpnInfo = new OcCartBenefit();
//		if (applyCpnList.size() > 0) {
//			int applyDscntAmt = applyCpnList.get(0).getDscntAmt();
//
//			OcCartBenefit applyParams = new OcCartBenefit();
//			applyParams.setCartSeq(params.getCartSeq());
//			applyParams.setDscntAmt(applyDscntAmt);
//			applyCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(applyParams);
//		}
//
//		//
//		if (UtilsText.equals(params.getApplyCouponFlag(), "Y")) {
//			int applyDscntAmt = params.getDscntAmt();
//
//			OcCartBenefit applyParams = new OcCartBenefit();
//			applyParams.setCartSeq(params.getCartSeq());
//			applyParams.setDscntAmt(applyDscntAmt);
//			applyCpnInfo = ocCartBenefitDao.getCartCouponPrdtInfo(applyParams);
//		}

		resultMap.put("applyCpnInfo", applyCpnInfo); // 적용 쿠폰 정보
		// 삭제 대상 cart seq N
		resultMap.put("delCpnList", delCpnList); // 적용 쿠폰 정보

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartPrdtCouponInsert
	 * @Date : 2019. 6. 7.
	 * @Author : flychani@3top.co.kr
	 * @param applyCpnList
	 * @return
	 */
	public int setCartPrdtCouponInsert(List<OcCartBenefit> applyCpnList) throws Exception {
		int cnt = 0;

		OcCartBenefit ocCartBenefit = new OcCartBenefit();

		for (OcCartBenefit applyCpn : applyCpnList) {
			cnt = ocCartBenefitDao.setCartPrdtCouponInsert(applyCpn);
		}

		return cnt;
	}

	/**
	 * @Desc :
	 * @Method Name : deleteCartPrdtCouponReset
	 * @Date : 2019. 6. 7.
	 * @Author : flychani@3top.co.kr
	 * @param ocCartBenefit
	 * @return
	 */
	public int deleteCartPrdtCouponReset(OcCartBenefit ocCartBenefit) throws Exception {
		int cnt = 0;

		// System.out.println("ocCartBenefit >>>>>>>>>>>>>" + ocCartBenefit);
		cnt = ocCartBenefitDao.deleteCartPrdtCouponReset(ocCartBenefit);

		return cnt;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartCounting
	 * @Date : 2019. 6. 13.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCartCounting(OcCart ocCart) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		boolean cart_check = false; //
		boolean isLogin = ocCart.isLogin(); // 로그인 여부

		if (!isLogin) {
			ocCart.setMemberNo(Const.NON_MEMBER_NO);
		}

		ocCart.setCartType(Const.CART_TYPE_NOMAL);

		int cartCnt = ocCartDao.getCartCounting(ocCart);

		resultMap.put("cartCnt", cartCnt);
		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : setCartMerge
	 * @Date : 2019. 6. 14.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public void setCartMerge(OcCart ocCart) throws Exception {
		boolean cart_check = false; //
		boolean isLogin = ocCart.isLogin(); // 로그인 여부

		List<OcCart> totalCartList = new ArrayList<>();

		ocCart.setCartType(Const.CART_TYPE_NOMAL);

		/**
		 * 1. 장바구니 사은품 , 배송비 , 행사 초기화
		 */
		int memCartCnt = cartService.setOcCartResetForMember(ocCart); // 회원 장바구니 장바구니 사은품 , 배송비 초기화
		int nonMemCartCnt = cartService.setOcCartResetForNonMember(ocCart); // 비회원 장바구니 장바구니 사은품 , 배송비 초기화

		if (isLogin) {// 비회원 로그인시 처리 필요 //
			// 회원
			List<OcCart> memberCartList = cartService.getMemberCartList(ocCart); // 회원 장바구니 상품
			List<OcCart> nonMemberCartList = cartService.getNonMemberCartList(ocCart);// 비회원 장바구니 상품

			if (nonMemberCartList.size() > 0) { // 비회원 장바구니 상품이 있을 경우

				// 동일 상품 & 옵션 상품 조회 list
				List<OcCart> equalsList = memberCartList.stream()
						.filter(mem -> nonMemberCartList.stream()
								.anyMatch(non -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				// 회원 기준 동일 상품 & 옵션 상품 제외 상품 조회 list
				List<OcCart> memExcludeList = memberCartList.stream()
						.filter(mem -> nonMemberCartList.stream()
								.noneMatch(non -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				// 비회원 기준 동일 상품 & 옵션 상품 제외 상품 조회 list
				List<OcCart> nonMemExcludeList = nonMemberCartList.stream()
						.filter(non -> memberCartList.stream()
								.noneMatch(mem -> non.getPrdtNo().equals(mem.getPrdtNo())
										&& non.getPrdtOptnNo().equals(mem.getPrdtOptnNo())))
						.collect(Collectors.toList());

				/**
				 * 동일상품 존재시 회원기준 장바구니에 현재 날짜로 update
				 * 
				 */
				cart_check = cartService.setCartPrdtMergeUpdate(equalsList);

				/**
				 * 동일 상품 비회원 장바구니 삭제 처리시 sessionid추가 처리 equalsList sessionid 기준 동일상품 삭제 처리
				 */
				equalsList = equalsList.stream().map(prdt -> {
					prdt.setSessionId(ocCart.getSessionId()); // session id 추가
					prdt.setNonMemberNo(Const.NON_MEMBER_NO); // 비회원 회원번호 추가
					return prdt;
				}).collect(Collectors.toList());

				equalsList.forEach(i -> {
					log.info(" session id add   cartSeq > " + i.getCartSeq() + " anyMatch  getPrdtNo > " + i.getPrdtNo()
							+ " anyMatch getPrdtOptnNo > " + i.getPrdtOptnNo() + " anyMatch session > "
							+ i.getSessionId());
				});
				cart_check = cartService.setCartPrdtMergeDelete(equalsList);

				/**
				 * 2. session id 기준 추가 상품 회원 장바구니에 담기 처리 update 비회원 장바구니에 회원번호 추가하여 update
				 */

				nonMemExcludeList = nonMemExcludeList.stream().peek(non -> non.setMemberNo(ocCart.getMemberNo()))
						.collect(Collectors.toList());

				nonMemExcludeList.forEach(i -> {
					log.info(" nonMemExcludeList memberNo add  cartSeq > " + i.getCartSeq() + " noneMatch  getPrdtNo > "
							+ i.getPrdtNo() + " noneMatch getPrdtOptnNo > " + i.getPrdtOptnNo() + " add getMemberNo > "
							+ i.getMemberNo());
				});
				cart_check = cartService.setCartPrdtAddUpdate(nonMemExcludeList);

				totalCartList.addAll(equalsList); //
				totalCartList.addAll(memExcludeList);
				totalCartList.addAll(nonMemExcludeList);
			} else { // 비회원 장바구니 상품이 없을 경우
				// 회원 장바구니
				totalCartList.addAll(memberCartList); //
			}

		} else {
			// 비회원 장바구니
			List<OcCart> nonMemberCartList = cartService.getNonMemberCartList(ocCart);// 비회원 장바구니 상품
			totalCartList.addAll(nonMemberCartList);//
		}
	}

	/**
	 * @Desc :
	 * @Method Name : getRegiPrdt
	 * @Date : 2019. 6. 14.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public Map<String, Object> getWishPrdtList(OcCartInfo params) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
//		System.out.println("parameter>>>>>>>>>>>>>>>>>" + params);
//		System.out.println("parameter>>>>>>>>>>>>>>>>>" + params.getMemberNo());
//		System.out.println("parameter>>>>>>>>>>>>>>>>>" + params.getSearchType());

		List<OcCartInfo> prdtList = new ArrayList<>();
		// 내가 찜한 상품
		prdtList = memberInterestProductService.getWishProdutListTopFive(params);

//		System.out.println("prdtList" + prdtList);
		resultMap.put("wishPrdtList", prdtList);
		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getPrdtOptionInfo
	 * @Date : 2019. 6. 26.
	 * @Author : flychani@3top.co.kr
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> getPrdtOptionInfo(Parameter<?> parameter) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 상품 옵션 목록 조회
		String prdtNo = parameter.getString("prdtNo");
		String mmnyPrdtYn = parameter.getString("mmnyPrdtYn");
		String prdtOptnNo = parameter.getString("prdtOptnNo");

		PdProductOption ppo = new PdProductOption();
		ppo.setPrdtNo(prdtNo);
		ppo.setPrdtOptnNo(prdtOptnNo);

		PdProductOption optnInfo = pdProductOptionDao.selectByPrimaryKey(ppo);

		System.out.println("optnInfo >>>> " + optnInfo);

		List<PdProductOptionWithStockAndPrice> prdtOptList = productOptionService
				.getProductOptionListWithStockAndPrice(prdtNo);

		for (PdProductOptionWithStockAndPrice optn : prdtOptList) {
			int stockTotQTy = 0;
			if (UtilsText.equals(optn.getSellStatCode(), "10001")) {
				if (UtilsText.equals(mmnyPrdtYn, "Y")) {
					optn.setStockTotQty(optn.getStockAiQty() + optn.getStockAsQty() + optn.getStockAwQty());
				} else {
					optn.setStockTotQty(optn.getStockVdQty());
				}
			} else {
				optn.setStockTotQty(0);
			}
		}

		long sizeOptn = prdtOptList.stream().filter(p -> !UtilsText.isEmpty(p.getOptnName())).count(); // 사이즈
		long addOptn = prdtOptList.stream().filter(p -> !UtilsText.isEmpty(p.getAddOptn2Text())).count(); // 추가옵션

		ObjectMapper mapper = new ObjectMapper();
		String jsonPrdtList = "";

		jsonPrdtList = mapper.writeValueAsString(prdtOptList);

		List<PdProductOptionWithStockAndPrice> distinctOptnList = new ArrayList<>(); // 중복 제거 list
		List<PdProductOptionWithStockAndPrice> copyOptnList = new ArrayList<>(); // 옵션 정보 copy list
		List<PdProductOptionWithStockAndPrice> copyAddOptnList = new ArrayList<>(); // 옵션 하위 정보 copy list
		List<PdProductOptionWithStockAndPrice> OptnList = new ArrayList<>(); // 담을 변수

		if (addOptn > 0) {
			copyOptnList.addAll(prdtOptList);
			copyAddOptnList.addAll(prdtOptList);

			// 중복 옵션명만 노출 처리
			Map seen = new ConcurrentHashMap<>();
			distinctOptnList = (List) prdtOptList.stream()
					.filter(x -> seen.putIfAbsent(x.getOptnName(), Boolean.TRUE) == null).collect(Collectors.toList());

			// 중복 옵션에 대한 하위 옵션 총 수량
			Map<String, IntSummaryStatistics> optnSummary = copyOptnList.stream().filter(x -> x != null)
					.collect(Collectors.groupingBy(PdProductOptionWithStockAndPrice::getOptnName,
							Collectors.summarizingInt(PdProductOptionWithStockAndPrice::getStockTotQty)));

			for (PdProductOptionWithStockAndPrice distinctOptn : distinctOptnList) {
				for (Entry<String, IntSummaryStatistics> data : optnSummary.entrySet()) {
					if (UtilsText.equals(data.getKey(), distinctOptn.getOptnName())) {
						PdProductOptionWithStockAndPrice option = new PdProductOptionWithStockAndPrice();
						option.setPrdtNo(distinctOptn.getPrdtNo());
						option.setPrdtOptnNo(distinctOptn.getPrdtOptnNo());
						option.setOptnName(distinctOptn.getOptnName());
						option.setOptnAddAmt(distinctOptn.getOptnAddAmt());
						// aa.set 금액
						option.setStockTotQty((int) data.getValue().getSum());
						// setCartCpnSelected(true);

						if (UtilsText.equals(distinctOptn.getOptnName(), optnInfo.getOptnName())) {
							option.setOptnSelected(true);
						} else {
							option.setOptnSelected(false);
						}

						OptnList.add(option);
					}
				}
			}

			// 추가 옵션 --> 상품 옵션명으로 하우 옵션정보 조회
			copyAddOptnList = copyAddOptnList.stream()
					.filter(p -> UtilsText.equals(p.getOptnName(), optnInfo.getOptnName()))
					.collect(Collectors.toList());

			for (PdProductOptionWithStockAndPrice addOptnInfo : copyAddOptnList) {
				if (UtilsText.equals(addOptnInfo.getPrdtOptnNo(), optnInfo.getPrdtOptnNo())) {
					addOptnInfo.setOptnSelected(true);
				} else {
					addOptnInfo.setOptnSelected(false);
				}
			}

			System.out.println("OptnList  >>>> " + OptnList);
			System.out.println("copyAddOptnList 추가 옵션 >>>> " + copyAddOptnList);
		} else {
			for (PdProductOptionWithStockAndPrice prdtOpt : prdtOptList) {
				if (UtilsText.equals(prdtOpt.getPrdtOptnNo(), optnInfo.getPrdtOptnNo())) {
					prdtOpt.setOptnSelected(true);
				} else {
					prdtOpt.setOptnSelected(false);
				}
			}
		}

//		System.out.println("OptnList " + OptnList);
		resultMap.put("prdtOptList", prdtOptList); // 상품 옵션 리스트
		resultMap.put("OptnList", OptnList); // 중복 제거 옵션 리스트
		resultMap.put("addOptnList", copyAddOptnList); // 중복 제거 옵션 리스트
		resultMap.put("jsonPrdtList", jsonPrdtList); // 상품 옵션 리스트
		resultMap.put("sizeOptn", sizeOptn); // 사이즈
		resultMap.put("addOptn", addOptn); // 추가 옵션
		return resultMap;
	}

	/**
	 * @Desc :
	 * @Method Name : getPrdtOptionAdd
	 * @Date : 2019. 6. 27.
	 * @Author : flychani@3top.co.kr
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> getPrdtOptionAdd(Parameter<?> parameter) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 상품 옵션 목록 조회
		String prdtNo = parameter.getString("prdtNo");
		String mmnyPrdtYn = parameter.getString("mmnyPrdtYn");
		String selectVal = parameter.getString("selectVal");

		List<PdProductOptionWithStockAndPrice> prdtOptList = productOptionService
				.getProductOptionListWithStockAndPrice(prdtNo);

		for (PdProductOptionWithStockAndPrice optn : prdtOptList) {
			int stockTotQTy = 0;
			if (UtilsText.equals(optn.getSellStatCode(), "10001")) {
				if (UtilsText.equals(mmnyPrdtYn, "Y")) {
					optn.setStockTotQty(optn.getStockAiQty() + optn.getStockAsQty() + optn.getStockAwQty());
				} else {
					optn.setStockTotQty(optn.getStockVdQty());
				}
			} else {
				optn.setStockTotQty(0);
			}
		}

		prdtOptList = prdtOptList.stream().filter(p -> UtilsText.equals(p.getOptnName(), selectVal))
				.collect(Collectors.toList());

		resultMap.put("addOption", prdtOptList);

		return resultMap;
	}

	/*************************************************************************************************
	 * jeon end
	 *************************************************************************************************/

	/*************************************************************************************************
	 * ljyoung start
	 *************************************************************************************************/
	/**
	 * @Desc : 주문을 위해 선택된 장바구니 정보를 가져온다.
	 * @Method Name : getCartList
	 * @Date : 2019. 4. 11.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderVo
	 * @return
	 * @throws Exception
	 */
	public List<OcCart> selectCartList(OrderCartVo orderVo) throws Exception {
		return ocCartDao.selectCartList(orderVo);
	}
	/*************************************************************************************************
	 * ljyoung end
	 *************************************************************************************************/

}
