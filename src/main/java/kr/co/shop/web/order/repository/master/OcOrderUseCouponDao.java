package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrderUseCoupon;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderUseCouponDao;

@Mapper
public interface OcOrderUseCouponDao extends BaseOcOrderUseCouponDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderUseCouponDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderUseCouponDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcOrderUseCoupon selectByPrimaryKey(OcOrderUseCoupon ocOrderUseCoupon) throws Exception;

	/**
	 * @Desc : 클레임 처리용 쿠폰 사용 목록 조회
	 * @Method Name : selectOrderUserCouponForClaimList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param ocOrderUseCoupon
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderUseCoupon> selectOrderUserCouponForClaimList(OcOrderUseCoupon ocOrderUseCoupon) throws Exception;

	/**
	 * @Desc : 주문 쿠폰 사용 정보 입력.
	 * @Method Name : insertList
	 * @Date : 2019. 6. 7.
	 * @Author : ljyoung@3top.co.kr
	 * @param couponList
	 * @return
	 * @throws Exception
	 */
	public void insertList(List<OcOrderUseCoupon> couponList) throws Exception;

	/**
	 * @Desc : 주문에 사용 된 쿠폰 목록
	 * @Method Name : couponListByOrder
	 * @Date : 2019. 6. 11.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrderUseCoupon
	 * @return
	 * @throws Exception
	 */
	public List<OcOrderUseCoupon> couponListByOrder(OcOrderUseCoupon ocOrderUseCoupon) throws Exception;

	/**
	 * @Desc : 주문사용쿠폰 클레임번호 업데이트
	 * @Method Name : updateOrderUseCouponClmNo
	 * @Date : 2019. 7. 4.
	 * @Author : KTH
	 * @param ocOrderUseCoupon
	 * @return
	 * @throws Exception
	 */
	public int updateOrderUseCouponClmNo(OcOrderUseCoupon ocOrderUseCoupon) throws Exception;
}
