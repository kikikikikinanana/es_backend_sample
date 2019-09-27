package kr.co.shop.web.order.model.master;

import java.util.List;

import kr.co.shop.web.order.model.master.base.BaseOcOrderProduct;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockOnlyOne;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcOrderProduct extends BaseOcOrderProduct {

	// 회원번호
	private String memberNo;

	// 원 주문번호
	private String orgOrderNo;

	// 브랜드 명
	private String brandName;

	// 상품색상코드 명
	private String prdtColorCodeName;

	// 페이지당로우갯수
	private java.lang.Integer rowsPerPage;

	// 페이지 번호
	private java.lang.Integer pageNum;

	// 조회조건 날짜 : ~부터
	private String fromDate;

	// 조회조건 날짜 : ~까지
	private String toDate;

	// 주문자명
	private String buyerName;

	// 주문자핸드폰번호
	private String buyerHdphnNoText;

	// 주문자우편번호
	private String buyerPostCodeText;

	// 주문자우편주소
	private String buyerPostAddrText;

	// 주문자상세주소
	private String buyerDtlAddrText;

	// 회수택배사코드
	private String logisVndrCode;

	// 회수택배사코드
	private String logisVndrCodeName;

	// 회수송장번호
	private String rtrvlWaybilNoText;

	// 송장번호
	private String waybilNoText;

	// 배송유형코드
	private String dlvyTypeName;

	// 배송ID
	private String dlvyIdText;

	// 메일 주소
	private String buyerEmailAddrText;

	// 배송유형코드 : 편의점픽업
	private String dlvyTypeCodeConveniencePickup;

	// 배송유형코드 : 매장픽업
	private String dlvyTypeCodeStorePickup;

	// 사이트 관리에 배송비
	private String asDlvyAmt;

	// 주문 결제금액 (천단위 , )
	private String strPymntAmt;

	// 주문일
	private String orderDtm;

	// 주문금액 (천단위 , )
	private String strOrderAmt;

	// list를 page단위로 일부 뽑을때 : Y / list를 전체 뽑을때 : N or null
	private String isPageable;

	// 주문상태코드 명
	private String orderStatCodeName;

	// 주문상품상태코드 명
	private String orderPrdtStatCodeName;

	// 주문상품순번 배열
	private java.lang.Short[] orderPrdtSeqs;

	// 백엔드에 조회를 원하는 주문상품상태코드 배열
	private String[] orderPrdtStatCodeList;

	// 상품타입코드 배열
	private String[] prdtTypeCodeList;

	// 사은품 명
	private String giftName;

	// 사은품 코드
	private String prdtTypeCodeGift;

	// 사은품 주문상품순번
	private java.lang.Short orderGiftPrdtSeq;

	// 루프플래그
	private String loopflag;
	private String giftPrdtName;
	private String giftPrdtNo;
	private String imagePathText;
	private String imageUrl;
	private String dlvyAmt;

	// 옵션변경 : 변경상품옵션번호
	private String changePrdtOptnNo;

	// 옵션변경 : 변경옵션명
	private String changeOptnName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : SAFE_KEY
	 */
	private String safeKey;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : SAFE_KEY_SEQ
	 */
	private String safeKeySeq;

	// 상품관련파일순번
	private java.lang.Integer prdtRltnFileSeq;

	// 이미지명
	private String imageName;

	// 대체텍스트
	private String altrnText;

	// 파일유형
	private String fileType;

	// 설명 : 상품번호+상품옵션번호
	private String prdtNoPrdtOptnNo;
	private String stockGbnCode;

	private int aiQty; // AI 재고수량
	private int asQty; // AS 재고수량
	private int awQty; // AW 재고 수량

	// 상품옵션 테이블 조회 모델
	private PdProductOptionWithStockOnlyOne pdProductOptionWithStockOnlyOne;

	private List<OcOrderProduct> vendorProdList;

	private java.lang.Integer freeDlvyStdrAmt;

	private java.lang.Integer vendorProdCnt;

	// 픽업 가능 일자
	private String pickupPsbltYmd;

	// 예약 출고 일자
	private String rsvDlvyDtm;

	// 배송상태 코드
	private String dlvyStatCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 주문상품상태코드
	 */
	private String whereOrderPrdtStatCode;

	// 리뷰
	private String reviewYn;

	// 주문상품상태코드 - 마이페이지선택
	private String orderPrdtStatCodeClick;

	private java.lang.Long prdtPoint;

	// 사은품_배송비 제외 여부 플래그
	private String giftDeliveryWhereYn;

	// 매출취소구분
	private String salesCnclGbnType;

	// 클레임번호
	private String clmNo;

}
