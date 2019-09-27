package kr.co.shop.web.display.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.display.model.master.DpWebzine;
import kr.co.shop.web.display.repository.master.base.BaseDpWebzineDao;

@Mapper
public interface DpWebzineDao extends BaseDpWebzineDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseDpWebzineDao 클래스에 구현 되어있습니다.
	 * BaseDpWebzineDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public DpWebzine selectByPrimaryKey(DpWebzine dpWebzine) throws Exception;

	/**
	 * 웹진 리스트 조회
	 * 
	 * @Desc : 웹진 리스트 조회
	 * @Method Name : selectDpWebzineList
	 * @Date : 2019. 1. 31.
	 * @Author : SANTA
	 * @param pageable
	 * @return
	 */
	public List<DpWebzine> selectDpWebzineList(Pageable<DpWebzine, DpWebzine> pageable) throws Exception;

	/**
	 * 웹진 리스트 카운트 조회
	 * 
	 * @Desc :
	 * @Method Name : selectAdminNoticeListCount
	 * @Date : 2019. 2. 1.
	 * @Author : SANTA
	 * @param pageable
	 * @return
	 */
	public int selectDpWebzineCount(Pageable<DpWebzine, DpWebzine> pageable) throws Exception;

	/**
	 * 웹진 조회
	 * 
	 * @Desc : 웹진 조회
	 * @Method Name : selectDpWebzine
	 * @Date : 2019. 2. 1.
	 * @Author : SANTA
	 * @param dpWebzine
	 * @return
	 * @throws Exception
	 */
	public DpWebzine selectDpWebzine(DpWebzine dpWebzine) throws Exception;

	/**
	 * @Desc : OTS 웹진 목록 조회
	 * @Method Name : selectOtsDpWebzine
	 * @Date : 2019. 6. 5.
	 * @Author : hsjhsj19
	 * @param dpWebzine
	 * @return
	 * @throws Exception
	 */
	public List<DpWebzine> selectOtsDpWebzine(DpWebzine dpWebzine) throws Exception;

}
