package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.DpDisplayPageCornerSet;
import kr.co.shop.web.display.repository.master.base.BaseDpDisplayPageCornerSetDao;

@Mapper
public interface DpDisplayPageCornerSetDao extends BaseDpDisplayPageCornerSetDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpDisplayPageCornerSetDao 클래스에 구현 되어있습니다.
	 * BaseDpDisplayPageCornerSetDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public DpDisplayPageCornerSet selectByPrimaryKey(DpDisplayPageCornerSet dpDisplayPageCornerSet) throws Exception;

	/**
	 * 전시 페이지 코너 셋 리스트 조회
	 * 
	 * @Desc :
	 * @Method Name : selectDpDisplayPageCornerSetList
	 * @Date : 2019. 4. 17.
	 * @Author : SANTA
	 * @param dpDisplayPageCornerSet
	 * @return
	 * @throws Exception
	 */
	public List<DpDisplayPageCornerSet> selectDpDisplayPageCornerSetList(DpDisplayPageCornerSet dpDisplayPageCornerSet)
			throws Exception;
}
