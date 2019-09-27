package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.display.model.master.BdPopup;
import kr.co.shop.web.display.repository.master.base.BaseBdPopupDao;

@Mapper
public interface BdPopupDao extends BaseBdPopupDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdPopupDao 클래스에 구현 되어있습니다. BaseBdPopupDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdPopup selectByPrimaryKey(BdPopup bdPopup) throws Exception;

	/**
	 * @Desc : 팝업 조회
	 * @Method Name : selectBdPopup
	 * @Date : 2019. 5. 28.
	 * @Author : 이가영
	 * @param bdPopup
	 * @return
	 */
	public BdPopup selectBdPopup(BdPopup bdPopup) throws Exception;

	/**
	 * @Desc : 팝업 리스트 조회
	 * @Method Name : selectBdPopupList
	 * @Date : 2019. 5. 28.
	 * @Author : 이가영
	 * @param bdPopup
	 * @return
	 */
	public List<BdPopup> selectBdPopupList(BdPopup bdPopup) throws Exception;

}
