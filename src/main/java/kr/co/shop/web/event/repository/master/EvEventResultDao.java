package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.event.model.master.EvEventResult;
import kr.co.shop.web.event.repository.master.base.BaseEvEventResultDao;

@Mapper
public interface EvEventResultDao extends BaseEvEventResultDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventResultDao 클래스에 구현 되어있습니다.
	 * BaseEvEventResultDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventResult selectByPrimaryKey(EvEventResult evEventResult) throws Exception;

	/**
	 * @Desc : 이벤트 결과 카운트
	 * @Method Name : selectEventResultCount
	 * @Date : 2019. 5. 10.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public int selectEventResultCount(Pageable<EvEventResult, EvEventResult> pageable) throws Exception;

	/**
	 * @Desc : 이벤트 결과 리스트
	 * @Method Name : selectEventResultList
	 * @Date : 2019. 5. 10.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<EvEventResult> selectEventResultList(Pageable<EvEventResult, EvEventResult> pageable) throws Exception;

	/**
	 * @Desc : 이벤트 결과 상세
	 * @Method Name : selectEventResult
	 * @Date : 2019. 5. 14.
	 * @Author : easyhun
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public EvEventResult selectEventResult(EvEventResult evEventResult) throws Exception;

}
