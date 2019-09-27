package kr.co.shop.web.cmm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.cmm.model.master.CmDaysCondtn;
import kr.co.shop.web.cmm.service.DaysCondtnService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class DaysCondtnController extends BaseController {

	@Autowired
	DaysCondtnService daysCondtnService;

	/**
	 * @Desc : 조건날짜 조회
	 * @Method Name : getDaysCondtn
	 * @Date : 2019. 3. 20.
	 * @Author : 이동엽
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "v1.0/days-condtn/select", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDaysCondtn(Parameter<CmDaysCondtn> parameter) throws Exception {
		CmDaysCondtn cmDaysCondtn = parameter.get();
		CmDaysCondtn result = daysCondtnService.getDaysCondtn(cmDaysCondtn);
		return UtilsREST.responseOk(parameter, result);
	}
}
