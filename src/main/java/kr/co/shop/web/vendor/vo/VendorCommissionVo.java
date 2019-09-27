/**
 * 
 */
package kr.co.shop.web.vendor.vo;

import java.util.List;

import kr.co.shop.common.bean.BaseBean;
import kr.co.shop.web.vendor.model.master.VdVendorExceptionCommission;
import lombok.Data;

@Data
public class VendorCommissionVo extends BaseBean {

	private static final long serialVersionUID = -8681689823079938230L;

	private String vndrNo;

	private java.lang.Short dfltCmsnRate;

	private List<VdVendorExceptionCommission> exceptionCommissionList;

}
