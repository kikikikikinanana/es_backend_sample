package kr.co.shop.web.product.controller;

import java.util.HashMap;
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
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsNumber;
import kr.co.shop.common.util.UtilsObject;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.product.model.master.BdProductReview;
import kr.co.shop.web.product.model.master.BdProductReviewImage;
import kr.co.shop.web.product.service.ProductReviewService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ProductReviewController extends BaseController {

	@Autowired
	private ProductReviewService reviewService;

	@ApiOperation(value = "상품 후기 목록", notes = "상품 후기 목록 조회", httpMethod = "POST", protocols = "http", response = BdProductReview.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "bdProductReview", value = "상품 후기 정보", required = true, dataType = "object", paramType = "body") })
	@PostMapping(value = "v1.0/product/review", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getList(Parameter<Object> parameter) throws Exception {
		log.debug("상품 Q&A 목록 조회");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> param = (Map<String, Object>) parameter.get();
		int pageNum = UtilsNumber.toInt("" + param.get("pageNum"));
		int rowsPerPage = UtilsNumber.toInt("" + param.get("rowsPerPage"));

		Pageable<Object, BdProductReview> pageable = new Pageable<Object, BdProductReview>(parameter);
		pageable.setPageNum(pageNum);
		pageable.setRowsPerPage(rowsPerPage);

		// 리뷰 조회
		pageable.setBean(param);
		Page<BdProductReview> productReviewList = this.reviewService.selectProductReview(pageable);
		resultMap.put("reviews", productReviewList.getGrid());

		if (UtilsObject.isEmpty(param.get("memberNo")) && pageNum < 1) { // 첫 조회는 pageNum이 0이다.
			// 베스트 리뷰 조회
			param.put("bestYn", "Y");
			pageable.setBean(param);
			Page<BdProductReview> productBestReviewList = this.reviewService.selectProductReview(pageable);
			resultMap.put("bestReviews", productBestReviewList.getGrid());
		}

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "상품 후기 등록", notes = "입력값을 확인 후에 상품 후기 등록", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "evltScore1", value = "만족도", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "evltScore2", value = "사이즈, 편의성", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "evltScore3", value = "화면대비 색상, 실용성", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rvwContText", value = "상품후기내용", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "productReviewImage", value = "업로드사진", required = false, dataType = "object", paramType = "body"),
			@ApiImplicitParam(name = "itemCode", value = "품목코드", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/product/review/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(Parameter<BdProductReview> parameter) throws Exception {
		log.debug("상품 후기 등록");

		BdProductReview bdProductReview = parameter.get();
		Map<String, Object> resultMap = reviewService.insertReview(bdProductReview);

		if ((boolean) resultMap.get("resultCode")) {
			reviewService.insertReviewEvlt(bdProductReview);
		}

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "상품 후기 이미지 등록", notes = "입력값을 확인 후에 상품 후기 이미지 등록", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtRvwSeq", value = "상품후기순번", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "imageSeq", value = "이미지순번", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "imagePathText", value = "이미지경로", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "imageUr", value = "이미지경로", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/product/review/save-image", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveImage(Parameter<BdProductReviewImage[]> parameter) throws Exception {
		log.debug("상품 후기 이미지 등록");
		return UtilsREST.responseOk(parameter, reviewService.insertReviewImage(parameter.get()));
	}

	@ApiOperation(value = "상품 후기 평가 통계 조회", notes = "해당 상품 후기 항목 통계", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtRvwCode1", value = "상품후기평가코드1", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtRvwCode2", value = "상품후기평가코드2", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "prdtRvwCode3", value = "상품후기평가코드3", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/product/review/review-statistic-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductReviewStatisticList(Parameter<BdProductReview> parameter) throws Exception {
		log.debug("상품 후기 통계 조회");
		return UtilsREST.responseOk(parameter, reviewService.selectProductReviewStatisticList(parameter.get()));
	}

	@ApiOperation(value = "상품후기 작성가능여부 조회", notes = "상품후기 작성가능여부 조회", httpMethod = "POST", protocols = "http", response = Map.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping("/v1.0/product/review/can-write-product-review")
	public ResponseEntity<?> canWriteProductReview(Parameter<OcOrderProduct> parameter) throws Exception {
		log.debug("상품후기 작성가능여부 조회");
		return UtilsREST.responseOk(parameter, reviewService.canWriteProductReview(parameter.get()));
	}
}