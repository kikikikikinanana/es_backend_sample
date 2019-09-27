package kr.co.shop.web.afterService.model.master;

import kr.co.shop.web.afterService.model.master.base.BaseOcAsAcceptProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcAsAcceptProduct extends BaseOcAsAcceptProduct {

	// 회원 번호
	private String memberNo;
	// 주문일
	private String orderDtm;
	// AS신청일
	private String asDtm;
	// 브랜드 명
	private String brandName;
	// 색상코드 명
	private String prdtColorCodeName;
	// 주문금액 str
	private String strOrderAmt;
	// AS구분코드명
	private String asGbnCodeName;
	// AS사유코드명
	private String asRsnCodeName;
	// AS상태코드명
	private String asStatCodeName;
	// list를 page단위로 일부 뽑을때 : Y / list를 전체 뽑을때 : N or null
	private String isPageable;
	// 한 페이지당 로우 겟수
	private java.lang.Integer rowsPerPage;
	// 페이지 번호
	private java.lang.Integer pageNum;
	// 조회조건 날짜 : ~부터
	private String fromDate;
	// 조회조건 날짜 : ~까지
	private String toDate;
	// 링크Target
	private String linkTargetType;
	// 업체번호
	private String vndrNo;
	// 업체상품코드
	private String vndrPrdtNo;

	// 택배사 코드
	private String rtrLogisVndrCodeName;

	// 수선항목
	private String asProcTypeDtlCodeName;

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

}
