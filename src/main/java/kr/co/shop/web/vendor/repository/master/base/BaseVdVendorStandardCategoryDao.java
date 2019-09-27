package kr.co.shop.web.vendor.repository.master.base;

import java.util.List;
import java.lang.Object;
import kr.co.shop.web.vendor.model.master.VdVendorStandardCategory;


/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다. 
 * 
 */

public interface BaseVdVendorStandardCategoryDao {
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public List<VdVendorStandardCategory> select(VdVendorStandardCategory vdVendorStandardCategory) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int insert(VdVendorStandardCategory vdVendorStandardCategory) throws Exception;
	
    /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int update(VdVendorStandardCategory vdVendorStandardCategory) throws Exception;
	
	 /**
     * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
     */
	public int delete(VdVendorStandardCategory vdVendorStandardCategory) throws Exception;


}
