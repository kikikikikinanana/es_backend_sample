package kr.co.shop.web.product.repository.master;

import org.apache.ibatis.annotations.Mapper;
import kr.co.shop.web.product.repository.master.base.BasePdProductRelationFileDao;
import kr.co.shop.web.product.model.master.PdProductRelationFile;

@Mapper
public interface PdProductRelationFileDao extends BasePdProductRelationFileDao {
	
     /**
     * 기본 insert, update, delete 메소드는 BasePdProductRelationFileDao 클래스에 구현 되어있습니다.
     * BasePdProductRelationFileDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면 됩니다.
     * 
     */

	public PdProductRelationFile selectByPrimaryKey(PdProductRelationFile pdProductRelationFile) throws Exception;

}
