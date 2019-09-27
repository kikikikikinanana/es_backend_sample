package kr.co.shop.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.sci.v2.ipin.secu.SciSecuManager;
import com.sci.v2.ipin.secu.hmac.SciHmac;

import kr.co.shop.common.config.Config;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Siren24 {
//    Log log = LogFactory.getLog(this.getClass());

	final String REQ_KEY = "REQ_SIREN24";

	private String id = ""; // 회원사 ID
	private String reqNum = ""; // 요청번호
	private String retUrl = ""; // 결과 수신 URL
	private String isMobile = ""; // 모바일 여부
	private String srvNo = ""; // 서비스번호
	private String exVar = "0000000000000000"; // 복호화용 임시필드

	private String encStr = "";
	private String reqInfo = ""; // 요청데이터

	private String retInfo = "";
	private String encPara = "";
	private String decPara = "";
	private String encMsg = ""; // HMAC 메세지

	private String tranjectionReqNum = ""; // 요청번호(세션)

	private String rtnReqNum = ""; // 요청번호(복호화)
	private String vDiscrNo = ""; // 가상식별번호(아이핀)
	private String name = ""; // 성명
	private String result = ""; // 결과값 (1인경우에만 정상)
	private String age = ""; // 연령대(1:0 ~ 8세 2:9 ~ 11세 3:12 ~ 13세 4:14세 5:15 ~ 17세 6:18세 7:19세 8:20세이상)
	private String sex = ""; // 성별(M:남성 F:여성)
	private String ip = ""; // ip
	private String authInfo = ""; // 발급수단정보(0:본인 공인인증서 1:본인 신용카드 2:본인 핸드폰 3:본인 대면확인 4:신원보증인 아이핀 5:신원보증인 공인인증서
									// 6:신원보증인 신용카드 7:신원보증인 핸드폰 8:신원보증인 대면확인 9:본인 외국인등록번호 10:본인 여권번호)
	private String birth = ""; // 생년월일(YYYYMMDD)
	private String fgn = ""; // 외국인구분(0:내국인 1:외국인)
	private String discrHash = ""; // 중복가입확인정보(DI)
	private String ciVersion = ""; // 연계정보 버젼(CI)
	private String ciscrHash = ""; // 연계정보

	private String var1 = ""; // 추가필드1
	private String var2 = ""; // 추가필드2
	private String var3 = ""; // 추가필드3
	private String var4 = ""; // 추가필드4
	private String var5 = ""; // 추가필드5

	private String msgChk = "N"; // 위조/변조 검증 결과
	private String message = ""; // 오류메시지
	private String plusInfo = ""; // 추가DATA정보

	private String userId = "";
	private String pageInfo = "";

	private HashMap<String, String> hm = new HashMap<String, String>();
	private String requestURL = null;

	private String redirectUrl = "";

	public Siren24() {
		// LOCAL
		this.hm.put("https://local.a-rt.com:8500/member/member-join-page", "012001");
		this.hm.put("https://local.a-rt.com:8500/member/member-change-join-page", "012002");
		this.hm.put("https://local.a-rt.com:8500/member/member-pw-search", "012003");
		this.hm.put("https://local.a-rt.com:8500/member/member-change-login-page", "012004");
		this.hm.put("https://local.a-rt.com:8500/promotion/event/draw/member/check", "012005");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-join-page", "015001");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-change-join-page", "015002");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-pw-search", "015003");
		this.hm.put("https://m.local.a-rt.com:8600/member/member-change-login-page", "015004");
		this.hm.put("https://m.local.a-rt.com:8600/promotion/event/draw/member/check", "015005");

		// DEV
		this.hm.put("https://dev.a-rt.com/member/member-join-page", "011006");
		this.hm.put("https://dev.a-rt.com/member/member-change-join-page", "011007");
		this.hm.put("https://dev.a-rt.com/member/member-pw-search", "011003");
		this.hm.put("https://dev.a-rt.com/member/member-change-login-page", "011008");
		this.hm.put("https://dev.a-rt.com/promotion/event/draw/member/check", "011005");
		this.hm.put("https://m.dev.a-rt.com/member/member-join-page", "014001");
		this.hm.put("https://m.dev.a-rt.com/member/member-change-join-page", "014002");
		this.hm.put("https://m.dev.a-rt.com/member/member-pw-search", "014003");
		this.hm.put("https://m.dev.a-rt.com/member/member-change-login-page", "014004");
		this.hm.put("https://m.dev.a-rt.com/promotion/event/draw/member/check", "014005");

		this.id = Config.getString("siren24.company.id");
		/*
		 * 결과수신 URL(23http:// 포함한 주소) 23http:// 인 경우 - 동일 프레임에서 결과 수신하는 경우 24http:// 인
		 * 경우 - 부모창에서 결과 수신하는 경우
		 */
		if (UtilsText.equals(this.isMobile, Const.BOOLEAN_TRUE)) {
			this.retUrl = "23" + Config.getString("url.www.mobile.https")
					+ Config.getString("siren24.return.mobile.url");
		} else {
			this.retUrl = "23" + Config.getString("url.www.https") + Config.getString("siren24.return.url");
		}
	}

	public void beforeSend() {
//        this.requestURL = request.getParameter("requestUrl");
		String redirectUrl = "";

		if (this.requestURL.indexOf("?") > -1) {
			this.requestURL = this.requestURL.substring(0, this.requestURL.indexOf("?"));
		}

		if (this.hm.containsKey(this.requestURL)) {
			this.srvNo = this.hm.get(this.requestURL);
		}

		if (StringUtils.isEmpty(this.redirectUrl)) {
			redirectUrl = "";
			redirectUrl = UtilsText.urlEncode(this.redirectUrl);
		}
		if (UtilsText.equals(this.isMobile, Const.BOOLEAN_TRUE)) {
			if (UtilsText.equals(this.pageInfo, CommonCode.CRTFC_PATH_CODE_DRAW_EVENT)) {
				this.retUrl = "23" + Config.getString("url.www.mobile.https")
						+ Config.getString("siren24.draw.return.url");
			} else {
				this.retUrl = "23" + Config.getString("url.www.mobile.https")
						+ Config.getString("siren24.return.mobile.url");
			}
		} else {
			if (UtilsText.equals(this.pageInfo, CommonCode.CRTFC_PATH_CODE_DRAW_EVENT)) {
				this.retUrl = "23" + Config.getString("url.www.https") + Config.getString("siren24.draw.return.url");
			} else {
				this.retUrl = "23" + Config.getString("url.www.https") + Config.getString("siren24.return.url");
			}
		}

		this.plusInfo = "Var1=" + this.userId + "&Var2=" + this.pageInfo + "&Var3=" + redirectUrl + "&Var4=&Var5=";
		this.retUrl = this.retUrl + "?" + this.plusInfo;

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
		this.reqNum = day + randomStr;

		// 암호화 모듈 선언
		SciSecuManager seed = new SciSecuManager();

		// 1차 암호화
		this.encStr = "";
		this.reqInfo = this.reqNum + "/" + this.id + "/" + this.srvNo + "/" + this.exVar; // 데이터 암호화
		this.encStr = seed.getEncPublic(this.reqInfo);

		// 위변조 검증 값 등록
		String hmacMsg = SciHmac.HMacEncriptPublic(this.encStr);

		// 2차 암호화
		this.reqInfo = seed.getEncPublic(this.encStr + "/" + hmacMsg + "/" + "00000000"); // 2차암호화

	}

	public void success(HttpServletRequest request) {
		try {

			this.retInfo = request.getParameter("retInfo").trim();
			this.var1 = request.getParameter("Var1").trim();
			this.var2 = request.getParameter("Var2").trim();
			this.var3 = request.getParameter("Var3").trim();
			this.var4 = request.getParameter("Var4").trim();
			this.var5 = request.getParameter("Var5").trim();
			this.tranjectionReqNum = (String) request.getSession().getAttribute(this.REQ_KEY);

			// var3(redirectUrl) Decoding
			if (StringUtils.isNotEmpty(this.var3)) {
				this.var3 = UtilsText.urlDecode(this.var3);
			}

			// 1. 암호화 모듈 (jar) Loading
			SciSecuManager sciSecuMg = new SciSecuManager();

			this.retInfo = sciSecuMg.getDec(this.retInfo, this.tranjectionReqNum);

			// 2.1차 파싱---------------------------------------------------------------
			int inf1 = this.retInfo.indexOf("/", 0);
			int inf2 = this.retInfo.indexOf("/", inf1 + 1);

			this.encPara = this.retInfo.substring(0, inf1); // 암호화된 통합 파라미터
			this.encMsg = this.retInfo.substring(inf1 + 1, inf2); // 암호화된 통합 파라미터의 Hash값

			// 3.위/변조 검증 ---------------------------------------------------------------
			if (sciSecuMg.getMsg(this.encPara).equals(this.encMsg)) {
				this.msgChk = "Y";
			}

			if (this.msgChk.equals("N")) {
				this.message = "비정상적인 접근입니다.";
				return;
			}

			// 4.파라미터별 값 가져오기
			// ---------------------------------------------------------------
			decPara = sciSecuMg.getDec(encPara, tranjectionReqNum);

			int info1 = this.decPara.indexOf("/", 0);
			int info2 = this.decPara.indexOf("/", info1 + 1);
			int info3 = this.decPara.indexOf("/", info2 + 1);
			int info4 = this.decPara.indexOf("/", info3 + 1);
			int info5 = this.decPara.indexOf("/", info4 + 1);
			int info6 = this.decPara.indexOf("/", info5 + 1);
			int info7 = this.decPara.indexOf("/", info6 + 1);
			int info8 = this.decPara.indexOf("/", info7 + 1);
			int info9 = this.decPara.indexOf("/", info8 + 1);
			int info10 = this.decPara.indexOf("/", info9 + 1);
			int info11 = this.decPara.indexOf("/", info10 + 1);
			// CI값은 전송유무에 따라 전송이 안될수 있음. 여기선 DI값만 사용하니 주석처리
			int info12 = this.decPara.indexOf("/", info11 + 1);
			int info13 = this.decPara.indexOf("/", info12 + 1);

			this.rtnReqNum = this.decPara.substring(0, info1); // 요청번호(복호화)
			this.vDiscrNo = this.decPara.substring(info1 + 1, info2); // 가상식별번호(아이핀)
			this.name = this.decPara.substring(info2 + 1, info3); // 성명
			this.result = this.decPara.substring(info3 + 1, info4); // 결과값 (1인경우에만 정상)
			this.age = this.decPara.substring(info4 + 1, info5); // 연령대(1:0 ~ 8세 2:9 ~ 11세 3:12 ~ 13세 4:14세 5:15 ~ 17세
																	// 6:18세 7:19세 8:20세이상)
			this.sex = this.decPara.substring(info5 + 1, info6); // 성별(M:남성 F:여성)
			this.ip = this.decPara.substring(info6 + 1, info7); // ip
			this.authInfo = this.decPara.substring(info7 + 1, info8); // 발급수단정보(0:본인 공인인증서 1:본인 신용카드 2:본인 핸드폰 3:본인 대면확인
																		// 4:신원보증인 아이핀 5:신원보증인 공인인증서 6:신원보증인 신용카드
																		// 7:신원보증인 핸드폰 8:신원보증인 대면확인 9:본인 외국인등록번호 10:본인
																		// 여권번호)
			this.birth = this.decPara.substring(info8 + 1, info9); // 생년월일(YYYYMMDD)
			this.fgn = this.decPara.substring(info9 + 1, info10); // 외국인구분(0:내국인 1:외국인)
			this.discrHash = this.decPara.substring(info10 + 1, info11); // 중복가입확인정보(DI)
			this.ciVersion = this.decPara.substring(info11 + 1, info12); // 연계정보 버젼(CI)
			this.ciscrHash = this.decPara.substring(info12 + 1, info13); // 연계정보

			this.discrHash = sciSecuMg.getDec(this.discrHash, this.tranjectionReqNum); // 중복가입확인정보는 한번더 복호화
			this.ciscrHash = sciSecuMg.getDec(this.ciscrHash, this.tranjectionReqNum); // 연계정보는 한번더 복호화

			// 인증기관에서 일시적으로 잘못된 전문을 주는경우가 있으므로 DI값(64바이트)의 길이를 한번 더 체크함.
			if (this.discrHash.length() != 64) {
				this.message = "인증결과를 처리중에 오류가 발생했습니다.";
				this.result = "-1";
			}
			// 인증기관에서 일시적으로 잘못된 전문을 주는경우가 있으므로 CI값(88바이트)의 길이를 한번 더 체크함.
			if (this.ciscrHash.length() != 88) {
				this.message = "인증결과를 처리중에 오류가 발생했습니다.";
				this.result = "-1";
			}

		} catch (Exception e) {
			this.message = "인증결과를 처리중에 오류가 발생했습니다.";
		}
	}
}
