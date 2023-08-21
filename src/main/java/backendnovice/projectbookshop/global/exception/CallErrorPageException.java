/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-22
 * @desc      : Custom exception class. that can be throwable when error page call is required.
 * @changelog :
 * 2023-08-21 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.global.exception;

public class CallErrorPageException extends Exception {
    private String message;
    public CallErrorPageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
