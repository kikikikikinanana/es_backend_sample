package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.mypage.vo.OrderStatVO;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderDao;
import kr.co.shop.web.order.vo.OrderFormVo.DeleveryAddress;

@Mapper
public interface OcOrderDao extends BaseOcOrderDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderDao 클래스에 구현 되어있습니다. BaseOcOrderDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	/**
	 * @Desc : 주문번호 신규채번을 위해 SEQ 조회
	 * @Method Name : selectOrderNoNextVal
	 * @Date : 2019. 4. 22.
	 * @Author : ljyoung@3top.co.kr
	 * @return
	 * @throws Exception
	 */
	public int selectOrderNoNextVal() throws Exception;

	public OcOrder selectByPrimaryKey(OcOrder ocOrder) throws Exception;

	/**
	 * @Desc : 최근 배송지 목록 갯수 조회
	 * @Method Name : selectRecentDlvyAddrListCount
	 * @Date : 2019. 4. 23.
	 * @Author : 이강수
	 * @param Pageable<OcOrder, OcOrder>
	 * @return int
	 * @throws Exception
	 */
	public int selectRecentDlvyAddrListCount(Pageable<MbMemberDeliveryAddress, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 최근 배송지 목록 조회
	 * @Method Name : selectRecentDlvyAddrList
	 * @Date : 2019. 4. 1.
	 * @Author : 이강수
	 * @param Pageable<OcOrder, OcOrder>
	 * @return List<OcOrder>
	 * @throws Exception
	 */
	public List<OcOrder> selectRecentDlvyAddrList(Pageable<MbMemberDeliveryAddress, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 클레임신청 대상 주문목록 갯수 조회
	 * @Method Name : selectClaimRequestOrderListCount
	 * @Date : 2019. 4. 16.
	 * @Author : 이강수
	 * @param Pageable<OcOrder, OcOrder> pageable
	 * @return int
	 * @throws Exception
	 */
	public int selectClaimRequestOrderListCount(Pageable<OcOrder, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 클레임신청 대상 주문목록 조회
	 * @Method Name : selectClaimRequestOrderList
	 * @Date : 2019. 4. 16.
	 * @Author : 이강수
	 * @param Pageable<OcOrder, OcOrder> pageable
	 * @return List<OcOrder>
	 * @throws Exception
	 */
	public List<OcOrder> selectClaimRequestOrderList(Pageable<OcOrder, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 최근 배송지 건수
	 * @Method Name : selectRecentAddrees
	 * @Date : 2019. 4. 24.
	 * @Author : ljyoung@3top.co.kr
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectRecentDlvyAddreesCount(Pageable<DeleveryAddress, DeleveryAddress> pageable) throws Exception;

	/**
	 * @Desc : 최근 배송지 목록
	 * @Method Name : selectRecentAddrees
	 * @Date : 2019. 4. 24.
	 * @Author : ljyoung@3top.co.kr
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<DeleveryAddress> selectRecentDlvyAddrees(Pageable<DeleveryAddress, DeleveryAddress> pageable)
			throws Exception;

	/**
	 * @Desc : 최근 픽업 매장
	 * @Method Name : selectRecentPickupStore
	 * @Date : 2019. 7. 3.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrder
	 * @return
	 * @throws Exception
	 */
	public OcOrder selectRecentPickupStore(OcOrder ocOrder) throws Exception;

	/**
	 * @Desc : 주문마스터 상세
	 * @Method Name : selectOrderDetail
	 * @Date : 2019. 5. 9.
	 * @Author : KTH
	 * @param ocOrder
	 * @return
	 * @throws Exception
	 */
	public OcOrder selectOrderDetail(OcOrder ocOrder) throws Exception;

	/**
	 * @Desc : 마이페이지 - 공통영역 최근 주문내역 조회
	 * @Method Name : selectOrderDetail
	 * @Date : 2019. 5. 13.
	 * @Author : 이강수
	 * @param ocOrder
	 * @return OrderStatVO
	 * @throws Exception
	 */
	public OrderStatVO selectCommonOrderStat(OcOrder ocOrder) throws Exception;

	/**
	 * 
	 * @Desc : 마이페이지 메인(주문/배송 현황 조회)
	 * @Method Name : selectMyPageOrderList
	 * @Date : 2019. 5. 23.
	 * @Author : flychani@3top.co.kr
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<OcOrder> selectMyPageOrderList(Pageable<OcOrder, OcOrder> pageable) throws Exception;

	/**
	 * 
	 * @Desc : 마이페이지 메인(주문/배송 현황 조회)
	 * @Method Name : selectMyPageOrderInfoCount
	 * @Date : 2019. 5. 23.
	 * @Author : flychani@3top.co.kr
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectMyPageOrderInfoCount(Pageable<OcOrder, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 오프라인 주문 리스트
	 * @Method Name : selectMyPageOrderOfflineList
	 * @Date : 2019. 5. 31.
	 * @Author : lee
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<OcOrder> selectMyPageOrderOfflineList(Pageable<OcOrder, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 오프라인 주문 카운트
	 * @Method Name : selectMyPageOrderOfflineInfoCount
	 * @Date : 2019. 5. 31.
	 * @Author : lee
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectMyPageOrderOfflineInfoCount(Pageable<OcOrder, OcOrder> pageable) throws Exception;

	/**
	 * @Desc : 상품 상태 update
	 * @Method Name : updateOrderStat
	 * @Date : 2019. 6. 4.
	 * @Author : ljyoung@3top.co.kr
	 * @param order
	 * @throws Exception
	 */
	public void updateOrderStat(OcOrder order) throws Exception;

	public int updateOcOrderAddrModify(OcOrder order);

	/**
	 * 
	 * @Desc : 주문번호별 상태 Count
	 * @Method Name : selectOrderStatCount
	 * @Date : 2019. 6. 10.
	 * @Author : NKB
	 * @param order
	 * @return
	 */
	public OrderStatVO selectOrderStatCount(OcOrder order);

	/**
	 * @Desc : 주문마스터 등록
	 * @Method Name : insertOrder
	 * @Date : 2019. 6. 14.
	 * @Author : KTH
	 * @param order
	 * @return
	 */
	public int insertOrder(OcOrder order) throws Exception;

	/**
	 * @Desc : 클레임 처리로 인한 주문 update
	 * @Method Name : updateOrderByClaim
	 * @Date : 2019. 6. 16.
	 * @Author : KTH
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int updateOrderByClaim(OcOrder order) throws Exception;

	/**
	 * 
	 * @Desc : 주문마스터 구매확정일자 update
	 * @Method Name : updateOrderDcsnDtm
	 * @Date : 2019. 7. 1.
	 * @Author : NKB
	 * @param ocOrder
	 * @return
	 * @throws Exception
	 */
	public int updateOrderDcsnDtm(OcOrder ocOrder) throws Exception;

}
