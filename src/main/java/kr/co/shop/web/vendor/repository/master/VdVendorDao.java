package kr.co.shop.web.vendor.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.repository.master.base.BaseVdVendorDao;
import kr.co.shop.web.vendor.vo.VendorCommissionVo;

@Mapper
public interface VdVendorDao extends BaseVdVendorDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseVdVendorDao 클래스에 구현 되어있습니다.
	 * BaseVdVendorDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public VdVendor selectByPrimaryKey(VdVendor vdVendor) throws Exception;

	public List<VdVendor> selectVendorInfoList(List<String> params) throws Exception;

	public List<VendorCommissionVo> getVendorCommissionList(List<String> params) throws Exception;

}
