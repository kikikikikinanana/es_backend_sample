package kr.co.shop.web.cart.vo;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcCartVendorVo extends BaseBean {

	private String vndrNo; // 업체번호

	private String vndrGbnType; // 업체구분

	private String vndrName; // 업체명

	private String logisVndrCode; // 택배사코드

	private java.lang.Short dlvyDayCount; // 배송소요일

	private java.lang.Integer freeDlvyStdrAmt; // 무료배송기준금액

	private java.lang.Integer dlvyAmt; // 배송비

	private String freeDlvyFlag; // 업체별 무료배송 여부

	private String dlvyPrdtNo; // 배송상품번호
	private String dlvyPrdtOptnNo; // 배송상품옵션번호

	private Long vndrTotalPrdtAmt; // 업체별 상품별 금액
	private Long vndrTotalPrdtCnt; // 업체별 상품 count ( 주문수량 미포함)

}
