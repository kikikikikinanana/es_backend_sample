/**
 * 
 */
package kr.co.shop.web.cmm.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.shop.web.cmm.model.master.CmTermsDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : TermsServiceTest.java
 * @Project : shop.backend
 * @Date : 2019. 4. 24.
 * @Author : Kimyounghyun
 */
@SpringBootTest
@Slf4j
class TermsServiceTest {

	@Autowired
	TermsService service;

	@Test
	void testGetMemberOrderTerms() {
		List<CmTermsDetail> list = service.getMemberOrderTerms();
		for (CmTermsDetail cmTermsDetail : list) {
			log.debug("cmTermsDetail - {}", cmTermsDetail);
		}
	}

	@Test
	void testGetNonMemberOrderTerms() {
		List<CmTermsDetail> list = service.getNonMemberOrderTerms();
		for (CmTermsDetail cmTermsDetail : list) {
			log.debug("cmTermsDetail - {}", cmTermsDetail);
		}
	}

}
