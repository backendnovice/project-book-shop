/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-27
 * @desc     : 레이어 간의 통신을 담당하는 댓글 데이터 전송 객체.
 */

package backendnovice.projectbookshop.board.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;

    private String content;

    private String writer;
}
