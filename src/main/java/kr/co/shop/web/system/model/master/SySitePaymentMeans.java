package kr.co.shop.web.system.model.master;

import kr.co.shop.web.system.model.master.base.BaseSySitePaymentMeans;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class SySitePaymentMeans extends BaseSySitePaymentMeans {

	// 회원유형코드필드명
	private String memberTypeCodeField;

	// 회원유형코드명
	private String memberTypeCodeName;

	// 결제수단코드필드명
	private String pymntMeansCodeField;

	// 결제수단코드명
	private String pymntMeansCodeName;

	// 결제수단상세코드 : KCP 하위 결제수단 분류
	private String addInfo2;

}
