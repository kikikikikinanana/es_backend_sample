package kr.co.shop.web.promotion.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.promotion.repository.master.base.BasePrPromotionTargetChannelDao;
import kr.co.shop.web.promotion.model.master.PrPromotionTargetChannel;

@Mapper
public interface PrPromotionTargetChannelDao extends BasePrPromotionTargetChannelDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePrPromotionTargetChannelDao 클래스에 구현 되어있습니다.
     * BasePrPromotionTargetChannelDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PrPromotionTargetChannel selectByPrimaryKey(PrPromotionTargetChannel prPromotionTargetChannel) throws Exception;

}
