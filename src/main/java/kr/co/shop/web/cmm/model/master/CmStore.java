package kr.co.shop.web.cmm.model.master;

import java.util.List;

import kr.co.shop.web.cmm.model.master.base.BaseCmStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CmStore extends BaseCmStore {

	// 매장서비스코드
	private String storeServiceCode;

	// 가능여부 Y/N
	private String psbltYn;

	// 매장유형코드 명
	private String storeTypeCodeName;

	// OTS인지 아닌지 구분하는 Y/N
	private String otsYn;

	private List<CmStoreService> storeServiceList;

	// 검색어
	private String storeSearchWord;

	// 매장별 행사 배너 이미지 URL
	private String eventImageUrl;

	// mobile paging
	private int moPageNum;
	private int moRowsPerPage;
	private String distance;
	private String isPop;
	private boolean isMobile;
	private boolean isExistMyLocation;
	private String myX;
	private String myY;

}
