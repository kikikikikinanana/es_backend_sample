package kr.co.shop.web.giftcard.repository.master;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.giftcard.model.master.OcKakaoExchangeOrder;
import kr.co.shop.web.giftcard.repository.master.base.BaseOcKakaoExchangeOrderDao;

@Mapper
public interface OcKakaoExchangeOrderDao extends BaseOcKakaoExchangeOrderDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcKakaoExchangeOrderDao 클래스에 구현 되어있습니다.
	 * BaseOcKakaoExchangeOrderDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public OcKakaoExchangeOrder selectByPrimaryKey(OcKakaoExchangeOrder ocKakaoExchangeOrder) throws Exception;

	/**
	 * @Desc : 카카오톡 쿠폰 정보와 선물하기 전문 내용 저장
	 * @Method Name : insertKakaoCouponExchange
	 * @Date : 2019. 5. 20.
	 * @Author : nalpari
	 * @param ocKakaoExchangeOrder
	 * @throws Exception
	 */
	public void insertKakaoCouponExchange(OcKakaoExchangeOrder ocKakaoExchangeOrder) throws Exception;

	/**
	 * @Desc : 카카오톡 쿠폰 교환 정보 조회
	 * @Method Name : insertKakaoCouponExchange
	 * @Date : 2019. 5. 20.
	 * @Author : nalpari
	 * @param ocKakaoExchangeOrder
	 * @throws Exception
	 */
	public OcKakaoExchangeOrder selectKakaoExchangeInfo(OcKakaoExchangeOrder ocKakaoExchangeOrder) throws Exception;
}
