package kr.co.shop.web.promotion.model.master;

import kr.co.shop.web.member.model.master.MbMemberCoupon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 해당 객체는 쿠폰 목록과 회원 쿠폰 목록을 함께 조회하기 위해 만들어졌음
 * @FileName : UpperCoupon.java
 * @Project : shop.backend
 * @Date : 2019. 5. 16.
 * @Author : hsjhsj19
 */
@Slf4j
@Data
public class UpperCoupon {

	/** 장바구니순번 */
	private String cartSeq;

	/** 상품번호 */
	private String prdtNo;

	private PrCoupon[] coupon;

	private MbMemberCoupon[] memberCoupon;
}
