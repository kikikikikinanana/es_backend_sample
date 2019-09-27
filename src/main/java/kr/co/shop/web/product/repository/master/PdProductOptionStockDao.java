package kr.co.shop.web.product.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.product.model.master.PdProductOptionStock;
import kr.co.shop.web.product.repository.master.base.BasePdProductOptionStockDao;

@Mapper
public interface PdProductOptionStockDao extends BasePdProductOptionStockDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdProductOptionStockDao 클래스에 구현 되어있습니다.
	 * BasePdProductOptionStockDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public PdProductOptionStock selectByPrimaryKey(PdProductOptionStock pdProductOptionStock) throws Exception;

	/**
	 * @Desc : 매장별 재고확인 갯수 조회
	 * @Method Name : selectProductOptionStoreStockCount
	 * @Date : 2019. 5. 13.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Integer selectProductOptionStoreStockCount(Pageable<Object, Map<String, Object>> pageable) throws Exception;

	/**
	 * @Desc : 매장별 재고확인 목록 조회
	 * @Method Name : selectProductOptionStoreStock
	 * @Date : 2019. 5. 13.
	 * @Author : hsjhsj19
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectProductOptionStoreStock(Pageable<Object, Map<String, Object>> pageable)
			throws Exception;

}
