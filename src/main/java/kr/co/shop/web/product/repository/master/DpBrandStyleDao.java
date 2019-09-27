package kr.co.shop.web.product.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.DpBrandStyle;
import kr.co.shop.web.product.repository.master.base.BaseDpBrandStyleDao;

@Mapper
public interface DpBrandStyleDao extends BaseDpBrandStyleDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpBrandStyleDao 클래스에 구현 되어있습니다.
	 * BaseDpBrandStyleDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpBrandStyle selectByPrimaryKey(DpBrandStyle dpBrandStyle) throws Exception;

	public List<DpBrandStyle> getBrandStyleList(DpBrandStyle brdStyle) throws Exception;

	public List<DpBrandStyle> getBrandCategoryList(DpBrandStyle brdStyle) throws Exception;

}
