package kr.co.shop.web.display.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.display.service.DisplayCommonService;
import kr.co.shop.web.display.service.DisplayPageService;
import kr.co.shop.web.display.vo.GnbVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class DisplayCommonController extends BaseController {

	@Autowired
	private DisplayCommonService gnbService;

	@Autowired
	private DisplayPageService displayPageService;

	/**
	 * GNB 영역 조회
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "GNB 영역 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dispPageNo", value = "전시페이지번호", required = true, dataType = "string", paramType = "query", defaultValue = "1000011"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "upCtgrNo", value = "상위 전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrLevel", value = "카테고리 레벨", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "ctgrNo", value = "전시카테고리번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "deviceCode", value = "디바이스코드", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/gnb", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGnb(Parameter<GnbVO> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, gnbService.getGnbData(parameter));
	}

	/**
	 * 헤더 띠배너
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "헤더 띠배너", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dispPageNoArr", value = "전시페이지번호Arr", required = true, dataType = "array", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "chnnlNo", value = "채널번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000"),
			@ApiImplicitParam(name = "deviceCode", value = "디바이스코드", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/header-banner", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHeaderBanner(Parameter<GnbVO> parameter) throws Exception {

		if (UtilsText.isBlank(parameter.get().getChnnlNo())) {
			return UtilsREST.responseOk(parameter, new HashMap<String, Object>());
		}

		return UtilsREST.responseOk(parameter, gnbService.getHeaderBanner(parameter));
	}

	/**
	 * App 스플래시 이미지 리스트 조회
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "App 스플래시 이미지 리스트 조회", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query", defaultValue = "10000") })
	@GetMapping(value = "v1.0/display/app/splash", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSplashImageList(Parameter<?> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, displayPageService.getAppSplashList(parameter.getString("siteNo")));
	}

}
