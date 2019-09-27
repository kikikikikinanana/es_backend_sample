package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.repository.master.base.BaseDpCategoryDao;

@Mapper
public interface DpCategoryDao extends BaseDpCategoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpCategoryDao 클래스에 구현 되어있습니다.
	 * BaseDpCategoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpCategory selectByPrimaryKey(DpCategory dpCategory) throws Exception;

	public List<DpCategory> selectDpCategoryList(DpCategory dpCategory) throws Exception;

	public DpCategory selectDpCategory(DpCategory dpCategory) throws Exception;

	public List<DpCategory> selectDpCategorySaleList(DpCategory dpCategory) throws Exception;

	public List<DpCategory> selectDpCategoryNameList(DpCategory dpCategory) throws Exception;
}
