package kr.co.shop.web.product.repository.master.base;

import java.util.List;
import java.lang.Object;
import kr.co.shop.web.product.model.master.SyStandardCategory;


/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다. 
 * 
 */

public interface BaseSyStandardCategoryDao {
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public List<SyStandardCategory> select(SyStandardCategory syStandardCategory) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int insert(SyStandardCategory syStandardCategory) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int update(SyStandardCategory syStandardCategory) throws Exception;
	
	 /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int delete(SyStandardCategory syStandardCategory) throws Exception;


}
