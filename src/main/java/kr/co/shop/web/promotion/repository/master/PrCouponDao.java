package kr.co.shop.web.promotion.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.product.model.master.PdProductCoupon;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.model.master.UpperCoupon;
import kr.co.shop.web.promotion.repository.master.base.BasePrCouponDao;

@Mapper
public interface PrCouponDao extends BasePrCouponDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePrCouponDao 클래스에 구현 되어있습니다.
	 * BasePrCouponDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PrCoupon selectByPrimaryKey(PrCoupon prCoupon) throws Exception;

	/**
	 * @Desc : 다운로드 가능 쿠폰 목록 (서비스 요청서)
	 * @Method Name : selectCanDownloadCouponList
	 * @Date : 2019. 4. 23.
	 * @Author : hsjhsj19
	 * @param coupon
	 * @return
	 */
	public List<UpperCoupon> selectCanDownloadCouponList(PrCoupon coupon) throws Exception;

	/**
	 * @Desc : 쿠폰 다운로드 기능 (서비스 요청서)
	 * @Method Name : selectCanDownloadCouponYn
	 * @Date : 2019. 4. 23.
	 * @Author : hsjhsj19
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public String selectCanDownloadCouponYn(PrCoupon coupon) throws Exception;

	/**
	 * @Desc : 회원보유쿠폰 카운트
	 * @Method Name : selectMemberCouponCount
	 * @Date : 2019. 4. 30.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectMemberCouponCount(Pageable<PrCoupon, PrCoupon> pageable) throws Exception;

	/**
	 * @Desc : 회원보유쿠폰리스트
	 * @Method Name : selectMemberCouponList
	 * @Date : 2019. 4. 30.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<PrCoupon> selectMemberCouponList(Pageable<PrCoupon, PrCoupon> pageable) throws Exception;

	/**
	 * @Desc : 회원보유쿠폰
	 * @Method Name : selectMemberCoupon
	 * @Date : 2019. 5. 27.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public PrCoupon selectMemberCoupon(PrCoupon prCoupon) throws Exception;

	/**
	 * @Desc : 다운로드 가능 쿠폰 리스트
	 * @Method Name : selectDownloadCouponList
	 * @Date : 2019. 5. 13.
	 * @Author : easyhun
	 * @param PrCoupon
	 * @return
	 * @throws Exception
	 */
	public List<PrCoupon> selectDownloadCouponList(PrCoupon prCoupon) throws Exception;

	/**
	 * 쿠폰 상세 조회
	 * 
	 * @Desc :
	 * @Method Name : selectPrCoupon
	 * @Date : 2019. 5. 13.
	 * @Author : easyhun
	 * @param PrCoupon
	 * @return
	 * @throws Exception
	 */
	public PrCoupon selectPrCoupon(PrCoupon prCoupon) throws Exception;

	/**
	 * 쿠폰 지류 조회
	 * 
	 * @Desc :
	 * @Method Name : selectPrCouponPaper
	 * @Date : 2019. 5. 20.
	 * @Author : easyhun
	 * @param PrCoupon
	 * @return
	 * @throws Exception
	 */
	public PrCoupon selectCouponPaper(PrCoupon prCoupon) throws Exception;

	/**
	 * 쿠폰 지류 수정
	 * 
	 * @Desc :
	 * @Method Name : updateCouponPaperByCpnNo
	 * @Date : 2019. 5. 20.
	 * @Author : easyhun
	 * @param cpnNo
	 * @return
	 * @throws Exception
	 */
	public PrCoupon updateCouponPaperByCpnNo(String cpnNo) throws Exception;

	/**
	 * @Desc : 쿠폰 다운가능 목록과 회원 쿠폰 목록 함께 조회
	 * @Method Name : memberCouponListAndDownloadCouponList
	 * @Date : 2019. 5. 16.
	 * @Author : hsjhsj19
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public UpperCoupon memberCouponListAndDownloadCouponList(PrCoupon coupon) throws Exception;

	/**
	 * @Desc : 쿠폰 단건 조회
	 * @Method Name : selectCouponByPrimaryKey
	 * @Date : 2019. 5. 21.
	 * @Author : hsjhsj19
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public PrCoupon selectCouponByPrimaryKey(PrCoupon coupon) throws Exception;

	/**
	 * @Desc : 상품에 해당하는 쿠폰 조회
	 * @Method Name : selectCouponByPrdtNo
	 * @Date : 2019. 6. 12.
	 * @Author : tennessee
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductCoupon> selectCouponByPrdtNo(String prdtNo) throws Exception;

	/**
	 * @Desc : 현재 시행중인 온라인 회원 기본 정책의 품절보상쿠폰 조회
	 * @Method Name : selectSoldOutCmpnsCpnPolicy
	 * @Date : 2019. 6. 15.
	 * @Author : KTH
	 * @return
	 * @throws Exception
	 */
	public PrCoupon selectSoldOutCmpnsCpnPolicy() throws Exception;

}
