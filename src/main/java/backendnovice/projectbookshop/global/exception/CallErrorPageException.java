/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-21
 * @modified : 2023-09-04
 * @desc     : Custom exception class. that can be throwable when error page call is required.
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
