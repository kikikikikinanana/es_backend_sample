/**
 * 
 */
package kr.co.shop.web.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.repository.master.MbMemberCouponDao;

/**
 * @Desc : 회원쿠폰서비스
 * @FileName : MemberCouponService.java
 * @Project : shop.backend
 * @Date : 2019. 4. 30.
 * @Author : Kimyounghyun
 */
@Service
public class MemberCouponService {

	@Autowired
	private MbMemberCouponDao mbMemberCouponDao;

	/**
	 * @Desc : 사용가능 쿠폰 개수
	 * @Method Name : getAvailableCouponCount
	 * @Date : 2019. 4. 3.
	 * @Author : 유성민
	 * @param memberNo
	 * @return
	 */
	public int getAvailableCouponCount(String memberNo) throws Exception {
		return mbMemberCouponDao.selectAvailableCouponCount(memberNo);
	}

	/**
	 * @Desc : 사용 가능한 쿠폰 리스트
	 * @Method Name : getAvailableCouponList
	 * @Date : 2019. 4. 30.
	 * @Author : Kimyounghyun
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberCoupon> getAvailableCouponList(String memberNo) throws Exception {
		return mbMemberCouponDao.selectAvailableCouponList(memberNo);
	}

	/**
	 * @Desc : 주문에서 요청한 회원 쿠폰 리스트
	 * @Method Name : getAvailableCouponListForOrder
	 * @Date : 2019. 4. 30.
	 * @Author : Kimyounghyun
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberCoupon> getAvailableCouponListForOrder(String memberNo) throws Exception {
		return mbMemberCouponDao.selectAvailableCouponListForOrder(memberNo);
	}

}
