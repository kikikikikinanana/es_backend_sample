/**
 * 
 */
package kr.co.shop.web.cmm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.cmm.model.master.CmStore;
import kr.co.shop.web.cmm.service.StoreService;
import kr.co.shop.web.member.model.master.MbMemberInterestStore;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 매장 컨드롤러
 * @FileName : StoreController.java
 * @Project : shop.backend
 * @Date : 2019. 4. 9.
 * @Author : 이강수
 */

@Slf4j
@RestController
@RequestMapping("api")
public class StoreController {

	@Autowired
	StoreService storeService;

	@Autowired
	CommonCodeService commonCodeService;

	@ApiOperation(value = "기프트카드 사용불가능한 매장 목록 조회", notes = "기프트카드 사용불가능한 매장 목록 조회한 Map 리턴", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/getStoreListUseGiftcardYn" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStoreListUseGiftcardYn(Parameter<CmStore> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, storeService.getStoreListUseGiftcardYn(parameter.get()));
	}

	/**
	 * @Desc : 매장 상세 조회
	 * @Method Name : getCmStoreDetail
	 * @Date : 2019. 5. 16.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "매장 상세 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCmStoreDetail(Parameter<CmStore> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, storeService.getCmStoreDetail(parameter.get()));
	}

	/**
	 * @Desc : 매장 리스트 조회
	 * @Method Name : getCmStoreList
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "매장 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCmStoreList(Parameter<CmStore> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, storeService.getCmStoreList(parameter.get()));
	}

	/**
	 * @Desc : 신규 매장 리스트 조회
	 * @Method Name : getNewCmStoreList
	 * @Date : 2019. 5. 8.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "신규 매장 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/new/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNewCmStoreList(Parameter<CmStore> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, storeService.getNewCmStoreList(parameter.get()));
	}

	/**
	 * @Desc : 회원 단골매장 조회
	 * @Method Name : getMbMemberInterestStore
	 * @Date : 2019. 5. 9.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "회원 단골매장 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/interest/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberInterestStore(Parameter<MbMemberInterestStore> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, storeService.getMemberInterestStore(parameter.get()));
	}

	/**
	 * @Desc : 공통코드 목록 조회
	 * @Method Name : getCommonCodeForCmStore
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "공통코드 목록 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/store/code" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCommonCodeForCmStore(Parameter<CmStore> parameter) throws Exception {

		Map<String, List<SyCodeDetail>> map = new HashMap<>();

		map.put(CommonCode.STORE_GBN_CODE, commonCodeService.getCode(CommonCode.STORE_GBN_CODE));
		map.put(CommonCode.STORE_SERVICE_CODE, commonCodeService.getCode(CommonCode.STORE_SERVICE_CODE));

		return UtilsREST.responseOk(parameter, map);
	}

	/**
	 * @Desc : 쿠폰 적용 매장 리스트 조회
	 * @Method Name : getCouponApplyStoreList
	 * @Date : 2019. 5. 10.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "쿠폰 적용 매장 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/coupon-apply/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponApplyStoreList(Parameter<PrCoupon> parameter) throws Exception {
		Pageable<PrCoupon, PrCoupon> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = storeService.getCouponApplyStoreList(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}
}
