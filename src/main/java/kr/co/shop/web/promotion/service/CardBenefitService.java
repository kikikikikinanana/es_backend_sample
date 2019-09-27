package kr.co.shop.web.promotion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.promotion.model.master.EvCardBenefit;
import kr.co.shop.web.promotion.repository.master.EvCardBenefitDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CardBenefitService {

	@Autowired
	private EvCardBenefitDao cardBenefitDao;

	/**
	 * @Desc : 현재 날짜로 적용중인 카드사 혜택 조회
	 * @Method Name : getApplyingCardBenefitList
	 * @Date : 2019. 5. 23.
	 * @Author : tennessee
	 * @return
	 * @throws Exception
	 */
	public List<EvCardBenefit> getApplyingCardBenefitList() throws Exception {
		return this.cardBenefitDao.selectApplyingCardBenefitList();
	}

}
