package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpSearchWord;
import kr.co.shop.web.display.repository.master.base.BaseDpSearchWordDao;

@Mapper
public interface DpSearchWordDao extends BaseDpSearchWordDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpSearchWordDao 클래스에 구현 되어있습니다.
	 * BaseDpSearchWordDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpSearchWord selectByPrimaryKey(DpSearchWord dpSearchWord) throws Exception;

	/**
	 * @Desc : 검색창 검색어 리스트 조회
	 * @Method Name : selectSearchWordList
	 * @Date : 2019. 5. 15.
	 * @Author : 이가영
	 * @param dpSearchWord
	 * @return
	 */
	public List<DpSearchWord> selectSearchWordList(DpSearchWord dpSearchWord) throws Exception;

}
