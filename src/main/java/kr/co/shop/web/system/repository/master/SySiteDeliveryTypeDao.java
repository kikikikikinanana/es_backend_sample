package kr.co.shop.web.system.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.shop.web.system.model.master.SySiteDeliveryType;
import kr.co.shop.web.system.repository.master.base.BaseSySiteDeliveryTypeDao;

@Mapper
public interface SySiteDeliveryTypeDao extends BaseSySiteDeliveryTypeDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseSySiteDeliveryTypeDao 클래스에 구현 되어있습니다.
	 * BaseSySiteDeliveryTypeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public SySiteDeliveryType selectByPrimaryKey(SySiteDeliveryType sySiteDeliveryType) throws Exception;

	public List<SySiteDeliveryType> selectUseBySiteNo(@Param("dlvyTypeCode") String dlvyTypeCode,
			@Param("siteNo") String siteNo);

}
