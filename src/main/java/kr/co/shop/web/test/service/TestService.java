package kr.co.shop.web.test.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.cart.repository.master.OcCartDao;
import kr.co.shop.web.cart.service.CartService;
import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.service.DisplayCategoryService;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.test.repository.master.TestDao;
import kr.co.shop.web.test.vo.CartPrdtSearchVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestService {
	@Autowired
	private CartService cartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private TestDao testDao;

	@Autowired
	OcCartDao ocCartDao;

	@Autowired
	DisplayCategoryService displayPageService;

	public Map<String, Object> getProductDCInfo1(OcCart ocCart) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		ocCart.setCartType(Const.CART_TYPE_NOMAL);
		List<OcCart> memberCartList = cartService.getMemberCartList(ocCart);

		List<PdProductWithAll> prdtInfoList = productService.getProductListWithAll(memberCartList);

		resultMap.put("cartList", prdtInfoList);

		return resultMap;
	}

	public Map<String, Object> getProductDCInfo(OcCart ocCart) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		ocCart.setCartType(Const.CART_TYPE_NOMAL);
		List<OcCart> memberCartList = cartService.getMemberCartList(ocCart);

		CartPrdtSearchVO cartPrdSearchVo = new CartPrdtSearchVO();

		cartPrdSearchVo.setDeviceCode(ocCart.getDeviceCode());
		cartPrdSearchVo.setAffltsCode("");
		cartPrdSearchVo.setEmpYn(ocCart.getEmpYn());
		cartPrdSearchVo.setMemberTypeCode(ocCart.getMemberTypeCode());
		cartPrdSearchVo.setMbshpGradeCode(ocCart.getMbshpGradeCode());
		cartPrdSearchVo.setCartPrdtList(memberCartList);

		List<PdProductWithAll> prdtInfoList = this.getProductListWithAll(cartPrdSearchVo);

		// List<PdProductWithAll> prdtInfoList = this.getProductInfo(memberCartList);

		// testDao.selectProductList(memberCartList);

		// for()

		resultMap.put("cartList", prdtInfoList);

		return resultMap;
	}

	public List<PdProductWithAll> getProductListWithAll(CartPrdtSearchVO cartPrdSearchVo) throws Exception {

		List<PdProductWithAll> pdProductWithAll = testDao.selectProductList1(cartPrdSearchVo);

		List<PrPromotion> prdtInfoList1 = testDao.selectPromotionDscntList1(cartPrdSearchVo);

		List<PdProductWithAll> productList = new ArrayList<PdProductWithAll>();

		for (PdProductWithAll prdt : pdProductWithAll) {

			// 재고통합여부 확인 'Y'이면서 AI,AW,AS 재고 없으면 서비스 호출
			if ("Y".equals(prdt.getStockIntgrYn()) && prdt.getStockAiQty() < 1 && prdt.getStockAwQty() < 1
					&& prdt.getStockAsQty() < 1) {

			}

			Long cartSeq = prdt.getCartSeq();
			Integer normalAmt = prdt.getNormalAmt();
			Integer sellAmt = prdt.getSellAmt();
			Integer optionAddAmt = prdt.getOptnAddAmt();
			Integer orderQty = prdt.getOrderQty();
			Integer dscntAmt = 0;

			normalAmt += optionAddAmt;
			sellAmt += optionAddAmt;

			List<PrPromotion> tempList = new ArrayList<PrPromotion>();
			PrPromotion prPromotion = new PrPromotion();
			Integer dcAmt = 0;
			PrPromotion giftPromotion = new PrPromotion();
			for (PrPromotion promoPrd : prdtInfoList1) {

				if (cartSeq == promoPrd.getCartSeq()) {

					if (promoPrd.getPromoTypeCode().equals("10005")) { // 사은품은 무조건 프로모션 정보에 담는다.
						giftPromotion = promoPrd;
					} else { // 사은품이 아님 프로모션인 경우
						prPromotion = promoPrd;
						if (promoPrd.getImdtlDscntType().equals("R")) { // 정률
							dcAmt = (int) Math.ceil((double) normalAmt * promoPrd.getDscntRate() / 10000) * 100;
							dscntAmt = normalAmt - dcAmt;

						} else if (promoPrd.getImdtlDscntType().equals("A")) { // 정액
							dscntAmt = (int) Math.ceil((double) (normalAmt - promoPrd.getDscntRate()) / 100) * 100;

						} else if (promoPrd.getImdtlDscntType().equals("U")) { // 균일가
							dscntAmt = promoPrd.getDscntRate();
						}

						if (dscntAmt < sellAmt) {
							prPromotion.setDscntAmt(dscntAmt);
							tempList.add(prPromotion);
						}
					}
				}
			}

			if (!tempList.isEmpty() && tempList.size() > 0) {
				Collections.sort(tempList, new sortPromotionDscntAmt());
				prPromotion = tempList.get(0);
			}

			List<PrPromotion> promoList = new ArrayList<PrPromotion>();

			if (giftPromotion.getCartSeq() != null) {
				promoList.add(giftPromotion);
			}
			if (prPromotion.getCartSeq() != null) {
				promoList.add(prPromotion);

				prdt.setDscntAmt(prPromotion.getDscntAmt());
			} else {
				prdt.setDscntAmt(prdt.getSellAmt());
			}

			double dscntRate = Math.ceil((((double) prdt.getNormalAmt()
					- (double) (prdt.getDscntAmt() != null ? prdt.getDscntAmt() : prdt.getSellAmt()))
					/ (double) prdt.getNormalAmt()) * 1000.0) / 10.0;

//			log.debug("cartSeq : [{}], NormalAmt : [{}], DscntAmt :[{}], dscntRate : [{}]", prdt.getCartSeq(),
//					prdt.getNormalAmt() != null ? prdt.getNormalAmt() : 0,
//					prdt.getDscntAmt() != null ? prdt.getDscntAmt() : 0, dscntRate);
			prdt.setDscntRate(dscntRate);

			PrPromotion[] promotion = new PrPromotion[promoList.size()];

//			System.out.println(prdt.getCartSeq() + "-" + promoList.size());
			Integer idx = 0;
			for (PrPromotion prmo : promoList) {
				promotion[idx++] = prmo;
			}

			prdt.setPromotion(promotion);

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
			int rtnValue = 0;

			if (args0.getDscntAmt() > args1.getDscntAmt()) {
				rtnValue = 1;
			} else if (args0.getDscntAmt() < args1.getDscntAmt()) {
				rtnValue = -1;
			} else {
				rtnValue = 0;
			}

			return rtnValue;
		}
	}

	public List<DpCategory> getDpCategoryGnb(DpCategory dpCategory) throws Exception {

		// 1depth 조회
		dpCategory.setCtgrLevel((short) 1);
		List<DpCategory> list = this.getDpCategoryList(dpCategory);

		for (DpCategory dc : list) {

			if (UtilsText.equals(dc.getLeafCtgrYn(), "N")) {
				dpCategory.setCtgrLevel((short) 2);
				dpCategory.setUpCtgrNo(dc.getCtgrNo());
				List<DpCategory> subList = this.getDpCategoryList(dpCategory);

				for (DpCategory subDc : subList) {
					if (UtilsText.equals(subDc.getLeafCtgrYn(), "N")) {
						dpCategory.setCtgrLevel((short) 3);
						dpCategory.setUpCtgrNo(subDc.getCtgrNo());
						List<DpCategory> subSubList = this.getDpCategoryList(dpCategory);
						subDc.setSubCategoryList(subSubList);
					}
				}

				dc.setSubCategoryList(subList);
			}
		}

		return list;
	}

//	@Cacheable(value = "displayCategoryService.getDpCategoryList", key = "#dpCategory.chnnlNo")
	public List<DpCategory> getDpCategoryList(DpCategory dpCategory) throws Exception {

		List<DpCategory> list = testDao.selectDpCategoryList(dpCategory);

		return list;
	}

	/*
	 * private List<PdProductWithAll> getProductInfo(List<OcCart> ocCartList) throws
	 * Exception {
	 *
	 * List<PdProductWithAll> pdProductWithAll =
	 * testDao.selectProductList(ocCartList); // 상품 리스트
	 *
	 * List<PrPromotion> prdtInfoList1 =
	 * testDao.selectPromotionDscntList(ocCartList);
	 *
	 * // System.out.println("=====================================");
	 *
	 * List<PdProductWithAll> productList = new ArrayList<PdProductWithAll>();
	 *
	 * for (PdProductWithAll prdt : pdProductWithAll) {
	 *
	 * // 재고통합여부 확인 'Y'이면서 AI,AW,AS 재고 없으면 서비스 호출 if
	 * ("Y".equals(prdt.getStockIntgrYn()) && prdt.getStockAiQty() < 1 &&
	 * prdt.getStockAwQty() < 1 && prdt.getStockAsQty() < 1) {
	 *
	 * }
	 *
	 * Long cartSeq = prdt.getCartSeq(); Integer normalAmt = prdt.getNormalAmt();
	 * Integer sellAmt = prdt.getSellAmt(); Integer optionAddAmt =
	 * prdt.getOptnAddAmt(); Integer orderQty = prdt.getOrderQty(); Integer dscntAmt
	 * = 0; // Integer // long prdt.g
	 *
	 * normalAmt += optionAddAmt; sellAmt += optionAddAmt;
	 *
	 * List<PrPromotion> tempList = new ArrayList<PrPromotion>(); PrPromotion
	 * prPromotion = new PrPromotion(); Integer dcAmt = 0; PrPromotion giftPromotion
	 * = new PrPromotion(); for (PrPromotion promoPrd : prdtInfoList1) {
	 *
	 * if (cartSeq == promoPrd.getCartSeq()) {
	 *
	 * prPromotion = promoPrd;
	 *
	 * if (promoPrd.getPromoTypeCode().equals("10005")) { // 사은품은 무조건 프로모션 정보에 담는다.
	 * giftPromotion = promoPrd; } else { // 사은품이 아님 프로모션인 경우 if
	 * (promoPrd.getImdtlDscntType().equals("R")) { // 정률 dcAmt = (int)
	 * Math.ceil((double) normalAmt * promoPrd.getDscntRate() / 10000) * 100;
	 * dscntAmt = normalAmt - dcAmt;
	 *
	 * } else if (promoPrd.getImdtlDscntType().equals("A")) { // 정액 dscntAmt = (int)
	 * Math.ceil((double) (normalAmt - promoPrd.getDscntRate()) / 100) * 100;
	 *
	 * } else if (promoPrd.getImdtlDscntType().equals("U")) { // 균일가 dscntAmt =
	 * promoPrd.getDscntRate(); }
	 *
	 * log.info("prdtNo : " + prdt.getPrdtNo()); log.info("dscntAmt : " + dscntAmt);
	 * log.info("sellAmt : " + sellAmt);
	 *
	 * if (dscntAmt < sellAmt) { // log.info("promo : " + promoPrd.getPromoNo() +
	 * promoPrd.getPromoName()); prdt.setDscntAmt(dscntAmt); prPromotion = promoPrd;
	 * } else { prdt.setDscntAmt(sellAmt); }
	 *
	 * }
	 *
	 * }
	 *
	 * }
	 *
	 * // log.debug("prPromotion {}", prPromotion);
	 *
	 * if (giftPromotion.getCartSeq() != null) { tempList.add(giftPromotion); } if
	 * (prPromotion.getCartSeq() != null) { tempList.add(prPromotion); Double
	 * dscntRate = Math.ceil((double) prdt.getSellAmt() / prdt.getNormalAmt() *
	 * 100); prdt.setDscntRate(dscntRate); }
	 *
	 * PrPromotion[] promotion = new PrPromotion[tempList.size()];
	 *
	 * Integer idx = 0; for (PrPromotion prmo : tempList) { promotion[idx++] = prmo;
	 * }
	 *
	 * prdt.setPromotion(promotion);
	 *
	 * productList.add(prdt);
	 *
	 * // log.debug(test.toString());
	 *
	 * }
	 *
	 * return productList;
	 *
	 * }
	 */
	// private void get
}
