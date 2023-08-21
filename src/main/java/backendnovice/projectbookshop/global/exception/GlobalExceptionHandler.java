/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-22
 * @desc      : A global-area exception handler class.
 * @changelog :
 * 2023-08-17 - backendnovice@gmail.com - create new file.
 * 2023-08-22 - backendnovice@gmail.com - handle CallErrorPageException.
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
