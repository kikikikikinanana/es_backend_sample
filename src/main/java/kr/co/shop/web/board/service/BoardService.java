package kr.co.shop.web.board.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.service.MemberService;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {

	@Autowired
	CommonCodeService codeService;

	@Autowired
	BdNoticeService noticeService;

	@Autowired
	private MemberService memberService;

	public Map<String, Object> getBoardMain() throws Exception {
		Map<String, Object> boardMap = new HashMap<String, Object>();

		SyCodeDetail syCodeDetail = new SyCodeDetail();
		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_GBN_CODE_INQUIRY);

		boardMap.put("cnslTypeCode", codeService.getUseCodeByAddInfo1(syCodeDetail));
		boardMap.put("noticeList", noticeService.getNoticeCsMain());

		return boardMap;
	}

	/**
	 * @Desc : 공통 회원 정보 조회
	 * @Method Name : getMemberInfo
	 * @Date : 2019. 6. 20.
	 * @Author : 고웅환
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMemberInfo(MbMember parameter) throws Exception {
		Map<String, Object> map = new HashMap<>();
		parameter = memberService.selectMemberInfo(parameter);
		parameter.setMemberTextMasking();

		map.put("memberInfo", parameter);

		return map;
	}
}
