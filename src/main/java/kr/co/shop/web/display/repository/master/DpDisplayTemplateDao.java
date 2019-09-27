package kr.co.shop.web.display.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpDisplayTemplate;
import kr.co.shop.web.display.repository.master.base.BaseDpDisplayTemplateDao;

@Mapper
public interface DpDisplayTemplateDao extends BaseDpDisplayTemplateDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpDisplayTemplateDao 클래스에 구현 되어있습니다.
	 * BaseDpDisplayTemplateDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpDisplayTemplate selectByPrimaryKey(DpDisplayTemplate dpDisplayTemplate) throws Exception;

	/**
	 * 전시 템플릿 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpDisplayTemplate
	 * @Date : 2019. 4. 16.
	 * @Author : SANTA
	 * @param dpDisplayTemplate
	 * @return
	 * @throws Exception
	 */
	public DpDisplayTemplate selectDpDisplayTemplate(DpDisplayTemplate dpDisplayTemplate) throws Exception;

}
