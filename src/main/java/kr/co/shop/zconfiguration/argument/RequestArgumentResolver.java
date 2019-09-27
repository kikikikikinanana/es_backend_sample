package kr.co.shop.zconfiguration.argument;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import kr.co.shop.common.log.Log;
import kr.co.shop.common.request.Parameter;
import kr.co.shop.common.util.UtilsText;

public class RequestArgumentResolver extends Log implements HandlerMethodArgumentResolver {

	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Parameter.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
        ParameterizedType genericParameterType = (ParameterizedType) parameter.getGenericParameterType();
        Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
        String typeName = actualTypeArguments[0].getTypeName();
		
		//제네릭타임을 제외한 클래스 이름
		String className = UtilsText.matcherGroup("(.*)(<(.*),(.*)?>)?", typeName, 1);
		
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
			
		Model model = (Model)mavContainer.getModel();

		logger.debug("resolveArgument {}",response);
		Parameter<?> returnParameter = new Parameter<>(request, response, model);
		returnParameter.populateParameter(className);
		
		return returnParameter;
	}

	
	public static void main(String[] args) {
		RequestArgumentResolver a =  new RequestArgumentResolver();
		String data = "kr.co.shop.web.sample.vo.SampleVO[123]";
		String [] groups = UtilsText.matcherGroups("\\[([0-9]+)?\\]", data);
		System.out.println(UtilsText.matcherGroup("\\[[0-9]*?\\]", data,0));
		
		System.out.println(data.replaceAll("\\[[0-9]*?\\]", ""));
		
//		for (String string : groups) {
//			System.out.println(string);
//		}
//		
//		a.getRegexGroup("\\w+" + " (?<airline>..) (?<origin>...)\\." + "(?<number>\\d+)\\." + "(?<destination>...)"+ "\\[(?<deptDate>\\d+-\\d+-\\d+)\\]", data, "airline");
	}
	
}
