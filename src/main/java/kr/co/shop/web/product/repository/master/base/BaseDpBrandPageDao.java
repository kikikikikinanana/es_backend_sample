package kr.co.shop.web.product.repository.master.base;

import java.util.List;

import kr.co.shop.web.product.model.master.DpBrandPage;

/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다.
 * 
 */

public interface BaseDpBrandPageDao {

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public List<DpBrandPage> select(DpBrandPage dpBrandPage) throws Exception;

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public int insert(DpBrandPage dpBrandPage) throws Exception;

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public int update(DpBrandPage dpBrandPage) throws Exception;

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public int delete(DpBrandPage dpBrandPage) throws Exception;

}
