package kr.co.shop.web.order.vo;

import java.io.Serializable;
import java.util.List;

import kr.co.shop.common.request.annotation.ParameterOption;
import kr.co.shop.constant.Const;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import lombok.Data;

/**
 * @Desc : 주문서 작성 FORM VO
 * @FileName : OrderVo.java
 * @Project : shop.fo
 * @Date : 2019. 4. 8.
 * @Author : ljyoung@3top.co.kr
 */
@Data
public class OrderFormVo implements Serializable {

	private static final long serialVersionUID = -2533959605541695259L;

	/**
	 * 회원정보
	 */
	private OrderFormMemberVo memberInfo;

	/**
	 * 총 정상가
	 */
	private Integer totalNormalAmt;

	/**
	 * 총 판매가
	 */
	private Integer totalSellAmt;

	/**
	 * 총 할인금액
	 */
	private Integer totalDscntAmt;

	/**
	 * 총 배송비
	 */
	private Integer totalDlvyAmt;

	/**
	 * 총 적립예정 포인트
	 */
	private double totalSavePoint = 0;

	/**
	 * 상품 할인가
	 */
	private Integer productDscntAmt = 0;

	/**
	 * 프로모션 할인가
	 */
	private Integer promotionDscntAmt = 0;

	/**
	 * 쿠폰 할인가
	 */
	private Integer couponDscntAmt = 0;

	/**
	 * 주문서 진입시 배송 유형, D : 일반배송, P : 픽업배송 (Default : D)
	 */
	private String cartDeliveryType;

	/**
	 * 주문 유입 유형, C:장바구니, Q:바로구매, E:DRAW 이벤트
	 */
	private String orderCartType;

	/**
	 * 디바이스코드
	 */
	private String deviceCode;

	/**
	 * 매장픽업가능여부
	 */
	private boolean isStorePickupDlvy = true;

	/**
	 * 편의점픽업가능여부
	 */
	private boolean isCsvPickupDlvy = true;

	/**
	 * 통합멤버십 여부
	 */
	private boolean isMemberShip = false;

	/**
	 * 예약주문 여부
	 */
	private String rsvOrderYn = Const.BOOLEAN_FALSE;

	/**
	 * 상품 기준 입점사 리스트
	 */
	@ParameterOption(target = "cartSeq")
	private List<OrderFormVendorVo> vendorList;

	/**
	 * 상품 쿠폰 리스트
	 */
	private List<OrderCoupon> couponList;

	@Data
	public static class OrderFormMemberVo {
		/**
		 * 설명 : 회원번호
		 */
		private String memberNo;

		/**
		 * 설명 : 회원명
		 */
		private String memberName;

		/**
		 * 설명 : 회원유형코드
		 */
		private String memberTypeCode;

		/**
		 * 설명 : 멤버십등급코드
		 */
		private String mbshpGradeCode;

		/**
		 * 설명 : 임직원여부
		 */
		private String empYn;

		/**
		 * 설명 : 이메일주소
		 */
		private String emailAddrText;

		/**
		 * 설명 : 핸드폰번호
		 */
		private String hdphnNoText;

		/**
		 * 설명 : 결제수단코드
		 */
		private String pymntMeansCode;

		/**
		 * 전체 포인트
		 */
		private int totalPoint = 0;

		/**
		 * 구매포인트
		 */
		private int accessPoint = 0;

		/**
		 * 이벤트포인트
		 */
		private int eventPoint = 0;
	}

	@Data
	public static class OrderFormVendorVo {
		/**
		 * 설명 : 업체번호
		 */
		private String vndrNo;

		/**
		 * 설명 : 업체구분
		 */
		private String vndrGbnType;

		/**
		 * 설명 : 업체명
		 */
		private String vndrName;

		/**
		 * 설명 : 무료배송기준금액
		 */
		private java.lang.Integer freeDlvyStdrAmt;

		/**
		 * 설명 : 배송비
		 */
		private java.lang.Integer dlvyAmt;

		/**
		 * 설명 : 무료배송여부
		 */
		private boolean isFreeDlvy;

		/**
		 * 상품 종류
		 */
		private int productCount;

		/**
		 * 총 상품 갯수
		 */
		private int totalProductCount;

		/**
		 * 상품 리스트
		 */
		@ParameterOption(target = "vndrNo")
		private List<OrderFormProductVo> productList;

	}

	@Data
	public static class OrderFormProductVo {
		/**
		 * 주문 수량
		 */
		private java.lang.Integer orderQty;

		/**
		 * 상품 포인트
		 */
		private String savePoint;

		/**
		 * 설명 : 카테고리번호
		 */
		private String ctgrNo;

		/**
		 * 설명 : 기획전번호
		 */
		private String plndpNo;

		/**
		 * 설명 : 이벤트번호
		 */
		private String eventNo;

		private Integer manyShoesDcAmt; // 프로모션 할인가 중 다족구매 할인금액

		private Integer cpnDscntAmt; // 쿠폰할인금액
		private Integer promoDscntAmt; // 쿠폰할인금액
		private Integer totalDscntAmt; // 할인금액합계

		private String nomalCpnNo;// 일반쿠폰 번호
		private String plusCpnNo;// 플러스쿠폰 번호
		private String nomalHoldCpnSeq; // 일반 쿠폰 보유 쿠폰 순번
		private String plusHoldCpnSeq; // 플러스 쿠폰 보유 쿠폰 순번

		private java.lang.Integer nomalCpnDcAmt;// 일반쿠폰 할인금액
		private java.lang.Integer plusCpnDcAmt; // 플러스쿠폰 할인금액

		/**
		 * 상품 정보
		 */
		private PdProductWithAll product;
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

		private List<OrderCartBenefit> couponList;

	}

	@Data
	public static class OrderCartBenefit implements Serializable {

		private static final long serialVersionUID = -5834372378331384238L;

		/**
		 * 설명 : 쿠폰번호
		 */
		private String cpnNo;

		/**
		 * 설명 : 보유쿠폰순번
		 */
		private java.lang.Integer holdCpnSeq;

		/**
		 * 할인가
		 */
		private java.lang.Integer cpnApplyDcAmt;

		/**
		 * 설명 : 쿠폰적용수량
		 */
		private java.lang.Integer cpnApplyQty;
	}

	@Data
	public static class DeleveryAddress implements Serializable {

		private static final long serialVersionUID = 1319001807501747369L;
		/**
		 * 회원번호
		 */
		private String memberNo;
		/**
		 * 이름
		 */
		private String rcvrName;
		/**
		 * 핸드폰 번호
		 */
		private String rcvrHdphnNoText;

		/**
		 * 우편번호
		 */
		private String rcvrPostCodeText;

		/**
		 * 우편번호 주소
		 */
		private String rcvrPostAddrText;

		/**
		 * 상세주소
		 */
		private String rcvrDtlAddrText;

	}
}
