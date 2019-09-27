package kr.co.shop.web.member.model.master;

import kr.co.shop.web.member.model.master.base.BaseMbMemberExpostSavePoint;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MbMemberExpostSavePoint extends BaseMbMemberExpostSavePoint {
	/**
	 * 고객명
	 */
	private String memberName;
	
	/**
	 * 링크 url
	 */
	private String linkUrl;


}
