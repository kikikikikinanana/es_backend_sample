package kr.co.shop.web.system.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.system.model.master.SySiteChnnl;
import kr.co.shop.web.system.repository.master.base.BaseSySiteChnnlDao;

@Mapper
public interface SySiteChnnlDao extends BaseSySiteChnnlDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseSySiteChnnlDao 클래스에 구현 되어있습니다.
	 * BaseSySiteChnnlDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public SySiteChnnl selectByPrimaryKey(SySiteChnnl sySiteChnnl) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : selectUseChannelListBySiteNo
	 * @Date : 2019. 3. 19.
	 * @Author : Kimyounghyun
	 * @param sySiteChnnl
	 * @return
	 */
	public List<SySiteChnnl> selectUseChannelListBySiteNo(SySiteChnnl sySiteChnnl);

	/**
	 * @Desc :
	 * @Method Name : selectUseChannelList
	 * @Date : 2019. 3. 20.
	 * @Author : Kimyounghyun
	 * @return
	 */
	public List<SySiteChnnl> selectUseChannelList();

}
