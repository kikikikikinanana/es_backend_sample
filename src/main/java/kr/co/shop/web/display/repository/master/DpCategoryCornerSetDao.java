package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpCategoryCornerSet;
import kr.co.shop.web.display.repository.master.base.BaseDpCategoryCornerSetDao;

@Mapper
public interface DpCategoryCornerSetDao extends BaseDpCategoryCornerSetDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpCategoryCornerSetDao 클래스에 구현 되어있습니다.
	 * BaseDpCategoryCornerSetDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public DpCategoryCornerSet selectByPrimaryKey(DpCategoryCornerSet dpCategoryCornerSet) throws Exception;

	/**
	 * 전시 카테고리 코너 셋 리스트 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpCategoryCornerSetList
	 * @Date : 2019. 5. 7.
	 * @Author : SANTA
	 * @param dpCategoryCornerSet
	 * @return
	 * @throws Exception
	 */
	public List<DpCategoryCornerSet> selectDpCategoryCornerSetList(DpCategoryCornerSet dpCategoryCornerSet)
			throws Exception;
}
