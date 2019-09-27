package kr.co.shop.web.board.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.board.model.master.BdMemberCounsel;
import kr.co.shop.web.board.model.master.BdMemberCounselAttachFile;
import kr.co.shop.web.board.repository.master.base.BaseBdMemberCounselAttachFileDao;

@Mapper
public interface BdMemberCounselAttachFileDao extends BaseBdMemberCounselAttachFileDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseBdMemberCounselAttachFileDao 클래스에 구현
	 * 되어있습니다. BaseBdMemberCounselAttachFileDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public BdMemberCounselAttachFile selectByPrimaryKey(BdMemberCounselAttachFile bdMemberCounselAttachFile)
			throws Exception;

	/**
	 * @Desc : 상세보기시에 파일 불러오기
	 * @Method Name : selectInqryFileDetail
	 * @Date : 2019. 4. 10.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @return
	 * @throws Exception
	 */
	public List<BdMemberCounselAttachFile> selectInqryFileDetail(BdMemberCounsel bdMemberCounsel) throws Exception;

	/**
	 * @Desc : 첨부파일 등록
	 * @Method Name : insertInqryFile
	 * @Date : 2019. 4. 24.
	 * @Author : 신인철
	 * @param bdMemberCounselAttachFile
	 * @throws Exception
	 */
	public void insertInqryFile(BdMemberCounselAttachFile bdMemberCounselAttachFile) throws Exception;

	/**
	 * @Desc : 첨부파일 삭제
	 * @Method Name : deleteInqryFile
	 * @Date : 2019. 4. 24.
	 * @Author : 신인철
	 * @param bdMemberCounsel
	 * @throws Exception
	 */
	public void deleteInqryFile(BdMemberCounsel bdMemberCounsel) throws Exception;

}
