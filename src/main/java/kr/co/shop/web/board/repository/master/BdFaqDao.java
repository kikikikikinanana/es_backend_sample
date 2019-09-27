package kr.co.shop.web.board.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.board.model.master.BdFaq;
import kr.co.shop.web.board.repository.master.base.BaseBdFaqDao;

@Mapper
public interface BdFaqDao extends BaseBdFaqDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdFaqDao 클래스에 구현 되어있습니다. BaseBdFaqDao는 절대
	 * 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdFaq selectByPrimaryKey(BdFaq bdFaq) throws Exception;

	/**
	 * @Desc : 파라메터 있을시에 조회결과 개수 조회
	 * @Method Name : selectFaqMainCount
	 * @Date : 2019. 4. 5.
	 * @Author : 신인철
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectFaqByCnslTypeCount(Pageable<BdFaq, BdFaq> pageable) throws Exception;

	/**
	 * @Desc : FAQ 메인페이지 호출
	 * @Method Name : selectTop10List
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public List<BdFaq> selectTop10List() throws Exception;

	/**
	 * @Desc : FAQ 소분류 메뉴 클릭시 리스트 조회
	 * @Method Name : selectFaqByCnslTypeDtlCode
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public List<BdFaq> selectFaqByCnslDtlCode(Pageable<BdFaq, BdFaq> pageable) throws Exception;

	/**
	 * @Desc : FAQ 검색
	 * @Method Name : selectFaqBySearchValue
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public List<BdFaq> selectFaqBySearchValue(Pageable<BdFaq, BdFaq> pageable) throws Exception;

	/**
	 * @Desc : 검색 카운트
	 * @Method Name : selectBySearchValueCount
	 * @Date : 2019. 3. 25.
	 * @Author : 신인철
	 * @param bdFaq
	 * @return
	 * @throws Exception
	 */
	public int selectBySearchValueCount(Pageable<BdFaq, BdFaq> pageable) throws Exception;

}
