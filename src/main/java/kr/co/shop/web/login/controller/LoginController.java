package kr.co.shop.web.login.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.login.service.LoginService;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class LoginController extends BaseController {

	@Autowired
	LoginService loginService;

	@ApiOperation(value = "로그인 form data 조회", notes = "로그인 화면에 필요한 data를 조회한다.", httpMethod = "GET", protocols = "http", response = SyCodeDetail.class, responseContainer = "List")
	@GetMapping(value = "v1.0/login/login-form", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loginForm(Parameter<?> parameter) throws Exception {
		Map<String, Object> map = loginService.getLoginFormData();

		return UtilsREST.responseOk(parameter, map);
	}

}