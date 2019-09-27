package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderProductDao;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderProduct;
import kr.co.shop.web.product.model.master.PdProductOptionStock;

@Mapper
public interface OcOrderProductDao extends BaseOcOrderProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderProductDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcOrderProduct selectByPrimaryKey(OcOrderProduct ocOrderProduct) throws Exception;

	/**
	 * @Desc : 구매확정상태의 주문 상품 목록 갯수 조회
	 * @Method Name : selectOrderProductListCount
	 * @Date : 2019. 3. 26.
	 * @Author : 이강수
	 * @param Pageable<OcOrderProduct, OcOrderProduct>
	 * @return int
	 * @throws Exception
	 */
	public int selectOrderProductListCount(Pageable<OcOrderProduct, OcOrderProduct> pageable) throws Exception;

	/**
	 * @Desc : 주문 상품 목록 조회
	 * @Method Name : selectOrderProductList
	 * @Date : 2019. 3. 26.
	 * @Author : 이강수
	 * @param Pageable<OcOrderProduct, OcOrderProduct>
	 * @return List<OcOrderProduct>
	 * @throws Exception
	 */
	public List<OcOrderProduct> selectOrderProductList(Pageable<OcOrderProduct, OcOrderProduct> pageable)
			throws Exception;

	/**
	 * @Desc : 원 주문번호 기준 주문상품 목록 조회
	 * @Method Name : selectOrgOrderProductList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderProduct> selectOrgOrderProductList(OcOrderProduct ocOrderProduct) throws Exception;

	/**
	 * @Desc : 클레임신청 대상 주문의 주문상품목록 조회
	 * @Method Name : selectClaimRequestOrderProductList
	 * @Date : 2019. 4. 16.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return List<OcOrderProduct>
	 * @throws Exception
	 */
	public List<OcOrderProduct> selectClaimRequestOrderProductList(OcOrder ocOrder) throws Exception;

	/**
	 * @Desc : 주문번호로 해당 주문의 주문상품이 속한 업체번호 목록을 조회
	 * @Method Name : selectVndrNoGroupByOrderNo
	 * @Date : 2019. 4. 23.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return List<OcOrder>
	 * @throws Exception
	 */
	public List<OcOrder> selectVndrNoGroupByOrderNo(OcOrder ocOrder) throws Exception;

	/**
	 * @Desc : 1:1 상담내역 수정 페이지에서 주문상품 조회
	 * @Method Name : selectOrderPrdtInMemberCnsl
	 * @Date : 2019. 5. 24.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return OcOrderProduct
	 * @throws Exception
	 */
	public OcOrderProduct selectOrderPrdtInMemberCnsl(OcOrderProduct ocOrderProduct) throws Exception;

	/**
	 * 
	 * @Desc :마이페이지 메인 (주문/배송 현황 조회)
	 * @Method Name : selectOrderProductByOrderNo
	 * @Date : 2019. 5. 23.
	 * @Author : flychani@3top.co.kr
	 * @param orderProduct
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderProduct> selectOrderProductByOrderNo(OcOrderProduct orderProduct) throws Exception;

	/**
	 * 
	 * @Desc :마이페이지 메인 (주문/배송 현황 조회) - 주문상세에서 사용 전체
	 * @Method Name : selectOrderProductAllByOrder
	 * @Date : 2019. 5. 23.
	 * @Author : flychani@3top.co.kr
	 * @param orderProduct
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderProduct> selectOrderProductAllByOrder(OcOrderProduct orderProduct) throws Exception;

	/**
	 * @Desc : 주문 상품 insert
	 * @Method Name : insertProductList
	 * @Date : 2019. 5. 27.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderProduct
	 * @throws Exception
	 */
	public void insertProductList(List<OrderProduct> orderProduct) throws Exception;

	/**
	 * @Desc : 주문 상품 사은품 insert
	 * @Method Name : insertGiftProduct
	 * @Date : 2019. 5. 30.
	 * @Author : ljyoung@3top.co.kr
	 * @param giftPrdtList
	 */
	public void insertGiftProduct(List<OrderProduct> giftPrdtList) throws Exception;

	/**
	 * @Desc : 주문 상품 배송비 insert
	 * @Method Name : insertDlvyProduct
	 * @Date : 2019. 5. 30.
	 * @Author : ljyoung@3top.co.kr
	 * @param giftPrdtList
	 * @throws Exception
	 */
	public void insertDlvyProduct(List<OrderProduct> giftPrdtList) throws Exception;

	/**
	 * @Desc : 주문 상품 재고 수량 변경
	 * @Method Name : updateProductStock
	 * @Date : 2019. 5. 31.
	 * @Author : ljyoung@3top.co.kr
	 * @param prdtStock
	 * @throws Exception
	 */
	public void updateProductStock(PdProductOptionStock prdtStock) throws Exception;

	/**
	 * @Desc : 주문 상품 상태 일괄 update
	 * @Method Name : updateProductStat
	 * @Date : 2019. 6. 4.
	 * @Author : ljyoung@3top.co.kr
	 * @param order
	 * @throws Exception
	 */
	public void updateOrderStat(OcOrder order) throws Exception;

	/**
	 * 
	 * @Desc :배송상태 구매확정으로 변경 처리
	 * @Method Name : updateOrderConfirm
	 * @Date : 2019. 6. 12.
	 * @Author : NKB
	 * @param order
	 * @throws Exception
	 */
	public int updateOrderConfirm(OcOrderProduct ocOrderProduct) throws Exception;

	/**
	 * @Desc : 주문상품 등록
	 * @Method Name : insertOrderProduct
	 * @Date : 2019. 6. 16.
	 * @Author : KTH
	 * @param ocOrderProduct
	 * @return
	 * @throws Exception
	 */
	public int insertOrderProduct(OcOrderProduct ocOrderProduct) throws Exception;

	/**
	 * @Desc : 다족구매 프로모션 재조정으로 인한 최근 금액변경 상품 추출
	 * @Method Name : selectRecentMultiBuyReApplyProduct
	 * @Date : 2019. 7. 10.
	 * @Author : KTH
	 * @param ocOrderProduct
	 * @return
	 * @throws Exception
	 */
	public OcOrderProduct selectRecentMultiBuyReApplyProduct(OcOrderProduct ocOrderProduct) throws Exception;

	/**
	 * @Desc : 리오더 주문상품 목록
	 * @Method Name : selectReOrderProductList
	 * @Date : 2019. 7. 15.
	 * @Author : KTH
	 * @param orderProduct
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderProduct> selectReOrderProductList(OcOrderProduct orderProduct) throws Exception;

	/**
	 * @Desc : 주문상품 update
	 * @Method Name : updateOrderProduct
	 * @Date : 2019. 6. 16.
	 * @Author : KTH
	 * @param ocOrderProduct
	 * @return
	 * @throws Exception
	 */
	public int updateOrderProduct(OcOrderProduct ocOrderProduct) throws Exception;

	public int updateOrderProductOptionChange(OcOrderProduct ocOrderProduct) throws Exception;

}
