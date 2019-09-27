package kr.co.shop.web.claim.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.claim.model.master.OcClaim;
import kr.co.shop.web.claim.model.master.OcClaimProduct;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimDao;

@Mapper
public interface OcClaimDao extends BaseOcClaimDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcClaimDao 클래스에 구현 되어있습니다. BaseOcClaimDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcClaim selectByPrimaryKey(OcClaim ocClaim) throws Exception;

	/**
	 * @Desc : 클레임 페이징을 위한 클레임 번호 목록 갯수 카운트
	 * @Method Name : selectClaimListCountForPaging
	 * @Date : 2019. 4. 10.
	 * @Author : 이강수
	 * @param Pageable<OcClaim, OcClaim> pageable
	 * @return int
	 * @throws Exception
	 */
	public int selectClaimListCountForPaging(Pageable<OcClaim, OcClaim> pageable) throws Exception;

	/**
	 * @Desc : 클레임 페이징을 위한 클레임 번호 목록 조회
	 * @Method Name : selectClaimListForPaging
	 * @Date : 2019. 4. 10.
	 * @Author : 이강수
	 * @param Pageable<OcClaim, OcClaim> pageable
	 * @return List<OcClaim>
	 * @throws Exception
	 */
	public List<OcClaim> selectClaimListForPaging(Pageable<OcClaim, OcClaim> pageable) throws Exception;

	/**
	 * @Desc : 클레임 접수철회 업데이트
	 * @Method Name : updateClaimWthdraw
	 * @Date : 2019. 4. 19.
	 * @Author : 이강수
	 * @param OcClaim
	 * @return int
	 * @throws Exception
	 */
	public int updateClaimWthdraw(OcClaim ocClaim) throws Exception;

	/**
	 * @Desc : 클레임 상세 조회
	 * @Method Name : selectClaimDetail
	 * @Date : 2019. 4. 22.
	 * @Author : 이강수, KTH
	 * @param OcClaim
	 * @return OcClaim
	 * @throws Exception
	 */
	public OcClaim selectClaimDetail(OcClaim ocClaim) throws Exception;

	/**
	 * @Desc : 클레임 상세 업데이트
	 * @Method Name : updateClaimDetail
	 * @Date : 2019. 4. 30.
	 * @Author : 이강수
	 * @param OcClaim
	 * @return int
	 * @throws Exception
	 */
	public int updateClaimDetail(OcClaim ocClaim) throws Exception;

	/**
	 * @Desc : 클레임 등록
	 * @Method Name : insertClaimInfo
	 * @Date : 2019. 5. 9.
	 * @Author : KTH
	 * @param ocClaim
	 * @return
	 * @throws Exception
	 */
	public int insertClaimInfo(OcClaim ocClaim) throws Exception;

	/**
	 * @Desc : 클레임 주문금액합계/배송비합계 조회
	 * @Method Name : selectSumOrderAmtSumDlvyAmt
	 * @Date : 2019. 6. 18.
	 * @Author : 이강수
	 * @param OcClaimProduct
	 * @return OcClaim
	 * @throws Exception
	 */
	public OcClaim selectSumOrderAmtSumDlvyAmt(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 클레임 상태 업데이트
	 * @Method Name : updateClaimStat
	 * @Date : 2019. 6. 20.
	 * @Author : KTH
	 * @param ocClaim
	 * @return
	 * @throws Exception
	 */
	public int updateClaimStat(OcClaim ocClaim) throws Exception;

}
