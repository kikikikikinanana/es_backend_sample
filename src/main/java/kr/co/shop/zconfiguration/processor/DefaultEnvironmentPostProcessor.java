package kr.co.shop.zconfiguration.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;

import kr.co.shop.common.config.Config;
import lombok.extern.slf4j.Slf4j;

/***
 * spring boot 에서 @PropertySource 어노테이션은 .properteis 파일을 읽어 사용 할 수 있지만 늦게 Environment 에 구성 된다.
 * 실제 어플리케이션이 모든 환경 설정이 끝난 뒤 properties 항목을 호출 하면 잘 작동 하지만 @Configuration 같이 환경 설정 단계에서 Environment 객체를
 * 사용하려면 값이 제대로 나오지 않는다. 어플리케이션 구동 이전 환경 설정이 진행 중인 상태에서도 사용 하기 위해서 Processor을 따로 구현 해 준다. 해당 클래스 구현 처리는 스프링 부트의 권장 사항 이다.
 * 
 * @author jangcool
 * @link https://docs.spring.io/spring-boot/docs/2.0.x-SNAPSHOT/reference/htmlsingle/#howto-customize-the-environment-or-application-context
 */
@Slf4j
public class DefaultEnvironmentPostProcessor  implements EnvironmentPostProcessor {
	

	private final PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		//.properties 정보들이 들어 있는 ConfigurableEnvironment 객체 등록.
		Config.load(environment);
	}
}