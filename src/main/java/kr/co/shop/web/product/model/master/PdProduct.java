package kr.co.shop.web.product.model.master;

import java.util.Date;
import java.util.List;

import kr.co.shop.web.member.model.master.MbMemberInterestProduct;
import kr.co.shop.web.product.model.master.base.BasePdProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 상품 정보
 * @FileName : PdProduct.java
 * @Project : shop.backend
 * @Date : 2019. 5. 29.
 * @Author : tennessee
 */
@Slf4j
@Data
public class PdProduct extends BasePdProduct {

	/** 유형 */
	private String type; // PdProduct.TYPE_SAVE, PdProduct.TYPE_MODIFY, PdProduct.TYPE_TEMPORARY

	/** 첨부 파일 - 상품대표이미지 */
	private List<PdProductRelationFile> productImage;
	/** 첨부 파일 - 상품 추가이미지 및 동영상 */
	private List<PdProductRelationFile> productImageExtra;
	/** 첨부 파일 - 상세설명이미지 */
	private List<PdProductRelationFile> productDetailImage;

	/** 상품상세정보 (PC, MOBILE) */
	private List<PdProductDetail> productDetail;

	/** 전시 카테고리 목록 */
//	private DpCategory[] displayCategoryList;

	/** 상품정보고시 항목 */
//	private List<PdProductAddInfo> productAddInfo;
	/** 상품정보고시 - 정보고시 */
	private List<PdProductAddInfo> productNotice;
	/** 상품정보고시 - 취급주의사항 */
	private List<PdProductAddInfo> productPrecaution;
	/** 상품정보고시 - 인증정보 */
	private List<PdProductAddInfo> productAuthority;

	/** 상품옵션 */
	private List<PdProductOption> productOption;

	/** 관련상품 (색상연계상품) */
	private List<PdRelationProduct> cntcPrdtSetupList;

	/** 관련상품 (관련용품) */
	private List<PdRelationProduct> rltnGoodsSetupList;

	/** 상품 아이콘 */
	private List<CmProductIcon> productIcon;

	/** 상품 색상 코드 */
	private List<PdProductColor> productColor;

	/** 상품 가격 (최근) */
	private PdProductPriceHistory productPrice;

	/** 상품관심여부(회원로그인정보 의존) */
	private List<MbMemberInterestProduct> memberInterest;
	/** 다족구매여부 */
	private Integer promotionCountDiscountMultiShoues;

	/* 기획전 */
	/** 상품 관련 파일 */
	private List<PdProductRelationFile> pdProductRelationFiles;

	private DpBrand brand;

	/** 브랜드명 */
	private String brandName;

	/** 상품이미지url */
	private String imageUrl;

	/** 상품이미지대체텍스트 */
	private String altrnText;
	/* 기획전 */

	/** 판매 종료 Y/N (Y:판매종료) **/
	private String isSellEndYn;

	/** 브랜드스타일 번호 **/
	private String brandStyleSeq;

	public long getSellStartTime() {
		long time = 0;

		if (getSellStartDtm() != null) {
			Date today = new Date();
			time = getSellStartDtm().getTime() - today.getTime();
		}
		return time / 1000;
	}

}
