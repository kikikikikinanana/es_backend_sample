package kr.co.shop.web.system.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.system.model.master.SyCodeDetail;
import kr.co.shop.web.system.repository.master.SyCodeDetailDao;

@Service
public class CommonCodeService {

	@Autowired
	private SyCodeDetailDao syCodeDetailDao;

	/**
	 * @Desc : 사용중인 코드를 조회한다.
	 * @Method Name : getUseCode
	 * @Date : 2019. 2. 1.
	 * @Author : 신인철
	 * @param codeField
	 * @return
	 * @throws Exception
	 */
	public List<SyCodeDetail> getUseCode(String codeField) throws Exception {
		List<SyCodeDetail> codeList = syCodeDetailDao.selectUseCode(codeField);
		return codeList;
	}

	/**
	 * @Desc : 사용중이지 않은 코드를 포함하여 조회한다.
	 * @Method Name : getCode
	 * @Date : 2019. 2. 12.
	 * @Author : 신인철
	 * @param codeField
	 * @return
	 * @throws Exception
	 */
	public List<SyCodeDetail> getCode(String codeField) throws Exception {
		List<SyCodeDetail> codeList = syCodeDetailDao.selectCode(codeField);
		return codeList;
	}

	/**
	 * @Desc : codeField와 addinfo1로 코드 번호와 이름 조회
	 * @Method Name : getUseCodeByAddInfo1
	 * @Date : 2019. 3. 11.
	 * @Author : Kimyounghyun
	 * @param codeField
	 * @param addInfo1
	 * @return
	 * @throws Exception
	 */
	public List<SyCodeDetail> getUseCodeByAddInfo1(SyCodeDetail syCodeDetail) throws Exception {
		List<SyCodeDetail> codeList = syCodeDetailDao.selectUseCodeByAddInfo1(syCodeDetail);
		return codeList;
	}

	/**
	 * @Desc : 여러개의 코드정보를 가져올때 이용
	 * @Method Name : getUseCodeByGroup
	 * @Date : 2019. 2. 1.
	 * @Author : 유성민
	 * @param codeFields
	 * @return
	 */
	public Map<String, List<SyCodeDetail>> getUseCodeByGroup(String[] codeFields) throws Exception {
		List<SyCodeDetail> codeList = syCodeDetailDao.selectUseCodeByGroup(codeFields);
		if (codeList == null) {
			return null;
		}
		return codeList.stream().collect(Collectors.groupingBy(SyCodeDetail::getCodeField));
	}

	/**
	 * @Desc : 사용중이지 않은 코드들도 함께 조회
	 * @Method Name : getCodeByGroup
	 * @Date : 2019. 2. 12.
	 * @Author : 신인철
	 * @param codeFields
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<SyCodeDetail>> getCodeByGroup(String[] codeFields) throws Exception {
		List<SyCodeDetail> codeList = syCodeDetailDao.selectCodeByGroup(codeFields);
		if (codeList == null) {
			return null;
		}
		return codeList.stream().collect(Collectors.groupingBy(SyCodeDetail::getCodeField));
	}
}
