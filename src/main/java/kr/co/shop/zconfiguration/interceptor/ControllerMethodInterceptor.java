package kr.co.shop.zconfiguration.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.co.shop.common.util.UtilsText;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ControllerMethodInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HandlerMethod handlerMethod = getHandlerMethod(handler);


        //※[고정] 수정 금지
        //Controller 메소드가 void 일 경우에 Spring view에서 자동으로 ModelAndView객체를 생성한다.
        //이때 jsp페이지로 이동 하여 404에러가 뜨는데 void는 필요 없으므로 ModelAndView 객체를 비워준다.
        if (handlerMethod != null && handlerMethod.isVoid() && modelAndView != null) {
            
        	if (UtilsText.isBlank(response.getContentType())) {
                response.setContentType(MediaType.TEXT_HTML_VALUE);
            }
            
        	response.setStatus(HttpStatus.OK.value());
            modelAndView.clear();
        }

    }

    /**
     * HandlerMethod를 리턴 한다. 
     * @param handler HandlerMethod
     * @return HandlerMethod
     */
    private HandlerMethod getHandlerMethod(Object handler) {

        if (handler instanceof HandlerMethod) {
            return (HandlerMethod) handler;
        }

        return null;
    }
}