package kr.co.shop.web.system.model.master;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.shop.web.system.model.master.base.BaseSySite;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class SySite extends BaseSySite {

	private String siteUrl;

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
