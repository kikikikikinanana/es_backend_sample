package kr.co.shop.web.member.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.member.model.master.MbMemberInterestBrand;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberInterestBrandDao;

@Mapper
public interface MbMemberInterestBrandDao extends BaseMbMemberInterestBrandDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberInterestBrandDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberInterestBrandDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public MbMemberInterestBrand selectByPrimaryKey(MbMemberInterestBrand mbMemberInterestBrand) throws Exception;

	public int selectMyInterestedBrandListCount(Pageable<MbMemberInterestBrand, MbMemberInterestBrand> pageable)
			throws Exception;

	public List<MbMemberInterestBrand> selectMyInterestedBrandList(
			Pageable<MbMemberInterestBrand, MbMemberInterestBrand> pageable) throws Exception;

	public void deleteMyInterestedBrand(MbMemberInterestBrand params) throws Exception;

}
