package kr.co.shop.web.display.model.master;

import java.util.List;
import java.util.Map;

import kr.co.shop.web.display.model.master.base.BaseDpCategoryCorner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DpCategoryCorner extends BaseDpCategoryCorner {

	private DpCategoryCornerName dpCategoryCornerName;

	private List<DpCategoryCornerSet> dpCategoryCornerSetList;

	private List<Map<String, Object>> dpCategoryCornerSetMapList;

	private Map<String, Object> dpCategoryCornerSetMap;

}
