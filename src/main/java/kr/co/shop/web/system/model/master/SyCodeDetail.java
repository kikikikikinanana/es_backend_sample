package kr.co.shop.web.system.model.master;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.shop.web.system.model.master.base.BaseSyCodeDetail;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class SyCodeDetail extends BaseSyCodeDetail {

	@JsonIgnore
	private String rgsterNo;

	@JsonIgnore
	private String moderNo;

}
