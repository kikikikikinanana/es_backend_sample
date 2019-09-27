package kr.co.shop.web.order.model.master;

import java.util.List;

import kr.co.shop.web.order.model.master.base.BaseOcOrder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class OcOrder extends BaseOcOrder {

	// 주문상품상태코드
	private String orderPrdtStatCode;

	// 주문상품상태코드 배열
	private String[] orderPrdtStatCodeList;

	// String타입 : 주문일
	private String strOrderDtm;

	// String타입 : 결제금액
	private String strPymntAmt;

	// 주문상품목록 List
	private List<OcOrderProduct> ocOrderProductList;

	// 원 자사, 업체별 주문상품목록 List
	private List<OcOrderProduct> vndrOrgOrderProductList;

	// 자사기준 주문상품목록 List
	private List<OcOrderProduct> mmnyProductList;

	// 주문상품목록 List Size
	private java.lang.Integer ocOrderProductListSize;

	// 상품타입코드 배열
	private String[] prdtTypeCodeList;

	// 업체번호
	private String vndrNo;

	// 로그인 사용자 memberNo
	private String loginMemberNo;

	// 사이트 명
	private String siteName;

	// 회원유형코드 명
	private String memberTypeCodeName;

	// 멤버십등급코드 명
	private String mbshpGradeCodeName;

	// 디바이스코드 명
	private String deviceCodeName;

	// 배송유형코드 명
	private String dlvyTypeCodeName;

	// 주문상태코드 명
	private String orderStatCodeName;

	// 로그인ID
	private String loginId;

	// 클레임구분코드
	private String clmGbnCode;

	// 업체별 배송비
	private java.lang.Integer vndrDlvyAmt;

	// 무료배송기준금액
	private java.lang.Integer freeDlvyStdrAmt;

	// 기준 배송비
	private java.lang.Integer stdrDlvyAmt;

	// 상품유형코드
	private String prdtTypeCode;

	// 날짜 관련
	private String fromDate;
	private String toDate;

	// 주문 기간
	private java.lang.Integer orderPeriod;
	// 클레임 기간
	private java.lang.Integer clmPeriod;

	// 구매확정 여부
	private String buyDcsnYn;

	// 전체 취소 가능 여부
	private String orderCancelAllYn;

	// 오프라인 주문상품목록 List
	private List<IfOffDealHistory> ifOffDealHistoryList;

	// 상품관련파일순번
	private java.lang.Integer prdtRltnFileSeq;

	// 자사상품여부
	private String mmnyPrdtYn;

	// 취소된 배송비 금액
	private java.lang.Integer canceledDlvyAmt;

	// 클레임상품 상태 코드
	private String[] clmPrdtStatCodes;

	// 업체 상품리스트
	private List<OcOrderProduct> vendorProdList;

	// 주문상품상태코드 - 마이페이지선택
	private String orderPrdtStatCodeClick;

	// 포인트
	private java.lang.Long prdtPoint;

	// 비회원 주문번호
	private String nonOrderNo;

	// 적립예정 포인트
	private double totalSavePoint;
}
