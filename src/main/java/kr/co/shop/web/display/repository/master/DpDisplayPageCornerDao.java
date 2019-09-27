package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpDisplayPageCorner;
import kr.co.shop.web.display.repository.master.base.BaseDpDisplayPageCornerDao;

@Mapper
public interface DpDisplayPageCornerDao extends BaseDpDisplayPageCornerDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpDisplayPageCornerDao 클래스에 구현 되어있습니다.
	 * BaseDpDisplayPageCornerDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public DpDisplayPageCorner selectByPrimaryKey(DpDisplayPageCorner dpDisplayPageCorner) throws Exception;

	/**
	 * 전시 페이지 코너 리스트 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpDisplayPageCornerList
	 * @Date : 2019. 4. 16.
	 * @Author : SANTA
	 * @param dpDisplayPageCorner
	 * @return
	 * @throws Exception
	 */
	public List<DpDisplayPageCorner> selectDpDisplayPageCornerList(DpDisplayPageCorner dpDisplayPageCorner)
			throws Exception;
}
