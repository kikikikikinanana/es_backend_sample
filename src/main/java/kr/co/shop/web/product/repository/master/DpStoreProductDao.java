package kr.co.shop.web.product.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.product.model.master.DpStoreProduct;
import kr.co.shop.web.product.repository.master.base.BaseDpStoreProductDao;

@Mapper
public interface DpStoreProductDao extends BaseDpStoreProductDao {

	public List<DpStoreProduct> getStoreProductList(Pageable<Object, DpStoreProduct> pageable) throws Exception;

	public int getStoreProductTotalCount(Pageable<Object, DpStoreProduct> pageable) throws Exception;

}
