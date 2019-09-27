package kr.co.shop.web.board.repository.master.base;

import java.util.List;
import java.lang.Object;
import kr.co.shop.web.board.model.master.BdAdminNotice;


/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다. 
 * 
 */

public interface BaseBdAdminNoticeDao {
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public List<BdAdminNotice> select(BdAdminNotice bdAdminNotice) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int insert(BdAdminNotice bdAdminNotice) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int update(BdAdminNotice bdAdminNotice) throws Exception;
	
	 /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int delete(BdAdminNotice bdAdminNotice) throws Exception;


}
