package kr.co.shop.web.order.model.master;

import kr.co.shop.web.order.model.master.base.BaseOcOrderProductApplyPromotion;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class OcOrderProductApplyPromotion extends BaseOcOrderProductApplyPromotion {

	/**
	 * 설명 : 상위상품번호
	 */
	private String upPrdtNo;

	/**
	 * 설명 : 프로모션 상품번호
	 */
	private String prdtNo;

	/**
	 * 프로모션 상품 옵션번호
	 */
	private String prdtOptnNo;

	/**
	 * 상품명
	 */
	private String prdtName;

}
