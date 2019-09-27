package kr.co.shop.web.display.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsREST;
import kr.co.shop.web.display.model.master.BdPopup;
import kr.co.shop.web.display.service.PopupService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class PopupController extends BaseController {

	@Autowired
	private PopupService popupService;

	@ApiOperation(value = "팝업 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/popup" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBdPopup(Parameter<BdPopup> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, popupService.getBdPopup(parameter.get()));
	}

	@ApiOperation(value = "팝업 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/popup/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBdPopupList(Parameter<BdPopup> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, popupService.getBdPopupList(parameter.get()));
	}

	@ApiOperation(value = "팝업 맵 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = { "v1.0/display/popup/map" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBdPopupMap(Parameter<BdPopup> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, popupService.getBdPopupMap(parameter.get()));
	}
}