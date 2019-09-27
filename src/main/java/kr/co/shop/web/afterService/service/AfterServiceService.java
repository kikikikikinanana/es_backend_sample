package kr.co.shop.web.afterService.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.payment.PaymentEntrance;
import kr.co.shop.interfaces.module.payment.base.PaymentResult;
import kr.co.shop.interfaces.module.payment.base.model.PaymentInfo;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApprovalReturn;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancel;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancelReturn;
import kr.co.shop.web.afterService.model.master.OcAsAccept;
import kr.co.shop.web.afterService.model.master.OcAsAcceptAttachFile;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProduct;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProductStatusHistory;
import kr.co.shop.web.afterService.model.master.OcAsPayment;
import kr.co.shop.web.afterService.repository.master.OcAsAcceptAttachFileDao;
import kr.co.shop.web.afterService.repository.master.OcAsAcceptDao;
import kr.co.shop.web.afterService.repository.master.OcAsAcceptProductDao;
import kr.co.shop.web.afterService.repository.master.OcAsAcceptProductStatusHistoryDao;
import kr.co.shop.web.afterService.repository.master.OcAsPaymentDao;
import kr.co.shop.web.cmm.service.MailService;
import kr.co.shop.web.cmm.service.MessageService;
import kr.co.shop.web.member.repository.master.MbMemberDao;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.order.repository.master.OcOrderDao;
import kr.co.shop.web.order.repository.master.OcOrderProductDao;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AfterServiceService {

	@Autowired
	OcAsAcceptProductDao ocAsAcceptProductDao;

	@Autowired
	OcOrderProductDao ocOrderProductDao;

	@Autowired
	OcOrderDao ocOrderDao;

	@Autowired
	OcAsAcceptDao ocAsAcceptDao;

	@Autowired
	OcAsAcceptProductStatusHistoryDao ocAsAcceptProductStatusHistoryDao;

	@Autowired
	OcAsAcceptAttachFileDao ocAsAcceptAttachFileDao;

	@Autowired
	CommonCodeService commonCodeService;

	@Autowired
	OcAsPaymentDao ocAsPaymentDao;

	@Autowired
	private PaymentEntrance payment;

	@Autowired
	private MbMemberDao mbMemberDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private MessageService messageService;

	/**
	 * @Desc : A/S 신청 목록 조회
	 * @Method Name : getAfterserviceRequestList
	 * @Date : 2019. 3. 25.
	 * @Author : 이강수
	 * @param Pageable<OcAsAcceptProduct, OcAsAcceptProduct>
	 * @return Page<OcAsAcceptProduct>
	 * @throws Exception
	 */
	public Page<OcAsAcceptProduct> getAfterserviceRequestList(Pageable<OcAsAcceptProduct, OcAsAcceptProduct> pageable)
			throws Exception {

		int count = ocAsAcceptProductDao.selectAsAcceptProductListCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			List<OcAsAcceptProduct> list = ocAsAcceptProductDao.selectAsAcceptProductList(pageable);

			pageable.setContent(list);
		}

		return pageable.getPage();
	}

	/**
	 * @Desc : AS신청 페이지를 위한 사용중인 공통코드 조회
	 * @Method Name : getUseCodeForAsRequest
	 * @Date : 2019. 4. 3.
	 * @Author : 이강수
	 * @param String
	 * @return List<SyCodeDetail>
	 * @throws Exception
	 */
	public List<SyCodeDetail> getUseCodeForAsRequest(String codeField) throws Exception {

		return commonCodeService.getUseCode(codeField);
	};

	/**
	 * @Desc : 구매확정상태의 주문 상품 목록 조회
	 * @Method Name : getOrderProductList
	 * @Date : 2019. 3. 26.
	 * @Author : 이강수
	 * @param Pageable<OcOrderProduct, OcOrderProduct>
	 * @return Page<OcOrderProduct>
	 * @throws Exception
	 */
	public Page<OcOrderProduct> getOrderProductList(Pageable<OcOrderProduct, OcOrderProduct> pageable)
			throws Exception {

		// 사은품, 배송비 제외
		String[] prdtTypeCodeList = { CommonCode.PRDT_TYPE_CODE_GIFT, CommonCode.PRDT_TYPE_CODE_DELIVERY };
		pageable.getBean().setPrdtTypeCodeList(prdtTypeCodeList);

		int count = ocOrderProductDao.selectOrderProductListCount(pageable);
		pageable.setTotalCount(count);

		if (count > 0) {
			pageable.getBean().setDlvyTypeCodeConveniencePickup(CommonCode.DLVY_TYPE_CODE_CONVENIENCE_PICKUP);
			pageable.getBean().setDlvyTypeCodeStorePickup(CommonCode.DLVY_TYPE_CODE_STORE_PICKUP);

			List<OcOrderProduct> list = ocOrderProductDao.selectOrderProductList(pageable);

			pageable.setContent(list);
		}

		return pageable.getPage();
	}

	/***************************************************************************************
	 * E : 이강수
	 **************************************************************************************/

	/**
	 * @Desc : FO AS접수
	 * @Method Name : createOcAsAccept
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param ocAsAccept
	 * @throws Exception
	 */
	public Map<String, Object> setOcAsAccept(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 결제 에 필요한 정보를 받음.
		KcpPaymentApproval kcpPaymentApproval = ocAsAccept.getKcpPaymentApproval();

		// 주문 마스터 조회
		OcOrder ocOrderParam = new OcOrder();
		ocOrderParam.setOrderNo(ocAsAccept.getOrderNo());
		OcOrder ocOrder = ocOrderDao.selectByPrimaryKey(ocOrderParam);

		// FO,MO 접수 마스터에 필요한 정보를 SET
		setOcAsAcceptParam(ocAsAccept, ocOrder);

		// 접수마스트 INSERT
		ocAsAcceptDao.insertOcAsAccept(ocAsAccept);

		// 주문 상품을 조회
		OcOrderProduct ocOrderProductParam = new OcOrderProduct();
		ocOrderProductParam.setOrderNo(ocAsAccept.getOrderNo());
		ocOrderProductParam.setOrderPrdtSeq(ocAsAccept.getOrderPrdtSeq());
		OcOrderProduct ocOrderProduct = ocOrderProductDao.selectByPrimaryKey(ocOrderProductParam);

		// AS 접수
		setOcAsAcceptProduct(ocAsAccept, ocOrderProduct);

		// 첨부파일이 존재하면 INSERT
		if (ocAsAccept.getAttachFiles() != null) {
			OcAsAcceptAttachFile[] files = ocAsAccept.getAttachFiles();
			for (OcAsAcceptAttachFile file : files) {
				file.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
				file.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
				ocAsAcceptAttachFileDao.insertOcAsAcceptAttchFile(file);
			}
		}

		// 히스토리 시퀀스 채번
		String newHistorSeq = getAsAcceptPrdtStatHistSeqNextVal(ocAsAccept.getAsAcceptNo(),
				ocAsAccept.getAsAcceptPrdtSeq());
		OcAsAcceptProductStatusHistory histor = new OcAsAcceptProductStatusHistory();
		setAsAcceptProductStatusHistoryParam(ocAsAccept, histor, newHistorSeq);
		ocAsAcceptProductStatusHistoryDao.insert(histor);

		// 배송비 결제 시작
		if (ocAsAccept.getAddDlvyAmt() > 0) {
			// 먼저 OC_AS_PAYMENT INSERT
			OcAsPayment ocAsPayment = new OcAsPayment();
			ocAsPayment.setAsAcceptNo(ocAsAccept.getAsAcceptNo());// 접수번호
			ocAsPayment.setPymntDtm(null); // 결제일시
			ocAsPayment.setDeviceCode(CommonCode.DEVICE_PC); // 디바이스코드
			ocAsPayment.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // 주결제수단여부
			if (UtilsText.equals(ocAsAccept.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
				ocAsPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_CARD); // 결제수단코드-신용카드
			} else {
				ocAsPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER); // 결제수단코드-실시간 계좌이체

			}
			ocAsPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP); // 결제업체코드
			ocAsPayment.setPymntOrganCodeText(null); // 결제기관코드
			ocAsPayment.setPymntOrganNoText(null); // 결제기관번호(카드번호, 은행계좌번호)
			ocAsPayment.setPymntOrganName(null); // 결제기관명
			ocAsPayment.setInstmtTermCount((short) 0); // 할부기간
			ocAsPayment.setPymntAprvNoText(null); // 결제승인번호
			ocAsPayment.setPymntTodoAmt(ocAsAccept.getAddDlvyAmt()); // 결제예정금액
			ocAsPayment.setPymntAmt(ocAsAccept.getAddDlvyAmt()); // 결제금액
			ocAsPayment.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // 카드부분취소가능여부
			ocAsPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // 현금영수증발급여부 리턴값으로 UPDATE
			ocAsPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // 에스크로적용여부
			ocAsPayment.setEscrAprvNoText(null); // 에스크로승인번호
			ocAsPayment.setBankCode(null); // 환불:회원환불계좌, 환수:가상계좌발급
			ocAsPayment.setAcntNoText(null); // 계좌번호
			ocAsPayment.setAcntHldrName(null); // 예금주명
			ocAsPayment.setVrtlAcntIssueDtm(null); // 가상계좌발급일시
			ocAsPayment.setVrtlAcntExprDtm(null); // 가상계좌만료일시
			ocAsPayment.setRspnsCodeText(null); // 응답코드
			ocAsPayment.setRspnsMesgText(null); // 응답메시지
			ocAsPayment.setRedempRfndOpetrNo(ocAsAccept.getRgsterNo()); // 환수환불처리자번호
			ocAsPayment.setRedempRfndOpetrDtm(null); // 환수환불처리일시
			ocAsPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP); // 환수환불구분-환수
			ocAsPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // 자사처리대상여부 AS 는 문의
			ocAsPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_DELIVERY_AMT); // 발생사유코드 - 배송비
			ocAsPayment.setPymntStatCode(null); // 결제상태코드
			ocAsPayment.setPymntLogInfo(null); // 결제로그
			// ocAsPayment.setRgsterNo(ocAsAccept.getRgsterNo()); // 등록자
			ocAsPayment.setModerNo(ocAsAccept.getRgsterNo()); // 수정자
			ocAsPayment.setModDtm(null); // 수정일시

			ocAsPaymentDao.insertOcAsPayment(ocAsPayment);

			kcpPaymentApproval.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
			kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
			PaymentResult paymentResult = payment
					.approval(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentApproval)); // KCP 결제

			// 결제 실패인 경우 exception
			if (UtilsText.equals(paymentResult.getSuccessYn(), Const.BOOLEAN_FALSE)) {
				map.put("successYn", Const.BOOLEAN_FALSE);
				throw new Exception(paymentResult.getMessage());
			}

			KcpPaymentApprovalReturn kcpPaymentApprovalReturn = ((KcpPaymentApprovalReturn) paymentResult.getData());
			log.info("#################### :: " + paymentResult.toString());
			log.info("#################### :: " + kcpPaymentApprovalReturn.toString());
			List<SyCodeDetail> bankList = commonCodeService.getCode(CommonCode.BANK_CODE); // 은행코드 목록

			// kcp 리턴 bankcode를 이용하여 공통코드의 bankcode 를 추출
			String commonBankCode = bankList.stream()
					.filter(x -> UtilsText.equals(x.getAddInfo1(), kcpPaymentApprovalReturn.getBankCode()))
					.map(SyCodeDetail::getCodeDtlNo).findFirst().orElse(null);

			// 결제 성공이면 OC_AS_PAYMENT 업데이트
			// TODO 계좌번호 안들어옴.
			OcAsPayment ocAsPaymentResult = new OcAsPayment();
			if (UtilsText.equals(ocAsAccept.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
				ocAsPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getCardCd());
				ocAsPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getCardName());
				ocAsPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getCardNo());
				ocAsPaymentResult.setInstmtTermCount(Short.valueOf(kcpPaymentApprovalReturn.getQuota()));
				// [신용카드]카드 구분정보 (법인카드 여부), 0 : 개인, 1 : 법인
				if ("0".equals(kcpPaymentApprovalReturn.getCardBinType01())) {
					ocAsPaymentResult.setCardType(CommonCode.CARD_TYPE_NORMAL);
				} else {
					ocAsPaymentResult.setCardType(CommonCode.CARD_TYPE_CORPORATE);
				}
				// [신용카드]카드 구분정보(체크카드 여부), 0 : 일반, 1 : 체크
				if ("0".equals(kcpPaymentApprovalReturn.getCardBinType02())) {
					ocAsPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_NORMAL);
				} else {
					ocAsPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_CHECK);
				}

				ocAsPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
				ocAsPaymentResult.setCashRcptAprvNo(UtilsText.EMPTY);
				ocAsPaymentResult.setCashRcptDealNo(UtilsText.EMPTY);
			} else {
				ocAsPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getBankCode()); // 결제기관코드(카드, 은행코드...)
				ocAsPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getBankName()); // 결제기관명(카드, 은행명...)
				ocAsPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getAccount()); // 결제기관번호(카드번호, 은행계좌번호...)
				ocAsPaymentResult.setBankCode(commonBankCode); // 계좌 공통코드
				ocAsPaymentResult.setInstmtTermCount(Short.valueOf("0"));
				if (kcpPaymentApprovalReturn.getCashAuthno() != null
						&& kcpPaymentApprovalReturn.getCashAuthno() != "") {
					ocAsPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_TRUE);
					ocAsPaymentResult.setCashRcptAprvNo(kcpPaymentApprovalReturn.getCashAuthno());
					ocAsPaymentResult.setCashRcptDealNo(kcpPaymentApprovalReturn.getCashNo());
				} else {
					ocAsPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
				}
			}
			ocAsPaymentResult.setPymntAprvNoText(kcpPaymentApprovalReturn.getTno()); // 결제승인번호
			ocAsPaymentResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // 결제상태코드
			ocAsPaymentResult.setRspnsCodeText(kcpPaymentApprovalReturn.getResCd()); // 응답코드
			ocAsPaymentResult.setRspnsMesgText(kcpPaymentApprovalReturn.getResMsg()); // 응답메시지
			ocAsPaymentResult.setRedempRfndOpetrNo(ocAsAccept.getRgsterNo()); // 환수환불처리자번호
			ocAsPaymentResult.setPymntLogInfo(new ObjectMapper().writeValueAsString(kcpPaymentApprovalReturn)); // 결제로그
			ocAsPaymentResult.setModerNo(ocAsAccept.getRgsterNo()); // 수정자
			ocAsPaymentResult.setAsAcceptNo(ocAsPayment.getAsAcceptNo());
			ocAsPaymentResult.setAsPymntSeq(ocAsPayment.getAsPymntSeq());

			try {
				// AS 결제 수정(kcp 리턴 값 사용)
				ocAsPaymentDao.updateOcAsPaymentAccount(ocAsPaymentResult);
			} catch (Exception e) {
				// AS 결제 수정이 실패할 경우 kcp card 요청 취소
				KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
				kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
				kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
				kcpPaymentCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP 원거래 거래번호
				kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
				kcpPaymentCancel.setCustIp(kcpPaymentApproval.getCustIp()); // 변경 요청자 IP
				kcpPaymentCancel.setModDesc("가맹점 처리 실패"); // 취소 사유

				// 익셉션 발생하면 결제한 금액 취소
				payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));

				map.put("successYn", Const.BOOLEAN_FALSE);
				throw new Exception(e.getMessage());
			}
		} // 수선일 경우 배송비 결제 END if

		// TODO
		// 메일 보낼 회원 정보 조회
//		MbMember mbMember = new MbMember();
//		mbMember.setMemberNo(ocAsAccept.getRgsterNo());
//		MbMember mbMemberData = mbMemberDao.selectByPrimaryKey(mbMember);

		// mail start
//		MailTemplateVO mail = new MailTemplateVO();
//		mail.setRealTime(true);
//		mail.setMailTemplateId(MailCode.MEMBER_CERTIFICATION_NUMBER);
		// 정보를 담을 맵 생성해야함.
//		mail.setMailTemplateModel("");
//		mail.setReceiverMemberNo(mbMemberData.getMemberNo());
//		mail.setReceiverName(mbMemberData.getMemberName());
//		mail.setReceiverEmail(mbMemberData.getEmailAddrText());
//		try {
//			mailService.sendMail(mail);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}

		// 회원에게 message , kakao talk
//		MessageVO messageVO = new MessageVO();
//		messageVO.setMesgContText("[A-RT] 접수번호 : (" + ocAsAccept.getAsAcceptNo() + ") 로 등록되었습니다.");
//		messageVO.setSndrName(Const.SYS_SENDER_MESSAGE_NAME);
//		messageVO.setSendTelNoText(Const.SYS_SENDER_MESSAGE_NUMBER);
//		messageVO.setRecvTelNoText(mbMemberData.getHdphnNoText());
//		messageVO.setRcvrName(mbMemberData.getMemberName());
//		messageVO.setReal(true);
//		try {
//			messageService.setSendMessageProcess(messageVO);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}

		map.put("asAcceptNo", ocAsAccept.getAsAcceptNo());
		map.put("successYn", Const.BOOLEAN_TRUE);

		return map;
	}

	/**
	 * @Desc : 접수마스터에 INSERT 하는 SETTER
	 * @Method Name : setOcAsAcceptParam
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param asAcceptNo
	 * @param ocAsAcceptParam
	 * @param ocOrder
	 * @throws Exception
	 */
	public void setOcAsAcceptParam(OcAsAccept ocAsAcceptParam, OcOrder ocOrder) throws Exception {
		ocAsAcceptParam.setAsAcceptNo(ocAsAcceptParam.getAsAcceptNo());
		ocAsAcceptParam.setSiteNo(ocOrder.getSiteNo());
		ocAsAcceptParam.setMemberNo(ocOrder.getMemberNo());
		ocAsAcceptParam.setOrgOrderNo(ocOrder.getOrgOrderNo());
		ocAsAcceptParam.setOrgOrderDtm(ocOrder.getOrderDtm());
		ocAsAcceptParam.setMemberTypeCode(ocOrder.getMemberTypeCode());
		ocAsAcceptParam.setMbshpGradeCode(ocOrder.getMbshpGradeCode());
		ocAsAcceptParam.setEmpYn(ocOrder.getEmpYn());
		ocAsAcceptParam.setOtsVipYn(ocOrder.getOtsVipYn());
		ocAsAcceptParam.setDeviceCode(ocAsAcceptParam.getDeviceCode());
		ocAsAcceptParam.setDlvyTypeCode(ocOrder.getDlvyTypeCode());
		ocAsAcceptParam.setUnProcYn(Const.BOOLEAN_FALSE);
		ocAsAcceptParam.setAdminAcceptYn(Const.BOOLEAN_FALSE);
		ocAsAcceptParam.setAsStatCode(CommonCode.AS_STAT_CODE_ACCEPT);
	}

	/**
	 * @Desc : 접수상품에 SETTER
	 * @Method Name : setOcAsAcceptProduct
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param newAsAcceptNo
	 * @param ocAsAccept
	 * @param ocOrderProduct
	 * @throws Exception
	 */
	public void setOcAsAcceptProduct(OcAsAccept ocAsAccept, OcOrderProduct ocOrderProduct) throws Exception {

		OcAsAcceptProduct newOcAsProduct = new OcAsAcceptProduct();
		newOcAsProduct.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		newOcAsProduct.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		newOcAsProduct.setOrgOrderNo(ocAsAccept.getOrgOrderNo());
		newOcAsProduct.setOrderNo(ocAsAccept.getOrgOrderNo());
		newOcAsProduct.setOrderPrdtSeq(ocAsAccept.getOrderPrdtSeq());
		newOcAsProduct.setPrdtNo(ocOrderProduct.getPrdtNo());
		newOcAsProduct.setPrdtOptnNo(ocOrderProduct.getPrdtOptnNo());
		newOcAsProduct.setPrdtName(ocOrderProduct.getPrdtName());
		newOcAsProduct.setEngPrdtName(ocOrderProduct.getEngPrdtName());
		newOcAsProduct.setOptnName(ocOrderProduct.getOptnName());
		newOcAsProduct.setPrdtTypeCode(ocOrderProduct.getPrdtTypeCode());
		newOcAsProduct.setStyleInfo(ocOrderProduct.getStyleInfo());
		newOcAsProduct.setPrdtColorCode(ocOrderProduct.getPrdtColorCode());
		newOcAsProduct.setBrandNo(ocOrderProduct.getBrandNo());
		newOcAsProduct.setOrderAmt(ocOrderProduct.getOrderAmt());
		newOcAsProduct.setAsRsnCode(ocAsAccept.getAsRsnCode());
		newOcAsProduct.setAsAcceptContText(ocAsAccept.getAsAcceptContText());
		newOcAsProduct.setAsPrdtStatCode(CommonCode.AS_STAT_CODE_ACCEPT);
		newOcAsProduct.setRgsterNo(ocAsAccept.getRgsterNo());

		ocAsAcceptProductDao.insertOcAsAcceptProduct(newOcAsProduct);

	}

	/**
	 * @Desc : 히스트리 SETTER
	 * @Method Name : setAsAcceptProductStatusHistoryParam
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param asAcceptNo
	 * @param ocAsAccept
	 * @param ocAsAcceptProductStatusHistoryParam
	 * @param newHistorSeq
	 * @throws Exception
	 */
	public void setAsAcceptProductStatusHistoryParam(OcAsAccept ocAsAccept,
			OcAsAcceptProductStatusHistory ocAsAcceptProductStatusHistoryParam, String newHistorSeq) throws Exception {
		ocAsAcceptProductStatusHistoryParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductStatusHistoryParam.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		ocAsAcceptProductStatusHistoryParam.setAsAcceptPrdtStatHistSeq(Short.parseShort(newHistorSeq));
		ocAsAcceptProductStatusHistoryParam.setAsPrdtStatCode(CommonCode.AS_STAT_CODE_ACCEPT);
		ocAsAcceptProductStatusHistoryParam.setRgsterNo(ocAsAccept.getRgsterNo());
	}

	/**
	 * @Desc : 히스토리 SEQ 채번한다.
	 * @Method Name : getAsAcceptPrdtStatHistSeqNextVal
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param asAcceptNo
	 * @param asAcceptPrdtSeq
	 * @return
	 * @throws Exception
	 */
	public String getAsAcceptPrdtStatHistSeqNextVal(String asAcceptNo, Short asAcceptPrdtSeq) throws Exception {
		// 접수번호 채번
		String newHistorySeq = ocAsAcceptProductStatusHistoryDao.selectAsAcceptPrdtStatHistSeqNextVal(asAcceptNo,
				asAcceptPrdtSeq);
		return newHistorySeq;
	}

	/**
	 * @Desc :AS접수 상세 조회
	 * @Method Name : getAsAcceptDetailInfo
	 * @Date : 2019. 4. 10.
	 * @Author : lee
	 * @param ocAsAccept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAsAcceptDetailInfo(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// AS 상세 조회
		OcAsAccept asAcceptInfo = ocAsAcceptDao.selectOcAsAcceptDetailInfo(ocAsAccept);
		resultMap.put("asAcceptInfo", asAcceptInfo);

		// AS 상품 조회
		OcAsAcceptProduct ocAsAcceptProductParam = new OcAsAcceptProduct();
		ocAsAcceptProductParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductParam.setAsAcceptPrdtSeq(asAcceptInfo.getAsAcceptPrdtSeq());
		OcAsAcceptProduct ocAsAcceptProduct = ocAsAcceptProductDao
				.selectAsAcceptProductDetailInfo(ocAsAcceptProductParam);
		resultMap.put("ocAsAcceptProductInfo", ocAsAcceptProduct);
		// 공지 첨부 파일 정보
		OcAsAcceptAttachFile ocAsAcceptAttachFileParam = new OcAsAcceptAttachFile();
		ocAsAcceptAttachFileParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptAttachFileParam.setAsAcceptPrdtSeq(asAcceptInfo.getAsAcceptPrdtSeq());
		List<OcAsAcceptAttachFile> ocAsAcceptAttachFile = ocAsAcceptAttachFileDao
				.selectAsAcceptAttachFileList(ocAsAcceptAttachFileParam);
		resultMap.put("ocAsAcceptAttachFileList", ocAsAcceptAttachFile);

		// 결제정보 조회
		OcAsPayment ocAsPaymentParam = new OcAsPayment();
		ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsPaymentParam.setAsPymntSeq(asAcceptInfo.getAsAcceptPrdtSeq());
		OcAsPayment ocAsPayment = ocAsPaymentDao.selectAsPymntDetailInfo(ocAsPaymentParam);
		resultMap.put("ocAsPaymentInfo", ocAsPayment);

		return resultMap;

	}

	/**
	 * @Desc :AS접수 철회 ( 배송비 결제도 취소 포함)
	 * @Method Name : setWithdrawalAccept
	 * @Date : 2019. 4. 10.
	 * @Author : lee
	 * @param ocAsAccept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setWithdrawalAccept(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// AS접수마스터 UPDATE
		ocAsAccept.setAsStatCode(CommonCode.AS_PRDT_STAT_CODE_WITHDRAWAL_ACCEPT);
		ocAsAccept.setModerNo(ocAsAccept.getModerNo());
		ocAsAcceptDao.updateOcAsAcceptWithdrawal(ocAsAccept);

		// AS 상품 조회
		OcAsAcceptProduct ocAsAcceptProductParam = new OcAsAcceptProduct();
		ocAsAcceptProductParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductParam.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		ocAsAcceptProductParam.setAsPrdtStatCode(CommonCode.AS_PRDT_STAT_CODE_WITHDRAWAL_ACCEPT);
		ocAsAcceptProductParam.setModerNo(ocAsAccept.getModerNo());
		ocAsAcceptProductDao.updateOcAsProductPrdtStatCode(ocAsAcceptProductParam);

		// 히스토리 시퀀스 채번
		String newHistorSeq = getAsAcceptPrdtStatHistSeqNextVal(ocAsAccept.getAsAcceptNo(),
				ocAsAccept.getAsAcceptPrdtSeq());
		OcAsAcceptProductStatusHistory histor = new OcAsAcceptProductStatusHistory();
		histor.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		histor.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		histor.setAsAcceptPrdtStatHistSeq(Short.parseShort(newHistorSeq));
		histor.setAsPrdtStatCode(CommonCode.AS_PRDT_STAT_CODE_WITHDRAWAL_ACCEPT);
		histor.setRgsterNo(ocAsAccept.getModerNo());
		ocAsAcceptProductStatusHistoryDao.insert(histor);

		// 결제정보 조회
		OcAsPayment ocAsPaymentParam = new OcAsPayment();
		ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsPaymentParam.setAsPymntSeq(ocAsAccept.getAsAcceptPrdtSeq());
		OcAsPayment ocAsPayment = ocAsPaymentDao.selectByPrimaryKey(ocAsPaymentParam);

		if (ocAsPayment != null) {
			// AS 결제 취소
			KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
			kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
			kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
			kcpPaymentCancel.setTno(ocAsPayment.getPymntAprvNoText()); // 결제 승인번호
			kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // 전체취소 STSC / 부분취소 STPC
			kcpPaymentCancel.setCustIp(kcpPaymentCancel.getCustIp()); // 변경 요청자 IP
			kcpPaymentCancel.setModDesc("AS접수철회에 의한 취소 "); // 취소 사유

			PaymentResult result = payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
			KcpPaymentCancelReturn kcpPaymentCancelReturn = ((KcpPaymentCancelReturn) result.getData());

			log.debug("########################{}###########", result.toString());
			log.debug("########################{}###########", kcpPaymentCancelReturn.toString());

			if (UtilsText.equals(result.getSuccessYn(), Const.BOOLEAN_FALSE)) {
				// 결제 취소 실패하면
				ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
				ocAsPaymentParam.setAsPymntSeq(ocAsAccept.getAsAcceptPrdtSeq());
				ocAsPaymentParam.setRspnsCodeText(kcpPaymentCancelReturn.getResCd()); // 응답코드
				ocAsPaymentParam.setRspnsMesgText(kcpPaymentCancelReturn.getResMsg()); // 응답메시지
				ocAsPaymentParam.setPymntLogInfo(new ObjectMapper()
						.writeValueAsString(kcpPaymentCancelReturn + "::결제실패 데이타 ::" + result.getData()));
				ocAsPaymentParam.setModerNo(ocAsAccept.getModerNo());
				ocAsPaymentDao.updateOcAsPaymentAmtCancel(ocAsPaymentParam);
				resultMap.put("successYn", Const.BOOLEAN_FALSE);
				resultMap.put("errorMessage", "OC_AS_PAYMNET UPDATE 실패 :: " + result.getMessage());
				throw new Exception(result.getMessage());
			}
			// 결제 취소 정상이면 결제 취소 로그와 함계 PYMENT 테이블 업데이트 함.
			ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
			ocAsPaymentParam.setAsPymntSeq(ocAsAccept.getAsAcceptPrdtSeq());
			ocAsPaymentParam.setRspnsCodeText(kcpPaymentCancelReturn.getResCd()); // 응답코드
			ocAsPaymentParam.setRspnsMesgText(kcpPaymentCancelReturn.getResMsg()); // 응답메시지
			ocAsPaymentParam.setRedempRfndOpetrNo(ocAsAccept.getModerNo()); // 응답메시지
			ocAsPaymentParam.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REFUND);
			ocAsPaymentParam.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL);
			ocAsPaymentParam.setPymntLogInfo(
					new ObjectMapper().writeValueAsString(kcpPaymentCancelReturn + (result.getMessage())));
			ocAsPaymentParam.setModerNo(ocAsAccept.getModerNo());

			// OC_AS_PAYMENT UPDATE
			try {
				ocAsPaymentDao.updateOcAsPaymentAmtCancel(ocAsPaymentParam);
			} catch (Exception e) {
				resultMap.put("successYn", Const.BOOLEAN_FALSE);
				resultMap.put("errorMessage", "OC_AS_PAYMNET UPDATE 실패 :: " + e.getMessage());
				throw new Exception("OC_AS_PAYMNET UPDATE 실패");
			}
		}

		resultMap.put("successYn", Const.BOOLEAN_TRUE);
		return resultMap;

	}

	/**
	 * @Desc : AS접수 수정
	 * @Method Name : updateOcAsAccept
	 * @Date : 2019. 4. 10.
	 * @Author : lee
	 * @param ocAsAccept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateOcAsAccept(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// 접수마스터 수정
		ocAsAcceptDao.updateOcAsAccept(ocAsAccept);

		// 접수상품 수정
		OcAsAcceptProduct ocAsAcceptProductParam = new OcAsAcceptProduct();
		ocAsAcceptProductParam.setAsRsnCode(ocAsAccept.getAsRsnCode());
		ocAsAcceptProductParam.setAsAcceptContText(ocAsAccept.getAsAcceptContText());
		ocAsAcceptProductParam.setModerNo(ocAsAccept.getModerNo());
		ocAsAcceptProductParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductParam.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		ocAsAcceptProductDao.updateOcAsAcceptProduct(ocAsAcceptProductParam);

		// 화면에서 삭제할 파일 넘버를 받아서 삭제처리
		OcAsAcceptAttachFile[] files = ocAsAccept.getAttachFiles();
		if (null != ocAsAccept.getRemoveAtchFileSeq()) {
			// 삭제 파일 DB 삭제
			for (int atchFileSeq : ocAsAccept.getRemoveAtchFileSeq()) {
				OcAsAcceptAttachFile removeFile = new OcAsAcceptAttachFile();
				removeFile.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
				removeFile.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
				removeFile.setAtchFileSeq(atchFileSeq);
				ocAsAcceptAttachFileDao.deleteOcAsAcceptAttachFileByAtchFileSeq(removeFile);
			}
			// 새로운 파일 INSERT 삭제한 스퀀스를 사용 예) 1, 3번을 지웠으면 1,3 다시 insert
			if (null != files) {
				for (int attNewFilCnt = 0; attNewFilCnt < files.length; attNewFilCnt++) {
					int insertSeq[] = ocAsAccept.getRemoveAtchFileSeq();
					OcAsAcceptAttachFile file = new OcAsAcceptAttachFile();
					if (null != ocAsAccept.getFileName()) {
						file.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
						file.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
						file.setAtchFileSeq(insertSeq[attNewFilCnt]);
						file.setAtchFileName(files[attNewFilCnt].getAtchFileName());
						file.setAtchFileName(files[attNewFilCnt].getAtchFileName());
						file.setAtchFilePathText(files[attNewFilCnt].getAtchFilePathText());
						file.setAtchFileUrl(files[attNewFilCnt].getAtchFileUrl());
						ocAsAcceptAttachFileDao.insertOcAsAcceptAttchModifyFile(file);
					}
				}
			}
		} else {
			// 수정할 첨부파일 없다면
			if (null != files) {
				for (OcAsAcceptAttachFile file : files) {
					// 파일명은 있지만 첨부파일 처음 등록되는 경우도 insert 들어가는 조건
					if (null != ocAsAccept.getFileName() || null == ocAsAccept.getFileSeq()) {
						file.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
						file.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
						file.setAtchFileName(file.getAtchFileName());
						file.setAtchFilePathText(file.getAtchFilePathText());
						file.setAtchFileUrl(file.getAtchFileUrl());
						ocAsAcceptAttachFileDao.insertOcAsAcceptAttchFile(file);
					}
				}
			}
		}

		map.put("asAcceptNo", ocAsAccept.getAsAcceptNo());
		map.put("successYn", Const.BOOLEAN_TRUE);

		return map;
	}
}
