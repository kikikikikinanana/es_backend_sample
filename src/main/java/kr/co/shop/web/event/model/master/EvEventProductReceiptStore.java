package kr.co.shop.web.event.model.master;

import kr.co.shop.web.event.model.master.base.BaseEvEventProductReceiptStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class EvEventProductReceiptStore extends BaseEvEventProductReceiptStore {

	private String storeName;

}
