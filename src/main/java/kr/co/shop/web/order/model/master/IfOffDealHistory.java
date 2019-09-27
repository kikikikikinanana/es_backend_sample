package kr.co.shop.web.order.model.master;

import kr.co.shop.web.order.model.master.base.BaseIfOffDealHistory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class IfOffDealHistory extends BaseIfOffDealHistory {

	// SALE_DATE + DEAL_NO+POS_NO
	private String orderNo;

	// 리뷰
	private String reviewYn;

	// 스토어 네임명
	private String storeName;

	// 로그인한 memberNo
	private String memberNo;

	// 브랜드 이름
	private String brandName;

	// 상품명
	private String prdtName;
    // 이미지 패스
	private String imagePathText;
    // 이미지 url
	private String imageUrl;
    // 이미지 네임
	private String altrnText;
	// 상품 색상 어정보
	private String prdtColorInfo;

}
