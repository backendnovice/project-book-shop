/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-22
 * @modified : 2023-09-04
 * @desc     : A common data transfer object class for response.
 */

package backendnovice.projectbookshop.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
