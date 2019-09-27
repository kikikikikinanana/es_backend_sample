package kr.co.shop.web.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.shop.common.controller.BaseController;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsDevice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class MainController extends BaseController {

	@ApiOperation(value = "첫 화면 주 페이지(main/init)")
	@PostMapping("v1.0/main/init")
	public ResponseEntity<Map> main(Parameter<?> parameter) throws Exception {

		Map<String, Object> result = new HashMap<>();

		log.debug("############## main ");
		log.debug("############## param 한글 {} ", parameter.getString("name"));
		result.put("result", HttpStatus.OK);
		result.put("name", "post 방식의 rest api 호출 입니다.");
//		String s = UtilsRest.write(m);
		log.debug("Device DeviceUtils.getCurrentDevice(parameter.getRequest()).isMobile(): {}",
				UtilsDevice.getDevice(parameter.getRequest()).isMobile());
		log.debug("Device DeviceUtils.getCurrentDevice(parameter.getRequest()).isNormal(): {}",
				UtilsDevice.getDevice(parameter.getRequest()).isNormal());
		log.debug("Device DeviceUtils.getCurrentDevice(parameter.getRequest()).isTablet(): {}",
				UtilsDevice.getDevice(parameter.getRequest()).isTablet());
		log.debug("Device DeviceUtils.getCurrentDevice(parameter.getRequest()).getDevicePlatform(): {}",
				UtilsDevice.getDevice(parameter.getRequest()).getDevicePlatform());

		log.debug("Device parameter.isMobile(): {}", parameter.isMobile());
		log.debug("Device parameter.isPc(): {}", parameter.isPc());
		log.debug("Device parameter.isTablet(): {}", parameter.isTablet());
		log.debug("Device parameter.getDevicePlatform(): {}", parameter.getDevicePlatform());
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
}