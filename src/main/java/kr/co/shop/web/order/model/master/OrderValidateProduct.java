package kr.co.shop.web.order.model.master;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderValidateProduct implements Serializable {

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 상품번호
	 */
	private String prdtNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 상품옵션번호
	 */
	private String prdtOptnNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 재고수량
	 */

	private java.lang.Integer sumStockQty;

	private String prdtNoPrdtOptnNo;

	private int aiQty; // AI 재고수량
	private int asQty; // AS 재고수량
	private int awQty; // AW 재고 수량

}
