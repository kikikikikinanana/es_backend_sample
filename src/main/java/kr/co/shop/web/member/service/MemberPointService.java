package kr.co.shop.web.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.interfaces.module.member.MembershipPointService;
import kr.co.shop.interfaces.module.member.model.PrivateReport;
import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.model.master.MbMemberPoint;
import kr.co.shop.web.member.repository.master.MbMemberPointDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberPointService {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MembershipPointService membershipPointService;

	@Autowired
	private MbMemberPointDao mbMemberPointDao;

	/**
	 * @Desc : 가용포인트 조회
	 * @Method Name : getMemberPointInfo
	 * @Date : 2019. 4. 30.
	 * @Author : 3TOP
	 * @param memberNo
	 * @throws Exception
	 */
	public PrivateReport getMemberPointInfo(String memberNo) throws Exception {
		String safeKey = "";
		// 유저정보 조회
		MbMember member = new MbMember();
		member.setMemberNo(memberNo);
		member = memberService.selectMemberInfo(member);

		// 가용포인트 인터페이스 조회[memA910a]
		safeKey = member.getSafeKey();
		return membershipPointService.getPrivateReportBySafeKey(safeKey);
	}

	/**
	 * @Desc : 포인트 적립
	 * @Method Name : setSaveMemberPoint
	 * @Date : 2019. 6. 21.
	 * @Author : 3TOP
	 * @param memberNo
	 * @param changeAmount
	 * @throws Exception
	 */
	public void setSaveMemberPoint(String memberNo, int changeAmount) throws Exception {
		registUserPointByUser(memberNo, changeAmount);
	}

	/**
	 * @Desc : 포인트 차감
	 * @Method Name : setUseMemberPoint
	 * @Date : 2019. 6. 21.
	 * @Author : 3TOP
	 * @param memberNo
	 * @param changeAmount
	 * @throws Exception
	 */
	public void setUseMemberPoint(String memberNo, int changeAmount) throws Exception {
		changeAmount = changeAmount * -1;
		registUserPointByUser(memberNo, changeAmount);
	}

	/**
	 * @Desc : 기타적립금 적립
	 * @Method Name : setEtcSaveMemberPoint
	 * @Date : 2019. 6. 21.
	 * @Author : 3TOP
	 * @param memberNo
	 * @param changeAmount
	 * @throws Exception
	 */
	public void setEtcSaveMemberPoint(String memberNo, int changeAmount, String etcCode) throws Exception {
		etcProcessUserPointByUser(memberNo, changeAmount, etcCode);
	}

	/**
	 * @Desc : 기타적립금 차감
	 * @Method Name : setEtcUseMemberPoint
	 * @Date : 2019. 6. 21.
	 * @Author : 3TOP
	 * @param memberNo
	 * @param changeAmount
	 * @throws Exception
	 */
	public void setEtcUseMemberPoint(String memberNo, int changeAmount, String etcCode) throws Exception {
		changeAmount = changeAmount * -1;
		etcProcessUserPointByUser(memberNo, changeAmount, etcCode);
	}

	/**
	 * @Desc : 포인트 적립/차감
	 * @Method Name : registUserPointByUser
	 * @Date : 2019. 6. 20.
	 * @Author : 3TOP
	 * @param safeKey
	 * @param changeAmount
	 * @param type
	 */
	private void registUserPointByUser(String memberNo, int changeAmount) throws Exception {
		String safeKey = validateCheckMemberInfo(memberNo);
		validateCheckPoint(changeAmount);

		// TODO : 이력 테이블 기록

		// 인터페이스 호출 : 포인트 강제 적립/차감 [memA940a]
		membershipPointService.registUserPointByUser(safeKey, changeAmount);
	}

	/**
	 * @Desc : 기타적립금 적립/차감
	 * @Method Name : registUserPointByUser
	 * @Date : 2019. 6. 20.
	 * @Author : 3TOP
	 * @param safeKey
	 * @param changeAmount
	 * @param type
	 */
	private void etcProcessUserPointByUser(String memberNo, int changeAmount, String etcCode) throws Exception {
		String safeKey = validateCheckMemberInfo(memberNo);
		validateCheckPoint(changeAmount);

		// TODO : 이력 테이블 기록

		// 인터페이스 호출 : 기타적립금 적립차감 [memA870a]
		membershipPointService.updateEtcProcessUserPointByUser(safeKey, changeAmount, etcCode);
	}

	/**
	 * @Desc : 포인트 사용
	 * @Method Name : setUseBuyPoint
	 * @Date : 2019. 6. 24.
	 * @Author : 이동엽
	 * @param memberNo
	 * @param point
	 * @param eventpoint
	 * @throws Exception
	 */
	public void setUseBuyPoint(MbMemberPoint params) throws Exception {
		String safeKey = validateCheckMemberInfo(params.getMemberNo());
		int point = 0;
		int eventPoint = 0;

		if (params.getPoint() > 0) {
			point = params.getPoint() * -1;
		}

		if (params.getEventPoint() > 0) {
			eventPoint = params.getEventPoint() * -1;
		}

		// 이력 등록

		// 인터페이스 호출 : 포인트 적립 혹은 차감 [memB290a]
		if (!membershipPointService.updatePointForMembershipHandler(safeKey, point, Const.POINT_TYPE_USE, eventPoint)) {
			throw new Exception("데이터를 조회하는데 실패하였습니다. 잠시 후 다시 시도해주시거나 고객센터(1588-9667)로 연락주시기 바랍니다.");
		}
	}

	/**
	 * @Desc : 포인트 사용 취소
	 * @Method Name : setCancelBuyPoint
	 * @Date : 2019. 6. 24.
	 * @Author : 이동엽
	 * @param memberNo
	 * @param point
	 * @param eventpoint
	 * @throws Exception
	 */
	public void setCancelBuyPoint(MbMemberPoint params) throws Exception {
		PrivateReport privateReport = getMemberPointInfo(params.getMemberNo());
		int point = 0;
		int eventPoint = 0;

		// 가용포인트 조회
		if (privateReport == null) {
			throw new Exception("회원 포인트 데이터 연동 조회에 실패하였습니다.");
		}

		// 이벤트 만료일을 체크하여 이벤트 포인트 설정
		int validDate = privateReport != null ? privateReport.getEventValidateDate() : -1;
		if (validDate < 0) {
			eventPoint = 0;
		}

		// 유저 정보 체크
		String safeKey = validateCheckMemberInfo(params.getMemberNo());

		// 이력 등록

		// 인터페이스 호출 : 포인트 적립 혹은 차감 [memB290a]
		if (!membershipPointService.updatePointForMembershipHandler(safeKey, point, Const.POINT_TYPE_CANCEL,
				eventPoint)) {
			throw new Exception("데이터를 조회하는데 실패하였습니다. 잠시 후 다시 시도해주시거나 고객센터(1588-9667)로 연락주시기 바랍니다.");
		}
	}

	/**
	 * @Desc : 환수 포인트 조회
	 * @Method Name : getClawbackPoint
	 * @Date : 2019. 6. 25.
	 * @Author : 이동엽
	 * @param memberNo
	 * @param orderNo
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public int getClawbackPoint(String memberNo, String orderNo, int price) throws Exception {
		// 환수포인트 조회의 경우 유저정보가 없어도 값을 리턴해야 하므로 하위 작성된 유저 체크로직을 타지 않고 별도로 회원 정보를 조회
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = memberService.selectMemberInfo(mbMember);

		if (mbMember != null && UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_MEMBERSHIP)) {
			// 인터페이스 호출 : 환수 포인트 조회[memB310a]
			return membershipPointService.getClawbackPoint(mbMember.getSafeKey(), orderNo, price);
		} else {
			return 0;
		}
	}

	/**
	 * @Desc : 포인트 환수
	 * @Method Name : saveClawbackPoint
	 * @Date : 2019. 6. 25.
	 * @Author : 이동엽
	 * @param memberNo
	 * @param dlvyNo
	 * @param clawbackPoint
	 * @return
	 * @throws Exception
	 */
	public boolean saveClawbackPoint(String memberNo, String dlvyNo, int clawbackPoint) throws Exception {
		// 환수포인트 조회의 경우 유저정보가 없어도 값을 리턴해야 하므로 하위 작성된 유저 체크로직을 타지 않고 별도로 회원 정보를 조회
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = memberService.selectMemberInfo(mbMember);

		if (UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_MEMBERSHIP)) {
			// 인터페이스 호출 : 포인트 환수[memB320a]
			return membershipPointService.updateClawbackPoint(mbMember.getSafeKey(), dlvyNo, clawbackPoint);
		} else {
			return false;
		}
	}

	/**
	 * @Desc : 유저 정보 체크
	 * @Method Name : validateCheckMemberInfo
	 * @Date : 2019. 6. 21.
	 * @Author : 이동엽
	 * @param memberNo
	 * @param changeAmount
	 * @return
	 * @throws Exception
	 */
	public String validateCheckMemberInfo(String memberNo) throws Exception {
		MbMember mbMember = new MbMember();
		mbMember.setMemberNo(memberNo);
		mbMember = memberService.selectMemberInfo(mbMember);

		if (mbMember == null) {
			throw new Exception("유저 정보가 존재하지 않습니다.");
		} else if (!UtilsText.equals(mbMember.getMemberTypeCode(), CommonCode.MEMBER_TYPE_MEMBERSHIP)) {
			throw new Exception("통합 맴버십 회원이 아닙니다.");
		}

		return mbMember.getSafeKey();
	}

	/**
	 * @Desc : 포인트 체크
	 * @Method Name : validateCheckPoint
	 * @Date : 2019. 6. 24.
	 * @Author : 이동엽
	 * @param changeAmount
	 * @throws Exception
	 */
	public void validateCheckPoint(int changeAmount) throws Exception {
		// 변경 포인트 체크
		if (changeAmount == 0) {
			throw new Exception("변경 포인트가 0 일수는 없습니다.");
		}
	}

	/**
	 * @Desc : 포인트 이력 등록
	 * @Method Name : insertMemberPoint
	 * @Date : 2019. 6. 12.
	 * @Author : 이동엽
	 * @param mbMemberPoint
	 * @throws Exception
	 */
	public void insertMemberPoint(MbMemberPoint mbMemberPoint) throws Exception {
		mbMemberPointDao.insertMemberPoint(mbMemberPoint);
	}
}
