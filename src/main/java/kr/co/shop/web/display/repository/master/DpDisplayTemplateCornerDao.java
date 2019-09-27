package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpDisplayTemplateCorner;
import kr.co.shop.web.display.repository.master.base.BaseDpDisplayTemplateCornerDao;

@Mapper
public interface DpDisplayTemplateCornerDao extends BaseDpDisplayTemplateCornerDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpDisplayTemplateCornerDao 클래스에 구현 되어있습니다.
	 * BaseDpDisplayTemplateCornerDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public DpDisplayTemplateCorner selectByPrimaryKey(DpDisplayTemplateCorner dpDisplayTemplateCorner) throws Exception;

	public List<DpDisplayTemplateCorner> selectTemplateCornerList(DpDisplayTemplateCorner dpDisplayTemplateCorner)
			throws Exception;

}
