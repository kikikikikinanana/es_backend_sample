package kr.co.shop.web.promotion.model.master;

import kr.co.shop.web.promotion.model.master.base.BasePrCoupon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PrCoupon extends BasePrCoupon {

	/** 서비스 요청 필요 정보 */
	private String memberNo; // 회원번호
	private String memberTypeCodeName; // 멤버쉽타입
	private String cartSeq; // 장바구니순번
	private String prdtNo; // 상품번호
	private String prdtOptnNo; // 상품옵션번호
	private String holdCpnSeq; // 보유쿠폰순번
	private Integer applyPsbltQty; // 적용가능수량(확인을 해봐야함)
	private String applyPsbltQtyYn; // 적용가능수량여부(확인을 해봐야함)
	private String cpnDwldYn; // 쿠폰다운여부
	private Integer shareRate; // 업체분담율
	private String siteNo; // 사이트번호

	private String[] cpnNos;
	private String storeCount;
	private String productCount;
	private String deviceCodeName;
	private String limitDay;
	private String storeName;
	private String cpnStatCode;
	private int memberCouponCnt;
	private String paperNoText;
	private String rgstYn;
	private String strValidStartDtm;
	private String strValidEndDtm;
	private String sortType;
	private String storeUseYn;

	private Integer dscntAmt; // 할인 가격
	private Integer normalAmt; // 정상가
	private Integer orderQty; // 주문 수량
	private Integer optnAddAmt; // 옵션 추가 금액

}
