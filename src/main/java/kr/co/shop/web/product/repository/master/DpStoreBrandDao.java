package kr.co.shop.web.product.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.DpStoreBrand;
import kr.co.shop.web.product.model.master.DpStoreProduct;
import kr.co.shop.web.product.repository.master.base.BaseDpStoreBrandDao;

@Mapper
public interface DpStoreBrandDao extends BaseDpStoreBrandDao {

	/**
	 * 이 select 메소드는 Code Generator를 통하여 생성 되었습니다.
	 */
	public List<DpStoreBrand> getStoreBrandList(DpStoreProduct dpStoreProduct) throws Exception;
}
