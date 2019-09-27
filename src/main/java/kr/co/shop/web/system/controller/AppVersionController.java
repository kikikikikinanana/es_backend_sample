/**
 * 
 */
package kr.co.shop.web.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsREST;
import kr.co.shop.web.system.model.master.SyAppVersion;
import kr.co.shop.web.system.service.AppVersionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class AppVersionController extends BaseController {

	@Autowired
	AppVersionService appVersionService;

	/**
	 *
	 * @Desc : 앱버전 정보를 조회한다.
	 * @Method Name : getAppVersionData
	 * @Date : 2019. 6. 27.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "앱버전 데이터를 조회", notes = "사이트/os에 따른 앱버전 데이터를 조회", httpMethod = "GET", protocols = "http", response = SyAppVersion.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "appOsCode", value = "OS코드", required = true, dataType = "string", paramType = "query") })
	@GetMapping(value = "v1.0/app/version", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAppVersionData(Parameter<SyAppVersion> parameter) throws Exception {
		SyAppVersion syAppVersion = parameter.get();
		syAppVersion = appVersionService.getAppVersionData(syAppVersion);

		return UtilsREST.responseOk(parameter, syAppVersion);
	}

}
