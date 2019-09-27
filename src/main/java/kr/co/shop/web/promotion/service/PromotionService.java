package kr.co.shop.web.promotion.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.util.UtilsObject;
import kr.co.shop.constant.Const;
import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductOptionPriceHistory;
import kr.co.shop.web.product.model.master.PdProductPriceHistory;
import kr.co.shop.web.product.model.master.PdProductPromotion;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.repository.master.PdProductOptionPriceHistoryDao;
import kr.co.shop.web.product.repository.master.PdProductPriceHistoryDao;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.model.master.PrPromotion;
import kr.co.shop.web.promotion.repository.master.PrCouponDao;
import kr.co.shop.web.promotion.repository.master.PrPromotionDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PromotionService {

	@Autowired
	private PrPromotionDao promotionDao;

	@Autowired
	private PrCouponDao couponDao;

	@Autowired
	private PdProductPriceHistoryDao productPriceHistoryDao;

	@Autowired
	private PdProductOptionPriceHistoryDao productOptionPriceHistoryDao;

	/**
	 * @Desc : 상품 프로모션 목록 조회
	 * @Method Name : getProductPromotionInfoList
	 * @Date : 2019. 4. 30.
	 * @Author : hsjhsj19
	 * @param carts
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getProductPromotionInfoList(List<OcCart> carts) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (OcCart cart : carts) {
			Map<String, Object> map = new HashMap<String, Object>();

			// 정상가 가격 조회용
			PdProductPriceHistory productPrice = new PdProductPriceHistory();
			productPrice.setPrdtNo(cart.getPrdtNo());
			productPrice = productPriceHistoryDao.selectMaxPrdtPriceHistSeq(productPrice);
			// 옵션 추가 가격 조회용
			PdProductOptionPriceHistory productOptionPrice = productOptionPriceHistoryDao
					.selectMaxPrdtOptnPriceHistSeq(cart.getPrdtNo(), cart.getPrdtOptnNo());

			PrPromotion promotion = new PrPromotion();
			promotion.setPrdtNo(cart.getPrdtNo()); // 대상상품조건용
			promotion.setSiteNo(cart.getSiteNo()); // 채널조건용(사이트로 조회시 3건이 나올수도있어서 채널 번호로 받아와야함)
			promotion.setCtgrNo(cart.getCtgrNo()); // 카테고리용
			promotion.setMemberTypeCode(cart.getMemberTypeCode()); // 대상등급조건용
			promotion.setMbshpGradeCode(cart.getMbshpGradeCode()); // 대상등급조건용
			promotion.setEmpYn(cart.getEmpYn()); // 대상등급조건용
			promotion.setOrderQty(cart.getOrderQty()); // 다족할인용
			promotion.setNormalAmt(productPrice.getNormalAmt()); // 할인가계산용
			promotion.setOptnAddAmt(productOptionPrice != null ? productOptionPrice.getOptnAddAmt() : 0); // 할인가계산용
			// 디바이스용은 안줘서 아직 promotion.setDeviceCode();

			// 프로모션 할인 목록 조회
			List<PrPromotion> promotionList = searchPromotionList(promotion);

			map.put("cartSeq", cart.getCartSeq());
			map.put("prdtNo", cart.getPrdtNo());
			map.put("orderQty", cart.getOrderQty());
			map.put("promotionList", promotionList);
			list.add(map);
		}

		return list;

	}

	/**
	 * @Desc : 프로모션 할인 목록 조회
	 * @Method Name : searchPromotionList
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param promotion
	 * @return
	 * @throws Exception
	 */
	public List<PrPromotion> searchPromotionList(PrPromotion promotion) throws Exception {
		List<PrPromotion> promotionList = new ArrayList<PrPromotion>();

		// 할인율을 가진 목록 조회 (상품,브랜드,카테고리)
		promotion.setPromoTypeCodeArr(new String[] { "10002", "10003", "10004" }); // 즉시할인, 제휴사 즉시할인, 타임특가
		promotionList.addAll(this.promotionDao.getPromotionDscntList(promotion));

		// 다족구매할인 목록 조회
		promotion.setPromoTypeCodeArr(new String[] { "10001" }); // 다족구매할인
		promotionList.addAll(this.promotionDao.getPromotionMultiBuyDiscountList(promotion));

		// 사은품 목록 조회 (사은품을 식별 할 키가 필요한지 확인 추후 개발 예정일거 같음)
		promotion.setPromoTypeCodeArr(new String[] { "10005" }); // 사은품 지급
		promotionList.addAll(this.promotionDao.getPromotionFreeGiftList(promotion));

		return promotionList;
	}

	/**
	 * @Desc : 상품/옵션에 적용된 프로모션과 쿠폰에 의한 할인 금액 Validator
	 *       회원번호=memberNo,상품번호=prdtNo,옵션번호=prdtOptnNo,주문수량=orderQty,
	 *       적용프로모션번호=promoNo,프로모션 할인가격(수량X)=promoDscntAmt,적용쿠폰번호=cpnNo,
	 *       쿠폰할인가격(수량X)=cpnDscntAmt
	 * @Method Name : validatePrdtOptnPromoAndCpn
	 * @Date : 2019. 5. 21.
	 * @Author : hsjhsj19
	 * @param memberNo
	 * @param prdtNo
	 * @param prdtOptnNo
	 * @param orderQty
	 * @param promoNo
	 * @param promoDscntAmt
	 * @param cpnNo
	 * @param cpnDscntAmt
	 * @return
	 * @throws Exception
	 */
	public String validatePrdtOptnPromoAndCpn(List<PdProductWithAll> prdtList) throws Exception {
		String validator = Const.BOOLEAN_FALSE;
		for (PdProductWithAll prdt : prdtList) {
			// 정상가 가격 조회용
			PdProductPriceHistory productPrice = new PdProductPriceHistory();
			productPrice.setPrdtNo(prdt.getPrdtNo());
			productPrice = productPriceHistoryDao.selectMaxPrdtPriceHistSeq(productPrice);
			// 옵션 추가 가격 조회용
			PdProductOptionPriceHistory productOptionPrice = productOptionPriceHistoryDao
					.selectMaxPrdtOptnPriceHistSeq(prdt.getPrdtNo(), prdt.getPrdtOptnNo());

			// 프로모션 정보 조회
			PrPromotion promotion = new PrPromotion();
			promotion.setPromoNo(prdt.getPromoNo());
			promotion.setPrdtNo(prdt.getPrdtNo());
			promotion.setOrderQty(prdt.getOrderQty());
			promotion.setNormalAmt(productPrice.getNormalAmt());
			promotion.setOptnAddAmt(productOptionPrice.getOptnAddAmt());
			promotion = this.promotionDao.selectPromotionByPrimaryKey(promotion);

			if (promotion != null) {
				// 다족 프로모션인 경우 다시 조회
				if ("10001".equals(promotion.getPromoTypeCode())) {
					promotion.setPromoTypeCodeArr(new String[] { "10001" });
					List<PrPromotion> promotions = this.promotionDao.getPromotionMultiBuyDiscountList(promotion);
					promotion = promotions.get(0);
				}

				// 넘어온 프로모션 가격과 상품 가격 비교
				if (prdt.getPromoDscntAmt().equals(promotion.getDscntAmt())) {
					validator = Const.BOOLEAN_TRUE;
					// 쿠폰번호가 있고 쿠폰동시적용가능여부 N이면 적용 불가 처리
					if (UtilsObject.isNotEmpty(prdt.getCpnNo())
							&& Const.BOOLEAN_FALSE.equals(promotion.getCpnSmtmApplyPsbltYn())) {
						validator = Const.BOOLEAN_FALSE;
						break;
					}
				} else {
					validator = Const.BOOLEAN_FALSE;
				}

			}

			// 쿠폰 정보 조회
			PrCoupon coupon = new PrCoupon();
			coupon.setCpnNo(prdt.getCpnNo());
			coupon.setNormalAmt(productPrice.getNormalAmt());
			coupon.setOrderQty(prdt.getOrderQty());
			coupon.setOptnAddAmt(productOptionPrice.getOptnAddAmt());
			coupon = this.couponDao.selectCouponByPrimaryKey(coupon);
			if (coupon != null) {
				if (prdt.getCpnDscntAmt().equals(coupon.getDscntAmt())) {
					validator = Const.BOOLEAN_TRUE;
				} else {
					validator = Const.BOOLEAN_FALSE;
				}
			}

			if (Const.BOOLEAN_FALSE.equals(validator)) {
				break;
			}
		}

		return validator.equals(Const.BOOLEAN_TRUE) ? "True" : "False";
	}

	/**
	 * @Desc : 한 상품에 대한 적용 프로모션 조회. 최저가순으로 조회.(중복되는 경우, 프로모션기간 긴 것이 우선됨)
	 * @Method Name : getPromotionByPrdtNo
	 * @Date : 2019. 6. 12.
	 * @Author : tennessee
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductPromotion> getPromotionByPrdtNo(String prdtNo) throws Exception {
		return this.promotionDao.selectPromotionByPrdtNo(prdtNo);
	}

	/**
	 * @Desc : 상품번호에 해당하는 사은픔 프로모션정보 및 사은품정보 조회
	 * @Method Name : getPromotionOfGiftByPrdtNo
	 * @Date : 2019. 7. 10.
	 * @Author : tennessee
	 * @param prdtNo
	 * @throws Exception
	 */
	public List<PdProduct> getPromotionOfGiftByPrdtNo(String prdtNo) throws Exception {
		return this.promotionDao.selectPromotionOfGiftByPrdtNo(prdtNo);
	}

}
