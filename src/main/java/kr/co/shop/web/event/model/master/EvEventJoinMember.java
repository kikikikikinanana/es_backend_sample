package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEventJoinMember;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventJoinMember extends BaseEvEventJoinMember {

	private String limitType;

	private String recentYn;

	private java.sql.Timestamp przwrOrderStartYmd;

	private java.sql.Timestamp przwrOrderEndYmd;

	private java.sql.Timestamp pickupStartYmd;

	private java.sql.Timestamp pickupEndYmd;

	private String prdtRecptStoreType;

	private String onlnRecptYn;

	private String loginIdDupPermYn;

}
