package kr.co.shop.web.cmm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.cmm.model.master.CmArea;
import kr.co.shop.web.cmm.model.master.CmAreaDetail;
import kr.co.shop.web.cmm.repository.master.CmAreaDao;
import kr.co.shop.web.cmm.repository.master.CmAreaDetailDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AreaService {

	@Autowired
	CmAreaDao cmAreaDao;

	@Autowired
	CmAreaDetailDao cmAreaDetailDao;

	/**
	 * @Desc : 지역 리스트 조회 (광역시/도)
	 * @Method Name : getCmAreaList
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @return
	 */
	public List<CmArea> getCmAreaList() throws Exception {

		return cmAreaDao.selectCmAreaList();
	}

	/**
	 * @Desc : 지역 상세 리스트 조회 (시/군/구)
	 * @Method Name : getCmAreaDetailList
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @return
	 */
	public List<CmAreaDetail> getCmAreaDetailList(CmAreaDetail cmAreaDetail) throws Exception {

		return cmAreaDetailDao.selectCmAreaDetailList(cmAreaDetail);
	}

}
