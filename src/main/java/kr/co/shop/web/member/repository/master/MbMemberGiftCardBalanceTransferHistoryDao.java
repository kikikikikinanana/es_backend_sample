package kr.co.shop.web.member.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberGiftCardBalanceTransferHistoryDao;
import kr.co.shop.web.member.model.master.MbMemberGiftCardBalanceTransferHistory;

@Mapper
public interface MbMemberGiftCardBalanceTransferHistoryDao extends BaseMbMemberGiftCardBalanceTransferHistoryDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseMbMemberGiftCardBalanceTransferHistoryDao 클래스에 구현 되어있습니다.
     * BaseMbMemberGiftCardBalanceTransferHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public MbMemberGiftCardBalanceTransferHistory selectByPrimaryKey(MbMemberGiftCardBalanceTransferHistory mbMemberGiftCardBalanceTransferHistory) throws Exception;

}
