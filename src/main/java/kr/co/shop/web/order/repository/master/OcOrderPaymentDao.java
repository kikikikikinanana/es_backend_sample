package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderPayment;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderPaymentDao;

@Mapper
public interface OcOrderPaymentDao extends BaseOcOrderPaymentDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderPaymentDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderPaymentDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcOrderPayment selectByPrimaryKey(OcOrderPayment ocOrderPayment) throws Exception;

	public List<OcOrderPayment> selectDetail(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * 
	 * @Desc : 대표카드 판변값 (없으면 원카드번호 전달)
	 * @Method Name : selectByPrimaryKey
	 * @Date : 2019. 5. 23.
	 * @Author : NKB
	 * @param ocOrderPayment
	 * @return
	 * @throws Exception
	 */
	public OcOrderPayment selectByReturnRprsntCard(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : selectPaymentDetail
	 * @Date : 2019. 5. 27.
	 * @Author : flychani@3top.co.kr
	 * @param orderPayment
	 * @return
	 */
	public List<OcOrderPayment> selectPaymentDetail(OcOrderPayment orderPayment) throws Exception;

	/**
	 * @Desc : 주문 결제 정보 조회
	 * @Method Name : selectPaymentList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param ocOrderPayment
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderPayment> selectPaymentList(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * @Desc : 결제수단 상태 일괄 update
	 * @Method Name : updateOrderStat
	 * @Date : 2019. 6. 4.
	 * @Author : ljyoung@3top.co.kr
	 * @param order
	 * @throws Exception
	 */
	public void updateOrderStat(OcOrder order) throws Exception;

	/**
	 * @Desc : 주결제 수단 저장
	 * @Method Name : insertMainPayment
	 * @Date : 2019. 6. 12.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrderPayment
	 * @throws Exception
	 */
	public void insertMainPayment(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * @Desc : 기프트 카드 사용 정보 입력
	 * @Method Name : insertGiftPayment
	 * @Date : 2019. 6. 10.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrderPayment
	 */
	public void insertGiftPayment(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * @Desc : 포인트 사용 정보 저장
	 * @Method Name : insertPointPayment
	 * @Date : 2019. 6. 28.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrderPayment
	 * @throws Exception
	 */
	public void insertPointPayment(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * 
	 * @Desc :주문 상세 결제 변경 이력
	 * @Method Name : selectPaymentListHistory
	 * @Date : 2019. 6. 11.
	 * @Author : NKB
	 * @param ocOrderPayment
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderPayment> selectPaymentListHistory(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * @Desc : 결제 정보 조회
	 * @Method Name : selectPaymentInfo
	 * @Date : 2019. 6. 13.
	 * @Author : lee
	 * @param ocOrderPayment
	 * @return
	 * @throws Exception
	 */
	public OcOrderPayment selectPaymentInfo(OcOrderPayment ocOrderPayment) throws Exception;

	/**
	 * @Desc : 결제 수단별 영수증 조회 (구매포인트, 이벤트 포인트 제외)
	 * @Method Name : selectReceiptPayment
	 * @Date : 2019. 6. 20.
	 * @Author : lee
	 * @param ocOrderPayment
	 * @return
	 * @throws Exception
	 */
	public OcOrderPayment selectReceiptPayment(OcOrderPayment ocOrderPayment) throws Exception;

	public void insertPaymentChange(OcOrderPayment ocOrderPayment) throws Exception;

	public int updateOcOrderPaymentAccount(OcOrderPayment ocOrderPayment) throws Exception;

	public int updateOcOrderOldPaymentCancel(OcOrderPayment ocOrderPayment) throws Exception;

	public int updateOcOrderPaymenKcpLog(OcOrderPayment ocOrderPayment) throws Exception;
}
