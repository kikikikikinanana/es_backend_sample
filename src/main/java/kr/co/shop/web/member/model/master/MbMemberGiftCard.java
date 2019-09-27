package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberGiftCard;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberGiftCard extends BaseMbMemberGiftCard {

	private String imageUrl;
	private String cnt;
	private String pdGiftCardName;

}
