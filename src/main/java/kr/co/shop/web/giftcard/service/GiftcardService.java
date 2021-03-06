package kr.co.shop.web.giftcard.service;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.member.MembershipPointService;
import kr.co.shop.interfaces.module.member.model.GiftCard;
import kr.co.shop.interfaces.module.payment.PaymentEntrance;
import kr.co.shop.interfaces.module.payment.base.PaymentResult;
import kr.co.shop.interfaces.module.payment.base.model.PaymentInfo;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApprovalReturn;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentCancel;
import kr.co.shop.interfaces.module.payment.nice.NiceGiftService;
import kr.co.shop.interfaces.module.payment.nice.NmcService;
import kr.co.shop.interfaces.module.payment.nice.model.BalanceRequest;
import kr.co.shop.interfaces.module.payment.nice.model.BalanceResponse;
import kr.co.shop.interfaces.module.payment.nice.model.BalanceTransferRequest;
import kr.co.shop.interfaces.module.payment.nice.model.BalanceTransferResponse;
import kr.co.shop.interfaces.module.payment.nice.model.ChargeRequest;
import kr.co.shop.interfaces.module.payment.nice.model.ChargeResponse;
import kr.co.shop.interfaces.module.payment.nice.model.CollectionRequest;
import kr.co.shop.interfaces.module.payment.nice.model.CollectionResponse;
import kr.co.shop.interfaces.module.payment.nice.model.CommNiceRes;
import kr.co.shop.interfaces.module.payment.nice.model.NmcVO;
import kr.co.shop.interfaces.module.payment.nice.model.SaleAgenciesRequest;
import kr.co.shop.interfaces.module.payment.nice.model.SaleAgenciesResponse;
import kr.co.shop.interfaces.module.payment.nice.model.TranHistoryRequest;
import kr.co.shop.interfaces.module.payment.nice.model.TranHistoryResponse;
import kr.co.shop.web.cmm.service.MailService;
import kr.co.shop.web.cmm.vo.MailTemplateVO;
import kr.co.shop.web.giftcard.model.master.OcGiftCardInterlockHistory;
import kr.co.shop.web.giftcard.model.master.OcGiftCardOrder;
import kr.co.shop.web.giftcard.model.master.OcGiftCardOrderPayment;
import kr.co.shop.web.giftcard.model.master.OcKakaoExchangeOrder;
import kr.co.shop.web.giftcard.model.master.PdGiftCard;
import kr.co.shop.web.giftcard.model.master.RvGiftCardComparison;
import kr.co.shop.web.giftcard.repository.master.OcGiftCardInterlockHistoryDao;
import kr.co.shop.web.giftcard.repository.master.OcGiftCardOrderDao;
import kr.co.shop.web.giftcard.repository.master.OcGiftCardOrderPaymentDao;
import kr.co.shop.web.giftcard.repository.master.OcKakaoExchangeOrderDao;
import kr.co.shop.web.giftcard.repository.master.PdGiftCardDao;
import kr.co.shop.web.giftcard.repository.master.RvGiftCardComparisonDao;
import kr.co.shop.web.giftcard.vo.GiftcardVo;
import kr.co.shop.web.member.model.master.MbMemberGiftCard;
import kr.co.shop.web.member.repository.master.MbMemberGiftCardDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GiftcardService {

	@Autowired
	private NiceGiftService niceGiftService;

	@Autowired
	private NmcService nmcService;

	@Autowired
	private MailService mailService;

	@Autowired
	private MembershipPointService mpService;

	@Autowired
	private MbMemberGiftCardDao mbMemberGiftCardDao;

	@Autowired
	private PdGiftCardDao pdGiftCardDao;

	@Autowired
	private OcGiftCardOrderDao ocGiftCardOrderDao;

	@Autowired
	private OcGiftCardOrderPaymentDao ocGiftCardOrderPaymentDao;

	@Autowired
	private OcGiftCardInterlockHistoryDao ocGiftCardInterlockHistoryDao;

	@Autowired
	private OcKakaoExchangeOrderDao ocKakaoExchangeOrderDao;

	@Autowired
	private RvGiftCardComparisonDao rvGiftCardComparisonDao;

	@Autowired
	private PaymentEntrance payment;

	/**
	 * @Desc : ??????????????? ???????????? api
	 * @Method Name : getBalance
	 * @Date : 2019. 4. 12.
	 * @Author : YSW
	 * @param balanceRequest
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBalance(BalanceRequest balanceRequest) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		MbMemberGiftCard mbMemberGiftCard = new MbMemberGiftCard();
		mbMemberGiftCard.setCardNoText(balanceRequest.getCardNo());
		mbMemberGiftCard.setMemberNo(balanceRequest.getCompanyUserNo());
		MbMemberGiftCard preObj = mbMemberGiftCardDao.selectCardImgCode(mbMemberGiftCard);
		List<MbMemberGiftCard> isRegisted = mbMemberGiftCardDao.selectMyGiftCard(mbMemberGiftCard);

		// ?????? ?????? ??????
//		mbMemberGiftCard.setGiftCardNo(balanceResult.getResData().getCardImgCode());
		BalanceRequest balanceParam = null;
		if (isRegisted.size() > 0) {
			mbMemberGiftCard.setGiftCardNo(preObj.getGiftCardNo());
			balanceParam = new BalanceRequest("2018176174", balanceRequest.getCardNo(), 1, preObj.getGiftCardNo());
			resultMap.put("isRegisted", true); // ?????????????????? ??????
		} else {
			mbMemberGiftCard.setGiftCardNo("0000000001");
			balanceParam = new BalanceRequest("2018176174", balanceRequest.getCardNo(), 1, "0000000001");
			resultMap.put("isRegisted", false); // ?????????????????? ??????
		}
		resultMap.put("balanceCardInfo", mbMemberGiftCardDao.selectCardImg(mbMemberGiftCard));

		CommNiceRes<BalanceResponse> balanceResult = niceGiftService.sendBalance(balanceParam);

		resultMap.put("niceResCode", balanceResult.getResCode());
		resultMap.put("niceResMsg", balanceResult.getResMsg());
		resultMap.put("niceResData", balanceResult.getResData());

		TranHistoryRequest tranHistoryParam = new TranHistoryRequest("2018176174", balanceRequest.getCardNo(), 1, "1");
		CommNiceRes<TranHistoryResponse> tranHistoryResult = niceGiftService.sendTranHistory(tranHistoryParam);

		resultMap.put("tranHisResCode", tranHistoryResult.getResCode());
		resultMap.put("tranHisResMsg", tranHistoryResult.getResMsg());
		resultMap.put("tranHisResData", tranHistoryResult.getResData());

		// ?????????????????? ??????
		resultMap.put("giftCardInfo", mbMemberGiftCardDao.selectGiftcardInfo(mbMemberGiftCard));

		return resultMap;
	}

	public Map<String, Object> getMyGiftCardBalance(BalanceRequest balanceRequest) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		BalanceRequest balanceParam = new BalanceRequest("2018176174", balanceRequest.getCardNo(), 1);
		CommNiceRes<BalanceResponse> balanceResult = niceGiftService.sendBalance(balanceParam);

		resultMap.put("niceResCode", balanceResult.getResCode());
		resultMap.put("niceResMsg", balanceResult.getResMsg());
		resultMap.put("niceResData", balanceResult.getResData());

		if (!balanceRequest.isHistory()) {
			TranHistoryRequest tranHistoryParam = new TranHistoryRequest("2018176174", balanceRequest.getCardNo(), 1,
					"1");
			CommNiceRes<TranHistoryResponse> tranHistoryResult = niceGiftService.sendTranHistory(tranHistoryParam);

			resultMap.put("tranHisResCode", tranHistoryResult.getResCode());
			resultMap.put("tranHisResMsg", tranHistoryResult.getResMsg());
			resultMap.put("tranHisResData", tranHistoryResult.getResData());
		}

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????
	 * @Method Name : registMyGiftcard
	 * @Date : 2019. 4. 15.
	 * @Author : nalpari
	 * @param giftcardVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> registMyGiftcard(GiftcardVo giftcardVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			MbMemberGiftCard param = new MbMemberGiftCard();
			param.setMemberNo(giftcardVO.getMemberNo());
			param.setCardNoText(giftcardVO.getGiftCardNo());

			int duplicationCardCnt = mbMemberGiftCardDao.selectDuplCard(param);
			if (duplicationCardCnt == 0) {

				int count = mbMemberGiftCardDao.selectGiftCardCount(param);
				log.debug("##### count: {}", count);

				if (count < 15) {
					BalanceRequest balanceParam = new BalanceRequest("2018176174", giftcardVO.getGiftCardNo(), 1);
					CommNiceRes<BalanceResponse> balanceResult = niceGiftService.sendBalance(balanceParam);
					log.debug("##### resCode: {}", balanceResult.getResCode());
					log.debug("##### cardImgCode: {}", balanceResult.getResData().getCardImgCode());

					if ("0000".equals(balanceResult.getResCode())) {

						CollectionRequest collectionParam = new CollectionRequest("S0", "1",
								"37" + giftcardVO.getGiftCardNo() + "=4912", 1, giftcardVO.getCardPinNoText());
						CommNiceRes<CollectionResponse> collectionResult = niceGiftService
								.sendCollection(collectionParam);

						if ("0000".equals(collectionResult.getResCode())) {
							// MbMemberGiftCard param = new MbMemberGiftCard();
							// param.setMemberNo(giftcardVO.getMemberNo());
							String giftCardNo = mbMemberGiftCardDao
									.selectGiftCardNo(balanceResult.getResData().getCardImgCode());
							if (giftCardNo != null) {
								// param.setGiftCardSeq(count + 1);
								param.setGiftCardSeq(mbMemberGiftCardDao.selectMyGiftCardCnt(param) + 1);
								param.setGiftCardNo(giftCardNo);
								param.setGiftCardName(giftcardVO.getGiftCardName());
								param.setCardNoText(giftcardVO.getGiftCardNo());
								param.setCardPinNoText(giftcardVO.getCardPinNoText());
								param.setCardAmt(Integer.parseInt(balanceResult.getResData().getConBalanceAmount()));
								if (count < 1) {
									// ????????? ???????????? ????????? ????????? ??????????????? ??????
									param.setRprsntCardYn("Y");
								} else {
									if ("Y".equals(giftcardVO.getRprsntCardYn())) {
										// ????????? ????????? ?????????????????? ??????????????? ???????????? ?????? ????????? ?????? ??????????????? ?????? ????????? ??????????????? ??????
										mbMemberGiftCardDao.updateRprsntAll(param);
									}
									param.setRprsntCardYn(giftcardVO.getRprsntCardYn());
								}
								param.setDelYn("N");

								mbMemberGiftCardDao.registGiftCard(param);

								// ????????? ???????????? ??????????????? ??????????????? ????????? ???????????? ????????????
								OcGiftCardOrder ocGiftCardOrder = new OcGiftCardOrder();
								ocGiftCardOrder.setGiftCardStatCode(CommonCode.GIFT_CARD_STAT_CODE_ACCEPT);
								ocGiftCardOrder.setMemberNo(giftcardVO.getMemberNo());
								ocGiftCardOrder.setCardNoText(giftcardVO.getGiftCardNo());
								ocGiftCardOrder.setGiftCardOrderTypeCode(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_GIFT);

								ocGiftCardOrderDao.updateGiftCardStat(ocGiftCardOrder);

								resultMap.put("resCode", "0000");
								resultMap.put("resMsg", "success");
								resultMap.put("resData", "");
							} else {
								resultMap.put("resCode", "9991");
								resultMap.put("resMsg", "???????????? ?????? ?????????????????????. ?????? ??????????????????.");
								resultMap.put("resData", "");
							}
						} else {
							resultMap.put("resCode", "8041");
							resultMap.put("resMsg", "PIN????????? ???????????? ????????????. ?????? ??????????????????.");
							resultMap.put("resData", "");
						}
					} else if ("8449".equals(balanceResult.getResCode())) {
						resultMap.put("resCode", "8449");
						resultMap.put("resMsg", "???????????? ?????? ?????????????????????. ?????? ??????????????????.");
						resultMap.put("resData", "");
					} else {
						resultMap.put("resCode", "9000");
						resultMap.put("resMsg", "????????????/??????????????? ??????????????????.");
						resultMap.put("resData", "");
					}
				} else {
					resultMap.put("resCode", "9990");
					resultMap.put("resMsg",
							"?????? ?????? ????????? MY?????????????????? 15????????? ?????????. ???????????? ????????? ??????/???????????????, ???????????? ????????? ????????????????????? ??????????????? ???????????????.");
					resultMap.put("resData", count);
				}
			} else {
				resultMap.put("resCode", "9992");
				resultMap.put("resMsg", "?????? ????????? ?????????????????????.");
				resultMap.put("resData", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "error");
			resultMap.put("resData", e.getMessage());
		}

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????/??????/?????? ??????
	 * @Method Name : getGiftcardMultiuseList
	 * @Date : 2019. 4. 17.
	 * @Author : YSW
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGiftcardMultiuseList(Pageable<PdGiftCard, PdGiftCard> pageable) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<PdGiftCard> result = pdGiftCardDao.selectPdGiftCardList(pageable);

		resultMap.put("list", result);
		resultMap.put("totalCount", pdGiftCardDao.selectPdGiftCardListTotal());
//		resultMap.put("totalCount", result.size());

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????/??????/?????? ??????
	 * @Method Name : getPdGiftCardInfo
	 * @Date : 2019. 4. 17.
	 * @Author : YSW
	 * @param pdGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPdGiftCardInfo(PdGiftCard pdGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("info", pdGiftCardDao.selectPdGiftCardInfo(pdGiftCard));

		return resultMap;
	}

	/**
	 * @Desc : ??????/?????? ?????????
	 * @Method Name : setGiftCardKcp
	 * @Date : 2019. 4. 22.
	 * @Author : nalpari
	 * @param ocGiftCardOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setGiftCardKcp(OcGiftCardOrder ocGiftCardOrder) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		KcpPaymentApproval kcpPaymentApproval = ocGiftCardOrder.getKcpPaymentApproval();
		KcpPaymentApprovalReturn kcpPaymentApprovalReturn = null;

		String payCode = "00";
		if (UtilsText.equals(ocGiftCardOrder.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			payCode = "01";
		}
		SaleAgenciesRequest saleAgenciesRequest = new SaleAgenciesRequest(kcpPaymentApproval.getGoodMny(),
				kcpPaymentApproval.getGoodMny(), "01", "3", "", "A8", ocGiftCardOrder.getRcvrHdphnNoText(), "",
				"0000000001", "01", "Y", "A8", 1860, payCode, "2018176174");
		ChargeRequest chargeRequest = new ChargeRequest(ocGiftCardOrder.getCardNoText(),
				kcpPaymentApproval.getGoodMny(), "01", "3", "01", "1");

//		SaleAgenciesResponse resObj = null;
		CommNiceRes<SaleAgenciesResponse> saleAgenciesResult = null;
		CommNiceRes<ChargeResponse> chargeResult = null;

		// exception ????????? cancel ????????? ?????? ?????? ??????
		String oApprovalNo = "";
		String oApprovalDate = "";

		int paymentStep = 0;

		try {
			/**
			 * step1 - giftCardOrder ?????? ??????
			 */
			setGiftCardOrderTemp(ocGiftCardOrder, kcpPaymentApproval);
			paymentStep = 1;

			/**
			 * step2 - KCP Payment ??????
			 */
			kcpPaymentApproval.setSiteCd(Config.getString("pg.kcp.gift.siteCode"));
			kcpPaymentApproval.setSiteKey(Config.getString("pg.kcp.gift.siteKey"));
			log.debug("##### KCP PAYMENT START!!");
			PaymentResult paymentResult = payment
					.approval(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentApproval)); // KCP ??????
			paymentStep = 2;
			kcpPaymentApprovalReturn = ((KcpPaymentApprovalReturn) paymentResult.getData());
			log.debug("##### KCP PAYMENT END!!");
			log.info("#################### :: " + paymentResult.toString());
			log.info("#################### :: " + kcpPaymentApprovalReturn.toString());

			resultMap.put("kcpResCode", kcpPaymentApprovalReturn.getResCd());
			resultMap.put("kcpResMsg", kcpPaymentApprovalReturn.getResMsg());

			/**
			 * KCP Payment ?????? ?????? ??????
			 */
			setGiftCardKcpPayment(ocGiftCardOrder, kcpPaymentApprovalReturn);
			paymentStep = 3;

			if ("saleAgencies".equals(ocGiftCardOrder.getKcpPayType())) {
				// ??????/????????? ???????????? ??????????????? ????????? ???????????? ??????
				saleAgenciesRequest.setOrdNumber(ocGiftCardOrder.getGiftCardOrderNo());

				/**
				 * nice ?????? ????????? - ??????/??????
				 */
				saleAgenciesResult = niceGiftService.sendSaleAgencies(saleAgenciesRequest);
				paymentStep = 4;

				oApprovalNo = saleAgenciesResult.getResData().getApprovalNo();
				oApprovalDate = saleAgenciesResult.getResData().getApprovalDate();

				resultMap.put("niceResCode", saleAgenciesResult.getResCode());
				resultMap.put("niceResMsg", saleAgenciesResult.getResMsg());

				/**
				 * nice ?????? ????????? ?????? ??????
				 */
				OcGiftCardInterlockHistory ocGiftCardInterlockHistory = new OcGiftCardInterlockHistory();
				ocGiftCardInterlockHistory.setGiftCardOrderNo(ocGiftCardOrder.getGiftCardOrderNo());
				ocGiftCardInterlockHistory.setGiftCardOrderTypeCode(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_GIFT);
				ocGiftCardInterlockHistory.setSendInfo(saleAgenciesResult.getReqStr());
				ocGiftCardInterlockHistory.setRspnsInfo(saleAgenciesResult.getResStr());
				ocGiftCardInterlockHistory.setRgstDtm(null);

				ocGiftCardInterlockHistoryDao.insertGiftCardNiceHistory(ocGiftCardInterlockHistory);
				paymentStep = 5;

				/**
				 * giftCardOrder ????????????
				 */
				String aprvDt = saleAgenciesResult.getResData().getApprovalDate()
						+ saleAgenciesResult.getResData().getApprovalTime();
				log.debug("##### aprvDt: {}", aprvDt);
				String formatedAprvDt = aprvDt.substring(0, 4) + "-" + aprvDt.substring(4, 6) + "-"
						+ aprvDt.substring(6, 8) + " " + aprvDt.substring(8, 10) + ":" + aprvDt.substring(10, 12) + ":"
						+ aprvDt.substring(12, 14);
				log.debug("##### formatedAprvDt: {}", formatedAprvDt);
				SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
				Date approvalDtm = dtFormat.parse(formatedAprvDt);

				ocGiftCardOrder.setModerNo(ocGiftCardOrder.getMemberNo());
				ocGiftCardOrder.setGiftCardAprvNoText(saleAgenciesResult.getResData().getApprovalNo());
				ocGiftCardOrder.setGiftCardAprvDtm(new Timestamp(approvalDtm.getTime()));
				ocGiftCardOrder.setCardNoText(saleAgenciesResult.getResData().getConCardNo());
				ocGiftCardOrder.setCardPinNoText(saleAgenciesResult.getResData().getConPin());
				ocGiftCardOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_COMPLETE);

				ocGiftCardOrderDao.updateGiftCardPayment(ocGiftCardOrder);

				resultMap.put("cardNoText", saleAgenciesResult.getResData().getConCardNo());
				resultMap.put("couponEndDate", saleAgenciesResult.getResData().getCouponEndDate());
				resultMap.put("pymntMeansCode", ocGiftCardOrder.getPymntMeansCode());

				/**
				 * ?????? ??????
				 */
				HashMap<String, Object> mailTempObj = new HashMap<>();
				mailTempObj.put("title", "??????????????? ??????/???????????? ?????????????????????.");
				mailTempObj.put("kind", "[A-RT] [???????????????] ??????/???????????????");
				mailTempObj.put("payDtm",
						aprvDt.substring(0, 4) + "??? " + aprvDt.substring(4, 6) + "??? " + aprvDt.substring(6, 8) + "???");
				mailTempObj.put("orderNum", ocGiftCardOrder.getGiftCardOrderNo());
				mailTempObj.put("cardNum",
						UtilsMasking.cardNumber(saleAgenciesResult.getResData().getConCardNo(), true));

				PdGiftCard pdGiftCard = new PdGiftCard();
				pdGiftCard.setGiftCardNo(ocGiftCardOrder.getGiftCardNo());
				mailTempObj.put("cardName", pdGiftCardDao.selectPdGiftCardInfoForSale(pdGiftCard).getGiftCardName());

				String endDtm = saleAgenciesResult.getResData().getCouponEndDate();
				mailTempObj.put("endDtm",
						endDtm.substring(0, 4) + "??? " + endDtm.substring(4, 6) + "??? " + endDtm.substring(6) + "???");

				mailTempObj.put("rcvrName", ocGiftCardOrder.getRcvrName());
				mailTempObj.put("telNum", ocGiftCardOrder.getBuyerHdphnNoText());
				mailTempObj.put("message", ocGiftCardOrder.getMmsSendMesgText());

				String appTime = kcpPaymentApprovalReturn.getAppTime();
				mailTempObj.put("paymentDtm",
						appTime.substring(0, 4) + "??? " + appTime.substring(4, 6) + "??? " + appTime.substring(6, 8) + "??? "
								+ appTime.substring(8, 10) + ":" + appTime.substring(10, 12) + ":"
								+ appTime.substring(12));

				String pymntMeans = "????????????";
				if ("10002".equals(ocGiftCardOrder.getPymntMeansCode())) {
					pymntMeans = "?????????????????????";
				}
				mailTempObj.put("pymntMeans", pymntMeans);
				mailTempObj.put("payAmt", String.format("%,d", Integer.parseInt(kcpPaymentApprovalReturn.getAmount())));
				mailTempObj.put("orderStatCode", "????????????");

				MailTemplateVO mailParam = new MailTemplateVO();
				mailParam.setMailTemplateId("EC-07001");
				mailParam.setMailTemplateModel(mailTempObj);
				mailParam.setReceiverMemberNo(ocGiftCardOrder.getMemberNo());
				mailParam.setReceiverName(ocGiftCardOrder.getBuyerName());
				mailParam.setReceiverEmail(ocGiftCardOrder.getMemberEmail());

				mailService.sendMail(mailParam);

				paymentStep = 6;
			} else if ("charge".equals(ocGiftCardOrder.getKcpPayType())) {
				/**
				 * nice ?????? ????????? - ??????
				 */
				chargeResult = niceGiftService.sendCharge(chargeRequest);
				paymentStep = 4;

				oApprovalNo = chargeResult.getResData().getApprovalNo();
				oApprovalDate = chargeResult.getResData().getApprovalDate();

				resultMap.put("niceResCode", chargeResult.getResCode());
				resultMap.put("niceResMsg", chargeResult.getResMsg());

				/**
				 * nice ?????? ????????? ?????? ??????
				 */
				OcGiftCardInterlockHistory ocGiftCardInterlockHistory = new OcGiftCardInterlockHistory();
				ocGiftCardInterlockHistory.setGiftCardOrderNo(ocGiftCardOrder.getGiftCardOrderNo());
				ocGiftCardInterlockHistory.setGiftCardOrderTypeCode(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_GIFT);
				ocGiftCardInterlockHistory.setSendInfo(chargeResult.getReqStr());
				ocGiftCardInterlockHistory.setRspnsInfo(chargeResult.getResStr());
				ocGiftCardInterlockHistory.setRgstDtm(null);

				ocGiftCardInterlockHistoryDao.insertGiftCardNiceHistory(ocGiftCardInterlockHistory);
				paymentStep = 5;

				/**
				 * giftCardOrder ????????????
				 */
				String aprvDt = chargeResult.getResData().getApprovalDate()
						+ chargeResult.getResData().getApprovalTime();
				log.debug("##### aprvDt: {}", aprvDt);
				String formatedAprvDt = aprvDt.substring(0, 4) + "-" + aprvDt.substring(4, 6) + "-"
						+ aprvDt.substring(6, 8) + " " + aprvDt.substring(8, 10) + ":" + aprvDt.substring(10, 12) + ":"
						+ aprvDt.substring(12, 14);
				log.debug("##### formatedAprvDt: {}", formatedAprvDt);
				SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
				Date approvalDtm = dtFormat.parse(formatedAprvDt);

				ocGiftCardOrder.setModerNo(ocGiftCardOrder.getMemberNo());
				ocGiftCardOrder.setGiftCardAprvNoText(chargeResult.getResData().getApprovalNo());
				ocGiftCardOrder.setGiftCardAprvDtm(new Timestamp(approvalDtm.getTime()));
				ocGiftCardOrder.setCardNoText(ocGiftCardOrder.getCardNoText());
				ocGiftCardOrder.setCardPinNoText(null);
				ocGiftCardOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_COMPLETE);

				ocGiftCardOrderDao.updateGiftCardPayment(ocGiftCardOrder);
				log.debug("##### updateGiftCardPayment");

				resultMap.put("cardNoText", ocGiftCardOrder.getCardNoText());
				resultMap.put("conBalanceAmount", chargeResult.getResData().getConBalanceAmount());
				resultMap.put("couponEndDate", chargeResult.getResData().getCouponEndDate());
				resultMap.put("pymntMeansCode", ocGiftCardOrder.getPymntMeansCode());
				log.debug("##### resultMap Setting");

				/**
				 * ?????? ???????????? ????????? ????????????????????? ????????? ???????????? ?????????
				 */
				MbMemberGiftCard myCardParam = new MbMemberGiftCard();
				myCardParam.setCardNoText(ocGiftCardOrder.getCardNoText());
				myCardParam.setCardAmt(chargeResult.getResData().getConBalanceAmount().intValue());
				mbMemberGiftCardDao.updateGiftCardAmt(myCardParam);
				log.debug("##### balance update");

				/**
				 * ?????? ??????
				 */
				HashMap<String, Object> mailTempObj = new HashMap<>();
				mailTempObj.put("title", "??????????????? ???????????? ?????????????????????.");
				mailTempObj.put("kind", "[A-RT] [???????????????] ???????????????");
				mailTempObj.put("payDtm",
						aprvDt.substring(0, 4) + "??? " + aprvDt.substring(4, 6) + "??? " + aprvDt.substring(6, 8) + "???");
				mailTempObj.put("orderNum", ocGiftCardOrder.getGiftCardOrderNo());
				mailTempObj.put("cardNum", UtilsMasking.cardNumber(ocGiftCardOrder.getCardNoText(), true));

				PdGiftCard pdGiftCard = new PdGiftCard();
				// cardNoText??? ??????????????? ????????????
				pdGiftCard.setGiftCardNo(ocGiftCardOrder.getCardNoText());
				mailTempObj.put("cardName", pdGiftCardDao.selectPdGiftCardInfo(pdGiftCard).getGiftCardName());

				String endDtm = chargeResult.getResData().getCouponEndDate();
				mailTempObj.put("endDtm",
						endDtm.substring(0, 4) + "??? " + endDtm.substring(4, 6) + "??? " + endDtm.substring(6) + "???");

				mailTempObj.put("rcvrName", ocGiftCardOrder.getRcvrName());
				mailTempObj.put("telNum", ocGiftCardOrder.getBuyerHdphnNoText());
				mailTempObj.put("message", ocGiftCardOrder.getMmsSendMesgText());

				String appTime = kcpPaymentApprovalReturn.getAppTime();
				mailTempObj.put("paymentDtm",
						appTime.substring(0, 4) + "??? " + appTime.substring(4, 6) + "??? " + appTime.substring(6, 8) + "??? "
								+ appTime.substring(8, 10) + ":" + appTime.substring(10, 12) + ":"
								+ appTime.substring(12));

				String pymntMeans = "????????????";
				if ("10002".equals(ocGiftCardOrder.getPymntMeansCode())) {
					pymntMeans = "?????????????????????";
				}
				mailTempObj.put("pymntMeans", pymntMeans);
				mailTempObj.put("payAmt", String.format("%,d", Integer.parseInt(kcpPaymentApprovalReturn.getAmount())));
				mailTempObj.put("orderStatCode", "????????????");

				MailTemplateVO mailParam = new MailTemplateVO();
				mailParam.setMailTemplateId("EC-07001");
				mailParam.setMailTemplateModel(mailTempObj);
				mailParam.setReceiverMemberNo(ocGiftCardOrder.getMemberNo());
				mailParam.setReceiverName(ocGiftCardOrder.getRcvrName());
				mailParam.setReceiverEmail(ocGiftCardOrder.getMemberEmail());

				mailService.sendMail(mailParam);

				paymentStep = 6;
			}

			resultMap.put("completeResCode", "0000");
			resultMap.put("completeResMsg", "??????");

			resultMap.put("completeDataPayType", payCode);
			resultMap.put("completeDataPayAmt", kcpPaymentApproval.getGoodMny());
		} catch (Exception e) {
			e.printStackTrace();

			if (paymentStep >= 2) {
				log.debug("##### kcp TNO: {}", kcpPaymentApprovalReturn.getTno());
				// AS ?????? ????????? ????????? ?????? kcp card ?????? ??????
				KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
				kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.gift.siteCode"));
				kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.gift.siteKey"));
				kcpPaymentCancel.setTno(kcpPaymentApprovalReturn.getTno()); // KCP ????????? ????????????
				kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
				kcpPaymentCancel.setCustIp(kcpPaymentApproval.getCustIp()); // ?????? ????????? IP
				kcpPaymentCancel.setModDesc("????????? ?????? ??????"); // ?????? ??????

				// ????????? ???????????? ????????? ?????? ??????
				payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));

				resultMap.put("successYn", "N");
				// throw new Exception(e.getMessage());
			}

			if (paymentStep >= 4) {
				if ("saleAgencies".equals(ocGiftCardOrder.getKcpPayType())) {
					saleAgenciesRequest.setMessageType("0420");
					saleAgenciesRequest.setServiceCode("A2");
					saleAgenciesRequest.setOApprovalNo(oApprovalNo);
					saleAgenciesRequest.setOApprovalDate(oApprovalDate);

					CommNiceRes<SaleAgenciesResponse> saleAgenciesCancelResult = niceGiftService
							.sendSaleAgencies(saleAgenciesRequest);
					resultMap.put("niceCancelRes", saleAgenciesCancelResult.getResCode());
					resultMap.put("niceCancelMsg", saleAgenciesCancelResult.getResMsg());
				} else if ("charge".equals(ocGiftCardOrder.getKcpPayType())) {
					chargeRequest.setMessageType("0420");
					chargeRequest.setServiceCode("A3");
					chargeRequest.setOApprovalNo(oApprovalNo);
					chargeRequest.setOApprovalDate(oApprovalDate);

					CommNiceRes<ChargeResponse> chargeCancelResult = niceGiftService.sendCharge(chargeRequest);
					resultMap.put("niceCancelRes", chargeCancelResult.getResCode());
					resultMap.put("niceCancelMsg", chargeCancelResult.getResMsg());

					if ("0000".equals(chargeCancelResult.getResCode())) {
						/**
						 * ?????? ???????????? ????????? ????????????????????? ????????? ???????????? ?????????
						 */
						MbMemberGiftCard myCardParam = new MbMemberGiftCard();
						myCardParam.setCardNoText(ocGiftCardOrder.getCardNoText());
						myCardParam.setCardAmt(chargeCancelResult.getResData().getConBalanceAmount().intValue());
						mbMemberGiftCardDao.updateGiftCardAmt(myCardParam);
					}
				}
			}
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "GIFTCARD ORDER EXCEPTION");
		}

		return resultMap;
	}

	/**
	 * @Desc : giftCardOrder ?????? ??????
	 * @Method Name : setGiftCardOrderTemp
	 * @Date : 2019. 4. 22.
	 * @Author : nalpari
	 * @param ocGiftCardOrder
	 * @param kcpPaymentApproval
	 * @throws Exception
	 */
	public void setGiftCardOrderTemp(OcGiftCardOrder ocGiftCardOrder, KcpPaymentApproval kcpPaymentApproval)
			throws Exception {
		ocGiftCardOrder.setGiftCardOrderNo(ocGiftCardOrder.getGiftCardOrderNo());
//		ocGiftCardOrder.setSiteNo(kcpPaymentApproval.getSiteCd());
		ocGiftCardOrder.setEmpOrderYn("N");
//		ocGiftCardOrder.setMemberNo("");
//		ocGiftCardOrder.setMemberTypeCode("");
//		ocGiftCardOrder.setMbshpGradeCode("");
//		ocGiftCardOrder.setEmpYn("");
//		ocGiftCardOrder.setOtsVipYn("");
		ocGiftCardOrder.setDeviceCode(CommonCode.DEVICE_PC);
//		ocGiftCardOrder.setBuyerName("");
//		ocGiftCardOrder.setBuyerHdphnNoText("");
//		ocGiftCardOrder.setRcvrName("");
//		ocGiftCardOrder.setRcvrHdphnNoText("");
		ocGiftCardOrder.setGiftCardNo(ocGiftCardOrder.getGiftCardNo());
		ocGiftCardOrder.setPymntAmt((int) kcpPaymentApproval.getGoodMny());
		ocGiftCardOrder.setGiftCardAprvNoText("");
//		ocGiftCardOrder.setGiftCardAprvDtm(null);
		if ("saleAgencies".equals(ocGiftCardOrder.getKcpPayType())) {
			ocGiftCardOrder.setGiftCardOrderTypeCode(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_GIFT);
		} else if ("charge".equals(ocGiftCardOrder.getKcpPayType())) {
			ocGiftCardOrder.setGiftCardOrderTypeCode(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_CHARGE);
		}
		ocGiftCardOrder.setGiftCardStatCode(CommonCode.GIFT_CARD_STAT_CODE_WAIT);
		ocGiftCardOrder.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
		ocGiftCardOrder.setTaxBillIssueStatCode(CommonCode.TAX_BILL_ISSUE_STAT_CODE_UNPUBLISHED);
//		ocGiftCardOrder.setMmsReSendDtm(null);
		ocGiftCardOrder.setOrderStatCode(CommonCode.ORDER_STAT_CODE_TEMP);
		ocGiftCardOrder.setRgsterNo(ocGiftCardOrder.getMemberNo());
//		ocGiftCardOrder.setRgstDtm(null);

		ocGiftCardOrderDao.insertGiftCardPayment(ocGiftCardOrder);
	}

	/**
	 * @Desc : ??????????????? KCP ?????? ?????? ??????
	 * @Method Name : setGiftCardKcpPayment
	 * @Date : 2019. 4. 22.
	 * @Author : nalpari
	 * @param ocGiftCardOrder
	 * @param kcpPaymentApprovalReturn
	 * @throws Exception
	 */
	public void setGiftCardKcpPayment(OcGiftCardOrder ocGiftCardOrder,
			KcpPaymentApprovalReturn kcpPaymentApprovalReturn) throws Exception {
		OcGiftCardOrderPayment ocGiftCardOrderPayment = new OcGiftCardOrderPayment();

		ocGiftCardOrderPayment.setGiftCardOrderNo(ocGiftCardOrder.getGiftCardOrderNo());
//		ocGiftCardOrderPayment.setGiftCardOrderPymntSeq(Short.valueOf("0"));
		ocGiftCardOrderPayment.setPymntDtm(null);
		ocGiftCardOrderPayment.setDeviceCode(CommonCode.DEVICE_PC);
		ocGiftCardOrderPayment.setMainPymntMeansYn(Const.BOOLEAN_TRUE);
//		if (UtilsText.equals(ocGiftCardOrderPayment.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
		if (UtilsText.equals(ocGiftCardOrder.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			ocGiftCardOrderPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_CARD);
			ocGiftCardOrderPayment.setIntrstFreeYn(kcpPaymentApprovalReturn.getNoinf());
			ocGiftCardOrderPayment.setCardType(CommonCode.CARD_GBN_TYPE_NORMAL);
		} else {
			ocGiftCardOrderPayment.setPymntMeansCode(CommonCode.PYMNT_MEANS_CODE_ACCOUNT_TRANSFER);
		}
		ocGiftCardOrderPayment.setPymntVndrCode(CommonCode.PYMNT_VNDR_CODE_KCP);
		ocGiftCardOrderPayment.setPymntOrganCodeText(kcpPaymentApprovalReturn.getCardCd());
		ocGiftCardOrderPayment.setPymntOrganName(kcpPaymentApprovalReturn.getCardName());
		ocGiftCardOrderPayment.setPymntOrganNoText(kcpPaymentApprovalReturn.getCardNo());
		// [????????????]?????? ???????????? (???????????? ??????), 0 : ??????, 1 : ??????
		if ("0".equals(kcpPaymentApprovalReturn.getCardBinType01())) {
			ocGiftCardOrderPayment.setInstmtTermCount(Short.valueOf(kcpPaymentApprovalReturn.getQuota()));
		} else {
			ocGiftCardOrderPayment.setCardType(CommonCode.CARD_GBN_TYPE_CHECK);
		}
		// [????????????]?????? ????????????(???????????? ??????), 0 : ??????, 1 : ??????
		if ("0".equals(kcpPaymentApprovalReturn.getCardBinType02())) {
			ocGiftCardOrderPayment.setCardGbnType(CommonCode.CARD_TYPE_NORMAL);
		} else {
			ocGiftCardOrderPayment.setCardGbnType(CommonCode.CARD_TYPE_CORPORATE);
		}
		ocGiftCardOrderPayment.setPymntAprvNoText(kcpPaymentApprovalReturn.getTno());
		ocGiftCardOrderPayment.setPymntAmt(Integer.parseInt(kcpPaymentApprovalReturn.getAmount()));
		ocGiftCardOrderPayment.setCashRcptIssueYn(Const.BOOLEAN_FALSE);
		ocGiftCardOrderPayment.setGiftCardPinNoText("");
		ocGiftCardOrderPayment.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_FINISH);
		ocGiftCardOrderPayment.setRspnsCodeText(kcpPaymentApprovalReturn.getResCd());
		ocGiftCardOrderPayment.setRspnsMesgText(kcpPaymentApprovalReturn.getResMsg());
		ocGiftCardOrderPayment.setPymntLogInfo(new ObjectMapper().writeValueAsString(kcpPaymentApprovalReturn));
		ocGiftCardOrderPayment.setRgsterNo(ocGiftCardOrder.getMemberNo());
		ocGiftCardOrderPayment.setRgstDtm(null);

		ocGiftCardOrderPaymentDao.insertGiftCardKcpPayment(ocGiftCardOrderPayment);
	}

	/**
	 * @Desc : ??????????????? ??????/?????? ??? ?????? ?????? ??????
	 * @Method Name : getGiftCardSaleAgenciesInfo
	 * @Date : 2019. 5. 13.
	 * @Author : nalpari
	 * @param ocGiftCardOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGiftCardSaleAgenciesInfo(OcGiftCardOrder ocGiftCardOrder) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			resultMap.put("resData", ocGiftCardOrderDao.selectGiftCardSaleAgenciesInfo(ocGiftCardOrder));
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ???????????? ???????????? ??????
	 * @Method Name : getKakaoCouponCheck
	 * @Date : 2019. 5. 17.
	 * @Author : nalpari
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getKakaoCouponCheck(GiftcardVo giftcardVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			NmcVO param = new NmcVO();
			param.setCouponNum(giftcardVO.getKakaoCouponNum());

			resultMap.put("resData", nmcService.getKakaoCouponInfo(param));
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ???????????? ????????????
	 * @Method Name : setKakaoCouponExchange
	 * @Date : 2019. 5. 17.
	 * @Author : nalpari
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setKakaoCouponExchange(GiftcardVo giftcardVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		SaleAgenciesRequest saleAgenciesRequest = null;
		CommNiceRes<SaleAgenciesResponse> saleAgenciesResult = null;
		NmcVO kakaoCouponResult = null;
		String oApprovalNo = null;
		String oApprovalDate = null;
		int exchangeStep = 0;

		try {
			NmcVO param = new NmcVO();
			/*
			 * ???????????? ?????? ??????
			 */
			param.setCouponNum(giftcardVO.getKakaoCouponNum());
			/*
			 * ???????????? ?????? ???????????? - abcmart????????? ????????????
			 */
			param.setAdmitNum(null);
			/*
			 * ?????????????????? ????????? ?????? ?????? - ???????????? ??? ?????? ?????? ?????? ??????
			 */
			param.setUseAmount(giftcardVO.getUseAmount());

			/*
			 * ???????????? ?????? ???????????? ????????????
			 */
			kakaoCouponResult = nmcService.setKakaoCouponUse(param);
			exchangeStep = 1;

			/*
			 * ???????????? ?????? ??????????????? ???????????? ?????? ?????????????????? ?????? - ???????????? ???????????? ?????? ??????
			 */
			saleAgenciesRequest = new SaleAgenciesRequest(Long.parseLong(kakaoCouponResult.getUseAmount()),
					Long.parseLong(kakaoCouponResult.getUseAmount()), "01", "3", "", "A8",
					giftcardVO.getRcvrHdphnNoText(), "", "0000000001", "01", "Y", "A8", 1860, "05", "2018176174");

			saleAgenciesResult = niceGiftService.sendSaleAgencies(saleAgenciesRequest);
			/*
			 * ??????????????? ?????? ??? ??????
			 */
			oApprovalNo = saleAgenciesResult.getResData().getApprovalNo();
			oApprovalDate = saleAgenciesResult.getResData().getApprovalDate();
			exchangeStep = 2;

			/*
			 * ???????????? ?????? ?????? ????????? ??????????????? ?????? ????????? ????????? ????????? DB??? ??????
			 */
			OcKakaoExchangeOrder ocKakaoExchangeOrder = new OcKakaoExchangeOrder();
			ocKakaoExchangeOrder.setGiftCardOrderNo(giftcardVO.getGiftCardOrderNo());
			ocKakaoExchangeOrder.setOrderDtm(null);
			ocKakaoExchangeOrder.setSiteNo(giftcardVO.getSiteNo());
			ocKakaoExchangeOrder.setMemberNo(giftcardVO.getMemberNo());
			ocKakaoExchangeOrder.setMemberTypeCode(giftcardVO.getMemberTypeCode());
			ocKakaoExchangeOrder.setEmpYn(giftcardVO.getEmpYn());
			ocKakaoExchangeOrder.setOtsVipYn(giftcardVO.getOtsVipYn());
			ocKakaoExchangeOrder.setDeviceCode(CommonCode.DEVICE_PC);
			ocKakaoExchangeOrder.setExchngAmt(Integer.parseInt(kakaoCouponResult.getUseAmount()));
			ocKakaoExchangeOrder.setRcvrName(giftcardVO.getRcvrName());
			ocKakaoExchangeOrder.setRcvrHdphnNoText(giftcardVO.getRcvrHdphnNoText());
			ocKakaoExchangeOrder.setCardNoText(saleAgenciesResult.getResData().getConCardNo());
			ocKakaoExchangeOrder.setCardPinNoText(saleAgenciesResult.getResData().getConPin());
			ocKakaoExchangeOrder.setTrId(kakaoCouponResult.getTrId());
			ocKakaoExchangeOrder.setAdmitNum(kakaoCouponResult.getAdmitNum());
			ocKakaoExchangeOrder.setCouponNum(kakaoCouponResult.getCouponNum());
			ocKakaoExchangeOrder.setUseAmount(Integer.parseInt(kakaoCouponResult.getUseAmount()));
			ocKakaoExchangeOrder.setOrderBalance(Integer.parseInt(kakaoCouponResult.getOrderBalance()));
			ocKakaoExchangeOrder.setDataHash(kakaoCouponResult.getDataHash());
			ocKakaoExchangeOrder.setResultCodeText(kakaoCouponResult.getResultCode());
			ocKakaoExchangeOrder.setCashReceiptAmount(Integer.parseInt(kakaoCouponResult.getCashReceiptAmount()));
			ocKakaoExchangeOrder.setNoTaxAmount(Integer.parseInt(kakaoCouponResult.getNoTaxAmount()));
			ocKakaoExchangeOrder.setRgsterNo(giftcardVO.getMemberNo());
			ocKakaoExchangeOrder.setRgstDtm(null);

			ocKakaoExchangeOrderDao.insertKakaoCouponExchange(ocKakaoExchangeOrder);

			/*
			 * ???????????? ??????
			 */
			BalanceRequest balanceParam = new BalanceRequest(giftcardVO.getMemberNo(),
					ocKakaoExchangeOrder.getCardNoText(), 1);
			CommNiceRes<BalanceResponse> balanceResult = niceGiftService.sendBalance(balanceParam);

			OcKakaoExchangeOrder resultObj = new OcKakaoExchangeOrder();
			resultObj.setGiftCardOrderNo(giftcardVO.getGiftCardOrderNo());
			resultObj.setMemberNo(giftcardVO.getMemberNo());
			resultObj.setTrId(kakaoCouponResult.getTrId());
			resultObj.setAdmitNum(kakaoCouponResult.getAdmitNum());
			resultObj.setCouponNum(kakaoCouponResult.getCouponNum());
			resultObj.setUseAmount(Integer.parseInt(kakaoCouponResult.getUseAmount()));
			resultObj.setOrderBalance(Integer.parseInt(kakaoCouponResult.getOrderBalance()));
			resultObj.setCardNoText(ocKakaoExchangeOrder.getCardNoText());

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "SUCCESS");
			resultMap.put("resData", resultObj);
			resultMap.put("balanceResData", balanceResult.getResData());

			/**
			 * ?????? ??????
			 */
			HashMap<String, Object> mailTempObj = new HashMap<>();
			mailTempObj.put("kind", "[A-RT] [???????????????] ????????? ");
			mailTempObj.put("payDtm", oApprovalNo.substring(0, 4) + "??? " + oApprovalNo.substring(4, 6) + "??? "
					+ oApprovalNo.substring(6, 8) + "???");
			mailTempObj.put("orderNum", giftcardVO.getGiftCardOrderNo());
			mailTempObj.put("cardNum", UtilsMasking.cardNumber(saleAgenciesResult.getResData().getConCardNo(), true));

			PdGiftCard pdGiftCard = new PdGiftCard();
			pdGiftCard.setGiftCardNo(giftcardVO.getGiftCardNo());
			mailTempObj.put("cardName", pdGiftCardDao.selectPdGiftCardInfo(pdGiftCard).getGiftCardName());

			String endDtm = saleAgenciesResult.getResData().getApprovalDate();
			mailTempObj.put("endDtm",
					endDtm.substring(0, 4) + "??? " + endDtm.substring(4, 6) + "??? " + endDtm.substring(6) + "???");

			mailTempObj.put("rcvrName", giftcardVO.getMemberName() + "(" + giftcardVO.getRcvrHdphnNoText() + ")");

			MailTemplateVO mailParam = new MailTemplateVO();
			mailParam.setMailTemplateId("EC-07001");
			mailParam.setMailTemplateModel(mailTempObj);
			mailParam.setReceiverMemberNo(giftcardVO.getMemberNo());
			mailParam.setReceiverName(giftcardVO.getMemberName());
			mailParam.setReceiverEmail(giftcardVO.getMemberEmail());

			mailService.sendMail(mailParam);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "FAIL");

			/*
			 * ???????????? ????????? ????????? ??? ???????????? ??????????????? ???????????? ????????? ?????? ??????
			 * 
			 * ???????????? ?????? ?????? ?????? ????????? ????????? ???????????? ????????? ?????? ?????? ?????????
			 * 
			 * ???????????? ?????? ?????? ????????? ?????? ????????? ???????????? ?????? ?????? ?????? ?????? ??????
			 */
			if (exchangeStep > 1) {
				NmcVO param = new NmcVO();
				param.setCouponNum(kakaoCouponResult.getCouponNum());
				param.setOriginAdmitNum(kakaoCouponResult.getAdmitNum());

				nmcService.setKakaoCouponUseCancel(param);
			}

			if (exchangeStep > 2) {
				saleAgenciesRequest.setMessageType("0420");
				saleAgenciesRequest.setServiceCode("A2");
				saleAgenciesRequest.setOApprovalNo(oApprovalNo);
				saleAgenciesRequest.setOApprovalDate(oApprovalDate);

				CommNiceRes<SaleAgenciesResponse> saleAgenciesCancelResult = niceGiftService
						.sendSaleAgencies(saleAgenciesRequest);
				resultMap.put("niceCancelRes", saleAgenciesCancelResult.getResCode());
				resultMap.put("niceCancelMsg", saleAgenciesCancelResult.getResMsg());
			}
		}

		return resultMap;
	}

	public Map<String, Object> getKakaoExchangeInfo(GiftcardVo giftcardVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		OcKakaoExchangeOrder ocKakaoExchangeOrder = new OcKakaoExchangeOrder();
		ocKakaoExchangeOrder.setGiftCardOrderNo(giftcardVO.getGiftCardOrderNo());

		try {
			resultMap.put("resData", ocKakaoExchangeOrderDao.selectKakaoExchangeInfo(ocKakaoExchangeOrder));
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "FAIL");
			resultMap.put("resData", null);
		}

		return resultMap;
	}

	/**
	 * @Desc : my ??????????????? ????????? ??????
	 * @Method Name : getGiftCardList
	 * @Date : 2019. 4. 24.
	 * @Author : nalpari
	 * @param mbMemberGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGiftCardList(MbMemberGiftCard mbMemberGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			resultMap.put("resData", mbMemberGiftCardDao.selectMyGiftCard(mbMemberGiftCard));
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ???????????? ?????? - ???????????? ?????????????????? update
	 * @Method Name : setGiftCardDelYn
	 * @Date : 2019. 6. 19.
	 * @Author : nalpari
	 * @param mbMemberGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setGiftCardTermination(MbMemberGiftCard mbMemberGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// ?????? ???????????? ????????????
			mbMemberGiftCardDao.updateDelYn(mbMemberGiftCard);
			// ???????????? ???????????? ??????
			mbMemberGiftCard.setCardNoText(null);
			// ?????? ??????????????? ??????
			List<MbMemberGiftCard> tmpList = mbMemberGiftCardDao.selectMyGiftCard(mbMemberGiftCard);
			if (tmpList.size() > 0) {
				boolean isRprSntCard = false;
				for (MbMemberGiftCard item : tmpList) {
					if (UtilsText.equals(item.getRprsntCardYn(), "Y")) {
						isRprSntCard = true;
					}
				}

				if (!isRprSntCard) {
					MbMemberGiftCard param = new MbMemberGiftCard();
					param.setCardNoText(tmpList.get(0).getCardNoText());

					mbMemberGiftCardDao.updateRprsnt(mbMemberGiftCard);
				}
			}

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ???????????? ?????? ??????
	 * @Method Name : setRprsntCard
	 * @Date : 2019. 4. 24.
	 * @Author : nalpari
	 * @param mbMemberGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setRprsntCard(MbMemberGiftCard mbMemberGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			mbMemberGiftCardDao.updateRprsntAll(mbMemberGiftCard);
			mbMemberGiftCardDao.updateRprsnt(mbMemberGiftCard);

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ?????? ??????
	 * @Method Name : setGiftCardName
	 * @Date : 2019. 4. 24.
	 * @Author : nalpari
	 * @param mbMemberGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setGiftCardName(MbMemberGiftCard mbMemberGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			mbMemberGiftCardDao.updateGiftCardName(mbMemberGiftCard);
			String giftCardName = mbMemberGiftCardDao.selectGiftCardName(mbMemberGiftCard);

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
			resultMap.put("giftCardName", giftCardName);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ???????????? ??????
	 * @Method Name : getUseHistory
	 * @Date : 2019. 5. 23.
	 * @Author : nalpari
	 * @param rvGiftCardComparison
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUseHistory(Pageable<RvGiftCardComparison, RvGiftCardComparison> pageable)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {

			List<RvGiftCardComparison> result = rvGiftCardComparisonDao.selectUseHistory(pageable);

			resultMap.put("list", result);
			resultMap.put("totalCount", result.size());

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
		}

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????,??????,?????? ?????? ?????? ??????
	 * @Method Name : selectGiftCardHistory
	 * @Date : 2019. 4. 29.
	 * @Author : nalpari
	 * @param ocGiftCardOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGiftCardHistoryList(Pageable<OcGiftCardOrder, OcGiftCardOrder> pageable)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<OcGiftCardOrder> result = new ArrayList();
		int totalCount = ocGiftCardOrderDao.selectGiftCardHistoryCount(pageable.getBean());
		try {
			result = ocGiftCardOrderDao.selectGiftCardHistoryList(pageable);
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "success");
			resultMap.put("list", result);
			resultMap.put("totalCount", totalCount);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "fail");
			resultMap.put("list", result);
			resultMap.put("totalCount", totalCount);
		}

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ??????,??????,?????? ?????? ?????? ??????
	 * @Method Name : setGiftCardHistoryCancel
	 * @Date : 2019. 4. 30.
	 * @Author : nalpari
	 * @param ocGiftCardOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setGiftCardHistoryCancel(OcGiftCardOrder ocGiftCardOrder) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		KcpPaymentApproval kcpPaymentApproval = ocGiftCardOrder.getKcpPaymentApproval();
		OcGiftCardOrder giftCardHis = ocGiftCardOrderDao.selectGiftCardHistoryForCancel(ocGiftCardOrder);

		InetAddress ip = InetAddress.getLocalHost();
		String custIp = ip.getHostAddress();

		// 1.kcp ?????? ??????
		KcpPaymentCancel kcpPaymentCancel = new KcpPaymentCancel();
		kcpPaymentCancel.setSiteCd(Config.getString("pg.kcp.gift.siteCode"));
		kcpPaymentCancel.setSiteKey(Config.getString("pg.kcp.gift.siteKey"));
		kcpPaymentCancel.setTno(giftCardHis.getPymntAprvNoText()); // KCP ????????? ????????????
		kcpPaymentCancel.setModType(CommonCode.KCP_MOD_TYPE_STSC); // ???????????? STSC / ???????????? STPC
		kcpPaymentCancel.setCustIp(custIp); // ?????? ????????? IP
		kcpPaymentCancel.setModDesc("????????? ??????"); // ?????? ??????

		// KCP ????????? ?????? ??????
		payment.cancel(new PaymentInfo(Const.PAYMENT_VENDOR_NAME_KCP, kcpPaymentCancel));

		OcGiftCardOrderPayment updatePaymentParam = new OcGiftCardOrderPayment();
		updatePaymentParam.setGiftCardOrderNo(ocGiftCardOrder.getGiftCardOrderNo());
		updatePaymentParam.setPymntStatCode(CommonCode.PYMNT_STAT_CODE_PAYMENT_CANCEL);
		// OC_GIFT_CARD_ORDER_PAYMENT ????????? ????????????
		ocGiftCardOrderPaymentDao.updateGiftCardPaymentCancelStat(updatePaymentParam);

		// 2.nice ?????? ??????
		String payCode = "00";
		if (UtilsText.equals(giftCardHis.getPymntMeansCode(), CommonCode.PYMNT_MEANS_CODE_CARD)) {
			payCode = "01";
		}

		Date approvalDtm = null;
		/**
		 * ?????? ??????
		 */
		HashMap<String, Object> mailTempObj = new HashMap<>();
		mailTempObj.put("kind", "[A-RT] [???????????????] ?????????");

		if (UtilsText.equals(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_GIFT, giftCardHis.getGiftCardOrderTypeCode())) {
			SaleAgenciesRequest saleAgenciesRequest = new SaleAgenciesRequest(giftCardHis.getPymntAmt(),
					giftCardHis.getPymntAmt(), "01", "3", "1", "A8", giftCardHis.getRcvrHdphnNoText(), "", "0000000001",
					"01", "Y", "A8", 1860, payCode, "2018176174");
			saleAgenciesRequest.setMessageType("0420");
			saleAgenciesRequest.setServiceCode("C2");
			saleAgenciesRequest.setOApprovalNo(giftCardHis.getGiftCardAprvNoText());
			saleAgenciesRequest
					.setOApprovalDate(giftCardHis.getGiftCardAprvDtm().toString().replace("-", "").substring(0, 8));

			log.debug("##### saleAgenciesRequest: {}", saleAgenciesRequest);

			CommNiceRes<SaleAgenciesResponse> saleAgenciesCancelResult = niceGiftService
					.sendSaleAgencies(saleAgenciesRequest);

			resultMap.put("niceCancelCode", saleAgenciesCancelResult.getResCode());
			resultMap.put("niceCancelMsg", saleAgenciesCancelResult.getResMsg());
			resultMap.put("nideCancelData", saleAgenciesCancelResult.getResData());

			// ?????? ?????? ??????
			String aprvDt = saleAgenciesCancelResult.getResData().getApprovalDate()
					+ saleAgenciesCancelResult.getResData().getApprovalTime();
			log.debug("##### aprvDt: {}", aprvDt);
			String formatedAprvDt = aprvDt.substring(0, 4) + "-" + aprvDt.substring(4, 6) + "-" + aprvDt.substring(6, 8)
					+ " " + aprvDt.substring(8, 10) + ":" + aprvDt.substring(10, 12) + ":" + aprvDt.substring(12, 14);
			log.debug("##### formatedAprvDt: {}", formatedAprvDt);
			SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
			approvalDtm = dtFormat.parse(formatedAprvDt);

			/**
			 * ???????????? Object
			 */
			mailTempObj.put("payTypeCode", "saleAgencies");
			mailTempObj.put("payDtm",
					aprvDt.substring(0, 4) + "??? " + aprvDt.substring(4, 6) + "??? " + aprvDt.substring(6, 8) + "???");
			mailTempObj.put("orderNum", ocGiftCardOrder.getGiftCardOrderNo());
			mailTempObj.put("cardNum",
					UtilsMasking.cardNumber(saleAgenciesCancelResult.getResData().getConCardNo(), true));

			PdGiftCard pdGiftCard = new PdGiftCard();
			pdGiftCard.setGiftCardNo(ocGiftCardOrder.getGiftCardNo());
			mailTempObj.put("cardName", pdGiftCardDao.selectPdGiftCardInfo(pdGiftCard).getGiftCardName());

			String endDtm = saleAgenciesCancelResult.getResData().getCouponEndDate();
			mailTempObj.put("endDtm",
					endDtm.substring(0, 4) + "??? " + endDtm.substring(4, 6) + "??? " + endDtm.substring(6) + "???");

			mailTempObj.put("rcvrName", ocGiftCardOrder.getRcvrName());
			mailTempObj.put("telNum", ocGiftCardOrder.getBuyerHdphnNoText());
			mailTempObj.put("message", ocGiftCardOrder.getMmsSendMesgText());

			String pymntMeans = "????????????";
			if ("10002".equals(ocGiftCardOrder.getPymntMeansCode())) {
				pymntMeans = "?????????????????????";
			}
			mailTempObj.put("pymntMeans", pymntMeans);
			mailTempObj.put("payAmt", String.format("%,d", giftCardHis.getPymntAmt()));

		} else if (UtilsText.equals(CommonCode.GIFT_CARD_ORDER_TYPE_CODE_CHARGE,
				giftCardHis.getGiftCardOrderTypeCode())) {
			ChargeRequest chargeRequest = new ChargeRequest(giftCardHis.getCardNoText(), giftCardHis.getPymntAmt(),
					"01", "3", "01", "1");
			chargeRequest.setMessageType("0420");
			chargeRequest.setServiceCode("C3");
			chargeRequest.setOApprovalNo(giftCardHis.getGiftCardAprvNoText());
			log.debug("##### aprvNoText: {}", giftCardHis.getGiftCardAprvNoText());
			chargeRequest
					.setOApprovalDate(giftCardHis.getGiftCardAprvDtm().toString().substring(0, 10).replace("-", ""));
			log.debug("##### aprvDtm: {}",
					giftCardHis.getGiftCardAprvDtm().toString().substring(0, 10).replace("-", ""));

			CommNiceRes<ChargeResponse> chargeCancelResult = niceGiftService.sendCharge(chargeRequest);

			resultMap.put("niceCancelCode", chargeCancelResult.getResCode());
			resultMap.put("niceCancelMsg", chargeCancelResult.getResMsg());
			resultMap.put("nideCancelData", chargeCancelResult.getResData());

			// ?????? ?????? ??????
			String aprvDt = chargeCancelResult.getResData().getApprovalDate()
					+ chargeCancelResult.getResData().getApprovalTime();
			log.debug("##### aprvDt: {}", aprvDt);
			String formatedAprvDt = aprvDt.substring(0, 4) + "-" + aprvDt.substring(4, 6) + "-" + aprvDt.substring(6, 8)
					+ " " + aprvDt.substring(8, 10) + ":" + aprvDt.substring(10, 12) + ":" + aprvDt.substring(12, 14);
			log.debug("##### formatedAprvDt: {}", formatedAprvDt);
			SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
			approvalDtm = dtFormat.parse(formatedAprvDt);

			/**
			 * ???????????? Object
			 */
			mailTempObj.put("payTypeCode", "charge");
			mailTempObj.put("payDtm",
					aprvDt.substring(0, 4) + "??? " + aprvDt.substring(4, 6) + "??? " + aprvDt.substring(6, 8) + "???");
			mailTempObj.put("orderNum", ocGiftCardOrder.getGiftCardOrderNo());
			mailTempObj.put("cardNum", UtilsMasking.cardNumber(giftCardHis.getCardNoText(), true));

			PdGiftCard pdGiftCard = new PdGiftCard();
			pdGiftCard.setGiftCardNo(ocGiftCardOrder.getGiftCardNo());
			mailTempObj.put("cardName", pdGiftCardDao.selectPdGiftCardInfo(pdGiftCard).getGiftCardName());

			String endDtm = chargeCancelResult.getResData().getCouponEndDate();
			mailTempObj.put("endDtm",
					endDtm.substring(0, 4) + "??? " + endDtm.substring(4, 6) + "??? " + endDtm.substring(6) + "???");

			mailTempObj.put("rcvrName", ocGiftCardOrder.getRcvrName());
			mailTempObj.put("telNum", ocGiftCardOrder.getBuyerHdphnNoText());
//			mailTempObj.put("message", ocGiftCardOrder.getMmsSendMesgText());

			String pymntMeans = "????????????";
			if ("10002".equals(ocGiftCardOrder.getPymntMeansCode())) {
				pymntMeans = "?????????????????????";
			}
			mailTempObj.put("pymntMeans", pymntMeans);
			mailTempObj.put("payAmt", String.format("%,d", giftCardHis.getPymntAmt()));
		}

		/**
		 * ????????????
		 */
		MailTemplateVO mailParam = new MailTemplateVO();
		mailParam.setMailTemplateId("EC-070002");
		mailParam.setMailTemplateModel(mailTempObj);
		mailParam.setReceiverMemberNo(ocGiftCardOrder.getMemberNo());
		mailParam.setReceiverName(ocGiftCardOrder.getRcvrName());
		mailParam.setReceiverEmail(ocGiftCardOrder.getMemberEmail());

		mailService.sendMail(mailParam);

		// OC_GIFT_CARD_ORDER ????????? ????????????
		OcGiftCardOrder updateOrderParam = new OcGiftCardOrder();
		updateOrderParam.setGiftCardOrderNo(ocGiftCardOrder.getGiftCardOrderNo());
		updateOrderParam.setOrderStatCode(CommonCode.ORDER_STAT_CODE_CANCEL_COMPLETE);
		updateOrderParam.setModDtm(new Timestamp(approvalDtm.getTime()));
		updateOrderParam.setModerNo(ocGiftCardOrder.getMemberNo());

		ocGiftCardOrderDao.updateGiftCardOrderCancelStat(updateOrderParam);

		return resultMap;
	}

	/**
	 * @Desc : ??????????????? ????????? ?????? ?????? ??????
	 * @Method Name : getGiftCardChargeInfo
	 * @Date : 2019. 5. 7.
	 * @Author : nalpari
	 * @param balanceRequest
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getGiftCardChargeInfo(BalanceRequest balanceRequest) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			BalanceRequest balanceParam = new BalanceRequest("2018176174", balanceRequest.getCardNo(), 1);
			CommNiceRes<BalanceResponse> balanceResult = niceGiftService.sendBalance(balanceParam);

			MbMemberGiftCard mbMemberGiftCard = new MbMemberGiftCard();
			mbMemberGiftCard.setMemberNo(balanceRequest.getCompanyUserNo());
			mbMemberGiftCard.setCardNoText(balanceRequest.getCardNo());

			MbMemberGiftCard myGiftCardInfo = mbMemberGiftCardDao.selectGiftcardInfo(mbMemberGiftCard);
			resultMap.put("cardName", myGiftCardInfo.getGiftCardName());

			resultMap.put("resCode", balanceResult.getResCode());
			resultMap.put("resMsg", balanceResult.getResMsg());
			resultMap.put("resData", balanceResult.getResData());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "getGiftCardChargeInfo Exception");
		}

		return resultMap;
	}

	/**
	 * @Desc : ??? ?????? ?????? ??????
	 * @Method Name : getMyGiftCardCheck
	 * @Date : 2019. 5. 24.
	 * @Author : nalpari
	 * @param mbMemberGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMyGiftCardCheck(MbMemberGiftCard mbMemberGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			MbMemberGiftCard myGiftCardCheck = mbMemberGiftCardDao.selectMyGiftCardCheck(mbMemberGiftCard);
			resultMap.put("resData", myGiftCardCheck);
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "getMyGiftCardList Exception");
		}

		return resultMap;
	}

	public Map<String, Object> setGiftCardAmountEscalation(GiftcardVo giftcardVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cardNoText", giftcardVO.getCardNoText().length);

		int currIdx = 0;
		List<BalanceTransferRequest> currObj = new ArrayList<>();
		CommNiceRes<BalanceTransferResponse> balanceTransferResult = null;
		try {
			for (int i = 0; i < giftcardVO.getCardNoText().length; i++) {
				BalanceTransferRequest balanceTransferRequest = new BalanceTransferRequest("AC",
						Long.parseLong(giftcardVO.getCardAmt()[i]), giftcardVO.getCardNoText()[i],
						giftcardVO.getRprSntCardNoText(), "01", "3", "7", "A8", "01", "N", "0001", 1860,
						giftcardVO.getCardPin()[i]);

				balanceTransferResult = niceGiftService.sendBalanceTransfer(balanceTransferRequest);
				balanceTransferRequest.setOApprovalNo(balanceTransferResult.getResData().getApprovalNo());
				balanceTransferRequest.setOApprovalDate(balanceTransferResult.getResData().getApprovalDate());

				currObj.add(balanceTransferRequest);
				currIdx++;

				MbMemberGiftCard mbMemberGiftCard = new MbMemberGiftCard();
				mbMemberGiftCard.setDelYn("Y");
				mbMemberGiftCard.setCardNoText(giftcardVO.getCardNoText()[i]);
				mbMemberGiftCard.setMemberNo(giftcardVO.getMemberNo());
				mbMemberGiftCardDao.updateGiftCardDelYn(mbMemberGiftCard);
			}

			if (!"0000".equals(balanceTransferResult.getResCode())) {
				// ????????? ??????
				throw new Exception("BalanceTransferFail");
			}

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			if (currIdx > 0) {
				for (int i = 0; i < currIdx; i++) {
					currObj.get(i).setMessageType("0420");
					CommNiceRes<BalanceTransferResponse> balanceTransferCancelResult = niceGiftService
							.sendBalanceTransfer(currObj.get(i));
				}
			}

			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "FAIL");
		}

		return resultMap;
	}

	/**
	 * @Desc : ??? ?????? ??????????????? ??????
	 * @Method Name : getMyGiftCardList
	 * @Date : 2019. 5. 24.
	 * @Author : nalpari
	 * @param mbMemberGiftCard
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMyGiftCardList(MbMemberGiftCard mbMemberGiftCard) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<MbMemberGiftCard> myGiftCardList = mbMemberGiftCardDao.selectMyGiftCardList(mbMemberGiftCard);
			resultMap.put("resData", myGiftCardList);
			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "getMyGiftCardList Exception");
		}

		return resultMap;
	}

	/**
	 * @Desc : (???)?????????????????? ????????????
	 * @Method Name : getOldGiftCardBalance
	 * @Date : 2019. 6. 26.
	 * @Author : nalpari
	 * @param cardNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOldGiftCardBalance(String cardNo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			GiftCard result = mpService.checkGiftCard(cardNo);

			resultMap.put("resCode", "0000");
			resultMap.put("resMsg", "SUCCESS");
			resultMap.put("resData", result);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resCode", "9999");
			resultMap.put("resMsg", "Digital GiftCard get balance is Failed.");
			resultMap.put("resData", e.getMessage());
		}

		return resultMap;
	}
}
