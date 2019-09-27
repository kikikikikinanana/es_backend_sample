package kr.co.shop.web.afterService.model.master;

import kr.co.shop.common.request.FileUpload;
import kr.co.shop.interfaces.module.payment.kcp.model.KcpPaymentApproval;
import kr.co.shop.web.afterService.model.master.base.BaseOcAsAccept;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OcAsAccept extends BaseOcAsAccept {

	// 업로드 파일 배열
	private FileUpload[] upload_file;

	// 업로드 테이블 배열 선언
	private OcAsAcceptAttachFile[] attachFiles;

	// 결제 모듈 설정겂
	private KcpPaymentApproval kcpPaymentApproval;

	// 주문 순번
	private Short orderPrdtSeq;

	// 내용
	private String asAcceptContText;

	// 사유코드
	private String asRsnCode;

	// 상품 순번
	private short asAcceptPrdtSeq;

	// 카드 인지 실시간인지 구분코드
	private String pymntMeansCode;

	// 접수일시 변환 YYYY.MM.DD
	private String asAcceptDate;

	// 상태 코드 네임
	private String asStatCodeName;

	// 사유구분코드 네임
	private String asGbnCodeName;

	private String orgOrderDate;

	private int[] removeAtchFileSeq;

	private String[] fileName;
	private int[] fileSeq;

	private String strModDtm;

	private String asProcTypeCodeName;
}
