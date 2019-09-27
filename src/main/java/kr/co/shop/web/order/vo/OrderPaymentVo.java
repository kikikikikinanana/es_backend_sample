/**
 * 
 */
package kr.co.shop.web.order.vo;

import java.io.Serializable;
import java.util.List;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.request.annotation.ParameterOption;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.payment.kakao.model.KakaoPaymentApproval;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.interfaces.module.payment.naver.model.NaverPaymentApproval;
import kr.co.shop.web.cart.model.master.OcCartBenefit;
import kr.co.shop.web.order.model.master.OcOrderConvenienceStore;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.model.master.OcOrderProductApplyPromotion;
import kr.co.shop.web.order.model.master.OcOrderUseCoupon;
import lombok.Data;
import lombok.ToString;

/**
 * @Desc : 주문 결제 VO
 * @FileName : OrderPaymentVo.java
 * @Project : shop.fo
 * @Date : 2019. 4. 15.
 * @Author : ljyoung@3top.co.kr
 */
@Data
public class OrderPaymentVo implements Serializable {

	private static final long serialVersionUID = 8681932560404681822L;

	/**
	 * 설명 : 주문번호
	 */
	private String orderNo;

	/**
	 * 설명 : 사이트번호
	 */
	private String siteNo;

	/**
	 * 설명 : 디바이스코드
	 */
	private String deviceCode;

	/**
	 * 설명 : 임직원주문여부
	 */
	private String empOrderYn;

	/**
	 * 설명 : 회원번호
	 */
	private String memberNo;

	/**
	 * 설명 : 회원유형코드
	 */
	private String memberTypeCode;

	/**
	 * 설명 : 안심키
	 */
	private String safeKey;

	/**
	 * 설명 : 임직원여부
	 */
	private String empYn;

	/**
	 * 설명 : OTS VIP 여부
	 */
	private String otsVipYn;

	/**
	 * 설명 : 비회원비밀번호
	 */
	private String pswdText;

	/**
	 * 설명 : 비회원비밀번호Salt
	 */
	private String pswdSaltText;

	/**
	 * 설명 : 예약주문여부
	 */
	private String rsvOrderYn;

	/**
	 * 예약주문 발송일자
	 */
	private java.sql.Timestamp rsvDlvyYmd;

	/**
	 * 설명 : 배송유형코드
	 */
	private String dlvyTypeCode;

	/**
	 * 설명 : 매장번호
	 */
	private String storeNo;

	/**
	 * 설명 : 매장명
	 */
	private String storeName;

	/**
	 * 설명 : 주문자명
	 */
	private String buyerName;

	/**
	 * 설명 : 주문자이메일주소
	 */
	private String buyerEmailAddrText;

	/**
	 * 설명 : 주문자핸드폰번호
	 */
	private String buyerHdphnNoText;

	/**
	 * 설명 : 수취인명
	 */
	private String rcvrName;

	/**
	 * 설명 : 수취인핸드폰번호
	 */
	private String rcvrHdphnNoText;

	/**
	 * 설명 : 수취인우편번호
	 */
	private String rcvrPostCodeText;

	/**
	 * 설명 : 수취인우편주소
	 */
	private String rcvrPostAddrText;

	/**
	 * 설명 : 수취인상세주소
	 */
	private String rcvrDtlAddrText;

	/**
	 * 설명 : 배송메모
	 */
	private String dlvyMemoText;

	/**
	 * 설명 : 정상가총액
	 */
	private java.lang.Integer totalNormalAmt;

	/**
	 * 설명 : 판매가총액
	 */
	private java.lang.Integer totalSellAmt;

	/**
	 * 설명 : 프로모션할인총액
	 */
	private java.lang.Integer totalPromoDscntAmt;

	/**
	 * 설명 : 쿠폰할인총액
	 */
	private java.lang.Integer totalCpnDscntAmt;

	/**
	 * 설명 : 임직원할인총액
	 */
	private java.lang.Integer totalEmpDscntAmt;

	/**
	 * 설명 : 포인트사용액
	 */
	private java.lang.Integer pointUseAmt;

	/**
	 * 설명 : 구매포인트사용액
	 */
	private java.lang.Integer accessPointUseAmt;

	/**
	 * 설명 : 이벤트포인트사용액
	 */
	private java.lang.Integer eventPointUseAmt;

	/**
	 * 설명 : 자사배송비
	 */
	private java.lang.Integer mmnyDlvyAmt;

	/**
	 * 설명 : 입점사배송비총액
	 */
	private java.lang.Integer totalVndrDlvyAmt;

	/**
	 * 설명 : 결제예정금액
	 */
	private java.lang.Integer pymntTodoAmt;

	/**
	 * 설명 : 결제금액
	 */
	private java.lang.Integer pymntAmt;

	/**
	 * 설명 : 편의점 정보
	 */
	private OcOrderConvenienceStore orderConvenienceStore;

	/**
	 * 설명 : 주문상품정보
	 */
	private String orderPrdtInfo;

	/**
	 * 주문 상품 정보
	 */
	@ParameterOption(target = "orderNo")
	private OrderProduct[] orderProduct;

	/**
	 * 주문 프로모션 정보
	 */
	@ParameterOption(target = "promoNo")
	private OrderPromotion[] promotionList;

	/**
	 * 더블포인트쿠폰 정보
	 */
	@ParameterOption(target = "orderNo")
	private OcOrderUseCoupon doublePointCoupon;

	/**
	 * 무료배송쿠폰 정보
	 */
	private OcOrderUseCoupon[] dlvyCouponList;

	/**
	 * 상품쿠폰 정보
	 */
	private OrderCoupon[] prdtCouponList;

	/**
	 * 주문 주결제 수단 정보
	 */
	@ParameterOption(target = "orderNo")
	private OrderPayment orderMainPayment;

	/**
	 * 기프트 카드 결제 정보
	 */
	private OrderPayment orderGiftPayment;

	/**
	 * 주문 동의 정보
	 */
	@ParameterOption(target = "orderNo")
	private OrderTermsAgree[] orderTermsAgree;

	/**
	 * @Desc : 주문 상품 정보
	 * @FileName : OrderPaymentVo.java
	 * @Project : shop.fo
	 * @Date : 2019. 5. 8.
	 * @Author : ljyoung@3top.co.kr
	 */
	@Data
	@ToString(callSuper = true)
	public static class OrderProduct extends OcOrderProduct {

		private static final long serialVersionUID = -4478489953567782849L;

		/**
		 * 설명 : 장바구니순번
		 */
		private java.lang.Long cartSeq;

	}

	/**
	 * @Desc : 주문 프로모션 정보
	 * @FileName : OrderPaymentVo.java
	 * @Project : shop.fo
	 * @Date : 2019. 6. 13.
	 * @Author : ljyoung@3top.co.kr
	 */
	@Data
	@ToString(callSuper = true)
	public static class OrderPromotion extends OcOrderProductApplyPromotion {

		private static final long serialVersionUID = 672869094884531708L;

		/**
		 * 설명 : 장바구니순번
		 */
		private java.lang.Long cartSeq;

	}

	/**
	 * @Desc : 주문 주결제 수단 정보
	 * @FileName : OrderPaymentVo.java
	 * @Project : shop.fo
	 * @Date : 2019. 4. 15.
	 * @Author : ljyoung@3top.co.kr
	 */
	@Data
	public static class OrderPayment implements Serializable {

		private static final long serialVersionUID = -4079574568887105027L;

		/**
		 * 설명 : 주문번호
		 */
		private String orderNo;

		/**
		 * 설명 : 주문결제순번
		 */
		private java.lang.Short orderPymntSeq;

		/**
		 * 설명 : 주결제수단여부
		 */
		private String mainPymntMeansYn;

		/**
		 * 설명 : 결제수단코드
		 */
		private String pymntMeansCode;

		/**
		 * 설명 : 결제 interface용 PG 구분
		 */
		private String addInfo2;

		/**
		 * 기프트카드 번호
		 */
		private String giftCardNo;

		/**
		 * 기프트카드 인증번호
		 */
		private String giftCardPinNo;

		/**
		 * 결제 금액
		 */
		private java.lang.Integer payAmt;

		/**
		 * 결제 수당 저장여부
		 */
		private String savePaymentYN;

		/**
		 * KCP 승인 PARAMETER
		 */
		private KcpPaymentApproval kcpPaymentApproval;

		/**
		 * 네이버페이 승인 PARAMETER
		 */
		private NaverPaymentApproval naverPaymentApproval;

		/**
		 * 카카오페이 승인 PARAMETER
		 */
		private KakaoPaymentApproval kakaoPaymentApproval;

		public Object returnPayment() {

			if (addInfo2.equals(Const.PAYMENT_VENDOR_NAME_KCP)) {
				kcpPaymentApproval.setSiteCd(Config.getString("pg.kcp.siteCode"));
				kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.siteKey"));
				return kcpPaymentApproval;
			}
			if (addInfo2.equals(Const.PAYMENT_VENDOR_NAME_NAVER)) {
				return naverPaymentApproval;
			}
			if (addInfo2.equals(Const.PAYMENT_VENDOR_NAME_KAKAO)) {
				return kakaoPaymentApproval;
			}
			return null;
		}

	}

	/**
	 * @Desc : 주문 동의 정보
	 * @FileName : OrderPaymentVo.java
	 * @Project : shop.fo
	 * @Date : 2019. 4. 15.
	 * @Author : ljyoung@3top.co.kr
	 */
	@Data
	@ToString(callSuper = true)
	public static class OrderTermsAgree implements Serializable {

		private static final long serialVersionUID = -7717720746492814545L;

		/**
		 * 설명 : 주문번호
		 */
		private String orderNo;

		/**
		 * 설명 : 약관순번
		 */
		private java.lang.Integer termsSeq;

		/**
		 * 설명 : 약관상세순번
		 */
		private java.lang.Integer termsDtlSeq;

		/**
		 * 설명 : 약관동의여부
		 */
		private String termsAgreeYn;
	}

	/**
	 * @Desc : 주문 사용 쿠폰 정보
	 * @FileName : OrderPaymentVo.java
	 * @Project : shop.fo
	 * @Date : 2019. 6. 5.
	 * @Author : ljyoung@3top.co.kr
	 */
	@Data
	public static class OrderCoupon implements Serializable {

		private static final long serialVersionUID = 6133927484597760545L;

		/**
		 * 설명 : 장바구니순번
		 */
		private java.lang.Long cartSeq;

		private List<OcCartBenefit> couponList;

	}
}
