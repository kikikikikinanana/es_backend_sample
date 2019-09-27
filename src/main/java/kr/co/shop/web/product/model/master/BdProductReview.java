package kr.co.shop.web.product.model.master;

import kr.co.shop.common.util.UtilsMasking;
import kr.co.shop.web.order.model.master.IfOffDealHistory;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import kr.co.shop.web.product.model.master.base.BaseBdProductReview;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BdProductReview extends BaseBdProductReview {

	/** 상품후기이미지 */
	private BdProductReviewImage[] productReviewImages;

	/** 상품후기평가 */
	private BdProductReviewEvlt[] productReviewEvlts;

	/** 주문상품 */
	private OcOrderProduct orderProduct;

	/** 오프라인주문이력 */
	private IfOffDealHistory offDealHistory;

	/** 품목코드 */
	/** 10000 신발 10001 의류 10002 용품 10003 잡화 */
	private String itemCode;

	/** 정렬 키워드 */
	private String orderKeyword;

	/** 평가점수 */
	private Integer evltScore1; // 만족도
	private Integer evltScore2; // 사이즈, 편의성
	private Integer evltScore3; // 색상, 실용성

	/** 상품후기코드 */
	private String prdtRvwCode1;
	private String prdtRvwCode2;
	private String prdtRvwCode3;

	/** 회원 ID */
	private String loginId;

	/** 상품색상코드명 */
	private String prdtColorCodeName;

	/** 상품옵션번호 */
	private String optnName;

	/** 회원유형코드 */
	private String memberTypeCode;

	/** 주문상품옵션번호 */
	private String prdtOptnNo;

	/** 상품명 */
	private String prdtName;

	/** 작성일 문자열 */
	private String strWriteDtm;

	/** 답변일 문자열 */
	private String strAswrDtm;

	// 회원 ID 마스킹 처리
	public String getLoginId() {
		return UtilsMasking.loginId(this.loginId);
	}

}
