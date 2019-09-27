package kr.co.shop.web.vendor.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.vendor.model.master.VdVendorProductAllNotice;
import kr.co.shop.web.vendor.repository.master.base.BaseVdVendorProductAllNoticeDao;

@Mapper
public interface VdVendorProductAllNoticeDao extends BaseVdVendorProductAllNoticeDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseVdVendorProductAllNoticeDao 클래스에 구현
	 * 되어있습니다. BaseVdVendorProductAllNoticeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public VdVendorProductAllNotice selectByPrimaryKey(VdVendorProductAllNotice vdVendorProductAllNotice)
			throws Exception;

	public VdVendorProductAllNotice selectVendorProductAllNoticeList(VdVendorProductAllNotice vendorProductAllNotice)
			throws Exception;

	public List<VdVendorProductAllNotice> searchVendorProductAllNoticeList(
			VdVendorProductAllNotice vendorProductAllNotice);

}
