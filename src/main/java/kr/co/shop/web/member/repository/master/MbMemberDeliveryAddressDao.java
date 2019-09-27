package kr.co.shop.web.member.repository.master;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.member.model.master.MbMemberDeliveryAddress;
import kr.co.shop.web.member.repository.master.base.BaseMbMemberDeliveryAddressDao;

@Mapper
public interface MbMemberDeliveryAddressDao extends BaseMbMemberDeliveryAddressDao {

	/**
	 * 기본 insert, update, delete 메소드는 BaseMbMemberDeliveryAddressDao 클래스에 구현 되어있습니다.
	 * BaseMbMemberDeliveryAddressDao는 절대 수정 불가 하며 새로운 메소드 추가 하실 경우에는 해당 소스에서 작업 하시면
	 * 됩니다.
	 * 
	 */

	public MbMemberDeliveryAddress selectByPrimaryKey(MbMemberDeliveryAddress mbMemberDeliveryAddress) throws Exception;

	/**
	 * @Desc : 배송주소록 목록 개수
	 * @Method Name : selectMemberDeliveryAddressCount
	 * @Date : 2019. 4. 10.
	 * @Author : 유성민
	 * @param mbMemberDeliveryAddress
	 * @return
	 */
	public int selectMemberDeliveryAddressCount(MbMemberDeliveryAddress mbMemberDeliveryAddress) throws Exception;

	/**
	 * @Desc : 배송주소록 목록 페이징
	 * @Method Name : selectMemberDeliveryAddressPaging
	 * @Date : 2019. 4. 10.
	 * @Author : 유성민
	 * @param pageable
	 * @return
	 */
	public List<MbMemberDeliveryAddress> selectMemberDeliveryAddressPaging(
			Pageable<MbMemberDeliveryAddress, MbMemberDeliveryAddress> pageable) throws Exception;

	/**
	 * @Desc : 배송지 순번 생성
	 * @Method Name : selectDlvyAddrSeqNextVal
	 * @Date : 2019. 4. 19.
	 * @Author : 유성민
	 * @param params
	 * @return
	 */
	public int selectDlvyAddrSeqNextVal(MbMemberDeliveryAddress params) throws Exception;

	/**
	 * @Desc : 기본배송지 해제
	 * @Method Name : updateDefaultDeliveryAddressCancle
	 * @Date : 2019. 4. 19.
	 * @Author : 유성민
	 * @param params
	 * @return
	 */
	public int updateDefaultDeliveryAddressCancle(MbMemberDeliveryAddress params) throws Exception;

	/**
	 * @Desc : 회원 배송지 리스트를 조회
	 * @Method Name : selectMemberDeliveryList
	 * @Date : 2019. 4. 30.
	 * @Author : 이동엽
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public List<MbMemberDeliveryAddress> selectMemberDeliveryList(String memberNo) throws Exception;

}
