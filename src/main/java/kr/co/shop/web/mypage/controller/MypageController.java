package kr.co.shop.web.mypage.controller;

import java.util.HashMap;
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
import kr.co.shop.common.paging.Page;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.member.model.master.MbMemberEasyLogin;
import kr.co.shop.web.member.model.master.MbMemberExpostSavePoint;
import kr.co.shop.web.member.model.master.MbMemberInterestBrand;
import kr.co.shop.web.member.model.master.MbMemberInterestStore;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.mypage.service.MypageService;
import kr.co.shop.web.mypage.vo.MypageVO;
import kr.co.shop.web.mypage.vo.PointVO;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class MypageController extends BaseController {

	@Autowired
	MypageService mypageService;

	@Autowired
	MemberService memberService;

	@Autowired
	private CommonCodeService commonCodeService;

	@ApiOperation(value = "회원기본정보영역 조회(쿠폰 갯수, 포인트)", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "safeKey", value = "안심키", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "memberTypeCode", value = "회원유형코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "mbshpGradeCode", value = "멤버십등급코드", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "empLoginYn", value = "임직원로그인여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/getMemberBasicInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberBasicInfo(Parameter<MbMember> parameter) throws Exception {

		MbMember param = parameter.get();
		Map<String, Object> map = mypageService.getMemberBasicInfo(param);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "회원정보 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "v1.0/mypage/selectMemberInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectMemberInfo(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		MbMember result = memberService.selectMemberInfo(params);
		return UtilsREST.responseOk(parameter, result);
	}

	@ApiOperation(value = "회원정보 수정", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/mypage/updateMemberInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMemberInfo(Parameter<MbMember> parameter) throws Exception {
		MbMember params = parameter.get();
		memberService.updateMemberInfo(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "회원포인트정보 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "safeKey", value = "안심키", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/getPointInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPointInfo(Parameter<MypageVO> parameter) throws Exception {

		MypageVO param = parameter.get();
		Map<String, Object> map = mypageService.getPointInfo(param);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "포인트 비밀번호 변경", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pointPassword", value = "포인트비밀번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pswdText", value = "온라인비밀번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/updateCardPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCardPassword(Parameter<PointVO> parameter) throws Exception {

		PointVO param = parameter.get();
		Map<String, Object> map = mypageService.updateCardPassword(param);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "비밀번호 유효성체크", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pswdText", value = "비밀번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/getPasswordValidation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPasswordValidation(Parameter<MypageVO> parameter) throws Exception {

		MypageVO param = parameter.get();
		Map<String, Object> map = mypageService.getPasswordValidation(param);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "멤버십 포인트 사후 적립 신청", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "onlnBuyYn", value = "온라인구매여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "buyNoText", value = "구매번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "buyYmd", value = "구매일자", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pymntAmt", value = "결제금액", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "storeNo", value = "매장번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "posNoText", value = "POS번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dealNoText", value = "거래번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "storeName", value = "매장명", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "crtfcNoText", value = "인증번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "adminRgstYn", value = "관리자등록여부", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/createLatedSavePoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createLatedSavePoint(Parameter<MbMemberExpostSavePoint> parameter) throws Exception {

		MbMemberExpostSavePoint params = parameter.get();
		Map<String, Object> map = mypageService.insertLatedSavePoint(params);

		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "배송주소록 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrSeq", value = "배송지순번", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rowsPerPage", value = "한 페이지당 로우 겟수", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "pageNum", value = "페이지 번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/selectMemberDeliveryAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> selectMemberDeliveryAddress(Parameter<MbMemberDeliveryAddress> parameter)
			throws Exception {

		Pageable<MbMemberDeliveryAddress, MbMemberDeliveryAddress> pageable = new Pageable<>(parameter);
		pageable.setRowsPerPage(parameter.get().getRowsPerPage());
		pageable.setPageNum(parameter.get().getPageNum());
		Page<MbMemberDeliveryAddress> page = memberService.selectMemberDeliveryAddress(pageable);

		return UtilsREST.responseOk(parameter, page.getGrid());
	}

	@ApiOperation(value = "기본배송지 설정", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrSeq", value = "배송지순번", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/setDefaultDeliveryAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setDefaultDeliveryAddress(Parameter<MbMemberDeliveryAddress> parameter) throws Exception {
		MbMemberDeliveryAddress params = parameter.get();
		Map<String, Object> map = mypageService.setDefaultDeliveryAddress(params);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "배송주소록 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrSeq", value = "배송지순번", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "addDlvyAddrYn", value = "추가배송지여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dfltDlvyAddrYn", value = "기본배송지여부", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrName", value = "배송지명", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rcvrName", value = "받는사람명", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "hdphnNoText", value = "핸드폰번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "postCodeText", value = "우편번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "postAddrText", value = "우편주소", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dtlAddrText", value = "상세주소", required = false, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/insertMemberDeliveryAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertDlvyAddr(Parameter<MbMemberDeliveryAddress> parameter) throws Exception {

		MbMemberDeliveryAddress params = parameter.get();

		Map<String, Object> map = memberService.insertMemberDeliveryAddress(params);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "배송주소록 수정", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrSeq", value = "배송지순번", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "addDlvyAddrYn", value = "추가배송지여부", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dfltDlvyAddrYn", value = "기본배송지여부", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrName", value = "배송지명", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rcvrName", value = "받는사람명", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "hdphnNoText", value = "핸드폰번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "postCodeText", value = "우편번호", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "postAddrText", value = "우편주소", required = false, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dtlAddrText", value = "상세주소", required = false, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/updateMemberDeliveryAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMemberDeliveryAddress(Parameter<MbMemberDeliveryAddress> parameter)
			throws Exception {

		MbMemberDeliveryAddress params = parameter.get();

		Map<String, Object> map = memberService.updateMemberDeliveryAddress(params);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "배송주소록 삭제", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "dlvyAddrSeq", value = "배송지순번", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/deleteMemberDeliveryAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteMemberDeliveryAddress(Parameter<MbMemberDeliveryAddress[]> parameter)
			throws Exception {

		MbMemberDeliveryAddress[] params = parameter.get();
		memberService.deleteMemberDeliveryAddress(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "단골매장조회", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "query") })
	@GetMapping(value = "/v1.0/mypage/getCustomStore", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomStore(Parameter<MypageVO> parameter) throws Exception {

		MypageVO params = parameter.get();
		Map<String, Object> map = memberService.getCustomStore(params);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "단골매장추가", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "storeNo", value = "매장번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "rgsterNo", value = "등록자번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/updateCustomStore", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCustomStore(Parameter<MbMemberInterestStore> parameter) throws Exception {

		MbMemberInterestStore params = parameter.get();
		memberService.updateCustomStore(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "단골매장삭제", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "siteNo", value = "사이트번호", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/deleteCustomStore", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteCustomStore(Parameter<MbMemberInterestStore> parameter) throws Exception {

		MbMemberInterestStore params = parameter.get();
		memberService.deleteCustomStore(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "관심 브랜드조회", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "siteNo", value = "회원번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "pageNum", value = "페이지번호", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "rowsPerPage", value = "페이지row수", required = false, dataType = "string", paramType = "query") })
	@GetMapping(value = "/v1.0/mypage/getMyInterestedBrandList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyInterestedBrandList(Parameter<MbMemberInterestBrand> parameter) throws Exception {

		Pageable<MbMemberInterestBrand, MbMemberInterestBrand> pageable = new Pageable<>(parameter);
		Page<MbMemberInterestBrand> page = mypageService.getMyInterestedBrandList(pageable);

		return UtilsREST.responseOk(parameter, page.getGrid());
	}

	@ApiOperation(value = "관심브랜드삭제", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "delGubun", value = "삭제구분(A:전체, S:해당브랜드번호)", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "brandNo", value = "브랜드번호", required = false, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/deleteMyInterestedBrand", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteMyInterestedBrand(Parameter<MbMemberInterestBrand> parameter) throws Exception {

		MbMemberInterestBrand params = parameter.get();
		if (UtilsText.equals(params.getDelGubun(), "S")) {
			params.validate();
		}
		mypageService.deleteMyInterestedBrand(params);
		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "간편로그인 연결설정조회", httpMethod = "GET", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query") })
	@GetMapping(value = "/v1.0/mypage/getMemberEasyLoginInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberEasyLoginInfo(Parameter<MypageVO> parameter) throws Exception {

		MypageVO params = parameter.get();
		Map<String, Object> map = mypageService.getMemberEasyLoginInfo(params);
		return UtilsREST.responseOk(parameter, map);
	}

	@ApiOperation(value = "SNS 계정정보 조회", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "snsGbnCode", value = "SNS구분", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "/v1.0/mypage/getMemberEasyLoginAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberEasyLoginAccount(Parameter<MbMemberEasyLogin> parameter) throws Exception {
		MbMemberEasyLogin params = parameter.get();
		MbMemberEasyLogin result = mypageService.getMemberEasyLoginAccount(params);

		return UtilsREST.responseOk(parameter, result);
	}

	@ApiOperation(value = "간편회원 정보 업데이트", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "snsGbnCode", value = "SNS구분", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "addInfo1", value = "추가정보1(accessToken)", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "/v1.0/mypage/updateEasyLoginInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateEasyLoginInfo(Parameter<MbMemberEasyLogin> parameter) throws Exception {
		MbMemberEasyLogin params = parameter.get();
		mypageService.updateEasyLoginInfo(params);

		return UtilsREST.responseOk(parameter);
	}

	@ApiOperation(value = "간편회원 정보 삭제", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "snsGbnCode", value = "SNS구분", required = true, dataType = "string", paramType = "query") })
	@PostMapping(value = "/v1.0/mypage/deleteEasyLoginInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteEasyLoginInfo(Parameter<MbMemberEasyLogin> parameter) throws Exception {
		MbMemberEasyLogin params = parameter.get();
		mypageService.deleteEasyLoginInfo(params);

		return UtilsREST.responseOk(parameter);
	}

	/**
	 * 
	 * @Desc : 회원탈퇴 탈퇴사유 코드를 조회한다.
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 4. 15.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "회원탈퇴 사유 코드 조회", httpMethod = "GET", protocols = "http")
	@GetMapping(value = "/v1.0/mypage/getLeaveRsnCodeList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getLeaveRsnCodeList(Parameter<?> parameter) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(CommonCode.LEAVE_RSN_CODE, commonCodeService.getUseCode(CommonCode.LEAVE_RSN_CODE));

		return UtilsREST.responseOk(parameter, map);
	}

	/**
	 * 
	 * @Desc : 회원탈퇴 탈퇴사유 코드를 조회한다.
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 4. 15.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "공통 코드 조회", httpMethod = "GET", protocols = "http")
	@GetMapping(value = "/v1.0/mypage/getCommonCodeList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCommonCodeList(Parameter<?> parameter) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("CODE", commonCodeService.getUseCode(parameter.getString("codeField")));

		return UtilsREST.responseOk(parameter, map);
	}

	/**
	 * 
	 * @Desc : 계좌를 인증한다.
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 4. 15.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "계좌인증", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "acntNoText", value = "계좌번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "bankCode", value = "은행코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "acntHldrName", value = "예금주", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/account-auth", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> accountAuth(Parameter<MbMember> parameter) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		MbMember mbMember = parameter.get();

		// 공통 프로젝트의 계좌인증에서 인증을 처리하고 결과를 리턴받음
		map.put("result", mypageService.getAccountAuthProcess(mbMember));

		return UtilsREST.responseOk(parameter, map);
	}

	/**
	 * 
	 * @Desc : 환불계좌를 등록한다.
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 4. 15.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "환불계좌 등록", httpMethod = "POST", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberNo", value = "회원번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "acntNoText", value = "계좌번호", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "bankCode", value = "은행코드", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "acntHldrName", value = "예금주", required = true, dataType = "string", paramType = "body") })
	@PostMapping(value = "/v1.0/mypage/refund-acnt-update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refundAcntUpdate(Parameter<MbMember> parameter) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		MbMember mbMember = parameter.get();

		// 공통 프로젝트의 계좌인증에서 인증을 처리하고 결과를 리턴받음
		map.put("result", mypageService.setRefundAcntUpdate(mbMember));

		return UtilsREST.responseOk(parameter, map);
	}

	/**
	 * 
	 * @Desc : 회원탈퇴
	 * @Method Name : setLeaveProcess
	 * @Date : 2019. 4. 16.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "회원탈퇴", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/mypage/setLeaveProcess", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setLeaveProcess(Parameter<MbMember> parameter) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		MbMember mbMember = parameter.get();

		return UtilsREST.responseOk(parameter, memberService.setLeaveProcess(mbMember));
	}

	/**
	 * 
	 * @Desc : 주문개수, 주문건수에 따른 회원 등급 조회
	 * @Method Name : getMemberGrade
	 * @Date : 2019. 3. 21.
	 * @Author : choi
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "주문개수, 주문건수에 따른 회원 등급 조회", httpMethod = "POST", protocols = "http")
	@PostMapping(value = "/v1.0/mypage/getMemberGrade", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMemberGrade(Parameter<?> parameter) throws Exception {
		Map<String, Object> map = mypageService.getMemberGrade();

		return UtilsREST.responseOk(parameter, map);
	}

}