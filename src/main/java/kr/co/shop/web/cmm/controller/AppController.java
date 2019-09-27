package kr.co.shop.web.cmm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.cmm.model.master.CmPushAppDownloadMember;
import kr.co.shop.web.cmm.service.AppService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class AppController extends BaseController {

	@Autowired
	AppService appService;

	@ApiOperation(value = "FCM 토큰 등록", notes = "App 설치 시 fcm 토큰을 포함한 디바이스 정보를 저장한다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "appOsCode", value = "app os코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "appVerText", value = "app 버전", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "fcmTokenText", value = "fcm 토큰", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "appPushRecvYn", value = "push 수신여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "nightPushRecvYn", value = "야간 push 수신여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "bioCrtfcLoginUseYn", value = "바이오인증 로그인여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "v1.0/app/fcm-token/create" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createFcmToken(Parameter<CmPushAppDownloadMember> parameter) throws Exception {
		CmPushAppDownloadMember cmPushAppDownloadMember = parameter.get();
		appService.setFcmToken(cmPushAppDownloadMember);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "디바이스 토큰 저장", notes = "App 최초 로그인 시 디바이스 토큰을 저장한다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fcmTokenText", value = "fcm 토큰", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "deviceTokenText", value = "디바이스 토큰", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "loginId", value = "로그인 아이디", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "v1.0/app/device-token/update" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateDeviceToken(Parameter<CmPushAppDownloadMember> parameter) throws Exception {
		CmPushAppDownloadMember cmPushAppDownloadMember = parameter.get();
		appService.setDeviceToken(cmPushAppDownloadMember);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "push수신동의", notes = "App push 수신 동의를 저장한다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fcmTokenText", value = "fcm 토큰", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "appPushRecvYn", value = "push 수신여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "nightPushRecvYn", value = "야간 push 수신여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "v1.0/app/receive-push/update" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateReceivePush(Parameter<CmPushAppDownloadMember> parameter) throws Exception {
		CmPushAppDownloadMember cmPushAppDownloadMember = parameter.get();
		appService.setReceivePush(cmPushAppDownloadMember);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "바이오인증로그인사용여부", notes = "App 바이오인증 로그인 사용여부를 저장한다.", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "loginId", value = "로그인아이디", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "deviceTokenText", value = "디바이스 토큰", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "bioCrtfcLoginUseYn", value = "push 수신여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = { "v1.0/app/bio-login/update" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateBioLogin(Parameter<CmPushAppDownloadMember> parameter) throws Exception {
		CmPushAppDownloadMember cmPushAppDownloadMember = parameter.get();
		appService.setBioLogin(cmPushAppDownloadMember);

		return UtilsREST.responseOk(parameter);
	}

}