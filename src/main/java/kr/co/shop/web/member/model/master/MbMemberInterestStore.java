package kr.co.shop.web.member.model.master;

import kr.co.shop.common.exception.ValidatorException;
import kr.co.shop.common.message.Message;
import kr.co.shop.common.validation.Validator;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.member.model.master.base.BaseMbMemberInterestStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberInterestStore extends BaseMbMemberInterestStore implements Validator {
	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.shop.common.validation.Validator#validate()
	 */
	@Override
	public void validate() throws ValidatorException {
		if (UtilsText.isBlank(super.getMemberNo())) {
			validationMessage("mypage.valid.variableisnull", "",
					new String[] { Message.getMessage("mypage.valid.memberNo") });
		}
		if (UtilsText.isBlank(super.getSiteNo())) {
			validationMessage("mypage.valid.variableisnull", "",
					new String[] { Message.getMessage("mypage.valid.siteNo") });
		}
	}

}
