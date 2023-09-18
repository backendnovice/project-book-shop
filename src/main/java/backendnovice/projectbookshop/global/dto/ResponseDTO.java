/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-22
 * @modified : 2023-09-18
 * @desc     : 뷰 레이어로 통신을 담당하는 Response 데이터 전송 객체.
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
}
