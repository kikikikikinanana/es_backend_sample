package kr.co.shop.web.product.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdRelationProduct;
import kr.co.shop.web.product.repository.master.base.BasePdRelationProductDao;

@Mapper
public interface PdRelationProductDao extends BasePdRelationProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdRelationProductDao 클래스에 구현 되어있습니다.
	 * BasePdRelationProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PdRelationProduct selectByPrimaryKey(PdRelationProduct pdRelationProduct) throws Exception;

	/**
	 * @Desc : 연관상품 목록 조회
	 * @Method Name : selectRelationProductTop30List
	 * @Date : 2019. 5. 15.
	 * @Author : hsjhsj19
	 * @param pdRelationProduct
	 * @return
	 * @throws Exception
	 */
	public List<PdProduct> selectRelationProductTop30List(PdRelationProduct pdRelationProduct) throws Exception;

	/**
	 * @Desc : 연계상품번호 조회
	 * @Method Name : selectRltnPrdtNo
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @param relationProduct
	 * @return
	 * @throws Exception
	 */
	public List<String> selectRltnPrdtNo(PdRelationProduct relationProduct) throws Exception;

}
