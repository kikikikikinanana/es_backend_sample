package kr.co.shop.web.display.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpDisplayPageCornerName;
import kr.co.shop.web.display.repository.master.base.BaseDpDisplayPageCornerNameDao;

@Mapper
public interface DpDisplayPageCornerNameDao extends BaseDpDisplayPageCornerNameDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpDisplayPageCornerNameDao 클래스에 구현 되어있습니다.
	 * BaseDpDisplayPageCornerNameDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public DpDisplayPageCornerName selectByPrimaryKey(DpDisplayPageCornerName dpDisplayPageCornerName) throws Exception;

	/**
	 * 전시 페이지 코너명 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpDisplayPageCornerName
	 * @Date : 2019. 4. 17.
	 * @Author : SANTA
	 * @param dpDisplayPageCornerName
	 * @return
	 * @throws Exception
	 */
	public DpDisplayPageCornerName selectDpDisplayPageCornerName(DpDisplayPageCornerName dpDisplayPageCornerName)
			throws Exception;

}
