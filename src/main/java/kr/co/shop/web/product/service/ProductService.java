package kr.co.shop.web.product.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.event.model.master.EvEventJoinMember;
import kr.co.shop.web.event.service.EventService;
import kr.co.shop.web.product.model.master.CartProductSearchVO;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductDetailWrapper;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.repository.master.PdProductDao;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.promotion.repository.master.PrPromotionDao;
import kr.co.shop.web.promotion.service.CouponService;
import kr.co.shop.web.promotion.service.PromotionService;
import kr.co.shop.web.system.service.CommonCodeService;
import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private PdProductDao productDao;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private EventService eventService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private PrPromotionDao promotionDao;

	/** 상품종류 - 드로우 */
	public static final String KIND_OF_PRODUCT_DRAW = "D";
	/** 상품종류 - 런칭예정 */
	public static final String KIND_OF_PRODUCT_LAUNCH = "L";
	/** 상품종류 - 예약 */
	public static final String KIND_OF_PRODUCT_BOOKED = "B";
	/** 상품종류 - 일반 */
	public static final String KIND_OF_PRODUCT_NORMAL = "N";

	public Map<String, Integer> getDisplayProductTotalCount(PageableProduct<?, PdProductWrapper> pageableProduct)
			throws Exception {
		return this.productDao.selectDisplayProductTotalCount(pageableProduct);
	}

	/**
	 * @Desc : 전시상품조회
	 * @Method Name : getDisplayProductList
	 * @Date : 2019. 5. 24.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public Page<PdProductWrapper> getDisplayProductList(PageableProduct<?, PdProductWrapper> pageableProduct)
			throws Exception {
		List<PdProductWrapper> productList = this.productDao.selectDisplayProductList(pageableProduct);
		pageableProduct.setContent(productList);
		return pageableProduct.getPage();
	}

	/**
	 * @Desc : 상품 상세정보 조회
	 * @Method Name : getDisplayProductDetail
	 * @Date : 2019. 5. 29.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public PdProductDetailWrapper getDisplayProductDetail(
			PageableProduct<PdProductDetailWrapper, PdProductDetailWrapper> pageableProduct) throws Exception {
		return this.productDao.selectDisplayProductDetail(pageableProduct);
	}

	/**
	 * @Desc : 상품 주요정보 조회
	 * @Method Name : getDisplayProduct
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public PdProductWrapper getDisplayProduct(PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct)
			throws Exception {
		PdProductWrapper result = this.productDao.selectDisplayProduct(pageableProduct);
		// 프로모션 정보 조회
		result.setPromotion(this.promotionService.getPromotionByPrdtNo(pageableProduct.getBean().getPrdtNo()));

		// 사은품프로모션정보 및 사은품정보 조회
		result.setProductGift(promotionService.getPromotionOfGiftByPrdtNo(result.getPrdtNo()));

		// 적용쿠폰 정보 조회
		result.setCoupon(this.couponService.getCouponByPrdtNo(pageableProduct.getBean().getPrdtNo()));

		switch (result.getProductKind()) {
		case KIND_OF_PRODUCT_DRAW:
			result.setDrawRemainTime(); // DRAW 상품인 경우, 판매대기시간 또는 판매완료대기시간 설정

			// 이벤트 참여가능정보 확인
			if (UtilsText.isNotBlank(pageableProduct.getMemberNo())) {
				EvEventJoinMember criteriaForEventJoinMember = new EvEventJoinMember();
				criteriaForEventJoinMember.setEventNo(result.getEventDraw().getEventNo());
				criteriaForEventJoinMember.setMemberNo(pageableProduct.getMemberNo());
				result.setEventDrawMessageCode(this.eventService.getDrawParticipationCheck(criteriaForEventJoinMember));
			}
			break;
		case KIND_OF_PRODUCT_LAUNCH:
			result.setSellRemainTime(); // 런칭예정상품인 경우, 판매대기시간 설정
			break;
		case KIND_OF_PRODUCT_BOOKED:
			break;
		case KIND_OF_PRODUCT_NORMAL:
			break;
		}

		// 배송비 조회
		if (UtilsText.equals(Const.BOOLEAN_FALSE, result.getFreeDlvyYn())) {
			if (UtilsText.isNotEmpty(result.getVndrNo())) {
				VdVendor criteriaVendor = new VdVendor();
				criteriaVendor.setVndrNo(result.getVndrNo());
				VdVendor vendor = this.vendorService.getVendorInfo(criteriaVendor);
				result.setDlvyAmt(vendor.getDlvyAmt()); // 배송비 설정
				result.setFreeDlvyStdrAmt(vendor.getFreeDlvyStdrAmt()); // 무료배송기준금액 설정
			}
		} else {
			result.setDlvyAmt(0);
		}

		return result;
	}

	/**
	 * @Desc : 상품 상세 접근점검정보 조회
	 * @Method Name : getDisplayProductAccessCheckInfo
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public PdProductWrapper getDisplayProductAccessCheckInfo(
			PageableProduct<PdProduct, PdProductWrapper> pageableProduct) throws Exception {
		return this.productDao.selectDisplayProductAccessCheckInfo(pageableProduct);
	}

	/**
	 * @Desc : 연계상품 조회
	 * @Method Name : getDisplayRelatedProduct
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public Page<PdProductWrapper> getDisplayRelatedProduct(
			PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct) throws Exception {
		List<PdProductWrapper> productList = this.productDao.selectDisplayRelatedProduct(pageableProduct);
		pageableProduct.setContent(productList);
		return pageableProduct.getPage();
	}

	/**
	 * @Desc : 상품 상세 정보 조회 (현재는 상품 마스터만)
	 * @Method Name : getProduct
	 * @Date : 2019. 5. 20.
	 * @Author : hsjhsj19
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public PdProduct getProduct(String prdtNo) throws Exception {
		PdProduct product = new PdProduct();
		product.setPrdtNo(prdtNo);
		return this.productDao.selectByPrimaryKey(product);
	}

	/**
	 * @Desc : 장바구니 상품 조회
	 * @Method Name : getProductDCInfo
	 * @Date : 2019. 5. 30.
	 * @Author : hsjhsj19
	 * @param ocCart
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWithAll> getProductListWithAll(List<OcCart> carts) throws Exception {

		if (carts.isEmpty()) {
			return null;
		}

		OcCart ocCart = carts.get(0);
		CartProductSearchVO cartPrdSearchVo = new CartProductSearchVO();
		// 디바이스 코드
		cartPrdSearchVo.setDeviceCode(ocCart.getDeviceCode());
		// 제휴사코드
		cartPrdSearchVo.setAffltsCode("");
		// 임직원 여부
		cartPrdSearchVo.setEmpYn(ocCart.getEmpYn());
		// 회원 구분 코드
		cartPrdSearchVo
				.setMemberTypeCode(TextUtils.isEmpty(ocCart.getMemberTypeCode()) ? CommonCode.MEMBER_TYPE_CODE_NONMEMBER
						: ocCart.getMemberTypeCode());
		// 회원 등급 코드
		cartPrdSearchVo
				.setMbshpGradeCode(TextUtils.isEmpty(ocCart.getMbshpGradeCode()) ? CommonCode.MBSHP_GRADE_CODE_NORMAL
						: ocCart.getMbshpGradeCode());
		// 픽업가능여부
		cartPrdSearchVo.setStorePickupPsbltYn(ocCart.getStorePickupPsbltYn());
		// 상품코드 목록
		cartPrdSearchVo.setCartPrdtList(carts);

		return this.getProductListWithAll(cartPrdSearchVo);

	}

	public List<PdProductWithAll> getProductListWithAll(CartProductSearchVO cartPrdSearchVo) throws Exception {

		List<PdProductWithAll> productList = new ArrayList<PdProductWithAll>();

		if (cartPrdSearchVo == null || cartPrdSearchVo.getCartPrdtList().isEmpty()) {
			throw new Exception("잘못된 접근입니다.");
		}

		List<PdProductWithAll> pdProductWithAll = productDao.selectProductWithAllList(cartPrdSearchVo);

		// 픽업 가능 여부 상품일 경우는 해당 상품만 장바구니 상품 리스트로 변경 처리
		if (!UtilsText.isEmpty(cartPrdSearchVo.getStorePickupPsbltYn())
				&& Const.BOOLEAN_TRUE.equals(cartPrdSearchVo.getStorePickupPsbltYn())) {
			List<OcCart> cartPrdtList = new ArrayList<OcCart>();

			for (PdProductWithAll prdt : pdProductWithAll) {
				OcCart cartPrdt = new OcCart();

				cartPrdt.setCartSeq(prdt.getCartSeq());
				cartPrdt.setSiteNo(prdt.getSiteNo());
				cartPrdt.setChnnlNo(prdt.getChnnlNo());
				cartPrdt.setPrdtNo(prdt.getPrdtNo());
				cartPrdt.setPrdtOptnNo(prdt.getPrdtOptnNo());
				cartPrdt.setOrderQty(prdt.getOrderQty());
				cartPrdt.setVndrNo(prdt.getVndrNo());

				cartPrdtList.add(cartPrdt);
			}
			cartPrdSearchVo.setCartPrdtList(cartPrdtList);
		}
		List<PrPromotion> promotionList = promotionDao.selectPromotiontWithAllList(cartPrdSearchVo);

		for (PdProductWithAll prdt : pdProductWithAll) {
			// 재고통합여부 확인 'Y'이면서 AI,AW,AS 재고 없으면 서비스 호출
			if (Const.BOOLEAN_TRUE.equals(prdt.getStockIntgrYn()) && prdt.getStockAiQty() < 1
					&& prdt.getStockAwQty() < 1) {

			}

			// 장바구니번호
			Long cartSeq = prdt.getCartSeq();
			// 옵션추가금액
			Integer optionAddAmt = prdt.getOptnAddAmt();
			// 정상가
			Integer normalAmt = prdt.getNormalAmt() + optionAddAmt;
			prdt.setNormalAmt(normalAmt);
			// 판매가
			Integer sellAmt = prdt.getSellAmt() + optionAddAmt;
			prdt.setSellAmt(sellAmt);
			// 주문수량
			Integer orderQty = prdt.getOrderQty();
			// 최저가
			Integer dscntAmt = 0;
			// 할인금액
			Integer dcAmt = 0;
			// 정상가 - 판매가 * 수량
			prdt.setPrdtDscntAmt((normalAmt - sellAmt) * orderQty);

			// 프로모션
			PrPromotion prPromotion = new PrPromotion();
			// 프로모션 사은품
			List<PrPromotion> giftList = new ArrayList<PrPromotion>();
			// 최저가 비교 프로모션 목록
			List<PrPromotion> tempList = new ArrayList<PrPromotion>();
			// 담길 프로모션 목록
			List<PrPromotion> promoList = new ArrayList<PrPromotion>();

			// 2019-06-21 추가사항
			// 회원이 임직원인 경우
			if (Const.BOOLEAN_TRUE.equals(cartPrdSearchVo.getEmpYn())) {
				dcAmt = (int) Math.ceil((double) normalAmt * prdt.getEmpDscntRate() / 10000) * 100;
				dscntAmt = normalAmt - dcAmt;
				prdt.setDscntAmt(dscntAmt); // 최저가
				prdt.setTotalSellAmt(dscntAmt * orderQty); // 총 판매가
				prdt.setDscntRate(prdt.getEmpDscntRate()); // 할인률

				// 사은품 찾기 반복
				for (PrPromotion promo : promotionList) {
					if (cartSeq.compareTo(promo.getCartSeq()) == 0) {
						// 사은품은 무조건 프로모션 정보에 담는다.
						if (promo.getPromoTypeCode().equals(CommonCode.PROMO_TYPE_CODE_GIFT_PAYMENT)) {
							giftList.add(promo);
						}
					}
				}
				prdt.setPromotion(giftList.toArray(new PrPromotion[giftList.size()]));
				productList.add(prdt);
				continue;
			}

			// 최저가 찾기 반복
			for (PrPromotion promo : promotionList) {
				if (cartSeq.compareTo(promo.getCartSeq()) == 0) {
					// 사은품은 무조건 프로모션 정보에 담는다.
					if (promo.getPromoTypeCode().equals(CommonCode.PROMO_TYPE_CODE_GIFT_PAYMENT)) {
						giftList.add(promo);
					} else { // 사은품이 아닌 프로모션인 경우
						prPromotion = promo;
						if (promo.getImdtlDscntType().equals(CommonCode.DSCNT_TYPE_RATE)) { // 정률
							dcAmt = (int) Math.ceil((double) normalAmt * promo.getDscntRate() / 10000) * 100;
							dscntAmt = normalAmt - dcAmt;
						} else if (promo.getImdtlDscntType().equals(CommonCode.DSCNT_TYPE_AMT)) { // 정액
							dcAmt = promo.getDscntRate();
							dscntAmt = normalAmt - dcAmt;
						} else { // 균일가
							prdt.setPrdtDscntAmt(0);
							dcAmt = promo.getDscntRate();
							dscntAmt = promo.getDscntRate();
						}
						log.debug(
								"cartSeq : {}, sellAmt : [{}], dscntAmt : [{}], promoNo :[{}], imdtlDscntType : [{}], getDscntRate : [{}]",
								prdt.getCartSeq(), sellAmt, dscntAmt, promo.getPromoNo(), promo.getImdtlDscntType(),
								promo.getDscntRate());
						if (sellAmt.compareTo(dscntAmt) > 0) {
							log.debug("========================= cartSeq : {} ================================",
									prdt.getCartSeq());
							prPromotion.setDscntAmt(dscntAmt);
							prPromotion.setDcAmt(dcAmt);
							tempList.add(prPromotion);
						}
					}
				}
			}

			if (!tempList.isEmpty() && tempList.size() > 0) {
				Collections.sort(tempList, new sortPromotionDscntAmt());
				prPromotion = tempList.get(0);
			} else {
				prPromotion = new PrPromotion();
			}

			for (PrPromotion giftPromotion : giftList) {
				if (giftPromotion.getCartSeq() == null) {
					promoList.add(giftPromotion);
				}
			}

			if (prPromotion.getCartSeq() != null) {
				promoList.add(prPromotion);
				dscntAmt = prPromotion.getDscntAmt();
				prdt.setDscntAmt(dscntAmt);
			} else {
				dscntAmt = sellAmt;
				prdt.setDscntAmt(sellAmt);
			}

			// 총 판매가
			prdt.setTotalSellAmt(dscntAmt * orderQty);

			// 할인률
			double dscntRate = Math
					.ceil((((double) normalAmt - (double) (dscntAmt != null ? dscntAmt : sellAmt)) / (double) normalAmt)
							* 1000.0)
					/ 10.0;
			prdt.setDscntRate(dscntRate);

//			log.error(" cartSeq : {}, prdt.getDscntAmt() : {}, ImdtlDscntType : {}", prdt.getCartSeq(),
//					prdt.getDscntAmt(), prPromotion.getImdtlDscntType());

//			log.debug("cartSeq : [{}], NormalAmt : [{}], DscntAmt :[{}], dscntRate : [{}]", prdt.getCartSeq(),
//					prdt.getNormalAmt() != null ? prdt.getNormalAmt() : 0,
//					prdt.getDscntAmt() != null ? prdt.getDscntAmt() : 0, dscntRate);

//			System.out.println(prdt.getCartSeq() + "-" + promoList.size());

			prdt.setPromotion(promoList.toArray(new PrPromotion[promoList.size()]));
			productList.add(prdt);

		}

		return productList;
	}

	/**
	 * @Desc : 상품에 걸린 프로모션 할인 금액으로 정렬
	 * @FileName : TestService.java
	 * @Project : shop.backend
	 * @Date : 2019. 5. 30.
	 * @Author : kiowa
	 */
	static class sortPromotionDscntAmt implements Comparator<PrPromotion> {
		@Override
		public int compare(PrPromotion args0, PrPromotion args1) {
			return args0.getDscntAmt().compareTo(args1.getDscntAmt());
		}
	}

}
