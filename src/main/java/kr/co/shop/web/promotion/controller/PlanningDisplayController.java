package kr.co.shop.web.promotion.controller;

import java.util.List;
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
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsDevice;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.product.model.master.PdProductWrapper;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplay;
import kr.co.shop.web.promotion.model.master.PrPlanningDisplayCorner;
import kr.co.shop.web.promotion.service.PlanningDisplayService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api")
public class PlanningDisplayController extends BaseController {

	@Autowired
	PlanningDisplayService planningDisplayService;

	/**
	 * @Desc : 기획전 목록 조회
	 * @Method Name : getPlanningDisplayList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "기획전 목록 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "plndpStatCode", value = "기획전승인코드", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/planning-display/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPlanningDisplayList(Parameter<PrPlanningDisplay> parameter) throws Exception {

		// device code
		if (UtilsDevice.getDevice(parameter.getRequest()).isMobile()) {
			parameter.get().setDeviceCode(CommonCode.DEVICE_MOBILE);
		} else if (UtilsDevice.getDevice(parameter.getRequest()).isNormal()) {
			parameter.get().setDeviceCode(CommonCode.DEVICE_PC);
		}

		Pageable<PrPlanningDisplay, PrPlanningDisplay> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(12);
		pageable.setPageNum(parameter.get().getPageNum());

		Page<PrPlanningDisplay> page = planningDisplayService.getPrPlanningDisplayList(pageable);

		return UtilsREST.responseOk(parameter, page.getGrid());
	}

	/**
	 * @Desc : 기획전 상세 조회
	 * @Method Name : getPlanningDisplayDetail
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "기획전 상세 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "plndpNo", value = "기획전번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/planning-display/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPlanningDisplayDetail(Parameter<PrPlanningDisplay> parameter) throws Exception {

		PrPlanningDisplay prPlanningDisplay = parameter.get();
		prPlanningDisplay = planningDisplayService.getPrPlanningDisplayDetail(prPlanningDisplay);

		return UtilsREST.responseOk(parameter, prPlanningDisplay);
	}

	/**
	 * @Desc : 기획전 상세 코너 리스트 조회 (일반)
	 * @Method Name : getPlanningDisplayCornerList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "기획전 상세 코너 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "v1.0/promotion/planning-display/detail/corner/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPlanningDisplayCornerList(Parameter<PrPlanningDisplay> parameter) throws Exception {

		List<PrPlanningDisplayCorner> prPlanningDisplayCornerList = planningDisplayService
				.getPrPlanningDisplayCornerList(parameter);

		return UtilsREST.responseOk(parameter, prPlanningDisplayCornerList);
	}

	/**
	 * @Desc : 기획전 상세 상품 리스트 조회 (소제목 미적용)
	 * @Method Name : getPlanningDisplayCornerList
	 * @Date : 2019. 4. 30.
	 * @Author : 이가영
	 * @param parameter
	 * @return
	 */
	@ApiOperation(value = "기획전 상세 상품 리스트 조회 (소제목 미적용)", httpMethod = "POST", protocols = "http", response = Map.class)
	@PostMapping(value = "v1.0/promotion/planning-display/detail/product/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPlanningDisplayProductList(Parameter<PrPlanningDisplay> parameter) throws Exception {

		Page<PdProductWrapper> page = planningDisplayService.getPrPlanningDisplayProductList(parameter);

		return UtilsREST.responseOk(parameter, page);
	}
}