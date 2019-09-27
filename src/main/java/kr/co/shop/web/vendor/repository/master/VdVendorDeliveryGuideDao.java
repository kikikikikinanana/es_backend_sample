package kr.co.shop.web.vendor.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.vendor.model.master.VdVendorDeliveryGuide;
import kr.co.shop.web.vendor.repository.master.base.BaseVdVendorDeliveryGuideDao;

@Mapper
public interface VdVendorDeliveryGuideDao extends BaseVdVendorDeliveryGuideDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseVdVendorDeliveryGuideDao 클래스에 구현 되어있습니다.
	 * BaseVdVendorDeliveryGuideDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public VdVendorDeliveryGuide selectByPrimaryKey(VdVendorDeliveryGuide vdVendorDeliveryGuide) throws Exception;

	public List<VdVendorDeliveryGuide> getVendorDeliveryGuide(String vndrNo) throws Exception;

}
