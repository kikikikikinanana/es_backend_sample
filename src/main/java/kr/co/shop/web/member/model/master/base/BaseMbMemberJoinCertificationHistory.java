package kr.co.shop.web.member.model.master.base;

import lombok.Data;
import java.io.Serializable;
import kr.co.shop.common.bean.BaseBean;

@Data
public class BaseMbMemberJoinCertificationHistory extends BaseBean implements Serializable {

    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 회원가입인증이력순번
     */
	private java.lang.Long memberJoinCrtfcHistSeq;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 핸드폰번호
     */
	private String hdphnNoText;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 인증번호
     */
	private String crtfcNoText;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 인증성공여부
     */
	private String crtfcSuccessYn;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 인증일시
     */
	private java.sql.Timestamp crtfcDtm;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 등록일시
     */
	private java.sql.Timestamp rgstDtm;
	
}
