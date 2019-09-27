package kr.co.shop.web.event.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.event.model.master.EvEventTargetProduct;
import kr.co.shop.web.event.repository.master.base.BaseEvEventTargetProductDao;

@Mapper
public interface EvEventTargetProductDao extends BaseEvEventTargetProductDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseEvEventTargetProductDao 클래스에 구현 되어있습니다.
	 * BaseEvEventTargetProductDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public EvEventTargetProduct selectByPrimaryKey(EvEventTargetProduct evEventTargetProduct) throws Exception;

	/**
	 * 런칭캘린더 대상 상품 top1 조회 쿼리
	 * 
	 * @Desc :
	 * @Method Name : selectDrawTargetProduct
	 * @Date : 2019. 5. 28
	 * @Author : easyhun
	 * @param
	 * @throws Exception
	 */
	public EvEventTargetProduct selectDrawTargetProduct(EvEventTargetProduct evEventTargetProduct) throws Exception;

	/**
	 * 이벤트 상세(드로우 대상 상품 리스트 조회 쿼리)
	 * 
	 * @Desc :
	 * @Method Name : selectDrawTargetProductList
	 * @Date : 2019. 7. 05
	 * @Author : easyhun
	 * @param
	 * @throws Exception
	 */
	public List<EvEventTargetProduct> selectDrawTargetProductListByEventNo(String eventNo) throws Exception;

}
