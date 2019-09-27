package kr.co.shop.web.display.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.display.model.master.DpDisplayTemplate;
import kr.co.shop.web.display.repository.master.DpDisplayTemplateDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DisplayTemplateService {

	@Autowired
	private DpDisplayTemplateDao dpDisplayTemplateDao;

	public DpDisplayTemplate getDpTemplate(DpDisplayTemplate dpDisplayTemplate) throws Exception {

		dpDisplayTemplate = dpDisplayTemplateDao.selectDpDisplayTemplate(dpDisplayTemplate);

		return dpDisplayTemplate;
	}

}
