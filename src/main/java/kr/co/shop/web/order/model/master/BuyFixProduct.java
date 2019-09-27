package kr.co.shop.web.order.model.master;

import lombok.Data;

@Data
public class BuyFixProduct {

	/**
	 * 주문번호
	 */
	private String orderNum;

	/**
	 * 상품코드
	 */
	private String prdtCode;

	/**
	 * 옵션아이디
	 */
	private String optnId;

	/**
	 * 포인트
	 */
	private int point;

	/**
	 * 상품풀코드
	 */
	private String fullCode;

	/**
	 * 정상리턴여부
	 */
	private String resultYn;

	public void setFullCode(String sangPumFullCd) {
		this.fullCode = sangPumFullCd;
		String optnId = sangPumFullCd.substring(sangPumFullCd.length() - 3);
		String prdtCode = sangPumFullCd.substring(0, 7);

		this.prdtCode = prdtCode;
		this.optnId = optnId;
	}
}
