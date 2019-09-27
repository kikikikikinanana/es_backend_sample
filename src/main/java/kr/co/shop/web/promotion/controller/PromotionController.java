package kr.co.shop.web.promotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.promotion.model.master.EvCardBenefit;
import kr.co.shop.web.promotion.service.CardBenefitService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc : 프로모션 컨트롤러
 * @FileName : PromotionController.java
 * @Project : shop.backend
 * @Date : 2019. 7. 5.
 * @Author : tennessee
 */
@Slf4j
@Controller
@RequestMapping("api")
public class PromotionController extends BaseController {

	@Autowired
	private CardBenefitService cardBenefitService;

	/**
	 * @Desc : 적용중인 카드 혜택 조회
	 * @Method Name : getApplyingCardBenefitList
	 * @Date : 2019. 5. 23.
	 * @Author : tennessee
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "현재 날짜로 적용중인 카드 혜택 조회", notes = "현재 날짜로 적용중인 카드 혜택 조회", httpMethod = "GET", protocols = "http", response = EvCardBenefit.class)
	@GetMapping(value = "v1.0/promotion/card/benefit/applying", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getApplyingCardBenefitList(Parameter<?> parameter) throws Exception {
		log.debug("카드 혜택 조회");
		return UtilsREST.responseOk(parameter, this.cardBenefitService.getApplyingCardBenefitList());
	}

}