package kr.co.shop.web.member.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberCouponDao;

@Mapper
public interface MbMemberCouponDao extends BaseMbMemberCouponDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberCouponDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberCouponDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public MbMemberCoupon selectByPrimaryKey(MbMemberCoupon mbMemberCoupon) throws Exception;

	public int selectAvailableCouponCount(String memberNo) throws Exception;

	public List<MbMemberCoupon> selectAvailableCouponList(String memberNo) throws Exception;

	public List<MbMemberCoupon> selectAvailableCouponListForOrder(String memberNo);

	public int selectMemberCouponHoldSeq(String memberNo) throws Exception;

	/**
	 * @Desc : 회원 보유쿠폰 사용 업데이트
	 * @Method Name : updateMemberCouponUseInfo
	 * @Date : 2019. 5. 13.
	 * @Author : KTH
	 * @param mbMemberCoupon
	 * @return
	 * @throws Exception
	 */
	public int updateMemberCouponUseInfo(MbMemberCoupon mbMemberCoupon) throws Exception;

	/**
	 * @Desc : 회원 쿠폰 발급 등록
	 * @Method Name : insertMemberCoupon
	 * @Date : 2019. 6. 15.
	 * @Author : KTH
	 * @param mbMemberCoupon
	 * @return
	 * @throws Exception
	 */
	public int insertMemberCoupon(MbMemberCoupon mbMemberCoupon) throws Exception;

	/**
	 * @Desc : 회원 쿠폰 재 발급 등록(복원 - 기 사용된 쿠폰 기준)
	 * @Method Name : insertMemberCouponReIssue
	 * @Date : 2019. 6. 16.
	 * @Author : KTH
	 * @param mbMemberCoupon
	 * @return
	 * @throws Exception
	 */
	public int insertMemberCouponReIssue(MbMemberCoupon mbMemberCoupon) throws Exception;

}
