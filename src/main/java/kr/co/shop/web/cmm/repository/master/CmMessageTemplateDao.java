package kr.co.shop.web.cmm.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cmm.model.master.CmMessageTemplate;
import kr.co.shop.web.cmm.repository.master.base.BaseCmMessageTemplateDao;
import kr.co.shop.web.cmm.vo.MessageVO;

@Mapper
public interface CmMessageTemplateDao extends BaseCmMessageTemplateDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseCmMessageTemplateDao 클래스에 구현 되어있습니다.
	 * BaseCmMessageTemplateDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 *
	 */

	public CmMessageTemplate selectByPrimaryKey(CmMessageTemplate cmMessageTemplate) throws Exception;

	public CmMessageTemplate selectMessageTemplateByMesgId(String mesgId) throws Exception;

	public CmMessageTemplate selectMessageTemplate(MessageVO messageVO) throws Exception;

}
