package kr.co.shop.web.cmm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.cmm.model.master.CmArea;
import kr.co.shop.web.cmm.model.master.CmAreaDetail;
import kr.co.shop.web.cmm.service.AreaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class AreaController extends BaseController {

	@Autowired
	AreaService areaService;

	@ApiOperation(value = "지역 리스트 조회(광역시/도)", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "v1.0/area/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCmAreaList(Parameter<CmArea> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, areaService.getCmAreaList());
	}

	@ApiOperation(value = "지역 상세 리스트 조회(시/군/구)", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "v1.0/area-detail/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCmAreaDetailList(Parameter<CmAreaDetail> parameter) throws Exception {

		return UtilsREST.responseOk(parameter, areaService.getCmAreaDetailList(parameter.get()));
	}
}