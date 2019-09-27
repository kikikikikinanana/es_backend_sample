package kr.co.shop.web.system.service;

import org.springframework.stereotype.Service;

import kr.co.shop.common.util.UtilsRequest;
import kr.co.shop.constant.Const;
import kr.co.shop.util.UtilsText;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemService {

	public String getIpAddress() {
		for (String header : Const.HEADER_NAMES_FOR_CLIENT_IP) {
			String ipAddress = UtilsRequest.getRequest().getHeader(header);
			if (UtilsText.isNotEmpty(ipAddress)) {
				return UtilsText.trim(UtilsText.split(ipAddress, ",")[0]);
			}
		}
		return UtilsRequest.getRequest().getRemoteAddr();
	}
}
