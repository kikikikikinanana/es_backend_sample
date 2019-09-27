package kr.co.shop.web.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.Kmcism;
import kr.co.shop.util.Siren24;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberEasyLogin;
import kr.co.shop.web.member.model.master.MbMemberLoginHistory;
import kr.co.shop.web.member.model.master.MbPersistentLogins;
import kr.co.shop.web.member.service.MemberPointService;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.member.vo.CertificationVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class MemberController extends BaseController {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberPointService memberPointService;

	@ApiOperation(value = "회원 조회", notes = "로그인 아이디로 회원을 조회한다.", httpMethod = "POST", protocols = "http", response = MbMember.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "로그인 아이디", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/user/select", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadMemberByLoginId(Parameter<MbMember> parameter) throws Exception {
		MbMember mbMember = parameter.get();
		log.debug("loadMemberByLoginId - {}", mbMember);
		MbMember member = memberService.getMemberByLoginId(parameter.get());

		return UtilsREST.responseOk(parameter, member);
	}

	@PostMapping(value = "v1.0/member/user/remember-me/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNewToken(Parameter<MbPersistentLogins> parameter) throws Exception {
		memberService.createNewToken(parameter.get());
		return UtilsREST.responseOk(parameter);

	}

	@PostMapping(value = "v1.0/member/user/remember-me/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateToken(Parameter<MbPersistentLogins> parameter) throws Exception {
		memberService.updateToken(parameter.get());
		return UtilsREST.responseOk(parameter);

	}

	@PostMapping(value = "v1.0/member/user/remember-me/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTokenForSeries(Parameter<MbPersistentLogins> parameter) throws Exception {
		MbPersistentLogins samplePersistentLogins = memberService.getTokenForSeries(parameter.get());
		return UtilsREST.responseOk(parameter, samplePersistentLogins);
	}

	@PostMapping(value = "v1.0/member/user/remember-me/remove", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeUserTokens(Parameter<MbPersistentLogins> parameter) throws Exception {
		memberService.removeUserTokens(parameter.get());
		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/user/select-member-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectMemberList(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		List<MbMember> memberList = memberService.selectMemberList(params);
		return UtilsREST.responseOk(parameter, memberList);
	}

	@PostMapping(value = "v1.0/member/user/select-member-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectMemberInfo(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();

		return UtilsREST.responseOk(parameter, memberService.selectMemberInfo(params));
	}

	@PostMapping(value = "v1.0/member/user/select-id-search-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectIdSerarchInfoList(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		List<MbMember> memberList = memberService.selectIdSerarchInfoList(params);
		return UtilsREST.responseOk(parameter, memberList);
	}

	@PostMapping(value = "v1.0/member/user/select-sns-login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectSnsLoginInfo(Parameter<MbMemberEasyLogin> parameter) throws Exception {
		MbMember result = new MbMember();
		MbMemberEasyLogin mbMemberEasyLogin = parameter.get();
		result = memberService.getSnsLoginInfo(mbMemberEasyLogin);

		return UtilsREST.responseOk(parameter, result);
	}

	@ApiOperation(value = "SNS 연결정보 저장", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "snsGbnCode", value = "SNS구분코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "snsCnnctnInfo", value = "SNS연결정보(아이디)", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "addInfo01", value = "SNS추가정보1(Access_token)", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "addInfo02", value = "SNS추가정보2(Refresh_token)", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/user/create-sns-connect-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSnsConnectInfo(Parameter<MbMemberEasyLogin> parameter) throws Exception {
		MbMemberEasyLogin params = parameter.get();
		memberService.createSnsConnectInfo(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "회원 로그인 실패 카운트 업데이트", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "loginFailIncrease", value = "로그인 실패여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/user/update-member-login-fail-count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateLoginFailCount(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		memberService.updateLoginFailCount(params);
		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/user/update-member-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMemberInfo(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		memberService.updateMemberInfo(params);
		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/user/create-member-history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createMemberHistory(Parameter<MbMemberLoginHistory> parameter) throws Exception {
		MbMemberLoginHistory params = parameter.get();
		memberService.createMemberHistory(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "휴대폰 인증번호 발송요청", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "crtfcPathCode", value = "인증경로", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoSendInfo", value = "인증정보(휴대폰번호)", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoMemberName", value = "회원명", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = "v1.0/member/join/create-cert-number-hdphn", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCertNumberHdphn(Parameter<CertificationVO> parameter) throws Exception {
		CertificationVO certificationVO = parameter.get();
		// TODO validate

		String certKey = memberService.createCertNumberHdphn(certificationVO);

		return UtilsREST.responseOk(parameter, certKey);
	}

	@ApiOperation(value = "이메일 인증번호 발송요청", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "crtfcPathCode", value = "인증경로", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoSendInfo", value = "인증정보(이메일주소)", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = "v1.0/member/join/create-cert-number-email", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCertNumberEmail(Parameter<CertificationVO> parameter) throws Exception {
		CertificationVO certificationVO = parameter.get();
		// TODO validate

		memberService.createCertNumberEmail(certificationVO);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "인증번호 확인", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "memberName", value = "회원명", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoSendInfo", value = "인증정보(휴대폰/이메일)", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoText", value = "인증번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "validTime", value = "인증시간", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "crtfcSuccessYn", value = "인증여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/join/validate-certification-number", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateCertificationNumber(Parameter<CertificationVO> parameter) throws Exception {
		CertificationVO certificationVO = parameter.get();
		// TODO validate

		memberService.setValidateCertificationNumber(certificationVO);

		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/user/create-member-info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNewMemberInfo(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		memberService.createNewMemberInfo(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "아이핀 인증 정보 확인", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/member/ipin-certify-check", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getIpinCertifyCheck(Parameter<Siren24> parameter) throws Exception {

		Siren24 siren24 = new Siren24();
		siren24.setRequestURL(parameter.getString("requestUrl"));
		siren24.setUserId(parameter.getString("userId"));
		siren24.setPageInfo(parameter.getString("pageInfo"));
		siren24.setIsMobile(parameter.getString("isMobile"));
		Map<String, Object> map = memberService.getIpinCertifyCheck(siren24);

		return UtilsREST.responseOk(parameter, map);
	}

	@PostMapping(value = "/v1.0/member/kmci-certify-check", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getKmciCertifyCheck(Parameter<Kmcism> parameter) throws Exception {
		Kmcism kmcism = new Kmcism();
		kmcism.setRequestURL(parameter.getString("requestUrl"));
		kmcism.setUserId(parameter.getString("userId"));
		kmcism.setPageInfo(parameter.getString("pageInfo"));
		kmcism.setIsMobile(parameter.getString("isMobile"));
		Map<String, Object> map = memberService.getKmciCertifyCheck(kmcism);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "인증이력 회원번호 업데이트(아이디/패스워드 찾기)", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "login", value = "로그인아이디", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoSendInfo", value = "인증정보(휴대폰/이메일)", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoText", value = "인증번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/join/update-certification-member", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCertificationMember(Parameter<CertificationVO> parameter) throws Exception {
		CertificationVO certificationVO = parameter.get();
		memberService.updateCertificationMember(certificationVO);
		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/join/member-pw-change", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePwChange(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();

		memberService.updatePwChange(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "맴버십 회원 전환 가입 약관동의", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "termsDtlSeq", value = "약관상세순번", required = true, dataType = "int", paramType = "body"),
			@ApiImplicitParam(name = "termsAgreeYn", value = "약관동의여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/join/member-change", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMemberChangeInfo(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();

		memberService.updateMemberChangeInfo(params);
		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/join/member-change-join", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMemberChangeJoin(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();

		memberService.updateMemberChangeJoin(params);
		return UtilsREST.responseOk(parameter);
	}

	@PostMapping(value = "v1.0/member/selectRecmdStore", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectRecmdStore(Parameter<?> parameter) throws Exception {
		String storeCode = parameter.getString("storeCode");
		return UtilsREST.responseOk(parameter, memberService.selectRecmdStore(storeCode));
	}

	@ApiOperation(value = "회원 맴버십포인트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "v1.0/member/selectMemberPoint")
	public ResponseEntity<?> getMemberPoint(Parameter<MbMember> parameter) throws Exception {
		MbMember param = parameter.get();
		Map<String, Object> map = memberService.getMemberPoint(param);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "APP 로그인", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginId", value = "로그인아이디", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "deviceToken", value = "디바이스인증토큰", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = "v1.0/member/join/select-app-login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectAppLoginInfo(Parameter<?> parameter) throws Exception {
		MbMember result = new MbMember();
		result.setLoginId(parameter.getString("loginId"));
		result.setDeviceToken(parameter.getString("deviceToken"));

		result = memberService.getAppLoginInfo(result);
		return UtilsREST.responseOk(parameter, result);
	}

}