package kr.co.shop.web.display.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpCategory;
import kr.co.shop.web.display.model.master.DpCategoryProduct;
import kr.co.shop.web.display.repository.master.base.BaseDpCategoryProductDao;

@Mapper
public interface DpCategoryProductDao extends BaseDpCategoryProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpCategoryProductDao 클래스에 구현 되어있습니다.
	 * BaseDpCategoryProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpCategoryProduct selectByPrimaryKey(DpCategoryProduct dpCategoryProduct) throws Exception;

	public int selectDpCategoryProductTotalCount(DpCategory dpCategoray) throws Exception;

}
