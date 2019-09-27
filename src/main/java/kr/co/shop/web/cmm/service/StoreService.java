/**
 * 
 */
package kr.co.shop.web.cmm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.constant.Const;
import kr.co.shop.web.cmm.model.master.CmStore;
import kr.co.shop.web.cmm.model.master.CmStoreService;
import kr.co.shop.web.cmm.repository.master.CmStoreDao;
import kr.co.shop.web.cmm.repository.master.CmStoreServiceDao;
import kr.co.shop.web.member.model.master.MbMemberInterestStore;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 매장 서비스
 * @FileName : StoreService.java
 * @Project : shop.backend
 * @Date : 2019. 4. 9.
 * @Author : 이강수
 */

@Slf4j
@Service
public class StoreService {

	@Autowired
	CmStoreDao cmStoreDao;

	@Autowired
	CmStoreServiceDao cmStoreServiceDao;

	/**
	 * @Desc : 기프트카드 사용불가능한 매장 목록 조회
	 * @Method : getStoreListUseGiftcardYn
	 * @Date : 2019. 4. 9.
	 * @Author : 이강수
	 * @param CmStore
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getStoreListUseGiftcardYn(CmStore cmStore) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		// 매장서비스코드 : 기프트카드 (임시)
		cmStore.setStoreServiceCode("10003");
		// 기프트카드 사용불가능 매장 : "N"
		cmStore.setPsbltYn(Const.BOOLEAN_FALSE);
		// 매장유형코드 : OTS (임시)
		cmStore.setStoreTypeCode("10008");

		cmStore.setOtsYn(Const.BOOLEAN_FALSE);
		map.put("noOTS", cmStoreDao.selectStoreListNotUseGiftcard(cmStore));

		cmStore.setOtsYn(Const.BOOLEAN_TRUE);
		map.put("OTS", cmStoreDao.selectStoreListNotUseGiftcard(cmStore));

		return map;
	}

	/**
	 * @Desc : 매장상세 검색
	 * @Method Name : getStoreDetail
	 * @Date : 2019. 4. 22.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public CmStore getStoreDetail(CmStore params) throws Exception {
		CmStore cmStore = new CmStore();
		cmStore = cmStoreDao.selectByPrimaryKey(params);

		CmStoreService cmStoreService = new CmStoreService();
		cmStoreService.setStoreNo(params.getStoreNo());
		cmStoreService.setPsbltYn(Const.BOOLEAN_TRUE);
		List<CmStoreService> storeServiceList = cmStoreServiceDao.select(cmStoreService);
		cmStore.setStoreServiceList(storeServiceList);
		return cmStore;
	}

	/**
	 * @Desc : 매장 상세 조회
	 * @Method Name : getCmStoreDetail
	 * @Date : 2019. 5. 16.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public CmStore getCmStoreDetail(CmStore cmStore) throws Exception {

		return cmStoreDao.selectCmStoreDetail(cmStore);
	}

	/**
	 * @Desc : 매장 상세 조회 (by storeNo)
	 * @Method Name : getCmStoreDetailByStoreNo
	 * @Date : 2019. 5. 27.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public CmStore getCmStoreDetailByStoreNo(String storeNo) throws Exception {

		return cmStoreDao.selectCmStoreDetailByStoreNo(storeNo);
	}

	/**
	 * @Desc : 매장 리스트 조회
	 * @Method Name : getCmStoreList
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public Map<String, Object> getCmStoreList(CmStore cmStore) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("count", cmStoreDao.selectCmStoreListCount(cmStore));
		resultMap.put("list", cmStoreDao.selectCmStoreList(cmStore));

		return resultMap;
	}

	/**
	 * @Desc : 신규 매장 리스트 조회
	 * @Method Name : getNewCmStoreList
	 * @Date : 2019. 5. 8.
	 * @Author : 이가영
	 * @param cmStore
	 * @return
	 */
	public List<CmStore> getNewCmStoreList(CmStore cmStore) throws Exception {

		return cmStoreDao.selectNewCmStoreList(cmStore);
	}

	/**
	 * @Desc : 회원 단골매장 조회
	 * @Method Name : getMbMemberInterestStore
	 * @Date : 2019. 5. 9.
	 * @Author : 이가영
	 * @param mbMemberInterestStore
	 * @return
	 */
	public CmStore getMemberInterestStore(MbMemberInterestStore mbMemberInterestStore) throws Exception {

		return cmStoreDao.selectMemberInterestStore(mbMemberInterestStore);
	}

	/**
	 * @Desc : 쿠폰 적용 매장 리스트 조회
	 * @Method Name : getCouponApplyStoreList
	 * @Date : 2019. 5. 10.
	 * @Author : 이가영
	 * @param prCoupon
	 * @return
	 */
	public Map<String, Object> getCouponApplyStoreList(Pageable<PrCoupon, PrCoupon> pageable) throws Exception {
		Map<String, Object> map = new HashMap<>();

		int totalCount = cmStoreDao.selectCouponApplyStoreCount(pageable);

		if (totalCount > 0) {
			List<CmStore> storeList = cmStoreDao.selectCouponApplyStoreList(pageable);
			map.put("storeList", storeList);
		}

		map.put("totalCount", totalCount);

		return map;
	}

	/**
	 * @Desc : 오프라인 매장별 행사 배너 정보
	 * @Method Name : getStoreBannerMap
	 * @Date : 2019. 6. 5.
	 * @Author : 이가영
	 * @param storeNo
	 * @return
	 */
	public Map<String, String> getStoreBannerUrlMap(String storeNo) throws Exception {

		Map<String, String> result = new HashMap<>();
		CmStore cmStore = cmStoreDao.selectCmStoreDetailByStoreNo(storeNo);

		result.put("pc", cmStore.getPcImageUrl());
		result.put("mobile", cmStore.getMobileImageUrl());

		return result;
	}
}
