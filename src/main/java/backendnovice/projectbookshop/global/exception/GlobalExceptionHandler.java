/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-17
 * @modified : 2023-09-18
 * @desc     : 전 영역 사용자 정의 예외 핸들링 클래스.
 */

package backendnovice.projectbookshop.global.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * CallErrorPageException이 호출되면 에러 엔드포인트를 반환한다.
     * @param exception
     *      CallErrorPageException
     * @return
     *      결과 URI
     */
    @ExceptionHandler(CallErrorPageException.class)
    public String handleCallErrorPageException(Model model, CallErrorPageException exception) {
        model.addAttribute("message", exception.getMessage());
        return "errors/error";
    }
}
