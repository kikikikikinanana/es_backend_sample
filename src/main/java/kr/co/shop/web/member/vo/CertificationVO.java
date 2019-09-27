package kr.co.shop.web.member.vo;

import lombok.Data;

@Data
public class CertificationVO {
	private String memberNo;
	private String memberName;
	private String loginId;
	private String crtfcPathCode;
	private String crtfcNoSendInfo;
	private String crtfcNoText;
	private int validTime;
	private String crtfcSuccessYn;
	private String certificationNumber;
}
