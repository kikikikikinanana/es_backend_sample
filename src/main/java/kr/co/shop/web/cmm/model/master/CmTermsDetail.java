package kr.co.shop.web.cmm.model.master;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.shop.web.cmm.model.master.base.BaseCmTermsDetail;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class CmTermsDetail extends BaseCmTermsDetail {

	List<CmTerms> list;

	@JsonIgnore
	private String userId;

	@JsonIgnore
	private String rgsterNo;

	@JsonIgnore
	private java.sql.Timestamp rgstDtm;

	@JsonIgnore
	private String moderNo;

	@JsonIgnore
	private java.sql.Timestamp modDtm;

}
