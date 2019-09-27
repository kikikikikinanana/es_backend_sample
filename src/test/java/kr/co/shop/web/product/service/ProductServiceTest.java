package kr.co.shop.web.product.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductDetailWrapper;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockOnlyOne;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.model.master.PdRelationProduct;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : ProductServiceTest.java
 * @Project : shop.backend
 * @Date : 2019. 5. 22.
 * @Author : tennessee
 */
@SpringBootTest
@Slf4j
class ProductServiceTest {

	@Autowired
	ProductService productService;
	@Autowired
	ProductRelationService productRelationService;
	@Autowired
	ProductOptionService productOptionService;

	/**
	 * @Desc : 매핑테이블기준 상품정보 조회
	 * @Method Name : testGetDisplayProductListByMappingTable
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	@SuppressWarnings("serial")
	void testGetDisplayProductListByMappingTable() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);
		pageableProduct.setRowsPerPage(10);

		pageableProduct.setCondition("10000", "10000");
		/**
		 * 테이블 "dp_category_product"에서, 컬럼 "ctgr_no"가 "1000000004"인 row 찾기
		 */
		pageableProduct.setUseTableMapping("dp_category_product", new LinkedHashMap<String, String>() {
			{
				put("ctgr_no", "1000000006");
			}
		}, null);
//		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_NEWEST, null, null);
//		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_CUSTOM, "sort_seq", "desc");

		Page<PdProductWrapper> test = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 매핑테이블 기준 상품정보 조회
	 * @Method Name : testGetDisplayProductListByPrdtNoList
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("serial")
	void testGetDisplayProductListByPrdtNoList() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);
		pageableProduct.setRowsPerPage(10);

		pageableProduct.setCondition("10001", "10003");
		// 상품번호에 해당하는 정보 조회
		pageableProduct.setNotUseTableMapping(new ArrayList<String>() {
			{
				add("2000000116");
				add("2000000117");
				add("2000000115");
				add("2000000134");
				add("2000000045");
			}
		});
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);

		Page<PdProductWrapper> test = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 상품 상세정보 조회 테스트
	 * @Method Name : testGetDisplayProductDetail
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductDetail() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<PdProductDetailWrapper> parameter = new Parameter<PdProductDetailWrapper>(request, response);
		PageableProduct<PdProductDetailWrapper, PdProductDetailWrapper> pageableProduct = new PageableProduct<PdProductDetailWrapper, PdProductDetailWrapper>(
				parameter);
		PdProductDetailWrapper dummyBean = new PdProductDetailWrapper();
		dummyBean.setPrdtNo("2000000045");
		pageableProduct.setRowsPerPage(1);
		pageableProduct.setBean(dummyBean);
		pageableProduct.setCondition("10000", "10000");
//		pageableProduct.setDetailView(true);
		pageableProduct.setType(PageableProduct.TYPE_DETAIL);
		pageableProduct.setDeviceCode(CommonCode.DEVICE_PC);
		pageableProduct.setNotUseTableMapping(null);
		pageableProduct.setUsePaging(false, null, null, null);

		PdProductDetailWrapper test = this.productService.getDisplayProductDetail(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 상품 주요정보 조회 테스트
	 * @Method Name : testGetDisplayProduct
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProduct() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<PdProductWrapper> parameter = new Parameter<PdProductWrapper>(request, response);
		PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct = new PageableProduct<PdProductWrapper, PdProductWrapper>(
				parameter);
		PdProductWrapper dummyBean = new PdProductWrapper();
		dummyBean.setPrdtNo("2000000045");
		pageableProduct.setRowsPerPage(1);
		pageableProduct.setBean(dummyBean);
		pageableProduct.setMemberInterest("MB20000064", Const.BOOLEAN_FALSE);
		pageableProduct.setCondition("10000", "10000");
//		pageableProduct.setDetailView(true);
		pageableProduct.setType(PageableProduct.TYPE_DETAIL);
		pageableProduct.setDeviceCode(CommonCode.DEVICE_PC);
		pageableProduct.setNotUseTableMapping(null);
		pageableProduct.setUsePaging(false, null, null, null);

		PdProductWrapper test = this.productService.getDisplayProduct(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 상품 상세페이지 접근검사항목 조회
	 * @Method Name : testGetDisplayProductAccessCheckInfo
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductAccessCheckInfo() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<PdProduct> parameter = new Parameter<PdProduct>(request, response);
		PageableProduct<PdProduct, PdProductWrapper> pageableProduct = new PageableProduct<PdProduct, PdProductWrapper>(
				parameter);
		PdProductWrapper dummyBean = new PdProductWrapper();
		dummyBean.setPrdtNo("2000000045");
		pageableProduct.setRowsPerPage(1);
		pageableProduct.setBean(dummyBean);
		pageableProduct.setUseOtherTables(false);
		pageableProduct.setCondition("10000", "10000");
//		pageableProduct.setDetailView(true);
		pageableProduct.setType(PageableProduct.TYPE_DETAIL);
		pageableProduct.setNotUseTableMapping(null);
		pageableProduct.setUsePaging(false, null, null, null);

		PdProductWrapper test = this.productService.getDisplayProductAccessCheckInfo(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 연계상품 조회
	 * @Method Name : testGetDisplayRelatedProduct
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayRelatedProduct() throws Exception {
		String prdtNo = "1000000061";
		String siteNo = "10000";
		String chnnlNo = "10000";

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<PdProductWrapper> parameter = new Parameter<PdProductWrapper>(request, response);
		PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct = new PageableProduct<PdProductWrapper, PdProductWrapper>(
				parameter);

		// 색상연계 상품번호 조회
		PdRelationProduct criteriaForRelation = new PdRelationProduct();
		criteriaForRelation.setPrdtNo(prdtNo);
		criteriaForRelation.setRltnPrdtTypeCode(CommonCode.RLTN_PRDT_TYPE_CODE_PRODUCT);
		List<String> prdtNoList = productRelationService.getRltnPrdtNo(criteriaForRelation);

		pageableProduct.setRowsPerPage(10);
		pageableProduct.setCondition(siteNo, chnnlNo);
		pageableProduct.setNotUseTableMapping(prdtNoList);
		pageableProduct.setUsePaging(false, null, null, null);

		Page<PdProductWrapper> test = this.productService.getDisplayRelatedProduct(pageableProduct);
		log.debug("result : {}", test);
	}

//	@Test
	void testGetProductOptionListWithStock() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<PdProduct> parameter = new Parameter<PdProduct>(request, response);
		PageableProduct<PdProduct, PdProduct> pageableProduct = new PageableProduct<PdProduct, PdProduct>(parameter);

		PdProductOptionWithStockOnlyOne test = this.productOptionService
				.getProductOptionListWithStockOnlyOne("1000000088", "270");
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 카테고리 및 rm 하위카테고리에 해당하는 상품 목록 조회
	 * @Method Name : testGetDisplayProductListAboutCategory
	 * @Date : 2019. 6. 7.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductListAboutCategory() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);
		pageableProduct.setRowsPerPage(10);

		pageableProduct.setCondition("10000", "10000");
		/**
		 * 테이블 "dp_category_product"에서, 컬럼 "ctgr_no"가 "1000000004"인 row 찾기
		 */
//		pageableProduct.setUseTableMapping("", null, null);
		pageableProduct.setConditionCategoryNo("1000000001");
		pageableProduct.setMemberInterest("MB20000064", "Y");
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);

		Page<PdProductWrapper> test = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", test);
		pageableProduct.setMemberInterest("MB20000064", "N");
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);

		test = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 오프라인 전용 상품 조회
	 * @Method Name : testGetDisplayProductOnlyOffline
	 * @Date : 2019. 6. 26.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductOnlyOffline() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);
		pageableProduct.setRowsPerPage(10);

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONLY_OFFLINE);

		Map<String, Integer> totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		Page<PdProductWrapper> test = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", test);
	}

	/**
	 * @Desc : 상품 목록 전체 갯수 조회
	 * @Method Name : testGetDisplayProductTotalCount
	 * @Date : 2019. 6. 26.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductTotalCount() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setConditionCategoryNo("1000000001");
		pageableProduct.setMemberInterest("MB20000064", "Y");
		pageableProduct.setUsePaging(false, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);

		Map<String, Integer> result = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("result : {}", result.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
	}

	/**
	 * @Desc : 런칭상품 조회 테스트
	 * @Method Name : testGetDisplayProductAboutLaunch
	 * @Date : 2019. 6. 27.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductAboutLaunch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);

		Map<String, Integer> totalCount = null;
		Page<PdProductWrapper> result = null;

//		log.debug("========== 런칭완료 상품 조회 ==========");
//		pageableProduct.setCondition("10000", "10000");
//		pageableProduct.setConditionCategoryNo("1000000001"); // 특정 카테고리 기준 하위를 검색해야 하는 경우
//		pageableProduct.setConditionLaunchProduct(PageableProduct.CONDITION_LAUNCH_PRODUCT_LAUNCHED);
//		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);
//		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
//		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
//		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
//		result = this.productService.getDisplayProductList(pageableProduct);
//		log.debug("result : {}", result);
//
//		log.debug("========== 런칭예정 상품 조회 ==========");
//		pageableProduct.setConditionCategoryNo("1000000001");
//		pageableProduct.setCondition("10000", "10002");
//		pageableProduct.setConditionLaunchProduct(PageableProduct.CONDITION_LAUNCH_PRODUCT_UPCOMMING);
//		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
//		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
//		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
//		result = this.productService.getDisplayProductList(pageableProduct);
//		log.debug("result : {}", result);

		log.debug("========== 런칭예정 및 런칭완료 상품 조회 ==========");
		pageableProduct.setNotUseTableMapping(null);
		pageableProduct.setCondition("10000", "10000"); // 사이트/채널번호 설정
		pageableProduct.setConditionLaunchProduct(PageableProduct.CONDITION_LAUNCH_PRODUCT_ALL); // 업커밍, 런칭예정 모두 조회
		pageableProduct.setConditionCategoryRoot(); // 루트카테고리기준 조회 (위 사이트/채널번호 설정으로 인해 해당 채널카테고리로 국한됨)
		pageableProduct.setConditionSellStartDate("20190101"); // 판매시작일자기준 조회
		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		result = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", result);
	}

	/**
	 * @Desc : 승인일자 기준 검색
	 * @Method Name : testGetDisplayProductUsingAprverDtm
	 * @Date : 2019. 6. 27.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductUsingAprverDtm() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);

		Map<String, Integer> totalCount = null;
		Page<PdProductWrapper> result = null;

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setConditionCategoryNo("1000000001"); // 특정 카테고리 기준 하위를 검색해야 하는 경우
		// 승인일자가 한달 이전인 상품 조회
		pageableProduct.setConditionApprovedProductWithinDateRange(PageableProduct.CONDITION_DATE_RANGE_MONTH, -1);
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);

		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		result = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", result);
	}

	/**
	 * @Desc : 카테고리이름 기준 연계 상품 조회
	 * @Method Name : testGetDisplayProductByCategoryName
	 * @Date : 2019. 6. 27.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductByCategoryName() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);

		Map<String, Integer> totalCount = null;
		Page<PdProductWrapper> result = null;

		pageableProduct.setCondition("10000", "10000");
//		pageableProduct.setConditionCategoryName("운동화"); // 상위 카테고리이름 입력
		pageableProduct.setConditionCategoryNo("1000000001");
		pageableProduct.setConditionDiscountRateGreaterThan(30); // TODO 할인율 설정 쿼리 프로시저 추가 필요
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_EVALUATION, null, null);

		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		result = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", result);
	}

	/**
	 * @Desc : 브랜드 번호에 의한 상품 조회
	 * @Method Name : testGetDisplayProductByBrandNo
	 * @Date : 2019. 7. 1.
	 * @Author : tennessee
	 * @throws Exception
	 */
//	@Test
	void testGetDisplayProductByBrandNo() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Parameter<Object> parameter = new Parameter<Object>(request, response);
		PageableProduct<Object, PdProductWrapper> pageableProduct = new PageableProduct<Object, PdProductWrapper>(
				parameter);

		Map<String, Integer> totalCount = null;
		Page<PdProductWrapper> result = null;

		pageableProduct.setCondition("10000", "10000");
		pageableProduct.setConditionBrandNo("000072"); // 브랜드번호 입력
		pageableProduct.setUsePaging(true, PageableProduct.PAGING_SORT_TYPE_LOW_PRICE, null, null);

		totalCount = this.productService.getDisplayProductTotalCount(pageableProduct);
		log.debug("totalCount : {}", totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));
		pageableProduct.setTotalCount(totalCount.get(PageableProduct.MAP_KEY_TOTAL_COUNT));

		result = this.productService.getDisplayProductList(pageableProduct);
		log.debug("result : {}", result);
	}

}
