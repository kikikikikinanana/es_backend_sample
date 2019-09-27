package kr.co.shop.web.vendor.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.vendor.repository.master.base.BaseVdVendorWrhsDlvyAddressDao;
import kr.co.shop.web.vendor.model.master.VdVendorWrhsDlvyAddress;

@Mapper
public interface VdVendorWrhsDlvyAddressDao extends BaseVdVendorWrhsDlvyAddressDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseVdVendorWrhsDlvyAddressDao 클래스에 구현 되어있습니다.
     * BaseVdVendorWrhsDlvyAddressDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public VdVendorWrhsDlvyAddress selectByPrimaryKey(VdVendorWrhsDlvyAddress vdVendorWrhsDlvyAddress) throws Exception;

}
