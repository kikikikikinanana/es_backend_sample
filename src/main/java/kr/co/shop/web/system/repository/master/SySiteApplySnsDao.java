package kr.co.shop.web.system.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.system.model.master.SySiteApplySns;
import kr.co.shop.web.system.repository.master.base.BaseSySiteApplySnsDao;

@Mapper
public interface SySiteApplySnsDao extends BaseSySiteApplySnsDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseSySiteApplySnsDao 클래스에 구현 되어있습니다.
	 * BaseSySiteApplySnsDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public SySiteApplySns selectByPrimaryKey(SySiteApplySns sySiteApplySns) throws Exception;

	public List<SySiteApplySns> selectApplySnsList(String siteNo);

}
