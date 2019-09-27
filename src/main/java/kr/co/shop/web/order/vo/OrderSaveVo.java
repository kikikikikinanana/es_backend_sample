/**
 * 
 */
package kr.co.shop.web.order.vo;

import java.io.Serializable;
import java.util.List;

import kr.co.shop.web.member.model.master.MbMember;
import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderConvenienceStore;
import kr.co.shop.web.order.model.master.OcOrderDeliveryHistory;
import kr.co.shop.web.order.model.master.OcOrderUseCoupon;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderPayment;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderProduct;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderPromotion;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderTermsAgree;
import lombok.Data;
import lombok.ToString;

/**
 * @Desc : 주문서 저장시 정보를 들고 다닐 VO
 * @FileName : OrderSaveVo.java
 * @Project : shop.backend
 * @Date : 2019. 5. 29.
 * @Author : ljyoung@3top.co.kr
 */
@Data
@ToString(callSuper = true)
public class OrderSaveVo implements Serializable {

	private static final long serialVersionUID = 2510132902638450454L;

	OcOrder ocOrder;

	MbMember orderMember;

	OcOrderConvenienceStore cvsStore;

	List<OrderProduct> prdtList;

	List<OrderProduct> giftPrdtList;

	List<OrderProduct> dlvyPrdtList;

	List<OrderPromotion> prdtPromotionList;

	List<OcOrderUseCoupon> couponList;

	List<OcOrderDeliveryHistory> dlvyList;

	OrderTermsAgree[] termsList;

	OrderPayment mainPayment;

	OrderPayment giftPayment;

	OrderPayment accessPointPayment;

	OrderPayment eventPointPayment;

}
