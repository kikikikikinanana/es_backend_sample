package kr.co.shop.constant;

import kr.co.shop.common.config.Config;
import kr.co.shop.common.constant.BaseConst;
import kr.co.shop.common.util.UtilsText;

public class Const extends BaseConst {
	public static final String URL_IMG_HTTPS = Config.getString("url.images.https");
	public static final String URL_IMG_UPLOAD_PATH = UtilsText.concat(URL_IMG_HTTPS,
			Config.getString("url.images.upload.path"));

	// 일일 휴대폰 인증번호 발송 제한 횟수
	public static final int CERTIFY_NUMBER_LIMIT_COUNT = Config.getInt("certify.number.limit.count", 3);
	// 휴대폰 인증번호 유효 시간(second)
	public static final int CERTIFY_NUMBER_VALID_TIME = Config.getInt("certify.number.valid.time", 180);

	public static final String[] HEADER_NAMES_FOR_CLIENT_IP = { "X-Forwarded-For", "Proxy-Client-IP",
			"WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };

	public static final String SYS_SENDER_EMAIL_ADDRESS = Config.getString("sys.sender.email.address");
	public static final String SYS_SENDER_EMAIL_NAME = Config.getString("sys.sender.email.name");

	public static final String SYS_SENDER_MESSAGE_NUMBER = Config.getString("sys.sender.message.number");
	public static final String SYS_SENDER_MESSAGE_NAME = Config.getString("sys.sender.message.name");

	public static final String SITE_PC_URL_LIST = Config.getString("site.pc.url.list");
	public static final String SITE_MOBILE_URL_LIST = Config.getString("site.mobile.url.list");
	public static final String CHANNEL_PC_URL_LIST = Config.getString("channel.pc.url.list");
	public static final String CHANNEL_MOBILE_URL_LIST = Config.getString("channel.mobile.url.list");
}
