package kr.co.shop.web.display.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.util.UtilsText;
import kr.co.shop.web.display.model.master.BdPopup;
import kr.co.shop.web.display.repository.master.BdPopupDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PopupService {

	@Autowired
	private BdPopupDao bdPopupDao;

	/**
	 * @Desc : 팝업 조회
	 * @Method Name : getBdPopup
	 * @Date : 2019. 5. 28.
	 * @Author : 이가영
	 * @param bdPopup
	 * @return
	 */
	public BdPopup getBdPopup(BdPopup bdPopup) throws Exception {

		return bdPopupDao.selectBdPopup(bdPopup);
	}

	/**
	 * @Desc : 팝업 리스트 조회
	 * @Method Name : getBdPopupList
	 * @Date : 2019. 5. 28.
	 * @Author : 이가영
	 * @param bdPopup
	 * @return
	 */
	public List<BdPopup> getBdPopupList(BdPopup bdPopup) throws Exception {

		return bdPopupDao.selectBdPopupList(bdPopup);
	}

	/**
	 * @Desc : 팝업 map 조회
	 * @Method Name : getBdPopupMap
	 * @Date : 2019. 6. 3.
	 * @Author : 이가영
	 * @param bdPopup
	 * @return
	 */
	public Map<String, List<BdPopup>> getBdPopupMap(BdPopup bdPopup) throws Exception {

		Map<String, List<BdPopup>> map = new LinkedHashMap<>();
		List<BdPopup> list = bdPopupDao.selectBdPopupList(bdPopup);

		List<BdPopup> dList = new ArrayList<>();
		List<BdPopup> wList = new ArrayList<>();
		List<BdPopup> nList = new ArrayList<>();

		for (BdPopup item : list) {
			if (UtilsText.equals(item.getDispLimitType(), "D")) {
				dList.add(item);
			} else if (UtilsText.equals(item.getDispLimitType(), "W")) {
				wList.add(item);
			} else {
				nList.add(item);
			}
			;
		}

		map.put("D", dList);
		map.put("W", wList);
		map.put("N", nList);

		return map;
	}

}
