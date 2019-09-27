package kr.co.shop.web.product.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.product.model.master.PdProductOptionPriceHistory;
import kr.co.shop.web.product.repository.master.base.BasePdProductOptionPriceHistoryDao;

@Mapper
public interface PdProductOptionPriceHistoryDao extends BasePdProductOptionPriceHistoryDao {

	/**
	 * 기본 insert, update, delete 메소드는 BasePdProductOptionPriceHistoryDao 클래스에 구현
	 * 되어있습니다. BasePdProductOptionPriceHistoryDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당
	 * 소스에서 작업 하시면 됩니다.
	 * 
	 */

	public PdProductOptionPriceHistory selectByPrimaryKey(PdProductOptionPriceHistory pdProductOptionPriceHistory)
			throws Exception;

	/**
	 * @Desc : 적용일 중 가장 최근 항목 조회
	 * @Method Name : selectMaxPrdtOptnPriceHistSeq
	 * @Date : 2019. 5. 2.
	 * @Author : hsjhsj19
	 * @param prdtNo
	 * @param prdtOptnNo
	 * @return
	 */
	public PdProductOptionPriceHistory selectMaxPrdtOptnPriceHistSeq(String prdtNo, String prdtOptnNo);

}
