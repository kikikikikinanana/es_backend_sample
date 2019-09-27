package kr.co.shop.web.display.repository.master.base;

import java.util.List;

import kr.co.shop.web.display.model.master.DpWebzineDetailImage;

/**
 * ※ 절대 수정 금지. 해당 파일은 code generator 작동 시 내용을 전부 덮어 씌우게 됩니다.
 * 
 */

public interface BaseDpWebzineDetailImageDao {

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public List<DpWebzineDetailImage> select(DpWebzineDetailImage dpWebzineDetailImage) throws Exception;

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public int insert(DpWebzineDetailImage dpWebzineDetailImage) throws Exception;

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public int update(DpWebzineDetailImage dpWebzineDetailImage) throws Exception;

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public int delete(DpWebzineDetailImage dpWebzineDetailImage) throws Exception;

}
