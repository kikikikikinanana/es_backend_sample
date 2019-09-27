package kr.co.shop.web.member.model.master;

import java.util.Map;

import kr.co.shop.common.exception.ValidatorException;
import kr.co.shop.common.message.Message;
import kr.co.shop.common.validation.Validator;
import kr.co.shop.util.UtilsText;
import kr.co.shop.web.event.model.master.EvEvent;
import kr.co.shop.web.member.model.master.base.BaseMbMemberInterestBrand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberInterestBrand extends BaseMbMemberInterestBrand implements Validator {

	private String siteNo;
	private String brandEnName;
	private String imageName;
	private String imagePathText;
	private String brandName;
	private String imageUrl;
	private String altrnText;
	private String aconnectDispYn;
	private String delGubun;
	private Map<String, Object> productMap;

	/** 이벤트(draw)정보 */
	private EvEvent eventDraw;

	/** 상품종류 */
	private String productKind;

	/** 브랜드 이미지 */
	private String brandImgUrl;

	/** 판매 종료 Y/N (Y:판매종료) **/
	private String isSellEndYn;

	/** 브랜드스타일 번호 **/
	private String brandStyleSeq;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.shop.common.validation.Validator#validate()
	 */
	@Override
	public void validate() throws ValidatorException {
		if (UtilsText.isBlank(getBrandNo())) {
			validationMessage("mypage.valid.variableisnull", "",
					new String[] { Message.getMessage("mypage.valid.brandNo") });
		}
	}

}
