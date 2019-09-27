package kr.co.shop.web.order.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrderTermsAgree;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderTermsAgreeDao;
import kr.co.shop.web.order.vo.OrderPaymentVo.OrderTermsAgree;

@Mapper
public interface OcOrderTermsAgreeDao extends BaseOcOrderTermsAgreeDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderTermsAgreeDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderTermsAgreeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcOrderTermsAgree selectByPrimaryKey(OcOrderTermsAgree ocOrderTermsAgree) throws Exception;

	public void insertOrderTermsAgree(OrderTermsAgree[] ocOrderTermsAgree) throws Exception;
}
