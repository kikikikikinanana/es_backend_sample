package kr.co.shop.web.display.model.master;

import java.util.List;
import java.util.Map;

import kr.co.shop.web.display.model.master.base.BaseDpDisplayPageCorner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpDisplayPageCorner extends BaseDpDisplayPageCorner {

	private DpDisplayPageCornerName dpDisplayPageCornerName;

	private List<DpDisplayPageCornerSet> dpDisplayPageCornerSetList;

	private List<Map<String, Object>> dpDisplayPageCornerSetMapList;

	private Map<String, Object> dpDisplayPageCornerSetMap;

}
