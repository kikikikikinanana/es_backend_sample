package kr.co.shop.web.event.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventPublicationNumber;
import kr.co.shop.web.event.repository.master.base.BaseEvEventPublicationNumberDao;

@Mapper
public interface EvEventPublicationNumberDao extends BaseEvEventPublicationNumberDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventPublicationNumberDao 클래스에 구현
	 * 되어있습니다. BaseEvEventPublicationNumberDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public EvEventPublicationNumber selectByPrimaryKey(EvEventPublicationNumber evEventPublicationNumber)
			throws Exception;

	/**
	 * 이벤트 발행번호 업데이트
	 * 
	 * @Desc : 이벤트 발행번호 업데이트
	 * @Method Name : updatePublNumber
	 * @Date : 2019. 7. 03
	 * @Author : easyhun
	 * @param eventNo
	 * @return
	 */
	public void updatePublNumber(EvEventPublicationNumber evEventPublicationNumber) throws Exception;

}
