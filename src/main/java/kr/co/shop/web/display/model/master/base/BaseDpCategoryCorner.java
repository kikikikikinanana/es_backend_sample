package kr.co.shop.web.display.model.master.base;

import lombok.Data;
import java.io.Serializable;
import kr.co.shop.common.bean.BaseBean;

@Data
public class BaseDpCategoryCorner extends BaseBean implements Serializable {

    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 카테고리번호
     */
	private String ctgrNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 전시템플릿번호
     */
	private String dispTmplNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 전시템플릿코너순번
     */
	private java.lang.Integer dispTmplCornerSeq;
	
}
