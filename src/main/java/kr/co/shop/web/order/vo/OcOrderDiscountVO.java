package kr.co.shop.web.order.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcOrderDiscountVO {

	private String orderNo;
	private String memberNo;

	private String discountGbn;
	private String discountId;
	private String discountTxt;
	private String discountStartDt; // 쿠폰 유효기간 , 프로모션 기간 Strart
	private String discountEndDt; // 쿠폰 유효기간 , 프로모션 기간 End

	private String discountType;
	private Integer discountValue;
	private Integer discountAmt;
	private String discountPrdtNo;
	private String discountPrdtName;

	private String discoountPrdCode;
	private String discoountPrdName;
	private Integer discoountCnt;

}
