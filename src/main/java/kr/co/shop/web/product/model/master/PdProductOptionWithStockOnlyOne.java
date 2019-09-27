package kr.co.shop.web.product.model.master;

import java.util.List;

import kr.co.shop.web.product.model.master.base.BasePdProductOption;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 서비스 요청서 조회용
 * @FileName : PdProductOptionWithStockAndPrice.java
 * @Project : shop.bo
 * @Date : 2019. 4. 22.
 * @Author : hsjhsj19
 */
@Slf4j
@Data
public class PdProductOptionWithStockOnlyOne extends BasePdProductOption {

	/** Start Input param */
	/** 채널번호 */
	private String chnnlNo;

	/** 사이트번호 */
	private String siteNo;

	/** 재고구분코드 목록 */
	private List<SyCodeDetail> stockGbnCodeList;
	/** End Input param */

	/** 옵션추가금액 */
	private Integer optnAddAmt;

	// 상품옵션재고 정보
	/** 온라인물류 */
	private Integer stockAiQty;
	/** 스마트물류 */
	private Integer stockAwQty;
	/** 오프라인매장 */
	private Integer stockAsQty;
	/** 입점사배송 */
	private Integer stockVdQty;

	// 판매상태
	private String sellStatCode;

	// 전체재고
	private Integer totalStockQty;

}
