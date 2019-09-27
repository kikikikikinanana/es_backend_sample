package kr.co.shop.web.order.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderZeroBankbookFailureDetailsDao;
import kr.co.shop.web.order.model.master.OcOrderZeroBankbookFailureDetails;

@Mapper
public interface OcOrderZeroBankbookFailureDetailsDao extends BaseOcOrderZeroBankbookFailureDetailsDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseOcOrderZeroBankbookFailureDetailsDao 클래스에 구현 되어있습니다.
     * BaseOcOrderZeroBankbookFailureDetailsDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public OcOrderZeroBankbookFailureDetails selectByPrimaryKey(OcOrderZeroBankbookFailureDetails ocOrderZeroBankbookFailureDetails) throws Exception;

}
