package kr.co.shop.web.promotion.model.master.base;

import lombok.Data;
import java.io.Serializable;
import kr.co.shop.common.bean.BaseBean;

@Data
public class BasePrPlanningDisplayApplyDevice extends BaseBean implements Serializable {

    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 기획전번호
     */
	private String plndpNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 디바이스코드
     */
	private String deviceCode;
	
}
