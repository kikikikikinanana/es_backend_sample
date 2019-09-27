package kr.co.shop.web.cart.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cart.model.master.OcCartBenefit;
import kr.co.shop.web.cart.repository.master.base.BaseOcCartBenefitDao;

@Mapper
public interface OcCartBenefitDao extends BaseOcCartBenefitDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcCartBenefitDao 클래스에 구현 되어있습니다.
	 * BaseOcCartBenefitDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcCartBenefit selectByPrimaryKey(OcCartBenefit ocCartBenefit) throws Exception;

	/**
	 * @Desc : 상품 쿠폰 정보 조회
	 * @Method Name : getCartCoupon
	 * @Date : 2019. 5. 31.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public List<OcCartBenefit> getCartCoupon(OcCartBenefit ocCartBenefit) throws Exception;

	/**
	 * @Desc : 쿠폰삭제
	 * @Method Name : deleteCartPrdtCouponReset
	 * @Date : 2019. 6. 7.
	 * @Author : flychani@3top.co.kr
	 * @param ocCartBenefit
	 * @return
	 */
	public int deleteCartPrdtCouponReset(OcCartBenefit ocCartBenefit) throws Exception;

	/**
	 * @Desc : 쿠폰 적용
	 * @Method Name : setCartPrdtCouponInsert
	 * @Date : 2019. 6. 7.
	 * @Author : flychani@3top.co.kr
	 * @param applyCpn
	 * @return
	 */
	public int setCartPrdtCouponInsert(OcCartBenefit applyCpn) throws Exception;

	/**
	 * @Desc : 쿠폰 정보조회 list
	 * @Method Name : getCartCouponList
	 * @Date : 2019. 6. 11.
	 * @Author : flychani@3top.co.kr
	 * @param benefit
	 * @return
	 */
	public List<OcCartBenefit> getCartCouponList(OcCartBenefit benefit) throws Exception;

	/**
	 * @Desc : 장바구니 상품별 쿠폰 목록
	 * @Method Name : getCartCouponPrdtList
	 * @Date : 2019. 6. 16.
	 * @Author : flychani@3top.co.kr
	 * @param benefit
	 * @return
	 */
	public List<OcCartBenefit> getCartCouponPrdtList(OcCartBenefit benefit) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : getCartCouponPrdtInfo
	 * @Date : 2019. 6. 18.
	 * @Author : flychani@3top.co.kr
	 * @param benefit
	 * @return
	 */
	public OcCartBenefit getCartCouponPrdtInfo(OcCartBenefit benefit) throws Exception;

}
