package kr.co.shop.web.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.board.model.master.BdFaq;
import kr.co.shop.web.board.service.BoardService;
import kr.co.shop.web.member.model.master.MbMember;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class BoardController extends BaseController {

	@Autowired
	BoardService boardService;

	@ApiOperation(value = "고객센터 메인 ", notes = "고객센터메인", httpMethod = "GET", protocols = "http", responseContainer = "Map")
	@GetMapping(value = { "/v1.0/board/customer/read-customer-main" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readBoardMain(Parameter<BdFaq> parameter) throws Exception {
		Map<String, Object> boardMap = boardService.getBoardMain();
		return UtilsREST.responseOk(parameter, boardMap);
	}

	/**
	 * @Desc : 공통 회원 정보 조회
	 * @Method Name : readMemberInfo
	 * @Date : 2019. 6. 20.
	 * @Author : 고웅환
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "공통 회원정보 조회", notes = "회원정보를 조회한다.", httpMethod = "GET", protocols = "http", response = MbMember.class, responseContainer = "Map")
	@GetMapping(value = { "/v1.0/board/common/member-info" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readMemberInfo(Parameter<MbMember> parameter) throws Exception {
		Map<String, Object> map = boardService.getMemberInfo(parameter.get());

		return UtilsREST.responseOk(parameter, map);
	}
}