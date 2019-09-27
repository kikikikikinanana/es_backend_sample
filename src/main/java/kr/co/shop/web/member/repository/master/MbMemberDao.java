package kr.co.shop.web.member.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberDao;

@Mapper
public interface MbMemberDao extends BaseMbMemberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 * ※ 중 요 ※
	 * 
	 * sqlSession 은 다음 메소드를 이용 하여 호출 합니다. 본인이 쓰고 있는 sqlSession(DB 호출)이 어떤 것인지 명확 하게
	 * 알아 보기 위함입니다.
	 * 
	 * - getSqlSessionMaster()
	 */

	public MbMember selectByPrimaryKey(MbMember mbMember) throws Exception;

	/***
	 * login_id 값으로 회원 정보를 조회 한다.
	 * 
	 * @param mbMember
	 * @return MbMember
	 * @throws Exception
	 */
	public MbMember selectByLoginId(MbMember mbMember) throws Exception;

	/**
	 * @Desc : 회원 리스트를 조회
	 * @Method Name : selectMemberList
	 * @Date : 2019. 3. 21.
	 * @Author : 이동엽
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public List<MbMember> selectMemberList(MbMember mbMember) throws Exception;

	/**
	 * @Desc : 회원 데이터를 조회
	 * @Method Name : selectMemberInfo
	 * @Date : 2019. 3. 21.
	 * @Author : 이동엽
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public MbMember selectMemberInfo(MbMember mbMember) throws Exception;

	/**
	 * @Desc : 아이디 찾기 정보 조회
	 * @Method Name : selectLoginMemberInfoList
	 * @Date : 2019. 5. 15.
	 * @Author : 이동엽
	 * @param mbMember
	 * @return
	 * @throws Exception
	 */
	public List<MbMember> selectIdSerarchInfoList(MbMember mbMember) throws Exception;

	/**
	 * @Desc : 회원 로그인 실패카운터 업데이트
	 * @Method Name : updateLoginFailCount
	 * @Date : 2019. 3. 21.
	 * @Author : 이동엽
	 * @param mbMember
	 * @throws Exception
	 */
	public void updateLoginFailCount(MbMember mbMember) throws Exception;

	/**
	 * @Desc : getSafeKey(안심키) 조회
	 * @Method Name : getSafeKey
	 * @Date : 2019. 3. 25.
	 * @Author : 유성민
	 * @param memberNo
	 * @return
	 */
	public String getSafeKey(String memberNo) throws Exception;

	/**
	 * @Desc : 회원탈퇴 업데이트
	 * @Method Name : updateLeave
	 * @Date : 2019. 4. 17.
	 * @Author : 최경호
	 * @param mbMember
	 * @return
	 */
	public int updateLeave(MbMember mbMember) throws Exception;

	/**
	 * @Desc : 환불계좌 업데이트
	 * @Method Name : updateRefundAcnt
	 * @Date : 2019. 4. 17.
	 * @Author : 최경호
	 * @param mbMember
	 * @return
	 */
	public int updateRefundAcnt(MbMember mbMember);

	/**
	 * @Desc : 회원 시퀀스 조회
	 * @Method Name : selectMemberSequence
	 * @Date : 2019. 3. 26.
	 * @Author : 이동엽
	 * @return
	 * @throws Exception
	 */
	public String selectMemberSequence() throws Exception;

	public void insertMemberInfo(MbMember mbMember) throws Exception;

	public MbMember selectLeaveMemberInfo(MbMember mbMember) throws Exception;

	/**
	 * @Desc : 회원 결제수단 수정
	 * @Method Name : updatePaymentMeansCode
	 * @Date : 2019. 6. 18.
	 * @Author : 3TOP
	 * @param mbMember
	 * @throws Exception
	 */
	public void updatePaymentMeansCode(MbMember mbMember) throws Exception;

}
