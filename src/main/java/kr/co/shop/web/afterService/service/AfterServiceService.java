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
	 * @Desc : A/S ?????? ?????? ??????
	 * @Method Name : getAfterserviceRequestList
	 * @Date : 2019. 3. 25.
	 * @Author : ?????????
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
	 * @Desc : AS?????? ???????????? ?????? ???????????? ???????????? ??????
	 * @Method Name : getUseCodeForAsRequest
	 * @Date : 2019. 4. 3.
	 * @Author : ?????????
	 * @param String
	 * @return List<SyCodeDetail>
	 * @throws Exception
	 */
	public List<SyCodeDetail> getUseCodeForAsRequest(String codeField) throws Exception {

		return commonCodeService.getUseCode(codeField);
	};

	/**
	 * @Desc : ????????????????????? ?????? ?????? ?????? ??????
	 * @Method Name : getOrderProductList
	 * @Date : 2019. 3. 26.
	 * @Author : ?????????
	 * @param Pageable<OcOrderProduct, OcOrderProduct>
	 * @return Page<OcOrderProduct>
	 * @throws Exception
	 */
	public Page<OcOrderProduct> getOrderProductList(Pageable<OcOrderProduct, OcOrderProduct> pageable)
			throws Exception {

		// ?????????, ????????? ??????
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
	 * E : ?????????
	 **************************************************************************************/

	/**
	 * @Desc : FO AS??????
	 * @Method Name : createOcAsAccept
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param ocAsAccept
	 * @throws Exception
	 */
	public Map<String, Object> setOcAsAccept(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// ?????? ??? ????????? ????????? ??????.
		KcpPaymentApproval kcpPaymentApproval = ocAsAccept.getKcpPaymentApproval();

		// ?????? ????????? ??????
		OcOrder ocOrderParam = new OcOrder();
		ocOrderParam.setOrderNo(ocAsAccept.getOrderNo());
		OcOrder ocOrder = ocOrderDao.selectByPrimaryKey(ocOrderParam);

		// FO,MO ?????? ???????????? ????????? ????????? SET
		setOcAsAcceptParam(ocAsAccept, ocOrder);

		// ??????????????? INSERT
		ocAsAcceptDao.insertOcAsAccept(ocAsAccept);

		// ?????? ????????? ??????
		OcOrderProduct ocOrderProductParam = new OcOrderProduct();
		ocOrderProductParam.setOrderNo(ocAsAccept.getOrderNo());
		ocOrderProductParam.setOrderPrdtSeq(ocAsAccept.getOrderPrdtSeq());
		OcOrderProduct ocOrderProduct = ocOrderProductDao.selectByPrimaryKey(ocOrderProductParam);

		// AS ??????
		setOcAsAcceptProduct(ocAsAccept, ocOrderProduct);

		// ??????????????? ???????????? INSERT
		if (ocAsAccept.getAttachFiles() != null) {
			OcAsAcceptAttachFile[] files = ocAsAccept.getAttachFiles();
			for (OcAsAcceptAttachFile file : files) {
				file.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
				file.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
				ocAsAcceptAttachFileDao.insertOcAsAcceptAttchFile(file);
			}
		}

		// ???????????? ????????? ??????
		String newHistorSeq = getAsAcceptPrdtStatHistSeqNextVal(ocAsAccept.getAsAcceptNo(),
				ocAsAccept.getAsAcceptPrdtSeq());
		OcAsAcceptProductStatusHistory histor = new OcAsAcceptProductStatusHistory();
		setAsAcceptProductStatusHistoryParam(ocAsAccept, histor, newHistorSeq);
		ocAsAcceptProductStatusHistoryDao.insert(histor);

		// ????????? ?????? ??????
		if (ocAsAccept.getAddDlvyAmt() > 0) {
			// ?????? OC_AS_PAYMENT INSERT
			OcAsPayment ocAsPayment = new OcAsPayment();
			ocAsPayment.setAsAcceptNo(ocAsAccept.getAsAcceptNo());// ????????????
			ocAsPayment.setPymntDtm(null); // ????????????
			ocAsPayment.setDeviceCode(CommonCode.DEVICE_PC); // ??????????????????
			ocAsPayment.setMainPymntMeansYn(Const.BOOLEAN_TRUE); // ?????????????????????
			if (UtilsText.equals(ocAsAccept.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
				ocAsPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_CARD); // ??????????????????-????????????
			} else {
				ocAsPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER); // ??????????????????-????????? ????????????

			}
			ocAsPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP); // ??????????????????
			ocAsPayment.setPymntOrganCodeText(null); // ??????????????????
			ocAsPayment.setPymntOrganNoText(null); // ??????????????????(????????????, ??????????????????)
			ocAsPayment.setPymntOrganName(null); // ???????????????
			ocAsPayment.setInstmtTermCount((short) 0); // ????????????
			ocAsPayment.setPymntAprvNoText(null); // ??????????????????
			ocAsPayment.setPymntTodoAmt(ocAsAccept.getAddDlvyAmt()); // ??????????????????
			ocAsPayment.setPymntAmt(ocAsAccept.getAddDlvyAmt()); // ????????????
			ocAsPayment.setCardPartCnclPsbltYn(Const.BOOLEAN_FALSE); // ??????????????????????????????
			ocAsPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE); // ??????????????????????????? ??????????????? UPDATE
			ocAsPayment.setEscrApplyYn(Const.BOOLEAN_FALSE); // ????????????????????????
			ocAsPayment.setEscrAprvNoText(null); // ????????????????????????
			ocAsPayment.setBankCode(null); // ??????:??????????????????, ??????:??????????????????
			ocAsPayment.setAcntNoText(null); // ????????????
			ocAsPayment.setAcntHldrName(null); // ????????????
			ocAsPayment.setVrtlAcntIssueDtm(null); // ????????????????????????
			ocAsPayment.setVrtlAcntExprDtm(null); // ????????????????????????
			ocAsPayment.setRspnsCodeText(null); // ????????????
			ocAsPayment.setRspnsMesgText(null); // ???????????????
			ocAsPayment.setRedempRfndOpetrNo(ocAsAccept.getRgsterNo()); // ???????????????????????????
			ocAsPayment.setRedempRfndOpetrDtm(null); // ????????????????????????
			ocAsPayment.setRedempRfndGbnType(CommonCode.REDEMP_RFND_GBN_TYPE_REDEMP); // ??????????????????-??????
			ocAsPayment.setMmnyProcTrgtYn(Const.BOOLEAN_FALSE); // ???????????????????????? AS ??? ??????
			ocAsPayment.setOcrncRsnCode(CommonCode.OCRNC_RSN_CODE_DELIVERY_AMT); // ?????????????????? - ?????????
			ocAsPayment.setPymntStatCode(null); // ??????????????????
			ocAsPayment.setPymntLogInfo(null); // ????????????
			// ocAsPayment.setRgsterNo(ocAsAccept.getRgsterNo()); // ?????????
			ocAsPayment.setModerNo(ocAsAccept.getRgsterNo()); // ?????????
			ocAsPayment.setModDtm(null); // ????????????

			ocAsPaymentDao.insertOcAsPayment(ocAsPayment);

			kcpPaymentApproval.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
			kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
			PaymentResult paymentResult = payment
					.approval(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentApproval)); // KCP ??????

			// ?????? ????????? ?????? exception
			if (UtilsText.equals(paymentResult.getSuccessYn(), Const.BOOLEAN_FALSE)) {
				map.put("successYn", Const.BOOLEAN_FALSE);
				throw new Exception(paymentResult.getMessage());
			}

			KcpPaymentApprovalReturn kcpPaymentApprovalReturn = ((KcpPaymentApprovalReturn) paymentResult.getData());
			log.info("#################### :: " + paymentResult.toString());
			log.info("#################### :: " + kcpPaymentApprovalReturn.toString());
			List<SyCodeDetail> bankList = commonCodeService.getCode(CommonCode.BANK_CODE); // ???????????? ??????

			// kcp ?????? bankcode??? ???????????? ??????????????? bankcode ??? ??????
			String commonBankCode = bankList.stream()
					.filter(x -> UtilsText.equals(x.getAddInfo1(), kcpPaymentApprovalReturn.getBankCode()))
					.map(SyCodeDetail::getCodeDtlNo).findFirst().orElse(null);

			// ?????? ???????????? OC_AS_PAYMENT ????????????
			// TODO ???????????? ????????????.
			OcAsPayment ocAsPaymentResult = new OcAsPayment();
			if (UtilsText.equals(ocAsAccept.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
				ocAsPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getCardCd());
				ocAsPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getCardName());
				ocAsPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getCardNo());
				ocAsPaymentResult.setInstmtTermCount(Short.valueOf(kcpPaymentApprovalReturn.getQuota()));
				// [????????????]?????? ???????????? (???????????? ??????), 0 : ??????, 1 : ??????
				if ("0".equals(kcpPaymentApprovalReturn.getCardBinType01())) {
					ocAsPaymentResult.setCardType(CommonCode.CARD_TYPE_NORMAL);
				} else {
					ocAsPaymentResult.setCardType(CommonCode.CARD_TYPE_CORPORATE);
				}
				// [????????????]?????? ????????????(???????????? ??????), 0 : ??????, 1 : ??????
				if ("0".equals(kcpPaymentApprovalReturn.getCardBinType02())) {
					ocAsPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_NORMAL);
				} else {
					ocAsPaymentResult.setCardGbnType(CommonCode.CARD_GBN_TYPE_CHECK);
				}

				ocAsPaymentResult.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
				ocAsPaymentResult.setCashRcptAprvNo(UtilsText.EMPTY);
				ocAsPaymentResult.setCashRcptDealNo(UtilsText.EMPTY);
			} else {
				ocAsPaymentResult.setPymntOrganCodeText(kcpPaymentApprovalReturn.getBankCode()); // ??????????????????(??????, ????????????...)
				ocAsPaymentResult.setPymntOrganName(kcpPaymentApprovalReturn.getBankName()); // ???????????????(??????, ?????????...)
				ocAsPaymentResult.setPymntOrganNoText(kcpPaymentApprovalReturn.getAccount()); // ??????????????????(????????????, ??????????????????...)
				ocAsPaymentResult.setBankCode(commonBankCode); // ?????? ????????????
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
			ocAsPaymentResult.setPymntAprvNoText(kcpPaymentApprovalReturn.getTno()); // ??????????????????
			ocAsPaymentResult.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH); // ??????????????????
			ocAsPaymentResult.setRspnsCodeText(kcpPaymentApprovalReturn.getResCd()); // ????????????
			ocAsPaymentResult.setRspnsMesgText(kcpPaymentApprovalReturn.getResMsg()); // ???????????????
			ocAsPaymentResult.setRedempRfndOpetrNo(ocAsAccept.getRgsterNo()); // ???????????????????????????
			ocAsPaymentResult.setPymntLogInfo(new ObjectMapper().writeValueAsString(kcpPaymentApprovalReturn)); // ????????????
			ocAsPaymentResult.setModerNo(ocAsAccept.getRgsterNo()); // ?????????
			ocAsPaymentResult.setAsAcceptNo(ocAsPayment.getAsAcceptNo());
			ocAsPaymentResult.setAsPymntSeq(ocAsPayment.getAsPymntSeq());

			try {
				// AS ?????? ??????(kcp ?????? ??? ??????)
				ocAsPaymentDao.updateOcAsPaymentAccount(ocAsPaymentResult);
			} catch (Exception e) {
				// AS ?????? ????????? ????????? ?????? kcp card ?????? ??????
				KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
				kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
				kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
				kcpPaymentCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP ????????? ????????????
				kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
				kcpPaymentCancel.setCustIp(kcpPaymentApproval.getCustIp()); // ?????? ????????? IP
				kcpPaymentCancel.setModDesc("????????? ?????? ??????"); // ?????? ??????

				// ????????? ???????????? ????????? ?????? ??????
				payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));

				map.put("successYn", Const.BOOLEAN_FALSE);
				throw new Exception(e.getMessage());
			}
		} // ????????? ?????? ????????? ?????? END if

		// TODO
		// ?????? ?????? ?????? ?????? ??????
//		MbMember mbMember = new MbMember();
//		mbMember.setMemberNo(ocAsAccept.getRgsterNo());
//		MbMember mbMemberData = mbMemberDao.selectByPrimaryKey(mbMember);

		// mail start
//		MailTemplateVO mail = new MailTemplateVO();
//		mail.setRealTime(true);
//		mail.setMailTemplateId(MailCode.MEMBER_CERTIFICATION_NUMBER);
		// ????????? ?????? ??? ???????????????.
//		mail.setMailTemplateModel("");
//		mail.setReceiverMemberNo(mbMemberData.getMemberNo());
//		mail.setReceiverName(mbMemberData.getMemberName());
//		mail.setReceiverEmail(mbMemberData.getEmailAddrText());
//		try {
//			mailService.sendMail(mail);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}

		// ???????????? message , kakao talk
//		MessageVO messageVO = new MessageVO();
//		messageVO.setMesgContText("[A-RT] ???????????? : (" + ocAsAccept.getAsAcceptNo() + ") ??? ?????????????????????.");
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
	 * @Desc : ?????????????????? INSERT ?????? SETTER
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
	 * @Desc : ??????????????? SETTER
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
	 * @Desc : ???????????? SETTER
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
	 * @Desc : ???????????? SEQ ????????????.
	 * @Method Name : getAsAcceptPrdtStatHistSeqNextVal
	 * @Date : 2019. 4. 2.
	 * @Author : lee
	 * @param asAcceptNo
	 * @param asAcceptPrdtSeq
	 * @return
	 * @throws Exception
	 */
	public String getAsAcceptPrdtStatHistSeqNextVal(String asAcceptNo, Short asAcceptPrdtSeq) throws Exception {
		// ???????????? ??????
		String newHistorySeq = ocAsAcceptProductStatusHistoryDao.selectAsAcceptPrdtStatHistSeqNextVal(asAcceptNo,
				asAcceptPrdtSeq);
		return newHistorySeq;
	}

	/**
	 * @Desc :AS?????? ?????? ??????
	 * @Method Name : getAsAcceptDetailInfo
	 * @Date : 2019. 4. 10.
	 * @Author : lee
	 * @param ocAsAccept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAsAcceptDetailInfo(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// AS ?????? ??????
		OcAsAccept asAcceptInfo = ocAsAcceptDao.selectOcAsAcceptDetailInfo(ocAsAccept);
		resultMap.put("asAcceptInfo", asAcceptInfo);

		// AS ?????? ??????
		OcAsAcceptProduct ocAsAcceptProductParam = new OcAsAcceptProduct();
		ocAsAcceptProductParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductParam.setAsAcceptPrdtSeq(asAcceptInfo.getAsAcceptPrdtSeq());
		OcAsAcceptProduct ocAsAcceptProduct = ocAsAcceptProductDao
				.selectAsAcceptProductDetailInfo(ocAsAcceptProductParam);
		resultMap.put("ocAsAcceptProductInfo", ocAsAcceptProduct);
		// ?????? ?????? ?????? ??????
		OcAsAcceptAttachFile ocAsAcceptAttachFileParam = new OcAsAcceptAttachFile();
		ocAsAcceptAttachFileParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptAttachFileParam.setAsAcceptPrdtSeq(asAcceptInfo.getAsAcceptPrdtSeq());
		List<OcAsAcceptAttachFile> ocAsAcceptAttachFile = ocAsAcceptAttachFileDao
				.selectAsAcceptAttachFileList(ocAsAcceptAttachFileParam);
		resultMap.put("ocAsAcceptAttachFileList", ocAsAcceptAttachFile);

		// ???????????? ??????
		OcAsPayment ocAsPaymentParam = new OcAsPayment();
		ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsPaymentParam.setAsPymntSeq(asAcceptInfo.getAsAcceptPrdtSeq());
		OcAsPayment ocAsPayment = ocAsPaymentDao.selectAsPymntDetailInfo(ocAsPaymentParam);
		resultMap.put("ocAsPaymentInfo", ocAsPayment);

		return resultMap;

	}

	/**
	 * @Desc :AS?????? ?????? ( ????????? ????????? ?????? ??????)
	 * @Method Name : setWithdrawalAccept
	 * @Date : 2019. 4. 10.
	 * @Author : lee
	 * @param ocAsAccept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setWithdrawalAccept(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// AS??????????????? UPDATE
		ocAsAccept.setAsStatCode(CommonCode.AS_PRDT_STAT_CODE_WITHDRAWAL_ACCEPT);
		ocAsAccept.setModerNo(ocAsAccept.getModerNo());
		ocAsAcceptDao.updateOcAsAcceptWithdrawal(ocAsAccept);

		// AS ?????? ??????
		OcAsAcceptProduct ocAsAcceptProductParam = new OcAsAcceptProduct();
		ocAsAcceptProductParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductParam.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		ocAsAcceptProductParam.setAsPrdtStatCode(CommonCode.AS_PRDT_STAT_CODE_WITHDRAWAL_ACCEPT);
		ocAsAcceptProductParam.setModerNo(ocAsAccept.getModerNo());
		ocAsAcceptProductDao.updateOcAsProductPrdtStatCode(ocAsAcceptProductParam);

		// ???????????? ????????? ??????
		String newHistorSeq = getAsAcceptPrdtStatHistSeqNextVal(ocAsAccept.getAsAcceptNo(),
				ocAsAccept.getAsAcceptPrdtSeq());
		OcAsAcceptProductStatusHistory histor = new OcAsAcceptProductStatusHistory();
		histor.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		histor.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		histor.setAsAcceptPrdtStatHistSeq(Short.parseShort(newHistorSeq));
		histor.setAsPrdtStatCode(CommonCode.AS_PRDT_STAT_CODE_WITHDRAWAL_ACCEPT);
		histor.setRgsterNo(ocAsAccept.getModerNo());
		ocAsAcceptProductStatusHistoryDao.insert(histor);

		// ???????????? ??????
		OcAsPayment ocAsPaymentParam = new OcAsPayment();
		ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsPaymentParam.setAsPymntSeq(ocAsAccept.getAsAcceptPrdtSeq());
		OcAsPayment ocAsPayment = ocAsPaymentDao.selectByPrimaryKey(ocAsPaymentParam);

		if (ocAsPayment != null) {
			// AS ?????? ??????
			KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
			kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.claim.siteCode"));
			kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.claim.siteKey"));
			kcpPaymentCancel.setTno(ocAsPayment.getPymntAprvNoText()); // ?????? ????????????
			kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
			kcpPaymentCancel.setCustIp(kcpPaymentCancel.getCustIp()); // ?????? ????????? IP
			kcpPaymentCancel.setModDesc("AS??????????????? ?????? ?????? "); // ?????? ??????

			PaymentResult result = payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));
			KcpPaymentCancelReturn kcpPaymentCancelReturn = ((KcpPaymentCancelReturn) result.getData());

			log.debug("########################{}###########", result.toString());
			log.debug("########################{}###########", kcpPaymentCancelReturn.toString());

			if (UtilsText.equals(result.getSuccessYn(), Const.BOOLEAN_FALSE)) {
				// ?????? ?????? ????????????
				ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
				ocAsPaymentParam.setAsPymntSeq(ocAsAccept.getAsAcceptPrdtSeq());
				ocAsPaymentParam.setRspnsCodeText(kcpPaymentCancelReturn.getResCd()); // ????????????
				ocAsPaymentParam.setRspnsMesgText(kcpPaymentCancelReturn.getResMsg()); // ???????????????
				ocAsPaymentParam.setPymntLogInfo(new ObjectMapper()
						.writeValueAsString(kcpPaymentCancelReturn + "::???????????? ????????? ::" + result.getData()));
				ocAsPaymentParam.setModerNo(ocAsAccept.getModerNo());
				ocAsPaymentDao.updateOcAsPaymentAmtCancel(ocAsPaymentParam);
				resultMap.put("successYn", Const.BOOLEAN_FALSE);
				resultMap.put("errorMessage", "OC_AS_PAYMNET UPDATE ?????? :: " + result.getMessage());
				throw new Exception(result.getMessage());
			}
			// ?????? ?????? ???????????? ?????? ?????? ????????? ?????? PYMENT ????????? ???????????? ???.
			ocAsPaymentParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
			ocAsPaymentParam.setAsPymntSeq(ocAsAccept.getAsAcceptPrdtSeq());
			ocAsPaymentParam.setRspnsCodeText(kcpPaymentCancelReturn.getResCd()); // ????????????
			ocAsPaymentParam.setRspnsMesgText(kcpPaymentCancelReturn.getResMsg()); // ???????????????
			ocAsPaymentParam.setRedempRfndOpetrNo(ocAsAccept.getModerNo()); // ???????????????
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
				resultMap.put("errorMessage", "OC_AS_PAYMNET UPDATE ?????? :: " + e.getMessage());
				throw new Exception("OC_AS_PAYMNET UPDATE ??????");
			}
		}

		resultMap.put("successYn", Const.BOOLEAN_TRUE);
		return resultMap;

	}

	/**
	 * @Desc : AS?????? ??????
	 * @Method Name : updateOcAsAccept
	 * @Date : 2019. 4. 10.
	 * @Author : lee
	 * @param ocAsAccept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateOcAsAccept(OcAsAccept ocAsAccept) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// ??????????????? ??????
		ocAsAcceptDao.updateOcAsAccept(ocAsAccept);

		// ???????????? ??????
		OcAsAcceptProduct ocAsAcceptProductParam = new OcAsAcceptProduct();
		ocAsAcceptProductParam.setAsRsnCode(ocAsAccept.getAsRsnCode());
		ocAsAcceptProductParam.setAsAcceptContText(ocAsAccept.getAsAcceptContText());
		ocAsAcceptProductParam.setModerNo(ocAsAccept.getModerNo());
		ocAsAcceptProductParam.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
		ocAsAcceptProductParam.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
		ocAsAcceptProductDao.updateOcAsAcceptProduct(ocAsAcceptProductParam);

		// ???????????? ????????? ?????? ????????? ????????? ????????????
		OcAsAcceptAttachFile[] files = ocAsAccept.getAttachFiles();
		if (null != ocAsAccept.getRemoveAtchFileSeq()) {
			// ?????? ?????? DB ??????
			for (int atchFileSeq : ocAsAccept.getRemoveAtchFileSeq()) {
				OcAsAcceptAttachFile removeFile = new OcAsAcceptAttachFile();
				removeFile.setAsAcceptNo(ocAsAccept.getAsAcceptNo());
				removeFile.setAsAcceptPrdtSeq(ocAsAccept.getAsAcceptPrdtSeq());
				removeFile.setAtchFileSeq(atchFileSeq);
				ocAsAcceptAttachFileDao.deleteOcAsAcceptAttachFileByAtchFileSeq(removeFile);
			}
			// ????????? ?????? INSERT ????????? ???????????? ?????? ???) 1, 3?????? ???????????? 1,3 ?????? insert
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
			// ????????? ???????????? ?????????
			if (null != files) {
				for (OcAsAcceptAttachFile file : files) {
					// ???????????? ????????? ???????????? ?????? ???????????? ????????? insert ???????????? ??????
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
