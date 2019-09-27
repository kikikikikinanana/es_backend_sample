/**
 * 
 */
package kr.co.shop.web.system.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.shop.web.system.model.master.SyCodeDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : CommonCodeServiceTest.java
 * @Project : shop.backend
 * @Date : 2019. 3. 4.
 * @Author : Kimyounghyun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class CommonCodeServiceTest {

	@Autowired
	private CommonCodeService service;

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.CommonCodeService#getCodeNoName(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseCode() throws Exception {
//		fail("Not yet implemented");
		List<SyCodeDetail> syCodeDetails = service.getUseCode("SNS_GBN_CODE");
		for (SyCodeDetail syCodeDetail : syCodeDetails) {
			log.debug("testGetUseCode - {}", syCodeDetail.toString());
		}

	}
}
