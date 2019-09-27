/**
 * 
 */
package kr.co.shop.web.system.controller;

import java.util.List;

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
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.model.master.SySiteChnnl;
import kr.co.shop.web.system.service.SiteService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : SiteController.java
 * @Project : shop.backend
 * @Date : 2019. 3. 19.
 * @Author : Kimyounghyun
 */
@Slf4j
@RestController
@RequestMapping("api")
public class SiteController extends BaseController {

	@Autowired
	SiteService siteService;

	@ApiOperation(value = "사이트 목록 조회", notes = "사이트 목록을 조회한다.", httpMethod = "GET", protocols = "http", response = SySite.class, responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pc", value = "default = true, 이 값에 따라서 pc, mobile url이 리턴된다.", required = false, dataType = "Boolean", paramType = "body") })
	@GetMapping(value = { "v1.0/site/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUseSite(Parameter<SySite> parameter) throws Exception {
		SySite sySite = parameter.get();
		if (sySite.getPc() == null) {
			sySite.setPc(true);
		}
		List<SySite> sySiteList = siteService.getSiteList(sySite);

		return UtilsREST.responseOk(parameter, sySiteList);
	}

	@ApiOperation(value = "사이트에 속한 사용중인 채널목록 조회", notes = "사이트에 속한 사용중인 채널 목록을 조회한다.", httpMethod = "GET", protocols = "http", response = SySiteChnnl.class, responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pc", value = "default = true, 이 값에 따라서 pc, mobile url이 리턴된다.", required = false, dataType = "Boolean", paramType = "body") })
	@GetMapping(value = { "v1.0/site/channel/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUseChannelBySiteNo(Parameter<SySiteChnnl> parameter) throws Exception {
		SySiteChnnl sySiteChnnl = parameter.get();
		if (sySiteChnnl.getPc() == null) {
			sySiteChnnl.setPc(true);
		}
		List<SySiteChnnl> sySiteChnnlList = siteService.getUseChannelListBySiteNo(sySiteChnnl);

		return UtilsREST.responseOk(parameter, sySiteChnnlList);
	}

	@ApiOperation(value = "사용중인 전체 채널 목록 조회", notes = "사이트구분 없이 전체 채널 목록을 조회한다.", httpMethod = "GET", protocols = "http", response = SySite.class, responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pc", value = "default = true, 이 값에 따라서 pc, mobile url이 리턴된다.", required = false, dataType = "Boolean", paramType = "body") })
	@GetMapping(value = { "v1.0/channel/list" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUseChannel(Parameter<SySiteChnnl> parameter) throws Exception {
		SySiteChnnl sySiteChnnl = parameter.get();
		List<SySiteChnnl> sySiteList = siteService.getUseChannelList(sySiteChnnl);

		return UtilsREST.responseOk(parameter, sySiteList);
	}

}
