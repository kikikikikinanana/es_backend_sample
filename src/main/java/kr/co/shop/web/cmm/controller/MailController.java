package kr.co.shop.web.cmm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.cmm.service.MailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController	
@RequestMapping("api")
public class MailController extends BaseController {
	
	@Autowired
	MailService mailService;
	
	
	@GetMapping(value = { "/v1.0/mail/mail-send" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setSendMail(Parameter<?> parameter) throws Exception {
		
		return UtilsREST.responseOk(parameter, null);
	}
}				