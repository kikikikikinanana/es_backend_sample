package kr.co.shop.web.cmm.repository.master.base;

import java.util.List;
import java.lang.Object;
import kr.co.shop.web.cmm.model.master.CmEmailSendHistory;


/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다. 
 * 
 */

public interface BaseCmEmailSendHistoryDao {
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public List<CmEmailSendHistory> select(CmEmailSendHistory cmEmailSendHistory) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int insert(CmEmailSendHistory cmEmailSendHistory) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int update(CmEmailSendHistory cmEmailSendHistory) throws Exception;
	
	 /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int delete(CmEmailSendHistory cmEmailSendHistory) throws Exception;


}
