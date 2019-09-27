package kr.co.shop.web.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.web.member.repository.master.MbMemberInterestProductDao;
import kr.co.shop.web.product.model.master.PdProductOption;
import kr.co.shop.web.product.model.master.PdProductOptionStock;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockAndPrice;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockOnlyOne;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.repository.master.PdProductDao;
import kr.co.shop.web.product.repository.master.PdProductOptionDao;
import kr.co.shop.web.product.repository.master.PdProductOptionStockDao;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductOptionService {

	@Autowired
	private PdProductDao productDao;

	@Autowired
	private PdProductOptionDao productOptionDao;

	@Autowired
	private PdProductOptionStockDao productOptionStockDao;

	@Autowired
	private MbMemberInterestProductDao memberInterestProductDao;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private ProductService productService;

	/**
	 * @Desc : 상품 옵션 목록 재고와 가격까지 함께 조회 (서비스 요청서)
	 * @Method Name : getProductOptionListWithStockAndPrice
	 * @Date : 2019. 4. 22.
	 * @Author : hsjhsj19
	 * @param memberNo
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductOptionWithStockAndPrice> getProductOptionListWithStockAndPrice(String prdtNo)
			throws Exception {
		PdProductOptionWithStockAndPrice optionWithStockAndPrice = new PdProductOptionWithStockAndPrice();
		optionWithStockAndPrice.setPrdtNo(prdtNo);

		String[] codeFields = { CommonCode.STOCK_GBN_CODE };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);
		optionWithStockAndPrice.setStockGbnCodeList(codeList.get(CommonCode.STOCK_GBN_CODE)); // 재고구분코드

		List<PdProductOptionWithStockAndPrice> list = this.productOptionDao
				.selectProductOptionListWithStockAndPrice(optionWithStockAndPrice);
		if (2 < list.size()) {
			for (int i = 1; i < list.size(); i++) {
				PdProductOptionWithStockAndPrice bdProductOptionWithStockAndPrice = list.get(i);
				bdProductOptionWithStockAndPrice.setOptnAddAmt(0);
			}
		}
		return list;
	}

	/**
	 * @Desc : 사이트번호=siteNo,상품번호=prdtNo,상품옵션번호=prdtOptnNo,주문수량 =orderPsbltQty 상품 옵션별
	 *       Validator (단건으로 받았을 경우)
	 * @Method Name : getProductOptionValidator
	 * @Date : 2019. 4. 22.
	 * @Author : hsjhsj19
	 * @param siteNo
	 * @param prdtNo
	 * @param prdtOptnNo
	 * @param totalOrderQty
	 * @return
	 * @throws Exception
	 */
	public String getProductOptionValidator(String siteNo, String prdtNo, String prdtOptnNo, Integer orderPsbltQty)
			throws Exception {
		// siteNo는 어떻게 쓰이는 지 확인 필요
		String result = "";
		PdProductOption option = new PdProductOption();
		option.setPrdtNo(prdtNo);
		option.setPrdtOptnNo(prdtOptnNo);
		option.setOrderPsbltQty(orderPsbltQty);
		List<String> list = this.productOptionDao.productOptionValidator(option);
		for (String str : list) {
			if ("False".equals(str)) {
				result = str;
				break;
			} else {
				result = str;
			}
		}
		return result;
	}

	/**
	 * @Desc : 사이트번호=siteNo,상품번호=prdtNo,상품옵션번호=prdtOptnNo,주문수량 =orderPsbltQty 상품 옵션별
	 *       Validator (List로 받았을 경우)
	 * @Method Name : getProductOptionValidator
	 * @Date : 2019. 4. 23.
	 * @Author : hsjhsj19
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getProductOptionValidator(List<Map<String, Object>> params) throws Exception {
		// siteNo는 어떻게 쓰이는 지 확인 필요
		List<String> list = new ArrayList<String>();
		String result = "";
		for (Map<String, Object> param : params) {
			PdProductOption option = new PdProductOption();
			option.setPrdtNo(param.get("prdtNo").toString());
			option.setPrdtOptnNo(param.get("prdtOptnNo").toString());
			option.setOrderPsbltQty(Integer.parseInt(param.get("orderPsbltQty").toString()));
			list.addAll(this.productOptionDao.productOptionValidator(option));
		}

		for (String str : list) {
			if ("False".equals(str)) {
				result = str;
				break;
			} else {
				result = str;
			}
		}

		return result;
	}

	/**
	 * @Desc : 매장별 재고 현황 목록 조회
	 * @Method Name : selectProductStoreStockList
	 * @Date : 2019. 5. 14.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Page<Map<String, Object>> selectProductStoreStockList(Pageable<Object, Map<String, Object>> pageable)
			throws Exception {
		Integer count = this.productOptionStockDao.selectProductOptionStoreStockCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.setContent(this.productOptionStockDao.selectProductOptionStoreStock(pageable));
		}

		return pageable.getPage();
	}

	/**
	 * @Desc : 상품/옵션별 재고 Validator(서비스 요청서)
	 *       상품번호=prdtNo,옵션번호=prdtOptnNo,주문수량=orderQty
	 * @Method Name : validatePrdtOptnStock
	 * @Date : 2019. 5. 21.
	 * @Author : hsjhsj19
	 * @param prdtNo
	 * @param prdtOptnNo
	 * @return
	 * @throws Exception
	 */
	public String validatePrdtOptnStock(List<PdProductWithAll> prdtWithAllList) throws Exception {
		String validator = Const.BOOLEAN_FALSE;

		String[] codeFields = { CommonCode.STOCK_GBN_CODE };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);

		for (PdProductWithAll prdt : prdtWithAllList) {

			// 사용여부
			if (Const.BOOLEAN_FALSE.equals(prdt.getUseYn())) {
				validator = Const.BOOLEAN_FALSE;
				break;
			}

			// 판매종료
			if ("10004".equals(prdt.getSellStatCode())) {
				validator = Const.BOOLEAN_FALSE;
				break;
			}

			Integer orderQty = prdt.getOrderQty(); // 수량
			if (0 < orderQty) {
				prdt.setStockGbnCodeList(codeList.get(CommonCode.STOCK_GBN_CODE)); // 재고구분코드
				PdProductWithAll withAll = this.productDao.selectProductListWithAll(prdt);

				if (Const.BOOLEAN_TRUE.equals(withAll.getMmnyPrdtYn())) { // 자사 상품인 경우 AI,AW,AS 참고
					Integer orderAiQty, orderAwQty = 0;

					// 1.INET물류
					Integer stockAiQty = withAll.getStockAiQty();
					if (orderQty <= stockAiQty) {
						orderAiQty = orderQty;
					} else {
						orderAiQty = stockAiQty;
					}

					// 2.스마트창고
					Integer stockAwQty = withAll.getStockAwQty();
					if (orderQty - orderAiQty <= stockAwQty) {
						orderAwQty = orderQty - orderAiQty;
					} else {
						orderAwQty = stockAwQty;
					}
					if (Const.BOOLEAN_TRUE.equals(withAll.getStockIntgrYn())) { // 재고통합여부
						Integer orderAsQty = 0;

						// 3.오프라인매장
						Integer stockAsQty = withAll.getStockAsQty();
						// AI,AW가 0 인 경우
						if (stockAiQty <= 0 && stockAwQty <= 0) {
							// 서비스 호출
							// List<ProductOfflineStockHttp> offline =
							// ProductOfflineStockService.getProductOfflineStockFromHttp(pdProductWithAll.getVndrPrdtNoText(),
							// stockAsQty = 0;
						}

						if (orderQty - orderAiQty - orderAwQty <= stockAsQty) {
							orderAsQty = orderQty - orderAiQty - orderAwQty;
						} else {
							orderAsQty = stockAsQty;
						}
						if (orderQty.equals(orderAiQty + orderAwQty + orderAsQty)) {
							validator = Const.BOOLEAN_TRUE;
						} else {
							validator = Const.BOOLEAN_FALSE;
							break;
						}
					} else {
						if (orderQty.equals(orderAiQty + orderAwQty)) {
							validator = Const.BOOLEAN_TRUE;
						} else {
							validator = Const.BOOLEAN_FALSE;
							break;
						}
					}

				} else { // 입점사 상품인 경우 VD만 참고
					if (Const.BOOLEAN_TRUE.equals(withAll.getStockMgmtYn())) { // 재고관리여부
						Integer stockVdQty = withAll.getStockVdQty();
						if (orderQty <= stockVdQty) {
							validator = Const.BOOLEAN_TRUE;
						} else {
							validator = Const.BOOLEAN_FALSE;
							break;
						}
					} else {
						validator = Const.BOOLEAN_TRUE;
					}
				}
			} else {
				validator = Const.BOOLEAN_FALSE;
				break;
			}

			// 한 건이라도 N인 경우 종료
			if (Const.BOOLEAN_FALSE.equals(validator)) {
				break;
			}

		}
		return validator.equals(Const.BOOLEAN_TRUE) ? "True" : "False";
	}

	/**
	 *
	 * @Desc : 상품 옵션 목록 재고와 가격까지 함께 조회 (서비스 요청서)
	 * @Method Name : getProductOptionListWithStockOnlyOne
	 * @Date : 2019. 6. 4.
	 * @Author : NKB
	 * @param prdtNo
	 * @param prdtOptnNo
	 * @return
	 * @throws Exception
	 */
	public PdProductOptionWithStockOnlyOne getProductOptionListWithStockOnlyOne(String prdtNo, String prdtOptnNo)
			throws Exception {
		PdProductOptionWithStockAndPrice optionWithStockAndPrice = new PdProductOptionWithStockAndPrice();
		optionWithStockAndPrice.setPrdtNo(prdtNo);
		optionWithStockAndPrice.setPrdtOptnNo(prdtOptnNo);

		String[] codeFields = { CommonCode.STOCK_GBN_CODE };
		Map<String, List<SyCodeDetail>> codeList = commonCodeService.getUseCodeByGroup(codeFields);
		optionWithStockAndPrice.setStockGbnCodeList(codeList.get(CommonCode.STOCK_GBN_CODE)); // 재고구분코드

		PdProductOptionWithStockOnlyOne list = this.productOptionDao
				.selectProductOptionListWithStockOnlyOne(optionWithStockAndPrice);

		return list;
	}

	/**
	 * @Desc : 상품 옵션 목록 재고까지 함께 조회(서비스 요청서)
	 * @Method Name : getProductOptionListWithStock
	 * @Date : 2019. 4. 22.
	 * @Author : hsjhsj19
	 * @param siteNo
	 * @param prdtNo
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWithAll> getProductOptionListWithStock(String siteNo, String prdtNo, String prdtOptnNo)
			throws Exception {

		PdProductWithAll productWithAll = new PdProductWithAll();
		productWithAll.setSiteNo(siteNo);
		productWithAll.setPrdtNo(prdtNo);
		productWithAll.setPrdtOptnNo(prdtOptnNo);

		// 공통코드 조회
		String[] codeFields = { CommonCode.STOCK_GBN_CODE };
		Map<String, List<SyCodeDetail>> pair = this.commonCodeService.getCodeByGroup(codeFields);
		productWithAll.setStockGbnCodeList(pair.get(CommonCode.STOCK_GBN_CODE));

		return this.productOptionDao.selectProductOptionListWithStock(productWithAll);
	}

	/**
	 * @Desc : 옵션 재고 조회 단건
	 * @Method Name : getPorudctOptionStock
	 * @Date : 2019. 6. 21.
	 * @Author : kiowa
	 * @param prdtNo        : 상품 코드
	 * @param prdtOptnNo    : 옵션 코드
	 * @param storePickupYn : 픽업 배송 여부
	 * @return 판매 가능 재고 수량
	 * @throws Exception
	 */
	public int getProductOptionStock(String prdtNo, String prdtOptnNo, String storePickupYn) throws Exception {
		int availableStockQty = 0;

		List<Map<String, Object>> productOptionList = new ArrayList<Map<String, Object>>();

		Map<String, Object> prdoctOption = new HashMap<String, Object>();

		prdoctOption.put("prdtNo", prdtNo);
		prdoctOption.put("prdtOptnNo", prdtOptnNo);

		productOptionList.add(prdoctOption);

		List<PdProductOptionStock> productOptionStockList = this.selectProductOptionStock(productOptionList);

		if (!productOptionStockList.isEmpty() && productOptionStockList.size() > 0) {
			availableStockQty = this.stockCalculation(productOptionStockList.get(0), storePickupYn);
		}

		return availableStockQty;
	}

	/**
	 * @Desc : 옵션 재고 조회 다건
	 * @Method Name : getProductOptionStock
	 * @Date : 2019. 6. 21.
	 * @Author : kiowa
	 * @param productOptions Map key [prdtNo, prdtOptnNo]
	 * @param storePickupYn  : 픽업 배송 여부
	 * @return Map Key [prdtNo, prdtOptnNo, stockQty]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getProductOptionStock(List<Map<String, Object>> productOptions,
			String storePickupYn) throws Exception {

		List<PdProductOptionStock> productOptionStockList = this.selectProductOptionStock(productOptions);

		for (Map<String, Object> productOption : productOptions) {
			int availableStockQty = 0;
			if (!productOptionStockList.isEmpty() && productOptionStockList.size() > 0) {
				PdProductOptionStock productOptionStock = productOptionStockList.stream()
						.filter(x -> x.getPrdtNo().equals(productOption.get("prdtNo"))
								&& x.getPrdtOptnNo().equals(productOption.get("prdtOptnNo")))
						.collect(Collectors.toList()).get(0);

				availableStockQty = this.stockCalculation(productOptionStock, storePickupYn);

			}
			productOption.put("stockQty", availableStockQty);
		}

		return productOptions;
	}

	/**
	 * @Desc : 옵션 재고 계산
	 * @Method Name : stockCalculation
	 * @Date : 2019. 6. 21.
	 * @Author : kiowa
	 * @param prdtOptionStock
	 * @param storePickupYn
	 * @return
	 * @throws Exception
	 */
	private int stockCalculation(PdProductOptionStock prdtOptionStock, String storePickupYn) throws Exception {
		int availableStockQty = 0;

		storePickupYn = UtilsText.isEmpty(storePickupYn) ? "N" : storePickupYn; // 픽업 가능 상품 조회

		String mmnyPrdtYn = prdtOptionStock.getMmnyPrdtYn(); // 자사 상품 여부
		String stockMgmtYn = prdtOptionStock.getStockMgmtYn(); // 입점 업체 재고 관리 여부
		String stockIntgrYn = prdtOptionStock.getStockIntgrYn(); // 재고 통합 여부
		String sellStatCode = prdtOptionStock.getSellStatCode(); // 옵션 재고 상태 코드
		String useYn = prdtOptionStock.getUseYn(); // 옵션 전시 여부

		Integer stockAiQty = prdtOptionStock.getStockAiQty(); // 온라인 물류 재고 수량
		Integer stockAwQty = prdtOptionStock.getStockAwQty(); // 스마트 물류 재고 수량
		Integer stockAsQty = prdtOptionStock.getStockAsQty(); // 오프라인 매장 재고 수량
		Integer stockVdQty = prdtOptionStock.getStockVdQty(); // 입점사 재고 수량

		// 옵션 판매 상태가 판매 중이면서 전시인 경우에만 재고 계산을 한다.
		if (CommonCode.SELL_STAT_CODE_PROC.equals(sellStatCode) && Const.BOOLEAN_TRUE.equals(useYn)) {
			if (Const.BOOLEAN_TRUE.equals(mmnyPrdtYn)) { // 자사 상품
				if (Const.BOOLEAN_TRUE.equals(storePickupYn)) { // 픽업 가능 상품 재고 조회시.
					availableStockQty = stockAsQty;
				} else {
					if (Const.BOOLEAN_TRUE.equals(stockIntgrYn)) { // 재고 통합 관리
						availableStockQty = stockAiQty + stockAwQty + stockAsQty;
					} else {// 재고 통합 안함 (온라인 재고만 재고 체크)
						availableStockQty = stockAsQty;
					}
				}

			} else {
				if (Const.BOOLEAN_TRUE.equals(stockMgmtYn)) { // 재고 관리
					availableStockQty = stockVdQty;
				} else {
					availableStockQty = 10000;
				}
			}
		}

		return availableStockQty;
	}

	/**
	 * @Desc : 상품 옵션 재고 정보를 조회한다.
	 * @Method Name : selectProductOptionStock
	 * @Date : 2019. 6. 20.
	 * @Author : kiowa
	 * @param productOptionStock
	 * @return
	 * @throws Exception
	 */
	private List<PdProductOptionStock> selectProductOptionStock(List<Map<String, Object>> productOption)
			throws Exception {

		return productOptionDao.selectProductOptionStock(productOption);

	}
}
