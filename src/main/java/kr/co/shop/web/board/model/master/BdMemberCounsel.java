package kr.co.shop.web.board.model.master;

import java.util.List;

import kr.co.shop.common.request.FileUpload;
import kr.co.shop.common.request.annotation.ParameterOption;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProduct;
import kr.co.shop.web.board.model.master.base.BaseBdMemberCounsel;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
public class BdMemberCounsel extends BaseBdMemberCounsel {

	private String vndrName;

	private String cnslTypeName;

	private String aswrStatName;
	private String inquiryId;
	private String inquiryName;
	private String aswrId;
	private String aswrName;

	private String fromDate;
	private String toDate;

	private String dpInqryDtm;
	private String dpAswrDtm;
	private String dpAswrContText;

	private OcAsAcceptProduct ocAsAcceptProduct;
	private OcOrderProduct ocOrderProduct;

	private String prdtName;
	private String prdtColorCode;
	private String optnName;
	private String brandName;

	private int[] removeAtchFileSeq;

	/**
	 * 문의 첨부 파일
	 */
	private List<BdMemberCounselAttachFile> inqryCounselAttachFiles;

	private FileUpload[] inqryUpLoadFile;

	/**
	 * 파일 업로드용
	 */
	@ParameterOption(target = "atchFileGbnType")
	private BdMemberCounselAttachFile[] inqryAtchFiles;

	/**
	 * 답변 첨부 파일
	 */
	private List<BdMemberCounselAttachFile> aswrCounselAttachFiles;

}
