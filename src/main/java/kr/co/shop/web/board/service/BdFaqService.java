package kr.co.shop.web.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.board.model.master.BdFaq;
import kr.co.shop.web.board.repository.master.BdFaqDao;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BdFaqService {

	@Autowired
	private BdFaqService faqService;

	@Autowired
	private CommonCodeService commoncodeService;

	@Autowired
	private BdFaqDao faqDao;

	public Map<String, Object> getFaqBest10() throws Exception {
		Map<String, Object> faqMap = new HashMap<String, Object>();
		SyCodeDetail syCodeDetail = new SyCodeDetail();

		faqMap.put("faqList", faqDao.selectTop10List());

		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);

		faqMap.put("cnslTypeCode", commoncodeService.getUseCodeByAddInfo1(syCodeDetail));

		return faqMap;
	}

	/**
	 * @Desc : FAQ메인 페이지 호출 파라메터 있을시
	 * @Method Name : getFaqMain
	 * @Date : 2019. 3. 21.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqMain(Pageable<BdFaq, BdFaq> pageable) throws Exception {
		Map<String, Object> faqMap = new HashMap<String, Object>();
		SyCodeDetail syCodeDetail = new SyCodeDetail();

		int totalCount = faqDao.selectFaqByCnslTypeCount(pageable);

		if (totalCount > 0) {
			faqMap.put("faqList", faqDao.selectFaqByCnslDtlCode(pageable));
		}

		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);

		faqMap.put("cnslTypeCode", commoncodeService.getUseCodeByAddInfo1(syCodeDetail));

		faqMap.put("totalCount", totalCount);

		return faqMap;
	}

	/**
	 * @Desc : FAQ 메인 MO (코드 물고올시에)
	 * @Method Name : getFaqMainByParamMO
	 * @Date : 2019. 5. 16.
	 * @Author : 신인철
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqMainByParamMO(Pageable<BdFaq, BdFaq> pageable) throws Exception {
		Map<String, Object> faqMap = new HashMap<String, Object>();
		SyCodeDetail syCodeDetail = new SyCodeDetail();

		faqMap.put("faqList", faqDao.selectFaqByCnslDtlCode(pageable));

		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);
		faqMap.put("cnslTypeCode", commoncodeService.getUseCodeByAddInfo1(syCodeDetail));

		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		syCodeDetail.setAddInfo1(pageable.getBean().getCnslTypeCode());
		faqMap.put("cnslTypeDtlCode", commoncodeService.getUseCodeByAddInfo1(syCodeDetail));

		return faqMap;
	}

	/**
	 * @Desc : FAQ 대분류 클릭시 리스트 조회
	 * @Method Name : getFaqByCnslTypeCode
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqByCnslTypeCode(Pageable<BdFaq, BdFaq> pageable) throws Exception {
		Map<String, Object> faqMap = new HashMap<>();

		SyCodeDetail syCodeDetail = new SyCodeDetail();
		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		syCodeDetail.setAddInfo1(pageable.getBean().getCnslTypeCode());

		List<SyCodeDetail> cnslTypeDtlCode = commoncodeService.getUseCodeByAddInfo1(syCodeDetail);
		pageable.getBean().setCnslTypeDtlCode(cnslTypeDtlCode.get(0).getCodeDtlNo());

		int totalCount = faqDao.selectFaqByCnslTypeCount(pageable);
		List<BdFaq> faqList = faqDao.selectFaqByCnslDtlCode(pageable);

		faqMap.put("cnslTypeDtlCode", cnslTypeDtlCode);
		faqMap.put("faqList", faqList);
		faqMap.put("totalCount", totalCount);

		return faqMap;
	}

	/**
	 * @Desc : FAQ 소분류 클릭시 리스트 조회
	 * @Method Name : getFaqCnslTypeDtlCode
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqByCnslDtlCode(Pageable<BdFaq, BdFaq> pageable) throws Exception {
		Map<String, Object> faqMap = new HashMap<>();

		int totalCount = faqDao.selectFaqByCnslTypeCount(pageable);
		List<BdFaq> faqList = faqDao.selectFaqByCnslDtlCode(pageable);

		if (faqList.size() < pageable.getRowsPerPage()) {
			faqMap.put("endData", true);
		}

		faqMap.put("faqList", faqList);
		faqMap.put("totalCount", totalCount);

		return faqMap;
	}

	/**
	 * @Desc : FAQ 검색
	 * @Method Name : getFaqBySearchValue
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqBySearchValue(Pageable<BdFaq, BdFaq> pageable) throws Exception {
		Map<String, Object> faqMap = new HashMap<>();
		List<BdFaq> faqList = faqDao.selectFaqBySearchValue(pageable);

		if (faqList.size() < pageable.getRowsPerPage()) {
			faqMap.put("endData", true);
		}

		faqMap.put("faqList", faqList);
		faqMap.put("totalCount", faqDao.selectBySearchValueCount(pageable));

		return faqMap;
	}

	/**
	 * @Desc : 상담 상세 코드만 가져오기
	 * @Method Name : getFaqDtlCode
	 * @Date : 2019. 4. 5.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqDtlCode(BdFaq bdFaq) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		SyCodeDetail syCodeDetail = new SyCodeDetail();
		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		syCodeDetail.setAddInfo1(bdFaq.getCnslTypeCode());

		rsMap.put("faqDtlCode", commoncodeService.getUseCodeByAddInfo1(syCodeDetail));

		return rsMap;
	}

	/**
	 * @Desc : 고객센터 메인에서 대분류 누를시에
	 * @Method Name : getFaqByCnslTypeCodeMO
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFaqByCnslTypeCodeMO(Pageable<BdFaq, BdFaq> pageable) throws Exception {
		Map<String, Object> faqMap = new HashMap<>();

		SyCodeDetail syCodeDetail = new SyCodeDetail();
		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		syCodeDetail.setAddInfo1(pageable.getBean().getCnslTypeCode());

		List<SyCodeDetail> cnslTypeDtlCode = commoncodeService.getUseCodeByAddInfo1(syCodeDetail);
		pageable.getBean().setCnslTypeDtlCode(cnslTypeDtlCode.get(0).getCodeDtlNo());

		List<BdFaq> faqList = faqDao.selectFaqByCnslDtlCode(pageable);
		int totalCount = faqList.size();

		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);

		faqMap.put("cnslTypeCode", commoncodeService.getUseCodeByAddInfo1(syCodeDetail));

		faqMap.put("cnslTypeDtlCode", cnslTypeDtlCode);
		faqMap.put("faqList", faqList);
		faqMap.put("totalCount", totalCount);
		faqMap.put("top10List", faqDao.selectTop10List());

		return faqMap;
	}

}
