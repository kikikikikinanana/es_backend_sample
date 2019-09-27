package kr.co.shop.web.cmm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.cmm.model.master.CmDaysCondtn;
import kr.co.shop.web.cmm.repository.master.CmDaysCondtnDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DaysCondtnService {

	@Autowired
	private CmDaysCondtnDao cmDaysCondtnDao;

	/**
	 * @Desc : 조건날짜 조회
	 * @Method Name : getDaysCondtn
	 * @Date : 2019. 3. 20.
	 * @Author : 이동엽
	 * @param cmDaysCondtn
	 * @return
	 * @throws Exception
	 */
	public CmDaysCondtn getDaysCondtn(CmDaysCondtn cmDaysCondtn) throws Exception {
		return cmDaysCondtnDao.selectDaysCondtn(cmDaysCondtn);
	}
}
