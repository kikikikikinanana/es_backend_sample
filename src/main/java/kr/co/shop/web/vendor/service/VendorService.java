package kr.co.shop.web.vendor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.constant.CommonCode;
import kr.co.shop.web.board.model.master.BdContactUs;
import kr.co.shop.web.board.repository.master.BdContactUsDao;
import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.service.CommonCodeService;
import kr.co.shop.web.vendor.model.master.VdVendor;
import kr.co.shop.web.vendor.model.master.VdVendorBrandEmployeeDiscount;
import kr.co.shop.web.vendor.model.master.VdVendorDeliveryGuide;
import kr.co.shop.web.vendor.repository.master.VdVendorBrandEmployeeDiscountDao;
import kr.co.shop.web.vendor.repository.master.VdVendorDao;
import kr.co.shop.web.vendor.repository.master.VdVendorDeliveryGuideDao;
import kr.co.shop.web.vendor.vo.VendorCommissionVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VendorService {

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private BdContactUsDao bdContactUsDao;

	@Autowired
	private VdVendorDao vdVendorDao;

	@Autowired
	private VdVendorBrandEmployeeDiscountDao vdVendorBrandEmployeeDiscountDao;

	@Autowired
	private VdVendorDeliveryGuideDao vdVendorDeliveryGuideDao;

	/**
	 * @Desc :입점문의 화면에 필요한 data를 조회한다.
	 * @Method Name : getgetContactUsFormData
	 * @Date : 2019. 3. 12.
	 * @Author : 유성민
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getContactUsFormData() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SyCodeDetail syCodeDetail = new SyCodeDetail();
		syCodeDetail.setCodeField(CommonCode.CNSL_TYPE_DTL_CODE);
		syCodeDetail.setAddInfo1(CommonCode.CNSL_TYPE_CODE_VENDOR_INQUIRY);

		map.put("commonCode", commonCodeService.getUseCodeByAddInfo1(syCodeDetail));

		return map;
	}

	/**
	 * @Desc : 입점문의 저장
	 * @Method Name : saveVendorContactUs
	 * @Date : 2019. 3. 14.
	 * @Author : 유성민
	 * @param params
	 * @return
	 */
	public void saveVendorContactUs(BdContactUs params) throws Exception {
		bdContactUsDao.insert(params);
	}

	/**
	 * @Desc : 입점업체 기본정보조회
	 * @Method Name : getVendorInfo
	 * @Date : 2019. 4. 12.
	 * @Author : 유성민
	 * @param param
	 * @return
	 */
	public VdVendor getVendorInfo(VdVendor param) throws Exception {
		return vdVendorDao.selectByPrimaryKey(param);
	}

	/**
	 * @Desc : 입점업체 기본정보 리스트
	 * @Method Name : getVendorInfoList
	 * @Date : 2019. 4. 25.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<VdVendor> getVendorInfoList(List<String> params) throws Exception {
		if (params.isEmpty()) {
			throw new Exception("업체번호가 없습니다");
		}

		return vdVendorDao.selectVendorInfoList(params);
	}

	/**
	 * @Desc : 업체수수료 조회
	 * @Method Name : getVendorCommissionList
	 * @Date : 2019. 6. 7.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<VendorCommissionVo> getVendorCommissionList(List<String> params) throws Exception {
		if (params.isEmpty()) {
			throw new Exception("업체번호가 없습니다");
		}

		return vdVendorDao.getVendorCommissionList(params);
	}

	/**
	 * @Desc : 업체브랜드별 임직원할인률 조회
	 * @Method Name : getVendorBrandEmployeeDiscountList
	 * @Date : 2019. 6. 19.
	 * @Author : 유성민
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<VdVendorBrandEmployeeDiscount> getVendorBrandEmployeeDiscountList(List<String> params)
			throws Exception {
		if (params.isEmpty()) {
			throw new Exception("업체번호가 없습니다");
		}

		return vdVendorBrandEmployeeDiscountDao.getVendorBrandEmployeeDiscountList(params);
	}

	/**
	 * @Desc :상품 배송/교환/반품/as 안내 정보
	 * @Method Name : getVendorDeliveryGuide
	 * @Date : 2019. 6. 21.
	 * @Author : 유성민
	 * @param vndrNo
	 * @return
	 * @throws Exception
	 */
	public List<VdVendorDeliveryGuide> getVendorDeliveryGuide(String vndrNo) throws Exception {
		return vdVendorDeliveryGuideDao.getVendorDeliveryGuide(vndrNo);
	}

}
