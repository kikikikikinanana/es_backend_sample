package kr.co.shop.web.system.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.system.model.master.SySitePaymentMeans;
import kr.co.shop.web.system.repository.master.base.BaseSySitePaymentMeansDao;

@Mapper
public interface SySitePaymentMeansDao extends BaseSySitePaymentMeansDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseSySitePaymentMeansDao 클래스에 구현 되어있습니다.
	 * BaseSySitePaymentMeansDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public SySitePaymentMeans selectByPrimaryKey(SySitePaymentMeans sySitePaymentMeans) throws Exception;

	public List<SySitePaymentMeans> selectUsePaymentMeans(SySitePaymentMeans sySitePaymentMeans);

}
