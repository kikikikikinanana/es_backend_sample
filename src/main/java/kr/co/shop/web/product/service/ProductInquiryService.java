package kr.co.shop.web.product.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.constant.Const;
import kr.co.shop.web.product.model.master.BdProductInquiry;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.repository.master.BdProductInquiryDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductInquiryService {

	@Autowired
	private BdProductInquiryDao productInquiryDao;

	@Autowired
	private ProductService productService;

	/**
	 * @Desc : 상품 문의 목록
	 * @Method Name : selectProductInquiry
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 */
	public Page<BdProductInquiry> selectProductInquiry(Pageable<Object, BdProductInquiry> pageable) throws Exception {
		Integer count = this.productInquiryDao.selectProductInquiryCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.setContent(this.productInquiryDao.selectProductInquiry(pageable));
		}

		return pageable.getPage();
	}

	public Map<String, Object> crudProductInquiry(BdProductInquiry bdProductInquiry) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean resultType = false;
		String resultMsg = "처리 중 오류가 발생했습니다.";
		if (Const.CRUD_C.equals(bdProductInquiry.getStatus())) {
			PdProduct product = productService.getProduct(bdProductInquiry.getPrdtNo());
			bdProductInquiry.setVndrNo(product.getVndrNo());
			productInquiryDao.insertProductInquiry(bdProductInquiry);
			resultType = true;
			resultMsg = "Q&A가 등록되었습니다. 남기신 상품 Q&A내역은 마이페이지에서도 확인 가능합니다.";
		} else if (Const.CRUD_U.equals(bdProductInquiry.getStatus())) {
			productInquiryDao.updateProductInquiry(bdProductInquiry);
			resultType = true;
			resultMsg = "Q&A가 수정되었습니다.";
		} else if (Const.CRUD_D.equals(bdProductInquiry.getStatus())) {
			productInquiryDao.deleteProductInquiry(bdProductInquiry);
			resultType = true;
			resultMsg = "Q&A가 삭제되었습니다.";
		}
		resultMap.put("resultType", resultType);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

}
