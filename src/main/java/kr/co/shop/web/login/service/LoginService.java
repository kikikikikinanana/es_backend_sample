/**
 * 
 */
package kr.co.shop.web.login.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.system.service.CommonCodeService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : LoginService.java
 * @Project : shop.backend
 * @Date : 2019. 3. 5.
 * @Author : Kimyounghyun
 */

@Slf4j
@Service
public class LoginService {

	@Autowired
	CommonCodeService commonCodeService;

	/**
	 * @Desc : 로그인 화면에 필요한 데이터를 조회.
	 * @Method Name : getLoginFormData
	 * @Date : 2019. 3. 5.
	 * @Author : Kimyounghyun
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getLoginFormData() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commonCode", commonCodeService.getUseCode(CommonCode.SNS_GBN_CODE));

		return map;
	}

}
