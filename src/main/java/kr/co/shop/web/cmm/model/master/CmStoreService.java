package kr.co.shop.web.cmm.model.master;

import kr.co.shop.web.cmm.model.master.base.BaseCmStoreService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CmStoreService extends BaseCmStoreService {

	private String clsPsblt;
	private String clsName;

}