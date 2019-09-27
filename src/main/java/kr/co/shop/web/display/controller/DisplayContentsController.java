package kr.co.shop.web.display.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
//import kr.co.shop.bo.display.vo.DpWebzineSearchVO;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.display.model.master.DpWebzine;
import kr.co.shop.web.display.service.DisplayContentsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class DisplayContentsController extends BaseController {

	@Autowired
	DisplayContentsService displayContentsService;

	/**
	 * 웹진 리스트조회
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "웹진 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "wbznType", value = "웹진타입", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "페이지", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/contents/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWebzineList(Parameter<DpWebzine> parameter) throws Exception {

		Pageable<DpWebzine, DpWebzine> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Page<DpWebzine> page = displayContentsService.getDpWebzineList(pageable);

//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("page", page);

		return UtilsREST.responseOk(parameter, page.getGrid());
	}

	/**
	 * 웹진 조회
	 * 
	 * @Desc :
	 * @Method Name : getWebzine
	 * @Date : 2019. 4. 11.
	 * @Author : SANTA
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "웹진 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "wbznSeq", value = "웹진순번", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/contents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWebzine(Parameter<DpWebzine> parameter) throws Exception {

		DpWebzine dpWebzine = parameter.get();
		dpWebzine = displayContentsService.getDpWebzine(dpWebzine, parameter);

		return UtilsREST.responseOk(parameter, dpWebzine);
	}

	@ApiOperation(value = "OTS 웹진 목록 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/display/contents/get-ots-webzine-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOtsWebzineList(Parameter<DpWebzine> parameter) throws Exception {
		log.debug("OTS 웹진 목록 조회");
		List<DpWebzine> list = this.displayContentsService.getOtsWebzineList(parameter.get());
		return UtilsREST.responseOk(parameter, list);
	}

}