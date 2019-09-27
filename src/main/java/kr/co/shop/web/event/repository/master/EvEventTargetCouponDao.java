package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventTargetCoupon;
import kr.co.shop.web.event.repository.master.base.BaseEvEventTargetCouponDao;

@Mapper
public interface EvEventTargetCouponDao extends BaseEvEventTargetCouponDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventTargetCouponDao 클래스에 구현 되어있습니다.
	 * BaseEvEventTargetCouponDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public EvEventTargetCoupon selectByPrimaryKey(EvEventTargetCoupon evEventTargetCoupon) throws Exception;

	/**
	 * @Desc : 이벤트 상세 쿠폰 리스트 조회
	 * @Method Name : getEventTargetCouponListByEventNo
	 * @Date : 2019. 7. 03.
	 * @Author : 이지훈
	 * @param eventNo
	 * @return
	 */
	public List<EvEventTargetCoupon> getEventTargetCouponListByEventNo(String eventNo) throws Exception;

}
