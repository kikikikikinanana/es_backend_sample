package kr.co.shop.web.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsCollection;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductDetailWrapper;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.model.master.PdRelationProduct;
import kr.co.shop.web.product.service.ProductRelationService;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.model.master.SySiteApplySns;
import kr.co.shop.web.system.service.CommonCodeService;
import kr.co.shop.web.system.service.SiteService;
import kr.co.shop.web.vendor.model.master.VdVendorDeliveryGuide;
import kr.co.shop.web.vendor.service.VendorProductAllNoticeService;
import kr.co.shop.web.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRelationService productRelationService;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private VendorProductAllNoticeService vendorProduAllNoticeService;

	@Autowired
	private SiteService siteService;

	/**
	 * @Desc : 상품 페이지 접근정보 조회
	 * @Method Name : getProductAccessCheckInfo
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 접근점검정보 조회", notes = "상품 접근점검정보 조회", httpMethod = "GET", protocols = "http", response = PdProduct.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "deviceCode", value = "장치코드", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query", defaultValue = "2000000045") })
	@GetMapping(value = "v1.0/product/product/check/access", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductAccessCheckInfo(Parameter<String> parameter) throws Exception {
		PageableProduct<PdProduct, PdProductWrapper> pageableProduct = new PageableProduct<PdProduct, PdProductWrapper>(
				new Parameter<PdProduct>(parameter.getRequest(), parameter.getResponse()));

		PdProductWrapper dummyBean = new PdProductWrapper();
		dummyBean.setPrdtNo(parameter.getString("prdtNo"));
		pageableProduct.setBean(dummyBean);
		pageableProduct.setCondition(parameter.getString("siteNo"), parameter.getString("chnnlNo"));
		pageableProduct.setDeviceCode(parameter.getString("deviceCode"));
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONOFFLINE, CommonCode.PRDT_TYPE_CODE_ONLY_ONLINE,
				CommonCode.PRDT_TYPE_CODE_ONLY_OFFLINE); // 온라인전용, 온/오프라인, 오프라인전용 상품 조회
		PdProductWrapper product = this.productService.getDisplayProductAccessCheckInfo(pageableProduct);

		return UtilsREST.responseOk(parameter, product);
	}

	/**
	 * @Desc : 상품 주요정보 조회
	 * @Method Name : getProduct
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 주요정보 조회", notes = "상품 상세 정보 조회", httpMethod = "GET", protocols = "http", response = PdProductDetailWrapper.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query", defaultValue = "2000000045"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query", defaultValue = "MB20000064"),
			@ApiImplicitParam(name = "deviceCode", value = "장치코드", required = true, dataType = "string", paramType = "query", defaultValue = CommonCode.DEVICE_PC) })
	@GetMapping(value = "v1.0/product/product", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProduct(Parameter<String> parameter) throws Exception {
		PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct = new PageableProduct<PdProductWrapper, PdProductWrapper>(
				new Parameter<PdProductWrapper>(parameter.getRequest(), parameter.getResponse()));

		PdProductWrapper dummyBean = new PdProductWrapper();
		dummyBean.setPrdtNo(parameter.getString("prdtNo"));
		pageableProduct.setBean(dummyBean);
		pageableProduct.setMemberInterest(parameter.getString("memberNo"), Const.BOOLEAN_FALSE);
//		pageableProduct.setDetailView(false);
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONOFFLINE, CommonCode.PRDT_TYPE_CODE_ONLY_ONLINE,
				CommonCode.PRDT_TYPE_CODE_ONLY_OFFLINE); // 온라인전용, 온/오프라인, 오프라인전용 상품 조회
		pageableProduct.setType(PageableProduct.TYPE_MAJOR);
		pageableProduct.setDeviceCode(parameter.getString("deviceCode")); // draw이벤트 확인용
		pageableProduct.setCondition(parameter.getString("siteNo"), parameter.getString("chnnlNo"));
		pageableProduct.setNotUseTableMapping(null);
		pageableProduct.setUsePaging(false, null, null, null);
		pageableProduct.setRowsPerPage(1);

		return UtilsREST.responseOk(parameter, this.productService.getDisplayProduct(pageableProduct));
	}

	/**
	 * @Desc : 상품 상세정보 조회
	 * @Method Name : getProductDetail
	 * @Date : 2019. 5. 20.
	 * @Author : tennessee
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 상세정보 조회", notes = "상품 상세 정보 조회", httpMethod = "GET", protocols = "http", response = PdProductDetailWrapper.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query", defaultValue = "2000000045"),
			@ApiImplicitParam(name = "deviceCode", value = "장치코드", required = true, dataType = "string", paramType = "query", defaultValue = "10000") })
	@GetMapping(value = "v1.0/product/product/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductDetail(Parameter<String> parameter) throws Exception {
		PageableProduct<PdProductDetailWrapper, PdProductDetailWrapper> pageableProduct = new PageableProduct<PdProductDetailWrapper, PdProductDetailWrapper>(
				new Parameter<PdProductDetailWrapper>(parameter.getRequest(), parameter.getResponse()));

		PdProductDetailWrapper dummyBean = new PdProductDetailWrapper();
		dummyBean.setPrdtNo(parameter.getString("prdtNo"));
		pageableProduct.setBean(dummyBean);
//		pageableProduct.setDetailView(true);
		pageableProduct.setPrdtTypeCodes(CommonCode.PRDT_TYPE_CODE_ONOFFLINE, CommonCode.PRDT_TYPE_CODE_ONLY_ONLINE,
				CommonCode.PRDT_TYPE_CODE_ONLY_OFFLINE); // 온라인전용, 온/오프라인, 오프라인전용 상품 조회
		pageableProduct.setType(PageableProduct.TYPE_DETAIL);
		pageableProduct.setDeviceCode(parameter.getString("deviceCode"));
		pageableProduct.setCondition(parameter.getString("siteNo"), parameter.getString("chnnlNo"));
		pageableProduct.setNotUseTableMapping(null);
		pageableProduct.setUsePaging(false, null, null, null);
		pageableProduct.setRowsPerPage(1);

		PdProductDetailWrapper detail = this.productService.getDisplayProductDetail(pageableProduct);
		detail.setAllNotice(
				this.vendorProduAllNoticeService.getVendorProductAllNoticeList(detail.getDetail().getPrdtNo()));
		return UtilsREST.responseOk(parameter, detail);
	}

	/**
	 * @Desc : 상품 색상연계상품 조회
	 * @Method Name : getRelatedProductByColor
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 색상연계 상품 조회", notes = "상품 상세 정보 조회", httpMethod = "GET", protocols = "http", response = PdProductWrapper.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query", defaultValue = "1000000061") })
	@GetMapping(value = "v1.0/product/product/related/color", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRelatedProductByColor(Parameter<String> parameter) throws Exception {
		PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct = new PageableProduct<PdProductWrapper, PdProductWrapper>(
				new Parameter<PdProductWrapper>(parameter.getRequest(), parameter.getResponse()));

		PdRelationProduct criteriaForRelation = new PdRelationProduct();
		criteriaForRelation.setPrdtNo(parameter.getString("prdtNo"));
		criteriaForRelation.setRltnPrdtTypeCode(CommonCode.RLTN_PRDT_TYPE_CODE_PRODUCT);
		List<String> prdtNoList = productRelationService.getRltnPrdtNo(criteriaForRelation);

		List<PdProductWrapper> result = new ArrayList<PdProductWrapper>();

		if (UtilsCollection.isNotEmpty(prdtNoList)) {
			PdProductWrapper dummyBean = new PdProductWrapper();
			dummyBean.setPrdtNo(parameter.getString("prdtNo"));
			pageableProduct.setBean(dummyBean);
			pageableProduct.setType(PageableProduct.TYPE_LIST_RELATED_COLOR);
			pageableProduct.setCondition(parameter.getString("siteNo"), parameter.getString("chnnlNo"));
			pageableProduct.setNotUseTableMapping(prdtNoList);
			pageableProduct.setUsePaging(false, null, null, null);
			pageableProduct.setRowsPerPage(999);

			result = this.productService.getDisplayRelatedProduct(pageableProduct).getContent();
		}

		return UtilsREST.responseOk(parameter, result);
	}

	@ApiOperation(value = "관련상품 조회", notes = "상품번호와 연계된 관련상품을 조회합니다.", httpMethod = "GET", protocols = "http", response = PdProductWrapper.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query", defaultValue = "1000000061") })
	@GetMapping(value = "v1.0/product/product/related/goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRelatedProductByGoods(Parameter<String> parameter) throws Exception {
		PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct = new PageableProduct<PdProductWrapper, PdProductWrapper>(
				new Parameter<PdProductWrapper>(parameter.getRequest(), parameter.getResponse()));

		PdRelationProduct criteriaForRelation = new PdRelationProduct();
		criteriaForRelation.setPrdtNo(parameter.getString("prdtNo"));
		criteriaForRelation.setRltnPrdtTypeCode(CommonCode.RLTN_PRDT_TYPE_CODE_GOODS);
		List<String> prdtNoList = productRelationService.getRltnPrdtNo(criteriaForRelation);

		List<PdProductWrapper> result = new ArrayList<PdProductWrapper>();

		if (UtilsCollection.isNotEmpty(prdtNoList)) {
			PdProductWrapper dummyBean = new PdProductWrapper();
			dummyBean.setPrdtNo(parameter.getString("prdtNo"));
			pageableProduct.setBean(dummyBean);
			pageableProduct.setType(PageableProduct.TYPE_LIST_RELATED_GOODS);
			pageableProduct.setCondition(parameter.getString("siteNo"), parameter.getString("chnnlNo"));
			pageableProduct.setNotUseTableMapping(prdtNoList);
			pageableProduct.setUsePaging(false, null, null, null);
			pageableProduct.setRowsPerPage(999);

			result = this.productService.getDisplayRelatedProduct(pageableProduct).getContent();
		}

		return UtilsREST.responseOk(parameter, result);
	}

	@ApiOperation(value = "코드 목록 조회", notes = "상품 필요 코드 목록 조회(SyCodeDetail 객체)", httpMethod = "POST", protocols = "http", response = SyCodeDetail.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "codeField", value = "상세코드 객체", required = true, dataType = "object", paramType = "body") })
	@PostMapping(value = "v1.0/product/product/get-code-detail-list-object", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCodeDetailListObject(Parameter<SyCodeDetail> parameter) throws Exception {
		return UtilsREST.responseOk(parameter, this.commonCodeService.getUseCodeByAddInfo1(parameter.get()));
	}

	@ApiOperation(value = "코드 목록 조회", notes = "상품 필요 코드 목록 조회(String 배열 목록)", httpMethod = "POST", protocols = "http", response = SyCodeDetail.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "codeField", value = "상세코드 스트링 배열", required = true, dataType = "string array", paramType = "body") })
	@PostMapping(value = "v1.0/product/product/get-code-detail-list-array", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCodeDetailListArray(Parameter<String[]> parameter) throws Exception {
		/*
		 * OcCart ocCart = new OcCart(); ocCart.setLogin(true);
		 * ocCart.setSessionId("AF270F37345384B326B03B737C000C49PXXX");
		 * ocCart.setMemberNo("MB00000002"); ocCart.setMemberTypeCode("10000");
		 * ocCart.setSiteNo("10000"); ocCart.setDeviceCode("10000");
		 * ocCart.setChnnlNo("10000"); ocCart.setCartType(Const.CART_TYPE_NOMAL);
		 * 
		 * ocCart.setCartSeq(65L); ocCart.setPrdtNo("1000004527");
		 * ocCart.setPrdtOptnNo("260"); ocCart.setOrderQty(4); ocCart.setEmpYn("Y");
		 * 
		 * List<OcCart> carts = new ArrayList<OcCart>(); carts.add(ocCart);
		 * 
		 * List<PdProductWithAll> list = productService.getProductListWithAll(carts);
		 */
		/*
		 * UpperCoupon list2 = couponService.getCanDownloadCouponList("MB10016051",
		 * "","1000000150", "230", 0, 5000000,"", "");
		 */
		/*
		 * List<PdProductWithAll> list = new ArrayList<PdProductWithAll>();
		 * PdProductWithAll pd = new PdProductWithAll(); pd.setPrdtNo("1000004527");
		 * pd.setPrdtOptnNo("250"); pd.setOrderQty(1); pd.setPromoNo("2000000026");
		 * pd.setCpnNo("1000001598"); pd.setPromoDscntAmt(28000);
		 * pd.setCpnDscntAmt(29000); list.add(pd);
		 * 
		 * String result = promotionService.validatePrdtOptnPromoAndCpn(list);
		 */
		return UtilsREST.responseOk(parameter, this.commonCodeService.getUseCodeByGroup(parameter.get()));
	}

	@ApiOperation(value = "상품안내 가이드 조회", notes = "상품상세 배송안내가이드 목록 조회", httpMethod = "POST", protocols = "http", response = VdVendorDeliveryGuide.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "vndrNo", value = "업체번호", required = true, dataType = "String", paramType = "body") })
	@PostMapping(value = "v1.0/product/product/guide/get-delivery-guide-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDeliveryGuideList(Parameter<PdProduct> parameter) throws Exception {
		PdProduct product = parameter.get();
		return UtilsREST.responseOk(parameter, vendorService.getVendorDeliveryGuide(product.getVndrNo()));
	}

	/**
	 * @Desc : 사이트에서 사용중인 소셜 서비스 조회
	 * @Method Name : getAvailableSocialService
	 * @Date : 2019. 7. 1.
	 * @Author : tennessee
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "사이트에서 사용중인 소셜 서비스 조회", notes = "사이트에서 사용중인 소셜 서비스 조회", httpMethod = "GET", protocols = "http", response = SySiteApplySns.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "String", paramType = "query") })
	@GetMapping(value = "v1.0/product/product/service/social", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAvailableSocialService(Parameter<?> parameter) throws Exception {
		return UtilsREST.responseOk(parameter, siteService.getUseSiteApplySnsList(parameter.getString("siteNo")));
	}

}
