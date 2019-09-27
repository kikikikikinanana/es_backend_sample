package kr.co.shop.web.system.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.repository.master.base.BaseSySiteDao;

@Mapper
public interface SySiteDao extends BaseSySiteDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseSySiteDao 클래스에 구현 되어있습니다. BaseSySiteDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public SySite selectByPrimaryKey(SySite sySite) throws Exception;

	public SySite selectSite(String siteNo) throws Exception;

	public List<SySite> selectList() throws Exception;

}
