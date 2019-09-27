package kr.co.shop.web.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.web.product.model.master.PdProduct;
import kr.co.shop.web.product.model.master.PdRelationProduct;
import kr.co.shop.web.product.repository.master.PdRelationProductDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductRelationService {

	@Autowired
	private PdRelationProductDao relationProductDao;

	/**
	 * @Desc : 추천,베스트,신상 연관상품 목록 조회
	 * @Method Name : selectRelationProductRBNList
	 * @Date : 2019. 5. 16.
	 * @Author : hsjhsj19
	 * @param pdRelationProduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectRelationProductRBNList(PdRelationProduct pdRelationProduct) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<PdProduct> list = relationProductDao.selectRelationProductTop30List(pdRelationProduct);

		List<PdProduct> recommendList = new ArrayList<PdProduct>();
		List<PdProduct> bestList = new ArrayList<PdProduct>();
		List<PdProduct> newList = new ArrayList<PdProduct>();

		for (PdProduct product : list) {
			if ("R".equals(product.getType())) {
				recommendList.add(product);
			} else if ("B".equals(product.getType())) {
				bestList.add(product);
			} else if ("N".equals(product.getType())) {
				newList.add(product);
			}
		}

		// 동일 소카테고리(전시카테고리)의 베스트 상품 30개 추출 후 랜덤 노출
		resultMap.put("relationProducts", getOnly15List(recommendList));
		// 동일 브랜드 의 베스트 상품 30개 추출 후 랜덤 노출
		resultMap.put("bestRelationProducts", getOnly15List(bestList));
		// 동일 브랜드의 신상품 30개 추출 후 랜덤 노출
		resultMap.put("newRelationProducts", getOnly15List(newList));
		return resultMap;
	}

	/**
	 * @Desc : 무작위로 15개 선별
	 * @Method Name : getOnly15List
	 * @Date : 2019. 5. 16.
	 * @Author : hsjhsj19
	 * @param list
	 * @return
	 */
	private List<PdProduct> getOnly15List(List<PdProduct> list) {
		if (15 >= list.size()) {
			Random random = new Random();
			for (int i = 0; i < list.size(); i++) {
				PdProduct prod = list.get(i);
				int index = i + random.nextInt(list.size() - i);
				list.set(i, list.get(index));
				list.set(index, prod);
			}
			return list;
		}
		while (true) {
			int newInt = (int) (Math.random() * list.size());
			try {
				list.remove(newInt);
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
			if (list.size() == 15) {
				break;
			}
		}
		return list;
	}

	/**
	 * @Desc : 상품번호에 의한 연관상품 번호 조회
	 * @Method Name : getRltnPrdtNo
	 * @Date : 2019. 6. 3.
	 * @Author : tennessee
	 * @param relationProduct
	 * @return
	 * @throws Exception
	 */
	public List<String> getRltnPrdtNo(PdRelationProduct relationProduct) throws Exception {
		return this.relationProductDao.selectRltnPrdtNo(relationProduct);
	}

}
