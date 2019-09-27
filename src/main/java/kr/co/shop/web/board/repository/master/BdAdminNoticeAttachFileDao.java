package kr.co.shop.web.board.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.board.repository.master.base.BaseBdAdminNoticeAttachFileDao;
import kr.co.shop.web.board.model.master.BdAdminNoticeAttachFile;

@Mapper
public interface BdAdminNoticeAttachFileDao extends BaseBdAdminNoticeAttachFileDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseBdAdminNoticeAttachFileDao 클래스에 구현 되어있습니다.
     * BaseBdAdminNoticeAttachFileDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public BdAdminNoticeAttachFile selectByPrimaryKey(BdAdminNoticeAttachFile bdAdminNoticeAttachFile) throws Exception;

}
