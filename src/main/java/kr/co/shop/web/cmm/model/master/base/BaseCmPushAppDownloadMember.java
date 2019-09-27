package kr.co.shop.web.cmm.model.master.base;

import lombok.Data;
import java.io.Serializable;
import kr.co.shop.common.bean.BaseBean;

@Data
public class BaseCmPushAppDownloadMember extends BaseBean implements Serializable {

    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : APP다운로드회원순번
     */
	private java.lang.Long appDwldMemberSeq;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 사이트번호
     */
	private String siteNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : APP OS코드
     */
	private String appOsCode;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : APP버전
     */
	private String appVerText;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : FCM Token
     */
	private String fcmTokenText;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 디바이스Token
     */
	private String deviceTokenText;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : APP설치일시
     */
	private java.sql.Timestamp appInstallDtm;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 회원번호
     */
	private String memberNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : APP-PUSH수신여부
     */
	private String appPushRecvYn;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : APP-PUSH수신여부수정일시
     */
	private java.sql.Timestamp appPushRecvYnModDtm;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 바이오인증로그인사용여부
     */
	private String bioCrtfcLoginUseYn;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 바이오인증로그인사용여부수정일시
     */
	private java.sql.Timestamp bioCrtfcLoginUseYnModDtm;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 야간PUSH수신여부
     */
	private String nightPushRecvYn;
	
}
