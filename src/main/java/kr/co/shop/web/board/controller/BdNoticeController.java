package kr.co.shop.web.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import kr.co.shop.web.board.model.master.BdNotice;
import kr.co.shop.web.board.service.BdNoticeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class BdNoticeController extends BaseController {

	@Autowired
	BdNoticeService noticeService;

	/**
	 * @Desc : 공지사항 메인페이지 호출
	 * @Method Name : readNoticeMain
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "공지사항 메인 ", notes = "공지사항 메인 호출", httpMethod = "GET", protocols = "http", response = BdNotice.class, responseContainer = "List")
	@PostMapping(value = { "/v1.0/board/notice/read-notice-main" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readNoticeMain(Parameter<BdNotice> parameter) throws Exception {
		Pageable<BdNotice, BdNotice> pageable = new Pageable<>(parameter);

		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());

		Map<String, Object> noticeMap = noticeService.getNoticeMain(pageable);

		return UtilsREST.responseOk(parameter, noticeMap);
	}

	/**
	 * @Desc : 공지사항 상세보기 호출
	 * @Method Name : readNoticeDetail
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "공지사항 상세보기 ", notes = "공지사항 상세보기 호출", httpMethod = "GET", protocols = "http", response = BdNotice.class, responseContainer = "Map")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "notcSeq", value = "공지사항 순번", required = true, dataType = "string", paramType = "body") })
	@GetMapping(value = { "/v1.0/board/notice/read-notice-detail" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readNoticeDetail(Parameter<BdNotice> parameter) throws Exception {
		Map<String, Object> noticeMap = noticeService.getNoticeDetail(parameter.get());

		return UtilsREST.responseOk(parameter, noticeMap);
	}

	/**
	 * @Desc : 푸터 공지사항 노출
	 * @Method Name : readFooterNotice
	 * @Date : 2019. 5. 2.
	 * @Author : 신인철
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "푸터 공지사항 노출 ", notes = "푸터 공지사항 노출", httpMethod = "GET", protocols = "http", response = BdNotice.class, responseContainer = "List")
	@GetMapping(value = { "/v1.0/board/notice/read-footer-notice" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readFooterNotice(Parameter<BdNotice> parameter) throws Exception {
		List<BdNotice> noticeList = noticeService.getFooterNotice();

		return UtilsREST.responseOk(parameter, noticeList);
	}

}