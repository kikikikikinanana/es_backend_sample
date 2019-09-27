package kr.co.shop.web.claim.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.claim.model.master.OcClaim;
import kr.co.shop.web.claim.model.master.OcClaimProduct;
import kr.co.shop.web.claim.repository.master.base.BaseOcClaimProductDao;
import kr.co.shop.web.claim.vo.OcClaimPromoVO;

@Mapper
public interface OcClaimProductDao extends BaseOcClaimProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcClaimProductDao 클래스에 구현 되어있습니다.
	 * BaseOcClaimProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcClaimProduct selectByPrimaryKey(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 클레임 상품 목록 조회
	 * @Method Name : selectClaimProductList
	 * @Date : 2019. 4. 10.
	 * @Author : 이강수
	 * @param OcClaim
	 * @return List<OcClaimProduct>
	 * @throws Exception
	 */
	public List<OcClaimProduct> selectClaimProductList(OcClaim ocClaim) throws Exception;

	/**
	 * @Desc : 원 주문 클레임 상품 목록 조회 쿼리
	 * @Method Name : selectOrgClaimProductList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param ocClaimProduct
	 * @return
	 * @throws Exception
	 */
	public List<OcClaimProduct> selectOrgClaimProductList(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 클레임상품 상태코드 업데이트
	 * @Method Name : updateClaimProductStatCode
	 * @Date : 2019. 4. 19.
	 * @Author : 이강수
	 * @param ocClaimProduct
	 * @return int
	 * @throws Exception
	 */
	public int updateClaimProductStatCode(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 클레임사유코드/클레임기타사유 업데이트
	 * @Method Name : updateClmRsnCodeText
	 * @Date : 2019. 4. 29.
	 * @Author : 이강수
	 * @param ocClaimProduct
	 * @return int
	 * @throws Exception
	 */
	public int updateClmRsnCodeText(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 교환대상 상품 수정
	 * @Method Name : updateClmChangeOptn
	 * @Date : 2019. 7. 1.
	 * @Author : KTH
	 * @param ocClaimProduct
	 * @return
	 * @throws Exception
	 */
	public int updateClmChangeOptn(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 클레임 상품 저장
	 * @Method Name : insertClaimProduct
	 * @Date : 2019. 5. 9.
	 * @Author : KTH
	 * @param ocClaimProduct
	 * @return
	 * @throws Exception
	 */
	public int insertClaimProduct(OcClaimProduct ocClaimProduct) throws Exception;

	/**
	 * @Desc : 원 주문에 걸린 다족구매 프로모션 현황 조회(현재 클레임 포함)
	 * @Method Name : selectOrderMultiBuyPromoCheckList
	 * @Date : 2019. 5. 27.
	 * @Author : KTH
	 * @param ocClaimProduct
	 * @return
	 * @throws Exception
	 */
	public List<OcClaimPromoVO> selectOrderMultiBuyPromoCheckList(OcClaimProduct ocClaimProduct) throws Exception;

}
