package kr.co.shop.web.customer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.util.UtilsREST;
import kr.co.shop.web.customer.service.PolicyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class PolicyController extends BaseController {
	@Autowired
	PolicyService policyService;

	/**
	 *
	 * @Desc      	: 온라인 회원의 혜택 정보를 가져온다.
	 * @Method Name : getPolicyV10
	 * @Date  	  	: 2019. 6. 27.
	 * @Author    	: 최경호
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "시행중인 온라인 혜택의 데이터를 조회", notes = "시행중인 온라인 혜택의 데이터를 조회[기본정보, 쿠폰정보를 Map으로 리턴]", httpMethod = "GET", protocols = "http", response = Map.class)
	@GetMapping(value = "v1.0/customer/policy/select", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOnlinePolicy(Parameter<?> parameter) throws Exception {
		Map<String, Object> result = policyService.getOnlinePolicyDataProcess();

//		System.out.println("onlinePolicyData >>>" + result.get("onlinePolicyData"));
//		System.out.println("========================================================================");
//		System.out.println("onlinePolicyDetail >>>" + result.get("onlinePolicyDetail"));

		return UtilsREST.responseOk(parameter, result);
	}
}