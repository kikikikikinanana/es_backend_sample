package kr.co.shop.web.board.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.board.model.master.BdMemberCounsel;
import kr.co.shop.web.board.repository.master.base.BaseBdMemberCounselDao;

@Mapper
public interface BdMemberCounselDao extends BaseBdMemberCounselDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdMemberCounselDao 클래스에 구현 되어있습니다.
	 * BaseBdMemberCounselDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdMemberCounsel selectByPrimaryKey(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 나의 상담내역 리스트 카운트
	 * @Method Name : selectInqryCount
	 * @Date : 2019. 4. 9.
	 * @Author : 신인철
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectInqryCount(Pageable<BdMemberCounsel, BdMemberCounsel> pageable) throws Exception;

	/**
	 * @Desc : 나의 상담내역 리스트
	 * @Method Name : selectInqryList
	 * @Date : 2019. 4. 9.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public List<BdMemberCounsel> selectInqryList(Pageable<BdMemberCounsel, BdMemberCounsel> pageable) throws Exception;

	/**
	 * @Desc : 1:1 문의 상세 조회
	 * @Method Name : selectInqryDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public BdMemberCounsel selectInqryDetail(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 1:1문의 등록
	 * @Method Name : insertInqryDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void insertInqryDetail(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 1:1 문의 수정
	 * @Method Name : insertInqryDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void updateInqryDetail(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 고객의소리 등록
	 * @Method Name : insertVocDetail
	 * @Date : 2019. 3. 29.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void insertVocDetail(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 상담 삭제시에 관리자 메모 삭제
	 * @Method Name : deleteAddminMemo
	 * @Date : 2019. 5. 31.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void deleteAdminMemo(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 고객의소리 수정
	 * @Method Name : updateVocDetail
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void updateVocDetail(BdMemberCounsel bdMemberCounsel) throws Exception;
}
