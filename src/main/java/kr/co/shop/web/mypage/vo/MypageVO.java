package kr.co.shop.web.mypage.vo;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

/**
 * 
 * @Desc :
 * @FileName : MypageVO.java
 * @Project : shop.backend
 * @Date : 2019. 3. 13.
 * @Author : 유성민
 */
@Data
public class MypageVO extends BaseBean {

	private static final long serialVersionUID = -51785470418477007L;

	private String memberNo; // 회원번호
	private int couponCount; // 쿠폰 갯수
	private int pointAtm; // 포인트
	private String mbshpCardNo; // 멤버쉽카드번호
	private String memberTypeCode; // 온라인회원, 맴버쉽회원, 비회원
	private String mbshpGradeCode; // 일반, VIP
	private String empLoginYn; // 임직원 로그인여부
	private String safeKey;
	private String pswdText;
	private String loginId;
	private String hdphnNoText;
	private String telNoText;
	private String siteNo;
	private String storeNo;
	private String rgsterNo;
	private String empNo;
	private String isMobile;
}
