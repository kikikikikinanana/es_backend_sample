package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpCategoryCorner;
import kr.co.shop.web.display.repository.master.base.BaseDpCategoryCornerDao;

@Mapper
public interface DpCategoryCornerDao extends BaseDpCategoryCornerDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpCategoryCornerDao 클래스에 구현 되어있습니다.
	 * BaseDpCategoryCornerDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpCategoryCorner selectByPrimaryKey(DpCategoryCorner dpCategoryCorner) throws Exception;

	/**
	 * 전시 카테고리 코너 리스트 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpCategoryCornerList
	 * @Date : 2019. 5. 7.
	 * @Author : SANTA
	 * @param dpCategoryCorner
	 * @return
	 * @throws Exception
	 */
	public List<DpCategoryCorner> selectDpCategoryCornerList(DpCategoryCorner dpCategoryCorner) throws Exception;
}
