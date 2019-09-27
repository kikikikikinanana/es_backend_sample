package kr.co.shop.web.product.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.product.model.master.BdProductReview;
import kr.co.shop.web.product.model.master.PdRelationProduct;
import kr.co.shop.web.product.service.ProductRelationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ProductRelationController extends BaseController {

	@Autowired
	private ProductRelationService relationService;

	@ApiOperation(value = "연계 상품  목록 조회", notes = "연관,베스트,신상 연관상품 각 30건 중에서 15건만 임의 선택해서 돌려준다.", httpMethod = "POST", protocols = "http", response = BdProductReview.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "String", paramType = "body"),
			@ApiImplicitParam(name = "brandNo", value = "브랜드번호", required = true, dataType = "String", paramType = "body") })
	@PostMapping(value = "v1.0/product/relation/get-relation-best-new-product-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRBNList(Parameter<PdRelationProduct> parameter) throws Exception {
		log.debug("연계 상품  목록 조회");
		Map<String, Object> resultMap = this.relationService.selectRelationProductRBNList(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}
}
