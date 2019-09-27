package kr.co.shop.web.order.model.master.base;

import lombok.Data;
import java.io.Serializable;
import kr.co.shop.common.bean.BaseBean;

@Data
public class BaseIfOffDealHistory extends BaseBean implements Serializable {

    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : SAFE_KEY
     */
	private String safeKey;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : SAFE_KEY_SEQ
     */
	private String safeKeySeq;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : SALE_DATE
     */
	private String saleDate;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : STORE_CD
     */
	private String storeCd;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : POS_NO
     */
	private String posNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : DEAL_NO
     */
	private String dealNo;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : PRODUCT_CD
     */
	private String productCd;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : SIZE_CD
     */
	private String sizeCd;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : SALE_QTY
     */
	private java.math.BigDecimal saleQty;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : CONSUMER_PRICE
     */
	private java.math.BigDecimal consumerPrice;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : DC_PRICE
     */
	private java.math.BigDecimal dcPrice;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : SALE_PRICE
     */
	private java.math.BigDecimal salePrice;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : RETURN_FLAG
     */
	private String returnFlag;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : INS_DT
     */
	private java.sql.Timestamp insDt;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : INS_CD
     */
	private String insCd;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : UPT_DT
     */
	private java.sql.Timestamp uptDt;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : UPT_CD
     */
	private String uptCd;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : RETURN_STORE_CD
     */
	private String returnStoreCd;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : RETURN_DATE
     */
	private String returnDate;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : CONF_FLAG
     */
	private String confFlag;
	
    /**
     * 이 필드는 Code Generator를 통하여 생성 되었습니다.
     * 설명 : BUY_FIX_DTM
     */
	private java.sql.Timestamp buyFixDtm;
	
}
