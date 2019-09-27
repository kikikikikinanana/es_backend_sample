package kr.co.shop.web.display.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Page;
import kr.co.shop.web.display.model.master.DpWebzineProduct;
import kr.co.shop.web.display.repository.master.base.BaseDpWebzineProductDao;
import kr.co.shop.web.product.model.master.PageableProduct;
import kr.co.shop.web.product.model.master.PdProductWrapper;

@Mapper
public interface DpWebzineProductDao extends BaseDpWebzineProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpWebzineProductDao 클래스에 구현 되어있습니다.
	 * BaseDpWebzineProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpWebzineProduct selectByPrimaryKey(DpWebzineProduct dpWebzineProduct) throws Exception;

	public Page<PdProductWrapper> selectDpWebzineProductList(PageableProduct<?, PdProductWrapper> pageable)
			throws Exception;

}
