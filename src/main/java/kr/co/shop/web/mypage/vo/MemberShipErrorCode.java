package kr.co.shop.web.mypage.vo;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum MemberShipErrorCode {
	defalut("defalut", "입력하신 내용으로 조회된 내역이 없습니다."), noPurchaseHistory("04063801", "입력하신 내용으로 조회된 내역이 없습니다."),
	PurchaseHistoryError("04063802", "입력하신 내용으로 조회된 내역이 없습니다."), purchaseConfirmedError("04063803", "포인트 지급대상이 아닙니다."),
	returnReceiptError("04063804", "포인트 지급대상이 아닙니다."), etcError("04063805", "입력하신 내용으로 조회된 내역이 없습니다.");

	private String code;
	private String desc;

	private MemberShipErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@JsonValue
	public String getCode() {
		return code;
	}

	public static MemberShipErrorCode getByCode(String code) {
		for (MemberShipErrorCode type : values()) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		return MemberShipErrorCode.defalut;
		// throw new IllegalArgumentException( "MemberShipErrorCode No Data Found.." );
	}
}
