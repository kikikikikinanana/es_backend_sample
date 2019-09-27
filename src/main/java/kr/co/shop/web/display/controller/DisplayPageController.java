package kr.co.shop.web.display.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.display.model.master.DpDisplayPage;
import kr.co.shop.web.display.service.DisplayPageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class DisplayPageController extends BaseController {

	@Autowired
	private DisplayPageService displayPageService;

	@ApiOperation(value = "페이지 정보 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dispPageNo", value = "전시페이지번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "deviceCode", value = "디바이스코드", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/page/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPageInfo(Parameter<DpDisplayPage> parameter) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = displayPageService.getPageInfo(parameter.get(), parameter);

		return UtilsREST.responseOk(parameter, resultMap);
	}

}