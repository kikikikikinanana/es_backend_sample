package kr.co.shop.web.system.model.master;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.shop.web.system.model.master.base.BaseSySiteChnnl;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class SySiteChnnl extends BaseSySiteChnnl {

	private String chnnlUrl;

	@JsonIgnore
	private Boolean pc;

	@JsonIgnore
	private String rgsterNo;

	@JsonIgnore
	private java.sql.Timestamp rgstDtm;

	@JsonIgnore
	private String moderNo;

	@JsonIgnore
	private java.sql.Timestamp modDtm;

}
