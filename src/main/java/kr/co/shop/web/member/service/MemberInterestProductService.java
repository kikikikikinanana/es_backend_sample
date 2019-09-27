package kr.co.shop.web.member.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.util.UtilsObject;
import kr.co.shop.constant.Const;
import kr.co.shop.web.cart.vo.OcCartInfo;
import kr.co.shop.web.member.model.master.MbMemberInterestProduct;
import kr.co.shop.web.member.repository.master.MbMemberInterestProductDao;
import kr.co.shop.web.product.model.master.MbMemberInterestProductWrapper;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberInterestProductService {

	@Autowired
	private ProductService productService;

	@Autowired
	private MbMemberInterestProductDao memberInterestProductDao;

	/**
	 * @Desc : 회원 관심상품 목록 삭제
	 * @Method Name : deleteInterestProductList
	 * @Date : 2019. 5. 27.
	 * @Author : hsjhsj19
	 * @param interestProducts
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteInterestProductList(MbMemberInterestProduct[] interestProducts) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (MbMemberInterestProduct interestProduct : interestProducts) {
			this.memberInterestProductDao.deleteInterestProduct(interestProduct);
		}
		resultMap.put("resultType", "true");
		return resultMap;
	}

	/**
	 * @Desc : 회원 관심상품 등록/삭제
	 * @Method Name : crudInterestProduct
	 * @Date : 2019. 5. 27.
	 * @Author : hsjhsj19
	 * @param mbMemberInterestProduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> crudInterestProduct(MbMemberInterestProduct interestProduct) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultType", "false");

		PdProduct product = productService.getProduct(interestProduct.getPrdtNo());
		if (product != null) {
			interestProduct.setCtgrNo(product.getStdrCtgrNo()); // 현재는 기준 카테고리
			if (Const.BOOLEAN_TRUE.equals(interestProduct.getWrhsAlertReqYn())) {
				resultMap = this.requestReWarehousing(interestProduct); // 알림상품
			} else {
				resultMap = this.requestWishProduct(interestProduct); // 찜한상품
			}
		} else {
			resultMap.put("resultMsg", "상품번호가 등록되지 않았습니다.");
		}
		return resultMap;
	}

	/**
	 * @Desc : 찜한상품 문구 적용
	 * @Method Name : requestWishProduct
	 * @Date : 2019. 6. 26.
	 * @Author : hsjhsj19
	 * @param interestProduct
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> requestWishProduct(MbMemberInterestProduct interestProduct) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MbMemberInterestProduct> interestProductList = this.memberInterestProductDao
				.selectMemberInterestProductList(interestProduct);
		if (UtilsObject.isEmpty(interestProductList)) {
			// - 보관기한 없이, 100개 상품 제한
			Integer count = this.memberInterestProductDao.selectMemberInterestProductCount(interestProduct);
			if (count < 100) {
				this.memberInterestProductDao.insertWithPrimaryKey(interestProduct);
				resultMap.put("resultType", "true");
				resultMap.put("resultMsg", "찜한상품에 담겼습니다.");
			} else {
				resultMap.put("resultMsg", "찜한상품을 100개 이상 담을 수 없습니다.");
			}

		} else {
			this.memberInterestProductDao.deleteInterestProduct(interestProduct);
			resultMap.put("resultType", "true");
			resultMap.put("resultMsg", "찜한상품이 해제되었습니다.");
		}

		return resultMap;
	}

	/**
	 * @Desc : 재입고 신청 문구 적용
	 * @Method Name : requestReWarehousing
	 * @Date : 2019. 5. 27.
	 * @Author : hsjhsj19
	 * @param interestProduct
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> requestReWarehousing(MbMemberInterestProduct interestProduct) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MbMemberInterestProduct> interestProductList = this.memberInterestProductDao
				.selectMemberInterestProductList(interestProduct);
		if (UtilsObject.isEmpty(interestProductList)) {
			this.memberInterestProductDao.insertWithPrimaryKey(interestProduct);
			resultMap.put("resultType", "true");
			resultMap.put("resultMsg", "재입고 알림 서비스 신청이 완료되었습니다.");
		} else {
			resultMap.put("resultType", "false");
			resultMap.put("resultMsg", "이미 신청하신 옵션입니다. 마이페이지 > 쇼핑수첩 > 알림내역에서 확인가능합니다.");
		}

		return resultMap;
	}

	/**
	 * @Desc : 회원관심상품 목록 조회
	 * @Method Name : getOnlyMemberInterestProductList
	 * @Date : 2019. 5. 28.
	 * @Author : hsjhsj19
	 * @param mbMemberInterestProduct
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberInterestProduct> getOnlyMemberInterestProductList(
			MbMemberInterestProduct mbMemberInterestProduct) throws Exception {
		return this.memberInterestProductDao.selectMemberInterestProductList(mbMemberInterestProduct);
	}

	/**
	 * @Desc : 회원 관심상품 여부 조회
	 * @Method Name : getInterestProductYn
	 * @Date : 2019. 6. 5.
	 * @Author : 이가영
	 * @param interestProduct
	 * @return
	 */
	public Map<String, String> getInterestProductYn(MbMemberInterestProduct interestProduct) throws Exception {

		Map<String, String> result = new LinkedHashMap<>();

		List<MbMemberInterestProduct> list = this.memberInterestProductDao
				.selectMemberInterestProductYnList(interestProduct);

		for (MbMemberInterestProduct item : list) {

			result.put(item.getPrdtNo(), item.getInterestYn());
		}

		return result;
	}

	/**
	 * @Desc :회원 관심상품 TOP5 목록 조회
	 * @Method Name : getWishProdutListTopFive
	 * @Date : 2019. 6. 15.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public List<OcCartInfo> getWishProdutListTopFive(OcCartInfo params) throws Exception {
		List<OcCartInfo> prdtList = memberInterestProductDao.getWishProdutListTopFive(params);

		return prdtList;
	}

	/**
	 * @Desc : 전시상품조회
	 * @Method Name : getDisplayInterestProductList
	 * @Date : 2019. 6. 25.
	 * @Author : hsjhsj19
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public Page<MbMemberInterestProductWrapper> getDisplayInterestProductList(
			PageableProduct<MbMemberInterestProduct, MbMemberInterestProductWrapper> pageableProduct) throws Exception {
		Integer count = (Integer) this.memberInterestProductDao.selectDisplayProductTotalCount(pageableProduct)
				.get(PageableProduct.MAP_KEY_TOTAL_COUNT);
		pageableProduct.setTotalCount(count);

		if (count > 0) {
			pageableProduct.setContent(this.memberInterestProductDao.selectDisplayInterestProductList(pageableProduct));
		}

		log.debug("productList : [{}]", pageableProduct.getContent());
		return pageableProduct.getPage();
	}

}
