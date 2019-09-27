package kr.co.shop.web.system.model.master;

import kr.co.shop.web.system.model.master.base.BaseSySiteApplySns;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class SySiteApplySns extends BaseSySiteApplySns {

	private String snsChnnlCodeName;

}
