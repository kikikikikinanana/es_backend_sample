package kr.co.shop.web.product.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.CartProductSearchVO;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdProductDetail;
import kr.co.shop.web.product.model.master.PdProductDetailWrapper;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.repository.master.base.BasePdProductDao;

@Mapper
public interface PdProductDao extends BasePdProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdProductDao 클래스에 구현 되어있습니다.
	 * BasePdProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PdProduct selectByPrimaryKey(PdProduct pdProduct) throws Exception;

	/**
	 * @Desc : 상품 옵션, 재고, 가격, 상품 정보 목록 조회
	 * @Method Name : selectProductListWithAll
	 * @Date : 2019. 4. 22.
	 * @Author : hsjhsj19
	 * @param optionWithAll
	 * @return
	 * @throws Exception
	 */
	public PdProductWithAll selectProductListWithAll(PdProductWithAll optionWithAll) throws Exception;

	/**
	 * @Desc : 상품 번호 기반 상세 정보 조회
	 * @Method Name : selectByPrdtNo
	 * @Date : 2019. 4. 17.
	 * @Author : tennessee
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductDetail> selectByPrdtNo(String prdtNo) throws Exception;

	/**
	 * @Desc : 상품 전시 목록 전체갯수 조회
	 * @Method Name : selectDisplayProductTotalCount
	 * @Date : 2019. 6. 26.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> selectDisplayProductTotalCount(PageableProduct<?, PdProductWrapper> pageableProduct)
			throws Exception;

	/**
	 * @Desc : 상품 전시 목록 조회
	 * @Method Name : selectDisplayProductList
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWrapper> selectDisplayProductList(PageableProduct<?, PdProductWrapper> pageableProduct)
			throws Exception;

	/**
	 * @Desc : 상품관련컬럼 전체 조회(서비스 요청용)
	 * @Method Name : selectProductWithAllList
	 * @Date : 2019. 6. 24.
	 * @Author : hsjhsj19
	 * @param cartPrdSearchVo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWithAll> selectProductWithAllList(CartProductSearchVO cartPrdSearchVo) throws Exception;

	/**
	 * @Desc : 상품 주요정보 조회
	 * @Method Name : selectDisplayProduct
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public PdProductWrapper selectDisplayProduct(PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct)
			throws Exception;

	/**
	 * @Desc : 상품 상세정보 조회
	 * @Method Name : selectDisplayProductDetail
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public PdProductDetailWrapper selectDisplayProductDetail(
			PageableProduct<PdProductDetailWrapper, PdProductDetailWrapper> pageableProduct) throws Exception;

	/**
	 * @Desc : 상품 상세 페이지 접근 검사항목 조회
	 * @Method Name : selectDisplayProductAccessCheckInfo
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public PdProductWrapper selectDisplayProductAccessCheckInfo(
			PageableProduct<PdProduct, PdProductWrapper> pageableProduct) throws Exception;

	/**
	 * @Desc : 연계상품 조회
	 * @Method Name : selectDisplayRelatedProduct
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWrapper> selectDisplayRelatedProduct(
			PageableProduct<PdProductWrapper, PdProductWrapper> pageableProduct) throws Exception;

	/**
	 * @Desc : 배송비 상품 조회 쿼리
	 * @Method Name : selectProductDlvy
	 * @Date : 2019. 7. 4.
	 * @Author : KTH
	 * @param promoNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProduct> selectProductDlvy(String prdtTypeCode) throws Exception;

}
