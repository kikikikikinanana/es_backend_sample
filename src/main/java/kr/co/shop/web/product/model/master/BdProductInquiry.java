package kr.co.shop.web.product.model.master;

import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.web.product.model.master.base.BaseBdProductInquiry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BdProductInquiry extends BaseBdProductInquiry {

	/** 진행상태 */
	private String status;

	/** 회원 ID */
	private String loginId;

	/** 답변상태코드명 */
	private String aswrStatCodeName;

	/** 브랜드명 */
	private String brandName;

	/** 브랜드영문명 */
	private String brandEnName;

	/** 상품명 */
	private String prdtName;

	/** 영문상품명 */
	private String engPrdtName;

	/** 상품색상정보 */
	private String prdtColorInfo;

	/** 이미지URL */
	private String imageUrl;

	/** 대체텍스트 */
	private String altrnText;

	/** 상담유형상세코드명 */
	private String cnslTypeDtlCodeName;

	/** 로그인회원번호 */
	private String userMemberNo;

	/** 작성일 문자열 */
	private String strInqryDtm;

	/** 답변일 문자열 */
	private String strAswrDtm;

	// 회원 ID 마스킹 처리
	public String getLoginId() {
		return UtilsMasking.loginId(this.loginId);
	}
}
