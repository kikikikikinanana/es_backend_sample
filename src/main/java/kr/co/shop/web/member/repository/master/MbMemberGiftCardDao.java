package kr.co.shop.web.member.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.member.model.master.MbMemberGiftCard;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberGiftCardDao;

@Mapper
public interface MbMemberGiftCardDao extends BaseMbMemberGiftCardDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberGiftCardDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberGiftCardDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public MbMemberGiftCard selectByPrimaryKey(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public MbMemberGiftCard selectGiftcardInfo(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void registGiftCard(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public String selectGiftCardNo(String mgmtNoText) throws Exception;

	public int selectMyGiftCardCnt(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public int selectGiftCardCount(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public int selectDuplCard(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public List<MbMemberGiftCard> selectMyGiftCard(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void updateDelYn(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void updateRprsntAll(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void updateRprsnt(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void updateGiftCardName(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public String selectGiftCardName(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void updateGiftCardAmt(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public MbMemberGiftCard selectMyGiftCardCheck(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public List<MbMemberGiftCard> selectMyGiftCardList(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public void updateGiftCardDelYn(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public MbMemberGiftCard selectCardImg(MbMemberGiftCard mbMemberGiftCard) throws Exception;

	public MbMemberGiftCard selectCardImgCode(MbMemberGiftCard mbMemberGiftCard) throws Exception;
}
