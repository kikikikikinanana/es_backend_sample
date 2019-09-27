package kr.co.shop.web.cmm.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cmm.model.master.CmAreaDetail;
import kr.co.shop.web.cmm.repository.master.base.BaseCmAreaDetailDao;

@Mapper
public interface CmAreaDetailDao extends BaseCmAreaDetailDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmAreaDetailDao 클래스에 구현 되어있습니다.
	 * BaseCmAreaDetailDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public CmAreaDetail selectByPrimaryKey(CmAreaDetail cmAreaDetail) throws Exception;

	/**
	 * @Desc : 지역 상세 리스트 조회 (시/군/구)
	 * @Method Name : selectCmAreaDetailList
	 * @Date : 2019. 5. 7.
	 * @Author : 이가영
	 * @return
	 */
	public List<CmAreaDetail> selectCmAreaDetailList(CmAreaDetail cmAreaDetail) throws Exception;

}
