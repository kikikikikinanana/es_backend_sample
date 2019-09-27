package kr.co.shop.web.product.repository.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.PdProductOption;
import kr.co.shop.web.product.model.master.PdProductOptionStock;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockAndPrice;
import kr.co.shop.web.product.model.master.PdProductOptionWithStockOnlyOne;
import kr.co.shop.web.product.model.master.PdProductWithAll;
import kr.co.shop.web.product.repository.master.base.BasePdProductOptionDao;

@Mapper
public interface PdProductOptionDao extends BasePdProductOptionDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdProductOptionDao 클래스에 구현 되어있습니다.
	 * BasePdProductOptionDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 *
	 */

	public PdProductOption selectByPrimaryKey(PdProductOption pdProductOption) throws Exception;

	/**
	 * @Desc : 상품옵션 목록 재고와 가격까지 함께 조회 (서비스 요청서)
	 * @Method Name : selectProductOptionListWithStockAndPrice
	 * @Date : 2019. 4. 24.
	 * @Author : hsjhsj19
	 * @param optionWithStockAndPrice
	 * @return
	 * @throws Exception
	 */
	public List<PdProductOptionWithStockAndPrice> selectProductOptionListWithStockAndPrice(
			PdProductOptionWithStockAndPrice optionWithStockAndPrice) throws Exception;

	/**
	 * @Desc : 상품 옵션 유효성 검사
	 * @Method Name : productOptionValidator
	 * @Date : 2019. 4. 22.
	 * @Author : hsjhsj19
	 * @param option
	 * @return
	 * @throws Exception
	 */
	public List<String> productOptionValidator(PdProductOption option) throws Exception;

	/**
	 * @Desc : 상품 옵션 단건과 재고량 함께 조회
	 * @Method Name : selectProductOptionWithStock
	 * @Date : 2019. 5. 21.
	 * @Author : hsjhsj19
	 * @param prdt
	 * @return
	 * @throws Exception
	 */
	public PdProductOptionWithStockAndPrice selectProductOptionWithStock(PdProductWithAll prdt) throws Exception;

	/**
	 * @Desc : 상품옵션 목록 재고와 가격까지 함께 조회 (서비스 요청서)
	 * @Method Name : selectProductOptionListWithStock
	 * @Date : 2019. 4. 24.
	 * @Author : hsjhsj19
	 * @param productWithAll
	 * @return
	 * @throws Exception
	 */
	public List<PdProductWithAll> selectProductOptionListWithStock(PdProductWithAll productWithAll) throws Exception;

	/**
	 *
	 * @Desc :
	 * @Method Name : selectProductOptionListWithStockOnlyOne
	 * @Date : 2019. 6. 4.
	 * @Author : NKB
	 * @param optionWithStockAndPrice
	 * @return
	 * @throws Exception
	 */
	public PdProductOptionWithStockOnlyOne selectProductOptionListWithStockOnlyOne(
			PdProductOptionWithStockAndPrice optionWithStockAndPrice) throws Exception;

	/**
	 * @Desc : 상품 재고 수량을 조회 한다.
	 * @Method Name : selectProductOptionStock
	 * @Date : 2019. 6. 20.
	 * @Author : kiowa
	 * @param productOptionStock
	 * @return
	 * @throws Exception
	 */
	public List<PdProductOptionStock> selectProductOptionStock(List<Map<String, Object>> params) throws Exception;
}
