package kr.co.shop.web.system.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.system.repository.master.base.BaseSySiteDeliveryGuideDao;
import kr.co.shop.web.system.model.master.SySiteDeliveryGuide;

@Mapper
public interface SySiteDeliveryGuideDao extends BaseSySiteDeliveryGuideDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseSySiteDeliveryGuideDao 클래스에 구현 되어있습니다.
     * BaseSySiteDeliveryGuideDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public SySiteDeliveryGuide selectByPrimaryKey(SySiteDeliveryGuide sySiteDeliveryGuide) throws Exception;

}
