package kr.co.shop.web.afterService.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.afterService.model.master.OcAsAcceptProduct;
import kr.co.shop.web.afterService.repository.master.base.BaseOcAsAcceptProductDao;

@Mapper
public interface OcAsAcceptProductDao extends BaseOcAsAcceptProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcAsAcceptProductDao 클래스에 구현 되어있습니다.
	 * BaseOcAsAcceptProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcAsAcceptProduct selectByPrimaryKey(OcAsAcceptProduct ocAsAcceptProduct) throws Exception;

	/**
	 * @Desc : A/S 신청 목록 갯수 조회
	 * @Method Name : selectAsAcceptProductListCount
	 * @Date : 2019. 3. 25.
	 * @Author : 이강수
	 * @param Pageable<OcAsAcceptProduct, OcAsAcceptProduct>
	 * @return int
	 * @throws Exception
	 */
	public int selectAsAcceptProductListCount(Pageable<OcAsAcceptProduct, OcAsAcceptProduct> pageable) throws Exception;

	/**
	 * @Desc : A/S 신청 목록 조회
	 * @Method Name : selectAsAcceptProductList
	 * @Date : 2019. 3. 25.
	 * @Author : 이강수
	 * @param Pageable<OcAsAcceptProduct, OcAsAcceptProduct>
	 * @return List<OcAsAcceptProduct>
	 * @throws Exception
	 */
	public List<OcAsAcceptProduct> selectAsAcceptProductList(Pageable<OcAsAcceptProduct, OcAsAcceptProduct> pageable)
			throws Exception;

	public void insertOcAsAcceptProduct(OcAsAcceptProduct ocAsAcceptProduct) throws Exception;

	public OcAsAcceptProduct selectAsAcceptProductDetailInfo(OcAsAcceptProduct ocAsAcceptProduct) throws Exception;

	public void updateOcAsProductPrdtStatCode(OcAsAcceptProduct ocAsAcceptProduct) throws Exception; 

	public void updateOcAsAcceptProduct(OcAsAcceptProduct ocAsAcceptProduct) throws Exception;

}
