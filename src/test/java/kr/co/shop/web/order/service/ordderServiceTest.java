package kr.co.shop.web.order.service;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.shop.web.order.model.master.OcOrder;
import kr.co.shop.web.order.model.master.OcOrderProduct;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ordderServiceTest {

	@Autowired
	OrderService orderService;

	void testOrderStock() throws Exception {

		ArrayList<OcOrderProduct> oopList = new ArrayList<OcOrderProduct>();

		OcOrderProduct a1 = new OcOrderProduct();
		a1.setPrdtNo("prd1");
		a1.setPrdtOptnNo("opt1");
		a1.setOrderQty(2);
		a1.setPrdtNoPrdtOptnNo("prd1opt1");
		a1.setAwQty(80);
		a1.setAiQty(55);
		a1.setAsQty(20);
		oopList.add(a1);

		OcOrderProduct a2 = new OcOrderProduct();
		a2.setPrdtNo("prd1");
		a2.setPrdtOptnNo("opt1");
		a2.setOrderQty(1);
		a2.setPrdtNoPrdtOptnNo("prd1opt1");
		a2.setAwQty(80);
		a2.setAiQty(55);
		a2.setAsQty(20);
		oopList.add(a2);

		OcOrderProduct a22 = new OcOrderProduct();
		a22.setPrdtNo("prd1");
		a22.setPrdtOptnNo("opt2");
		a22.setOrderQty(1);
		a22.setAwQty(80);
		a22.setAiQty(55);
		a22.setAsQty(20);
		a22.setPrdtNoPrdtOptnNo("prd1opt2");
		oopList.add(a22);

		OcOrderProduct a3 = new OcOrderProduct();
		a3.setPrdtNo("prd2");
		a3.setPrdtOptnNo("opt2");
		a3.setOrderQty(2);
		a3.setPrdtNoPrdtOptnNo("prd2opt2");
		a3.setAwQty(80);
		a3.setAiQty(55);
		a3.setAsQty(20);
		oopList.add(a3);

		OcOrderProduct a4 = new OcOrderProduct();
		a4.setPrdtNo("prd2");
		a4.setPrdtOptnNo("opt2");
		a4.setOrderQty(2);
		a4.setPrdtNoPrdtOptnNo("prd2opt2");
		a4.setAwQty(80);
		a4.setAiQty(55);
		a4.setAsQty(20);
		oopList.add(a4);

		boolean rtn = orderService.updateOrderProductStock(oopList, "N");

		// log.debug("testGetAvailableCouponCount : {}", cnt);

	}

	@Test
	void testOrderOrder() throws Exception {
		OcOrder orderPara = new OcOrder();
		orderPara.setOrderNo("201902130004");

		orderService.getOrderDetailInfo(orderPara);

		// log.debug("testGetAvailableCouponCount : {}", cnt);

	}

}
