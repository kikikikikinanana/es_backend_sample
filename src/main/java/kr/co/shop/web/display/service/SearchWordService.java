package kr.co.shop.web.display.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.exception.IRException;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.GroupBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.shop.constant.CommonCode;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.display.model.master.DpSearchWord;
import kr.co.shop.web.display.model.master.DpSearchWordHistory;
import kr.co.shop.web.display.repository.master.DpSearchWordDao;
import kr.co.shop.web.display.repository.master.DpSearchWordHistoryDao;
import kr.co.shop.web.display.vo.SearchVO;
import kr.co.shop.web.system.model.master.SySiteChnnl;
import kr.co.shop.web.system.service.CommonCodeService;
import kr.co.shop.web.system.service.SiteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchWordService {

	@Autowired
	private DpSearchWordDao dpSearchWordDao;

	@Autowired
	private DpSearchWordHistoryDao dpSearchWordHistoryDao;

	@Autowired
	private SiteService siteService;

	@Autowired
	private CommonCodeService commonCodeService;

	@Value("${search.ip}")
	private String SEARCH_IP;

	@Value("${search.port}")
	private String SEARCH_PORT;

	/**
	 * @Desc : 검색어 리스트 조회
	 * @Method Name : getSearchWordList
	 * @Date : 2019. 5. 15.
	 * @Author : 이가영
	 * @param dpSearchWord
	 * @return
	 */
	public Map<String, Object> getSearchWordList(DpSearchWord dpSearchWord) throws Exception {

		Map<String, Object> map = new HashMap<>();

		// 검색창 검색어
		dpSearchWord.setSrchWordGbnType("S");
		List<DpSearchWord> searchBarList = dpSearchWordDao.selectSearchWordList(dpSearchWord);

		int len = searchBarList.size();
		int random = (int) (Math.random() * len);

		map.put("searchBar", searchBarList.get(random));

		// 추천 검색어
		dpSearchWord.setSrchWordGbnType("R");
		List<DpSearchWord> recommendedList = dpSearchWordDao.selectSearchWordList(dpSearchWord);

		map.put("recommended", recommendedList);

		return map;
	}

	/**
	 * @Desc : 인기 검색어 리스트 조회
	 * @Method Name : getPopularSearchWordList
	 * @Date : 2019. 5. 21.
	 * @Author : 이가영
	 * @param dpSearchWordHistory
	 * @return
	 */
	public Map<Integer, DpSearchWord> getPopularSearchWordList(DpSearchWordHistory dpSearchWordHistory)
			throws Exception {

		Map<Integer, DpSearchWord> map = new LinkedHashMap<Integer, DpSearchWord>();
		List<DpSearchWordHistory> list = dpSearchWordHistoryDao.selectPopularSearchWordList(dpSearchWordHistory);

		DpSearchWord dpSearchWord = new DpSearchWord();
		dpSearchWord.setSiteNo(dpSearchWordHistory.getSiteNo());
		dpSearchWord.setSrchWordGbnType("P");
		List<DpSearchWord> newList = dpSearchWordDao.selectSearchWordList(dpSearchWord);

		String[] newArr = UtilsText.isBlank(list.get(0).getRankArr()) ? null : list.get(0).getRankArr().split(",");
		String[] lastArr = UtilsText.isBlank(list.get(1).getRankArr()) ? null : list.get(1).getRankArr().split(",");

		//
		if (newList != null) {
			for (int i = 0; i < newList.size(); i++) {

				String keyword = newList.get(i).getSrchWordText();

				if (lastArr != null) {

					for (int j = 0; j < lastArr.length; j++) {
						if (UtilsText.equals(keyword, lastArr[j])) {
							if (i > j) {
								// 순위가 하락
								newList.get(i).setRank(-1);
								map.put(i, newList.get(i));
								break;
							} else if (i == j) {
								// 동률
								newList.get(i).setRank(0);
								map.put(i, newList.get(i));
								break;
							} else {
								// 순위 상승
								newList.get(i).setRank(1);
								map.put(i, newList.get(i));
								break;
							}
						} else {
							// 순위 상승
							newList.get(i).setRank(1);
							map.put(i, newList.get(i));
						}
					}
				} else {
					break;
				}
			}
		}

		return map;
	}

	/**
	 * @Desc : 스마트 서치 선택
	 * @Method Name : getSmartSearchResult
	 * @Date : 2019. 7. 10.
	 * @Author : 이가영
	 * @param searchParam
	 * @return
	 */
	public HashMap<String, Object> getSmartSearchResult(SearchVO searchVO) throws Exception {

		/** 검색 ip **/
		String searchIP = SEARCH_IP;

		/** 검색 port **/
		int searchPORT = Integer.parseInt(SEARCH_PORT);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> searchParam = mapper.convertValue(searchVO, HashMap.class); /** 파라미터를 담기 위한 map **/

		/** 파라미터 초기화 **/
		String aconnect = (String) searchParam.get("aconnect");
		if (aconnect == null)
			aconnect = "n"; /** a-connect 여부 **/
		String site = (String) searchParam.get("site");
		if (site == null)
			site = ""; /** 사이트 **/

		String gubun[];
		List<String> gubunParamList = (List<String>) searchParam.get("gubun");
		if (gubunParamList != null) {
			gubun = (String[]) gubunParamList.toArray(new String[gubunParamList.size()]);
		} else {
			gubun = new String[] { "" };
		}

		String category[];
		List<String> categoryParamList = (List<String>) searchParam.get("category");
		if (categoryParamList != null) {
			category = (String[]) categoryParamList.toArray(new String[categoryParamList.size()]);
		} else {
			category = new String[] { "" };
		}

		String brand[];
		List<String> brandParamList = (List<String>) searchParam.get("brand");
		if (brandParamList != null) {
			brand = (String[]) brandParamList.toArray(new String[brandParamList.size()]);
		} else {
			brand = new String[] { "" };
		}

		String shoesSize[];
		List<String> shoesSizeParamList = (List<String>) searchParam.get("shoesSize");
		if (shoesSizeParamList != null) {
			shoesSize = (String[]) shoesSizeParamList.toArray(new String[shoesSizeParamList.size()]);
		} else {
			shoesSize = new String[] { "" };
		}

		String menSize[];
		List<String> menSizeParamList = (List<String>) searchParam.get("menSize");
		if (menSizeParamList != null) {
			menSize = (String[]) menSizeParamList.toArray(new String[menSizeParamList.size()]);
		} else {
			menSize = new String[] { "" };
		}

		String womenSize[];
		List<String> womenSizeParamList = (List<String>) searchParam.get("womenSize");
		if (womenSizeParamList != null) {
			womenSize = (String[]) womenSizeParamList.toArray(new String[womenSizeParamList.size()]);
		} else {
			womenSize = new String[] { "" };
		}

		String kidsSize[];
		List<String> kidsSizeParamList = (List<String>) searchParam.get("kidsSize");
		if (kidsSizeParamList != null) {
			kidsSize = (String[]) kidsSizeParamList.toArray(new String[kidsSizeParamList.size()]);
		} else {
			kidsSize = new String[] { "" };
		}

		String color[];
		List<String> colorParamList = (List<String>) searchParam.get("color");
		if (colorParamList != null) {
			color = (String[]) colorParamList.toArray(new String[colorParamList.size()]);
		} else {
			color = new String[] { "" };
		}

		String price[];
		List<String> priceParamList = (List<String>) searchParam.get("price");
		if (priceParamList != null) {
			price = (String[]) priceParamList.toArray(new String[priceParamList.size()]);
		} else {
			price = new String[] { "" };
		}

		String etc[];
		List<String> etcParamList = (List<String>) searchParam.get("etc");
		if (etcParamList != null) {
			etc = (String[]) etcParamList.toArray(new String[etcParamList.size()]);
		} else {
			etc = new String[] { "" };
		}

		/** 반환 데이터 **/
		JSONObject totalResultJson = new JSONObject();

		/** 검색엔진 쿼리 및 결과 초기화 **/
		QuerySet querySet = null;
		SelectSet[] selectSet = null;
		WhereSet[] whereSet = null;
		FilterSet[] filterSet = null;
		GroupBySet[] groupBySet = null;
		Result result = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		GroupResult groupResult = null;
		GroupResult[] groupResultList = null;

		/** a-connect 여부 확인 후 컬렉션 설정 **/
		String collection = aconnect == "n" ? "SEARCH_PRODUCT"
				: aconnect == "y" ? "SEARCH_PRODUCT_ACONNECT" : "SEARCH_PRODUCT";

		/** 쿼리순서 **/
		String[] collectionSequence = new String[] { collection, collection };

		/** 카테고리 구분을 위한 리스트 생성 **/
		@SuppressWarnings("serial")
		HashMap<String, String> categoryMap = new HashMap<String, String>() {
			{
				put("m", "men");
				put("w", "women");
				put("k", "kids");
				put("s", "신발");
				put("c", "의류");
			}
		};

		/** 파라미터로 전달받은 신발 남자 여자 아이 사이즈 리스트 **/
		ArrayList<String> smwkList = new ArrayList<String>();

		if (!shoesSize[0].isEmpty()) {
			smwkList.add(shoesSize[0]);
		}
		if (!menSize[0].isEmpty()) {
			smwkList.add(menSize[0]);
		}
		if (!womenSize[0].isEmpty()) {
			smwkList.add(womenSize[0]);
		}
		if (!kidsSize[0].isEmpty()) {
			smwkList.add(kidsSize[0]);
		}

		/** 쿼리 셋 **/
		querySet = new QuerySet(collectionSequence.length);

		/** 첫번째 : 전체목록 | 두번째 : 선택목록 **/
		for (int s = 0; s < collectionSequence.length; s++) {

			/** SelectSet **/
			selectSet = new SelectSet[] { new SelectSet("PRDT_NO", Protocol.SelectSet.NONE) /** ID **/
			};

			/** WhereSet LIST **/
			ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();

			/** 사이트 **/
			whereSetList.add(new WhereSet("IDX_SITE_NO", Protocol.WhereSet.OP_HASALL, site, 1));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));

			/** 전체일때 **/
			if (s == 0) {

				/** 채널 전체 **/
				whereSetList.add(
						new WhereSet("IDX_CHNNL_NO", Protocol.WhereSet.OP_HASANY, new String("10000 10001 10002"), 1));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				/** 카테고리 전체 **/
				whereSetList.add(new WhereSet("IDX_CTGR_NO", Protocol.WhereSet.OP_HASANY,
						new String("1000000001 1000000081 1000000165"), 1));
			}

			/** 선택일때 **/
			if (s == collectionSequence.length - 1) {

				String productGubun = "";

				/** 상품구분 띄어쓰기 구분자로 하여 문자열 생성 **/
				for (int g = 0; g < gubun.length; g++) {
					if (g == 0) {
						productGubun = gubun[g];
					} else if (g > 0) {
						productGubun = productGubun + " " + gubun[g];
					}
				}

				/** 채널 **/
				whereSetList.add(new WhereSet("IDX_CHNNL_NO", Protocol.WhereSet.OP_HASANY, productGubun, 1));

				/** 카테고리 **/
				if (!category[0].isEmpty()) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int c = 0; c < category.length; c++) {
						whereSetList.add(new WhereSet("IDX_CTGR_NO", Protocol.WhereSet.OP_HASALL, category[c], 1));
						if (c < category.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				/** 브랜드 **/
				if (!brand[0].isEmpty()) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int b = 0; b < brand.length; b++) {
						whereSetList.add(new WhereSet("IDX_BRAND_NO", Protocol.WhereSet.OP_HASALL, brand[b], 1));
						if (b < brand.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				/** 신발 남자 여자 아이 중 선택이 2개 이상일 때 **/
				if (smwkList.size() > 1) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				}

				/** 신발 사이즈 **/
				if (!shoesSize[0].isEmpty()) {

					/** 신발 남자 여자 아이 중 선택이 1개 일 때 **/
					if (smwkList.size() == 1) {
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					}

					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int ss = 0; ss < shoesSize.length; ss++) {
						whereSetList.add(
								new WhereSet("IDX_PRDT_OPTION_INLINE", Protocol.WhereSet.OP_HASALL, shoesSize[ss], 1));
						if (ss < shoesSize.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					/** 신발 남자 여자 아이 중 선택이 2개 이상일 때 **/
					if (smwkList.size() > 1) {
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					}
				}

				/** 남성의류 사이즈 **/
				if (!menSize[0].isEmpty()) {

					/** 신발 남자 여자 아이 중 선택이 1개 일 때 **/
					if (smwkList.size() == 1) {
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereSetList
							.add(new WhereSet("IDX_CTGR_NAME", Protocol.WhereSet.OP_HASALL, categoryMap.get("m"), 1));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int ms = 0; ms < menSize.length; ms++) {
						whereSetList.add(
								new WhereSet("IDX_PRDT_OPTION_INLINE", Protocol.WhereSet.OP_HASALL, menSize[ms], 1));
						if (ms < menSize.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					/** 신발 남자 여자 아이 중 선택이 2개 이상일 때 **/
					if (smwkList.size() > 1) {
						if (shoesSize[0].isEmpty() || !womenSize[0].isEmpty() || !kidsSize[0].isEmpty()) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
				}

				/** 여성의류 사이즈 **/
				if (!womenSize[0].isEmpty()) {

					/** 신발 남자 여자 아이 중 선택이 1개 일 때 **/
					if (smwkList.size() == 1) {
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereSetList
							.add(new WhereSet("IDX_CTGR_NAME", Protocol.WhereSet.OP_HASALL, categoryMap.get("w"), 1));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int ws = 0; ws < womenSize.length; ws++) {
						whereSetList.add(
								new WhereSet("IDX_PRDT_OPTION_INLINE", Protocol.WhereSet.OP_HASALL, womenSize[ws], 1));
						if (ws < womenSize.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					/** 신발 남자 여자 아이 중 선택이 2개 이상일 때 **/
					if (smwkList.size() > 1) {
						if (!kidsSize[0].isEmpty()) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
				}

				/** 아동의류 사이즈 **/
				if (!kidsSize[0].isEmpty()) {

					/** 신발 남자 여자 아이 중 선택이 1개 일 때 **/
					if (smwkList.size() == 1) {
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereSetList
							.add(new WhereSet("IDX_CTGR_NAME", Protocol.WhereSet.OP_HASALL, categoryMap.get("k"), 1));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int ks = 0; ks < kidsSize.length; ks++) {
						whereSetList.add(
								new WhereSet("IDX_PRDT_OPTION_INLINE", Protocol.WhereSet.OP_HASALL, kidsSize[ks], 1));
						if (ks < kidsSize.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				/** 신발 남자 여자 아이 중 선택이 2개 이상일 때 **/
				if (smwkList.size() > 1) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				/** 색상 **/
				if (!color[0].isEmpty()) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int c = 0; c < color.length; c++) {
						whereSetList.add(new WhereSet("IDX_COLOR_ID", Protocol.WhereSet.OP_HASALL, color[c], 1));
						if (c < color.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				/** ETC **/
				if (!etc[0].isEmpty()) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					for (int e = 0; e < etc.length; e++) {
						whereSetList.add(new WhereSet("IDX_ETC", Protocol.WhereSet.OP_HASALL, etc[e], 1));
						if (e < etc.length - 1) {
							whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						}
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}
			}

			/** WhereSet **/
			whereSet = new WhereSet[whereSetList.size()];
			for (int a = 0; a < whereSetList.size(); a++) {
				whereSet[a] = whereSetList.get(a);
			}

			/** 가격 정보가 있을 때 **/
			if (s == collectionSequence.length - 1 && !price[0].isEmpty()) {
				/** FilterSet **/
				filterSet = new FilterSet[] {
						new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_PRDT_DC_PRICE", price, 1) };
			}

			/** GroupBySet **/
			groupBySet = new GroupBySet[] {
					new GroupBySet("GROUP_BRAND_EN_NAME",
							(byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC"),
					new GroupBySet("GROUP_PRDT_OPTION",
							(byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC"),
					new GroupBySet("GROUP_COLOR_ID",
							(byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC"),
					new GroupBySet("GROUP_PRDT_DC_PRICE",
							(byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC"),
					new GroupBySet("GROUP_ETC", (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME),
							"ASC") };

			Query query = new Query(); /** 쿼리 생성 **/
			query.setSelect(selectSet); /** Select 생성 **/
			query.setFrom(collectionSequence[s]); /** From 생성 **/
			query.setWhere(whereSet); /** Where 생성 **/
			query.setGroupBy(groupBySet); /** Group 생성 **/
			if (!price[0].isEmpty()) { /** 가격이 있을 때만 **/
				query.setFilter(filterSet); /** Filter 생성 **/
			}
			query.setDebug(true); /** Debug 로그 생성 여부 **/
			query.setPrintQuery(false); /** PrintQuery 로그 생성 여부 **/
			query.setResult(0, 1); /** 페이지 당 결과 갯수 설정 **/
			querySet.addQuery(query); /** 쿼리셋에 추가 **/
			/* QueryParser parser = new QueryParser(); *//** 쿼리로그 선언 **//*
																		 * System.out.println(s == 0 ? "전체쿼리 : " +
																		 * parser.queryToString(query) : "선택쿼리 : " +
																		 * parser.queryToString(query));
																		 */
		}

		/** 검색 서버로 검색 쿼리 전송 **/
		int returnCode = 0; /** 전송코드 **/
		command = new CommandSearchRequest(searchIP, searchPORT); /** 검색서버로 결과요청 **/
		returnCode = command.request(querySet); /** 요청 후 결과코드 **/
		System.out.println("스마트서치 전송결과코드 : " + returnCode + " | 전송결과 : " + (returnCode == 1 ? "성공" : "실패"));

		/** 전송 성공 **/
		if (returnCode > 0) {
			ResultSet results = command.getResultSet(); /** 결과셋 **/
			resultList = results.getResultList(); /** 결과 리스트 **/
			String[] groupList = new String[] { "BRAND_LIST", "SIZE_LIST", "COLOR_LIST", "PRICE_LIST",
					"ETC_LIST" }; /** 그룹별 결과 **/
			String[] sizeList = new String[] { "SHOES_SIZE", "MEN_SIZE", "WOMEN_SIZE", "KIDS_SIZE" }; /** 사이즈별 결과 **/
			String[] groupPart = new String[] { "ALL", "SELECT" }; /** 전체 | 선택 **/

			if (resultList.length > 0) {

				/** 결과 리스트 - 전체목록 | 선택목록 **/
				for (int i = 0; i < resultList.length; i++) {
					result = resultList[i]; /** 결과 받기 **/
					groupResultList = result.getGroupResults(); /** 그룹결과 받기 **/
					JSONObject groupJson = new JSONObject(); /** 그룹별 결과 **/

					System.out.println(
							i == 0 ? "전체결과갯수 : " + result.getTotalSize() : "선택결과갯수 : " + result.getTotalSize());

					/** 결과별 그룹 **/
					for (int j = 0; j < groupResultList.length; j++) {
						groupResult = groupResultList[j];

						/** 그룹별 리스트 **/
						ArrayList<String> eachGroupList = new ArrayList<String>();

						/** 그룹이 가격일 때 **/
						if (groupList[j].equals("PRICE_LIST")) {
							ArrayList<Integer> priceGroupList = new ArrayList<Integer>();

							/** 숫자 정렬을 위해 Integer 타입으로 변경하여 담기 **/
							for (int g = 0; g < groupResult.groupResultSize(); g++) {
								priceGroupList.add(Integer.parseInt(new String(groupResult.getId(g))));
							}

							/** 오름차순 정렬 **/
							Collections.sort(priceGroupList);

							/** 최대값 최소값만 가져온다 **/
							if (priceGroupList.size() > 0) {
								eachGroupList.add(String.valueOf(priceGroupList.get(0)));
								eachGroupList.add(String.valueOf(priceGroupList.get(priceGroupList.size() - 1)));
							}
							/** 다른 그룹일 때 **/
						} else {
							for (int g = 0; g < groupResult.groupResultSize(); g++) {
								eachGroupList.add(new String(groupResult.getId(g)));
							}
						}

						/** 사이즈는 신발 남자 여자 아이로 구분하여 리스트로 생성 **/
						if (groupList[j].equals("SIZE_LIST")) {

							/** 중복값 없이 담기 위한 각각 사이즈 set 초기화 **/
							HashSet<String> shoesSizeSet = new HashSet<String>();
							HashSet<String> menSizeSet = new HashSet<String>();
							HashSet<String> womenSizeSet = new HashSet<String>();
							HashSet<String> kidsSizeSet = new HashSet<String>();

							/** 사이즈 섞여있음 **/
							for (int s = 0; s < eachGroupList.size(); s++) {

								/** 구분자 형태를 배열로 받기위해 초기화 **/
								String[] shoesSizeArr, menSizeArr, womenSizeArr, kidsSizeArr = null;

								/** 남성 상품 사이즈 **/
								if (eachGroupList.get(s).substring(0, 1).equals("M")
										&& eachGroupList.get(s).substring(2, 4).equals(categoryMap.get("c"))) {
									menSizeArr = (eachGroupList.get(s).substring(5, eachGroupList.get(s).length()))
											.trim().split(",");
									for (int m = 0; m < menSizeArr.length; m++) {
										menSizeSet.add(menSizeArr[m]);
									}
								}
								/** 여성 상품 사이즈 **/
								else if (eachGroupList.get(s).substring(0, 1).equals("W")
										&& eachGroupList.get(s).substring(2, 4).equals(categoryMap.get("c"))) {
									womenSizeArr = (eachGroupList.get(s).substring(5, eachGroupList.get(s).length()))
											.trim().split(",");
									for (int w = 0; w < womenSizeArr.length; w++) {
										womenSizeSet.add(womenSizeArr[w]);
									}
								}
								/** 아동 상품 사이즈 **/
								else if (eachGroupList.get(s).substring(0, 1).equals("K")
										&& eachGroupList.get(s).substring(2, 4).equals(categoryMap.get("c"))) {
									kidsSizeArr = (eachGroupList.get(s).substring(5, eachGroupList.get(s).length()))
											.trim().split(",");
									for (int k = 0; k < kidsSizeArr.length; k++) {
										kidsSizeSet.add(kidsSizeArr[k]);
									}
								}
								/** 신발 사이즈 **/
								else if (eachGroupList.get(s).substring(2, 4).equals(categoryMap.get("s"))) {
									shoesSizeArr = (eachGroupList.get(s).substring(5, eachGroupList.get(s).length()))
											.trim().split(",");
									for (int ss = 0; ss < shoesSizeArr.length; ss++) {
										shoesSizeSet.add(shoesSizeArr[ss]);
									}
								}
							}

							/** 남성, 여성, 아동, 신발 사이즈 리스트를 담을 총 리스트 **/
							ArrayList<ArrayList<String>> totalSizeList = new ArrayList<ArrayList<String>>();

							/** 남성, 여성, 아동, 신발 사이즈를 담을 개별 리스트 **/
							ArrayList<String> shoesSizeList = new ArrayList<String>(shoesSizeSet);
							ArrayList<String> menSizeList = new ArrayList<String>(menSizeSet);
							ArrayList<String> womenSizeList = new ArrayList<String>(womenSizeSet);
							ArrayList<String> kidsSizeList = new ArrayList<String>(kidsSizeSet);

							/** 전체일때 **/
							if (i == 0) {

								/** 정렬 **/
								Collections.sort(shoesSizeList);
								Collections.sort(menSizeList);
								Collections.sort(womenSizeList);
								Collections.sort(kidsSizeList);

								/** 중복값이 제거된 리스트를 순차적으로 담기 **/
								totalSizeList.add(shoesSizeList);
								totalSizeList.add(menSizeList);
								totalSizeList.add(womenSizeList);
								totalSizeList.add(kidsSizeList);
							}

							/** 선택일때 **/
							if (i > 0) {

								/** 중복값이 제거된 리스트에서 선택된 값만 담기 **/
								ArrayList<String> selectShoesSizeList = new ArrayList<String>();
								ArrayList<String> selectMenSizeList = new ArrayList<String>();
								ArrayList<String> selectWomenSizeList = new ArrayList<String>();
								ArrayList<String> selectKidsSizeList = new ArrayList<String>();

								/** 신발을 선택했을 때 **/
								if (!shoesSize[0].isEmpty()) {
									for (String sa : shoesSize) {
										for (String sl : shoesSizeList) {
											if (sa.equals(sl)) {
												selectShoesSizeList.add(sl);
											}
										}
									}
									Collections.sort(selectShoesSizeList);
									totalSizeList.add(selectShoesSizeList);
									/** 신발을 선택하지 않았을 때 **/
								} else {
									Collections.sort(shoesSizeList);
									totalSizeList.add(shoesSizeList);
								}

								/** 남성 사이즈를 선택했을 때 **/
								if (!menSize[0].isEmpty()) {
									for (String sa : menSize) {
										for (String sl : menSizeList) {
											if (sa.equals(sl)) {
												selectMenSizeList.add(sl);
											}
										}
									}
									Collections.sort(selectMenSizeList);
									totalSizeList.add(selectMenSizeList);

									/** 남성 사이즈를 선택하지 않았을 때 **/
								} else {
									Collections.sort(menSizeList);
									totalSizeList.add(menSizeList);
								}

								/** 여성 사이즈를 선택했을 때 **/
								if (!womenSize[0].isEmpty()) {
									for (String sa : womenSize) {
										for (String sl : womenSizeList) {
											if (sa.equals(sl)) {
												selectWomenSizeList.add(sl);
											}
										}
									}
									Collections.sort(selectWomenSizeList);
									totalSizeList.add(selectWomenSizeList);

									/** 여성 사이즈를 선택하지 않았을 때 **/
								} else {
									Collections.sort(womenSizeList);
									totalSizeList.add(womenSizeList);
								}

								/** 아동 사이즈를 선택했을 때 **/
								if (!kidsSize[0].isEmpty()) {
									for (String sa : kidsSize) {
										for (String sl : kidsSizeList) {
											if (sa.equals(sl)) {
												selectKidsSizeList.add(sl);
											}
										}
									}
									Collections.sort(selectKidsSizeList);
									totalSizeList.add(selectKidsSizeList);

									/** 아동 사이즈를 선택하지 않았을 때 **/
								} else {
									Collections.sort(kidsSizeList);
									totalSizeList.add(kidsSizeList);
								}
							}

							/** 반환데이터에 구분하여 담기 **/
							for (int t = 0; t < totalSizeList.size(); t++) {
								groupJson.put(sizeList[t], totalSizeList.get(t));
							}

							/** etc결과를 반환데이터에 담기 **/
						} else if (groupList[j].equals("ETC_LIST")) {

							ArrayList<String> etcList = new ArrayList<String>();
							/** 전체일때 **/
							if (i > 0 && !etc[0].isEmpty()) {
								for (String el : eachGroupList) {
									for (String ea : etc) {
										if (el.equals(ea)) {
											etcList.add(ea);
										}
									}
								}
								groupJson.put(groupList[j], etcList);
								/** 선택이거나 선택이 없을 때 **/
							} else if ((i > 0 && etc[0].isEmpty()) || i == 0) {
								groupJson.put(groupList[j], eachGroupList);
							}

							/** brand결과를 반환데이터에 담기 **/
						} else if (groupList[j].equals("BRAND_LIST")) {

							/** 마지막 공백제거 **/
							for (int b = 0; b < eachGroupList.size(); b++) {
								eachGroupList.set(b,
										eachGroupList.get(b).substring(0, eachGroupList.get(b).length() - 1));
							}
							groupJson.put(groupList[j], eachGroupList);

							/** color결과를 반환데이터에 담기 **/
						} else if (groupList[j].equals("COLOR_LIST")) {

							ArrayList<String> colorList = new ArrayList<String>();
							/** 전체일때 **/
							if (i > 0 && !color[0].isEmpty()) {
								for (String cl : eachGroupList) {
									for (String ca : color) {
										if (cl.equals(ca.toUpperCase())) {
											colorList.add(ca);
										}
									}
								}
								groupJson.put(groupList[j], colorList);

								/** 선택이거나 선택이 없을 때 **/
							} else if ((i > 0 && color[0].isEmpty()) || i == 0) {
								groupJson.put(groupList[j], eachGroupList);
							}

							/** 다른 그룹결과를 반환데이터에 구분하여 담기 **/
						} else {
							groupJson.put(groupList[j], eachGroupList);
						}
					}
					/** 반환데이터에 전체 | 선택을 구분하여 담기 **/
					totalResultJson.put(groupPart[i], groupJson);
				}
			}
		} else {
			resultList = new Result[1];
			resultList[0] = new Result();
		}

		HashMap<String, Object> map = new ObjectMapper().convertValue(totalResultJson, HashMap.class);

		SySiteChnnl sySiteChnnl = new SySiteChnnl();
		sySiteChnnl.setSiteNo((String) searchParam.get("site"));
		sySiteChnnl.setPc(true);
		map.put("CHNNL_LIST", siteService.getUseChannelListBySiteNo(sySiteChnnl));

		map.put("THEME_LIST", commonCodeService.getUseCode(CommonCode.GENDER_GBN_CODE));

		return map;
	}

	/**
	 * @Desc : 자동 완성 검색어 리스트 조회
	 * @Method Name : getAutoComplete
	 * @Date : 2019. 7. 10.
	 * @Author : 이가영
	 * @param searchParam
	 * @return
	 */
	public List<String> getAutoComplete(SearchVO searchVO) throws IRException {

		/** 검색 ip **/
		String searchIP = SEARCH_IP;

		/** 검색 port **/
		int searchPORT = Integer.parseInt(SEARCH_PORT);

		/** 검색어 **/
		String searchWord = searchVO.getSearchWord();

		/** 검색엔진 쿼리 및 결과 초기화 **/
		Result result = null;
		Result[] resultList = null;

		/** 반환 데이터 **/
		JSONObject totalResultJson = new JSONObject();

		/** 컬렉션 명 **/
		String collection = "AUTOCOMPLETE";

		/** 쿼리셋 **/
		QuerySet querySet = new QuerySet(1);

		/** 검색어 하이라이트 **/
		char[] startTag = "<span class=\"spot\">".toCharArray();
		char[] endTag = "</span>".toCharArray();

		/** SelectSet **/
		SelectSet[] selectSet = new SelectSet[] {
				new SelectSet("KEYWORDS", (byte) Protocol.SelectSet.HIGHLIGHT) /** 검색어 **/
		};

		/** WhereSet **/
		WhereSet[] whereSet = new WhereSet[] {
				new WhereSet("IDX_KEYWORDS", Protocol.WhereSet.OP_HASALL, searchWord, 1) /** 검색어 **/
		};

		Query query = new Query(startTag, endTag); /** 쿼리 생성 **/
		query.setSelect(selectSet); /** Select 생성 **/
		query.setFrom(collection); /** From 생성 **/
		query.setWhere(whereSet); /** Where 생성 **/
		query.setDebug(true); /** Debug 로그 생성 여부 **/
		query.setPrintQuery(false); /** PrintQuery 로그 생성 여부 **/
		query.setLoggable(false); /** 로그에 남길 검색어 명시 여부 **/
		query.setResult(0, 9); /** 페이지 당 결과 갯수 설정 **/
		querySet.addQuery(query); /** 쿼리셋에 추가 **/

		/** 검색 서버로 검색 정보 전송 **/
		int returnCode = 0;
		CommandSearchRequest command = new CommandSearchRequest(searchIP, searchPORT);
		returnCode = command.request(querySet);
		log.debug("자동완성 전송결과코드 : " + returnCode + " | 전송결과 : " + (returnCode == 1 ? "성공" : "실패"));

		if (returnCode > 0) {
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
			if (resultList.length > 0) {
				for (int i = 0; i < resultList.length; i++) {
					result = resultList[i];
					List<String> autoCompleteList = new ArrayList<String>();
					for (int row = 0; row < result.getRealSize(); row++) {
						autoCompleteList.add(new String(result.getResult(row, 0)));
					}
					totalResultJson.put("AUTOCOMPLETE", autoCompleteList);
				}
			}
		} else {
			resultList = new Result[1];
			resultList[0] = new Result();
		}

		List<String> list = (List<String>) totalResultJson.get("AUTOCOMPLETE");

		return list;
	}

}