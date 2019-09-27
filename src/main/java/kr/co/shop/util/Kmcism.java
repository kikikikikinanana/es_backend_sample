package kr.co.shop.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.icert.comm.secu.IcertSecuManager;

import kr.co.shop.common.config.Config;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Kmcism {

	final String REQ_KEY = "REQ_KMCISM";

	private IcertSecuManager seed;
	private String userId = ""; // 회원ID
	private String pageInfo = ""; // 페이지 정보
	private String tr_cert = "";
	private String cpId = ""; // 회원사ID
	private String urlCode = ""; // URL코드
	private String certNum = ""; // 요청번호
	private String date = ""; // 요청일시
	private String certMet = "M"; // 본인인증방법(M:휴대폰인증)
	private String isMobile = ""; // 모바일 여부
	private String name = ""; // 성명
	private String phoneNo = ""; // 휴대폰번호
	private String phoneCorp = ""; // 이동통신사(SKT:SK텔레콤 KTF:KT LGT:LGU+ SKM:SKTmvno)
	private String birthDay = ""; // 생년월일
	private String gender = ""; // 성별(0:남자 1:여자)
	private String nation = ""; // 내외국인 구분(0:내국인 1:외국인)
	private String plusInfo = ""; // 추가DATA정보
	private String extendVar = "0000000000000000"; // 확장변수(0000000000000000)
	private String tr_url = ""; // 본인인증서비스 결과수신 POPUP URL

	private String rec_cert = ""; // 결과수신DATA
	private String k_certNum = ""; // 파라미터로 수신한 요청번호
	private String CI = ""; // 연계정보(CI)
	private String DI = ""; // 중복가입확인정보(DI)
	private String M_name = ""; // 미성년자 성명(해당 값은 추가 지원요청시 제공)
	private String M_birthDay = ""; // 미성년자 생년월일(해당 값은 추가 지원요청시 제공)
	private String M_Gender = ""; // 미성년자 성별(해당 값은 추가 지원요청시 제공)
	private String M_nation = ""; // 미성년자 내외국인(해당 값은 추가 지원요청시 제공)
	private String result = ""; // 결과값(Y:성공 N:실패 F:오류)
	private String ip = ""; // ip주소
	private String message = ""; // 오류메시지

	private String var1 = ""; // 추가필드1
	private String var2 = ""; // 추가필드2
	private String var3 = ""; // 추가필드3
	private String var4 = ""; // 추가필드4
	private String var5 = ""; // 추가필드5

	private HashMap<String, String> hm = new HashMap<String, String>();
	private String requestURL = null;

	private String redirectUrl = "";

	public Kmcism() {
		// LOCAL
		this.hm.put("https://local.a-rt.com:8500/member/member-join-page", "018001");
		this.hm.put("https://local.a-rt.com:8500/member/member-change-join-page", "018002");
		this.hm.put("https://local.a-rt.com:8500/member/member-pw-search", "018003");
		this.hm.put("https://local.a-rt.com:8500/member/member-change-login-page", "018004");
		this.hm.put("https://local.a-rt.com:8500/promotion/event/draw/member/check", "018005");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-join-page", "021001");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-change-join-page", "021002");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-pw-search", "021003");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-change-login-page", "021004");
		this.hm.put("https://m.local.a-rt.com:8600/promotion/event/draw/member/check", "021005");

		// DEV
		this.hm.put("https://dev.a-rt.com/member/member-join-page", "017001");
		this.hm.put("https://dev.a-rt.com/member/member-change-join-page", "017002");
		this.hm.put("https://dev.a-rt.com/member/member-pw-search", "017003");
		this.hm.put("https://dev.a-rt.com/member/member-change-login-page", "017004");
		this.hm.put("https://dev.a-rt.com/promotion/event/draw/member/check", "017005");
		this.hm.put("https://m.dev.a-rt.com/member/member-join-page", "020001");
		this.hm.put("https://m.dev.a-rt.com/member/member-change-join-page", "020002");
		this.hm.put("https://m.dev.a-rt.com/member/member-pw-search", "020003");
		this.hm.put("https://m.dev.a-rt.com/member/member-change-login-page", "020004");
		this.hm.put("https://m.dev.a-rt.com/promotion/event/draw/member/check", "020005");

		this.seed = new IcertSecuManager();
		this.cpId = Config.getString("kmc.company.id");

		if (UtilsText.equals(this.isMobile, Const.BOOLEAN_TRUE)) {
			this.tr_url = Config.getString("url.www.mobile.https") + Config.getString("kmc.return.mobile.url");
		} else {
			this.tr_url = Config.getString("url.www.https") + Config.getString("kmc.return.url");
		}
	}

	public void beforeSend() {
		String redirectUrl = "";

		// 모바일의 경우 생성후에 따로 tr_url을 수정한다
		if (UtilsText.equals(this.isMobile, Const.BOOLEAN_TRUE)) {
			if (UtilsText.equals(this.pageInfo, CommonCode.CRTFC_PATH_CODE_DRAW_EVENT)) {
				this.tr_url = Config.getString("url.www.mobile.https") + Config.getString("kmc.draw.return.url");
			} else {
				this.tr_url = Config.getString("url.www.mobile.https") + Config.getString("kmc.return.mobile.url");
			}
		} else {
			if (UtilsText.equals(this.pageInfo, CommonCode.CRTFC_PATH_CODE_DRAW_EVENT)) {
				this.tr_url = Config.getString("url.www.https") + Config.getString("kmc.draw.return.url");
			}
		}

		if (this.requestURL.indexOf("?") > -1) {
			this.requestURL = this.requestURL.substring(0, this.requestURL.indexOf("?"));
		}

		this.plusInfo = "Var1=" + this.userId + "&Var2=" + this.pageInfo + "&Var3=" + redirectUrl + "&Var4=&Var5=";

		if (hm.containsKey(this.requestURL)) {
			this.urlCode = hm.get(this.requestURL);
		}

		if (StringUtils.isEmpty(this.redirectUrl)) {
			redirectUrl = "";
		} else {
			redirectUrl = UtilsText.urlEncode(this.redirectUrl);
		}

		// 날짜 생성
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String day = sdf.format(today.getTime());

		Random ran = new Random();
		// 랜덤 문자 길이
		int numLength = 6;
		String randomStr = "";

		for (int i = 0; i < numLength; i++) {
			// 0 ~ 9 랜덤 숫자 생성
			randomStr += ran.nextInt(10);
		}

		// certNum은 최대 40byte 까지 사용 가능
		this.certNum = day + randomStr;

		this.date = day;

		// 02. 1차 암호화 (tr_cert 데이터변수 조합 후 암호화)
		String enc_tr_cert = "";
		this.tr_cert = this.cpId + "/" + this.urlCode + "/" + this.certNum + "/" + this.date + "/" + this.certMet + "/"
				+ this.birthDay + "/" + this.gender + "/" + this.name + "/" + this.phoneNo + "/" + this.phoneCorp + "/"
				+ this.nation + "/" + this.plusInfo + "/" + this.extendVar;
		enc_tr_cert = seed.getEnc(this.tr_cert, "");

		// 03. 1차 암호화 데이터에 대한 위변조 검증값 생성 (HMAC)
		String hmacMsg = "";
		hmacMsg = seed.getMsg(enc_tr_cert);

		// 04. 2차 암호화 (1차 암호화 데이터, HMAC 데이터, extendVar 조합 후 암호화)
		this.tr_cert = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + this.extendVar, "");
	}

	public void success(HttpServletRequest request) {

		String encPara = "";
		String encMsg1 = "";
		String encMsg2 = "";
		String msgChk = "";

		try {

			this.rec_cert = request.getParameter("rec_cert").trim();
			this.k_certNum = (String) request.getSession().getAttribute(this.REQ_KEY);

			// 01. 암호화 모듈 (jar) Loading
			IcertSecuManager seed = new IcertSecuManager();

			// 02. 1차 복호화
			// 수신된 certNum를 이용하여 복호화
			this.rec_cert = seed.getDec(this.rec_cert, this.k_certNum);

			// 03. 1차 파싱
			int inf1 = this.rec_cert.indexOf("/", 0);
			int inf2 = this.rec_cert.indexOf("/", inf1 + 1);

			encPara = this.rec_cert.substring(0, inf1); // 암호화된 통합 파라미터
			encMsg1 = this.rec_cert.substring(inf1 + 1, inf2); // 암호화된 통합 파라미터의 Hash값

			// 04. 위변조 검증
			encMsg2 = seed.getMsg(encPara);

			if (encMsg2.equals(encMsg1)) {
				msgChk = "Y";
			}

			if (msgChk.equals("N")) {
				this.message = "비정상적인 접근";
				log.info("=-= 비정상적인 접근 =");
			}

			// 05. 2차 복호화
			this.rec_cert = seed.getDec(encPara, this.k_certNum);
			log.debug("Kmcism.success() - rec_cert: " + rec_cert);

			// 06. 2차 파싱
			int info1 = this.rec_cert.indexOf("/", 0);
			int info2 = this.rec_cert.indexOf("/", info1 + 1);
			int info3 = this.rec_cert.indexOf("/", info2 + 1);
			int info4 = this.rec_cert.indexOf("/", info3 + 1);
			int info5 = this.rec_cert.indexOf("/", info4 + 1);
			int info6 = this.rec_cert.indexOf("/", info5 + 1);
			int info7 = this.rec_cert.indexOf("/", info6 + 1);
			int info8 = this.rec_cert.indexOf("/", info7 + 1);
			int info9 = this.rec_cert.indexOf("/", info8 + 1);
			int info10 = this.rec_cert.indexOf("/", info9 + 1);
			int info11 = this.rec_cert.indexOf("/", info10 + 1);
			int info12 = this.rec_cert.indexOf("/", info11 + 1);
			int info13 = this.rec_cert.indexOf("/", info12 + 1);
			int info14 = this.rec_cert.indexOf("/", info13 + 1);
			int info15 = this.rec_cert.indexOf("/", info14 + 1);
			int info16 = this.rec_cert.indexOf("/", info15 + 1);
			int info17 = this.rec_cert.indexOf("/", info16 + 1);
			int info18 = this.rec_cert.indexOf("/", info17 + 1);

			this.certNum = this.rec_cert.substring(0, info1);
			this.date = this.rec_cert.substring(info1 + 1, info2);
			this.CI = this.rec_cert.substring(info2 + 1, info3);
			this.phoneNo = this.rec_cert.substring(info3 + 1, info4);
			this.phoneCorp = this.rec_cert.substring(info4 + 1, info5);
			this.birthDay = this.rec_cert.substring(info5 + 1, info6);
			this.gender = this.rec_cert.substring(info6 + 1, info7);
			this.nation = this.rec_cert.substring(info7 + 1, info8);
			this.name = this.rec_cert.substring(info8 + 1, info9);
			this.result = this.rec_cert.substring(info9 + 1, info10);
			this.certMet = this.rec_cert.substring(info10 + 1, info11);
			this.ip = this.rec_cert.substring(info11 + 1, info12);
			this.M_name = this.rec_cert.substring(info12 + 1, info13);
			this.M_birthDay = this.rec_cert.substring(info13 + 1, info14);
			this.M_Gender = this.rec_cert.substring(info14 + 1, info15);
			this.M_nation = this.rec_cert.substring(info15 + 1, info16);
			this.plusInfo = this.rec_cert.substring(info16 + 1, info17);
			this.DI = this.rec_cert.substring(info17 + 1, info18);

			// 07. CI, DI 복호화
			this.CI = seed.getDec(this.CI, this.k_certNum);
			this.DI = seed.getDec(this.DI, this.k_certNum);

			// 08. 추가필드 파싱
			int ExInfo1 = this.plusInfo.indexOf("Var1");
			int ExInfo2 = this.plusInfo.indexOf("Var2");
			int ExInfo3 = this.plusInfo.indexOf("Var3");
			int ExInfo4 = this.plusInfo.indexOf("Var4");
			int ExInfo5 = this.plusInfo.indexOf("Var5");

			this.var1 = this.plusInfo.substring(ExInfo1 + 5, ExInfo2 - 1);
			this.var2 = this.plusInfo.substring(ExInfo2 + 5, ExInfo3 - 1);
			this.var3 = this.plusInfo.substring(ExInfo3 + 5, ExInfo4 - 1);
			this.var4 = this.plusInfo.substring(ExInfo4 + 5, ExInfo5 - 1);
			this.var5 = this.plusInfo.substring(ExInfo5 + 5);

			// var3(redirectUrl) Decoding
			if (StringUtils.isNotEmpty(this.var3)) {
				this.var3 = UtilsText.urlDecode(this.var3);
			}

			// 인증기관에서 일시적으로 잘못된 전문을 주는경우가 있으므로 DI값(64바이트)의 길이를 한번 더 체크함.
			if (this.DI.length() != 64) {
				this.message = "인증결과를 처리중에 오류가 발생했습니다.";
				this.result = "F";
				log.info("=-= 본인인증기관에서 잘못된 전문을 날림. =" + this.DI);
			}

			/** [S] 수신내역 유효성 검증 **********************************************************/
			// 1-1-1) date 값 검증
			// 현재 서버 시각 구하기
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
			String strCurrentTime = formatter.format(new Date());
			Date toDate = formatter.parse(strCurrentTime);
			Date fromDate = formatter.parse(this.date);
			long timediff = toDate.getTime() - fromDate.getTime();

			/** [E] 수신내역 유효성 검증 **********************************************************/

		} catch (Exception e) {
			this.message = "인증결과를 처리중에 오류가 발생했습니다.";
			log.error(e.getMessage(), e);
		}
	}
}
