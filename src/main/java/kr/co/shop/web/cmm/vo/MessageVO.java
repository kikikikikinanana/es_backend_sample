/**
 * 
 */
package kr.co.shop.web.cmm.vo;

import lombok.Data;

/**
 * @Desc :
 * @FileName : MessageVO.java
 * @Project : shop.bo
 * @Date : 2019. 3. 25.
 * @Author : Kimyounghyun
 */
@Data
public class MessageVO {

	// 메세지 Id
	private String mesgId;
	// 메세지 템플릿 model object
	private Object messageTemplateModel;
	// 메세지 제목
	private String mesgTitleText;
	// 메세지 내용
	private String mesgContText;
	// 발송자명
	private String sndrName;
	// 발송 전화번호
	private String sendTelNoText;
	// 수신자 전화번호
	private String recvTelNoText;
	// 수신자명
	private String rcvrName;
	// 회원번호
	private String memberNo;
	// 회원명
	private String memberName;
	// 사이트
	private String siteNo;
	// 메세지 타입
	private String messageType;
	// 송신타입
	private String sendTypeCode;
	// 관리자 발송 여부
	private String adminSendYn = "N";
	// 실시간 여부
	private boolean isReal = false;
	// 주문번호
	private String orderNo;
	// 주문상품순번
	private String orderPrdtSeq;
	// 상담스크립트순번
	private String cnslScriptSeq;
	// 실패시 전송 타입
	private String failedType;
	// 실패시 전송 제목
	private String failedSubject;
	// 실팰시 전송 메세지
	private String failedMsg;

	/**
	 * 클레임 - 주문취소
	 */
	// 취소금액
	private String cancelAmt;
	// 취소예정금액
	private String todoCancelAmt;
	// 운송장번호
	private String waybilNoText;
	// 택배사명
	private String logisVndrName;
	// 인수편의점명
	private String cvnstrName;
	// 랜딩 URL
	private String landingUrl;
	// 택배사 랜딩단축 URL
	private String logisVndrLandingUrl;

	/**
	 * 클레임 - 교환접수
	 */
	// 상품명
	private String prdtName;
	// 기존옵션명
	private String crntOptnName;
	// 변경옵션명
	private String chngOptnName;
	// 결제방식명
	private String pymntMeansName;
	// 결제금액
	private String pymntAmt;
	// 입금정보
	private String depositInfo;
	// 은행명
	private String bankName;
	// 계좌번호
	private String acntNoText;
	// 입금마감기한
	private String acntExprDtm;
	// 예금주
	private String acntHldr;
	// 브랜드명
	private String brandName;

	/**
	 * 클레임 - 반품접수
	 */
	// 반품배송비정보
	private String returnDlvyInfo;
	// 환불금액
	private String refundAmt;
}
