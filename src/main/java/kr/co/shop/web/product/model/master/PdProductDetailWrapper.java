package kr.co.shop.web.product.model.master;

import java.util.List;

import kr.co.shop.web.vendor.model.master.VdVendorProductAllNotice;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 상품 상세영역 정보 객체
 * @FileName : PdProductDetailWrapper.java
 * @Project : shop.backend
 * @Date : 2019. 5. 28.
 * @Author : tennessee
 */
@Slf4j
@Data
public class PdProductDetailWrapper {

	/** 찾을 상품번호 */
	private String prdtNo;

	/** 입점업체공지 */

	/** 상품 상세 이미지 */
	private List<PdProductRelationFile> file;

	/** 상품 상세 */
	private PdProductDetail detail;

	/** 연관상품 - 추천 */
	/** 연관상품 - 베스트 */
	/** 연관상품 - 연관 */

	/** 사이즈조견표 */

	/** 상품정보고시 */
	private List<PdProductAddInfo> notice;
	/** 취급주의사항 */
	private List<PdProductAddInfo> precaution;
	/** 인증정보 */
	private List<PdProductAddInfo> authority;

	/** 상품전체공지 */
	private List<VdVendorProductAllNotice> allNotice;

}
