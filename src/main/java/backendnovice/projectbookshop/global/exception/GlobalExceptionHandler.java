/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-17
 * @desc      : A global-area exception handler class.
 * @changelog :
 * 2023-08-17 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.global.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Get error page URI with message when uncategorized exception occurred.
     * @param exception
     *      Uncategorized common exception.
     * @return
     *      Error page URI.
     */
    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Model model, Exception exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/error";
    }
}
