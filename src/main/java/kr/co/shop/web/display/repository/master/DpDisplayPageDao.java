package kr.co.shop.web.display.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpDisplayPage;
import kr.co.shop.web.display.repository.master.base.BaseDpDisplayPageDao;

@Mapper
public interface DpDisplayPageDao extends BaseDpDisplayPageDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpDisplayPageDao 클래스에 구현 되어있습니다.
	 * BaseDpDisplayPageDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpDisplayPage selectByPrimaryKey(DpDisplayPage dpDisplayPage) throws Exception;

	/**
	 * 전시 페이지 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpDisplayPage
	 * @Date : 2019. 4. 16.
	 * @Author : SANTA
	 * @param dpDisplayPage
	 * @return
	 * @throws Exception
	 */
	public DpDisplayPage selectDpDisplayPage(DpDisplayPage dpDisplayPage) throws Exception;

}
