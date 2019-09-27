package kr.co.shop.web.system.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.repository.master.base.BaseSyCodeDetailDao;

@Mapper
public interface SyCodeDetailDao extends BaseSyCodeDetailDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseSyCodeDetailDao 클래스에 구현 되어있습니다.
	 * BaseSyCodeDetailDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public SyCodeDetail selectByPrimaryKey(SyCodeDetail syCodeDetail) throws Exception;

	public List<SyCodeDetail> selectUseCode(String codeField) throws Exception;

	public List<SyCodeDetail> selectCode(String codeField);

	public List<SyCodeDetail> selectUseCodeByAddInfo1(SyCodeDetail syCodeDetail) throws Exception;

	public List<SyCodeDetail> selectUseCodeByGroup(String[] codeFields) throws Exception;

	public List<SyCodeDetail> selectCodeByGroup(String[] codeFields);

}
