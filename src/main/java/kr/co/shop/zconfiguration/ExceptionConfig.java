package kr.co.shop.zconfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import kr.co.shop.util.UtilsREST;
import kr.co.shop.util.UtilsResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionConfig {
	
	private void writeCondition(HttpServletRequest request,HttpServletResponse response,  Exception e) {
		e.printStackTrace();
    	UtilsResponse.writeJson(response, UtilsREST.responseMessage(response, e));
	}
	
	/**
	 * 어플리케이션 에러 처리
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
    @ExceptionHandler(Exception.class)
    public void handleError(HttpServletRequest request,HttpServletResponse response,  Exception e)   {
    	
    	//ajax 나 json 타입일 경우
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	writeCondition(request, response, e);    		
    }

    
    /**
     * controller에 등록된 action handler를 찾을 수 없을 경우 not found 처리
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleError404(HttpServletRequest request,HttpServletResponse response,  Exception e)   {

		response.setStatus(HttpStatus.NOT_FOUND.value());
    	writeCondition(request, response, e);    		

    }
}