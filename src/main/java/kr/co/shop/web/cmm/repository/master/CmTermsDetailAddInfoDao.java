package kr.co.shop.web.cmm.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.cmm.repository.master.base.BaseCmTermsDetailAddInfoDao;
import kr.co.shop.web.cmm.model.master.CmTermsDetailAddInfo;

@Mapper
public interface CmTermsDetailAddInfoDao extends BaseCmTermsDetailAddInfoDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseCmTermsDetailAddInfoDao 클래스에 구현 되어있습니다.
     * BaseCmTermsDetailAddInfoDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public CmTermsDetailAddInfo selectByPrimaryKey(CmTermsDetailAddInfo cmTermsDetailAddInfo) throws Exception;

}
