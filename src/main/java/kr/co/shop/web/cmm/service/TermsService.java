package kr.co.shop.web.cmm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.cmm.model.master.CmTerms;
import kr.co.shop.web.cmm.model.master.CmTermsDetail;
import kr.co.shop.web.cmm.repository.master.CmTermsDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TermsService {

	@Autowired
	CmTermsDao cmTermsDao;

	/**
	 * @Desc : 시행중인 개인정보처리방침과 이력(목록)을 조회한다.
	 * @Method Name : getPrivacyWithList
	 * @Date : 2019. 3. 12.
	 * @Author : Kimyounghyun
	 * @param cmTerms
	 * @return
	 */
//	@Cacheable(value = "termsService.getPrivacyWithList", key = "#cmTerms.termsSeq?:'lasted'")
	public CmTermsDetail getPrivacyWithList(CmTerms cmTerms) {
		// 개인정보처리방침 공콩코드 set
		cmTerms.setTermsTypeCode(CommonCode.TERMS_TYPE_CODE_PRIVACY);
		cmTerms.setTermsDtlCode(CommonCode.TERMS_DTL_CODE_PRIVACY);
		// TODO 전시여부 판단 확인 필요
		List<CmTerms> list = cmTermsDao.selectTermsList(cmTerms);
		CmTermsDetail cmTermsDetail = cmTermsDao.selectTermsWithDetail(cmTerms);
		if (cmTermsDetail != null) {
			cmTermsDetail.setList(list);
		}

		return cmTermsDetail;
	}

	/**
	 * @Desc : 시행중인 사이트이용약관과 이력(목록)을 조회한다.
	 * @Method Name : getSiteWithList
	 * @Date : 2019. 3. 14.
	 * @Author : Kimyounghyun
	 * @param cmTerms
	 * @return
	 */
	public CmTermsDetail getSiteWithList(CmTerms cmTerms) {
		cmTerms.setTermsTypeCode(CommonCode.TERMS_TYPE_CODE_TERMSOFUSE);
		cmTerms.setTermsDtlCode(CommonCode.TERMS_DTL_CODE_SITE_USE);

		List<CmTerms> list = cmTermsDao.selectTermsList(cmTerms);
		CmTermsDetail cmTermsDetail = cmTermsDao.selectTermsWithDetail(cmTerms);
		if (cmTermsDetail != null) {
			cmTermsDetail.setList(list);
		}

		return cmTermsDetail;
	}

	/**
	 * @Desc : 시행중인 통합회원이용약관과 이력(목록)을 조회한다.
	 * @Method Name : getMemberWithList
	 * @Date : 2019. 4. 23.
	 * @Author : Kimyounghyun
	 * @param cmTerms
	 * @return
	 */
	public CmTermsDetail getMemberWithList(CmTerms cmTerms) {
		cmTerms.setTermsTypeCode(CommonCode.TERMS_TYPE_CODE_TERMSOFUSE);
		cmTerms.setTermsDtlCode(CommonCode.TERMS_DTL_CODE_MEMBERSHIP_USE);

		List<CmTerms> list = cmTermsDao.selectTermsList(cmTerms);
		CmTermsDetail cmTermsDetail = cmTermsDao.selectTermsWithDetail(cmTerms);
		if (cmTermsDetail != null) {
			cmTermsDetail.setList(list);
		}

		return cmTermsDetail;
	}

	/**
	 * @Desc : 시행중인 회원가입 동의 약관을 조회
	 * @Method Name : getSingupTermsList
	 * @Date : 2019. 3. 21.
	 * @Author : 이동엽
	 * @param cmTerms
	 * @return
	 */
	public List<CmTermsDetail> getSingupTermsList(CmTerms cmTerms) {
		cmTerms.setTermsTypeCode(CommonCode.TERMS_TYPE_CODE_SIGNUP);

		return cmTermsDao.selectTermsDetailList(cmTerms);
	}

	/**
	 * @Desc : 회원 주문 동의 약관 조회
	 * @Method Name : getMemberOrderTermsList
	 * @Date : 2019. 4. 24.
	 * @Author : Kimyounghyun
	 * @param cmTerms
	 * @return
	 */
	public List<CmTermsDetail> getMemberOrderTerms() {
		CmTerms cmTerms = new CmTerms();
		cmTerms.setTermsTypeCode(CommonCode.TERMS_TYPE_CODE_ORDER);
		cmTerms.setTermsDtlCode(CommonCode.TERMS_DTL_CODE_MEMBER_ORDER);

		return cmTermsDao.selectTermsDetailList(cmTerms);
	}

	/**
	 * @Desc : 비회원 주문 동의 약관 조회
	 * @Method Name : getNonMemberOrderTerms
	 * @Date : 2019. 4. 24.
	 * @Author : Kimyounghyun
	 * @param cmTerms
	 * @return
	 */
	public List<CmTermsDetail> getNonMemberOrderTerms() {
		CmTerms cmTerms = new CmTerms();
		cmTerms.setTermsTypeCode(CommonCode.TERMS_TYPE_CODE_ORDER);
		cmTerms.setTermsDtlCode(CommonCode.TERMS_DTL_CODE_NONMEMBER_ORDER);

		return cmTermsDao.selectTermsDetailList(cmTerms);
	}

}
