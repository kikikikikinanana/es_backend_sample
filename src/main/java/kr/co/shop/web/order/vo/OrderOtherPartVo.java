package kr.co.shop.web.order.vo;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

@Data
public class OrderOtherPartVo extends BaseBean implements Serializable {

	// 주문건 수
	private java.lang.Integer orderCnt;

	// 클레임건 수
	private java.lang.Integer claimCnt;

	// AS건 수
	private java.lang.Integer asCnt;

	/**************************************/

	// 주문번호
	private String orderNo;

	// 매장명
	private String storeName;

	// 멤버십회원여부
	private String memberShipYn;

	// 30일경과 여부
	private String buydayOverday;

	// 결제금액
	private java.lang.Integer pymntAmt;

	// 주문일자
	private java.sql.Timestamp orderDtm;

	// 구매확정일시
	private java.sql.Timestamp buyDcsnDtm;

	// 포인트 적립여부
	private String pointSaveYn;

	// 인증번호
	private String crtfcNoText;

}
