package kr.co.shop.web.product.model.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.shop.common.bean.Bean;
import kr.co.shop.common.constant.BaseConst;
import kr.co.shop.common.paging.Pageable;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsArray;
import kr.co.shop.common.util.UtilsText;
import kr.co.shop.constant.CommonCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 상품 목록 페이징 객체
 * @FileName : PageableProduct.java
 * @Project : shop.common
 * @Date : 2019. 5. 16.
 * @Author : tennessee
 */
@Slf4j
@SuppressWarnings("unused")
public class PageableProduct<V, M> extends Pageable<V, M> {

	/** 전체갯수조회 결과map에 대한 결과key */
	public static final String MAP_KEY_TOTAL_COUNT = "total_count";

	/**
	 * 조회 유형<br/>
	 */
	/** 유형 - 목록조회 */
	public static final String TYPE_LIST = "list";
	/** 유형 - 주요정보조회 */
	public static final String TYPE_MAJOR = "major";
	/** 유형 - 스타일컬러(색상연계) */
	public static final String TYPE_LIST_RELATED_COLOR = "related-color";
	/** 유형 - 관련용품 */
	public static final String TYPE_LIST_RELATED_GOODS = "related-goods";
	/** 유형 - 상세페이지 */
	public static final String TYPE_DETAIL = "detail";
	/** 유형. (기본값 : PageableProduct.TYPE_LIST) */
	private String type = TYPE_LIST;

	/**
	 * 테이블매핑유형<br/>
	 * 사용자지정 테이블과 매핑. 단, 해당 테이블에 "prdt_no"컬럼이 있어야 함<br/>
	 * 상품번호목록에 의한 포함검색 가능<br/>
	 * 문자열값에 대한 equal 검색 가능<br/>
	 * 숫자값에 대한 equal 검색 가능<br/>
	 * 카테고리 매핑유형을 통해 하위카테고리에 포함된 상품조회 가능. 단, upperCtgrNo가 설정되어야 함.<br/>
	 */
	/** 테이블매핑유형 - 사용자정의(사용자로부터 테이블명과 join대상컬럼 입력받음) */
	public static final String MAPPING_TYPE_CUSTOM = "custom-table";
	/** 테이블매핑유형 - 상위카테고리번호로 하위카테고리에 연결된 상품 조회 */
	public static final String MAPPING_TYPE_CATEGORY = "category";
	/** 매핑유형 */
	private String mappingType;
	/** 테이블매핑 사용여부 */
//	private boolean useTableMapping = true;
	/** 테이블매핑 사용 시, 매핑테이블이름 */
	private String mappingTableName;
	/** 테이블매핑 미사용 시, 조회할 상품번호목록 */
	private List<String> prdtNoList;
	/** 테이블매핑 사용 시, 비교할 문자열 컬럼 목록. KEY는 컬럼명, VALUE는 컬럼값 */
	private Map<String, String> conditionColumnString;
	/** 테이블매핑 사용 시, 비교할 숫자형 컬럼 목록. KEY는 컬럼명, VALUE는 컬럼값 */
	private Map<String, Integer> conditionColumnInteger;
	/** 상위카테고리 번호 */
//	private String upperCtgrNo;

	/** 상품 마스터 테이블 외 다른 테이블 사용 여부 */
	private boolean useOtherTables = true;

	/**
	 * 기본 검색 조건<br/>
	 * 사이트번호<br/>
	 * 채널번호<br/>
	 * A커넥트전시여부<br/>
	 */
	/** 사이트번호 */
	private String siteNo;
	/** 채널번호 */
	private String chnnlNo;
	/** A커넥트 전시여부 */
	private String aconnectDispYn;

	/** 장치 코드. 상품상세테이블 조회에서 사용됨 */
	private String deviceCode;

	/** 회원번호. 단건 상품 조회 시 찜정보 조회에 사용됨 */
	private String memberNo;
	/** 찜상품 조회 시, 알림/찜상품 구분값. (기본값 : 찜상품) */
	private String wrhsAlertReqYn = BaseConst.BOOLEAN_FALSE;

	/** 페이징 사용여부 */
	private boolean usePaging;
	private String sortColumnName;
	private String sortDirection;
	/** 페이징 유형. 객체 내에 설정된 상수값(PAGING_SORT_TYPE_*)으로 설정. */
	private String pagingSortType;

	/** 정렬기준 - 신상품순 */
	public static final String PAGING_SORT_TYPE_NEWEST = "newest";
	/** 정렬기준 - 베스트상품순. 주문 배치테이블 참고해야함. 아직 생성되지 않음 */
	public static final String PAGING_SORT_TYPE_BESTEST = "bestest";
	/** 정렬기준 - 상품평순 */
	public static final String PAGING_SORT_TYPE_EVALUATION = "evaluation";
	/** 정렬기준 - 낮은가격순 */
	public static final String PAGING_SORT_TYPE_LOW_PRICE = "lowPrice";
	/** 정렬기준 - 높은가격순 */
	public static final String PAGING_SORT_TYPE_HIGH_PRICE = "highPrice";
	/** 정렬기준 - 커스텀정렬기준순 */
	public static final String PAGING_SORT_TYPE_CUSTOM = "custom";
	/** 정렬기준 - 낮은할인율순 */
	public static final String PAGING_SORT_TYPE_LOW_DISCOUNT_RATE = "lowDiscountRate";
	/** 정렬기준 - 높은할인율순 */
	public static final String PAGING_SORT_TYPE_HIGH_DISCOUNT_RATE = "highDiscountRate";
	/** 정렬기준 - 승인일자과거순 */
	public static final String PAGING_SORT_TYPE_PAST_APPROVAL_DATE = "pastApprovalDate";
	/** 정렬기준 - 승인일자최근순 */
	public static final String PAGING_SORT_TYPE_RECENT_APPROVAL_DATE = "recentApprovalDate";

	/** 정렬기준 - 커스텀정렬기준순 정렬방향. 오름차순 */
	public static final String SORT_DIRECTION_ASCEND = "ASC";
	/** 정렬기준 - 커스텀정렬기준순 정렬방향. 내림차순 */
	public static final String SORT_DIRECTION_DESCEND = "DESC";

	/**
	 * 상품유형코드
	 */
	@SuppressWarnings("serial")
	private List<String> prdtTypeCodes = new ArrayList<String>() {
		{
			add(CommonCode.PRDT_TYPE_CODE_ONOFFLINE); // 상품유형코드 중 온/오프라인
			add(CommonCode.PRDT_TYPE_CODE_ONLY_ONLINE); // 상품유형코드 중 온라인전용
		}
	};

	/**
	 * 런칭상품관련 조건
	 */
	/** 런칭상품조건 - 미적용 */
	public static final String CONDITION_LAUNCH_PRODUCT_NONE = "none";
	/** 런칭상품조건 - 전체(런칭됨+출시예정상품) */
	public static final String CONDITION_LAUNCH_PRODUCT_ALL = "all";
	/** 런칭상품조건 - 런칭됨 (조건 : 6개월 이내 판매시작된 상품, 판매대기중이 아닌 상품, 승인완료 상품) */
	public static final String CONDITION_LAUNCH_PRODUCT_LAUNCHED = "launched";
	/** 런칭상품조건 - 출시예정상품 (조건 : 판매시작되지 않은 상품, 판매대기중인 상품, 승인완료 상품) */
	public static final String CONDITION_LAUNCH_PRODUCT_UPCOMMING = "upcomming";
	/** 런칭상품조건 (기본값 : 미적용) */
	private String conditionLaunchProduct = PageableProduct.CONDITION_LAUNCH_PRODUCT_NONE;

	/** 상품목록 조회 중 할인율 30% 이상인 상품 조회 */
	private boolean conditionDiscountRate30Percent = false;

	/** 날짜 범위 조건 - 년 */
	public static final String CONDITION_DATE_RANGE_YEAR = "YEAR";
	/** 날짜 범위 조건 - 월 */
	public static final String CONDITION_DATE_RANGE_MONTH = "MONTH";
	/** 날짜 범위 조건 - 일 */
	public static final String CONDITION_DATE_RANGE_DAY = "DAY";
	/**
	 * 승인기간 범위 조건<br/>
	 * map key : CONDITION_DATE_RANGE_*<br/>
	 * map value : key에 대한 기간(과거는 음수, 미래는 양수)
	 */
	private Map<String, Integer> conditionApprovedProductWithinDateRange = new HashMap<String, Integer>();

	/** 카테고리 연관 검색 시, 루트 카테고리 기준 연결 */
	public static final String CONDITION_CATEGORY_RELATION_BY_ROOT = "root";
	/** 카테고리 연관 검색 시, 카테고리번호(ctgrNo)기반 연결 */
	public static final String CONDITION_CATEGORY_RELATION_BY_NO = "no";
	/** 카테고리 연관 검색 시, 카테고리이름(ctgrName)기반 연결 */
	public static final String CONDITION_CATEGORY_RELATION_BY_NAME = "name";
	/** 카테고리 연관 검색 시, 카테고리 연결 유형 (기본값 : 카테고리번호기반 연결) */
	private String conditionCategoryRelationBy;
	/** 카테고리 연관 검색 시, 카테고리 연결 유형 값 */
	private String conditionCategoryRelationValue;
	/** 할인율 조건 */
	private Integer conditionDiscountRateGreaterThan;

	/** 판매시작일자 (형식 : yyyyMMdd) */
	private String conditionSellStartDate;

	/** 브랜드번호 */
	private String conditionBrandNo;

	/**
	 * @param parameter
	 */
	public PageableProduct(Parameter<V> parameter) {
		super(parameter);
	}

	/**
	 * @param parameter
	 * @param bean
	 */
	public PageableProduct(Parameter<?> parameter, Bean bean) {
		super(parameter, bean);
	}

	/**
	 * @param parameter
	 * @param clazz
	 */
	public PageableProduct(Parameter<?> parameter, Class<?> clazz) {
		super(parameter, clazz);
	}

	/**
	 * @Desc : 유형 설정. ex) PageableProduct.TYPE_LIST
	 * @Method Name : setType
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @Desc : 기본조건 설정
	 * @Method Name : setDefaultCondition
	 * @Date : 2019. 5. 22.
	 * @Author : tennessee
	 * @param siteNo
	 * @param chnnlNo
	 */
	public void setCondition(String siteNo, String chnnlNo) {
		this.siteNo = siteNo;
		this.chnnlNo = chnnlNo;
	}

	/**
	 * @Desc : 기본 조건 설정
	 * @Method Name : setDefaultCondition
	 * @Date : 2019. 5. 22.
	 * @Author : tennessee
	 * @param siteNo
	 * @param chnnlNo
	 * @param aconnectDispYn
	 */
	public void setCondition(String siteNo, String chnnlNo, String aconnectDispYn) {
		this.siteNo = siteNo;
		this.chnnlNo = chnnlNo;
		this.aconnectDispYn = aconnectDispYn;
	}

	/**
	 * @Desc : 상품 마스터 테이블 외 테이블 사용 여부. TRUE인 경우, 테이블들을 JOIN함
	 * @Method Name : setUseOtherTables
	 * @Date : 2019. 5. 30.
	 * @Author : tennessee
	 * @param useOtherTables
	 */
	public void setUseOtherTables(boolean useOtherTables) {
		this.useOtherTables = useOtherTables;
	}

	/**
	 * @Desc : 테이블매핑 사용 설정
	 * @Method Name : setUseTableMapping
	 * @Date : 2019. 5. 22.
	 * @Author : tennessee
	 * @param mappingTableName
	 * @param conditionColumnString
	 * @param conditionColumnInteger
	 */
	public void setUseTableMapping(String mappingTableName, Map<String, String> conditionColumnString,
			Map<String, Integer> conditionColumnInteger) {
		this.mappingType = MAPPING_TYPE_CUSTOM;
//		this.useTableMapping = true;
		this.mappingTableName = mappingTableName;
		this.conditionColumnString = conditionColumnString;
		this.conditionColumnInteger = conditionColumnInteger;
	}

	/**
	 * @Desc : 테이블매핑 미사용 설정. 상품번호에 의한 선택
	 * @Method Name : setNotUseTableMapping
	 * @Date : 2019. 5. 22.
	 * @Author : tennessee
	 * @param prdtNoList
	 */
	public void setNotUseTableMapping(List<String> prdtNoList) {
		this.mappingType = null;
//		this.useTableMapping = false;
		this.prdtNoList = prdtNoList;
	}

	/**
	 * @Desc : 페이징 사용유무 설정
	 * @Method Name : usePage
	 * @Date : 2019. 5. 27.
	 * @Author : tennessee
	 * @param usePage        사용유무
	 * @param pagingSortType 정렬기준설정. ex) PageableProduct.PAGING_SORT_TYPE_NEWEST
	 * @param sortColumnName 정렬기준이 될 매핑테이블 컬럼명 (매핑테이블 사용 및 커스텀 정렬기준을 사용하는 경우 적용)
	 * @param sortDirection  정렬방향 (매핑테이블 사용 및 커스텀 정렬기준을 사용하는 경우 적용)
	 */
	public void setUsePaging(boolean usePaging, String pagingSortType, String sortColumnName, String sortDirection) {
		this.usePaging = usePaging;
		this.pagingSortType = pagingSortType;
		if (PageableProduct.PAGING_SORT_TYPE_CUSTOM.equals(pagingSortType)) {
			this.sortColumnName = sortColumnName;
			if (UtilsText.isBlank(sortDirection)) {
				this.sortDirection = PageableProduct.SORT_DIRECTION_DESCEND;
			} else {
				this.sortDirection = sortDirection;
			}
		}
	}

	/**
	 * @Desc : 카테고리와 연관된 상품 조회 시 카테고리 번호를 기준으로 조회하도록 설정
	 * @Method Name : setConditionCategoryNo
	 * @Date : 2019. 6. 7.
	 * @Author : tennessee
	 * @param upperCategoryNo 상위카테고리번호
	 */
	public void setConditionCategoryNo(String upperCategoryNo) {
		this.mappingType = MAPPING_TYPE_CATEGORY;
		this.conditionCategoryRelationBy = PageableProduct.CONDITION_CATEGORY_RELATION_BY_NO;
		this.conditionCategoryRelationValue = upperCategoryNo;
	}

	/**
	 * @Desc : 카테고리와 연관된 상품 조회 시 카테고리 이름을 기준으로 조회하도록 설정
	 * @Method Name : setConditionCategoryName
	 * @Date : 2019. 6. 27.
	 * @Author : tennessee
	 * @param upperCategoryName
	 */
	public void setConditionCategoryName(String upperCategoryName) {
		this.mappingType = MAPPING_TYPE_CATEGORY;
		this.conditionCategoryRelationBy = PageableProduct.CONDITION_CATEGORY_RELATION_BY_NAME;
		this.conditionCategoryRelationValue = upperCategoryName;
	}

	public void setConditionCategoryRoot() {
		this.mappingType = MAPPING_TYPE_CATEGORY;
		this.conditionCategoryRelationBy = PageableProduct.CONDITION_CATEGORY_RELATION_BY_ROOT;
		this.conditionCategoryRelationValue = null;
	}

	/**
	 * @Desc : 회원 관심상품(알림/찜상품) 조회
	 * @Method Name : setMemberInterest
	 * @Date : 2019. 5. 29.
	 * @Author : tennessee
	 * @param memberNo       회원번호
	 * @param wrhsAlertReqYn 알림상품:Y, 찜상품:N
	 */
	public void setMemberInterest(String memberNo, String wrhsAlertReqYn) {
		this.memberNo = memberNo;
		this.wrhsAlertReqYn = wrhsAlertReqYn;
	}

	/**
	 * @Desc : 회원번호 반환
	 * @Method Name : getMemberNo
	 * @Date : 2019. 7. 10.
	 * @Author : tennessee
	 * @return
	 */
	public String getMemberNo() {
		return this.memberNo;
	}

	/**
	 * @Desc : 장치 코드 설정
	 * @Method Name : setDeviceCode
	 * @Date : 2019. 5. 29.
	 * @Author : tennessee
	 * @param deviceCode
	 */
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * @Desc : 장치 코드 반환
	 * @Method Name : getDeviceCode
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @return
	 */
	public String getDeviceCode() {
		return this.deviceCode;
	}

	/**
	 * @Desc : 조회 할 상품유형코드를 초기화한 이후 재설정 (초기값 : 온/오프라인, 온라인전용, 오프라인전용)
	 * @Method Name : setPrdtTypeCodes
	 * @Date : 2019. 6. 26.
	 * @Author : tennessee
	 * @param prdtTypeCodes 공통코드객체 내 값으로 설정. (ex. CommonCode.PRDT_TYPE_CODE_*)
	 */
	public void setPrdtTypeCodes(String... prdtTypeCodes) {
		if (UtilsArray.isNotEmpty(prdtTypeCodes)) {
			this.prdtTypeCodes.clear(); // 기존 상품유형코드 제거
			for (String prdtTypeCode : prdtTypeCodes) {
				this.prdtTypeCodes.add(prdtTypeCode);
			}
		}
	}

	/**
	 * @Desc : 런치상품 조건 설정
	 * @Method Name : setConditionLaunchProduct
	 * @Date : 2019. 6. 26.
	 * @Author : tennessee
	 * @param conditionLaunchProduct PageableProduct 객체 내 정적 상수 중
	 *                               CONDITION_LAUNCH_PRODUCT_*
	 */
	public void setConditionLaunchProduct(String conditionLaunchProduct) {
		this.conditionLaunchProduct = conditionLaunchProduct;
	}

	/**
	 * @Desc : 승인기간 범위 조건. (ex. 한달 이내 조회 = "CONDITION_DATE_RANGE_MONTH, -1")
	 * @Method Name : setConditionApprovedProductWithinDateRange
	 * @Date : 2019. 6. 27.
	 * @Author : tennessee
	 * @param dateRangeType  날짜 범위 유형. PageableProduct.CONDITION_DATE_RANGE_*
	 * @param dateRangeValue 날짜 범위 유형에 대한 값. ex) 10, -1
	 */
	public void setConditionApprovedProductWithinDateRange(String dateRangeType, Integer dateRangeValue) {
		this.conditionApprovedProductWithinDateRange.clear();
		this.conditionApprovedProductWithinDateRange.put(dateRangeType, dateRangeValue);
	}

	/**
	 * @Desc : 상품 최대할인율이 주어진 값보다 크거나 같은 상품을 조회하도록 설정
	 * @Method Name : setConditionDiscountRateGreaterThan
	 * @Date : 2019. 6. 27.
	 * @Author : tennessee
	 * @param discountRate
	 */
	public void setConditionDiscountRateGreaterThan(Integer discountRate) {
		this.conditionDiscountRateGreaterThan = discountRate;
	}

	/**
	 * @Desc : 판매시작일 기준 검색
	 * @Method Name : setConditionSellStartDate
	 * @Date : 2019. 6. 28.
	 * @Author : tennessee
	 * @param sellStartDate 판매시작일자. (형식. yyyyMMdd)
	 */
	public void setConditionSellStartDate(String sellStartDate) {
		this.conditionSellStartDate = sellStartDate;
	}

	/**
	 * @Desc : 브랜드 번호 기준 검색
	 * @Method Name : setConditionBrandNo
	 * @Date : 2019. 7. 2.
	 * @Author : tennessee
	 * @param brandNo
	 */
	public void setConditionBrandNo(String brandNo) {
		this.conditionBrandNo = brandNo;
	}

}
