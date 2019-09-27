package kr.co.shop.web.vendor.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.vendor.repository.master.base.BaseVdVendorManagerDao;
import kr.co.shop.web.vendor.model.master.VdVendorManager;

@Mapper
public interface VdVendorManagerDao extends BaseVdVendorManagerDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseVdVendorManagerDao 클래스에 구현 되어있습니다.
     * BaseVdVendorManagerDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public VdVendorManager selectByPrimaryKey(VdVendorManager vdVendorManager) throws Exception;

}
