package kr.co.shop.web.mypage.vo;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

/**
 * @Desc : 마이페이지 - 최근 주문내역 VO
 * @FileName : OrderStatVO.java
 * @Project : shop.backend
 * @Date : 2019. 5. 13.
 * @Author : 이강수
 */
@Data
public class OrderStatVO extends BaseBean {

	// 입금대기 상태 건수
	private java.lang.Integer standByCnt;
	// 결제완료 상태 건수
	private java.lang.Integer completeCnt;
	// 상품준비중 상태 건수
	private java.lang.Integer productPreparationCnt;
	// 배송중/픽업준비완료 상태 건수
	private java.lang.Integer dlvyingPickupReadyCnt;
	// 배송/수령완료 건수 상태 건수
	private java.lang.Integer dlvyFinishCnt;

	// 취소 클레임 건수
	private java.lang.Integer clmCancelCnt;
	// 교환 클레임 건수
	private java.lang.Integer clmExchangeCnt;
	// 반품 클레임 건수
	private java.lang.Integer clmReturnCnt;

	/* ****** 주문번호별 갯수 ************************ */
	// 총 주문상품 건수
	private java.lang.Integer orderTotalCnt;
	// 결제완료/입금대기 건수
	private java.lang.Integer orderReadlyCnt;
	// 배송완료
	private java.lang.Integer orderConfirmCnt;
	// 취소완료 건수
	private java.lang.Integer orderCancelCnt;
	// 접수된, 완료된 클레임 건수
	private java.lang.Integer orderClaimTotalCnt;
}
