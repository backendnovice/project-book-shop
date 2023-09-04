/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-17
 * @modified : 2023-09-04
 * @desc     : A global-area exception handler class.
 */

package backendnovice.projectbookshop.global.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Get error page URI with message when call error page exception occurred.
     * @param exception
     *      Call error page exception.
     * @return
     *      Error page URI.
     */
    @ExceptionHandler(CallErrorPageException.class)
    public String handleCallErrorPageException(Model model, CallErrorPageException exception) {
        model.addAttribute("message", exception.getMessage());
        return "errors/error";
    }
}
