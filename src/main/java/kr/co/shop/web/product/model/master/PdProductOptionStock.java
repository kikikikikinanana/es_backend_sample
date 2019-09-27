package kr.co.shop.web.product.model.master;

import kr.co.shop.web.product.model.master.base.BasePdProductOptionStock;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class PdProductOptionStock extends BasePdProductOptionStock {

	/**
	 * 자사 상품 재고 통합 여부
	 */
	private String stockIntgrYn;

	/**
	 * 자사 상품 여부
	 */
	private String mmnyPrdtYn;

	/**
	 * 입점업체 옵션 상품 판매 상태
	 */
	private String sellStatCode;

	/**
	 * 옵션 전시 여부
	 */
	private String useYn;

	/**
	 * 입점업체 재고 관리 여부
	 */
	private String stockMgmtYn;

	/**
	 * 온라인 물류 재고 수량
	 */
	private Integer stockAiQty;

	/**
	 * 스마트 물류 재고 수량
	 */
	private Integer stockAwQty;

	/**
	 * 오프라인 매장 재고 수량
	 */
	private Integer stockAsQty;

	/**
	 * 입점사 재고 수량
	 */
	private Integer stockVdQty;

}
