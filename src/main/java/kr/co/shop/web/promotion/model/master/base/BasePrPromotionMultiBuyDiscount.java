package kr.co.shop.web.promotion.model.master.base;

import lombok.Data;
import java.io.Serializable;
import kr.co.shop.common.bean.BaseBean;

@Data
public class BasePrPromotionMultiBuyDiscount extends BaseBean implements Serializable {

    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 프로모션번호
     */
	private String promoNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 다족구매할인순번
     */
	private java.lang.Integer multiBuyDscntSeq;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 구매수량
     */
	private java.lang.Integer buyQty;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : 할인율
     */
	private java.lang.Short dscntRate;
	
}
