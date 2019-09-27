package kr.co.shop.web.board.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.board.model.master.BdNotice;
import kr.co.shop.web.board.repository.master.base.BaseBdNoticeDao;

@Mapper
public interface BdNoticeDao extends BaseBdNoticeDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdNoticeDao 클래스에 구현 되어있습니다.
	 * BaseBdNoticeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdNotice selectByPrimaryKey(BdNotice bdNotice) throws Exception;

	/**
	 * @Desc : 공지사항 조회 결과 개수
	 * @Method Name : selectNoticeCount
	 * @Date : 2019. 4. 5.
	 * @Author : 3TOP_118
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectNoticeCount(Pageable<BdNotice, BdNotice> pageable) throws Exception;

	/**
	 * @Desc : 공지사항 메인페이지 리스트
	 * @Method Name : selectNoticeList
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public List<BdNotice> selectNoticeList(Pageable<BdNotice, BdNotice> pageable) throws Exception;

	/**
	 * @Desc : 공지사항 상세보기
	 * @Method Name : selectNoticeDetail
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @param bdNotice
	 * @return
	 * @throws Exception
	 */
	public BdNotice selectNoticeDetail(BdNotice bdNotice) throws Exception;

	/**
	 * @Desc : 공지사항 상세 호출시 이전 글 보기
	 * @Method Name : selectPrevNoticeDetail
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @param bdNotice
	 * @return
	 * @throws Exception
	 */
	public BdNotice selectPrevNoticeDetail(BdNotice bdNotice) throws Exception;

	/**
	 * @Desc : 공지사항 상세 호출시 다음 글 보기
	 * @Method Name : selectNextNoticeDetail
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @param bdNotice
	 * @return
	 * @throws Exception
	 */
	public BdNotice selectNextNoticeDetail(BdNotice bdNotice) throws Exception;

	/**
	 * @Desc : 푸터 공지사항 노출
	 * @Method Name : selectFooterNotice
	 * @Date : 2019. 5. 2.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public List<BdNotice> selectFooterNotice() throws Exception;

	/**
	 * @Desc : MO 고객센터 메인 공지사항 노출
	 * @Method Name : selectNoticeCsMain
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public List<BdNotice> selectNoticeCsMain() throws Exception;

}
