package kr.co.shop.web.system.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.system.repository.master.base.BaseSyCodeDao;
import kr.co.shop.web.system.model.master.SyCode;

@Mapper
public interface SyCodeDao extends BaseSyCodeDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseSyCodeDao 클래스에 구현 되어있습니다.
     * BaseSyCodeDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public SyCode selectByPrimaryKey(SyCode syCode) throws Exception;

}
