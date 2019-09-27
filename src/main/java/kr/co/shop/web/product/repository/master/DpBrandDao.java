package kr.co.shop.web.product.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.DpBrand;
import kr.co.shop.web.product.repository.master.base.BaseDpBrandDao;

@Mapper
public interface DpBrandDao extends BaseDpBrandDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpBrandDao 클래스에 구현 되어있습니다. BaseDpBrandDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpBrand selectByPrimaryKey(DpBrand dpBrand) throws Exception;

	public List<DpBrand> getStoreBrandList(DpBrand dpBrand) throws Exception;

	public List<DpBrand> getBrandPageLoad(DpBrand dpBrand) throws Exception;

	public List<DpBrand> getBrandList(DpBrand dpBrand) throws Exception;

	public List<DpBrand> getBrandListCount(DpBrand dpBrand) throws Exception;

	public List<DpBrand> selectBrandSaleList(DpBrand dpBrand) throws Exception;
}
