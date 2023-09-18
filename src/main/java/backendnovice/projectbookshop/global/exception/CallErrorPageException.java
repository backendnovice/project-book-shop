/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-21
 * @modified : 2023-09-18
 * @desc     : 에러 페이지 호출 예외 클래스.
 */

package backendnovice.projectbookshop.global.exception;

public class CallErrorPageException extends RuntimeException {
    private String message;
    public CallErrorPageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
