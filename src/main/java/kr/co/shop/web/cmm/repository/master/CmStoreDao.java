package kr.co.shop.web.cmm.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.cmm.model.master.CmStore;
import kr.co.shop.web.cmm.repository.master.base.BaseCmStoreDao;
import kr.co.shop.web.member.model.master.MbMemberInterestStore;
import kr.co.shop.web.promotion.model.master.PrCoupon;

@Mapper
public interface CmStoreDao extends BaseCmStoreDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmStoreDao 클래스에 구현 되어있습니다. BaseCmStoreDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public CmStore selectByPrimaryKey(CmStore cmStore) throws Exception;

	/**
	 * @Desc : 기프트카드 사용불가능한 매장 목록 조회
	 * @Method : selectStoreListUseGiftcardYn
	 * @Date : 2019. 4. 9.
	 * @Author : 이강수
	 * @param CmStore
	 * @return List<CmStore>
	 * @throws Exception
	 */
	public List<CmStore> selectStoreListNotUseGiftcard(CmStore cmStore) throws Exception;

	/**
	 * @Desc : 매장 상세 조회
	 * @Method Name : selectCmStoreDetail
	 * @Date : 2019. 5. 16.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public CmStore selectCmStoreDetail(CmStore cmStore) throws Exception;

	/**
	 * @Desc : 매장 상세 조회 (by storeNo)
	 * @Method Name : selectCmStoreDetailByStoreNo
	 * @Date : 2019. 5. 27.
	 * @Author : 이가영
	 * @param storeNo
	 * @return
	 */
	public CmStore selectCmStoreDetailByStoreNo(String storeNo) throws Exception;

	/**
	 * @Desc : 매장 리스트 카운트 조회
	 * @Method Name : selectCmStoreListCount
	 * @Date : 2019. 5. 21.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public int selectCmStoreListCount(CmStore cmStore) throws Exception;

	/**
	 * @Desc : 매장 리스트 조회
	 * @Method Name : selectCmStoreList
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public List<CmStore> selectCmStoreList(CmStore cmStore) throws Exception;

	/**
	 * @Desc : 신규 매장 리스트 조회
	 * @Method Name : selectNewCmStoreList
	 * @Date : 2019. 5. 8.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public List<CmStore> selectNewCmStoreList(CmStore cmStore) throws Exception;

	/**
	 * @Desc : 회원 단골매장 조회
	 * @Method Name : selectMbMemberInterestStore
	 * @Date : 2019. 5. 9.
	 * @Author : 이가영
	 * @param mbMemberInterestStore
	 * @return
	 */
	public CmStore selectMemberInterestStore(MbMemberInterestStore mbMemberInterestStore) throws Exception;

	/**
	 * @Desc : 쿠폰 적용 매장 카운트 조회
	 * @Method Name : selectCouponApplyStoreList
	 * @Date : 2019. 5. 10.
	 * @Author : 이가영
	 * @param prCoupon
	 * @return
	 */
	public int selectCouponApplyStoreCount(Pageable<PrCoupon, PrCoupon> pageable) throws Exception;

	/**
	 * @Desc : 쿠폰 적용 매장 리스트 조회
	 * @Method Name : selectCouponApplyStoreList
	 * @Date : 2019. 5. 10.
	 * @Author : 이가영
	 * @param prCoupon
	 * @return
	 */
	public List<CmStore> selectCouponApplyStoreList(Pageable<PrCoupon, PrCoupon> pageable) throws Exception;
}
