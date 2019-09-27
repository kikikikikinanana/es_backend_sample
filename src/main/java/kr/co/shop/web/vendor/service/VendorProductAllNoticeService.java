package kr.co.shop.web.vendor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.vendor.model.master.VdVendorProductAllNotice;
import kr.co.shop.web.vendor.repository.master.VdVendorProductAllNoticeDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VendorProductAllNoticeService {

	@Autowired
	private VdVendorProductAllNoticeDao vendorProductAllNoticeDao;

	/**
	 * @Desc : 상품전체공지 목록 조회
	 * @Method Name : getVendorProductAllNoticeList
	 * @Date : 2019. 6. 21.
	 * @Author : hsjhsj19
	 * @param vendorProductAllNotice
	 * @return
	 * @throws Exception
	 */
	public VdVendorProductAllNotice getVendorProductAllNoticeList(VdVendorProductAllNotice vendorProductAllNotice)
			throws Exception {
		return vendorProductAllNoticeDao.selectVendorProductAllNoticeList(vendorProductAllNotice);
	}

	/**
	 * @Desc : 상품전체공지 목록 조회
	 * @Method Name : getVendorProductAllNoticeList
	 * @Date : 2019. 7. 1.
	 * @Author : hsjhsj19
	 * @param prdtNo
	 * @return
	 * @throws Exception
	 */
	public List<VdVendorProductAllNotice> getVendorProductAllNoticeList(String prdtNo) throws Exception {
		VdVendorProductAllNotice vendorProductAllNotice = new VdVendorProductAllNotice();
		vendorProductAllNotice.setPrdtNo(prdtNo);
		return vendorProductAllNoticeDao.searchVendorProductAllNoticeList(vendorProductAllNotice);
	}
}
