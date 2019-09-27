package kr.co.shop.web.cart.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cart.model.master.OcCart;
import kr.co.shop.web.cart.repository.master.base.BaseOcCartDao;
import kr.co.shop.web.order.vo.OrderCartVo;

@Mapper
public interface OcCartDao extends BaseOcCartDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcCartDao 클래스에 구현 되어있습니다. BaseOcCartDao는
	 * 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public OcCart selectByPrimaryKey(OcCart ocCart) throws Exception;

	/**
	 * @Desc : 장바구니 상품정보
	 * @Method Name : getCartProductList
	 * @Date : 2019. 4. 9.
	 * @Author : flychani@3top.co.kr
	 * @return
	 */
	public List<OcCart> getCartProduct(OcCart ocCart) throws Exception;

	/**
	 * @Desc :회원 장바구니 상품 조회 ( 로그인 후)
	 * @Method Name : getMemberCartList
	 * @Date : 2019. 4. 17.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public List<OcCart> getMemberCartList(OcCart ocCart) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : getNonMemberCartList
	 * @Date : 2019. 4. 17.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public List<OcCart> getNonMemberCartList(OcCart ocCart) throws Exception;

	/**
	 * @Desc : 장바구니 장바구니 사은품 , 배송비 초기화
	 * @Method Name : setOcCartReset
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public int setOcCartReset(OcCart ocCart) throws Exception;

	/**
	 * @Desc : 회원 장바구니 비회원 장바구니 merge update
	 * @Method Name : setCartPrdeMerge
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param equalsList
	 * @return
	 */
	public int setCartPrdtMergeUpdate(List<OcCart> equalsList) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : setCartPrdeMergeDelete
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param equalsList
	 * @return
	 */
	public int setCartPrdtMergeDelete(List<OcCart> equalsList) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : setCartPrdtAddUpdate
	 * @Date : 2019. 4. 18.
	 * @Author : flychani@3top.co.kr
	 * @param nonMemExcludeList
	 * @return
	 */
	public int setCartPrdtAddUpdate(List<OcCart> nonMemExcludeList) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : setOcCartBenefitResetForMember
	 * @Date : 2019. 4. 19.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public int setOcCartBenefitResetForMember(OcCart ocCart) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : setOcCartBenefitResetForNonMember
	 * @Date : 2019. 4. 19.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public int setOcCartBenefitResetForNonMember(OcCart ocCart) throws Exception;

	/**
	 * @Desc : 주문을 위해 선택된 장바구니 정보를 가져온다.
	 * @Method Name : getCartList
	 * @Date : 2019. 4. 11.
	 * @Author : ljyoung@3top.co.kr
	 * @param orderVo
	 * @return
	 * @throws Exception
	 */
	public List<OcCart> selectCartList(OrderCartVo orderVo) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : setCartInsert
	 * @Date : 2019. 4. 22.
	 * @Author : flychani@3top.co.kr
	 * @param addCartList
	 * @return
	 */
	public int setCartInsert(List<OcCart> addCartList) throws Exception;

	/**
	 * @Desc : 장바구니 상품 등록
	 * @Method Name : setCartPrdtInsert
	 * @Date : 2019. 4. 24.
	 * @Author : flychani@3top.co.kr
	 * @param cartPrdt
	 */
	public void setCartPrdtInsert(OcCart cartPrdt) throws Exception;

	/**
	 * @Desc : 장바구니 상품 삭제
	 * @Method Name : setCartPrdtDelete
	 * @Date : 2019. 4. 26.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public int setCartPrdtDelete(OcCart params) throws Exception;

	/**
	 * @Desc :상품 수량 수정 저장
	 * @Method Name : setCartPrtdQtySave
	 * @Date : 2019. 4. 28.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public int setCartPrtdQtySave(OcCart params) throws Exception;

	/**
	 * @Desc : 상품 옵션 수정 저장
	 * @Method Name : setCartPrdtOptionSave
	 * @Date : 2019. 5. 16.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public int setCartPrdtOptionSave(OcCart params) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : getCartCounting
	 * @Date : 2019. 6. 14.
	 * @Author : flychani@3top.co.kr
	 * @param ocCart
	 * @return
	 */
	public int getCartCounting(OcCart ocCart) throws Exception;
}
