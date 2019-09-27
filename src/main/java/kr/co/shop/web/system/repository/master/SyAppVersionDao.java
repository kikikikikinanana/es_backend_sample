package kr.co.shop.web.system.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.system.model.master.SyAppVersion;
import kr.co.shop.web.system.repository.master.base.BaseSyAppVersionDao;

@Mapper
public interface SyAppVersionDao extends BaseSyAppVersionDao {

     /**
     * 기본 insert, update, delete 메소드는 BaseSyAppVersionDao 클래스에 구현 되어있습니다.
     * BaseSyAppVersionDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     *
     */

	public SyAppVersion selectByPrimaryKey(SyAppVersion syAppVersion) throws Exception;

	/**
	 *
	 * @Desc      	: 앱버전 정보를 조회한다.
	 * @Method Name : getAppVersionData
	 * @Date  	  	: 2019. 6. 27.
	 * @Author    	: 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public SyAppVersion selectAppVersionData(SyAppVersion syAppVersion) throws Exception;

}
