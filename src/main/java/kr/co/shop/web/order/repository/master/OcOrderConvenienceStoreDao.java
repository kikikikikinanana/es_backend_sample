package kr.co.shop.web.order.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.web.order.model.master.OcOrderConvenienceStore;
import kr.co.shop.web.order.repository.master.base.BaseOcOrderConvenienceStoreDao;

@Mapper
public interface OcOrderConvenienceStoreDao extends BaseOcOrderConvenienceStoreDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseOcOrderConvenienceStoreDao 클래스에 구현 되어있습니다.
	 * BaseOcOrderConvenienceStoreDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public OcOrderConvenienceStore selectByPrimaryKey(OcOrderConvenienceStore ocOrderConvenienceStore) throws Exception;

	/**
	 * @Desc :
	 * @Method Name : selectConvenienceStore
	 * @Date : 2019. 5. 24.
	 * @Author : flychani@3top.co.kr
	 * @param cvs
	 * @return
	 */
	public List<OcOrderConvenienceStore> selectConvenienceStore(OcOrderConvenienceStore cvs) throws Exception;

	/**
	 * @Desc : 편의점 정보 입력.
	 * @Method Name : insertCVS
	 * @Date : 2019. 6. 14.
	 * @Author : ljyoung@3top.co.kr
	 * @param ocOrderConvenienceStore
	 * @return
	 * @throws Exception
	 */
	public int insertCVS(OcOrderConvenienceStore ocOrderConvenienceStore) throws Exception;
}
