package kr.co.shop.web.display.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.display.repository.master.base.BaseBdPopupDeviceDao;
import kr.co.shop.web.display.model.master.BdPopupDevice;

@Mapper
public interface BdPopupDeviceDao extends BaseBdPopupDeviceDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseBdPopupDeviceDao 클래스에 구현 되어있습니다.
     * BaseBdPopupDeviceDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public BdPopupDevice selectByPrimaryKey(BdPopupDevice bdPopupDevice) throws Exception;

}
