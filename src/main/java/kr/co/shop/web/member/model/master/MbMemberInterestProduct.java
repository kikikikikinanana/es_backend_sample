package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberInterestProduct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberInterestProduct extends BaseMbMemberInterestProduct {

	/** 상태 */
	private String status;

	/** 상품 번호 배열 */
	private String[] prdtNoArr;

	/** 관심상품 여부 */
	private String interestYn;

	/** 옵션명 */
	private String optnName;

	/** 등록일 */
	private String strRgstDtm;
}
