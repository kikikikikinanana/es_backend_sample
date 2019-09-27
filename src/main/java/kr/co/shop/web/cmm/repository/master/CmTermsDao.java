package kr.co.shop.web.cmm.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cmm.model.master.CmTerms;
import kr.co.shop.web.cmm.model.master.CmTermsDetail;
import kr.co.shop.web.cmm.repository.master.base.BaseCmTermsDao;

@Mapper
public interface CmTermsDao extends BaseCmTermsDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmTermsDao 클래스에 구현 되어있습니다. BaseCmTermsDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public CmTerms selectByPrimaryKey(CmTerms cmTerms) throws Exception;

	public List<CmTerms> selectTermsList(CmTerms cmTerms);

	public CmTermsDetail selectTermsWithDetail(CmTerms cmTerms);

	public List<CmTermsDetail> selectTermsDetailList(CmTerms cmTerms);

}
