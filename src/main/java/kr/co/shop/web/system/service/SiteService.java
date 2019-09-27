/**
 * 
 */
package kr.co.shop.web.system.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import kr.co.shop.constant.Const;
import kr.co.shop.web.system.model.master.SySite;
import kr.co.shop.web.system.model.master.SySiteApplySns;
import kr.co.shop.web.system.model.master.SySiteChnnl;
import kr.co.shop.web.system.model.master.SySiteDeliveryType;
import kr.co.shop.web.system.model.master.SySitePaymentMeans;
import kr.co.shop.web.system.repository.master.SySiteApplySnsDao;
import kr.co.shop.web.system.repository.master.SySiteChnnlDao;
import kr.co.shop.web.system.repository.master.SySiteDao;
import kr.co.shop.web.system.repository.master.SySiteDeliveryTypeDao;
import kr.co.shop.web.system.repository.master.SySitePaymentMeansDao;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc :
 * @FileName : SiteService.java
 * @Project : shop.backend
 * @Date : 2019. 3. 19.
 * @Author : Kimyounghyun
 */
@Slf4j
@Service
public class SiteService {

	// 주문유형 : 예약
	public static final String ORDER_TYPE_RESERVE = "R";
	// 주문유형 : 일반
	public static final String ORDER_TYPE_GENERAL = "G";

	private static Map<String, String> SITE_PC_URL_MAP;
	private static Map<String, String> SITE_MOBILE_URL_MAP;
	private static Map<String, String> CHANNEL_PC_URL_MAP;
	private static Map<String, String> CHANNEL_MOBILE_URL_MAP;

	@Autowired
	SySiteDao sySiteDao;

	@Autowired
	SySiteChnnlDao sySiteChnnlDao;

	@Autowired
	SySiteDeliveryTypeDao sySiteDeliveryTypeDao;

	@Autowired
	SySitePaymentMeansDao sySitePaymentMeansDao;

	@Autowired
	SySiteApplySnsDao sySiteApplySnsDao;

	@Value("${spring.profiles.active}")
	public String activeProfile;

	@PostConstruct
	private void init() throws Exception {
		initConfig();
	}

	/**
	 * @Desc : 사이트별 사용중인 sns공유채널을 조회한다.
	 * @Method Name : getUseSiteApplySnsList
	 * @Date : 2019. 6. 21.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySiteApplySns> getUseSiteApplySnsList(String siteNo) throws Exception {
		return sySiteApplySnsDao.selectApplySnsList(siteNo);
	}

	/**
	 * @Desc : 사이트 목록을 조회한다. local/dev/prod 환경에 따라서 app-config.properties에서 서로 다른
	 *       URL정보를 캐쉬하기 때문에 spring.profiles.active 값을 key로 사용한다.
	 * @Method Name : getUseSiteList
	 * @Date : 2019. 3. 19.
	 * @Author : Kimyounghyun
	 * @return
	 * @throws Exception
	 */
	@Cacheable(value = "siteService.getSiteList", key = "{#param.pc, #root.target.activeProfile}")
	public List<SySite> getSiteList(SySite param) throws Exception {
		List<SySite> siteList = sySiteDao.selectList();
		for (SySite sySite : siteList) {
			if (param.getPc()) {
				sySite.setSiteUrl(SITE_PC_URL_MAP.get(sySite.getSiteNo()));
			} else {
				sySite.setSiteUrl(SITE_MOBILE_URL_MAP.get(sySite.getSiteNo()));
			}
		}

		return siteList;
	}

	/**
	 * @Desc : 사이트별 사용중인 채널목록을 조회한다. local/dev/prod 환경에 따라서 app-config.properties에서
	 *       서로 다른 URL정보를 캐쉬하기 때문에 spring.profiles.active 값을 key로 사용한다.
	 * @Method Name : getUseChannelListBySiteNo
	 * @Date : 2019. 3. 20.
	 * @Author : Kimyounghyun
	 * @param param
	 * @return
	 */
	@Cacheable(value = "siteService.getUseChannelListBySiteNo", key = "{#param.siteNo, #param.pc, #root.target.activeProfile}")
	public List<SySiteChnnl> getUseChannelListBySiteNo(SySiteChnnl param) throws Exception {
		List<SySiteChnnl> channelList = sySiteChnnlDao.selectUseChannelListBySiteNo(param);
		for (SySiteChnnl sySiteChnnl : channelList) {
			if (param.getPc()) {
				sySiteChnnl.setChnnlUrl(CHANNEL_PC_URL_MAP.get(sySiteChnnl.getChnnlNo()));
			} else {
				sySiteChnnl.setChnnlUrl(CHANNEL_MOBILE_URL_MAP.get(sySiteChnnl.getChnnlNo()));
			}
		}
		return channelList;
	}

	/**
	 * @Desc : 사이트 구분없이 전체 채널목록을 조회한다. local/dev/prod 환경에 따라서
	 *       app-config.properties에서 서로 다른 URL정보를 캐쉬하기 때문에 spring.profiles.active 값을
	 *       key로 사용한다.
	 * @Method Name : getUseChannelList
	 * @Date : 2019. 3. 20.
	 * @Author : Kimyounghyun
	 * @return
	 */
	@Cacheable(value = "siteService.getUseChannelList", key = "{#param.pc, #root.target.activeProfile}")
	public List<SySiteChnnl> getUseChannelList(SySiteChnnl param) throws Exception {
		List<SySiteChnnl> channelList = sySiteChnnlDao.selectUseChannelList();
		for (SySiteChnnl sySiteChnnl : channelList) {
			if (param.getPc()) {
				sySiteChnnl.setChnnlUrl(CHANNEL_PC_URL_MAP.get(sySiteChnnl.getChnnlNo()));
			} else {
				sySiteChnnl.setChnnlUrl(CHANNEL_MOBILE_URL_MAP.get(sySiteChnnl.getChnnlNo()));
			}
		}

		return channelList;
	}

	/**
	 * @Desc : 사이트 & 사이트별 배송비용 설정 조회
	 * @Method Name : getSite
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public SySite getSite(String siteNo) throws Exception {
		return sySiteDao.selectSite(siteNo);
	}

	/**
	 * @Desc : 사이트별 사용중인 배송유형을 조회한다.
	 * @Method Name : getUseDeliveryType
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySiteDeliveryType> getUseDeliveryType(String siteNo) throws Exception {
		return sySiteDeliveryTypeDao.selectUseBySiteNo(CommonCode.DLVY_TYPE_CODE, siteNo);

	}

	/**
	 * @Desc : 사이트별 사용중인 임직원 결제수단 조회 - 주/보조 전체
	 * @Method Name : getUseEmployeePaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseEmployeePaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_TRUE,
				ORDER_TYPE_GENERAL, null);
	}

	/**
	 * @Desc : 사이트별 사용중인 임직원 결제수단 조회 - 주 결제
	 * @Method Name : getUseEmployeeMainPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseEmployeeMainPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_TRUE,
				ORDER_TYPE_GENERAL, Const.BOOLEAN_TRUE);
	}

	/**
	 * @Desc : 사이트별 사용중인 임직원 결제수단 조회 - 보조 결제
	 * @Method Name : getUseEmployeeSubPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseEmployeeSubPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_TRUE,
				ORDER_TYPE_GENERAL, Const.BOOLEAN_FALSE);
	}

	/**
	 * @Desc : 사이트별 사용중인 온라인회원 결제수단 조회 - 주/보조 전체
	 * @Method Name : getUseOnlinePaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseOnlinePaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_ONLINE, Const.BOOLEAN_FALSE, ORDER_TYPE_GENERAL,
				null);
	}

	/**
	 * @Desc : 사이트별 사용중인 온라인회원 결제수단 조회 - 주 결제
	 * @Method Name : getUseOnlineMainPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseOnlineMainPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_ONLINE, Const.BOOLEAN_FALSE, ORDER_TYPE_GENERAL,
				Const.BOOLEAN_TRUE);
	}

	/**
	 * @Desc : 사이트별 사용중인 온라인회원 결제수단 조회 - 보조 결제
	 * @Method Name : getUseOnlineSubPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseOnlineSubPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_ONLINE, Const.BOOLEAN_FALSE, ORDER_TYPE_GENERAL,
				Const.BOOLEAN_FALSE);
	}

	/**
	 * @Desc : 사이트별 사용중인 멤버십 일반 결제수단 조회 - 주/보조 전체
	 * @Method Name : getUseMemberNormalPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseMemberNormalPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_FALSE,
				ORDER_TYPE_GENERAL, null);
	}

	/**
	 * @Desc : 사이트별 사용중인 멤버십 일반 결제수단 조회 - 주 결제
	 * @Method Name : getUseMemberNormalMainPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseMemberNormalMainPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_FALSE,
				ORDER_TYPE_GENERAL, Const.BOOLEAN_TRUE);
	}

	/**
	 * @Desc : 사이트별 사용중인 멤버십 일반 결제수단 조회 - 보조 결제
	 * @Method Name : getUseMemberNormalSubPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseMemberNormalSubPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_FALSE,
				ORDER_TYPE_GENERAL, Const.BOOLEAN_FALSE);
	}

	/**
	 * @Desc : 사이트별 사용중인 멤버십 예약 결제수단 조회 - 주/보조 전체
	 * @Method Name : getUseMemberReservePaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseMemberReservePaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_FALSE,
				ORDER_TYPE_RESERVE, null);
	}

	/**
	 * @Desc : 사이트별 사용중인 멤버십 예약 결제수단 조회 - 주 결제
	 * @Method Name : getUseMemberReserveMainPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseMemberReserveMainPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_FALSE,
				ORDER_TYPE_RESERVE, Const.BOOLEAN_TRUE);
	}

	/**
	 * @Desc : 사이트별 사용중인 멤버십 예약 결제수단 조회 - 보조 결제
	 * @Method Name : getUseMemberReserveSubPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseMemberReserveSubPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_MEMBERSHIP, Const.BOOLEAN_FALSE,
				ORDER_TYPE_RESERVE, Const.BOOLEAN_FALSE);
	}

	/**
	 * @Desc : 사이트별 사용중인 비회원 결제수단 조회 - 주/보조 전체
	 * @Method Name : getUseGuestPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseGuestPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_NONMEMBER, Const.BOOLEAN_FALSE,
				ORDER_TYPE_GENERAL, null);
	}

	/**
	 * @Desc : 사이트별 사용중인 비회원 결제수단 조회 - 주 결제
	 * @Method Name : getUseGuestMainPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseGuestMainPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_NONMEMBER, Const.BOOLEAN_FALSE,
				ORDER_TYPE_GENERAL, Const.BOOLEAN_TRUE);
	}

	/**
	 * @Desc : 사이트별 사용중인 비회원 결제수단 조회 - 보조 결제
	 * @Method Name : getUseGuestSubPaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param siteNo
	 * @return
	 * @throws Exception
	 */
	public List<SySitePaymentMeans> getUseGuestSubPaymentMeans(String siteNo) throws Exception {
		return getUsePaymentMeans(siteNo, CommonCode.MEMBER_TYPE_CODE_NONMEMBER, Const.BOOLEAN_FALSE,
				ORDER_TYPE_GENERAL, Const.BOOLEAN_FALSE);
	}

	/**
	 * @Desc : 케이스별 결제수단을 조회한다.
	 * @Method Name : getUsePaymentMeans
	 * @Date : 2019. 4. 4.
	 * @Author : Kimyounghyun
	 * @param stieNo
	 * @return
	 * @throws Exception
	 */
	private List<SySitePaymentMeans> getUsePaymentMeans(String siteNo, String memberTypeCode, String empYn,
			String orderType, String mainYn) throws Exception {
		SySitePaymentMeans sySitePaymentMeans = new SySitePaymentMeans();

		sySitePaymentMeans.setMemberTypeCodeField(CommonCode.MEMBER_TYPE_CODE);
		sySitePaymentMeans.setPymntMeansCodeField(CommonCode.PYMNT_MEANS_CODE);

		sySitePaymentMeans.setSiteNo(siteNo);
		sySitePaymentMeans.setMemberTypeCode(memberTypeCode);
		sySitePaymentMeans.setEmpYn(empYn);
		sySitePaymentMeans.setOrderType(orderType);

		if (UtilsText.isNotBlank(mainYn)) {
			sySitePaymentMeans.setMainPymntMeansYn(mainYn);
		}

		return sySitePaymentMeansDao.selectUsePaymentMeans(sySitePaymentMeans);
	}

	private void initConfig() throws Exception {
		SITE_PC_URL_MAP = new HashMap<String, String>();
		String sitePcUrlList = Const.SITE_PC_URL_LIST;
		SITE_PC_URL_MAP = Arrays.stream(sitePcUrlList.split(",")).map(s -> s.split("="))
				.collect(Collectors.toMap(a -> a[0], a -> a[1]));

		SITE_MOBILE_URL_MAP = new HashMap<String, String>();
		String siteMobileUrlList = Const.SITE_MOBILE_URL_LIST;
		SITE_MOBILE_URL_MAP = Arrays.stream(siteMobileUrlList.split(",")).map(s -> s.split("="))
				.collect(Collectors.toMap(a -> a[0], a -> a[1]));

		CHANNEL_PC_URL_MAP = new HashMap<String, String>();
		String channelPcUrlList = Const.CHANNEL_PC_URL_LIST;
		CHANNEL_PC_URL_MAP = Arrays.stream(channelPcUrlList.split(",")).map(s -> s.split("="))
				.collect(Collectors.toMap(a -> a[0], a -> a[1]));

		CHANNEL_MOBILE_URL_MAP = new HashMap<String, String>();
		String channelMobileUrlList = Const.CHANNEL_MOBILE_URL_LIST;
		CHANNEL_MOBILE_URL_MAP = Arrays.stream(channelMobileUrlList.split(",")).map(s -> s.split("="))
				.collect(Collectors.toMap(a -> a[0], a -> a[1]));

		log.debug("SITE_PC_URL_MAP : {}", SITE_PC_URL_MAP);
		log.debug("SITE_MOBILE_URL_MAP : {}", SITE_MOBILE_URL_MAP);
		log.debug("CHANNEL_PC_URL_MAP : {}", CHANNEL_PC_URL_MAP);
		log.debug("CHANNEL_MOBILE_URL_MAP : {}", CHANNEL_MOBILE_URL_MAP);
	}
}
