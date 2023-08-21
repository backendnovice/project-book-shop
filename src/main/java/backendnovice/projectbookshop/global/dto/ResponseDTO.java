/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-22
 * @desc      : A common data transfer object class for response.
 * @changelog :
 * 2023-08-22 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class ResponseDTO<T> {
    private final int code;

    private final String message;

    private final T data;

    /*
    public ResponseDTO<?> onSuccess(String message, T data) {
        return ResponseDTO.of(HttpStatus.OK.value(), message, data);
    }

    public ResponseDTO<?> onFailure(String message) {
        return ResponseDTO.of(HttpStatus.BAD_REQUEST.value(), message, null);
    }
    */
}
