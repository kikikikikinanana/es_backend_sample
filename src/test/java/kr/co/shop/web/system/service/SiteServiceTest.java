/**
 * 
 */
package kr.co.shop.web.system.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.model.master.SySiteApplySns;
import kr.co.shop.web.system.model.master.SySiteChnnl;
import kr.co.shop.web.system.model.master.SySiteDeliveryType;
import kr.co.shop.web.system.model.master.SySitePaymentMeans;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : SiteServiceTest.java
 * @Project : shop.backend
 * @Date : 2019. 4. 4.
 * @Author : Kimyounghyun
 */
@SpringBootTest
@Slf4j
class SiteServiceTest {

	@Autowired
	SiteService siteService;

	@Test
	void testgetUseSiteApplySnsList() throws Exception {
		String siteNo = "10000";
		List<SySiteApplySns> list = siteService.getUseSiteApplySnsList(siteNo);

		for (SySiteApplySns sySiteApplySns : list) {
			log.debug("testgetUseSiteApplySnsList - {}", sySiteApplySns);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getSiteList()}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetSiteList() throws Exception {
		SySite site = new SySite();
		List<SySite> list = siteService.getSiteList(site);

		for (SySite sySite : list) {
			log.debug("testGetSiteList - {}", sySite);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseChannelListBySiteNo(kr.co.shop.web.system.model.master.SySiteChnnl)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseChannelListBySiteNo() throws Exception {
		SySiteChnnl sySiteChnnl = new SySiteChnnl();
		sySiteChnnl.setSiteNo("10000");
		List<SySiteChnnl> list = siteService.getUseChannelListBySiteNo(sySiteChnnl);

		for (SySiteChnnl sySiteChnnl2 : list) {
			log.debug("testGetUseChannelListBySiteNo - {}", sySiteChnnl2);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseChannelList()}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseChannelList() throws Exception {
		SySiteChnnl sySiteChnnl = new SySiteChnnl();
		List<SySiteChnnl> list = siteService.getUseChannelList(sySiteChnnl);

		for (SySiteChnnl siteChnnl : list) {
			log.debug("testGetUseChannelList - {}", siteChnnl);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getSite(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetSite() throws Exception {
		SySite sySite = siteService.getSite("10000");
		log.debug("testGetSite - {}", sySite);
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseDeliveryType(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseDeliveryType() throws Exception {
		List<SySiteDeliveryType> list = siteService.getUseDeliveryType("10000");

		for (SySiteDeliveryType sySiteDeliveryType : list) {
			log.debug("testGetUseDeliveryType - {}", sySiteDeliveryType);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseEmployeePaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseEmployeePaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseEmployeePaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseEmployeePaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseEmployeeMainPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseEmployeeMainPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseEmployeeMainPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseEmployeeMainPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseEmployeeSubPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseEmployeeSubPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseEmployeeSubPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseEmployeeSubPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseOnlinePaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseOnlinePaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseOnlinePaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseOnlinePaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseOnlineMainPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseOnlineMainPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseOnlineMainPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseOnlineMainPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseOnlineSubPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseOnlineSubPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseOnlineSubPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseOnlineSubPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseMemberNormalPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseMemberNormalPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseMemberNormalPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseMemberNormalPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseMemberNormalMainPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseMemberNormalMainPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseMemberNormalMainPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseMemberNormalMainPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseMemberNormalSubPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseMemberNormalSubPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseMemberNormalSubPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseMemberNormalSubPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseMemberReservePaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseMemberReservePaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseMemberReservePaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseMemberReservePaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseMemberReserveMainPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseMemberReserveMainPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseMemberReserveMainPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseMemberReserveMainPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseMemberReserveSubPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseMemberReserveSubPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseMemberReserveSubPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseMemberReserveSubPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseGuestPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseGuestPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseGuestPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseGuestPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseGuestMainPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseGuestMainPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseGuestMainPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseGuestMainPaymentMeans - {}", sySitePaymentMeans);
		}
	}

	/**
	 * Test method for
	 * {@link kr.co.shop.web.system.service.SiteService#getUseGuestSubPaymentMeans(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetUseGuestSubPaymentMeans() throws Exception {
		List<SySitePaymentMeans> list = siteService.getUseGuestSubPaymentMeans("10000");

		for (SySitePaymentMeans sySitePaymentMeans : list) {
			log.debug("testGetUseGuestSubPaymentMeans - {}", sySitePaymentMeans);
		}
	}

}
