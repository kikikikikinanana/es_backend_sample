package kr.co.shop.web.customer.model.master;

import kr.co.shop.web.customer.model.master.base.BaseCmOnlineMemberPolicy;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString
public class CmOnlineMemberPolicy extends BaseCmOnlineMemberPolicy {
	/**
     * 설명 : 정책상세순번
     */
	private java.lang.Integer plcyDtlSeq;

	/**
     * 설명 : 구매금액
     */
	private java.lang.Integer buyAmt;

    /**
     * 설명 : 구매건수
     */
	private java.lang.Integer buyQty;

}
