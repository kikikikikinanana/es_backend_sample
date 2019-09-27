package kr.co.shop.web.product.model.master;

import java.io.Serializable;
import java.util.List;

import kr.co.shop.web.cart.model.master.OcCart;
import lombok.Data;

@Data
public class CartProductSearchVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 디바이스 코드 10000 : PC, 10001 : Mobile, 10002 : App */
	private String deviceCode;

	/* 회원 유형 코드 10000: 온라인, 10001: 멤버쉽, 10002: 비회원 */
	private String memberTypeCode;

	/* 회원 등급 코드 10000: 일반, 10001: VIP */
	private String mbshpGradeCode; // 회원등급

	private String empYn; // 임직원여부

	private String chnnlNo; // 채널 번호

	/* 제휴사 코드 */
	private String affltsCode;

	/* 픽업 가능 여부 */
	private String storePickupPsbltYn;

	private List<OcCart> cartPrdtList;

}
