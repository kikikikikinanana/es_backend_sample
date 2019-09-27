package kr.co.shop.zconfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(
			Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE));

	private ApiInfo apiInfo(String version, String groupName, String url) {
		return new ApiInfoBuilder()
				.title("ABCMart 2.0 REST API").description(UtilsText.concat("ABCMart 2.0 Backed API 서버의  <b>[", version,
						" ", groupName, "] API URL=", "/api/", version, url, "</b> 정보를 알 수 있다."))
				.version("v1.0").build();
	}

	private Docket docket(String version, String groupName, String path) {
		// Controller에서 넘어오는 parameter type class등은 제외
		return new Docket(DocumentationType.SWAGGER_2).groupName(UtilsText.concat(version, " ", groupName))
				.ignoredParameterTypes(Parameter.class).apiInfo(apiInfo(version, groupName, path))
				// contentType으로 값이 넘어올 경우 판단.
				.consumes(DEFAULT_PRODUCES_AND_CONSUMES).select()
				// 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
				.apis(RequestHandlerSelectors.basePackage("kr.co.shop.web"))
				.paths(PathSelectors.ant(UtilsText.concat("/api/", version, path))).build();
	}

	@Bean
	public Docket apiAllV10() {
		return docket("v1.0", "전체", "/**");
	}

	@Bean
	public Docket apiAllV11() {
		return docket("v1.1", "전체", "/**");
	}

	@Bean
	public Docket apiMainV10() {
		return docket("v1.0", "주 화면-main", "/main/**");
	}

	@Bean
	public Docket apiMemberV10() {
		return docket("v1.0", "회원-member", "/member/**");
	}

	@Bean
	public Docket apiLoginV10() {
		return docket("v1.0", "로그인-login", "/login/**");
	}

	@Bean
	public Docket apiTermsV10() {
		return docket("v1.0", "약관-terms", "/terms/**");
	}

	@Bean
	public Docket apiDisplayV10() {
		return docket("v1.0", "전시", "/display/**");
	}

	@Bean
	public Docket apiPromotionV10() {
		return docket("v1.0", "프로모션", "/promotion/**");
	}

	@Bean
	public Docket apiProductV10() {
		return docket("v1.0", "상품", "/product/**");
	}
}