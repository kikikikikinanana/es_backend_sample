package kr.co.shop.web.cmm.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.cmm.repository.master.base.BaseCmStoreServiceDao;
import kr.co.shop.web.cmm.model.master.CmStoreService;

@Mapper
public interface CmStoreServiceDao extends BaseCmStoreServiceDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseCmStoreServiceDao 클래스에 구현 되어있습니다.
     * BaseCmStoreServiceDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public CmStoreService selectByPrimaryKey(CmStoreService cmStoreService) throws Exception;

}
