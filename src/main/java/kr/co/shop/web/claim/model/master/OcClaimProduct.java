package kr.co.shop.web.claim.model.master;

import java.util.List;

import kr.co.shop.web.claim.model.master.base.BaseOcClaimProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcClaimProduct extends BaseOcClaimProduct {

	// 사이트 번호
	private String siteNo;

	// 클레임 구분 코드
	private String clmGbnCode;

	// 회원번호
	private String memberNo;

	// 클레임 일시
	private String clmDtm;

	// 브랜드 명
	private String brandName;

	// 상품컬러코드 명
	private String prdtColorCodeName;

	// 클레임사유코드 명
	private String clmRsnCodeName;

	// 클레임상품상태코드 배열
	private String[] clmPrdtStatCodes;

	// 클레임상품상태코드 명
	private String clmPrdtStatCodeName;

	// 클레임상품상태코드 addinfo2
	private String clmPrdtStatCodeAddInfo2;

	// 사은품 명
	private String giftName;

	// 사은품 상품 순번
	private java.lang.Short orderGiftPrdtSeq;

	// 사은품 코드
	private String prdtTypeCodeGift;

	// 배송비 코드
	private String prdtTypeCodeDelivery;

	// 프로모션유형코드
	private String promoTypeCode;

	// 주문금액 str
	private String strOrderAmt;

	// 상품 판매 가능 flag YN
	private String isSellYn;

	/**
	 * 상품관련파일 칼럼들
	 */
	// 상품관련파일순번
	private java.lang.Integer prdtRltnFileSeq;

	// 파일유형
	private String fileType;

	// 이미지명
	private String imageName;

	// 이미지경로
	private String imagePathText;

	// 이미지 URL
	private String imageUrl;

	// 대체 텍스트
	private String altrnText;

	// 환불대상 주문배송비
	private java.lang.Integer refundDlvyAmt;

	// 클레임 상품 목록
	private List<OcClaimProduct> claimProductList;

	// 옵션변경/재배송상품 : 변경상품옵션번호
	private String changePrdtOptnNo;

	// 옵션변경/재배송상품 : 변경옵션명
	private String changeOptnName;

	// 재배송상품 택배사코드
	private String changeLogisVndrCode;

	// 재배송상품 택배사코드명
	private String changeLogisVndrCodeName;

	// 재배송상품 운송장번호
	private String changeWaybilNoText;

	// 재배송상품 배송상태
	private String changeDlvyStatCodeName;

	// 재배송상품 상태 코드
	private String changeClmPrdtStatCode;

	// 업체별 주문 배송비
	private java.lang.Integer vndrOrderDlvyAmt;
}
