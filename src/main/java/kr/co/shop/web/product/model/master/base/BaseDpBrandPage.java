package kr.co.shop.web.product.model.master.base;

import java.io.Serializable;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

@Data
public class BaseDpBrandPage extends BaseBean implements Serializable {

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 브랜드번호
	 */
	private String brandNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 전시구분
	 */
	private String dispGbnType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 디바이스코드
	 */
	private String deviceCode;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 브랜드페이지순번
	 */
	private java.lang.Integer brandPageSeq;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 사이트번호
	 */
	private String siteNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 파일유형
	 */
	private String fileType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 제목
	 */
	private String titleText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 이미지명
	 */
	private String imageName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 이미지경로
	 */
	private String imagePathText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 이미지URL
	 */
	private String imageUrl;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 대체텍스트
	 */
	private String altrnText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 연결유형
	 */
	private String cnnctnType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 링크Target
	 */
	private String linkTargetType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 링크정보
	 */
	private String linkInfo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 텍스트컬러
	 */
	private String textColorType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 배경컬러사용여부
	 */
	private String bckgColorYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 배경컬러값
	 */
	private String bckgColorValue;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 업로드여부
	 */
	private String uploadYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 동영상재생방법
	 */
	private String moviePlayType;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 동영상명
	 */
	private String movieName;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 동영상경로
	 */
	private String moviePathText;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 동영상URL
	 */
	private String movieUrl;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 전시여부
	 */
	private String dispYn;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 전시시작일
	 */
	private java.sql.Timestamp dispStartYmd;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 전시종료일
	 */
	private java.sql.Timestamp dispEndYmd;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 정렬순번
	 */
	private java.lang.Integer sortSeq;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 등록자번호
	 */
	private String rgsterNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 등록일시
	 */
	private java.sql.Timestamp rgstDtm;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수정자번호
	 */
	private String moderNo;

	/**
	 * 이 필드는 Code Generator를 통하여 생성 되었습니다. 설명 : 수정일시
	 */
	private java.sql.Timestamp modDtm;

}
