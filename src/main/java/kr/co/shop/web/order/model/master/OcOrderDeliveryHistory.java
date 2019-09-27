package kr.co.shop.web.order.model.master;

import kr.co.shop.web.order.model.master.base.BaseOcOrderDeliveryHistory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcOrderDeliveryHistory extends BaseOcOrderDeliveryHistory {

	/**
	 * 설명 : 조건절 배송상태코드
	 */
	private String whereDlvyStatCode;

	/**
	 * 상품번호
	 */
	private String prdtNo;
}
