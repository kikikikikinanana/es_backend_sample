package kr.co.shop.web.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsObject;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.service.OrderOtherPartService;
import kr.co.shop.web.product.model.master.BdProductReview;
import kr.co.shop.web.product.model.master.BdProductReviewEvlt;
import kr.co.shop.web.product.model.master.BdProductReviewImage;
import kr.co.shop.web.product.repository.master.BdProductReviewDao;
import kr.co.shop.web.product.repository.master.BdProductReviewEvltDao;
import kr.co.shop.web.product.repository.master.BdProductReviewImageDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductReviewService {

	@Autowired
	private BdProductReviewDao productReviewDao;

	@Autowired
	private BdProductReviewImageDao productReviewImageDao;

	@Autowired
	private BdProductReviewEvltDao productReviewEvltDao;

	@Autowired
	private OrderOtherPartService orderOtherPartService;

	/**
	 * @Desc : 상품 후기 등록
	 * @Method Name : insertReview
	 * @Date : 2019. 4. 30.
	 * @Author : hsjhsj19
	 * @param bdProductReview
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> insertReview(BdProductReview bdProductReview) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		OcOrderProduct ocOrderProduct = new OcOrderProduct();
		ocOrderProduct.setMemberNo(bdProductReview.getMemberNo());
		ocOrderProduct.setPrdtNo(bdProductReview.getPrdtNo());
		ocOrderProduct.setPrdtOptnNo(bdProductReview.getPrdtOptnNo());
		ocOrderProduct.setSafeKey(bdProductReview.getSafeKey());

		ocOrderProduct.setMemberNo("MB00000007");
		ocOrderProduct.setPrdtNo("1000003");
		ocOrderProduct.setPrdtOptnNo("101");

		BdProductReview selectProductReview = orderOtherPartService.getOrderProductQnaTarget(ocOrderProduct);
		if (selectProductReview == null) {
			throw new Exception("주문이력이 없습니다.");
		} else {
			bdProductReview.setOnlnBuyYn(selectProductReview.getOnlnBuyYn());
			if ("Y".equals(selectProductReview.getOnlnBuyYn())) {
				bdProductReview.setOrderNo(selectProductReview.getOrderNo());
				bdProductReview.setOrderPrdtSeq(selectProductReview.getOrderPrdtSeq());
			} else {
				bdProductReview.setSafeKey(selectProductReview.getSafeKey());
				bdProductReview.setSafeKeySeq(selectProductReview.getSafeKeySeq());
				bdProductReview.setSaleDate(selectProductReview.getSaleDate());
				bdProductReview.setStoreCd(selectProductReview.getStoreCd());
				bdProductReview.setPosNo(selectProductReview.getPosNo());
				bdProductReview.setDealNo(selectProductReview.getDealNo());
				bdProductReview.setProductCd(selectProductReview.getProductCd());
				bdProductReview.setSizeCd(selectProductReview.getSizeCd());
			}
		}

		int cnt = productReviewDao.insert(bdProductReview);

		if (0 < cnt) {
			resultMap.put("resultCode", true);
			resultMap.put("resultMsg", "상품평이 등록되었습니다. 담당자 확인 후 포인트가 지급됩니다.");
			resultMap.put("prdtRvwSeq", bdProductReview.getPrdtRvwSeq());
		} else {
			resultMap.put("resultCode", true);
			resultMap.put("resultMsg", "처리 중 오류가 발생했습니다.");
		}

		return resultMap;
	}

	/**
	 * @Desc : 상품 후기 목록
	 * @Method Name : selectProductReview
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Page<BdProductReview> selectProductReview(Pageable<Object, BdProductReview> pageable) throws Exception {
		Integer count = this.productReviewDao.selectProductReviewCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.setContent(this.productReviewDao.selectProductReview(pageable));
		}

		return pageable.getPage();
	}

	/**
	 * @Desc : 상품 후기 통계 조회
	 * @Method Name : selectProductReviewStatisticList
	 * @Date : 2019. 5. 7.
	 * @Author : hsjhsj19
	 * @param productReview
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectProductReviewStatisticList(BdProductReview productReview) throws Exception {
		return productReviewDao.selectProductReviewPercentageList(productReview);
	}

	/**
	 * @Desc : 상품후기 이미지 등록
	 * @Method Name : insertReviewImage
	 * @Date : 2019. 6. 4.
	 * @Author : hsjhsj19
	 * @param bdProductReviewImages
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> insertReviewImage(BdProductReviewImage[] bdProductReviewImages) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int cnt = 0;
		for (BdProductReviewImage reviewImage : bdProductReviewImages) {
			if (UtilsObject.isNotEmpty(reviewImage.getPrdtRvwSeq())) {
				cnt += productReviewImageDao.insert(reviewImage);
			}
		}

		if (0 < cnt) {
			resultMap.put("resultCode", true);
		} else {
			resultMap.put("resultCode", false);
		}

		return resultMap;
	}

	/**
	 * @Desc : 상품후기 평가 등록
	 * @Method Name : insertReviewEvlt
	 * @Date : 2019. 6. 4.
	 * @Author : hsjhsj19
	 * @param bdProductReview
	 * @throws Exception
	 */
	public void insertReviewEvlt(BdProductReview bdProductReview) throws Exception {
		BdProductReviewEvlt productReviewEvlt = new BdProductReviewEvlt();
		productReviewEvlt.setPrdtRvwSeq(bdProductReview.getPrdtRvwSeq());

		productReviewEvlt.setPrdtRvwCode(bdProductReview.getPrdtRvwCode1());
		productReviewEvlt.setEvltScore(bdProductReview.getEvltScore1());
		productReviewEvltDao.insert(productReviewEvlt);

		productReviewEvlt.setPrdtRvwCode(bdProductReview.getPrdtRvwCode2());
		productReviewEvlt.setEvltScore(bdProductReview.getEvltScore2());
		productReviewEvltDao.insert(productReviewEvlt);

		productReviewEvlt.setPrdtRvwCode(bdProductReview.getPrdtRvwCode3());
		productReviewEvlt.setEvltScore(bdProductReview.getEvltScore3());
		productReviewEvltDao.insert(productReviewEvlt);
	}

	public OcOrderProduct canWriteProductReview(OcOrderProduct orderProduct) throws Exception {
		return orderOtherPartService.getOrderProductBuyConfirm(orderProduct);
	}

}
