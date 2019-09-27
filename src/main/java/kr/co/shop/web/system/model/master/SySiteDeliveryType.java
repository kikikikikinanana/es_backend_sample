package kr.co.shop.web.system.model.master;

import kr.co.shop.web.system.model.master.base.BaseSySiteDeliveryType;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class SySiteDeliveryType extends BaseSySiteDeliveryType {

	// 배송유형코드명
	private String dlvyTypeCodeName;

}
