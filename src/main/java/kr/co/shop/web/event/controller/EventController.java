package kr.co.shop.web.event.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.event.model.master.EvEvent;
import kr.co.shop.web.event.model.master.EvEventAnswer;
import kr.co.shop.web.event.model.master.EvEventAttendanceCheckMember;
import kr.co.shop.web.event.model.master.EvEventJoinMember;
import kr.co.shop.web.event.model.master.EvEventResult;
import kr.co.shop.web.event.model.master.EvEventRouletteBenefit;
import kr.co.shop.web.event.model.master.EvEventTargetProduct;
import kr.co.shop.web.event.service.EventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class EventController extends BaseController {
	@Autowired
	EventService eventService;

	@ApiOperation(value = "이벤트 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "statusType", value = "상태타입", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventList(Parameter<EvEvent> parameter) throws Exception {
		Pageable<EvEvent, EvEvent> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = eventService.getEventList(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 상세 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = false, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEvent(Parameter<EvEvent> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.getEvent(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 결과 리스트 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "v1.0/promotion/event/result/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventResultList(Parameter<EvEventResult> parameter) throws Exception {
		Pageable<EvEventResult, EvEventResult> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = eventService.getEventResultList(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 결과 상세 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/result/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventResult(Parameter<EvEventResult> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.getEventResult(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "회원 참여 이벤트 리스트 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/member-event/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberEventList(Parameter<EvEvent> parameter) throws Exception {
		Pageable<EvEvent, EvEvent> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = eventService.getMemberEventList(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 회원 댓글 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/answer/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventAnswerList(Parameter<EvEventAnswer> parameter) throws Exception {
		Pageable<EvEventAnswer, EvEventAnswer> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(15);
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> resultMap = eventService.getEventAnswerList(pageable);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 참여 회원 건수", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/join/member/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventJoinMemberCount(Parameter<EvEventJoinMember> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.getEventJoinMemberCount(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 회원 댓글 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/answer/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertEventAnswer(Parameter<EvEventAnswer> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.insertEventAnswer(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 회원 댓글 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventAswrSeq", value = "이벤트 댓글 번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/answer/modify", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateEventAnswer(Parameter<EvEventAnswer> parameter) throws Exception {
		eventService.updateEventAnswer(parameter.get());

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "이벤트 회원 댓글 삭제", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventAswrSeq", value = "이벤트 댓글 번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/answer/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteEventAnswer(Parameter<EvEventAnswer> parameter) throws Exception {
		eventService.deleteEventAnswer(parameter.get());

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "이벤트 출석체크 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/attend/check/member/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertEventAttendanceCheckMember(Parameter<EvEventAttendanceCheckMember> parameter)
			throws Exception {
		Map<String, Object> resultMap = eventService.insertEventAttendanceCheckMember(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 룰렛 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/roulette/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertEventRoulette(Parameter<EvEventRouletteBenefit> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.insertEventRoulette(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "드로우 이벤트 배너", httpMethod = "GET", protocols = "http")
	@PostMapping(value = "v1.0/promotion/event/draw", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDrawTargetProduct(Parameter<EvEventTargetProduct> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.getDrawTargetProduct(parameter);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "드로우 이벤트 상품 리스트", httpMethod = "GET", protocols = "http")
	@PostMapping(value = "v1.0/promotion/event/draw/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDrawTargetProductList(Parameter<EvEventTargetProduct> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.getDrawTargetProductList(parameter);

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 참여 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/draw/info/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertEventJoinMember(Parameter<EvEventJoinMember> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.insertEventJoinMember(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "이벤트 참여 등록(이벤트 발행번호)", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/publ/number/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertEventPublNumber(Parameter<EvEventJoinMember> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.insertEventPublNumber(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "드로우 이벤트 참여 정보", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eventNo", value = "이벤트번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "prdtNo", value = "상품번호", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "v1.0/promotion/event/draw/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDrawInfo(Parameter<EvEventJoinMember> parameter) throws Exception {

		Map<String, Object> resultMap = eventService.getDrawInfo(parameter.get());

		return UtilsREST.responseOk(parameter, resultMap);
	}

	@ApiOperation(value = "메인 드로우 상품 리스트", httpMethod = "GET", protocols = "http")
	@PostMapping(value = "v1.0/promotion/main/draw/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMainDrawTargetProductList(Parameter<EvEventTargetProduct> parameter) throws Exception {
		Map<String, Object> resultMap = eventService.getMainDrawTargetProductList(parameter);

		return UtilsREST.responseOk(parameter, resultMap);
	}
}