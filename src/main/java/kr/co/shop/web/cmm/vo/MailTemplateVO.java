/**
 * 
 */
package kr.co.shop.web.cmm.vo;

import lombok.Data;

/**
 * @Desc :
 * @FileName : MailVO.java
 * @Project : shop.bo
 * @Date : 2019. 3. 25.
 * @Author : Kimyounghyun
 */
@Data
public class MailTemplateVO {

	// 메일 템플릿 Id
	private String mailTemplateId;
	// 메일 템플릿 model object
	private Object mailTemplateModel;

	// 수신자 회원번호
	private String receiverMemberNo;
	// 수신자 메일주소
	private String receiverEmail;
	// 수신자 명
	private String receiverName;
	// 실시간 발송여부
	private boolean isRealTime = false;
}
