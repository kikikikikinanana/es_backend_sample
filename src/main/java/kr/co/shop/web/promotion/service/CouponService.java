package kr.co.shop.web.promotion.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.web.member.model.master.MbMemberCoupon;
import kr.co.shop.web.member.repository.master.MbMemberCouponDao;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductCoupon;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.product.service.ProductService;
import kr.co.shop.web.promotion.model.master.PrCoupon;
import kr.co.shop.web.promotion.model.master.PrCouponApplyProduct;
import kr.co.shop.web.promotion.model.master.UpperCoupon;
import kr.co.shop.web.promotion.repository.master.PrCouponDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CouponService {

	@Autowired
	private PrCouponDao prCouponDao;

	@Autowired
	private MbMemberCouponDao mbMemberCouponDao;

	@Autowired
	private ProductService productService;

	/**
	 * @Desc : 다운로드 가능 쿠폰 목록 (서비스 요청서)
	 * @Method Name : getCanDownloadCouponList
	 * @Date : 2019. 4. 23.
	 * @Author : hsjhsj19
	 * @return
	 * @throws Exception
	 */
	public UpperCoupon getCanDownloadCouponList(String memberNo, String cartSeq, String prdtNo, String prdtOptnNo,
			Integer qty, Integer dscntAmt, String siteNo, String mbshpGradeCode) throws Exception {
		PrCoupon coupon = new PrCoupon();
		coupon.setMemberNo(memberNo);
		coupon.setCartSeq(cartSeq);
		coupon.setPrdtNo(prdtNo);
		coupon.setPrdtOptnNo(prdtOptnNo);
		coupon.setDscntAmt(dscntAmt);
		coupon.setSiteNo(siteNo);

		System.out.println("coupon>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + coupon);

		// 회원등급은 아직 적용 안함
		return this.prCouponDao.memberCouponListAndDownloadCouponList(coupon);
	}

	/**
	 * @Desc : 쿠폰 다운로드 기능 (서비스 요청서)
	 * @Method Name : getCanDownloadCouponYn
	 * @Date : 2019. 4. 23.
	 * @Author : hsjhsj19
	 * @param cnpNo
	 * @param memberNo
	 * @param empYn
	 * @param memberTypeCode
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public String getCanDownloadCouponYn(String cpnNo, String memberNo, String empYn, String memberTypeCode,
			String siteNo) throws Exception {
		PrCoupon coupon = new PrCoupon();
		coupon.setCpnNo(cpnNo);
		coupon.setMemberNo(memberNo);
		coupon.setMemberTypeCodeName(memberTypeCode);
		String result = this.prCouponDao.selectCanDownloadCouponYn(coupon);
		if (result == null) {
			result = "False";
		}
		return result;
	}

	/**
	 * @Desc : 회원 보유 쿠폰
	 * @Method Name : getMemberCouponList
	 * @Date : 2019. 4. 30.
	 * @Author : 이지훈
	 * @param pageable
	 * @return
	 */
	public Map<String, Object> getMemberCouponList(Pageable<PrCoupon, PrCoupon> pageable) throws Exception {
		Map<String, Object> map = new HashMap<>();

		int totalCount = prCouponDao.selectMemberCouponCount(pageable);

		if (totalCount > 0) {
			List<PrCoupon> memberCouponList = prCouponDao.selectMemberCouponList(pageable);
			map.put("memberCouponList", memberCouponList);
		}

		map.put("totalCount", totalCount);

		return map;
	}

	/**
	 * @Desc : 다운로드 가능 쿠폰 리스트
	 * @Method Name : getDownloadCouponList
	 * @Date : 2019. 5. 13.
	 * @Author : 이지훈
	 * @param pageable
	 * @return
	 */
	public List<PrCoupon> getDownloadCouponList(PrCoupon prCoupon) throws Exception {

		List<PrCoupon> ingCouponList = prCouponDao.selectDownloadCouponList(prCoupon);

		return ingCouponList;
	}

	/**
	 * 쿠폰 등록(회원 쿠폰 등록)
	 * 
	 * @Desc : 쿠폰 등록(회원 쿠폰 등록)
	 * @Method Name : insertMemberCoupon
	 * @Date : 2019. 3. 6
	 * @Author : easyhun
	 * @param prCoupon
	 * @throws Exception
	 */
	public Map<String, Object> insertMemberCoupon(PrCoupon prCoupon) throws Exception {
		Map<String, Object> map = new HashMap<>();

		boolean checkCpn = true;
		String memberNo = prCoupon.getMemberNo();
		String exceptionType = "";

		if (prCoupon.getCpnNos() != null) {
			for (String cpnNo : prCoupon.getCpnNos()) {
				prCoupon.setCpnNo(cpnNo);
				PrCoupon detailPrCoupon = prCouponDao.selectPrCoupon(prCoupon);
				MbMemberCoupon mbMemberCoupon = new MbMemberCoupon();
				mbMemberCoupon.setCpnNo(cpnNo);
				mbMemberCoupon.setMemberNo(memberNo);
				mbMemberCoupon.setCpnIssueDtm(new Timestamp(new Date().getTime()));

				// T : 지정기간 , D : 지정일
				if (UtilsText.equals(detailPrCoupon.getValidTermGbnType(), "T")) {
					mbMemberCoupon.setValidStartDtm(detailPrCoupon.getValidStartDtm());
					mbMemberCoupon.setValidEndDtm(detailPrCoupon.getValidEndDtm());
				} else {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, +detailPrCoupon.getUseLimitDayCount());
					mbMemberCoupon.setValidStartDtm(new Timestamp(new Date().getTime()));
					mbMemberCoupon.setValidEndDtm(new Timestamp(cal.getTime().getTime()));
				}

				mbMemberCoupon.setCpnStatCode("10000"); // Const 값으로 변경 TO_DO
				mbMemberCoupon.setHoldCpnSeq(mbMemberCouponDao.selectMemberCouponHoldSeq(memberNo));
				mbMemberCoupon.setRgsterNo(memberNo);
				mbMemberCoupon.setRgstDtm(new Timestamp(new Date().getTime()));
				mbMemberCoupon.setModerNo(memberNo);
				mbMemberCoupon.setModDtm(prCoupon.getRgstDtm());

				if (!UtilsText.equals(detailPrCoupon.getTotalIssueLimitYn(), "Y")) {
					if (detailPrCoupon.getTotalIssueCount() >= detailPrCoupon.getTotalIssueLimitCount()) {
						checkCpn = false;
						exceptionType = "totalLimit";
					}
				}

				if (!UtilsText.equals(detailPrCoupon.getPer1psnIssueLimitYn(), "Y")) {
					if (detailPrCoupon.getMemberCouponCnt() >= detailPrCoupon.getPer1psnMaxIssueCount()) {
						checkCpn = false;
						exceptionType = "perMax";
					}
				}

				if (checkCpn) {
					mbMemberCouponDao.insert(mbMemberCoupon);

					// 발급한 count 쿠폰테이블 update
					detailPrCoupon.setTotalIssueCount(detailPrCoupon.getTotalIssueCount() + 1);
					prCouponDao.update(detailPrCoupon);
				}
			}
		}
		map.put("exceptionType", exceptionType);

		return map;
	}

	/**
	 * 쿠폰 지류 등록(회원 쿠폰 등록)
	 * 
	 * @Desc : 쿠폰 등록(회원 쿠폰 등록)
	 * @Method Name : insertPaperCoupon
	 * @Date : 2019. 5. 20
	 * @Author : easyhun
	 * @param prCoupon
	 * @throws Exception
	 */
	public Map<String, Object> insertCouponPaper(PrCoupon prCoupon) throws Exception {
		Map<String, Object> map = new HashMap<>();

		String exceptionType = "";

		if (UtilsText.isNotBlank(prCoupon.getPaperNoText())) {
			PrCoupon paperCoupon = prCouponDao.selectCouponPaper(prCoupon);
			if (paperCoupon != null) {
				if (UtilsText.equals(paperCoupon.getRgstYn(), "Y")
						|| UtilsText.equals(paperCoupon.getStoreUseYn(), "Y")) {
					exceptionType = "existCpn";
				} else {
					this.insertCouponWithoutCondition(prCoupon);
					// 지류 등록 update
					prCouponDao.updateCouponPaperByCpnNo(paperCoupon.getCpnNo());
				}
			} else {
				exceptionType = "noneCpn";
			}
		}
		map.put("exceptionType", exceptionType);

		return map;
	}

	/**
	 * 쿠폰 등록(회원 쿠폰 등록) / 쿠폰 제한 조건 X
	 * 
	 * @Desc : 쿠폰 등록(회원 쿠폰 등록)
	 * @Method Name : insertCouponWithoutCondition
	 * @Date : 2019. 5. 22
	 * @Author : easyhun
	 * @param prCoupon -> cpnNo, memberNo 필수
	 * @throws Exception
	 */
	public void insertCouponWithoutCondition(PrCoupon prCoupon) throws Exception {
		String cpnNo = prCoupon.getCpnNo();
		String memberNo = prCoupon.getMemberNo();

		if (UtilsText.isNotBlank(memberNo) && UtilsText.isNotBlank(cpnNo)) {
			PrCoupon paperCoupon = prCouponDao.selectPrCoupon(prCoupon);

			MbMemberCoupon mbMemberCoupon = new MbMemberCoupon();
			mbMemberCoupon.setCpnNo(cpnNo);
			mbMemberCoupon.setMemberNo(memberNo);
			mbMemberCoupon.setCpnIssueDtm(new Timestamp(new Date().getTime()));

			// T : 지정기간 , D : 지정일
			if (UtilsText.equals(paperCoupon.getValidTermGbnType(), "T")) {
				mbMemberCoupon.setValidStartDtm(paperCoupon.getValidStartDtm());
				mbMemberCoupon.setValidEndDtm(paperCoupon.getValidEndDtm());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, +paperCoupon.getUseLimitDayCount());
				mbMemberCoupon.setValidStartDtm(new Timestamp(new Date().getTime()));
				mbMemberCoupon.setValidEndDtm(new Timestamp(cal.getTime().getTime()));
			}

			mbMemberCoupon.setCpnStatCode("10000"); // Const 값으로 변경 TO_DO
			mbMemberCoupon.setHoldCpnSeq(mbMemberCouponDao.selectMemberCouponHoldSeq(memberNo));
			mbMemberCoupon.setRgsterNo(memberNo);
			mbMemberCoupon.setRgstDtm(new Timestamp(new Date().getTime()));
			mbMemberCoupon.setModerNo(memberNo);
			mbMemberCoupon.setModDtm(prCoupon.getRgstDtm());

			mbMemberCouponDao.insert(mbMemberCoupon);

			// 발급한 count 쿠폰테이블 update
			paperCoupon.setTotalIssueCount(paperCoupon.getTotalIssueCount() + 1);
			prCouponDao.update(paperCoupon);
		}
	}

	/**
	 * @Desc : 상품에 해당하는 쿠폰 조회. 혜택이 높은 쿠폰(일반/플러스쿠폰 모두) 순으로 반환.
	 * @Method Name : getCouponByPrdtNo
	 * @Date : 2019. 6. 12.
	 * @Author : tennessee
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<PdProductCoupon> getCouponByPrdtNo(String prdtNo) throws Exception {
		return this.prCouponDao.selectCouponByPrdtNo(prdtNo);
	}

	/**
	 * @Desc : 쿠폰 대상 상품 리스트
	 * @Method Name : getDrawTargetProductList
	 * @Date : 2019. 6. 21.
	 * @Author : 이지훈
	 * @param getDrawTargetProductList
	 * @return
	 */
	public Map<String, Object> getCouponApplyProductList(Parameter<PrCouponApplyProduct> parameter) throws Exception {
		Map<String, Object> map = new HashMap<>();

		PageableProduct<PrCouponApplyProduct, PdProductWrapper> pageable = new PageableProduct<PrCouponApplyProduct, PdProductWrapper>(
				parameter);

		pageable.setCondition(parameter.get().getSiteNo(), parameter.get().getChnnlNo());
		pageable.setUseTableMapping("PR_COUPON_APPLY_PRODUCT", new LinkedHashMap<String, String>() {
			{
				put("cpn_no", parameter.get().getCpnNo());
			}
		}, null);
		// pageable.setCategoryMapping(parameter.get().getCtgrNo());

		String pagingSortType = parameter.get().getPagingSortType() != null ? parameter.get().getPagingSortType()
				: PageableProduct.PAGING_SORT_TYPE_NEWEST;

		pageable.setUsePaging(true, pagingSortType, null, null);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());

		pageable.setPageNum(parameter.get().getPageNum());
		Page<PdProductWrapper> productList = this.productService.getDisplayProductList(pageable);
		map.put("productList", productList);

		return map;
	}

}
