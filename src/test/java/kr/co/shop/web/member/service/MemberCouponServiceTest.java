/**
 * 
 */
package kr.co.shop.web.member.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.shop.web.member.model.master.MbMemberCoupon;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : MemberCouponServiceTest.java
 * @Project : shop.backend
 * @Date : 2019. 4. 30.
 * @Author : Kimyounghyun
 */
@SpringBootTest
@Slf4j
class MemberCouponServiceTest {

	@Autowired
	MemberCouponService memberCouponService;

	/**
	 * Test method for
	 * {@link kr.co.shop.web.member.service.MemberCouponService#getAvailableCouponCount(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetAvailableCouponCount() throws Exception {
		String memberNo = "MB00000001";
		int cnt = memberCouponService.getAvailableCouponCount(memberNo);
		log.debug("testGetAvailableCouponCount : {}", cnt);
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.member.service.MemberCouponService#getAvailableCouponList(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetAvailableCouponList() throws Exception {
		String memberNo = "MB00000001";
		List<MbMemberCoupon> list = memberCouponService.getAvailableCouponList(memberNo);
		for (MbMemberCoupon mbMemberCoupon : list) {
			log.debug("testGetAvailableCouponList : {}", mbMemberCoupon);
		}

	}

	@Test
	void testGetAvailableCouponListForOrder() throws Exception {
		String memberNo = "MB00000001";
		List<MbMemberCoupon> list = memberCouponService.getAvailableCouponListForOrder(memberNo);
		for (MbMemberCoupon mbMemberCoupon : list) {
			log.debug("testGetAvailableCouponListForOrder : {}", mbMemberCoupon);
		}
	}

}
