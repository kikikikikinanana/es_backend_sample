package kr.co.shop.web.order.repository.master.base;

import java.util.List;
import java.lang.Object;
import kr.co.shop.web.order.model.master.OcOrderTermsAgree;


/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다. 
 * 
 */

public interface BaseOcOrderTermsAgreeDao {
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public List<OcOrderTermsAgree> select(OcOrderTermsAgree ocOrderTermsAgree) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int insert(OcOrderTermsAgree ocOrderTermsAgree) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int update(OcOrderTermsAgree ocOrderTermsAgree) throws Exception;
	
	 /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int delete(OcOrderTermsAgree ocOrderTermsAgree) throws Exception;


}
