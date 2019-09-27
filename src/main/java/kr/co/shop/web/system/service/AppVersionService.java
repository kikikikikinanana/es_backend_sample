/**
 * 
 */
package kr.co.shop.web.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.system.model.master.SyAppVersion;
import kr.co.shop.web.system.repository.master.SyAppVersionDao;

@Service
public class AppVersionService {

	@Autowired
	SyAppVersionDao syAppVersionDao;

	/**
	 *
	 * @Desc : 앱버전 정보를 조회한다.
	 * @Method Name : getAppVersionData
	 * @Date : 2019. 6. 27.
	 * @Author : 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public SyAppVersion getAppVersionData(SyAppVersion syAppVersion) throws Exception {
		return syAppVersionDao.selectAppVersionData(syAppVersion);
	}

}
