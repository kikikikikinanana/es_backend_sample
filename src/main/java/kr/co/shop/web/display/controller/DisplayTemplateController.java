package kr.co.shop.web.display.controller;

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
import kr.co.shop.web.display.model.master.DpDisplayTemplate;
import kr.co.shop.web.display.service.DisplayTemplateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class DisplayTemplateController extends BaseController {

	@Autowired
	private DisplayTemplateService displayTemplateService;

	@ApiOperation(value = "템플릿 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "dispTmplNo", value = "템플릿번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/template", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDpCategoryList(Parameter<DpDisplayTemplate> parameter) throws Exception {

		DpDisplayTemplate dpDisplayTemplate = displayTemplateService.getDpTemplate(parameter.get());

		return UtilsREST.responseOk(parameter, dpDisplayTemplate);
	}
}