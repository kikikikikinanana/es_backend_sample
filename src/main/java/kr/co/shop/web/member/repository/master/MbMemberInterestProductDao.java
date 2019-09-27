package kr.co.shop.web.member.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.cart.vo.OcCartInfo;
import kr.co.shop.web.member.model.master.MbMemberInterestProduct;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberInterestProductDao;
import kr.co.shop.web.product.model.master.MbMemberInterestProductWrapper;
import kr.co.shop.web.product.model.master.PageableProduct;

@Mapper
public interface MbMemberInterestProductDao extends BaseMbMemberInterestProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberInterestProductDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberInterestProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public MbMemberInterestProduct selectByPrimaryKey(MbMemberInterestProduct mbMemberInterestProduct) throws Exception;

	/**
	 * @Desc : 회원관심상품 등록
	 * @Method Name : insertWithPrimaryKey
	 * @Date : 2019. 5. 20.
	 * @Author : hsjhsj19
	 * @param mbMemberInterestProduct
	 * @return
	 * @throws Exception
	 */
	public int insertWithPrimaryKey(MbMemberInterestProduct mbMemberInterestProduct) throws Exception;

	/**
	 * @Desc : 회원관심상품 삭제
	 * @Method Name : deleteInterestProduct
	 * @Date : 2019. 5. 27.
	 * @Author : hsjhsj19
	 * @param interestProduct
	 * @return
	 * @throws Exception
	 */
	public int deleteInterestProduct(MbMemberInterestProduct interestProduct) throws Exception;

	/**
	 * @Desc : 회원 관심상품 목록 조회
	 * @Method Name : selectMemberInterestProductList
	 * @Date : 2019. 5. 28.
	 * @Author : hsjhsj19
	 * @param interestProduct
	 * @return
	 */
	public List<MbMemberInterestProduct> selectMemberInterestProductList(MbMemberInterestProduct interestProduct)
			throws Exception;

	/**
	 * @Desc : 회원 관심상품 여부 목록 조회
	 * @Method Name : selectMemberInterestProductYnList
	 * @Date : 2019. 6. 5.
	 * @Author : 이가영
	 * @param interestProduct
	 * @return
	 */
	public List<MbMemberInterestProduct> selectMemberInterestProductYnList(MbMemberInterestProduct interestProduct)
			throws Exception;

	/**
	 * @Desc : 회원 관심상품 TOP5 목록 조회
	 * @Method Name : getWishProdutListTopFive
	 * @Date : 2019. 6. 15.
	 * @Author : flychani@3top.co.kr
	 * @param params
	 * @return
	 */
	public List<OcCartInfo> getWishProdutListTopFive(OcCartInfo params) throws Exception;

	/**
	 * @Desc : 괸심상품 목록조회
	 * @Method Name : selectDisplayInterestProductList
	 * @Date : 2019. 6. 25.
	 * @Author : hsjhsj19
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberInterestProductWrapper> selectDisplayInterestProductList(
			PageableProduct<MbMemberInterestProduct, MbMemberInterestProductWrapper> pageableProduct) throws Exception;

	/**
	 * @Desc : 괸심상품 갯수조회
	 * @Method Name : selectMemberInterestProductCount
	 * @Date : 2019. 7. 4.
	 * @Author : hsjhsj19
	 * @param interestProduct
	 * @return
	 */
	public Integer selectMemberInterestProductCount(MbMemberInterestProduct interestProduct) throws Exception;

	/**
	 * @Desc : 괸심상품 갯수조회
	 * @Method Name : selectDisplayProductTotalCount
	 * @Date : 2019. 7. 11.
	 * @Author : hsjhsj19
	 * @param pageableProduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectDisplayProductTotalCount(
			PageableProduct<MbMemberInterestProduct, MbMemberInterestProductWrapper> pageableProduct) throws Exception;

}
