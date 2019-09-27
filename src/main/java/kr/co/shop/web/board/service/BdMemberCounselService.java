package kr.co.shop.web.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.web.afterService.model.master.OcAsAccept;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProduct;
import kr.co.shop.web.afterService.service.AfterServiceService;
import kr.co.shop.web.board.model.master.BdMemberCounsel;
import kr.co.shop.web.board.model.master.BdMemberCounselAttachFile;
import kr.co.shop.web.board.repository.master.BdMemberCounselAttachFileDao;
import kr.co.shop.web.board.repository.master.BdMemberCounselDao;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.service.OrderService;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BdMemberCounselService {
	@Autowired
	private BdMemberCounselDao counselDao;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private BdMemberCounselService counselService;

	@Autowired

	private BdMemberCounselAttachFileDao bdAttachFileDao;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AfterServiceService afterServiceService;

	@Autowired
	private OrderService orderService;

	/**
	 * @Desc : 마이페이지 나의 상담내역 리스트
	 * @Method Name : getInqryList
	 * @Date : 2019. 3. 20.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getInqryList(Pageable<BdMemberCounsel, BdMemberCounsel> pageable) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		int totalCount = counselDao.selectInqryCount(pageable);
		rsMap.put("totalCount", totalCount);

		List<SyCodeDetail> codeList = commonCodeService.getUseCode(CommonCode.CNSL_GBN_CODE);
		List<SyCodeDetail> cnslGbnCode = new ArrayList<SyCodeDetail>();

		for (SyCodeDetail syCodeDetail : codeList) {
			if (syCodeDetail.getCodeDtlNo().equals(CommonCode.CNSL_GBN_CODE_INQUIRY)) {
				cnslGbnCode.add(syCodeDetail);
			}
			if (syCodeDetail.getCodeDtlNo().equals(CommonCode.CNSL_GBN_CODE_VOC)) {
				cnslGbnCode.add(syCodeDetail);
			}
		}

		rsMap.put(CommonCode.CNSL_GBN_CODE, cnslGbnCode);
		rsMap.put("counselList", counselDao.selectInqryList(pageable));

		return rsMap;
	}

	/**
	 * @Desc : 마이페이지에서 상담리스트
	 * @Method Name : getInqryListMO
	 * @Date : 2019. 5. 30.
	 * @Author : 신인철
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getInqryListMO(Pageable<BdMemberCounsel, BdMemberCounsel> pageable) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		int totalCount = counselDao.selectInqryCount(pageable);
		List<BdMemberCounsel> counselList = counselDao.selectInqryList(pageable);

		if (counselList.size() < pageable.getRowsPerPage()) {
			rsMap.put("endData", true);
		}

		rsMap.put("totalCount", totalCount);
		rsMap.put("counselList", counselList);

		return rsMap;
	}

	/**
	 * @Desc : 마이페이지 상담내역 스크롤 이벤트
	 * @Method Name : getInqryListScroll
	 * @Date : 2019. 5. 30.
	 * @Author : 신인철
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getInqryListScroll(Pageable<BdMemberCounsel, BdMemberCounsel> pageable)
			throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		List<BdMemberCounsel> counselList = counselDao.selectInqryList(pageable);
		if (counselList.size() < pageable.getRowsPerPage()) {
			rsMap.put("endData", true);
		}
		rsMap.put("counselList", counselList);

		return rsMap;
	}

	/**
	 * @Desc : 상담내역 상세보기
	 * @Method Name : getInqryDetail
	 * @Date : 2019. 3. 20.
	 * @Author : 신인철, 이강수
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getInqryDetail(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<String, Object>();

		BdMemberCounsel detail = counselDao.selectInqryDetail(bdMemberCounsel);

		if (detail.getAsAcceptNo() != null && detail.getAsAcceptPrdtSeq() != null) {
			counselService.setAsAceeptNo(detail);
		}

		if (detail.getOrderNo() != null && detail.getOrderPrdtSeq() != null) {
			counselService.setOrderNo(detail);
		}

		rsMap.put("inqryDetail", detail);
		rsMap.put("attachFileList", bdAttachFileDao.selectInqryFileDetail(bdMemberCounsel));

		return rsMap;
	}

	/**
	 * @Desc : AS번호 넣기
	 * @Method Name : setAsAceeptNo
	 * @Date : 2019. 4. 23.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public BdMemberCounsel setAsAceeptNo(BdMemberCounsel detail) throws Exception {

		OcAsAccept ocAsAccept = new OcAsAccept();
		ocAsAccept.setAsAcceptNo(detail.getAsAcceptNo());
		ocAsAccept.setAsAcceptPrdtSeq(detail.getAsAcceptPrdtSeq());

		OcAsAcceptProduct productDetail = (OcAsAcceptProduct) afterServiceService.getAsAcceptDetailInfo(ocAsAccept)
				.get("ocAsAcceptProductInfo");

		detail.setOcAsAcceptProduct(productDetail);

		return detail;
	}

	/**
	 * @Desc : 주문번호 넣기
	 * @Method Name : setOrderNo
	 * @Date : 2019. 4. 23.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public BdMemberCounsel setOrderNo(BdMemberCounsel detail) throws Exception {

		OcOrderProduct ocOrderProduct = new OcOrderProduct();
		ocOrderProduct.setOrderNo(detail.getOrderNo());
		ocOrderProduct.setOrderPrdtSeq(detail.getOrderPrdtSeq());

		OcOrderProduct productDetail = orderService.getOrderPrdtInMemberCnsl(ocOrderProduct);

		detail.setOcOrderProduct(productDetail);

		return detail;
	}

	/**
	 * @Desc : 1:1문의 등록
	 * @Method Name : setInqryDetail
	 * @Date : 2019. 3. 20.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public Map<String, Object> setInqryDetail(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		try {
			if (bdMemberCounsel.getPrdtNo() != null) {

			}
			counselDao.insertInqryDetail(bdMemberCounsel);

			if (bdMemberCounsel.getInqryAtchFiles() != null) {
				counselService.setAttachFile(bdMemberCounsel);
			}
			rsMap.put("dbResult", 1);
		} catch (Exception e) {
			rsMap.put("dbResult", 0);
			rsMap.put("Message", e.toString());
		}

		return rsMap;
	}

	/**
	 * @Desc : 1:1 문의 수정
	 * @Method Name : updateInqryDetail
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateInqryDetail(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		if (bdMemberCounsel.getRemoveAtchFileSeq() != null) {
			BdMemberCounselAttachFile removeFile = new BdMemberCounselAttachFile();
			removeFile.setMemberCnslSeq(bdMemberCounsel.getMemberCnslSeq());
			for (int atchFileSeq : bdMemberCounsel.getRemoveAtchFileSeq()) {
				removeFile.setAtchFileSeq(atchFileSeq);
				bdAttachFileDao.delete(removeFile);
			}
		}

		try {
			counselDao.updateInqryDetail(bdMemberCounsel);

			if (bdMemberCounsel.getInqryAtchFiles() != null) {
				counselService.setAttachFile(bdMemberCounsel);
			}
			rsMap.put("dbResult", 1);
		} catch (Exception e) {
			rsMap.put("dbResult", 0);
			rsMap.put("Message", e.toString());
		}

		return rsMap;
	}

	/**
	 * @Desc : 문의 첨부파일 DB 저장
	 * @Method Name : setAttachFile
	 * @Date : 2019. 4. 22.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void setAttachFile(BdMemberCounsel bdMemberCounsel) throws Exception {
		BdMemberCounselAttachFile[] files = bdMemberCounsel.getInqryAtchFiles();
		for (BdMemberCounselAttachFile file : files) {
			file.setAtchFileGbnType("Q");
			file.setMemberCnslSeq(bdMemberCounsel.getMemberCnslSeq());
			bdAttachFileDao.insertInqryFile(file);
		}
	}

	/**
	 * @Desc : 1:1문의 등록페이지 호출
	 * @Method Name : getCreateInqryForm
	 * @Date : 2019. 3. 20.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCreateInqryForm(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<String, Object>();

		SyCodeDetail syCodeDetail = new SyCodeDetail();
		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);
		rsMap.put(CommonCode.CNSL_TYPE_CODE, commonCodeService.getUseCodeByAddInfo1(syCodeDetail));

		rsMap.put(CommonCode.CNSL_TYPE_DTL_CODE, commonCodeService.getUseCode(CommonCode.CNSL_TYPE_DTL_CODE));

		return rsMap;
	}

	/**
	 * @Desc : 1:1문의 상세보기 수정 폼
	 * @Method Name : getUpdateInqryForm
	 * @Date : 2019. 3. 20.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUpdateInqryForm(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<String, Object>();

		SyCodeDetail codeParam = new SyCodeDetail();
		codeParam.setCodeField(CommonCode.CNSL_TYPE_CODE);
		codeParam.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);
		List<SyCodeDetail> cnslTypeList = commonCodeService.getUseCodeByAddInfo1(codeParam);

		codeParam.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		List<SyCodeDetail> cnslTypeDtlList = commonCodeService.getUseCode(CommonCode.CNSL_TYPE_DTL_CODE);

		Map<String, Object> inqryMap = counselService.getInqryDetail(bdMemberCounsel);
		List<BdMemberCounselAttachFile> inqryFileList = new ArrayList<>();
		for (BdMemberCounselAttachFile file : (List<BdMemberCounselAttachFile>) inqryMap.get("attachFileList")) {
			if (UtilsText.equals(Const.COUNSEL_INQRY_FILE_GBN_TYPE, file.getAtchFileGbnType())) {
				inqryFileList.add(file);
			}
		}
		BdMemberCounsel counselParam = (BdMemberCounsel) inqryMap.get("inqryDetail");

		rsMap.put(CommonCode.CNSL_TYPE_CODE, cnslTypeList);
		rsMap.put(CommonCode.CNSL_TYPE_DTL_CODE, cnslTypeDtlList);
		rsMap.put("inqryDetail", counselParam);
		rsMap.put("attachFileList", inqryFileList);

		return rsMap;
	}

	/**
	 * @Desc : 고객의소리 상세 가져오기
	 * @Method Name : getUpdateInqryForm
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUpdateVocForm(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<String, Object>();

		SyCodeDetail codeParam = new SyCodeDetail();
		codeParam.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		codeParam.setAddInfo1(CommonCode.CNSL_TYPE_CODE_VOC);
		List<SyCodeDetail> cnslTypeDtlList = commonCodeService.getUseCodeByAddInfo1(codeParam);

		BdMemberCounsel counselParam = counselDao.selectInqryDetail(bdMemberCounsel);

		rsMap.put(CommonCode.CNSL_TYPE_DTL_CODE, cnslTypeDtlList);
		rsMap.put("vocDetail", counselParam);

		return rsMap;
	}

	/**
	 * @Desc : 1:1문의 삭제
	 * @Method Name : deleteInqry
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public Map<String, Object> deleteInqry(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();
		try {
			counselService.deleteInqryAttachFile(bdMemberCounsel);

			counselDao.deleteAdminMemo(bdMemberCounsel);

			counselDao.delete(bdMemberCounsel);

			rsMap.put("result", 1);
		} catch (Exception e) {
			rsMap.put("result", 0);
			rsMap.put("Message", e.toString());
		}
		return rsMap;
	}

	/**
	 * @Desc : 1:1 문의 첨부파일 삭제
	 * @Method Name : deleteInqryAttachFile
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void deleteInqryAttachFile(BdMemberCounsel bdMemberCounsel) throws Exception {
		bdAttachFileDao.deleteInqryFile(bdMemberCounsel);
	}

	/**
	 * @Desc : 고객의소리 등록 페이지 호출
	 * @Method Name : getCreateVocForm
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCreateVocForm(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();

		SyCodeDetail vocCodeValue = new SyCodeDetail();
		vocCodeValue.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		vocCodeValue.setAddInfo1(CommonCode.CNSL_TYPE_CODE_VOC);

		List<SyCodeDetail> cnslTypeDtlCode = commonCodeService.getUseCodeByAddInfo1(vocCodeValue);
		rsMap.put("cnslTypeDtlCode", cnslTypeDtlCode);

		return rsMap;
	}

	/**
	 * @Desc : 고객의소리 등록
	 * @Method Name : setVocDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setVocDetail(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();
		try {
			counselDao.insertVocDetail(bdMemberCounsel);

			rsMap.put("result", 1);
		} catch (Exception e) {
			rsMap.put("result", 0);
			rsMap.put("Message", e.toString());
		}
		return rsMap;
	}

	/**
	 * @Desc : 고객의 소리 수정
	 * @Method Name : updateVocDetail
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateVocDetail(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();
		try {
			counselDao.updateVocDetail(bdMemberCounsel);

			rsMap.put("result", 1);
		} catch (Exception e) {
			rsMap.put("result", 0);
			rsMap.put("Message", e.toString());
		}
		return rsMap;
	}

	/**
	 * @Desc : 상담리스트에서 상세보기시에 파일 불러오기
	 * @Method Name : getAttachFile
	 * @Date : 2019. 4. 10.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAttachFile(BdMemberCounsel bdMemberCounsel) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();
		try {
			List<BdMemberCounselAttachFile> fileList = bdAttachFileDao.selectInqryFileDetail(bdMemberCounsel);

			rsMap.put("result", 1);
			rsMap.put("fileList", fileList);
		} catch (Exception e) {
			rsMap.put("result", 0);
			rsMap.put("Message", e.toString());

		}

		return rsMap;
	}

}
