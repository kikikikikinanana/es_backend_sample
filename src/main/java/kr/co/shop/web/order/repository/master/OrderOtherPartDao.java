package kr.co.shop.web.order.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderDao;
import kr.co.shop.web.order.vo.OrderOtherPartVo;
import kr.co.shop.web.product.model.master.BdProductReview;

@Mapper
public interface OrderOtherPartDao extends BaseOcOrderDao {

	/**
	 * @Desc : 주문, 클레임, AS의 갯수 - 진행중 대상건수
	 * @Method Name : selectProgressOrderClmAsCount
	 * @Date : 2019. 4. 24.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return OrderOtherPartVo
	 * @throws Exception
	 */
	public OrderOtherPartVo selectProgressOrderClmAsCount(OcOrder param) throws Exception;

	/**
	 * @Desc : 구매확정 후 30 진행 여부 유효성 검사
	 * @Method Name : selectOverDayYnAfterBuyDecision
	 * @Date : 2019. 4. 24.
	 * @Author : 이강수
	 * @param OcOrder
	 * @return OrderOtherPartVo
	 * @throws Exception
	 */
	public OrderOtherPartVo selectOverDayYnAfterBuyDecision(OcOrder param) throws Exception;

	/**
	 * @Desc : 온라인/ 매장 사후적립을 위한 적합성 체크 프로시저
	 * @Method Name : callProcedureForDecodeOrderNum
	 * @Date : 2019. 4. 25.
	 * @Author : 유성민
	 * @param map
	 * @return output
	 * @throws Exception
	 */
	public String callProcedureForDecodeOrderNum(Map<String, String> map) throws Exception;

	/**
	 * @Desc : 포인트 적럽여부 update
	 * @Method Name : updatePointSaveYn
	 * @Date : 2019. 4. 25.
	 * @Author : 유성민
	 * @param params
	 * @return
	 */
	public int updatePointSaveYn(OcOrder params) throws Exception;

	/**
	 * @Desc : 주문상품 조회
	 * @Method Name : getOrderProductInfoList
	 * @Date : 2019. 5. 3.
	 * @Author : 유성민
	 * @param orderProductParams
	 * @return
	 */
	public List<OcOrderProduct> getOrderProductInfoList(OcOrderProduct params) throws Exception;

	/**
	 * 
	 * @Desc : 회원 포인트 사후적립내역
	 * @Method Name : getOrderMemberPointExportList
	 * @Date : 2019. 5. 22.
	 * @Author : NKB
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<OcOrder> getOrderMemberPointExportList(OcOrder params) throws Exception;

	/**
	 * 
	 * @Desc :주문번호 기준으로 구매확정 데이타 전달
	 * @Method Name : selectOrderProductBuyConfirm
	 * @Date : 2019. 5. 24.
	 * @Author : NKB
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public OcOrderProduct selectOrderProductBuyConfirm(OcOrderProduct param) throws Exception;

	/**
	 * 
	 * @Desc :
	 * @Method Name : selectOrderProductQnaTarget
	 * @Date : 2019. 5. 29.
	 * @Author : NKB
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public BdProductReview selectOrderProductQnaTarget(OcOrderProduct param) throws Exception;
}
