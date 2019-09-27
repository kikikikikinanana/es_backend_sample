package kr.co.shop.web.product.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.PdProductPriceHistory;
import kr.co.shop.web.product.repository.master.base.BasePdProductPriceHistoryDao;

@Mapper
public interface PdProductPriceHistoryDao extends BasePdProductPriceHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdProductPriceHistoryDao 클래스에 구현 되어있습니다.
	 * BasePdProductPriceHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public PdProductPriceHistory selectByPrimaryKey(PdProductPriceHistory pdProductPriceHistory) throws Exception;

	/**
	 * @Desc : 적용일 중에 가장 최근 가격 이력 조회
	 * @Method Name : selectMaxPrdtPriceHistSeq
	 * @Date : 2019. 4. 29.
	 * @Author : hsjhsj19
	 * @param productPrice
	 * @return
	 */
	public PdProductPriceHistory selectMaxPrdtPriceHistSeq(PdProductPriceHistory productPrice) throws Exception;

}
