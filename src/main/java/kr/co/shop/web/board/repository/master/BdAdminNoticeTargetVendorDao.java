package kr.co.shop.web.board.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.board.repository.master.base.BaseBdAdminNoticeTargetVendorDao;
import kr.co.shop.web.board.model.master.BdAdminNoticeTargetVendor;

@Mapper
public interface BdAdminNoticeTargetVendorDao extends BaseBdAdminNoticeTargetVendorDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BaseBdAdminNoticeTargetVendorDao 클래스에 구현 되어있습니다.
     * BaseBdAdminNoticeTargetVendorDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public BdAdminNoticeTargetVendor selectByPrimaryKey(BdAdminNoticeTargetVendor bdAdminNoticeTargetVendor) throws Exception;

}
