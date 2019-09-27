package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpSearchWordHistory;
import kr.co.shop.web.display.repository.master.base.BaseDpSearchWordHistoryDao;

@Mapper
public interface DpSearchWordHistoryDao extends BaseDpSearchWordHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpSearchWordHistoryDao 클래스에 구현 되어있습니다.
	 * BaseDpSearchWordHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public DpSearchWordHistory selectByPrimaryKey(DpSearchWordHistory dpSearchWordHistory) throws Exception;

	public List<DpSearchWordHistory> selectPopularSearchWordList(DpSearchWordHistory dpSearchWordHistory)
			throws Exception;

}
